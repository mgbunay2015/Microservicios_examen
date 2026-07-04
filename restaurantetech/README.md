# RestauranteTech — Microservicios

Sistema de gestión de menú y pedidos para una cadena de restaurantes, implementado con arquitectura de microservicios.

## Arquitectura

```
Cliente (Postman)
       ↓
[Nginx API Gateway — :8080]
       ↓              ↓                    ↓
[svc-menu:8081]  [svc-orders:8082]  [svc-inventory:8083]
                        ↓                    ↓
                 consulta → [svc-menu]  consulta → [svc-inventory]
```

| Componente     | Tecnología              | Puerto |
|----------------|-------------------------|--------|
| API Gateway    | Nginx                   | 8080   |
| svc-menu       | Spring Boot + PostgreSQL| 8081   |
| svc-orders     | Spring Boot + PostgreSQL| 8082   |
| svc-inventory  | Spring Boot + PostgreSQL| 8083   |
| db_menu        | PostgreSQL              | 5433   |
| db_orders      | PostgreSQL              | 5434   |
| db_inventory   | PostgreSQL              | 5435   |

## Requisitos

- Docker Desktop
- Docker Compose

## Ejecución

```bash
cd restaurantetech
docker-compose up --build
```

Esperar a que todos los contenedores estén activos. El único punto de entrada externo es **http://localhost:8080**.

## Endpoints (vía API Gateway)

### svc-menu — `/api/menu/dishes`

| Método | Ruta                        | Descripción        |
|--------|-----------------------------|--------------------|
| POST   | `/api/menu/dishes`          | Crear plato        |
| GET    | `/api/menu/dishes`          | Listar platos      |
| GET    | `/api/menu/dishes/{id}`     | Obtener plato      |
| PUT    | `/api/menu/dishes/{id}`     | Actualizar plato   |
| DELETE | `/api/menu/dishes/{id}`     | Eliminar plato     |

**Ejemplo — crear plato:**
```json
POST http://localhost:8080/api/menu/dishes
{
  "name": "Lomo saltado",
  "description": "Plato típico peruano",
  "category": "PLATO_FUERTE",
  "price": 15.50,
  "available": true
}
```

Categorías válidas: `ENTRADA`, `PLATO_FUERTE`, `POSTRE`, `BEBIDA`.

### svc-orders — `/api/orders`

| Método | Ruta                | Descripción      |
|--------|---------------------|------------------|
| POST   | `/api/orders`       | Crear pedido     |
| GET    | `/api/orders`       | Listar pedidos   |
| GET    | `/api/orders/{id}`  | Obtener pedido   |

**Ejemplo — crear pedido válido:**
```json
POST http://localhost:8080/api/orders
{
  "customerName": "Mesa 5",
  "dishId": 1,
  "quantity": 2
}
```

El servicio consulta `svc-menu` para validar el plato, verificar disponibilidad y calcular `total = price × quantity`. También consulta `svc-inventory` para verificar stock y decrementarlo al confirmar el pedido.

### svc-inventory — `/api/inventory` (Punto extra)

| Método | Ruta                    | Descripción                          |
|--------|-------------------------|--------------------------------------|
| POST   | `/api/inventory`        | Registrar stock de un plato          |
| GET    | `/api/inventory`        | Listar todo el inventario            |
| GET    | `/api/inventory/{dishId}` | Consultar stock de un plato        |
| PUT    | `/api/inventory/{dishId}` | Decrementar stock                  |

**Ejemplo — registrar inventario:**
```json
POST http://localhost:8080/api/inventory
{
  "dishId": 1,
  "stockQuantity": 10
}
```

**Flujo al crear pedido:**
1. `svc-orders` consulta `svc-menu` → valida plato y obtiene precio.
2. `svc-orders` consulta `svc-inventory` → verifica stock suficiente.
3. `svc-orders` ejecuta `PUT /api/inventory/{dishId}` → decrementa stock.
4. Se guarda el pedido con el total calculado.

### Escenarios de error

| Caso                         | Código | Mensaje                                              |
|------------------------------|--------|------------------------------------------------------|
| Plato no existe              | 404    | `El plato con id {dishId} no existe en el menú.`     |
| Plato no disponible          | 400    | `El plato '{name}' no está disponible actualmente.`  |
| Stock insuficiente           | 400    | `Stock insuficiente para el plato '{name}'. Disponible: X, solicitado: Y.` |
| Sin inventario registrado    | 404    | `No existe inventario registrado para el plato con id {dishId}.` |

## Demostración en Postman

1. Crear uno o más platos con `POST /api/menu/dishes`.
2. Registrar inventario con `POST /api/inventory` para cada plato.
3. Crear un pedido válido con `POST /api/orders` (plato existente, disponible y con stock).
4. Crear un pedido con cantidad mayor al stock → debe retornar **400**.
5. Crear un pedido con un `dishId` inexistente → debe retornar **404**.
6. Actualizar un plato con `available: false` y crear un pedido → debe retornar **400**.
7. Listar pedidos con `GET /api/orders`.

## Integrantes

- [Nombre estudiante 1]
- [Nombre estudiante 2]

## Repositorio

https://github.com/mgbunay2015/Microservicios_examen

## Video de demostración

Archivo en el repositorio: `video/RestauranteTech_Demo_Completa.mp4`
