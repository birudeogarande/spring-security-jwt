package com.saatvik.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity(debug = false)
@EnableMethodSecurity
public class SecurityConfiguration {

    @Autowired
    private DataSource dataSource;

    // This method configures the security filter chain for the application.
    // It specifies that requests to /api/user are permitted without authentication,
    // while all other requests require authentication.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(
                    (auth) -> auth

                  //  .requestMatchers("/api/user").permitAll() // Allow access to /api/user without authentication
                            .requestMatchers("/h2-console/**").permitAll() // Allow access to H2 console without authentication
                            .anyRequest().authenticated()
            )
            .httpBasic(withDefaults());
        http.headers(headersConfigurer->headersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)); // Allow framing from the same origin

        http.csrf(AbstractHttpConfigurer::disable); // Disable CSRF protection for simplicity, not recommended for production
        return http.build();
    }

    // This method configures the web security customizer.
    // It is commented out because the filterChain method already handles the request matchers.
    // Uncommenting this method would allow you to ignore security for the specified path.

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring().requestMatchers("/api/user");
//    }

    @Bean
    public JdbcUserDetailsManager userDetailsService() {
        UserDetails user = User.withUsername("user")
                .password(passwordEncoder().encode("user")) // Using {noop} for plain text password; in production,
                .roles("USER") // This user has both USER and ADMIN roles
                .build();

        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder().encode("admin")) // Using {noop} for plain text password; in production,
                //actual password should be bcrypt encoded
                .roles("ADMIN") // This user has ADMIN role
                .build();

        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
        userDetailsManager.createUser(user);
        userDetailsManager.createUser(admin);
//        return new InMemoryUserDetailsManager(user, admin);
        return userDetailsManager;
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


}