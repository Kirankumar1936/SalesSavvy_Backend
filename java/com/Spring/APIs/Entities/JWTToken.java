package com.Spring.APIs.Entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="jwt_tokens")
public class JWTToken {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int token_id;
	@ManyToOne
	@JoinColumn(name="user_id")
	private Users user;
	@Column
	private String token;
	@Column
	private  LocalDateTime expires_at;

	public JWTToken() {
		super();
		// TODO Auto-generated constructor stub
	}

	public JWTToken(int token_id, Users user, String token, LocalDateTime expires_at) {
		super();
		this.token_id = token_id;
		this.user = user;
		this.token = token;
		this.expires_at = expires_at;
	}

	public JWTToken(Users user, String token, LocalDateTime expires_at) {
		super();
		this.user = user;
		this.token = token;
		this.expires_at = expires_at;
	}

	public int getToken_id() {
		return token_id;
	}

	public void setToken_id(int token_id) {
		this.token_id = token_id;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public LocalDateTime getExpires_at() {
		return expires_at;
	}

	public void setExpires_at(LocalDateTime expires_at) {
		this.expires_at = expires_at;
	}
	

}
