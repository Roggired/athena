package ru.yofik.athena.admin.context.user.service;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import ru.yofik.athena.admin.context.client.model.AdminKeyStorage;
import ru.yofik.athena.admin.context.user.factory.UserFactory;
import ru.yofik.athena.admin.context.user.integration.UserApi;
import ru.yofik.athena.admin.context.user.model.Invitation;
import ru.yofik.athena.admin.context.user.model.User;
import ru.yofik.athena.admin.context.user.model.UserInfo;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequestScope
public class UserServiceImpl implements UserService {
    private final UserApi userApi;
    private final UserFactory userFactory;
    private final AdminKeyStorage adminKeyStorage;

    public UserServiceImpl(UserApi userApi, UserFactory userFactory, AdminKeyStorage adminKeyStorage) {
        this.userApi = userApi;
        this.userFactory = userFactory;
        this.adminKeyStorage = adminKeyStorage;
    }

    @Override
    public void createUser(String name, String login) {
        var user = userFactory.of(name, login);
        userApi.createUser(user, getToken());
    }

    @Override
    public String importUsers(InputStream inputStream) {
        try {
            var csvParser = CSVParser.parse(
                    inputStream,
                    StandardCharsets.UTF_8,
                    CSVFormat.DEFAULT
            );
            var users = csvParser.stream()
                    .map(record -> userFactory.of(
                            record.get(0),
                            record.get(1)
                    ))
                    .collect(Collectors.toList());
            users = users.stream()
                    .map(user -> userApi.createUser(user, getToken()))
                    .collect(Collectors.toList());

            return activateUsersAndGetJsonList(users);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @AllArgsConstructor
    private static class Pair {
        public final String login;
        public final String name;
        public final String invitation;
    }
    @AllArgsConstructor
    private static class PairWrapper {
        public final List<Pair> users;
    }

    private String activateUsersAndGetJsonList(List<User> users) {
        var pairs = users.stream()
                .map(user -> new Pair(
                        user.getLogin(),
                        user.getName(),
                        userApi.createNewInvitation(user.getId(), getToken()).getCode()
                ))
                .collect(Collectors.toList());

        return new Gson().toJson(new PairWrapper(pairs));
    }

    @Override
    public void deleteUser(long id) {
        userApi.deleteUser(id, getToken());
    }

    @Override
    public void lockUser(long id) {
        userApi.lockUser(id, getToken());
    }

    @Override
    public void unlockUser(long id) {
        userApi.unlockUser(id, getToken());
    }

    @Override
    public User getUser(long id) {
        return getUserImpl(id, getToken());
    }

    @Override
    public List<UserInfo> getAllUsers() {
        return userApi.findAll(getToken())
                .stream()
                .sorted((a, b) -> (int)(a.getId() - b.getId()))
                .peek(a -> a.setCreatedAt(formatDate(a.getCreatedAt())))
                .collect(Collectors.toList());
    }

    @Override
    public Invitation createNewInvitation(long id) {
        return userApi.createNewInvitation(id, getToken());
    }

    private User getUserImpl(long id, char[] token) {
        var user = userApi.findById(id, token);
        if (user.isEmpty()) {
            log.warn(() -> "User with id: " + id + " doesn't exist");
            throw new RuntimeException("User with id: " + id + " doesn't exist");
        }

        var result = user.get();
        result.setCreatedAt(formatDate(result.getCreatedAt()));

        if (result.getLock() != null) {
            result.getLock().setDate(formatDate(result.getLock().getDate()));
        }

        return result;
    }

    private char[] getToken() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return new char[0];
        } else {
            return adminKeyStorage.get((String) authentication.getCredentials());
        }
    }

    private String formatDate(String date) {
        var zonedDateTime = ZonedDateTime.parse(date);
        var localDateTime = zonedDateTime.toLocalDateTime();
        var dateTimeFormatter = new DateTimeFormatterBuilder().appendPattern("HH:mm dd.MM.yyyy").toFormatter();
        return localDateTime.format(dateTimeFormatter);
    }
}
