package com.saatvik.app.api;

import com.saatvik.app.dto.AuthenticationRequest;
import com.saatvik.app.dto.AuthenticationResponse;
import com.saatvik.app.jwt.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    // This controller will handle authentication-related endpoints.
    // You can add methods here to handle login, registration, etc.
    // For example:
     @PostMapping("/login")
     public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) {

         AuthenticationResponse authenticationResponse =  authenticationService.login(request);
          return ResponseEntity.ok(authenticationResponse);
     }

     @PostMapping("/register")
     public ResponseEntity<?> register(@RequestBody AuthenticationRequest request) {
        String message =  authenticationService.register(request);
         return ResponseEntity.ok(message);
     }

}
