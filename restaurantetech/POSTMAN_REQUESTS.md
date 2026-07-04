# RestauranteTech — Sentencias Postman

**Base URL (API Gateway):** `http://localhost:8080`

> Importar también el archivo `RestauranteTech_Postman_Collection.json` en Postman: **Import → Upload Files**.

---

## svc-menu — Platos

### POST — Crear plato (Lomo saltado)
```
POST http://localhost:8080/api/menu/dishes
Content-Type: application/json

{
  "name": "Lomo saltado",
  "description": "Plato típico peruano con arroz y papas fritas",
  "category": "PLATO_FUERTE",
  "price": 15.50,
  "available": true
}
```
**Respuesta esperada:** `201 Created`

---

### POST — Crear plato (Ceviche - Entrada)
```
POST http://localhost:8080/api/menu/dishes
Content-Type: application/json

{
  "name": "Ceviche",
  "description": "Pescado fresco marinado en limón",
  "category": "ENTRADA",
  "price": 12.00,
  "available": true
}
```

---

### POST — Crear plato (Suspiro limeño - Postre)
```
POST http://localhost:8080/api/menu/dishes
Content-Type: application/json

{
  "name": "Suspiro limeño",
  "description": "Postre tradicional peruano",
  "category": "POSTRE",
  "price": 8.50,
  "available": true
}
```

---

### POST — Crear plato (Chicha morada - Bebida)
```
POST http://localhost:8080/api/menu/dishes
Content-Type: application/json

{
  "name": "Chicha morada",
  "description": "Bebida tradicional de maíz morado",
  "category": "BEBIDA",
  "price": 4.00,
  "available": true
}
```

**Categorías válidas:** `ENTRADA`, `PLATO_FUERTE`, `POSTRE`, `BEBIDA`

---

### GET — Listar todos los platos
```
GET http://localhost:8080/api/menu/dishes
```
**Respuesta esperada:** `200 OK`

---

### GET — Obtener plato por ID
```
GET http://localhost:8080/api/menu/dishes/1
```
**Respuesta esperada:** `200 OK` o `404 Not Found`

---

### PUT — Actualizar plato
```
PUT http://localhost:8080/api/menu/dishes/1
Content-Type: application/json

{
  "name": "Lomo saltado especial",
  "description": "Versión premium con más porción",
  "category": "PLATO_FUERTE",
  "price": 18.00,
  "available": true
}
```
**Respuesta esperada:** `200 OK`

---

### PUT — Marcar plato como NO disponible
```
PUT http://localhost:8080/api/menu/dishes/2
Content-Type: application/json

{
  "name": "Ceviche",
  "description": "Pescado fresco marinado en limón",
  "category": "ENTRADA",
  "price": 12.00,
  "available": false
}
```
**Usar antes de probar error 400 en pedidos.**

---

### DELETE — Eliminar plato
```
DELETE http://localhost:8080/api/menu/dishes/1
```
**Respuesta esperada:** `204 No Content`

---

## svc-inventory — Inventario (Punto extra)

### POST — Registrar inventario plato 1
```
POST http://localhost:8080/api/inventory
Content-Type: application/json

{
  "dishId": 1,
  "stockQuantity": 10
}
```
**Respuesta esperada:** `201 Created`

---

### POST — Registrar inventario plato 2
```
POST http://localhost:8080/api/inventory
Content-Type: application/json

{
  "dishId": 2,
  "stockQuantity": 5
}
```

---

### POST — Registrar inventario plato 3
```
POST http://localhost:8080/api/inventory
Content-Type: application/json

{
  "dishId": 3,
  "stockQuantity": 8
}
```

---

### GET — Listar todo el inventario
```
GET http://localhost:8080/api/inventory
```
**Respuesta esperada:** `200 OK`

---

### GET — Consultar stock por dishId
```
GET http://localhost:8080/api/inventory/1
```
**Respuesta esperada:** `200 OK`

---

