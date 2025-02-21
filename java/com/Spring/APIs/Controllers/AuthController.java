package com.Spring.APIs.Controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Spring.APIs.Entities.Users;
import com.Spring.APIs.Services.AuthService;
import com.Spring.APIs.dtos.LoginDTO;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins={"http://localhost:5174","http://localhost:5173"},allowCredentials = "true")
public class AuthController {
	@Autowired
	AuthService authService;
	
	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody LoginDTO userCred,HttpServletResponse response) {
			
		try {
			Users user = authService.authenticate(userCred.getUsername(),userCred.getPassword());
			//Encrypted password:
//			String pwd = user.getPassword();
//			System.out.println("Encrypted password: "+pwd);
			//Decrypted Password:
			//We cant decrypt the password which was encrpyted using BcryptPasswordEncoder
			String token = authService.generateToken(user);
			// Set a cookie to token
			Cookie cookie = new Cookie("authToken",token);
			cookie.setHttpOnly(true);
			cookie.setSecure(false);
			cookie.setPath("/");
			cookie.setMaxAge(3600);
			cookie.setDomain("localhost");
			response.addCookie(cookie);
			
			response.addHeader("Set-cookie",
					String.format("authToken = %s;HttpOnly;path=/,Max-Age = 3600;SameSite=none",token));
			
			Map<String,Object> responseBody = new HashMap();
			responseBody.put("message","Login Success");
			responseBody.put("role",user.getRole().name());
			responseBody.put("username",user.getUsername());
			return ResponseEntity.ok(responseBody);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("Error",e.getMessage()));
		}		
	}
	
	@PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request,HttpServletResponse response) {
        try {
        	Users user=(Users) request.getAttribute("authenticatedUser");
            authService.logout(user);
            Cookie cookie = new Cookie("authToken", null);
            cookie.setHttpOnly(true);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("message", "Logout successful");
            return ResponseEntity.ok(responseBody);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Logout failed");
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
	
	
}
