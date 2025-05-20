package com.jwt.auth.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.jwt.auth.model.Role;
import com.jwt.auth.model.User;
import com.jwt.auth.repository.RolesReposritory;
import com.jwt.auth.repository.UserRepository;

@Service
public class UserdetailServiceImplementation implements UserDetailsService{
	  @Autowired
	    private UserRepository userRepository;
	    
	  @Autowired
	  private RolesReposritory roleRepository;
	  
	  
//	    public UserServiceImplementation(UserRepository userRepository) {
//	        this.userRepository=userRepository;
//	    }
//	    
	    
	    @Override
	    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	        User user = userRepository.findByUsername(username);
	        
	        if(user==null) {
	            throw new UsernameNotFoundException("User not found with this username : "+username);

	        }

	      //  List<GrantedAuthority> authorities = new ArrayList<>();
	        return user;
	    }
	    
	  
}
