package com.smartlogix.apigateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/usuarios/login", "/api/usuarios/registrar").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/usuarios/rol/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/usuarios/**").authenticated()

                        .requestMatchers(HttpMethod.DELETE, "/api/inventario/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/inventario/**").hasAnyRole("ADMIN", "VENDEDOR")
                        .requestMatchers(HttpMethod.PUT, "/api/inventario/**").hasAnyRole("ADMIN", "VENDEDOR")
                        .requestMatchers(HttpMethod.GET, "/api/inventario/**").authenticated()

                        .requestMatchers("/api/bodegas/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/proveedores/**").hasAnyRole("ADMIN", "PROVEEDOR")
                        .requestMatchers(HttpMethod.POST, "/api/proveedores/**").hasAnyRole("ADMIN", "PROVEEDOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/proveedores/**").hasRole("ADMIN")

                        .requestMatchers("/api/pedidos/**").hasAnyRole("ADMIN", "VENDEDOR", "USER")
                        .requestMatchers("/api/boletas/**").hasAnyRole("ADMIN", "VENDEDOR", "USER")

                        .requestMatchers("/api/envios/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/notificaciones/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/notificaciones/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/notificaciones/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*");
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}