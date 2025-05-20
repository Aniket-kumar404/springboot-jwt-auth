package com.jwt.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jwt.auth.model.User;
import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	User findByEmail(String email);
	User findByUsername(String username);
}
