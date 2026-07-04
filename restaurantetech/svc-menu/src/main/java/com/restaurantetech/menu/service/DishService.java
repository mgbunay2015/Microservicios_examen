package com.restaurantetech.menu.service;

import com.restaurantetech.menu.model.Dish;
import com.restaurantetech.menu.repository.DishRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class DishService {

    private final DishRepository dishRepository;

    public DishService(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    public Dish create(Dish dish) {
        dish.setId(null);
        if (dish.getAvailable() == null) {
            dish.setAvailable(true);
        }
        return dishRepository.save(dish);
    }

    public List<Dish> findAll() {
        return dishRepository.findAll();
    }

    public Dish findById(Long id) {
        return dishRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Plato no encontrado con id: " + id));
    }

    public Dish update(Long id, Dish updated) {
        Dish existing = findById(id);
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setCategory(updated.getCategory());
        existing.setPrice(updated.getPrice());
        if (updated.getAvailable() != null) {
            existing.setAvailable(updated.getAvailable());
        }
        return dishRepository.save(existing);
    }

    public void delete(Long id) {
        if (!dishRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Plato no encontrado con id: " + id);
        }
        dishRepository.deleteById(id);
    }
}
