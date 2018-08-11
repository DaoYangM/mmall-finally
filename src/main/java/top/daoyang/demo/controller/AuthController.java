package top.daoyang.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import top.daoyang.demo.payload.reponse.JwtAuthenticationReponse;
import top.daoyang.demo.payload.request.JwtAuthenticationRequest;
import top.daoyang.demo.security.JwtTokenProvider;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public JwtAuthenticationReponse login(@Valid @RequestBody JwtAuthenticationRequest jwtAuthenticationRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                jwtAuthenticationRequest.getUsername(),
                jwtAuthenticationRequest.getPassword()
        ));

        String jwtToken = jwtTokenProvider.generateToken(authentication);

        return new JwtAuthenticationReponse(jwtToken);
    }
}
