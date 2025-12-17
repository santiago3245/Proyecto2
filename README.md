# Sistema de Gestión de Inventario y Órdenes de Compra

Sistema completo con arquitectura de microservicios desarrollado con Spring Boot 3, Java 17 y React.

## 📋 Descripción

Sistema empresarial para la gestión integral de:
- **Catálogo de productos** con categorías y precios
- **Proveedores** nacionales e internacionales
- **Inventario** en múltiples bodegas
- **Órdenes de compra** con actualización automática de inventario

## 🏗️ Arquitectura

### Microservicios Backend (Spring Boot 3 + Java 17)

1. **producto-service** (Puerto 8081)
   - CRUD completo de productos
   - Validaciones con Bean Validation
   - Base de datos independiente

2. **proveedor-service** (Puerto 8082)
   - Gestión de proveedores
   - Control por país y estado
   - Base de datos independiente

3. **inventario-service** (Puerto 8083)
   - Control de stock por bodega
   - Comunicación con producto-service (Feign Client)
   - Operaciones de entrada/salida de stock

4. **orden-compra-service** (Puerto 8084)
   - Gestión completa de órdenes de compra
   - Comunicación con producto, proveedor e inventario services
   - Actualización automática de inventario al recibir orden
   - Estados: PENDIENTE → APROBADA → RECIBIDA / CANCELADA

### Frontend (React 18 + Vite)

- SPA con React Router
- Consumo de APIs REST
- Dashboard con estadísticas
- CRUD completo de todas las entidades
- Interfaz responsive

## 🚀 Tecnologías Utilizadas

### Backend
- Java 17
- Spring Boot 3.4.12
- Spring Data JPA
- Spring Cloud OpenFeign
- MySQL 8.0
- Maven
- Bean Validation

### Frontend
- React 18
- Vite
- React Router Dom
- Axios
- CSS3

### DevOps
- Docker
- Docker Compose
- Nginx

## 📦 Estructura del Proyecto

```
proyecto 2.0/
├── producto-service/          # Microservicio de productos
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── proveedor-service/         # Microservicio de proveedores
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── inventario-service/        # Microservicio de inventario
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── orden-compra-service/      # Microservicio de órdenes
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── frontend/                  # Aplicación React
│   ├── src/
│   ├── Dockerfile
│   ├── nginx.conf
│   └── package.json
├── docker-compose.yml         # Orquestación completa
└── README.md
```

## 🔧 Instalación y Ejecución

### Opción 1: Con Docker Compose (Recomendado)

```bash
# Clonar o navegar al proyecto
cd "proyecto 2.0"

# Levantar todos los servicios
docker-compose up -d

# Ver logs
docker-compose logs -f

# Detener servicios
docker-compose down
```

### Opción 2: Ejecución Local

#### Backend (cada microservicio)

```bash
cd producto-service
./mvnw spring-boot:run

cd proveedor-service
./mvnw spring-boot:run

cd inventario-service
./mvnw spring-boot:run

cd orden-compra-service
./mvnw spring-boot:run
```

#### Frontend

```bash
cd frontend
npm install
npm run dev
```

## 🌐 URLs de Acceso

| Servicio | URL | Descripción |
|----------|-----|-------------|
| Frontend | http://localhost:3000 | Aplicación web |
| Producto Service | http://localhost:8081 | API de productos |
| Proveedor Service | http://localhost:8082 | API de proveedores |
| Inventario Service | http://localhost:8083 | API de inventario |
| Orden Compra Service | http://localhost:8084 | API de órdenes |
| MySQL | localhost:3306 | Base de datos |

## 📚 Endpoints Principales

### Producto Service (8081)
```
GET    /productos
POST   /productos
GET    /productos/{id}
PUT    /productos/{id}
DELETE /productos/{id}
GET    /productos/codigo/{codigo}
GET    /productos/categoria/{categoria}
GET    /productos/estado/{estado}
```

### Proveedor Service (8082)
```
GET    /proveedores
POST   /proveedores
GET    /proveedores/{id}
PUT    /proveedores/{id}
DELETE /proveedores/{id}
GET    /proveedores/ruc/{ruc}
GET    /proveedores/pais/{pais}
GET    /proveedores/estado/{estado}
```

### Inventario Service (8083)
```
GET    /inventario
POST   /inventario
GET    /inventario/{id}
PUT    /inventario/{id}
DELETE /inventario/{id}
GET    /inventario/producto/{productoId}
GET    /inventario/bodega/{bodega}
POST   /inventario/agregar-stock
POST   /inventario/retirar-stock
```

### Orden Compra Service (8084)
```
GET    /ordenes-compra
POST   /ordenes-compra
GET    /ordenes-compra/{id}
PUT    /ordenes-compra/{id}
DELETE /ordenes-compra/{id}
GET    /ordenes-compra/numero/{numeroOrden}
GET    /ordenes-compra/proveedor/{proveedorId}
GET    /ordenes-compra/estado/{estado}
PUT    /ordenes-compra/{id}/aprobar
PUT    /ordenes-compra/{id}/recibir
PUT    /ordenes-compra/{id}/cancelar
```

