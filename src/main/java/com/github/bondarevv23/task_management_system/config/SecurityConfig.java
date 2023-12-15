package com.github.bondarevv23.task_management_system.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain clientFilterChain(
            HttpSecurity http,
            JwtAuthenticationConverter jwtAuthConverter
    ) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer(
                        server -> server.jwt(
                                jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter)
                        )
                )
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                .requestMatchers("/api/v1/tasks/**").hasRole("user")
                                .requestMatchers("/api/v1/comments/**").hasRole("user")
                                .anyRequest().permitAll()
                )
        ;
        return http.build();
    }

    private static final String REALM_ACCESS = "realm_access";
    private static final String ROLES = "roles";
    private static final String ROLE_PREFIX = "ROLE_";

    /**
     * By default, spring adds 'ROLE_' prefix to every role, that is why keycloak
     * role 'user' and spring role 'user' are not the same. This custom converter
     * takes list of user roles from realm_access.roles claim and add 'ROLE_' prefix
     * to each role.
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverterForKeycloak() {
        Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter = jwt -> {
            Map<String, Collection<String>> realmAccess = jwt.getClaim(REALM_ACCESS);
            Collection<String> roles = realmAccess.get(ROLES);
            return roles.stream()
                    .map(role -> new SimpleGrantedAuthority(ROLE_PREFIX + role))
                    .collect(Collectors.toList());
        };

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }
}
