package in.ramesh.config;

import in.ramesh.exception.CustomAccessDeniedHandler;
import in.ramesh.security.JwtFilter;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    //  Password Encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //  Security Filter Chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http

            //  CORS (IMPORTANT FOR FRONTEND)
            .cors(cors -> cors.configurationSource(request -> {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(List.of("*"));
                config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
                config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
                return config;
            }))
                      
            .exceptionHandling(exception -> 
            exception.accessDeniedHandler(customAccessDeniedHandler)
            )

            //  Disable CSRF (REST API)
            .csrf(csrf -> csrf.disable())

            //  Stateless session (JWT)
            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            //  Authorization rules
            .authorizeHttpRequests(auth -> auth

                    // public endpoints
                    .requestMatchers("/auth/**","/api/v1/premium/**").permitAll()

                    // employee APIs (secured)
                    .requestMatchers("/api/employees/**").authenticated()

                    // optional role-based (future use)
                    // .requestMatchers("/admin/**").hasRole("ADMIN")

                    .anyRequest().authenticated()
            )

            //  Disable default login forms
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())

            //  JWT filter
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}