## 🔍 Características Principales

### Validaciones
- Campos obligatorios y formatos válidos
- Números positivos y rangos
- Validación de unicidad (códigos, RUC, números de orden)
- Manejo de errores centralizado con @RestControllerAdvice

### Comunicación entre Microservicios
- Uso de Spring Cloud OpenFeign
- Validación de existencia de entidades relacionadas
- Manejo de errores de comunicación

### Funcionalidades de Negocio
- **Productos:** Gestión completa con categorización
- **Proveedores:** Control por país y estado
- **Inventario:** Múltiples bodegas, control de stock disponible/reservado
- **Órdenes de Compra:**
  - Creación con múltiples detalles
  - Flujo: Pendiente → Aprobada → Recibida
  - Actualización automática de inventario al recibir
  - Cálculo automático de totales

### Frontend
- Dashboard con estadísticas en tiempo real
- Formularios con validación
- Tablas interactivas
- Mensajes de éxito/error
- Diseño responsive
- Navegación con React Router

## 🗃️ Base de Datos

### Modelo de Datos

**producto**
- id, codigo, nombre, descripcion, categoria, precio
- unidad_medida, stock_minimo, estado
- fecha_creacion, fecha_actualizacion

**proveedor**
- id, ruc, razon_social, nombre_comercial
- pais, direccion, telefono, email, contacto, estado
- fecha_registro, fecha_actualizacion

**inventario**
- id, producto_id, bodega, ubicacion
- cantidad_disponible, cantidad_reservada
- fecha_ultima_entrada, fecha_ultima_salida
- fecha_actualizacion

**orden_compra**
- id, numero_orden, proveedor_id, bodega_destino
- fecha_orden, fecha_entrega_esperada, fecha_recepcion
- estado, total, observaciones

**detalle_orden_compra**
- id, orden_compra_id, producto_id
- cantidad, precio_unitario, subtotal

## 📋 Principios Aplicados

### Arquitectura
- ✅ Database per Service
- ✅ API Gateway Pattern (Frontend como punto de entrada)
- ✅ Service Discovery (mediante configuración)
- ✅ Circuit Breaker (manejo de errores)

### Desarrollo
- ✅ RESTful API Design
- ✅ Separación por capas (Controller, Service, Repository)
- ✅ DTOs para comunicación entre servicios
- ✅ Validaciones con Bean Validation
- ✅ Manejo centralizado de excepciones
- ✅ Códigos HTTP correctos (200, 201, 400, 404, 409)

### DevOps
- ✅ Containerización con Docker
- ✅ Orquestación con Docker Compose
- ✅ Variables de entorno
- ✅ Health checks
- ✅ Restart policies

## 🎯 Casos de Uso

### 1. Registro de Producto
1. Crear producto con código único
2. Asignar categoría, precio y stock mínimo
3. Validar campos obligatorios

### 2. Alta de Proveedor
1. Registrar proveedor con RUC único
2. Incluir datos de contacto
3. Definir país de origen

### 3. Creación de Orden de Compra
1. Seleccionar proveedor activo
2. Agregar productos con cantidades
3. Especificar bodega de destino
4. Sistema calcula totales automáticamente

### 4. Recepción de Mercadería
1. Orden en estado APROBADA
2. Ejecutar acción "Recibir"
3. Sistema actualiza inventario automáticamente
4. Orden pasa a estado RECIBIDA

## 🔒 Seguridad y Buenas Prácticas

- Validación de entrada en todos los endpoints
- Manejo de errores consistente
- Logs de debugging para SQL
- Configuración mediante variables de entorno
- Separación de responsabilidades
- Código limpio y documentado

## 🚀 Mejoras Futuras

- [ ] Implementar Spring Cloud Gateway
- [ ] Agregar Spring Cloud Config
- [ ] Implementar Eureka Service Discovery
- [ ] Agregar autenticación y autorización (Spring Security + JWT)
- [ ] Implementar Circuit Breaker con Resilience4j
- [ ] Agregar logging centralizado (ELK Stack)
- [ ] Implementar caché con Redis
- [ ] Agregar tests unitarios e integración
- [ ] Implementar paginación en listados
- [ ] Agregar reportes y analytics
- [ ] Implementar notificaciones
- [ ] Agregar auditoría de cambios

## 📝 Notas

- El sistema utiliza una única base de datos MySQL pero cada microservicio gestiona sus propias tablas
- La comunicación entre microservicios es síncrona mediante HTTP/REST
- El frontend consume directamente los microservicios
- Todos los servicios pueden escalar independientemente

## 👥 Equipo de Desarrollo

Proyecto desarrollado como parte del Sistema de Gestión de Inventario y Órdenes de Compra con arquitectura de microservicios.

## 📄 Licencia

Este proyecto es de código abierto y está disponible bajo la licencia MIT.
