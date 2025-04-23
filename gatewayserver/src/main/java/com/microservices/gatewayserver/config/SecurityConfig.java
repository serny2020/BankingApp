package com.microservices.gatewayserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity) {
        serverHttpSecurity.authorizeExchange(exchanges -> exchanges
                        .pathMatchers(HttpMethod.GET).permitAll()
                        .pathMatchers("/bank/accounts/**").authenticated()
                        .pathMatchers("/bank/cards/**").authenticated()
                        .pathMatchers("/bank/loans/**").authenticated()
                )
                .oauth2ResourceServer(oAuth2ResourceServerSpec ->
                        oAuth2ResourceServerSpec.jwt(Customizer.withDefaults())
                );

        serverHttpSecurity.csrf(csrfSpec -> csrfSpec.disable());

        return serverHttpSecurity.build();
    }

//    @Bean
//    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity) {
//        // Configure endpoint access rules
//        serverHttpSecurity.authorizeExchange(exchanges -> exchanges
//                        // Allow all GET requests without authentication
//                        .pathMatchers(HttpMethod.GET).permitAll()
//
//                        // Require "ACCOUNTS" role for accessing /bank/accounts/**
//                        .pathMatchers("/bank/accounts/**").hasRole("ACCOUNTS")
//
//                        // Require "CARDS" role for accessing /bank/cards/**
//                        .pathMatchers("/bank/cards/**").hasRole("CARDS")
//
//                        // Require "LOANS" role for accessing /bank/loans/**
//                        .pathMatchers("/bank/loans/**").hasRole("LOANS"))
//
//                // Configure JWT-based authentication using a custom converter
//                .oauth2ResourceServer(oAuth2ResourceServerSpec -> oAuth2ResourceServerSpec
//                        .jwt(jwtSpec -> jwtSpec
//                                .jwtAuthenticationConverter(grantedAuthoritiesExtractor()))); // Converts JWT claims into granted authorities
//
//        // Disable CSRF protection (suitable for stateless APIs)
//        serverHttpSecurity.csrf(csrfSpec -> csrfSpec.disable());
//
//        // Build and return the configured security filter chain
//        return serverHttpSecurity.build();
//    }
//
//    // Custom converter to extract user roles from the JWT token issued by Keycloak
//    private Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
//        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
//
//        // Set a custom converter that maps Keycloak roles into Spring Security authorities
//        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
//
//        // Wrap the converter for reactive security use
//        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
//    }

}