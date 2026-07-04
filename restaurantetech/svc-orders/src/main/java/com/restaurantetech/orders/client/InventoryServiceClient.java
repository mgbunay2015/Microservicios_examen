package com.restaurantetech.orders.client;

import com.restaurantetech.orders.dto.InventoryResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Component
public class InventoryServiceClient {

    private final RestTemplate restTemplate;
    private final String inventoryServiceUrl;

    public InventoryServiceClient(
            RestTemplate restTemplate,
            @Value("${inventory.service.url}") String inventoryServiceUrl) {
        this.restTemplate = restTemplate;
        this.inventoryServiceUrl = inventoryServiceUrl;
    }

    public InventoryResponse getInventoryByDishId(Long dishId) {
        try {
            return restTemplate.getForObject(
                    inventoryServiceUrl + "/api/inventario/{dishId}",
                    InventoryResponse.class,
                    dishId
            );
        } catch (HttpClientErrorException.NotFound ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No existe inventario registrado para el plato con id " + dishId + "."
            );
        } catch (HttpClientErrorException ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Error al consultar el servicio de inventario: " + ex.getMessage()
            );
        } catch (RestClientException ex) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "No se pudo conectar con el servicio de inventario."
            );
        }
    }

    public void decrementStock(Long dishId, Integer quantity) {
        try {
            restTemplate.put(
                    inventoryServiceUrl + "/api/inventario/{dishId}",
                    Map.of("quantity", quantity),
                    dishId
            );
        } catch (HttpClientErrorException.BadRequest ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Stock insuficiente para el plato con id " + dishId + "."
            );
        } catch (HttpClientErrorException ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Error al actualizar el inventario: " + ex.getMessage()
            );
        } catch (RestClientException ex) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "No se pudo conectar con el servicio de inventario."
            );
        }
    }
}
