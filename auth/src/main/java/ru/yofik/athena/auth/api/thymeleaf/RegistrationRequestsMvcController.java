package ru.yofik.athena.auth.api.thymeleaf;

import lombok.RequiredArgsConstructor;
import org.bouncycastle.cert.ocsp.Req;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yofik.athena.auth.api.rest.admin.requests.ApproveUserRegistrationRequest;
import ru.yofik.athena.auth.api.rest.admin.requests.FilteredUserRegistrationRequest;
import ru.yofik.athena.auth.domain.auth.service.UserRegistrationRequestService;
import ru.yofik.athena.common.domain.NewPage;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin-panel")
public class RegistrationRequestsMvcController {
    private final UserRegistrationRequestService userRegistrationRequestService;


    @GetMapping("/registration-requests-table")
    public String getUsersTable(
            Integer pageNumber,
            Model model,
            String email
    ) {
        if (pageNumber == null || pageNumber < 0) pageNumber = 0;
        var pageMeta = new NewPage.Meta(
                List.of("id"),
                pageNumber,
                3,
                0L
        );

        var request = new FilteredUserRegistrationRequest();
        if (email != null) {
            request.email = email.trim();
        }

        var page = userRegistrationRequestService.getPage(pageMeta, request);
        var totalPages = page.getMeta().elementsTotal / page.getMeta().size;
        if (page.getMeta().elementsTotal % page.getMeta().size != 0) totalPages++;

        model.addAttribute("email", email);
        model.addAttribute("requests", page.getContent());
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page.getMeta().sequentialNumber);
        var leftPageNumber = page.getMeta().sequentialNumber - 1;
        if (leftPageNumber < 0) leftPageNumber = 0;
        model.addAttribute("leftPageNumber", leftPageNumber);
        var rightPageNumber = page.getMeta().sequentialNumber + 1;
        if (rightPageNumber >= totalPages) rightPageNumber = (int) totalPages - 1;
        model.addAttribute("rightPageNumber", rightPageNumber);

        return "registration-requests";
    }

    @PostMapping("/actions/approve-registration-request")
    public String actionApproveRegistrationRequest(
            @RequestParam("requestId") long requestId,
            @RequestParam("login") String newUserLogin,
            @RequestParam("withNotification") boolean withNotification
    ) {
        var request = new ApproveUserRegistrationRequest();
        request.requestId = requestId;
        request.login = newUserLogin.trim();
        request.withNotification = withNotification;

        var createdUser = userRegistrationRequestService.approve(request);
        return "redirect:/admin-panel/user-views/view?id=" + createdUser.getId();
    }

    @PostMapping("/actions/reject-registration-request")
    public String actionRejectRegistrationRequest(
            @RequestParam("requestId") long requestId,
            @RequestParam("pageNumber") int currentPageNumber,
            @RequestParam("email") String email
    ) {
        userRegistrationRequestService.reject(requestId);
        return "redirect:/admin-panel/registration-requests-table?pageNumber=" + currentPageNumber + "&email=" + email;
    }
}
