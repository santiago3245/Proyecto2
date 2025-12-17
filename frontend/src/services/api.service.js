import { productoAPI, proveedorAPI, inventarioAPI, ordenCompraAPI } from '../config/api'

// ========== PRODUCTOS ==========
export const getProductos = () => productoAPI.get('/')
export const getProducto = (id) => productoAPI.get(`/${id}`)
export const createProducto = (data) => productoAPI.post('/', data)
export const updateProducto = (id, data) => productoAPI.put(`/${id}`, data)
export const deleteProducto = (id) => productoAPI.delete(`/${id}`)

// ========== PROVEEDORES ==========
export const getProveedores = () => proveedorAPI.get('/')
export const getProveedor = (id) => proveedorAPI.get(`/${id}`)
export const createProveedor = (data) => proveedorAPI.post('/', data)
export const updateProveedor = (id, data) => proveedorAPI.put(`/${id}`, data)
export const deleteProveedor = (id) => proveedorAPI.delete(`/${id}`)

// ========== BODEGAS ==========
export const getBodegas = () => inventarioAPI.get('/bodegas')
export const getBodega = (id) => inventarioAPI.get(`/bodegas/${id}`)
export const createBodega = (data) => inventarioAPI.post('/bodegas', data)
export const updateBodega = (id, data) => inventarioAPI.put(`/bodegas/${id}`, data)
export const deleteBodega = (id) => inventarioAPI.delete(`/bodegas/${id}`)

// ========== INVENTARIO ==========
export const getInventarios = () => inventarioAPI.get('/inventarios')
export const getInventario = (id) => inventarioAPI.get(`/inventarios/${id}`)
export const getInventarioPorProducto = (productoId) => inventarioAPI.get(`/inventarios/producto/${productoId}`)
export const getInventarioPorBodega = (bodegaId) => inventarioAPI.get(`/inventarios/bodega/${bodegaId}`)
export const createInventario = (data) => inventarioAPI.post('/inventarios', data)
export const updateInventario = (id, data) => inventarioAPI.put(`/inventarios/${id}`, data)
export const actualizarStock = (id, cantidad) => inventarioAPI.patch(`/inventarios/${id}/stock`, null, {
  params: { cantidad }
})

// ========== ÓRDENES DE COMPRA ==========
export const getOrdenesCompra = () => ordenCompraAPI.get('/')
export const getOrdenCompra = (id) => ordenCompraAPI.get(`/${id}`)
export const createOrdenCompra = (data) => ordenCompraAPI.post('/', data)
export const updateOrdenCompra = (id, data) => ordenCompraAPI.put(`/${id}`, data)
export const deleteOrdenCompra = (id) => ordenCompraAPI.delete(`/${id}`)
export const aprobarOrdenCompra = (id) => ordenCompraAPI.post(`/${id}/aprobar`)
export const cancelarOrdenCompra = (id) => ordenCompraAPI.post(`/${id}/cancelar`)
export const recibirOrdenCompra = (id) => ordenCompraAPI.post(`/${id}/recibir`)

// ========== DASHBOARD / REPORTES ==========
export const getProductosCriticos = (stockMinimo = 10) => 
  inventarioAPI.get('/inventarios/criticos', { params: { stockMinimo } })

export const getGastosPorProveedor = async () => {
  try {
    const response = await ordenCompraAPI.get('/')
    const ordenes = response.data
    
    const proveedoresMap = {}
    
    for (const orden of ordenes) {
      if (orden.estado !== 'CANCELADA') {
        if (!proveedoresMap[orden.proveedorId]) {
          proveedoresMap[orden.proveedorId] = {
            proveedorId: orden.proveedorId,
            total: 0,
            cantidadOrdenes: 0
          }
        }
        proveedoresMap[orden.proveedorId].total += orden.total || 0
        proveedoresMap[orden.proveedorId].cantidadOrdenes += 1
      }
    }
    
    return Object.values(proveedoresMap)
  } catch (error) {
    console.error('Error calculando gastos por proveedor:', error)
    return []
  }
}

export const getEstadisticas = async () => {
  try {
    const [productosRes, proveedoresRes, bodegasRes, inventariosRes, ordenesRes] = await Promise.all([
      getProductos(),
      getProveedores(),
      getBodegas(),
      getInventarios(),
      getOrdenesCompra()
    ])
    
    const totalProductos = productosRes.data.length
    const totalProveedores = proveedoresRes.data.length
    const totalBodegas = bodegasRes.data.length
    const totalInventario = inventariosRes.data.reduce((sum, inv) => sum + inv.cantidad, 0)
    const ordenesPendientes = ordenesRes.data.filter(o => o.estado === 'PENDIENTE').length
    
    return {
      totalProductos,
      totalProveedores,
      totalBodegas,
      totalInventario,
      ordenesPendientes
    }
  } catch (error) {
    console.error('Error obteniendo estadísticas:', error)
    return {
      totalProductos: 0,
      totalProveedores: 0,
      totalBodegas: 0,
      totalInventario: 0,
      ordenesPendientes: 0
    }
  }
}
