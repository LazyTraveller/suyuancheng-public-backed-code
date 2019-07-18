package com.suyuancheng.backend.spring.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

/**
 * @author hsj
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig(private val userDetailsService: UserDetailsService) :
    WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http
            .authorizeRequests()
            .antMatchers(HttpMethod.GET, "/api/rest/trading/**").permitAll()
            .antMatchers(
                HttpMethod.GET,
                "/index*",
                "/static/**",
                "/*.js",
                "/*.json",
                "/*.ico",
                "/*.jpg",
                "/*.css"
            )
            .permitAll()
            .antMatchers(HttpMethod.GET, "/api/rest/trading-item/**").permitAll()
            .antMatchers(HttpMethod.GET, "/api/rest/trading-info/**").permitAll()
            .antMatchers(HttpMethod.POST, "/api/rest/trading-info/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage("/login")
            .loginProcessingUrl("/user/login")
            .successHandler { _, _, _ ->
            }
            .failureHandler { _, response, _ ->
                response.status = HttpStatus.UNAUTHORIZED.value()
            }
            .permitAll()
            .and()
            .logout().logoutUrl("/user/logout")
            .logoutSuccessHandler { _, _, _ ->
            }
            .and().userDetailsService(userDetailsService).exceptionHandling()
            .defaultAuthenticationEntryPointFor(
                HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                AntPathRequestMatcher("/api/**")
            ).defaultAuthenticationEntryPointFor(
                HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                AntPathRequestMatcher("/info")
            )
            .and().csrf().disable()
    }

}