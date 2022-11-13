package ru.yofik.athena.auth.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.NullSecurityContextRepository;
import ru.yofik.athena.auth.domain.auth.service.AccessService;
import ru.yofik.athena.auth.domain.user.model.Role;

@Configuration
@RequiredArgsConstructor
public class SpringSecurityConfig {
    private final AccessService accessService;

    @Bean
    public SecurityFilterChain httpSecurity(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.addFilterBefore(
                new AthenaAuthenticationFilter(accessService),
                UsernamePasswordAuthenticationFilter.class
        );

        http.securityContext().securityContextRepository(new NullSecurityContextRepository());

        http.authorizeHttpRequests()
                .mvcMatchers("/api/v2/users/**")
                .hasAuthority(Role.ADMIN.name())
                .mvcMatchers("/api/v2/auth/**")
                .permitAll()
                .anyRequest()
                .denyAll();

        return http.build();
    }
}
