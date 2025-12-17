# Orden Compra Service

Microservicio para la gestión de órdenes de compra.

## Endpoints

- GET /ordenes-compra - Obtener todas las órdenes de compra
- GET /ordenes-compra/{id} - Obtener orden por ID
- GET /ordenes-compra/numero/{numeroOrden} - Obtener orden por número
- GET /ordenes-compra/proveedor/{proveedorId} - Obtener órdenes por proveedor
- GET /ordenes-compra/estado/{estado} - Obtener órdenes por estado
- POST /ordenes-compra - Crear nueva orden de compra
- PUT /ordenes-compra/{id} - Actualizar orden de compra
- DELETE /ordenes-compra/{id} - Eliminar orden de compra
- PUT /ordenes-compra/{id}/aprobar - Aprobar orden de compra
- PUT /ordenes-compra/{id}/recibir - Recibir orden de compra (actualiza inventario automáticamente)
- PUT /ordenes-compra/{id}/cancelar - Cancelar orden de compra

## Puerto
8084

## Comunicación con otros microservicios
- Se comunica con producto-service para validar productos (Feign Client)
- Se comunica con proveedor-service para validar proveedores (Feign Client)
- Se comunica con inventario-service para actualizar stock al recibir orden (Feign Client)
