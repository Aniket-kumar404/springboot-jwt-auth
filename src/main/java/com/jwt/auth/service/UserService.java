package com.jwt.auth.service;

import java.time.Instant;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.jwt.auth.model.RefreshToken;
import com.jwt.auth.model.Role;
import com.jwt.auth.model.User;
import com.jwt.auth.repository.RefreshTokenRepository;
import com.jwt.auth.repository.RolesReposritory;
import com.jwt.auth.repository.UserRepository;
import com.jwt.auth.request.AuthRequest;
import com.jwt.auth.request.TokenRequest;
import com.jwt.auth.request.UserDto;
import com.jwt.auth.utility.JwtProvider;

@Service
public class UserService {

	@Autowired
	JwtProvider provider;

	@Autowired
	private RolesReposritory roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	RefreshTokenRepository refreshTokenRepository;

	@Autowired
	AuthenticationManager manager;

	private static final Logger log = LoggerFactory.getLogger(UserService.class);

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
		log.info("User Created Successfully");
		log.info("User Details: {}", userDetails);
		return "Create Successfully !";
	}

	public String generateJwtToken(AuthRequest user) {
		User userData = userRepository.findByEmail(user.getEmail());
		Authentication authenticate = manager
				.authenticate(new UsernamePasswordAuthenticationToken(userData.getUsername(), user.getPassword()));

		if (authenticate == null) {
			log.error("Authentication Failed");
			return "Authentication Failed";
		}
		log.info("User Authentication Successful");
		log.info("User Details: {}", userData);
		return JwtProvider.generateToken(userData);

	}

	public String generateAccessToken(String refreshToken) {
		RefreshToken rToken = refreshTokenRepository.findByToken(refreshToken);
		User userData = userRepository.findByEmail(rToken.getUser().getEmail());
		if (userData == null) {
			log.error("User not found");
			return "User not found";
		}
		log.info("User Authentication Successful");
		log.info("User Details: {}", userData);
		User user = rToken.getUser();
		return JwtProvider.generateToken(user);
	}

	public String generateRefreshToken(String email) {
		User user = userRepository.findByEmail(email);
		if (user == null) {
			log.error("User not found");
			return "User not found";
		}
		String refreshToken = provider.createRefreshToken(email);
		log.info("Refresh Token Generated Successfully");
		log.info("Refresh Token: {}", refreshToken);
		return refreshToken;
	}

	public Boolean verifyExpiration(String refreshToken) {
		RefreshToken token = refreshTokenRepository.findByToken(refreshToken);
		if (token.getExpiryDate().isBefore(Instant.now())) {
			refreshTokenRepository.deleteByToken(refreshToken);
			throw new RuntimeException("Refresh token expired.");
		}
		return true;
	}

}
