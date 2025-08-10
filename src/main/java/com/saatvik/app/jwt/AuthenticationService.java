package com.saatvik.app.jwt;

import com.saatvik.app.dto.AuthenticationRequest;
import com.saatvik.app.dto.AuthenticationResponse;
import com.saatvik.app.dto.UserEntity;
import com.saatvik.app.service.CustomerUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationService {

    @Autowired
    private  AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomerUserDetailsService userDetailsService;

    public AuthenticationResponse login(AuthenticationRequest request) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

         String token = jwtUtil.generateToken(userDetails);

        List<String> list = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return new AuthenticationResponse(token, list, request.username());
    }

    public String register(AuthenticationRequest request) {


        boolean isExist = userDetailsService.userExists(request.username());
        if (!isExist) {
            userDetailsService.saveUser(
                    new UserEntity(request.username(),
                            passwordEncoder.encode(request.password()), String.join(",", request.roles())
                    )
            );
        }

    return "User registered successfully";

            // Uncomment the following code if you want to use JdbcUserDetailsManager for user registration
//         UserDetails user = User.withUsername(request.username())
//                .password(passwordEncoder.encode(request.password()))
//                .roles(request.roles().toArray(String[]::new))
//                .build();
//
//        JdbcUserDetailsManager jdbcUserDetailsManager = (JdbcUserDetailsManager) userDetailsService;
//        if (jdbcUserDetailsManager.userExists(request.username())) {
//            throw new RuntimeException("User already exists");
//        }
//        jdbcUserDetailsManager.createUser(user);
//        return "User registered successfully";
    }
}
