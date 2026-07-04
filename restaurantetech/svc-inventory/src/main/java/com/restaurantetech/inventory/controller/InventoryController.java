package com.restaurantetech.inventory.controller;

import com.restaurantetech.inventory.dto.CreateInventoryRequest;
import com.restaurantetech.inventory.dto.DecrementStockRequest;
import com.restaurantetech.inventory.model.InventoryItem;
import com.restaurantetech.inventory.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/inventario")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryItem create(@Valid @RequestBody CreateInventoryRequest request) {
        return inventoryService.create(request);
    }

    @GetMapping
    public List<InventoryItem> findAll() {
        return inventoryService.findAll();
    }

    @GetMapping("/{dishId}")
    public InventoryItem findByDishId(@PathVariable Long dishId) {
        return inventoryService.findByDishId(dishId);
    }

    @PutMapping("/{dishId}")
    public InventoryItem decrementStock(
            @PathVariable Long dishId,
            @Valid @RequestBody DecrementStockRequest request) {
        return inventoryService.decrementStock(dishId, request);
    }
}
