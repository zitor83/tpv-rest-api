package com.ortiz.tpv_backend.infrastructure.config;

import com.ortiz.tpv_backend.infrastructure.security.CustomAccessDeniedHandler;
import com.ortiz.tpv_backend.infrastructure.security.CustomAuthenticationEntryPoint;
import com.ortiz.tpv_backend.infrastructure.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
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
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SecurityConfig {

    @Autowired(required = false)
    private CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired(required = false)
    private CustomAccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(
        HttpSecurity http,
        JwtAuthenticationFilter jwtFilter
    ) throws Exception {
        http.csrf(csrf -> csrf.disable());
        http.cors(Customizer.withDefaults());

        // 1. Configuramos la sesión como Stateless (esencial para JWT)
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 2. Protegemos las rutas
        http.authorizeHttpRequests(auth -> auth
                // Pre-flight CORS (OPTIONS) permitido para navegadores
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // Endpoints públicos temporales para integración MVP sin login en frontend
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
                // Endpoints de autenticación (Login)
                .requestMatchers("/api/auth/login").permitAll()
                // El resto de la API requiere estar logueado
                .anyRequest().authenticated());

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
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager();
    }
}