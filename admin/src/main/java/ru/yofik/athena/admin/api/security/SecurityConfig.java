package ru.yofik.athena.admin.api.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.ExceptionTranslationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().disable();

        http.authenticationProvider(new AthenaAuthenticationProvider());
        http.addFilterAfter(new AthenaAuthenticationFilter(), ExceptionTranslationFilter.class);
        http.sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.maximumSessions(1));

        http.authorizeRequests()
                .antMatchers("/athena/logout")
                    .hasAuthority("ADMIN")
                .antMatchers("/admin-panel")
                    .hasAuthority("ADMIN")
                .antMatchers("/admin-panel/**")
                    .hasAuthority("ADMIN");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/css/**.css")
                .antMatchers("/js/**.js")
                .antMatchers("/")
                .antMatchers("/index")
                .antMatchers("/athena/login")
                .antMatchers("/error");
    }
}
