package com.jwt.auth.controller;


import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jwt.auth.dto.UserDto;
import com.jwt.auth.response.AuthRequest;
import com.jwt.auth.service.UserService;


@RestController
@RequestMapping("/auth")
public class UserController {

//	@Autowired
//	private UserdetailServiceImplementation userService;
	
	@Autowired 
	private UserService userService;
	
	
	
	
    @PostMapping("/create")
    public String createUser(@RequestBody UserDto user) {
    	System.out.println(user);
        return userService.create(user);
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest auth) {
    	System.out.println(auth);
    	String jwtToken = userService.generateJwtToken(auth);
    	HashMap<String,String> map = new HashMap<>();
    	map.put("Email",auth.getEmail());
    	map.put("token",jwtToken);
    	
        return new ResponseEntity<>(map,HttpStatus.ACCEPTED);
    }
}
