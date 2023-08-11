package com.onlineApplication.orderservice.controller;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.onlineApplication.orderservice.dto.OrderRequest;
import com.onlineApplication.orderservice.service.OrderService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

@RestController
@RequestMapping("/api/order")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod" )
	@TimeLimiter(name = "inventory")
	@Retry(name = "inventory")
	public CompletableFuture<String> placeOrder(@RequestBody OrderRequest orderRquest) {
		return CompletableFuture.supplyAsync(()-> orderService.placeOrder(orderRquest));
		
	}
	
	public CompletableFuture<String> fallbackMethod(OrderRequest orderRquest, RuntimeException runrtimeException) {
		return CompletableFuture.supplyAsync(()->"Oops! Somthing went wrong, please order after sometime!");
	}

}
