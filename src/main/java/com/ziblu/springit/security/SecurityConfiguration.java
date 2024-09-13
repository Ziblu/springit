package com.ziblu.springit.security;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

    private UserDetailsServiceImpl userDetailsService;

    public SecurityConfiguration(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(EndpointRequest.to("info")).permitAll()
                                .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ACTUATOR")
                                .requestMatchers("/h2-console/**").permitAll()
                                .requestMatchers("/actuator/**").hasRole("ACTUATOR")
                                .requestMatchers("/link/submit").hasRole("USER")
                                .requestMatchers("/link/**").permitAll()
                                .requestMatchers("/").permitAll()
                                .requestMatchers("/h2-console/**").permitAll()
                                .requestMatchers("/css/**", "/js/**", "/images/**", "/libs/**").permitAll()
                                .requestMatchers("/register").permitAll()
                                .requestMatchers("/profile").permitAll()
                                .requestMatchers("/activate/**").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                        .usernameParameter("email")
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
//                        .logoutSuccessHandler(logoutSuccessHandler)
//                        .invalidateHttpSession(true)
//                        .addLogoutHandler(logoutHandler)
//                        .deleteCookies(cookieNamesToClear)
                        .permitAll()
                )
                .rememberMe(rememberMe -> rememberMe
                        .key("uniqueAndSecret")
                        .userDetailsService(userDetailsService)
                )
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
        auth.userDetailsService(userDetailsService);
        return auth.build();
    }
}