package com.Spring.APIs.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Spring.APIs.Entities.Category;
import com.Spring.APIs.Entities.Product;
import com.Spring.APIs.Entities.ProductImage;
import com.Spring.APIs.Repositories.CategoryRepository;
import com.Spring.APIs.Repositories.ProductImageRepository;
import com.Spring.APIs.Repositories.ProductRepository;
@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private ProductImageRepository productImageRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	
	public List<Product> getProductsByCategory(String categoryName){
		if(categoryName != null && !categoryName.isEmpty()) {
			Optional<Category> categoryOpt = categoryRepository.findByCategoryName(categoryName);
			if(categoryOpt.isPresent()) {
				Category category = categoryOpt.get();
				return productRepository.findByCategory_CategoryId(category.getCategoryId());
			}else {
				throw new RuntimeException("Category not found");
			}
		}else {
			return productRepository.findAll();
		}
	}
	
	
	public List<String> getProductImages(Integer productId){
		List<ProductImage> productImages = productImageRepository.findByProduct_ProductId(productId);
		List<String> imageUrls = new ArrayList<>();
		for(ProductImage image:productImages) {
			imageUrls.add(image.getImageUrl());
		}
		return imageUrls;
	}
}
