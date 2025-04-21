package com.example.demo.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class SecurityConfig {
    @Bean

    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        return http
//                .httpBasic(httpBasic -> {})
//                .cors(cors -> cors.configurationSource(request -> {
//                    CorsConfiguration config = new CorsConfiguration();
//                    config.addAllowedOrigin("http://localhost:8000");
//                    config.addAllowedMethod("*");
//                    config.addAllowedHeader("*");
//                    config.setAllowCredentials(true);
//                    return config;
//                }))
//                .csrf(csrf -> csrf.disable())
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/login", "api/logout", "/api/currentUser","/api/userPosition").permitAll()
//                        .requestMatchers("/admin/**").hasRole("ADMIN")
//                        .anyRequest().authenticated()
//                )
//                .formLogin(form -> form
//                        .loginProcessingUrl("/login")
//                        .successHandler((request, response, authentication) -> {
//                            response.setContentType("application/json");
//                            response.setCharacterEncoding("UTF-8");
//                            PrintWriter writer = response.getWriter();
//
//
//                            request.getSession().setAttribute("user", authentication.getName());
//
//                            Map<String, Object> responseData = new HashMap<>();
//                            responseData.put("status", "ok");
//                            responseData.put("message", "Login successful");
//                            responseData.put("username", authentication.getName());
//
//                            writer.write(new ObjectMapper().writeValueAsString(responseData));
//                            writer.flush();
//                        })
//                )
//                .logout(logout -> logout
//                        .logoutUrl("/logout")
//                        .invalidateHttpSession(true)
//                        .deleteCookies("JSESSIONID")
//                        .clearAuthentication(true)
//                        .permitAll()
//                )
//                .build();
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(Customizer.withDefaults())        // keep if you use it
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration cfg = new CorsConfiguration();
                    cfg.addAllowedOrigin("http://localhost:8000");
                    cfg.addAllowedMethod("*");
                    cfg.addAllowedHeader("*");
                    cfg.setAllowCredentials(true);
                    return cfg;
                }))
                .csrf(csrf -> csrf.disable())                // REST‑only
                .sessionManagement(s ->                     // 🔹 change here
                        s.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login",
                                "/api/logout",          // 🔹 add leading /
                                "/api/currentUser",
                                "/api/userPosition").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginProcessingUrl("/login")
                        .successHandler((req, res, auth) -> {
                            res.setContentType("application/json;charset=UTF‑8");
                            req.getSession().setAttribute("user", auth.getName());
                            Map<String,Object> body = Map.of(
                                    "status","ok",
                                    "message","Login successful",
                                    "username",auth.getName());
                            new ObjectMapper().writeValue(res.getWriter(), body);
                        }))
                .logout(log -> log
                        .logoutUrl("/api/logout")                // 🔹 match the front‑end
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")             // add "remember-me" if you use it
                        .clearAuthentication(true)
                        .permitAll());

        return http.build();
    }


    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:8000");
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
