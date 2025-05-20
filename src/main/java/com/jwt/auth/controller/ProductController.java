package com.jwt.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/")
public class ProductController {
	@GetMapping("home")
	public String home() {
		return "User Home Controller";
	}
}
