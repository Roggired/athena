package ru.yofik.athena.auth.domain.user.model;

import lombok.*;
import ru.yofik.athena.common.utils.TimeUtils;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "reset_password_codes")
public class ResetPasswordCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String code;
    private LocalDateTime expirationDate;
    private long adminId;


    public static ResetPasswordCode createForAdmin(
            String code,
            Duration ttl,
            User user
    ) {
        return new ResetPasswordCode(
                0,
                code,
                TimeUtils.nowUTC().plusSeconds(ttl.toSeconds()),
                user.getId()
        );
    }
}
