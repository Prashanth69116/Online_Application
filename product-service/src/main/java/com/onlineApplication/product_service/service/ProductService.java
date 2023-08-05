package com.onlineApplication.product_service.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlineApplication.product_service.dto.ProductRequest;
import com.onlineApplication.product_service.dto.ProductResponse;
import com.onlineApplication.product_service.model.Product;
import com.onlineApplication.product_service.repository.ProductRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	public void createProduct(ProductRequest productRequest) {
		Product product = Product.builder().name(productRequest.getName()).description(productRequest.getDescription())
				.price(productRequest.getPrice()).build();

		productRepository.save(product);
		log.info("Product {} is saved ",product.getId());

	}

	public List<ProductResponse> getAllProducts() {
		List<Product> product = productRepository.findAll();
		return product.stream().map(this::mapToProductResponse).toList();	
	}
	private ProductResponse mapToProductResponse(Product product) {
		return ProductResponse.builder()
				.id(product.getId())
				.name(product.getName())
				.description(product.getDescription())
				.price(product.getPrice())
				.build();
	}
}
