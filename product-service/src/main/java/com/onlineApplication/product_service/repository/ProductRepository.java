package com.onlineApplication.product_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.onlineApplication.product_service.dto.ProductRequest;
import com.onlineApplication.product_service.model.Product;

@Repository
public interface ProductRepository extends MongoRepository<Product, String>{

	Product save(ProductRequest productRequest);

}
