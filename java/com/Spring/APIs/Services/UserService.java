package com.Spring.APIs.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.Spring.APIs.Entities.Users;
import com.Spring.APIs.Repositories.UserRepository;
@Service
public class UserService {
	
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository=userRepository;
		this.passwordEncoder=new BCryptPasswordEncoder();
	}
	
	public Users userRegister(Users user) {
		if(userRepository.findByUsername(user.getUsername()).isPresent()) {
			throw new RuntimeException("Username is already Taken");
		}
		if(userRepository.findByEmail(user.getEmail()).isPresent()) {
			throw new RuntimeException("Email is already Registered");
		}
		String ppwd = user.getPassword();
		String epwd = passwordEncoder.encode(ppwd);
		user.setPassword(epwd);
		
		return userRepository.save(user);
	}
}
