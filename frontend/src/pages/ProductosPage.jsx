import { useState, useEffect } from 'react'
import { getProductos, createProducto, updateProducto, deleteProducto, getInventarioPorProducto } from '../services/api.service'
import { getErrorMessage } from '../utils/errorHandler'

function ProductosPage() {
  const [productos, setProductos] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [showForm, setShowForm] = useState(false)
  const [editingId, setEditingId] = useState(null)
  const [formData, setFormData] = useState({
    nombre: '',
    descripcion: '',
    precio: '',
    categoria: ''
  })
  const [stockMap, setStockMap] = useState({})

  useEffect(() => {
    loadProductos()
  }, [])

  const loadProductos = async () => {
    try {
      setLoading(true)
      setError(null)
      const response = await getProductos()
      const productosData = response.data
      setProductos(productosData)
      
      // Cargar stock total para cada producto
      const stockData = {}
      for (const producto of productosData) {
        try {
          const inventarioRes = await getInventarioPorProducto(producto.id)
          const totalStock = inventarioRes.data.reduce((sum, inv) => sum + inv.cantidad, 0)
          stockData[producto.id] = totalStock
        } catch (err) {
          stockData[producto.id] = 0
        }
      }
      setStockMap(stockData)
    } catch (err) {
      setError(getErrorMessage(err))
    } finally {
      setLoading(false)
    }
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError(null)
    
    try {
      const data = {
        ...formData,
        precio: parseFloat(formData.precio)
      }
      
      if (editingId) {
        await updateProducto(editingId, data)
      } else {
        await createProducto(data)
      }
      
      setShowForm(false)
      setEditingId(null)
      setFormData({ nombre: '', descripcion: '', precio: '', categoria: '' })
      loadProductos()
    } catch (err) {
      setError(getErrorMessage(err))
    }
  }

  const handleEdit = (producto) => {
    setEditingId(producto.id)
    setFormData({
      nombre: producto.nombre,
      descripcion: producto.descripcion || '',
      precio: producto.precio.toString(),
      categoria: producto.categoria || ''
    })
    setShowForm(true)
  }

  const handleDelete = async (id) => {
    if (!confirm('¿Estás seguro de eliminar este producto?')) return
    
    try {
      setError(null)
      await deleteProducto(id)
      loadProductos()
    } catch (err) {
      setError(getErrorMessage(err))
    }
  }

  const handleCancel = () => {
    setShowForm(false)
    setEditingId(null)
    setFormData({ nombre: '', descripcion: '', precio: '', categoria: '' })
    setError(null)
  }

  if (loading) {
    return <div className="loading">Cargando productos...</div>
  }

  return (
    <div className="container">
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
        <h1>Productos</h1>
        {!showForm && (
          <button className="primary" onClick={() => setShowForm(true)}>
            + Nuevo Producto
          </button>
        )}
      </div>

      {error && <div className="error-message">{error}</div>}

      {showForm && (
        <div className="card">
          <h2>{editingId ? 'Editar Producto' : 'Nuevo Producto'}</h2>
          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label>Nombre *</label>
              <input
                type="text"
                value={formData.nombre}
                onChange={(e) => setFormData({ ...formData, nombre: e.target.value })}
                required
              />
            </div>
            
            <div className="form-group">
              <label>Descripción</label>
              <textarea
                value={formData.descripcion}
                onChange={(e) => setFormData({ ...formData, descripcion: e.target.value })}
                rows="3"
              />
            </div>
            
            <div className="form-group">
              <label>Precio *</label>
              <input
                type="number"
                step="0.01"
                min="0"
                value={formData.precio}
                onChange={(e) => setFormData({ ...formData, precio: e.target.value })}
                required
              />
            </div>
            
            <div className="form-group">
              <label>Categoría</label>
              <input
                type="text"
                value={formData.categoria}
                onChange={(e) => setFormData({ ...formData, categoria: e.target.value })}
              />
            </div>
            
            <div className="button-group">
              <button type="submit" className="success">
                {editingId ? 'Actualizar' : 'Crear'}
              </button>
              <button type="button" className="secondary" onClick={handleCancel}>
                Cancelar
              </button>
            </div>
          </form>
        </div>
      )}

      {!showForm && (
        <div className="card">
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Nombre</th>
                <th>Descripción</th>
                <th>Precio</th>
                <th>Categoría</th>
                <th>Stock Total</th>
                <th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              {productos.length === 0 ? (
                <tr>
                  <td colSpan="7" style={{ textAlign: 'center', padding: '40px', color: '#7f8c8d' }}>
                    No hay productos registrados
                  </td>
                </tr>
              ) : (
                productos.map(producto => (
                  <tr key={producto.id}>
                    <td>{producto.id}</td>
                    <td>{producto.nombre}</td>
                    <td>{producto.descripcion || '-'}</td>
                    <td>${producto.precio.toFixed(2)}</td>
                    <td>{producto.categoria || '-'}</td>
                    <td>
                      <strong>{stockMap[producto.id] || 0}</strong> unidades
                    </td>
                    <td>
                      <button className="warning" onClick={() => handleEdit(producto)}>
                        Editar
                      </button>
                      <button className="danger" onClick={() => handleDelete(producto.id)}>
                        Eliminar
                      </button>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      )}
    </div>
  )
}

export default ProductosPage
