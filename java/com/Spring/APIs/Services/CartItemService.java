package com.Spring.APIs.Services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.Spring.APIs.Entities.CartItem;
import com.Spring.APIs.Entities.Product;
import com.Spring.APIs.Entities.ProductImage;
import com.Spring.APIs.Entities.Users;
import com.Spring.APIs.Repositories.CartItemRepository;
import com.Spring.APIs.Repositories.ProductImageRepository;
import com.Spring.APIs.Repositories.ProductRepository;
import com.Spring.APIs.Repositories.UserRepository;

@Service
public class CartItemService {

	
	CartItemRepository cartItemRepository;
	UserRepository userRepository;
	ProductRepository productRepository;
	ProductImageRepository productImageRepository;
	
	
	public CartItemService(CartItemRepository cartItemRepository,UserRepository userRepository,ProductRepository productRepository,ProductImageRepository productImageRepository) {
		this.cartItemRepository = cartItemRepository;
		this.userRepository=userRepository;
		this.productRepository=productRepository;
		this.productImageRepository = productImageRepository;
	}
	
	public int getCartItemCount(int userId) {
		return cartItemRepository.countTotalItems(userId);
		
	}
	
	// Add an item to the cart
		public void addToCart(int userId, int productId, int quantity) {
			Users user = userRepository.findById(userId)
					.orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

			Product product = productRepository.findById(productId)
					.orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));

			// Fetch cart item for this userId and productId
			Optional<CartItem> existingItem = cartItemRepository.findByUserAndProduct(userId, productId);

			if (existingItem.isPresent()) {
				CartItem cartItem = existingItem.get();
				cartItem.setQuantity(cartItem.getQuantity() + quantity);
				cartItemRepository.save(cartItem);
			} else {
				CartItem newItem = new CartItem(user, product, quantity);
				cartItemRepository.save(newItem);
			}
		}
		
		
		//Get cart items for a user
		public Map<String,Object> getCartItems(int userId){
			List<CartItem>  cartItems= cartItemRepository.findCartItemsWithProductDetails(userId);
			
			Map<String,Object> response = new HashMap<>();
			Users user=userRepository.findById(userId).orElse(null);
			
			response.put("username",user.getUsername());
			response.put("role", user.getRole());
			
			List<Map<String,Object>> products = new ArrayList<>();
			int overallTotalPrice = 0;
			for(CartItem cartItem : cartItems) {
				Map<String,Object> productDetails = new HashMap<>();
				Product product = cartItem.getProduct();
			List<ProductImage>	productImages = productImageRepository.findByProduct_ProductId(product.getProductId());
			String imageUrl = null;
			if(productImages!=null && !productImages.isEmpty()) {
				imageUrl = productImages.get(0).getImageUrl();
			}
			else {
				imageUrl = "image not found";
			}
			
			productDetails.put("product_id", product.getProductId());
			productDetails.put("image_url", imageUrl);
			productDetails.put("name", product.getName());
			productDetails.put("description", product.getDescription());
			productDetails.put("price_per_unit", product.getPrice());
			productDetails.put("quantity", cartItem.getQuantity());
			double total_price = cartItem.getQuantity()*product.getPrice().doubleValue();
			productDetails.put("total_price", total_price);
			
			overallTotalPrice += total_price;
			
			products.add(productDetails);
			}
			Map<String,Object> cart = new HashMap<>();
			cart.put("products", products);
			cart.put("overall_total_price", overallTotalPrice);
			
			
			response.put("cart", cart);
			return response;
		}

		// Update Cart Item Quantity
		public void updateCartItemQuantity(int userId, int productId, int quantity) {
			Users user = userRepository.findById(userId)
					.orElseThrow(() -> new IllegalArgumentException("User not found"));

			Product product = productRepository.findById(productId)
					.orElseThrow(() -> new IllegalArgumentException("Product not found"));

			// Fetch cart item for this userId and productId
			Optional<CartItem> existingItem = cartItemRepository.findByUserAndProduct(userId, productId);

			if (existingItem.isPresent()) {
				CartItem cartItem = existingItem.get();
				if (quantity == 0) {
					deleteCartItem(userId, productId);
				} else {
					cartItem.setQuantity(quantity);
					cartItemRepository.save(cartItem);
				}
			}
		}

		// Delete Cart Item
		public void deleteCartItem(int userId, int productId) {
			Users user = userRepository.findById(userId)
					.orElseThrow(() -> new IllegalArgumentException("User not found"));

			Product product = productRepository.findById(productId)
					.orElseThrow(() -> new IllegalArgumentException("Product not found"));

			cartItemRepository.deleteCartItem(userId, productId);
		}
}
