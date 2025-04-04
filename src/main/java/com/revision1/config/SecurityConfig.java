package com.revision1.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
public class SecurityConfig {
    private JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //H(CD)2
        http.csrf().disable().cors().disable();

        http.addFilterBefore(jwtFilter, AuthorizationFilter.class);

        //HAAP
        //http.authorizeHttpRequests().anyRequest().permitAll();
        http.authorizeHttpRequests()
                .requestMatchers("/api/auth/sign-up","/api/auth/property/sign-up","/api/auth/login","/api/auth/sent-otp","/api/auth/verify-otp","/api/v1/property/get/property/images","/roomcontroller/rooms/available")
                .permitAll()
                .requestMatchers("/api/v1/review/addreview","/api/v1/review/seeReview","/bookings/book-hotel")
                .hasRole("USER")
                .requestMatchers("/api/v1/property/addproperty","/api/v1/property/upload/file/{bucketName}/property","/api/v1/property/upload/file/{bucketName}/property/{propertyId}","/roomcontroller/set-room-availibility")
                .hasRole("OWNER")
                .requestMatchers("/api/v1/property/deleteproperty")
                .hasAnyRole("OWNER","ADMIN")
                .requestMatchers("/api/auth/blog/sign-up")
                .hasRole("ADMIN")
                .requestMatchers("/api/auth/viewProfile")
                .hasAnyRole("OWNER","ADMIN","USER")
                .anyRequest().authenticated()


        ;
        return http.build();
    }

}
