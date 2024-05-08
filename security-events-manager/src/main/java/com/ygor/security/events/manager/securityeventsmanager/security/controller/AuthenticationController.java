package com.ygor.security.events.manager.securityeventsmanager.security.controller;

import com.ygor.security.events.manager.securityeventsmanager.security.dto.LoginRequestDTO;
import com.ygor.security.events.manager.securityeventsmanager.security.services.AuthenticationService;
import com.ygor.security.events.manager.securityeventsmanager.security.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    private final AuthenticationService authenticationService;

    private final TokenService tokenService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, TokenService tokenService){
        this.authenticationService = authenticationService;
        this.tokenService = tokenService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity authenticate(@RequestBody LoginRequestDTO loginRequestDTO) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());
        Authentication auth = this.authenticationManager.authenticate(usernamePassword);

        return ResponseEntity.ok().body(tokenService.generateToken(auth));
    }
}