### PUT — Decrementar stock manualmente
```
PUT http://localhost:8080/api/inventory/1
Content-Type: application/json

{
  "quantity": 2
}
```
**Respuesta esperada:** `200 OK` con stock actualizado

---

## svc-orders — Pedidos

### POST — Crear pedido válido
```
POST http://localhost:8080/api/orders
Content-Type: application/json

{
  "customerName": "Mesa 5",
  "dishId": 1,
  "quantity": 2
}
```
**Respuesta esperada:** `201 Created` con `total = price × quantity`  
**Ejemplo:** si price = 15.50 → total = 31.0

---

### POST — Crear pedido (Mesa 10)
```
POST http://localhost:8080/api/orders
Content-Type: application/json

{
  "customerName": "Mesa 10",
  "dishId": 1,
  "quantity": 1
}
```

---

### GET — Listar todos los pedidos
```
GET http://localhost:8080/api/orders
```
**Respuesta esperada:** `200 OK`

---

### GET — Obtener pedido por ID
```
GET http://localhost:8080/api/orders/1
```
**Respuesta esperada:** `200 OK` o `404 Not Found`

---

## Escenarios de ERROR (obligatorios en el video)

### ERROR 404 — Plato no existe en el menú
```
POST http://localhost:8080/api/orders
Content-Type: application/json

{
  "customerName": "Mesa 1",
  "dishId": 999,
  "quantity": 1
}
```
**Respuesta esperada:** `404`  
**Mensaje:** `"El plato con id 999 no existe en el menú."`

---

### ERROR 400 — Plato no disponible
```
POST http://localhost:8080/api/orders
Content-Type: application/json

{
  "customerName": "Mesa 2",
  "dishId": 2,
  "quantity": 1
}
```
> Ejecutar antes el PUT que marca el plato 2 con `"available": false`

**Respuesta esperada:** `400`  
**Mensaje:** `"El plato 'Ceviche' no está disponible actualmente."`

---

### ERROR 400 — Stock insuficiente
```
POST http://localhost:8080/api/orders
Content-Type: application/json

{
  "customerName": "Mesa 9",
  "dishId": 1,
  "quantity": 100
}
```
**Respuesta esperada:** `400`  
**Mensaje:** `"Stock insuficiente para el plato 'Lomo saltado'. Disponible: X, solicitado: 100."`

---

### ERROR 404 — Sin inventario registrado
```
POST http://localhost:8080/api/orders
Content-Type: application/json

{
  "customerName": "Mesa 7",
  "dishId": 4,
  "quantity": 1
}
```
> Crear plato 4 pero NO registrar inventario para él.

**Respuesta esperada:** `404`  
**Mensaje:** `"No existe inventario registrado para el plato con id 4."`

---

### ERROR 404 — Obtener plato inexistente
```
GET http://localhost:8080/api/menu/dishes/999
```

---

### ERROR 404 — Obtener pedido inexistente
```
GET http://localhost:8080/api/orders/999
```

---

## Flujo completo para la demostración (orden sugerido)

| # | Acción | Request |
|---|--------|---------|
| 1 | Crear plato | `POST /api/menu/dishes` (Lomo saltado) |
| 2 | Registrar inventario | `POST /api/inventory` `{ "dishId": 1, "stockQuantity": 10 }` |
| 3 | Pedido válido ✅ | `POST /api/orders` `{ "customerName": "Mesa 5", "dishId": 1, "quantity": 2 }` |
| 4 | Pedido inválido ❌ | `POST /api/orders` `{ "customerName": "Mesa 1", "dishId": 999, "quantity": 1 }` |
| 5 | Verificar stock | `GET /api/inventory/1` (debe mostrar 8 unidades) |
| 6 | Listar pedidos | `GET /api/orders` |

---

## Notas

- Todos los requests van por el **API Gateway** en puerto **8080**.
- No acceder directamente a los puertos 8081, 8082 ni 8083 desde Postman.
- Antes de crear pedidos, siempre registrar inventario con `POST /api/inventory`.
