package ru.yofik.athena.auth.service.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.yofik.athena.auth.domain.auth.service.InvitationService;
import ru.yofik.athena.common.api.exceptions.InvalidDataException;
import ru.yofik.athena.common.api.exceptions.NotFoundException;
import ru.yofik.athena.common.api.exceptions.UniquenessViolationException;
import ru.yofik.athena.auth.api.rest.user.requests.CreateUserRequest;
import ru.yofik.athena.auth.api.rest.user.requests.FilteredUsersRequest;
import ru.yofik.athena.auth.api.rest.user.requests.UpdateUserRequest;
import ru.yofik.athena.auth.domain.user.model.Role;
import ru.yofik.athena.auth.domain.user.repository.UserRepository;
import ru.yofik.athena.auth.domain.user.service.UserService;
import ru.yofik.athena.auth.domain.user.service.UserServiceImpl;
import ru.yofik.athena.common.utils.TimeUtils;
import ru.yofik.athena.common.domain.NewPage;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class UserServiceTest {
    private UserService userService;
    private UserRepository userRepositoryMock;

    @BeforeEach
    public void setup() {
        userRepositoryMock = UserRepositoryMockFactory.create();
        userService = new UserServiceImpl(userRepositoryMock, Mockito.mock(InvitationService.class));
    }

    @Test
    public void testMock() {
        var user = userRepositoryMock.findByLogin("qwerty");
        Assertions.assertTrue(user.isPresent());
        Assertions.assertEquals(1L, user.get().getId());
        Assertions.assertEquals("qwerty", user.get().getLogin());
    }

    @Test
    public void getUserReturnsCorrectly() {
        var user = userService.getUserById(1L);
        Assertions.assertEquals(1L, user.getId(), "Wrong id");
        Assertions.assertEquals("qwerty", user.getLogin(), "Wrong login");
    }

    @Test
    public void getUserThrowsOnWrongId() {
        Assertions.assertThrows(
                NotFoundException.class,
                () -> userService.getUserById(10L),
                "UserServiceImpl must throws on getUser with not existed id"
        );
    }

    @Test
    public void createsUserCorrectly() {
        var request = new CreateUserRequest();
        request.login = "12345";
        request.role = Role.USER;
        request.password = null;

        var user = userService.createUser(request);
        Assertions.assertEquals(10L, user.getId(), "Wrong id");
        Assertions.assertEquals(request.login, user.getLogin(), "Wrong login");
        Assertions.assertEquals(request.role, user.getRole(), "Wrong role");
        Assertions.assertTrue(user.getCredentialsExpirationDate().isAfter(TimeUtils.nowUTC()), "Credentials expiration date for user must be at least after now(UTC)");
        Assertions.assertFalse(user.isLocked(), "Created user account must not be locked");
        Assertions.assertTrue(user.getLockReason().isEmpty(), "Created user account must have empty lock reason");
        Assertions.assertFalse(user.isCredentialsExpired(), "Credentials expiration date for created user must not be expired");
        Assertions.assertEquals(TimeUtils.infinity(), user.getLastLoginDate(), "Last login date for created user must be equal to TimeUtils.infinity()");
    }

    @Test
    public void createsThrowsOnExistedLogin() {
        var request = new CreateUserRequest();
        request.login = "qwerty";
        request.role = Role.USER;
        request.password = null;

        Assertions.assertThrows(
                UniquenessViolationException.class,
                () -> userService.createUser(request),
                "UserService must throw UniquenessViolationException on try to create user with non-unique login"
        );
    }

    @Test
    public void createsThrowsOnNoPasswordForUser() {
        var request = new CreateUserRequest();
        request.login = "12345";
        request.role = Role.ADMIN;
        request.password = null;

        Assertions.assertThrows(
                InvalidDataException.class,
                () -> userService.createUser(request),
                "UserService must throw on try to create admin with null password"
        );

        var request1 = new CreateUserRequest();
        request1.login = "12345";
        request1.role = Role.ADMIN;
        request1.password = "";

        Assertions.assertThrows(
                InvalidDataException.class,
                () -> userService.createUser(request),
                "UserService must throw on try to create admin with empty password"
        );

        var request2 = new CreateUserRequest();
        request2.login = "12345";
        request2.role = Role.USER;
        request2.password = "qwerqwerwqerqwer";

        Assertions.assertThrows(
                InvalidDataException.class,
                () -> userService.createUser(request),
                "UserService must throw on try to create user with non-empty password"
        );
    }

    @Test
    public void updateReturnsCorrectly() {
        var request = new UpdateUserRequest();
        request.login = "newLogin";

        var user = userService.updateUser(1L, request);

        Assertions.assertEquals(request.login, user.getLogin(), "Wrong login");
    }

    @Test
    public void updateThrowsOnExistedLoginOnOtherUser() {
        var request = new UpdateUserRequest();
        request.login = "qwerty";

        Assertions.assertDoesNotThrow(
                () -> userService.updateUser(1L, request),
                "When try to set the same login for user, update() of UserService must not throw"
        );

        var request1 = new UpdateUserRequest();
        request1.login = "adminadmin";

        Assertions.assertThrows(
                UniquenessViolationException.class,
                () -> userService.updateUser(1L, request1),
                "When try to set login of another user to a user, update() of UserService must throw UniquenessViolationException"
        );
    }

    @Test
    public void updateThrowsOnInvalidId() {
        var request = new UpdateUserRequest();
        request.login = "qwerty";

        Assertions.assertThrows(
                NotFoundException.class,
                () -> userService.updateUser(10L, request),
                "When try to update not exist user, update() of UserService must throws"
        );
    }

    @Test
    public void deleteUserInvokesCorrectly() {
        userService.deleteUser(1L);

        verify(userRepositoryMock, times(1)).findById(1L);
        verify(userRepositoryMock, times(1)).deleteById(1L);
    }

    @Test
    public void getUsersPageableReturnsCorrectlyOnLoginFilter() {
        var pageMeta = new NewPage.Meta(
                List.of(),
                0,
                2,
                0L
        );

        var filters = new FilteredUsersRequest();
        filters.login = "q";

        var result = userService.getUsersPageable(pageMeta, filters);
        Assertions.assertEquals(0, result.getMeta().sequentialNumber, "Wrong sequential number");
        Assertions.assertEquals(2, result.getMeta().size, "Wrong size");
        Assertions.assertEquals(3, result.getMeta().elementsTotal, "Wrong elements total");
        Assertions.assertEquals(1, result.getContent().size(), "Invalid page content");
        Assertions.assertEquals(1L, result.getContent().get(0).getId(), "Invalid page content");
        Assertions.assertEquals("qwerty", result.getContent().get(0).getLogin(), "Invalid page content");
    }

    @Test
    public void getUsersPageableReturnsCorrectlyOnLoginAndRoleFilter() {
        var pageMeta = new NewPage.Meta(
                List.of(),
                0,
                2,
                0L
        );

        var filters = new FilteredUsersRequest();
        filters.login = "a";
        filters.role = Role.USER;

        var result = userService.getUsersPageable(pageMeta, filters);
        Assertions.assertEquals(0, result.getMeta().sequentialNumber, "Wrong sequential number");
        Assertions.assertEquals(2, result.getMeta().size, "Wrong size");
        Assertions.assertEquals(3, result.getMeta().elementsTotal, "Wrong elements total");
        Assertions.assertEquals(1, result.getContent().size(), "Invalid page content");
        Assertions.assertEquals(3L, result.getContent().get(0).getId(), "Invalid page content");
        Assertions.assertEquals("abcde", result.getContent().get(0).getLogin(), "Invalid page content");
    }
}
