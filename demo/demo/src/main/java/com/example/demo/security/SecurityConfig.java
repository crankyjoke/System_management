package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.core.userdetails.User;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * Define a PasswordEncoder bean so we can store and compare passwords securely.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Create an in-memory user store with two users: an admin and a regular user.
     */
    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        return new InMemoryUserDetailsManager(
                User.withUsername("admin")
                        .password(passwordEncoder.encode("admin123"))
                        .roles("ADMIN")
                        .build(),
                User.withUsername("user")
                        .password(passwordEncoder.encode("user123"))
                        .roles("USER")
                        .build()
        );
    }

    /**
     * Configure the security filter chain:
     * - Restrict `/admin/**` to only ADMIN role.
     * - Allow form-based login (built-in login page) and basic HTTP auth for testing.
     * - All other endpoints require authentication but can be accessed by any authenticated user.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .authorizeHttpRequests(auth -> auth
                        // Only allow users with ADMIN role to access `/admin/**` endpoints
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // All other endpoints require authentication
                        .anyRequest().authenticated()
                )
                // Enable form login (redirects to a default login page provided by Spring Security)
                .formLogin(Customizer.withDefaults())
                // (Optional) Also enable HTTP Basic authentication for quick testing
                .httpBasic(Customizer.withDefaults())
                // For simplicity, disable CSRF for testing REST endpoints
                .csrf(csrf -> csrf.disable())
                .build();
    }
}
