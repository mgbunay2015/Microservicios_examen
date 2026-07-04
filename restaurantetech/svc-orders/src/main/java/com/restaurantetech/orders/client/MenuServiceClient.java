package com.restaurantetech.orders.client;

import com.restaurantetech.orders.dto.DishResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

@Component
public class MenuServiceClient {

    private final WebClient menuWebClient;

    public MenuServiceClient(WebClient menuWebClient) {
        this.menuWebClient = menuWebClient;
    }

    public DishResponse getDishById(Long dishId) {
        try {
            return menuWebClient.get()
                    .uri("/api/menu/platos/{id}", dishId)
                    .retrieve()
                    .bodyToMono(DishResponse.class)
                    .block();
        } catch (WebClientResponseException.NotFound ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "El plato con id " + dishId + " no existe en el menú."
            );
        } catch (WebClientResponseException ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Error al consultar el servicio de menú: " + ex.getMessage()
            );
        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "No se pudo conectar con el servicio de menú."
            );
        }
    }
}
