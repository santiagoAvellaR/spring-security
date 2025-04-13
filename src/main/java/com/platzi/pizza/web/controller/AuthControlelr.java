package com.platzi.pizza.web.controller;

import com.platzi.pizza.service.dto.LoginDto;
import com.platzi.pizza.web.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthControlelr {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthControlelr(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @RequestMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
        UsernamePasswordAuthenticationToken login = new UsernamePasswordAuthenticationToken(
                loginDto.getUserName(),
                loginDto.getPassword()
        );
        Authentication authentication = this.authenticationManager.authenticate(login);
        // si pasa de esta linea, fue porque se autentico de manera correcta
        System.out.println("El usuario: "+ authentication.getPrincipal() + "se logeo correctamente el usuario? " + authentication.isAuthenticated());

        String jwt = jwtUtil.create(loginDto.getUserName());

        return ResponseEntity
                .ok().
                header(HttpHeaders.AUTHORIZATION, jwt)
                .build();
    }

}
