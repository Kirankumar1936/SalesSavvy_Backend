package com.Spring.APIs.Controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Spring.APIs.Entities.Users;
import com.Spring.APIs.Services.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins={"http://localhost:5174","http://localhost:5173"},allowCredentials = "true")
public class UserController {
	private UserService userService;
	
	public UserController(UserService userService) {
		this.userService =userService;
	}
	@PostMapping("/register")
	@CrossOrigin(origins = "http://localhost:5174", allowCredentials = "true") // Also apply to this method
	public ResponseEntity<?> registerUser(@RequestBody Users user) {
		try {
		Users registeredUser = userService.userRegister(user);
		return ResponseEntity.ok(Map.of("message","User Registered Successfully","user",registeredUser));
		}
		catch(Exception e) {
			return ResponseEntity.badRequest().body(Map.of("ERROR",e.getMessage()));
		} 
	}
}
