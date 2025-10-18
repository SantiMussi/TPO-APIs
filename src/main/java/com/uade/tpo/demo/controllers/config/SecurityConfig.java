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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthFilter;
        private final AuthenticationProvider authenticationProvider;
        private static final String FULL_PRODUCT_ENDPOINT = "/product/**";
        private static final String FULL_CATEGORY_ENDPOINT = "/categories/**";
        private static final String FULL_USER_ENDPOINT = "/users/**";
        private static final String PURCHASE_ENDPOINT = "/product/purchase";
        private static final String PRODUCT_MODIFY_ENDPOINT = "/product/{productId}/modify";
        private static final String PRODUCT_DELETE_ENDPOINT = "/product/{productId}/delete";
        private static final String USER_GET_ENDPOINT = "/users";
        private static final String FULL_COUPON_ENDPOINT = "/coupons/**";
        private static final String USER_GET_ME = "/users/me/**";
        private static final String ORDERS_GET_ENDPOINT = "/orders/**";

        
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                                .csrf(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(req -> req


                                                /*

                                                        MAPEO DE PATHS Y ROLES PARA ACCESO A REQUESTS

                                                 */

                                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                                .requestMatchers("/api/v1/auth/**").permitAll()
                                                .requestMatchers("/error/**").permitAll()

                                                //Purchase
                                                .requestMatchers(HttpMethod.POST, PURCHASE_ENDPOINT).hasAnyAuthority(Role.USER.name())

                                                // Modify
                                                .requestMatchers(HttpMethod.PUT, PRODUCT_MODIFY_ENDPOINT).hasAnyAuthority(Role.ADMIN.name(), Role.SELLER.name())
                                                
                                                // Delete
                                                .requestMatchers(HttpMethod.DELETE, PRODUCT_DELETE_ENDPOINT).hasAnyAuthority(Role.ADMIN.name(), Role.SELLER.name())

                                                // Get Users
                                                .requestMatchers(HttpMethod.GET, USER_GET_ENDPOINT).hasAnyAuthority(Role.ADMIN.name())

                                                //Category
                                                .requestMatchers(HttpMethod.GET, FULL_CATEGORY_ENDPOINT).permitAll()
                                                .requestMatchers(HttpMethod.POST, FULL_CATEGORY_ENDPOINT).hasAnyAuthority(Role.ADMIN.name()) 
                                                .requestMatchers(HttpMethod.PUT, FULL_CATEGORY_ENDPOINT).hasAnyAuthority(Role.ADMIN.name()) 
                                                
                                                //Product
                                                .requestMatchers(HttpMethod.POST, FULL_PRODUCT_ENDPOINT).hasAnyAuthority(Role.SELLER.name(), Role.ADMIN.name())
                                                .requestMatchers(HttpMethod.DELETE, FULL_PRODUCT_ENDPOINT).hasAnyAuthority(Role.SELLER.name(), Role.ADMIN.name())
                                                .requestMatchers(HttpMethod.PUT, FULL_PRODUCT_ENDPOINT).hasAnyAuthority(Role.SELLER.name(), Role.ADMIN.name())
                                                .requestMatchers(HttpMethod.GET, FULL_PRODUCT_ENDPOINT).permitAll()

                                                //User
                                                .requestMatchers(HttpMethod.GET, USER_GET_ME).authenticated()
                                                .requestMatchers(HttpMethod.GET, USER_GET_ENDPOINT).hasAnyAuthority(Role.ADMIN.name())
                                                .requestMatchers(HttpMethod.PUT, FULL_USER_ENDPOINT).hasAnyAuthority(Role.ADMIN.name())
                                                .requestMatchers(HttpMethod.DELETE, FULL_USER_ENDPOINT).hasAnyAuthority(Role.ADMIN.name())
                                                .requestMatchers(HttpMethod.GET, FULL_USER_ENDPOINT).hasAnyAuthority(Role.ADMIN.name())

                                                // Coupon
                                                .requestMatchers(HttpMethod.GET, FULL_COUPON_ENDPOINT).hasAnyAuthority(Role.ADMIN.name(), Role.USER.name())
                                                .requestMatchers(HttpMethod.POST, FULL_COUPON_ENDPOINT).hasAnyAuthority(Role.ADMIN.name())
                                                .requestMatchers(HttpMethod.DELETE, FULL_COUPON_ENDPOINT).hasAnyAuthority(Role.ADMIN.name())

                                                // Order
                                                .requestMatchers(HttpMethod.GET, ORDERS_GET_ENDPOINT).hasAnyAuthority(Role.ADMIN.name())
                                                
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

        @Bean
        public CorsConfigurationSource corsConfigurationSource(){
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(Arrays.asList("*"));
                configuration.setAllowCredentials(true);
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }
}
