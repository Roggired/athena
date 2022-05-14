package ru.yofik.athena.messenger.domain.user.service;

import org.springframework.stereotype.Service;
import ru.yofik.athena.common.Page;
import ru.yofik.athena.messenger.api.http.user.request.UpdateUserRequest;
import ru.yofik.athena.messenger.domain.user.model.User;
import ru.yofik.athena.messenger.domain.user.repository.UserRepository;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public Page<User> getPage(Page.Meta pageMeta) {
        return userRepository.getPage(pageMeta);
    }

    @Override
    public User getUser(long id) {
        return userRepository.getUser(id);
    }

    @Override
    public User updateUser(UpdateUserRequest request) {
        var currentUser = userRepository.getCurrentUser();
        currentUser.setName(request.name);
        currentUser.setLogin(request.login);

        return userRepository.updateUser(
                currentUser
        );
    }

    @Override
    public User getCurrentUser() {
        return userRepository.getCurrentUser();
    }
}
