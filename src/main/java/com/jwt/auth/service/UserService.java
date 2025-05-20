package com.jwt.auth.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.jwt.auth.SecurityConfig.JwtProvider;
import com.jwt.auth.dto.UserDto;
import com.jwt.auth.model.Role;
import com.jwt.auth.model.User;
import com.jwt.auth.repository.RolesReposritory;
import com.jwt.auth.repository.UserRepository;
import com.jwt.auth.response.AuthRequest;

@Service
public class UserService {

	@Autowired
	JwtProvider provider;

	@Autowired
	private RolesReposritory roleRepository;
	@Autowired
	private UserRepository userRepository;

	@Autowired
	AuthenticationManager manager;

	public String create(UserDto user) {

		if (userRepository.findByUsername(user.getUsername()) != null) {
			return "User with this username already exists!";
		}
		Role role = roleRepository.findByName(user.getRole());
		if (role == null) {
			role = Role.builder().name(user.getRole()).build();
			roleRepository.save(role);
		}

		User userDetails = User.builder().username(user.getUsername())
				.password(new BCryptPasswordEncoder()
				.encode(user.getPassword()))
				.roles(List.of(role))
				.email(user.getEmail())
				.build();
		userRepository.save(userDetails);

		return "Create Successfully !";
	}

	public String generateJwtToken(AuthRequest user) {
		User userData = userRepository.findByEmail(user.getEmail());
		Authentication authenticate = manager
				.authenticate(new UsernamePasswordAuthenticationToken(userData.getUsername(), user.getPassword()));
		if (authenticate.isAuthenticated())
			return JwtProvider.generateToken(authenticate);
		return "Authentication Failed";

	}

}
