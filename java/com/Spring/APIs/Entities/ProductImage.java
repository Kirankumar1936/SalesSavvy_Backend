package com.Spring.APIs.Entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "productimages")
public class ProductImage {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer image_id;
	
	@ManyToOne(fetch=FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="product_id",nullable =false)
	private Product product;
	
	@Column(nullable=false,columnDefinition = "TEXT")
	private String image_url;

	public ProductImage() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ProductImage(Integer imageId, Product product, String imageUrl) {
		super();
		this.image_id = imageId;
		this.product = product;
		this.image_url = imageUrl;
	}

	public ProductImage(Product product, String imageUrl) {
		super();
		this.product = product;
		this.image_url = imageUrl;
	}

	public Integer getImageId() {
		return image_id;
	}

	public void setImageId(Integer imageId) {
		this.image_id = imageId;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getImageUrl() {
		return image_url;
	}

	public void setImageUrl(String imageUrl) {
		this.image_url = imageUrl;
	}
	
	
	
	
}
