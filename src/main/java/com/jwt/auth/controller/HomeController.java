package com.jwt.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import io.swagger.annotations.Api;

@RestController
@RequestMapping("api")
//@Api(tags = "User Management")
public class HomeController {
 
	@GetMapping("/home")
	public ResponseEntity<String> home(){
		return new ResponseEntity<>("Home controller ",HttpStatus.ACCEPTED);
	}
}
