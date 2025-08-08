package com.saatvik.app.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class UserController {
Logger logger = LoggerFactory.getLogger(UserController.class);
    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String sayHello(){
        logger.info("User endpoint accessed");
        return "Hello User!!";
    }

    // want to authenticate only admin users

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String sayHelloToAdmin(){
        logger.info("Admin endpoint accessed");
        return "Hello Admin!!";
    }
}
