package com.Spring.APIs.AdminServices;

//Admin Service for Modify User functionality


import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.Spring.APIs.Entities.Role;
import com.Spring.APIs.Entities.Users;
import com.Spring.APIs.Repositories.JWTTokenRepository;
import com.Spring.APIs.Repositories.UserRepository;

import java.util.Optional;

@Service
public class AdminUserService {
	 private final UserRepository userRepository;
	    private final JWTTokenRepository jwtTokenRepository;

	    public AdminUserService(UserRepository userRepository, JWTTokenRepository jwtTokenRepository) {
	        this.userRepository = userRepository;
	        this.jwtTokenRepository = jwtTokenRepository;
	    }
	    
	    
	    @Transactional
	    public Users modifyUser(Integer userId, String username, String email, String role) {
	    	 // Check if the user exists
	        Optional<Users> userOptional = userRepository.findById(userId);
	        if (userOptional.isEmpty()) {
	            throw new IllegalArgumentException("User not found");
	        }
	        Users existingUser = userOptional.get();
	        // Update user fields
	        if (username != null && !username.isEmpty()) {
	            existingUser.setUsername(username);
	        }
	        if (email != null && !email.isEmpty()) {
	            existingUser.setEmail(email);
	        }
	        if (role != null && !role.isEmpty()) {
	            try {
	                existingUser.setRole(Role.valueOf(role));
	            } catch (IllegalArgumentException e) {
	                throw new IllegalArgumentException("Invalid role: " + role);
	            }
	        }

	        // Delete associated JWT tokens
	        jwtTokenRepository.deleteByUserId(userId);

	        // Save updated user
	        return userRepository.save(existingUser);
 }

 public Users getUserById(Integer userId) {
     return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
 }
}
