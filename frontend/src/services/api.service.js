import api from '../config/api';

export const productoService = {
  getAll: () => api.productos.get('/api/productos'),
  getById: (id) => api.productos.get(`/api/productos/${id}`),
  getByCodigo: (codigo) => api.productos.get(`/api/productos/codigo/${codigo}`),
  getByCategoria: (categoria) => api.productos.get(`/api/productos/categoria/${categoria}`),
  getByEstado: (estado) => api.productos.get(`/api/productos/estado/${estado}`),
  search: (nombre) => api.productos.get(`/api/productos/buscar`, { params: { nombre } }),
  getBajoStock: () => api.productos.get(`/api/productos/bajo-stock`),
  create: (producto) => api.productos.post('/api/productos', producto),
  update: (id, producto) => api.productos.put(`/api/productos/${id}`, producto),
  delete: (id) => api.productos.delete(`/api/productos/${id}`),
  // Reportes
  getProductosCriticos: () => api.productos.get('/api/productos/reportes/criticos'),
  getProductosBajoStock: () => api.productos.get('/api/productos/reportes/bajo-stock')
};

export const proveedorService = {
  getAll: () => api.proveedores.get('/api/proveedores'),
  getById: (id) => api.proveedores.get(`/api/proveedores/${id}`),
  getByRuc: (ruc) => api.proveedores.get(`/api/proveedores/ruc/${ruc}`),
  getByPais: (pais) => api.proveedores.get(`/api/proveedores/pais/${pais}`),
  getByEstado: (estado) => api.proveedores.get(`/api/proveedores/estado/${estado}`),
  getByCategoria: (categoria) => api.proveedores.get(`/api/proveedores/categoria/${categoria}`),
  search: (nombre) => api.proveedores.get(`/api/proveedores/buscar`, { params: { nombre } }),
  create: (proveedor) => api.proveedores.post('/api/proveedores', proveedor),
  update: (id, proveedor) => api.proveedores.put(`/api/proveedores/${id}`, proveedor),
  delete: (id) => api.proveedores.delete(`/api/proveedores/${id}`)
};

export const bodegaService = {
  getAll: () => api.inventario.get('/api/bodegas'),
  getById: (id) => api.inventario.get(`/api/bodegas/${id}`),
  getByCodigo: (codigo) => api.inventario.get(`/api/bodegas/codigo/${codigo}`),
  getByEstado: (estado) => api.inventario.get(`/api/bodegas/estado/${estado}`),
  getCapacidad: () => api.inventario.get('/api/bodegas/capacidad'),
  getCapacidadById: (id) => api.inventario.get(`/api/bodegas/${id}/capacidad`),
  create: (bodega) => api.inventario.post('/api/bodegas', bodega),
  update: (id, bodega) => api.inventario.put(`/api/bodegas/${id}`, bodega),
  delete: (id) => api.inventario.delete(`/api/bodegas/${id}`)
};

export const inventarioService = {
  getAll: () => api.inventario.get('/api/inventario'),
  getById: (id) => api.inventario.get(`/api/inventario/${id}`),
  getByProducto: (productoId) => api.inventario.get(`/api/inventario/producto/${productoId}`),
  getByBodega: (bodegaId) => api.inventario.get(`/api/inventario/bodega`, { params: { bodegaId } }),
  getByProductoAndBodega: (productoId, bodegaId) => api.inventario.get(`/api/inventario/producto/${productoId}/bodega/${bodegaId}`),
  getBajoStock: () => api.inventario.get(`/api/inventario/bajo-stock`),
  getSinStock: () => api.inventario.get(`/api/inventario/sin-stock`),
  search: (nombre) => api.inventario.get(`/api/inventario/buscar`, { params: { nombre } }),
  create: (inventario) => api.inventario.post('/api/inventario', inventario),
  update: (id, inventario) => api.inventario.put(`/api/inventario/${id}`, inventario),
  delete: (id) => api.inventario.delete(`/api/inventario/${id}`),
  agregarStock: (productoId, bodegaId, cantidad) => 
    api.inventario.post(`/api/inventario/agregar-stock`, null, { params: { productoId, bodegaId, cantidad } }),
  retirarStock: (productoId, bodegaId, cantidad) => 
    api.inventario.post(`/api/inventario/retirar-stock`, null, { params: { productoId, bodegaId, cantidad } })
};

export const ordenCompraService = {
  getAll: () => api.ordenesCompra.get('/api/ordenes-compra'),
  getById: (id) => api.ordenesCompra.get(`/api/ordenes-compra/${id}`),
  getByNumero: (numero) => api.ordenesCompra.get(`/api/ordenes-compra/numero/${numero}`),
  getByProveedor: (proveedorId) => api.ordenesCompra.get(`/api/ordenes-compra/proveedor/${proveedorId}`),
  getByEstado: (estado) => api.ordenesCompra.get(`/api/ordenes-compra/estado`, { params: { estado } }),
  getByBodega: (bodegaId) => api.ordenesCompra.get(`/api/ordenes-compra/bodega/${bodegaId}`),
  create: (orden) => api.ordenesCompra.post('/api/ordenes-compra', orden),
  update: (id, orden) => api.ordenesCompra.put(`/api/ordenes-compra/${id}`, orden),
  delete: (id) => api.ordenesCompra.delete(`/api/ordenes-compra/${id}`),
  aprobar: (id) => api.ordenesCompra.put(`/api/ordenes-compra/${id}/aprobar`),
  recibir: (id, detalles) => api.ordenesCompra.put(`/api/ordenes-compra/${id}/recibir`, detalles),
  cancelar: (id, motivo) => api.ordenesCompra.put(`/api/ordenes-compra/${id}/cancelar`, { motivo }),
  // Reportes
  getHistorial: () => api.ordenesCompra.get('/api/ordenes-compra/reportes/historial'),
  getGastosPorProveedor: () => api.ordenesCompra.get('/api/ordenes-compra/reportes/gastos-por-proveedor'),
  getEstadisticas: () => api.ordenesCompra.get('/api/ordenes-compra/reportes/estadisticas')
};
