package com.Spring.APIs.Services;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.Spring.APIs.Entities.JWTToken;
import com.Spring.APIs.Entities.Users;
import com.Spring.APIs.Repositories.JWTTokenRepository;
import com.Spring.APIs.Repositories.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
@Service
public class AuthService {
	
	private final Key SIGNING_KEY;
	private final UserRepository userRepository;
	private final JWTTokenRepository jwtTokenRepository;
	private BCryptPasswordEncoder passwordEncoder;
	
	//Injecting jwt.secret from properties file
	@Autowired
	public AuthService(UserRepository userRepository,JWTTokenRepository jwtTokenRepository,@Value("${jwt.secret}")String jwtSecret) {
		this.jwtTokenRepository = jwtTokenRepository;
		this.userRepository = userRepository;
		this.passwordEncoder = new BCryptPasswordEncoder();
		this.SIGNING_KEY = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
	}
	
	public Users authenticate(String username,String password) {
		//do the validation for username and password by matching with db.
		Users user = userRepository.findByUsername(username).orElseThrow(()-> new RuntimeException("Invalid username or password"));
		if (!passwordEncoder.matches(password,user.getPassword())){
			throw new RuntimeException("Invalid username or password");
		}
		return user;
	}
	
	public String generateToken(Users user) {
		String token;
		LocalDateTime currentTime = LocalDateTime.now();
		JWTToken existingToken =jwtTokenRepository.findByUserId(user.getUser_id());
		if(existingToken != null && currentTime.isBefore(existingToken.getExpires_at())) {
			token =existingToken.getToken();
		}
		else {
			token = generateNewToken(user);
			if(existingToken!=null) {
				jwtTokenRepository.delete(existingToken);	
			}
			saveToken(token,user);
		}
		return token;
		}
	
	public String generateNewToken(Users user) {
		String token = Jwts.builder().setSubject(user.getUsername()).
				claim("role",user.getRole().name()).
				setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis()+3600000))
				.signWith(SIGNING_KEY,SignatureAlgorithm.HS512)
				.compact();
		return token;
	}
	
	public void saveToken(String token,Users user) {
		JWTToken jwtToken = new JWTToken(user,token,LocalDateTime.now().plusHours(1));
		jwtTokenRepository.save(jwtToken);
	}
	//Validating token and returning t/f to controller
	public boolean validateToken(String token) {
		try {
			System.out.println("Validating Token...");
			//parse and validate the token
			Jwts.parserBuilder()
			.setSigningKey(SIGNING_KEY)
			.build()
			.parseClaimsJws(token);
			//Check if the token exists in the database and is not expired
			Optional<JWTToken> jwtToken = jwtTokenRepository.findByToken(token);
			if(jwtToken.isPresent()) {
				System.err.println("Token Expiry: "+jwtToken.get().getExpires_at());
				System.err.println("Current Time: "+LocalDateTime.now());
				return jwtToken.get().getExpires_at().isAfter(LocalDateTime.now());
			}
			return false;
		}catch(Exception e) {
			System.err.println("Token Validation failed: "+e.getMessage());
			return false;
		}
	}
	
	//getting username based on subject of Token to check user available in repository
	public String extractUsername(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(SIGNING_KEY)
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}
	
	 public void logout(Users user) {
	        jwtTokenRepository.deleteByUserId(user.getUser_id());
	    }
	
	
}
