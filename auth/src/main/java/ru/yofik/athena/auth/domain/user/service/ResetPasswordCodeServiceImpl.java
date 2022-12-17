package ru.yofik.athena.auth.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.yofik.athena.auth.domain.auth.service.code.CodeGenerator;
import ru.yofik.athena.auth.domain.user.model.ResetPasswordCode;
import ru.yofik.athena.auth.domain.user.model.Role;
import ru.yofik.athena.auth.domain.user.model.User;
import ru.yofik.athena.auth.domain.user.repository.ResetPasswordCodeRepository;
import ru.yofik.athena.auth.infrastructure.config.properties.AuthProperties;
import ru.yofik.athena.common.api.exceptions.AuthenticationException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResetPasswordCodeServiceImpl implements ResetPasswordCodeService {
    private final ResetPasswordCodeRepository resetPasswordCodeRepository;
    private final CodeGenerator codeGenerator;
    private final AuthProperties authProperties;


    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public ResetPasswordCode getOrCreateForAdmin(User user) {
        if (user.getRole() != Role.ADMIN) {
            throw new IllegalArgumentException("User for obtaining ResetPasswordCode object should be an ADMIN");
        }

        var maybeRestPasswordCode = resetPasswordCodeRepository.findByAdminId(user.getId());
        if (maybeRestPasswordCode.isEmpty()) {
            var resetPasswordCode = ResetPasswordCode.createForAdmin(
                    codeGenerator.generateLong(),
                    authProperties.changePasswordCodeDuration,
                    user
            );
            return resetPasswordCodeRepository.save(resetPasswordCode);
        }

        return maybeRestPasswordCode.get();
    }

    @Override
    public ResetPasswordCode getByCode(String code) {
        return resetPasswordCodeRepository.findByCode(code).orElseThrow(AuthenticationException::new);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void markAsUsed(ResetPasswordCode resetPasswordCode) {
        var maybeResetPasswordCode = resetPasswordCodeRepository.findById(resetPasswordCode.getId());
        if (maybeResetPasswordCode.isPresent()) {
            resetPasswordCodeRepository.delete(resetPasswordCode);
        }
    }
}
