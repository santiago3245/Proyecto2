# Orden de Compra Service

Microservicio de gestión de órdenes de compra con Spring Boot 3.4.0 y OpenFeign.

## Características

- **Gestión de Órdenes de Compra**: Crear, actualizar, aprobar, recibir y cancelar órdenes
- **Integración con Microservicios**: Comunicación con producto-service, proveedor-service e inventario-service mediante OpenFeign
- **Validación de Capacidad**: Verifica la capacidad de la bodega antes de aprobar órdenes
- **Gestión de Estados**: PENDIENTE → APROBADA → RECIBIDA / CANCELADA
- **Reportes**: Historial, gastos por proveedor y estadísticas

## Tecnologías

- Spring Boot 3.4.0
- Spring Data JPA
- Spring Cloud OpenFeign
- PostgreSQL
- Maven

## Puerto

El servicio se ejecuta en el puerto **8084**

## Endpoints

### Órdenes de Compra

- `GET /api/ordenes-compra` - Listar todas las órdenes
- `GET /api/ordenes-compra/{id}` - Obtener orden por ID
- `POST /api/ordenes-compra` - Crear nueva orden
- `PUT /api/ordenes-compra/{id}` - Actualizar orden
- `PUT /api/ordenes-compra/{id}/aprobar` - Aprobar orden (valida capacidad)
- `PUT /api/ordenes-compra/{id}/recibir` - Recibir orden (agrega stock)
- `PUT /api/ordenes-compra/{id}/cancelar` - Cancelar orden
- `GET /api/ordenes-compra/estado/{estado}` - Filtrar por estado
- `GET /api/ordenes-compra/proveedor/{proveedorId}` - Filtrar por proveedor

### Reportes

- `GET /api/ordenes-compra/reportes/historial` - Historial completo
- `GET /api/ordenes-compra/reportes/gastos-por-proveedor` - Gastos por proveedor
- `GET /api/ordenes-compra/reportes/estadisticas` - Estadísticas generales

## Modelo de Datos

### OrdenCompra

```json
{
  "id": 1,
  "numeroOrden": "OC-000001",
  "proveedorId": 1,
  "proveedorNombre": "Proveedor ABC",
  "bodegaId": 1,
  "bodegaNombre": "Bodega Principal",
  "fechaCreacion": "2024-01-01T10:00:00",
  "fechaAprobacion": "2024-01-01T11:00:00",
  "fechaRecepcion": "2024-01-02T09:00:00",
  "estado": "RECIBIDA",
  "total": 1500.00,
  "observaciones": "Orden urgente",
  "detalles": [
    {
      "id": 1,
      "productoId": 1,
      "productoNombre": "Producto X",
      "cantidad": 10,
      "precioUnitario": 150.00,
      "subtotal": 1500.00
    }
  ]
}
```

### CrearOrdenRequest

```json
{
  "proveedorId": 1,
  "bodegaId": 1,
  "observaciones": "Orden urgente",
  "detalles": [
    {
      "productoId": 1,
      "cantidad": 10
    }
  ]
}
```

## Estados de Orden

- **PENDIENTE**: Orden creada, pendiente de aprobación
- **APROBADA**: Orden aprobada, lista para recepción
- **RECIBIDA**: Productos recibidos y agregados al inventario
- **CANCELADA**: Orden cancelada

## Lógica de Aprobación

Al aprobar una orden, el sistema:

1. Consulta la capacidad actual de la bodega vía `inventario-service`
2. Calcula la cantidad total de productos de la orden
3. Calcula el porcentaje proyectado: `((capacidadActual + cantidadOrden) / capacidadMaxima) * 100`
4. Si el porcentaje proyectado > 100%, rechaza la aprobación con mensaje detallado
5. Si hay capacidad, aprueba la orden

## Lógica de Recepción

Al recibir una orden:

1. Verifica que la orden esté en estado APROBADA
2. Llama a `inventario-service/agregar-stock` para cada detalle
3. Actualiza el estado a RECIBIDA

## Configuración

Variables de entorno en `.env`:

```properties
DB_URL=jdbc:postgresql://localhost:5432/ordencompra_db
DB_USERNAME=postgres
DB_PASSWORD=postgres
PRODUCTO_SERVICE_URL=http://localhost:8081
PROVEEDOR_SERVICE_URL=http://localhost:8082
INVENTARIO_SERVICE_URL=http://localhost:8083
```

## Ejecutar

### Con Maven

```bash
./mvnw spring-boot:run
```

### Con Docker

```bash
docker build -t orden-compra-service .
docker run -p 8084:8084 --env-file .env orden-compra-service
```

## Base de Datos

El servicio requiere una base de datos PostgreSQL llamada `ordencompra_db`. La configuración está en `application.properties`.

## Integración con otros servicios

- **producto-service (8081)**: Obtiene información de productos
- **proveedor-service (8082)**: Obtiene información de proveedores
- **inventario-service (8083)**: Valida capacidad de bodegas y agrega stock
