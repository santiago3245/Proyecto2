# GUÍA RÁPIDA - PRUEBA LOCAL DEL SISTEMA

## 1. LEVANTAR LOS SERVICIOS CON DOCKER

```powershell
# Ir a la carpeta del proyecto
cd 'c:\Users\santi\OneDrive\Documentos\test\proyecto 2.0'

# Levantar todos los servicios (descarga imágenes de Docker Hub)
docker-compose up -d

# Ver el estado de los contenedores
docker-compose ps

# Ver logs si hay algún error
docker-compose logs -f
```

**Espera 30-60 segundos** para que todos los servicios inicien correctamente.

## 2. VERIFICAR QUE LOS SERVICIOS ESTÁN CORRIENDO

Abre tu navegador y verifica cada servicio:

- ✅ **Producto Service**: http://localhost:8081/api/productos
- ✅ **Proveedor Service**: http://localhost:8082/api/proveedores
- ✅ **Inventario Service**: http://localhost:8083/api/bodegas
- ✅ **Orden Compra Service**: http://localhost:8084/api/ordenes-compra
- ✅ **Frontend**: http://localhost:3000

Si ves `[]` (array vacío) en los endpoints, significa que están funcionando pero sin datos todavía.

## 3. IMPORTAR LA COLECCIÓN EN POSTMAN

1. Abre Postman
2. Click en **"Import"** (esquina superior izquierda)
3. Selecciona el archivo: `Sistema_Inventario_V2.1_Postman_Collection.json`
4. Click en **"Import"**

## 4. FLUJO DE PRUEBA RECOMENDADO

### PASO 1: Crear un Producto
```
POST http://localhost:8081/api/productos
Body (JSON):
{
  "codigo": "PROD001",
  "nombre": "Laptop Dell Inspiron 15",
  "descripcion": "Laptop con procesador i7, 16GB RAM, 512GB SSD",
  "categoria": "ELECTRONICA",
  "precio": 850.00,
  "unidadMedida": "UNIDAD",
  "stockMinimo": 5,
  "estado": "ACTIVO"
}
```
**Guarda el `id` que devuelve** (ej: 1)

### PASO 2: Crear un Proveedor
```
POST http://localhost:8082/api/proveedores
Body (JSON):
{
  "ruc": "1234567890001",
  "razonSocial": "TechMax Distribuidora S.A.",
  "nombreComercial": "TechMax",
  "direccion": "Av. Amazonas N34-451",
  "ciudad": "Quito",
  "pais": "Ecuador",
  "telefono": "+593 2 2456789",
  "email": "ventas@techmax.com",
  "sitioWeb": "www.techmax.com",
  "categoria": "TECNOLOGIA",
  "estado": "ACTIVO"
}
```
**Guarda el `id` que devuelve** (ej: 1)

### PASO 3: Crear una Bodega
```
POST http://localhost:8083/api/bodegas
Body (JSON):
{
  "codigo": "BOD-001",
  "nombre": "Bodega Central",
  "direccion": "Av. Industrial km 5",
  "ciudad": "Quito",
  "telefono": "+593 2 3456789",
  "capacidadMaxima": 100,
  "unidadCapacidad": "m²",
  "estado": "ACTIVO"
}
```
**Guarda el `id` que devuelve** (ej: 1)

### PASO 4: Verificar Capacidad de la Bodega
```
GET http://localhost:8083/api/bodegas/1/capacidad
```
Deberías ver:
```json
{
  "bodegaId": 1,
  "bodegaNombre": "Bodega Central",
  "capacidadMaxima": 100,
  "unidadCapacidad": "m²",
  "cantidadProductos": 0,
  "porcentajeUtilizacion": 0.0,
  "nivelAlerta": "VERDE"
}
```

### PASO 5: Crear una Orden de Compra (estado PENDIENTE)
```
POST http://localhost:8084/api/ordenes-compra
Body (JSON):
{
  "proveedorId": 1,
  "bodegaId": 1,
  "observaciones": "Orden de compra trimestral",
  "detalles": [
    {
      "productoId": 1,
      "cantidad": 10,
      "precioUnitario": 850.00
    }
  ]
}
```
**Guarda el `id` de la orden** (ej: 1)

