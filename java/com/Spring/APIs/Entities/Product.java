package com.Spring.APIs.Entities;

import java.math.BigDecimal;
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
@Table(name="products")
public class Product {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="product_id")
	private Integer productId;
	
	@Column(columnDefinition = "TEXT")
	private String name;
	
	@Column(columnDefinition = "TEXT")
	private String description;
	
	@Column(nullable=false,precision = 10,scale=2)
	private BigDecimal price;
	
	@Column(nullable=false)
	private Integer stock;
	
	@ManyToOne
	@JoinColumn(name="category_id")
	private Category category;
	
	@Column(nullable=false,columnDefinition = "TIMESTAMP DEFAULT CURRENT TIMESTAMP")
	private LocalDateTime created_at;
	
	@Column(nullable=false,columnDefinition = "TIMESTAMP DEFAULT CURRENT TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
	private LocalDateTime updated_at;

	public Product() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Product(Integer productId, String name, String description, BigDecimal price, Integer stock,
			Category category, LocalDateTime created_at, LocalDateTime updated_at) {
		super();
		this.productId = productId;
		this.name = name;
		this.description = description;
		this.price = price;
		this.stock = stock;
		this.category = category;
		this.created_at = created_at;
		this.updated_at = updated_at;
	}

	public Product(String name, String description, BigDecimal price, Integer stock, Category category,
			LocalDateTime created_at, LocalDateTime updated_at) {
		super();
		this.name = name;
		this.description = description;
		this.price = price;
		this.stock = stock;
		this.category = category;
		this.created_at = created_at;
		this.updated_at = updated_at;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public LocalDateTime getCreated_at() {
		return created_at;
	}

	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}

	public LocalDateTime getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(LocalDateTime updated_at) {
		this.updated_at = updated_at;
	}
	
	
	
}
