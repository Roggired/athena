package ru.yofik.athena.messenger.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.NullSecurityContextRepository;
import ru.yofik.athena.common.security.AccessTokenAuthenticationOAuth2Converter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain http(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().disable();
        http.oauth2ResourceServer().jwt().jwtAuthenticationConverter(new AccessTokenAuthenticationOAuth2Converter());
        http.securityContext().securityContextRepository(new NullSecurityContextRepository());

        http.authorizeRequests()
                .antMatchers("/api/v1/userProfiles/**")
                .hasAuthority("USER")
                .antMatchers("/api/v1/chats/**")
                .hasAuthority("USER")
                .antMatchers("/ws-api/v1/notifications")
                .hasAuthority("USER")
                .antMatchers("/actuator/**")
                .permitAll();

        return http.build();
    }
}
