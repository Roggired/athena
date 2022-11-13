package ru.yofik.athena.auth.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.yofik.athena.auth.api.user.requests.CreateUserRequest;
import ru.yofik.athena.auth.api.user.requests.FilteredUsersRequest;
import ru.yofik.athena.auth.api.user.requests.UpdateUserRequest;
import ru.yofik.athena.auth.domain.user.model.Role;
import ru.yofik.athena.auth.domain.user.model.User;
import ru.yofik.athena.auth.domain.user.repository.UserRepository;
import ru.yofik.athena.common.api.exceptions.InvalidDataException;
import ru.yofik.athena.common.api.exceptions.NotFoundException;
import ru.yofik.athena.common.api.exceptions.UniquenessViolationException;
import ru.yofik.athena.common.domain.NewPage;

import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public User createUser(CreateUserRequest request) {
        if (request.role == Role.ADMIN && request.password == null) {
            throw new InvalidDataException("Password must be specified for ADMIN");
        }

        if (request.role == Role.USER && request.password != null) {
            throw new InvalidDataException("Password cannot be specified for USER");
        }

        if (userRepository.findByLogin(request.login).isPresent()) {
            throw new UniquenessViolationException("Login must be unique");
        }

        User user;
        if (request.role == Role.ADMIN) {
            user = User.newAdmin(
                    request.login,
                    request.password.trim()
            );
        } else {
            user = User.newUser(
                    request.login,
                    UUID.randomUUID().toString()
            );
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

        user.setLogin(request.login);
        return userRepository.save(user);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void deleteUser(long id) {
        userRepository.findById(id).orElseThrow(NotFoundException::new);
        userRepository.deleteById(id);
    }

    @Override
    public User getUser(long id) {
        return userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public User getUser(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(NotFoundException::new);
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
