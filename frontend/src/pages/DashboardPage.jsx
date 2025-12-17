import { useState, useEffect } from 'react'
import { getEstadisticas, getProductosCriticos, getGastosPorProveedor, getOrdenesCompra, getProveedores } from '../services/api.service'
import '../styles/Dashboard.css'

function DashboardPage() {
  const [stats, setStats] = useState({
    totalProductos: 0,
    totalProveedores: 0,
    totalBodegas: 0,
    totalInventario: 0,
    ordenesPendientes: 0
  })
  const [productosCriticos, setProductosCriticos] = useState([])
  const [gastosPorProveedor, setGastosPorProveedor] = useState([])
  const [historialOrdenes, setHistorialOrdenes] = useState([])
  const [proveedoresMap, setProveedoresMap] = useState({})
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    loadDashboardData()
  }, [])

  const loadDashboardData = async () => {
    try {
      setLoading(true)
      
      // Cargar estadísticas
      const statsData = await getEstadisticas()
      setStats(statsData)
      
      // Cargar productos críticos
      const criticosRes = await getProductosCriticos(10)
      setProductosCriticos(criticosRes.data || [])
      
      // Cargar gastos por proveedor
      const gastosData = await getGastosPorProveedor()
      
      // Cargar proveedores para mostrar nombres
      const proveedoresRes = await getProveedores()
      const provMap = {}
      proveedoresRes.data.forEach(p => {
        provMap[p.id] = p.nombre
      })
      setProveedoresMap(provMap)
      setGastosPorProveedor(gastosData)
      
      // Cargar historial de órdenes
      const ordenesRes = await getOrdenesCompra()
      setHistorialOrdenes(ordenesRes.data.slice(0, 10))
      
    } catch (error) {
      console.error('Error cargando dashboard:', error)
    } finally {
      setLoading(false)
    }
  }

  if (loading) {
    return <div className="loading">Cargando dashboard...</div>
  }

  return (
    <div className="container">
      <h1>Dashboard</h1>
      
      {/* Estadísticas generales */}
      <div className="stats-grid">
        <div className="stat-card">
          <div className="stat-label">Total Productos</div>
          <div className="stat-value">{stats.totalProductos}</div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Total Proveedores</div>
          <div className="stat-value">{stats.totalProveedores}</div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Total Bodegas</div>
          <div className="stat-value">{stats.totalBodegas}</div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Items en Inventario</div>
          <div className="stat-value">{stats.totalInventario}</div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Órdenes Pendientes</div>
          <div className="stat-value" style={{ color: '#e74c3c' }}>{stats.ordenesPendientes}</div>
        </div>
      </div>

      {/* Productos con stock crítico */}
      <div className="chart-container">
        <h2>⚠️ Productos con Stock Crítico (menos de 10 unidades)</h2>
        {productosCriticos.length === 0 ? (
          <p style={{ color: '#27ae60', textAlign: 'center', padding: '20px' }}>
            ✓ No hay productos con stock crítico
          </p>
        ) : (
          <table>
            <thead>
              <tr>
                <th>Producto ID</th>
                <th>Bodega ID</th>
                <th>Cantidad</th>
                <th>Stock Mínimo</th>
              </tr>
            </thead>
            <tbody>
              {productosCriticos.map(item => (
                <tr key={`${item.productoId}-${item.bodegaId}`}>
                  <td>{item.productoId}</td>
                  <td>{item.bodegaId}</td>
                  <td style={{ color: '#e74c3c', fontWeight: 'bold' }}>{item.cantidad}</td>
                  <td>{item.stockMinimo}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>

      {/* Gastos por proveedor */}
      <div className="chart-container">
        <h2>💰 Gastos por Proveedor</h2>
        {gastosPorProveedor.length === 0 ? (
          <p style={{ textAlign: 'center', padding: '20px', color: '#7f8c8d' }}>
            No hay datos de gastos disponibles
          </p>
        ) : (
          <table>
            <thead>
              <tr>
                <th>Proveedor</th>
                <th>Cantidad de Órdenes</th>
                <th>Total Gastado</th>
              </tr>
            </thead>
            <tbody>
              {gastosPorProveedor.map(gasto => (
                <tr key={gasto.proveedorId}>
                  <td>{proveedoresMap[gasto.proveedorId] || `Proveedor #${gasto.proveedorId}`}</td>
                  <td>{gasto.cantidadOrdenes}</td>
                  <td>${gasto.total.toFixed(2)}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>

      {/* Historial de órdenes recientes */}
      <div className="chart-container">
        <h2>📜 Historial de Órdenes Recientes</h2>
        {historialOrdenes.length === 0 ? (
          <p style={{ textAlign: 'center', padding: '20px', color: '#7f8c8d' }}>
            No hay órdenes registradas
          </p>
        ) : (
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Proveedor</th>
                <th>Fecha</th>
                <th>Total</th>
                <th>Estado</th>
              </tr>
            </thead>
            <tbody>
              {historialOrdenes.map(orden => (
                <tr key={orden.id}>
                  <td>#{orden.id}</td>
                  <td>{proveedoresMap[orden.proveedorId] || `Proveedor #${orden.proveedorId}`}</td>
                  <td>{new Date(orden.fechaOrden).toLocaleDateString()}</td>
                  <td>${orden.total.toFixed(2)}</td>
                  <td>
                    <span className={`badge ${orden.estado.toLowerCase()}`}>
                      {orden.estado}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  )
}

export default DashboardPage
