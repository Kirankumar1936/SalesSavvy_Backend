package com.Spring.APIs.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.Spring.APIs.Entities.CartItem;

import jakarta.transaction.Transactional;
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

	
	@Query("SELECT COALESCE(SUM(c.quantity),0)FROM CartItem c WHERE c.user.user_id=:userId")
	int countTotalItems(int userId);
	
	// Fetch cart item for a given userId and productId
    @Query("SELECT c FROM CartItem c WHERE c.user.user_id = :userId AND c.product.productId = :productId")
    Optional<CartItem> findByUserAndProduct(int userId, int productId);

    @Query("SELECT c FROM CartItem c JOIN FETCH c.product p LEFT JOIN FETCH ProductImage pi ON p.productId = pi.product.productId WHERE c.user.user_id = :userId")
    List<CartItem> findCartItemsWithProductDetails(int userId);

    // Update quantity for a specific cart item
    @Query("UPDATE CartItem c SET c.quantity = :quantity WHERE c.id = :cartItemId")
    void updateCartItemQuantity(int cartItemId, int quantity);

    // Delete a product from the cart
    @Modifying
    @Transactional
    @Query("DELETE FROM CartItem c WHERE c.user.user_id = :userId AND c.product.productId = :productId")
    void deleteCartItem(int userId, int productId);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM CartItem c WHERE c.user.user_id = :userId")
    void deleteAllCartItemsByUserId(int userId);
	
	
	
	
}
