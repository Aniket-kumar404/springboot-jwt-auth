package com.jwt.auth.service;

import java.util.List;

import com.jwt.auth.model.User;

public interface UserDetailService {
	
	  	 public List<User> getAllUser()  ;
	     
	     public User findUserProfileByJwt(String jwt);
	     
	     public User findUserByEmail(String email) ;
	     
	     public User findUserById(String userId) ;

	     public List<User> findAllUsers();
}
