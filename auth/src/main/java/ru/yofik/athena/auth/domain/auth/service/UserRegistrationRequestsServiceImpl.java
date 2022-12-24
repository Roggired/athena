package ru.yofik.athena.auth.domain.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.yofik.athena.auth.api.rest.admin.requests.ApproveUserRegistrationRequest;
import ru.yofik.athena.auth.api.rest.admin.requests.FilteredUserRegistrationRequest;
import ru.yofik.athena.auth.api.rest.user.requests.CreateUserRequest;
import ru.yofik.athena.auth.domain.auth.model.UserRegistrationRequest;
import ru.yofik.athena.auth.domain.auth.repository.UserRegistrationRequestRepository;
import ru.yofik.athena.auth.domain.user.model.Role;
import ru.yofik.athena.auth.domain.user.model.User;
import ru.yofik.athena.auth.domain.user.service.UserService;
import ru.yofik.athena.auth.utils.TimeUtils;
import ru.yofik.athena.common.api.exceptions.NotFoundException;
import ru.yofik.athena.common.domain.NewPage;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserRegistrationRequestsServiceImpl implements UserRegistrationRequestService {
    private final UserService userService;
    private final UserRegistrationRequestRepository userRegistrationRequestRepository;

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public User approve(ApproveUserRegistrationRequest request) {
        var userRegistrationRequest = userRegistrationRequestRepository.findById(request.requestId)
                .orElseThrow(NotFoundException::new);

        var createUserRequest = new CreateUserRequest();
        createUserRequest.login = request.login;
        createUserRequest.email = userRegistrationRequest.getEmail();
        createUserRequest.role = Role.USER;
        createUserRequest.withNotification = request.withNotification;

        var user = userService.createUser(createUserRequest);

        userRegistrationRequest.setApproved(true);
        userRegistrationRequest.setApprovedAt(TimeUtils.nowUTC());
        userRegistrationRequest.setCreatedUserId(user.getId());
        userRegistrationRequestRepository.save(userRegistrationRequest);

        return user;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void reject(long requestId) {
        var maybeRequest = userRegistrationRequestRepository.findById(requestId);
        if (maybeRequest.isPresent()) {
            userRegistrationRequestRepository.deleteById(requestId);
        }
    }

    @Override
    public NewPage<UserRegistrationRequest> getPage(NewPage.Meta pageMeta, FilteredUserRegistrationRequest request) {
        var pageRequest = PageRequest.of(
                pageMeta.sequentialNumber,
                pageMeta.size,
                Sort.by(
                        List.of(
                                Sort.Order.asc("isApproved"),
                                Sort.Order.desc("requestedAt")
                        )
                )
        );

        var springPage = userRegistrationRequestRepository.findAllByEmail(
                request.email,
                pageRequest
        );

        return new NewPage<>(
                new NewPage.Meta(
                        List.of("isApproved", "requestedAt"),
                        springPage.getNumber(),
                        springPage.getSize(),
                        springPage.getTotalElements()
                ),
                springPage.getContent()
        );
    }
}
