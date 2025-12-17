import { useState, useEffect } from 'react'
import { getInventarios, getBodegas } from '../services/api.service'
import { getErrorMessage } from '../utils/errorHandler'

function InventarioPage() {
  const [inventarios, setInventarios] = useState([])
  const [bodegas, setBodegas] = useState([])
  const [bodegasMap, setBodegasMap] = useState({})
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [filtroProductoId, setFiltroProductoId] = useState('')
  const [filtroBodegaId, setFiltroBodegaId] = useState('')

  useEffect(() => {
    loadInventario()
  }, [])

  const loadInventario = async () => {
    try {
      setLoading(true)
      setError(null)
      
      const [inventariosRes, bodegasRes] = await Promise.all([
        getInventarios(),
        getBodegas()
      ])
      
      setInventarios(inventariosRes.data)
      setBodegas(bodegasRes.data)
      
      // Crear mapa de bodegas para resolución de nombres
      const bodegasMap = {}
      bodegasRes.data.forEach(bodega => {
        bodegasMap[bodega.id] = bodega.nombre
      })
      setBodegasMap(bodegasMap)
      
    } catch (err) {
      setError(getErrorMessage(err))
    } finally {
      setLoading(false)
    }
  }

  const inventariosFiltrados = inventarios.filter(inv => {
    const matchProducto = !filtroProductoId || inv.productoId.toString().includes(filtroProductoId)
    const matchBodega = !filtroBodegaId || inv.bodegaId.toString() === filtroBodegaId
    return matchProducto && matchBodega
  })

  if (loading) {
    return <div className="loading">Cargando inventario...</div>
  }

  return (
    <div className="container">
      <h1>Inventario</h1>

      {error && <div className="error-message">{error}</div>}

      <div className="card">
        <h3>Filtros</h3>
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '15px', marginBottom: '20px' }}>
          <div className="form-group">
            <label>Producto ID</label>
            <input
              type="text"
              placeholder="Buscar por ID de producto"
              value={filtroProductoId}
              onChange={(e) => setFiltroProductoId(e.target.value)}
            />
          </div>
          
          <div className="form-group">
            <label>Bodega</label>
            <select
              value={filtroBodegaId}
              onChange={(e) => setFiltroBodegaId(e.target.value)}
            >
              <option value="">Todas las bodegas</option>
              {bodegas.map(bodega => (
                <option key={bodega.id} value={bodega.id}>
                  {bodega.nombre}
                </option>
              ))}
            </select>
          </div>
        </div>

        {filtroProductoId || filtroBodegaId ? (
          <button 
            className="secondary" 
            onClick={() => {
              setFiltroProductoId('')
              setFiltroBodegaId('')
            }}
          >
            Limpiar Filtros
          </button>
        ) : null}
      </div>

      <div className="card">
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Producto ID</th>
              <th>Bodega</th>
              <th>Cantidad</th>
              <th>Stock Mínimo</th>
              <th>Stock Máximo</th>
              <th>Estado</th>
            </tr>
          </thead>
          <tbody>
            {inventariosFiltrados.length === 0 ? (
              <tr>
                <td colSpan="7" style={{ textAlign: 'center', padding: '40px', color: '#7f8c8d' }}>
                  {inventarios.length === 0 
                    ? 'No hay registros de inventario' 
                    : 'No se encontraron registros con los filtros aplicados'}
                </td>
              </tr>
            ) : (
              inventariosFiltrados.map(inv => {
                const bajoCritico = inv.cantidad < inv.stockMinimo
                const sobreStock = inv.cantidad > inv.stockMaximo
                
                return (
                  <tr key={inv.id}>
                    <td>{inv.id}</td>
                    <td>{inv.productoId}</td>
                    <td>
                      <strong>{bodegasMap[inv.bodegaId] || `Bodega #${inv.bodegaId}`}</strong>
                    </td>
                    <td>
                      <strong style={{ 
                        color: bajoCritico ? '#e74c3c' : sobreStock ? '#f39c12' : '#27ae60' 
                      }}>
                        {inv.cantidad}
                      </strong>
                    </td>
                    <td>{inv.stockMinimo}</td>
                    <td>{inv.stockMaximo}</td>
                    <td>
                      {bajoCritico ? (
                        <span className="badge" style={{ backgroundColor: '#fee', color: '#c33' }}>
                          ⚠️ Crítico
                        </span>
                      ) : sobreStock ? (
                        <span className="badge" style={{ backgroundColor: '#fff3cd', color: '#856404' }}>
                          ⚡ Sobrestock
                        </span>
                      ) : (
                        <span className="badge" style={{ backgroundColor: '#d4edda', color: '#155724' }}>
                          ✓ Normal
                        </span>
                      )}
                    </td>
                  </tr>
                )
              })
            )}
          </tbody>
        </table>
      </div>

      <div className="card" style={{ marginTop: '20px', backgroundColor: '#f8f9fa' }}>
        <h3>Resumen</h3>
        <p><strong>Total registros:</strong> {inventarios.length}</p>
        <p><strong>Productos en stock crítico:</strong> {inventarios.filter(inv => inv.cantidad < inv.stockMinimo).length}</p>
        <p><strong>Productos con sobrestock:</strong> {inventarios.filter(inv => inv.cantidad > inv.stockMaximo).length}</p>
      </div>
    </div>
  )
}

export default InventarioPage
