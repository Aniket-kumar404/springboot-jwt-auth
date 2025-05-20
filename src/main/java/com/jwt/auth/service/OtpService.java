package com.jwt.auth.service;

import java.util.Random;

import org.springframework.stereotype.Service;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

@Service
public class OtpService {
	  private final GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
	    private final Random random = new Random();

	    public String generateSecretKey() {
	        GoogleAuthenticatorKey key = googleAuthenticator.createCredentials();
	        return key.getKey();
	    }

	    public int generateOTP(String secretKey) {
	        return googleAuthenticator.getTotpPassword(secretKey);
	    }

	    public boolean validateOTP(String secretKey, int otp) {
	        return googleAuthenticator.authorize(secretKey, otp);
	    }
}
