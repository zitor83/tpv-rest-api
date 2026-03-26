package com.ortiz.tpv_backend.infrastructure.config;

import com.ortiz.tpv_backend.infrastructure.security.CustomAccessDeniedHandler;
import com.ortiz.tpv_backend.infrastructure.security.CustomAuthenticationEntryPoint;
import com.ortiz.tpv_backend.infrastructure.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired(required = false)
    private CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired(required = false)
    private CustomAccessDeniedHandler accessDeniedHandler;

    @Bean
    @Profile("local")
    public SecurityFilterChain localFilterChain(
        HttpSecurity http,
        JwtAuthenticationFilter jwtFilter
    ) throws Exception {
        http.csrf(csrf -> csrf.disable());
        http.cors(Customizer.withDefaults());

        // 1. Configuramos la sesión como Stateless (esencial para JWT)
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 2. Perfil local: endpoints MVP abiertos para integración de frontend sin login.
        http.authorizeHttpRequests(auth -> auth
                // Pre-flight CORS (OPTIONS) permitido para navegadores
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // Endpoints públicos del MVP
                .requestMatchers(HttpMethod.GET, "/api/products").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/orders").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/orders").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/expenses").permitAll()
                // OpenAPI 3.0 y Swagger UI
                .requestMatchers(
                    "/v3/api-docs",
                    "/v3/api-docs/**",
                    "/v3/api-docs.yaml",
                    "/swagger-ui/**",
                    "/swagger-ui.html"
                ).permitAll()
                // H2 Console
                .requestMatchers("/h2-console/**").permitAll()
                // Endpoints de autenticación
                .requestMatchers("/api/auth/login").permitAll()
                .anyRequest().permitAll());

        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));

        // 3. Configuramos handlers de errores de seguridad (si están disponibles)
        if (authenticationEntryPoint != null && accessDeniedHandler != null) {
            http.exceptionHandling(exceptions -> exceptions
                    // CustomAuthenticationEntryPoint maneja errores de autenticación (401)
                    .authenticationEntryPoint(authenticationEntryPoint)
                    // CustomAccessDeniedHandler maneja errores de autorización (403)
                    .accessDeniedHandler(accessDeniedHandler));
        }

        // 4. Añadimos el filtro que lee el Token en cada petición
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    @Profile("!local")
    public SecurityFilterChain securedFilterChain(
        HttpSecurity http,
        JwtAuthenticationFilter jwtFilter
    ) throws Exception {
        http.csrf(csrf -> csrf.disable());
        http.cors(Customizer.withDefaults());
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .requestMatchers(
                "/v3/api-docs",
                "/v3/api-docs/**",
                "/v3/api-docs.yaml",
                "/swagger-ui/**",
                "/swagger-ui.html"
            ).permitAll()
            .requestMatchers("/h2-console/**").permitAll()
            .requestMatchers("/api/auth/login").permitAll()
            .anyRequest().authenticated());

        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));

        if (authenticationEntryPoint != null && accessDeniedHandler != null) {
            http.exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler));
        }

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager();
    }
}