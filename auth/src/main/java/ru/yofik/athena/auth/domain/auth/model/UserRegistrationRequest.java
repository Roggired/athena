package ru.yofik.athena.auth.domain.auth.model;

import lombok.*;
import ru.yofik.athena.auth.utils.TimeUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "user_registration_requests")
public class UserRegistrationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String email;
    private LocalDateTime requestedAt;
    private boolean isApproved;
    private LocalDateTime approvedAt;
    private Long createdUserId;

    public static UserRegistrationRequest newRequest(String email) {
        return new UserRegistrationRequest(
                0L,
                email,
                TimeUtils.nowUTC(),
                false,
                TimeUtils.infinity(),
                null
        );
    }
}
