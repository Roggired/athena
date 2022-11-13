package ru.yofik.athena.auth.domain.user.model;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "lock")
public class Lock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private boolean locked;
    private String reason;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lock)) return false;
        Lock lock = (Lock) o;
        return id == lock.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
