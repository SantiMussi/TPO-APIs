package com.uade.tpo.demo.controllers.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

import com.uade.tpo.demo.entity.Role;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthFilter;
        private final AuthenticationProvider authenticationProvider;
        private static final String FULL_PRODUCT_ENDPOINT = "/product/**";
        private static final String FULL_CATEGORY_ENDPOINT = "/categories/**";
        
        
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(req -> req


                                                /*

                                                        MAPEO DE PATHS Y ROLES PARA ACCESO A REQUESTS

                                                 */

                                                .requestMatchers("/api/v1/auth/**").permitAll()
                                                .requestMatchers("/error/**").permitAll()

                                                //Category
                                                .requestMatchers(HttpMethod.GET, FULL_CATEGORY_ENDPOINT).hasAnyAuthority(Role.USER.name(), Role.SELLER.name(), Role.ADMIN.name())
                                                .requestMatchers(HttpMethod.POST, FULL_CATEGORY_ENDPOINT).hasAnyAuthority(Role.SELLER.name(), Role.ADMIN.name()) //Solo el seller y admin pueden crear categorias
                                                
                                                //Product
                                                .requestMatchers(HttpMethod.POST, FULL_PRODUCT_ENDPOINT).hasAnyAuthority(Role.SELLER.name(), Role.ADMIN.name())
                                                .requestMatchers(HttpMethod.DELETE, FULL_PRODUCT_ENDPOINT).hasAnyAuthority(Role.SELLER.name(), Role.ADMIN.name())
                                                .requestMatchers(HttpMethod.PUT, FULL_PRODUCT_ENDPOINT).hasAnyAuthority(Role.SELLER.name(), Role.ADMIN.name())                                                
                                                
                                                /*

                                                        FIN DE MAPEOS

                                                */

                                                .anyRequest()

                                                .authenticated())
                                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
}
