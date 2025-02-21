package com.Spring.APIs.Controllers;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Spring.APIs.Entities.Product;
import com.Spring.APIs.Entities.Users;
import com.Spring.APIs.Repositories.ProductRepository;
import com.Spring.APIs.Repositories.UserRepository;
import com.Spring.APIs.Services.CartItemService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins="http://localhost:5174",allowCredentials="true")
public class CartController {

		UserRepository userRepository;
		CartItemService cartItemService;
		ProductRepository productRepository;
		
		public CartController(UserRepository userRepository,CartItemService cartItemService,ProductRepository productRepository) {
			this.userRepository=userRepository;
			this.cartItemService=cartItemService;
			this.productRepository=productRepository;
		}
		@GetMapping("/items/count")
		public ResponseEntity<Integer> getCartCount(@RequestParam("username") String username){
			int count=0;
			Optional<Users> optUser = userRepository.findByUsername(username);
			if(optUser !=null) {
				Users user = optUser.get();
				count=cartItemService.getCartItemCount(user.getUser_id());
			}
			return ResponseEntity.ok(count);
		}
		
		 // Add an item to the cart
	    @PostMapping("/add")
	    @CrossOrigin(origins = "http://localhost:5174", allowCredentials = "true")
	    public ResponseEntity<Void> addToCart(@RequestBody Map<String, Object> request) {
	        String username = (String) request.get("username");
	        int productId = (int) request.get("productId");

	        // Handle quantity: Default to 1 if not provided
	        int quantity = request.containsKey("quantity") ? (int) request.get("quantity") : 1;

	        // Fetch the user using username
	        Users user = userRepository.findByUsername(username)
	                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));

	        // Add the product to the cart
	        cartItemService.addToCart(user.getUser_id(), productId, quantity);
	        return ResponseEntity.status(HttpStatus.CREATED).build();
	    }
	    
	    
	    @GetMapping("/items")
	    public ResponseEntity<Map<String,Object>> getCartItems(HttpServletRequest request){
	    	
	    	Users user = (Users) request.getAttribute("authenticatedUser");
	    Map<String,Object> response =  cartItemService.getCartItems(user.getUser_id());
	    return ResponseEntity.ok(response);
	    }
	    
	 // Update Cart Item Quantity
	    @PutMapping("/update")
	    public ResponseEntity<Void> updateCartItemQuantity(@RequestBody Map<String, Object> request) {
	        String username = (String) request.get("username");
	        int productId = (int) request.get("productId");
	        int quantity = (int) request.get("quantity");

	        // Fetch the user using username
	        Users user = userRepository.findByUsername(username)
	                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
	        Product product = productRepository.findById(productId)
					.orElseThrow(() -> new IllegalArgumentException("Product not found"));
	        
	        // Update the cart item quantity
	        cartItemService.updateCartItemQuantity(user.getUser_id(), productId, quantity);
	        return ResponseEntity.status(HttpStatus.OK).build();
	    }
	    
	 // Delete Cart Item
	    @DeleteMapping("/delete")
	    public ResponseEntity<Void> deleteCartItem(@RequestBody Map<String, Object> request) {
	        String username = (String) request.get("username");
	        int productId = (int) request.get("productId");

	        // Fetch the user using username
	        Users user = userRepository.findByUsername(username)
	                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));

	        // Delete the cart item
	        cartItemService.deleteCartItem(user.getUser_id(), productId);
	        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	    }

}
