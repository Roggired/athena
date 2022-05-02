package ru.yofik.athena.auth.context.user.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yofik.athena.auth.api.exception.InvalidDataException;
import ru.yofik.athena.auth.api.exception.ResourceAlreadyExistsException;
import ru.yofik.athena.auth.api.exception.ResourceNotFoundException;
import ru.yofik.athena.auth.api.exception.UnauthenticatedException;
import ru.yofik.athena.auth.context.user.api.request.ActivateUserRequest;
import ru.yofik.athena.auth.context.user.api.request.AuthorizeUserRequest;
import ru.yofik.athena.auth.context.user.api.request.CreateInvitationRequest;
import ru.yofik.athena.auth.context.user.api.request.CreateUserRequest;
import ru.yofik.athena.auth.context.user.factory.InvitationFactory;
import ru.yofik.athena.auth.context.user.factory.LockFactory;
import ru.yofik.athena.auth.context.user.factory.UserFactory;
import ru.yofik.athena.auth.context.user.model.LockReason;
import ru.yofik.athena.auth.context.user.model.User;
import ru.yofik.athena.auth.context.user.repository.InvitationRepository;
import ru.yofik.athena.auth.context.user.repository.LockRepository;
import ru.yofik.athena.auth.context.user.repository.UserRepository;
import ru.yofik.athena.auth.context.user.view.*;
import ru.yofik.athena.auth.infrastructure.security.InvalidTokenException;
import ru.yofik.athena.auth.infrastructure.security.Token;
import ru.yofik.athena.auth.infrastructure.security.TokenGenerator;
import ru.yofik.athena.auth.infrastructure.security.TokenVerifier;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final InvitationRepository invitationRepository;
    private final LockRepository lockRepository;
    private final UserFactory userFactory;
    private final LockFactory lockFactory;
    private final InvitationFactory invitationFactory;
    private final TokenGenerator<User> tokenGenerator;
    private final TokenVerifier<User> tokenVerifier;


    public UserServiceImpl(UserRepository userRepository,
                           InvitationRepository invitationRepository,
                           LockRepository lockRepository,
                           UserFactory userFactory,
                           LockFactory lockFactory,
                           InvitationFactory invitationFactory,
                           TokenGenerator<User> tokenGenerator,
                           TokenVerifier<User> tokenVerifier) {
        this.userRepository = userRepository;
        this.invitationRepository = invitationRepository;
        this.lockRepository = lockRepository;
        this.userFactory = userFactory;
        this.lockFactory = lockFactory;
        this.invitationFactory = invitationFactory;
        this.tokenGenerator = tokenGenerator;
        this.tokenVerifier = tokenVerifier;
    }


    @Override
    public AccessTokenView activate(ActivateUserRequest request, String deviceId) {
        var invitation = invitationRepository.findById(request.code);
        if (invitation.isEmpty() || invitation.get().getCount() <= 0) {
            log.warn(() -> "No such invitation: " + (invitation.isPresent() ? invitation.get() : "empty"));
            throw new InvalidDataException("Invalid invitation");
        }

        var user = getUserById(invitation.get().getUserId());
        user.setAllowedDeviceId(deviceId);
        user.setActivated(true);
        user.getInvitation().touch();
        save(user);

        return new AccessTokenView(new String(tokenGenerator.generateToken(user).getData()));
    }

    @Override
    public void create(CreateUserRequest request) {
        var existedUser = userRepository.findByLogin(request.login);
        if (existedUser.isPresent()) {
            log.warn(() -> "User with login: " + request.login + " already exists");
            throw new ResourceAlreadyExistsException("User with login: " + request.login + " already exists");
        }

        var user = userFactory.createNew(request.name, request.login);
        save(user);
    }

    @Override
    public UserView get(long id) {
        return getUserById(id).toView();
    }

    @Override
    public List<UserShortView> getAll() {
        return userRepository.findAll()
                .stream()
                .map(userFactory::from)
                .map(User::toShortView)
                .collect(Collectors.toList());
    }

    @Override
    public void lock(long id, LockReason lockReason) {
        var user = getUserById(id);
        lock(user, lockReason);
    }

    @Override
    @Transactional
    public void unlock(long id) {
        var user = getUserById(id);
        var lock = user.getLock();

        if (lock == null) {
            return;
        }

        lockRepository.deleteById(lock.getId());
        user.unlock();
        save(user);
    }

    private void lock(User user, LockReason lockReason) {
        var lock = lockFactory.create(lockReason);
        user.lock(lock);
        save(user);
    }

    @Override
    public InvitationView createInvitation(CreateInvitationRequest request) {
        var user = getUserById(request.userId);
        var invitationOptional = invitationRepository.findByUserId(user.getId());
        invitationOptional.ifPresent(invitationRepository::delete);

        var invitation = invitationFactory.create(request.count, user);
        user.setInvitation(invitation);
        save(user);

        return invitation.toView();
    }

    @Override
    public void delete(long id) {
        var user = getUserById(id);
        var userJpaDto = userFactory.toUserJpaDto(user);
        userRepository.delete(userJpaDto);
    }

    @Override
    public ClientUserView authorize(AuthorizeUserRequest request, String deviceId) {
        User tokenUser;
        User dbUser;
        try {
            tokenUser = tokenVerifier.verify(new Token(request.accessToken.toCharArray(), Token.Type.JWE), User.class);
            dbUser = getUserById(tokenUser.getId());

            if (!dbUser.getAllowedDeviceId().equals(deviceId)) {
                log.warn(() -> "Someone stole user " + dbUser.getLogin() + " access token!");
                lock(dbUser, LockReason.INVALID_DEVICE_ID);
                throw new UnauthenticatedException();
            }

            if (!dbUser.isActivated()) {
                log.warn(() -> "User: " + dbUser.getLogin() + " is deactivated");
                throw new UnauthenticatedException();
            }
        } catch (InvalidTokenException e) {
            log.warn(() -> "Invalid user token");
            throw new UnauthenticatedException();
        }

        return dbUser.toClientView();
    }

    private User getUserById(long id) {
        var userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            log.warn(() -> "User with id: " + id + " doesn't exist");
            throw new ResourceNotFoundException();
        }

        var user = userOptional.get();
        return userFactory.from(user);
    }

    private void save(User user) {
        var userJpaDto = userFactory.toUserJpaDto(user);
        userRepository.save(userJpaDto);

        if (user.hasInvitation()) {
            var invitationRedisDto = invitationFactory.toRedisDto(user.getInvitation());
            invitationRepository.save(invitationRedisDto);
        }
    }
}
