package com.jwt.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jwt.auth.model.Role;

@Repository
public interface RolesReposritory extends JpaRepository<Role, Integer> {
	Role findByName(String name);
}