### PASO 6: Aprobar la Orden (valida capacidad)
```
PUT http://localhost:8084/api/ordenes-compra/1/aprobar
```
Si la capacidad es suficiente → ✅ Orden APROBADA
Si excede 100% → ❌ Error con mensaje detallado

### PASO 7: Recibir la Orden (actualiza inventario automáticamente)
```
PUT http://localhost:8084/api/ordenes-compra/1/recibir
```
Esto automáticamente:
- Cambia estado a RECIBIDA
- Agrega stock al inventario
- Crea ubicación "RECEPCION"

### PASO 8: Verificar el Inventario
```
GET http://localhost:8083/api/inventario
```
Deberías ver el producto con cantidad 10 en ubicación "RECEPCION"

### PASO 9: Ver Capacidad Actualizada
```
GET http://localhost:8083/api/bodegas/1/capacidad
```
Ahora verás:
```json
{
  "cantidadProductos": 10,
  "porcentajeUtilizacion": 10.0,
  "nivelAlerta": "VERDE"
}
```

### PASO 10: Ver Reportes
```
GET http://localhost:8084/api/ordenes-compra/reportes/estadisticas
GET http://localhost:8084/api/ordenes-compra/reportes/gastos-por-proveedor
GET http://localhost:8084/api/ordenes-compra/reportes/historial
```

## 5. PRUEBA DE VALIDACIÓN DE CAPACIDAD (V2.1)

### Crear orden que exceda el 100%:
```
POST http://localhost:8084/api/ordenes-compra
{
  "proveedorId": 1,
  "bodegaId": 1,
  "observaciones": "Orden grande",
  "detalles": [
    {
      "productoId": 1,
      "cantidad": 95,
      "precioUnitario": 850.00
    }
  ]
}
```

Intentar aprobar:
```
PUT http://localhost:8084/api/ordenes-compra/2/aprobar
```

**Resultado esperado**: Error 400 con mensaje:
```
"La bodega 'Bodega Central' no tiene capacidad suficiente. 
Capacidad actual: 10/100 m² (10.0%). 
Con esta orden llegaría a: 105 m² (105.0%). 
Por favor, seleccione otra bodega o edite la orden."
```

## 6. PROBAR EL FRONTEND

Abre: http://localhost:3000

Navega por:
- 📊 Dashboard (reportes y gráficos)
- 📦 Productos
- 🏢 Proveedores
- 🏭 Bodegas
- 📋 Inventario
- 🛒 Órdenes de Compra

## 7. COMANDOS ÚTILES

```powershell
# Ver logs de un servicio específico
docker logs producto-service
docker logs orden-compra-service

# Reiniciar un servicio
docker-compose restart producto-service

# Detener todo
docker-compose down

# Detener y borrar datos (⚠️ borra la base de datos)
docker-compose down -v

# Ver uso de recursos
docker stats
```

## 8. SOLUCIÓN DE PROBLEMAS

### Si un servicio no inicia:
```powershell
docker logs <nombre-servicio>
```

### Si MySQL no se conecta:
```powershell
docker-compose down
docker-compose up -d mysql
# Espera 30 segundos
docker-compose up -d
```

### Si el frontend no carga:
```powershell
docker-compose restart frontend
```

## 9. ACCESO A LA BASE DE DATOS

```powershell
docker exec -it inventario-mysql mysql -uroot -proot

# Dentro de MySQL:
USE inventariodb;
SHOW TABLES;
SELECT * FROM producto;
SELECT * FROM orden_compra;
```

## 10. ENDPOINTS CLAVE PARA TESTING

| Servicio | Puerto | Health Check |
|----------|--------|--------------|
| Producto | 8081 | GET /api/productos |
| Proveedor | 8082 | GET /api/proveedores |
| Inventario | 8083 | GET /api/bodegas |
| Orden Compra | 8084 | GET /api/ordenes-compra |
| Frontend | 3000 | http://localhost:3000 |

---

**¡LISTO PARA PROBAR!** 🚀
