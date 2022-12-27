package ru.yofik.athena.auth.domain.user.model;

import lombok.*;
import ru.yofik.athena.common.utils.TimeUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

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
    private String sessionId;
    private LocalDateTime lastLoginDate;


    public static Session newSession() {
        return new Session(
                0,
                "",
                TimeUtils.infinity()
        );
    }


    public void startSession() {
        this.sessionId = UUID.randomUUID().toString();
        this.lastLoginDate = TimeUtils.nowUTC();
    }

    public void stopSession() {
        this.sessionId = "";
    }
}
