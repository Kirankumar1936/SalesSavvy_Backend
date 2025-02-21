package com.Spring.APIs.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.Spring.APIs.Entities.JWTToken;

import jakarta.transaction.Transactional;

@Repository
public interface JWTTokenRepository extends JpaRepository<JWTToken, Integer> {
	//Custom query to find tokens by user id
	@Query("SELECT t FROM JWTToken t WHERE t.user.user_id= :user_id")
	JWTToken findByUserId(int user_id);
	
	// custom query to delete tokens by user id
	@Modifying
	@Transactional
	@Query("DELETE FROM JWTToken t WHERE t.user.user_id = :user_id")
	void deleteByUserId(int user_id);
	Optional<JWTToken> findByToken(String token);
}
