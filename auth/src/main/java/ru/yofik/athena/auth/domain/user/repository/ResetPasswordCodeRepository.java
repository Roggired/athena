package ru.yofik.athena.auth.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yofik.athena.auth.domain.user.model.ResetPasswordCode;

import java.util.Optional;

@Repository
public interface ResetPasswordCodeRepository extends JpaRepository<ResetPasswordCode, Long> {
    Optional<ResetPasswordCode> findByAdminId(long adminId);
    Optional<ResetPasswordCode> findByCode(String code);
}
