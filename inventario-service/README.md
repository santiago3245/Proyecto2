# Inventario Service

Microservicio para la gestión de inventario en múltiples bodegas.

## Endpoints

- GET /inventario - Obtener todo el inventario
- GET /inventario/{id} - Obtener inventario por ID
- GET /inventario/producto/{productoId} - Obtener inventario por producto
- GET /inventario/bodega/{bodega} - Obtener inventario por bodega
- POST /inventario - Crear nuevo registro de inventario
- PUT /inventario/{id} - Actualizar inventario
- DELETE /inventario/{id} - Eliminar inventario
- POST /inventario/agregar-stock - Agregar stock a una bodega
- POST /inventario/retirar-stock - Retirar stock de una bodega

## Puerto
8083

## Comunicación con otros microservicios
- Se comunica con producto-service para validar productos (Feign Client)
