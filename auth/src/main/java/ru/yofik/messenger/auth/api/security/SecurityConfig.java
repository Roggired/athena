package ru.yofik.messenger.auth.api.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import ru.yofik.messenger.auth.context.client.model.ClientPermission;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().disable();

        http.addFilterAfter(new YofikBearerAuthenticationFilter(), ExceptionTranslationFilter.class);

        http.authorizeRequests()
                .antMatchers("/api/v1/clients/**")
                .hasAuthority(ClientPermission.AUTHORIZE_USER.name());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new YofikSecurityAuthenticationProvider());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // TODO admin auth
        web.ignoring()
                .antMatchers("/api/v1/admin/**");
    }
}
