package com.restaurantetech.inventory.service;

import com.restaurantetech.inventory.dto.CreateInventoryRequest;
import com.restaurantetech.inventory.dto.DecrementStockRequest;
import com.restaurantetech.inventory.model.InventoryItem;
import com.restaurantetech.inventory.repository.InventoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public InventoryItem create(CreateInventoryRequest request) {
        if (inventoryRepository.findByDishId(request.getDishId()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe inventario registrado para el plato con id " + request.getDishId() + "."
            );
        }

        InventoryItem item = new InventoryItem();
        item.setDishId(request.getDishId());
        item.setStockQuantity(request.getStockQuantity());
        return inventoryRepository.save(item);
    }

    public List<InventoryItem> findAll() {
        return inventoryRepository.findAll();
    }

    public InventoryItem findByDishId(Long dishId) {
        return inventoryRepository.findByDishId(dishId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe inventario para el plato con id " + dishId + "."
                ));
    }

    public InventoryItem decrementStock(Long dishId, DecrementStockRequest request) {
        InventoryItem item = findByDishId(dishId);

        if (item.getStockQuantity() < request.getQuantity()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Stock insuficiente para el plato con id " + dishId
                            + ". Disponible: " + item.getStockQuantity()
                            + ", solicitado: " + request.getQuantity() + "."
            );
        }

        item.setStockQuantity(item.getStockQuantity() - request.getQuantity());
        return inventoryRepository.save(item);
    }
}
