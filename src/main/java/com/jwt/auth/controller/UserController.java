package com.jwt.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jwt.auth.request.AuthRequest;
import com.jwt.auth.request.UserDto;
import com.jwt.auth.response.AuthResponse;
import com.jwt.auth.service.UserService;
import com.jwt.auth.utility.JwtProvider;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;


    @Autowired
    JwtProvider jwtProvider;

    @PostMapping("/create")
    public String createUser(@RequestBody UserDto user) {
        System.out.println(user);
        return userService.create(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest auth) {

        String jwtToken = userService.generateJwtToken(auth);
        String refreshToken = jwtProvider.createRefreshToken(auth.getEmail());

        return new ResponseEntity<>(AuthResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build(),
                HttpStatus.ACCEPTED);

    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestParam String refreshToken) {

        if (userService.verifyExpiration(refreshToken)) {;
            String accessToken = userService.generateAccessToken(refreshToken);
            return ResponseEntity.ok(
                    AuthResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
    }

}
