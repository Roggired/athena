package ru.yofik.athena.messenger.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import ru.yofik.athena.messenger.infrastructure.security.AthenaAuthenticationFilter;
import ru.yofik.athena.messenger.infrastructure.security.AthenaSecurityAuthenticationProvider;
import ru.yofik.athena.messenger.infrastructure.security.Authorities;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().disable();

        http.addFilterAfter(new AthenaAuthenticationFilter(), ExceptionTranslationFilter.class);
        http.sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.maximumSessions(1));

        http.authorizeRequests()
                .antMatchers("/api/v1/users/**")
                .hasAuthority(Authorities.ACTIVATE_USER.getAuthority())
                .antMatchers("/api/v1/userProfiles/**")
                .hasAuthority(Authorities.FULL_AUTHORITY.getAuthority())
                .antMatchers("/api/v1/chats/**")
                .hasAuthority(Authorities.FULL_AUTHORITY.getAuthority())
                .antMatchers("/ws-api/v1/notifications")
                .hasAuthority(Authorities.FULL_AUTHORITY.getAuthority());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new AthenaSecurityAuthenticationProvider());
    }
}
