package com.onlineApplication.Inventoryservice.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;


import com.onlineApplication.Inventoryservice.model.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long>{

	List<Inventory> findBySkuCodeIn(List<String> skuCode);
	

}
