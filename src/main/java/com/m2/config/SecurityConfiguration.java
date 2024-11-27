package com.m2.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;



@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration{
   @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.formLogin((form) -> form
               .loginPage("/html/login")
                .usernameParameter("username")
                .passwordParameter("password")
               .defaultSuccessUrl("/html/home", true)
                .failureUrl("/html/login?loginError=true")
            ).logout(logout -> logout
                        .logoutSuccessUrl("/html/login?logoutSuccess=true")
                        .deleteCookies("JSESSIONID"));
        httpSecurity
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**")) // Désactive CSRF pour la console H2
                .authorizeHttpRequests(
                (requests) -> {
                    requests.anyRequest().permitAll()
//                            .requestMatchers("/html/login", "/html/register", "/html/home", "/html/save").permitAll()
//                            .requestMatchers("/webjars/**", "/h2-console/**", "/css/**").permitAll()
//                            .requestMatchers("/html/**").authenticated()
                    ;
                }
                )
                .headers((headers) -> headers.frameOptions(Customizer.withDefaults()).disable());

       ;



        return httpSecurity.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
