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

//@EnableMethodSecurity(securedEnabled = true)
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

    private UserDetailsServiceImpl userDetailsService;
//    private final LogoutSuccessHandler logoutSuccessHandler;
//    private final LogoutHandler logoutHandler;
//    private final String[] cookieNamesToClear = {"JSESSIONID"};

//    public SecurityConfiguration(UserDetailsServiceImpl userDetailsService, LogoutSuccessHandler logoutSuccessHandler, LogoutHandler logoutHandler) {
//        this.userDetailsService = userDetailsService;
//        this.logoutSuccessHandler = logoutSuccessHandler;
//        this.logoutHandler = logoutHandler;
//    }

    public SecurityConfiguration(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//      http
//            .authorizeRequests()
//            .requestMatchers(EndpointRequest.to("info")).permitAll()
//            .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ACTUATOR")
//            .antMatchers("/actuator/").hasRole("ACTUATOR")
//            .antMatchers("/link/submit").hasRole("USER")
//            .antMatchers("/link/**").permitAll()
//            .antMatchers("/").permitAll()
//            .antMatchers("/h2-console/**").permitAll()
//            .and()
//            .formLogin()
//            .loginPage(/login)
//            .permitAll()
//            .and()
//            .logout()
//              .logoutUrl("/my/logout")
//              .logoutSuccessUrl("/my/index")
//              .logoutSuccessHandler(logoutSuccessHandler)
//              .invalidateHttpSession(true)
//              .addLogoutHandler(logoutHandler)
//              .deleteCookies(cookieNamesToClear)
//            .and()
//            .csrf().disable()
//            .headers().frameOptions().disable();
//     }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(EndpointRequest.to("info")).permitAll()
                                .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ACTUATOR")
                                .requestMatchers("/actuator/**").hasRole("ACTUATOR")
                                .requestMatchers("/link/submit").hasRole("USER")
                                .requestMatchers("/link/**").permitAll()
                                .requestMatchers("/").permitAll()
                                .requestMatchers("/h2-console/**").permitAll()
                                .requestMatchers("/css/**", "/js/**", "/images/**", "/libs/**").permitAll()
                                .requestMatchers("/register").permitAll()
                                .requestMatchers("/profile").permitAll()
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
                );
//                .csrf(csrf -> csrf.disable())
//                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));
        return http.build();
    }



    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
        auth.userDetailsService(userDetailsService);
        return auth.build();
    }

}
