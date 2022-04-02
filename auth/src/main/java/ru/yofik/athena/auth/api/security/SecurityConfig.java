package ru.yofik.athena.auth.api.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import ru.yofik.athena.auth.context.client.model.ClientPermission;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().disable();

        http.addFilterAfter(new AthenaBearerAuthenticationFilter(), ExceptionTranslationFilter.class);

        http.authorizeRequests()
                .antMatchers("/api/v1/clients/**")
                .hasAuthority(ClientPermission.AUTHORIZE_USER.name())
                .antMatchers("/api/v1/admin/login")
                .permitAll()
                .antMatchers("/api/v1/admin/**")
                .hasAuthority(ClientPermission.ADMIN.name())
                .antMatchers("/api/v1/teapot")
                .authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new AthenaSecurityAuthenticationProvider());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
//        web.debug(true);
//        web.ignoring()
//                .antMatchers("/api/v1/admin/**");
    }
}
