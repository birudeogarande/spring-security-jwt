package com.saatvik.app;

import com.saatvik.app.jwt.JWTAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity(debug = false)
@EnableMethodSecurity
public class SecurityConfiguration {


    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService;

    // This method configures the security filter chain for the application.
    // It specifies that requests to /api/user are permitted without authentication,
    // while all other requests require authentication.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(
                    (auth) -> auth
                            .requestMatchers("/auth/**").permitAll()
                            .requestMatchers("/h2-console/**").permitAll() // Allow access to H2 console without authentication
                            .anyRequest().authenticated()
            );
       // http.authenticationProvider(authenticationProvider); // Set the custom authentication provider
        http.sessionManagement(
                session -> session.sessionCreationPolicy(
                        org.springframework.security.config.http.SessionCreationPolicy.STATELESS
                )
        ); // Use stateless sessions

        http.exceptionHandling(
                exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                        })
        ); // Handle unauthorized access

        http.headers(headersConfigurer->headersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)); // Allow framing from the same origin

        http.csrf(AbstractHttpConfigurer::disable); // Disable CSRF protection for simplicity, not recommended for production

      http.addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public JWTAuthenticationFilter jwtAuthFilter() {
        return new JWTAuthenticationFilter();
    }


    @Bean
    public CommandLineRunner initDataSource(UserDetailsService userDetailsService) {
        return args -> {
            // This method can be used to initialize the database with default users or roles if needed.
            // For example, you can create default users here.
            UserDetails user = User.withUsername("user")
                    .password(passwordEncoder().encode("user")) // Using {noop} for plain text password; in production,
                    .roles("USER") // This user has both USER and ADMIN roles
                    .build();
            UserDetails admin = User.withUsername("admin")
                    .password(passwordEncoder().encode("admin")) // Using {noop} for plain text password; in production,
                    .roles("ADMIN") // This user has ADMIN role
                    .build();

            JdbcUserDetailsManager jdbcUserDetailsManager =  (JdbcUserDetailsManager) userDetailsService;
            ;
            if (jdbcUserDetailsManager.userExists("user")) {
                jdbcUserDetailsManager.updateUser(user);
            } else {
                jdbcUserDetailsManager.createUser(user);
            }

            if (jdbcUserDetailsManager.userExists("admin")) {
                jdbcUserDetailsManager.updateUser(admin);
            } else {
                jdbcUserDetailsManager.createUser(admin);
            }
        };
    }
    @Bean
    public JdbcUserDetailsManager userDetailsService() {
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    // Explain this method :
    // This method creates an AuthenticationProvider that uses the UserDetailsService and PasswordEncoder.
    // It is used to authenticate users based on their username and password.
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationProvider authenticationProvider) {
        return new ProviderManager(authenticationProvider);
    }
}