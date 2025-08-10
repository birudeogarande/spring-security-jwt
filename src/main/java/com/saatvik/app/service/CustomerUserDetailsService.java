package com.saatvik.app.service;

import com.saatvik.app.dao.UserRepo;
import com.saatvik.app.dto.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;
    @Override
    public UserEntity loadUserByUsername(String username) throws UsernameNotFoundException {
         return userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    public boolean userExists(String username) {
        return userRepo.existsByUsername(username);
    }

    public UserEntity saveUser(UserEntity user) {
        return userRepo.save(user);
    }
}
