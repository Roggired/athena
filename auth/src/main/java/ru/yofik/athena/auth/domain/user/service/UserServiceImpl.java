package ru.yofik.athena.auth.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.yofik.athena.auth.api.rest.user.requests.CreateUserRequest;
import ru.yofik.athena.auth.api.rest.user.requests.FilteredUsersRequest;
import ru.yofik.athena.auth.api.rest.user.requests.UpdateUserRequest;
import ru.yofik.athena.auth.domain.auth.service.InvitationService;
import ru.yofik.athena.auth.domain.auth.service.code.CodeGenerator;
import ru.yofik.athena.auth.domain.auth.service.mail.MailService;
import ru.yofik.athena.auth.domain.user.model.Role;
import ru.yofik.athena.auth.domain.user.model.User;
import ru.yofik.athena.auth.domain.user.repository.UserRepository;
import ru.yofik.athena.common.api.exceptions.InvalidDataException;
import ru.yofik.athena.common.api.exceptions.NotFoundException;
import ru.yofik.athena.common.api.exceptions.UniquenessViolationException;
import ru.yofik.athena.common.domain.NewPage;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final InvitationService invitationService;

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public User createUser(CreateUserRequest request) {
        if (request.role == Role.ADMIN && request.password == null) {
            throw new InvalidDataException("Password must be specified for ADMIN");
        }

        if (request.role == Role.USER && request.password != null) {
            throw new InvalidDataException("Password cannot be specified for USER");
        }

        if (request.role == Role.ADMIN && request.withNotification != null) {
            throw new InvalidDataException("With notification flag cannot be specified for ADMIN");
        }

        if (userRepository.findByLogin(request.login).isPresent()) {
            throw new UniquenessViolationException("Login must be unique");
        }

        User user;
        if (request.role == Role.ADMIN) {
            user = User.newAdmin(
                    request.email,
                    request.login,
                    request.password.trim(),
                    true
            );
        } else {
            user = User.newUser(
                    request.email,
                    request.login,
                    "no-invitation"
            );

            user = userRepository.save(user);
            if (request.withNotification != null && request.withNotification) {
                invitationService.inviteUser(user);
            } else {
                invitationService.inviteUserWithoutEmailNotification(user);
            }
        }

        return userRepository.save(user);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public User updateUser(long id, UpdateUserRequest request) {
        var user = userRepository.findById(id).orElseThrow(NotFoundException::new);

        var userByLogin = userRepository.findByLogin(request.login);
        if (userByLogin.isPresent() && id != userByLogin.get().getId()) {
            throw new UniquenessViolationException();
        }
        var userByEmail = userRepository.findByEmail(request.email);
        if (userByEmail.isPresent() && id != userByEmail.get().getId()) {
            throw new UniquenessViolationException();
        }

        user.setLogin(request.login);
        user.setEmail(request.email);
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        userRepository.findById(user.getId()).orElseThrow(NotFoundException::new);

        var userByLogin = userRepository.findByLogin(user.getLogin());
        if (userByLogin.isPresent() && userByLogin.get().getId() != user.getId()) {
            throw new UniquenessViolationException();
        }
        var userByEmail = userRepository.findByEmail(user.getEmail());
        if (userByEmail.isPresent() && userByEmail.get().getId() != user.getId()) {
            throw new UniquenessViolationException();
        }

        return userRepository.save(user);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void deleteUser(long id) {
        userRepository.findById(id).orElseThrow(NotFoundException::new);
        userRepository.deleteById(id);
    }

    @Override
    public User getUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public User getUserByLogin(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public List<User> getAdmins() {
        return userRepository.findAllAdmins();
    }

    @Override
    public NewPage<User> getUsersPageable(NewPage.Meta pageMeta, FilteredUsersRequest request) {
        var springPage = userRepository.findAllByFilters(
                request.login,
                request.role == null ? null : request.role.toString(),
                PageRequest.of(
                        pageMeta.sequentialNumber,
                        pageMeta.size,
                        Sort.by("id")
                )
        );

        pageMeta.elementsTotal = springPage.getTotalElements();
        return new NewPage<>(
                pageMeta,
                springPage.getContent()
        );
    }
}
