package com.restaurantetech.orders.service;

import com.restaurantetech.orders.client.InventoryServiceClient;
import com.restaurantetech.orders.client.MenuServiceClient;
import com.restaurantetech.orders.dto.CreateOrderRequest;
import com.restaurantetech.orders.dto.DishResponse;
import com.restaurantetech.orders.dto.InventoryResponse;
import com.restaurantetech.orders.model.Order;
import com.restaurantetech.orders.model.OrderStatus;
import com.restaurantetech.orders.repository.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuServiceClient menuServiceClient;
    private final InventoryServiceClient inventoryServiceClient;

    public OrderService(
            OrderRepository orderRepository,
            MenuServiceClient menuServiceClient,
            InventoryServiceClient inventoryServiceClient) {
        this.orderRepository = orderRepository;
        this.menuServiceClient = menuServiceClient;
        this.inventoryServiceClient = inventoryServiceClient;
    }

    public Order create(CreateOrderRequest request) {
        DishResponse dish = menuServiceClient.getDishById(request.getDishId());

        if (Boolean.FALSE.equals(dish.getAvailable())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El plato '" + dish.getName() + "' no está disponible actualmente."
            );
        }

        InventoryResponse inventory = inventoryServiceClient.getInventoryByDishId(request.getDishId());

        if (inventory.getStockQuantity() < request.getQuantity()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Stock insuficiente para el plato '" + dish.getName()
                            + "'. Disponible: " + inventory.getStockQuantity()
                            + ", solicitado: " + request.getQuantity() + "."
            );
        }

        inventoryServiceClient.decrementStock(request.getDishId(), request.getQuantity());

        Order order = new Order();
        order.setCustomerName(request.getCustomerName());
        order.setDishId(request.getDishId());
        order.setQuantity(request.getQuantity());
        order.setTotal(dish.getPrice() * request.getQuantity());
        order.setStatus(OrderStatus.PENDING);

        return orderRepository.save(order);
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado con id: " + id));
    }
}
