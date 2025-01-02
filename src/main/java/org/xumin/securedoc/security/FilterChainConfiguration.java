package org.xumin.securedoc.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.xumin.securedoc.service.UserService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class FilterChainConfiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request ->
                        request.requestMatchers("/user/login").permitAll()
                        .anyRequest().authenticated())
                .build();

    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
//        var myOwnAuthenticationProvider = new ApiAuthenticationProvider(userDetailsService);
//        return new ProviderManager(myOwnAuthenticationProvider);
        return null;
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        var admin = User.withDefaultPasswordEncoder()
//                        .username("admin")
//                        .password("12345678")
//                        .roles("USER")
//                        .build();
//
//        var hanna = User.withDefaultPasswordEncoder()
//                        .username("hanna")
//                        .password("12345678")
//                        .roles("USER")
//                        .build();
//
//        return new InMemoryUserDetailsManager(List.of(admin, hanna));
//    }

    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        return new InMemoryUserDetailsManager(
                User.withUsername("admin").password("12345678").roles("USER").build(),
                User.withUsername("hanna").password("12345678").roles("USER").build()
        );
    }
}
