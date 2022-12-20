package ru.yofik.athena.messenger.domain.user.service;

import org.springframework.stereotype.Service;
import ru.yofik.athena.common.domain.NewPage;
import ru.yofik.athena.common.domain.Page;
import ru.yofik.athena.messenger.api.http.user.request.UpdateUserRequest;
import ru.yofik.athena.messenger.domain.notification.service.NotificationService;
import ru.yofik.athena.messenger.domain.user.model.User;
import ru.yofik.athena.messenger.domain.user.repository.UserRepository;
import ru.yofik.athena.messenger.infrastructure.integration.auth.FilteredUsersRequest;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public UserServiceImpl(UserRepository userRepository, NotificationService notificationService) {
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    @Override
    public NewPage<User> getPage(NewPage.Meta pageMeta) {
        var currentUser = userRepository.getCurrentUser();
        var page = userRepository.getPage(pageMeta, new FilteredUsersRequest(null, null))
                .map(user -> {
                    user.setOnline(notificationService.isUserActive(user.getId()));
                    return user;
                });

        if (page.getContent().contains(currentUser)) {
           return replaceCurrentUser(page, pageMeta, currentUser);
        }

        return page;
    }

    private NewPage<User> replaceCurrentUser(NewPage<User> page, NewPage.Meta pageMeta, User currentUser) {
        var nextPageMeta = new NewPage.Meta(
                pageMeta.sequentialNumber + 1,
                pageMeta.size
        );
        var nextPage = userRepository.getPage(nextPageMeta, new FilteredUsersRequest(null, null))
                .map(user -> {
                    user.setOnline(notificationService.isUserActive(user.getId()));
                    return user;
                });

        if (nextPage.getContent().isEmpty()) {
            page.getContent().remove(currentUser);
            return page;
        }

        page.getContent().replaceAll(user -> {
            if (currentUser.getId() == user.getId()) {
                return nextPage.getContent().get(0);
            }
            return user;
        });

        return page;
    }

    @Override
    public User getById(long id) {
        var user = userRepository.getUser(id);
        user.setOnline(notificationService.isUserActive(id));
        return user;
    }

    @Override
    public User updateUser(UpdateUserRequest request) {
        var currentUser = userRepository.getCurrentUser();
        currentUser.setEmail(request.email);
        currentUser.setLogin(request.login);

        return userRepository.updateUser(
                currentUser
        );
    }

    @Override
    public User getCurrentUser() {
        var user = userRepository.getCurrentUser();
        user.setOnline(true);
        return user;
    }
}
