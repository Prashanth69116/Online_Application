package com.onlineApplication.orderservice.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.onlineApplication.orderservice.dto.InventoryResponse;
import com.onlineApplication.orderservice.dto.OrderLineItemsDto;
import com.onlineApplication.orderservice.dto.OrderRequest;
import com.onlineApplication.orderservice.model.Order;
import com.onlineApplication.orderservice.model.OrderLineItems;
import com.onlineApplication.orderservice.repository.OrderRepository;

@Service
@Transactional
public class OrderService {
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private WebClient.Builder webClientBuilder;
	
	public String placeOrder(OrderRequest orderRequest) {
		Order order = new Order();
		order.setOrderNumer(UUID.randomUUID().toString());
		
		List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
				.stream()
				.map(this::mapToDto)
				.toList();
		order.setOrderLineItemsList(orderLineItems);
		
		List<String> skuCodes = order.getOrderLineItemsList().stream()
				.map(OrderLineItems::getSkuCode)
				.toList();
		
		//Call Inventory Service, and place order if product is in
		//stock
		
		InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
			.uri("http://inventory-service/api/inventory",
					uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
			.retrieve()
			.bodyToMono(InventoryResponse[].class)
			.block();
		
		boolean allProductsInStock = Arrays.stream(inventoryResponseArray)
				.allMatch(InventoryResponse::isInStock);
		if(allProductsInStock) {
			orderRepository.save(order);
			return "Order Placed Successfully";
		} else {
			throw new IllegalArgumentException("Product is not in stock, please try again later");
		}
		
		
	}
	private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
		OrderLineItems orderLineItems = new OrderLineItems();
		orderLineItems.setPrice(orderLineItemsDto.getPrice());
		orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
		orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
		return orderLineItems;
		
		}

}
