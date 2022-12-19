package ru.yofik.athena.messenger.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import ru.yofik.athena.common.security.AccessTokenAuthenticationOAuth2Converter;
import ru.yofik.athena.messenger.infrastructure.security.Authorities;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain http(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().disable();
        http.oauth2ResourceServer().jwt().jwtAuthenticationConverter(new AccessTokenAuthenticationOAuth2Converter());

        http.authorizeRequests()
//                .antMatchers("/api/v1/users/**")
//                .hasAuthority(Authorities.ACTIVATE_USER.getAuthority())
                .antMatchers("/api/v1/userProfiles/**")
                .hasAuthority("USER")
                .antMatchers("/api/v1/chats/**")
                .hasAuthority("USER")
                .antMatchers("/ws-api/v1/notifications")
                .hasAuthority("USER");

        return http.build();
    }
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable().cors().disable();
//
//        http.addFilterAfter(new AthenaAuthenticationFilter(), ExceptionTranslationFilter.class);
//        http.sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.maximumSessions(1));

//        http.oauth2ResourceServer().jwt().jwtAuthenticationConverter(new AccessTokenAuthenticationOAuth2Converter());
//
//        http.authorizeRequests()
//                .antMatchers("/api/v1/users/**")
//                .hasAuthority(Authorities.ACTIVATE_USER.getAuthority())
//                .antMatchers("/api/v1/userProfiles/**")
//                .hasAuthority(Authorities.FULL_AUTHORITY.getAuthority())
//                .antMatchers("/api/v1/chats/**")
//                .hasAuthority(Authorities.FULL_AUTHORITY.getAuthority())
//                .antMatchers("/ws-api/v1/notifications")
//                .hasAuthority(Authorities.FULL_AUTHORITY.getAuthority());
//    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(new AthenaSecurityAuthenticationProvider());
//    }
}
