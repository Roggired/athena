package ru.yofik.athena.auth.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.NullSecurityContextRepository;
import ru.yofik.athena.auth.domain.auth.service.AuthService;
import ru.yofik.athena.auth.domain.user.model.Role;

@Configuration
@RequiredArgsConstructor
public class SpringSecurityConfig {
    private final AuthService authService;

    @Bean
    public SecurityFilterChain httpSecurity(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.sessionManagement().sessionFixation().none();
        http.addFilterBefore(
                new AthenaAuthenticationFilter(authService),
                UsernamePasswordAuthenticationFilter.class
        );

        http.securityContext().securityContextRepository(new NullSecurityContextRepository());

        http.authorizeHttpRequests()
                .mvcMatchers("/api/v2/users/**")
                .hasAuthority(Role.ADMIN.name())
                .mvcMatchers("/api/v2/auth/admins/sign-out")
                .hasAuthority(Role.ADMIN.name())
                .mvcMatchers("/api/v2/auth/users/sign-out")
                .hasAuthority(Role.USER.name())
                .mvcMatchers("/api/v2/auth/**")
                .permitAll()
                .mvcMatchers("/api/v2/user-management/**")
                .hasAuthority(Role.ADMIN.name())
                .mvcMatchers("/fonts/**", "/img/**", "/svg/**", "/js/**", "/css/**", "/", "/index", "/index.html", "/favicon.ico")
                .permitAll()
                .mvcMatchers("/auth/**")
                .permitAll()
                .mvcMatchers("/admin-panel/**")
                .hasAuthority(Role.ADMIN.name())
                .anyRequest()
                .denyAll();

        return http.build();
    }
}
