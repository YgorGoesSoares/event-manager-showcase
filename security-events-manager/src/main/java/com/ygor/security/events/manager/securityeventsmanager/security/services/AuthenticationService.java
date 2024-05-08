package com.ygor.security.events.manager.securityeventsmanager.security.services;

import com.ygor.security.events.manager.securityeventsmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {
    private final UserRepository userRepository;
    private final TokenService tokenService;

    @Autowired
    public AuthenticationService(UserRepository userRepository, TokenService tokenService) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username);
    }
}
