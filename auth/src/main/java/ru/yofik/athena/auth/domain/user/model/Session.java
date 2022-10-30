package ru.yofik.athena.auth.domain.user.model;

import lombok.*;
import ru.yofik.athena.auth.utils.TimeUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "session")
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String allowedDeviceId;
    @Column
    private LocalDateTime lastLoginDate;


    public static Session newSession(String allowedDeviceId) {
        return new Session(
                0,
                allowedDeviceId,
                TimeUtils.infinity()
        );
    }
}
