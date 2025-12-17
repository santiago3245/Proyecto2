import { useState, useEffect } from 'react'
import {
  getOrdenesCompra,
  createOrdenCompra,
  updateOrdenCompra,
  deleteOrdenCompra,
  aprobarOrdenCompra,
  cancelarOrdenCompra,
  recibirOrdenCompra,
  getProveedores,
  getProductos
} from '../services/api.service'
import { getErrorMessage } from '../utils/errorHandler'

function OrdenesCompraPage() {
  const [ordenes, setOrdenes] = useState([])
  const [proveedores, setProveedores] = useState([])
  const [productos, setProductos] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [showForm, setShowForm] = useState(false)
  const [editingId, setEditingId] = useState(null)
  const [showProductModal, setShowProductModal] = useState(false)
  const [formData, setFormData] = useState({
    proveedorId: '',
    fechaOrden: '',
    fechaEntregaEstimada: '',
    observaciones: '',
    detalles: []
  })
  const [productModal, setProductModal] = useState({
    productoId: '',
    cantidad: '',
    precioUnitario: ''
  })

  useEffect(() => {
    loadData()
  }, [])

  const loadData = async () => {
    try {
      setLoading(true)
      setError(null)
      
      const [ordenesRes, proveedoresRes, productosRes] = await Promise.all([
        getOrdenesCompra(),
        getProveedores(),
        getProductos()
      ])
      
      setOrdenes(ordenesRes.data)
      setProveedores(proveedoresRes.data)
      setProductos(productosRes.data)
    } catch (err) {
      setError(getErrorMessage(err))
    } finally {
      setLoading(false)
    }
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError(null)
    
    if (formData.detalles.length === 0) {
      setError('Debes agregar al menos un producto a la orden')
      return
    }
    
    try {
      const data = {
        proveedorId: parseInt(formData.proveedorId),
        fechaOrden: formData.fechaOrden,
        fechaEntregaEstimada: formData.fechaEntregaEstimada,
        observaciones: formData.observaciones,
        detalles: formData.detalles.map(d => ({
          productoId: d.productoId,
          cantidad: d.cantidad,
          precioUnitario: d.precioUnitario
        }))
      }
      
      if (editingId) {
        await updateOrdenCompra(editingId, data)
      } else {
        await createOrdenCompra(data)
      }
      
      setShowForm(false)
      setEditingId(null)
      resetForm()
      loadData()
    } catch (err) {
      setError(getErrorMessage(err))
    }
  }

  const handleEdit = (orden) => {
    if (orden.estado !== 'PENDIENTE') {
      setError('Solo se pueden editar órdenes en estado PENDIENTE')
      return
    }
    
    setEditingId(orden.id)
    setFormData({
      proveedorId: orden.proveedorId.toString(),
      fechaOrden: orden.fechaOrden,
      fechaEntregaEstimada: orden.fechaEntregaEstimada || '',
      observaciones: orden.observaciones || '',
      detalles: orden.detalles || []
    })
    setShowForm(true)
  }

  const handleDelete = async (id) => {
    if (!confirm('¿Estás seguro de eliminar esta orden?')) return
    
    try {
      setError(null)
      await deleteOrdenCompra(id)
      loadData()
    } catch (err) {
      setError(getErrorMessage(err))
    }
  }

  const handleAprobar = async (id) => {
    if (!confirm('¿Deseas aprobar esta orden de compra?')) return
    
    try {
      setError(null)
      await aprobarOrdenCompra(id)
      loadData()
    } catch (err) {
      setError(getErrorMessage(err))
    }
  }

  const handleCancelar = async (id) => {
    if (!confirm('¿Deseas cancelar esta orden de compra?')) return
    
    try {
      setError(null)
      await cancelarOrdenCompra(id)
      loadData()
    } catch (err) {
      setError(getErrorMessage(err))
    }
  }

  const handleRecibir = async (id) => {
    if (!confirm('¿Confirmas que has recibido esta orden de compra?')) return
    
    try {
      setError(null)
      await recibirOrdenCompra(id)
      loadData()
    } catch (err) {
      setError(getErrorMessage(err))
    }
  }

  const resetForm = () => {
    setFormData({
      proveedorId: '',
      fechaOrden: '',
      fechaEntregaEstimada: '',
      observaciones: '',
      detalles: []
    })
  }

  const handleCancel = () => {
    setShowForm(false)
    setEditingId(null)
    resetForm()
    setError(null)
  }

  const handleAddProduct = () => {
    if (!productModal.productoId || !productModal.cantidad || !productModal.precioUnitario) {
      alert('Completa todos los campos del producto')
      return
    }
    
    const producto = productos.find(p => p.id === parseInt(productModal.productoId))
    if (!producto) return
    
    const newDetalle = {
      productoId: parseInt(productModal.productoId),
      productoNombre: producto.nombre,
      cantidad: parseInt(productModal.cantidad),
      precioUnitario: parseFloat(productModal.precioUnitario),
      subtotal: parseInt(productModal.cantidad) * parseFloat(productModal.precioUnitario)
    }
    
    setFormData({
      ...formData,
      detalles: [...formData.detalles, newDetalle]
    })
    
    setProductModal({ productoId: '', cantidad: '', precioUnitario: '' })
    setShowProductModal(false)
  }

  const handleRemoveProduct = (index) => {
    const newDetalles = formData.detalles.filter((_, i) => i !== index)
    setFormData({ ...formData, detalles: newDetalles })
  }

  const getProveedorNombre = (id) => {
    const proveedor = proveedores.find(p => p.id === id)
    return proveedor ? proveedor.nombre : `Proveedor #${id}`
  }

  const calcularTotal = () => {
    return formData.detalles.reduce((sum, d) => sum + d.subtotal, 0)
  }

  if (loading) {
    return <div className="loading">Cargando órdenes de compra...</div>
  }

  return (
    <div className="container">
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
        <h1>Órdenes de Compra</h1>
        {!showForm && (
          <button className="primary" onClick={() => setShowForm(true)}>
            + Nueva Orden de Compra
          </button>
        )}
      </div>

      {error && <div className="error-message">{error}</div>}

      {showForm && (
        <div className="card">
          <h2>{editingId ? 'Editar Orden de Compra' : 'Nueva Orden de Compra'}</h2>
          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label>Proveedor *</label>
              <select
                value={formData.proveedorId}
                onChange={(e) => setFormData({ ...formData, proveedorId: e.target.value })}
                required
              >
                <option value="">Selecciona un proveedor</option>
                {proveedores.map(prov => (
                  <option key={prov.id} value={prov.id}>
                    {prov.nombre}
                  </option>
                ))}
              </select>
            </div>
            
            <div className="form-group">
              <label>Fecha de Orden *</label>
              <input
                type="date"
                value={formData.fechaOrden}
                onChange={(e) => setFormData({ ...formData, fechaOrden: e.target.value })}
                required
              />
            </div>
            
            <div className="form-group">
              <label>Fecha de Entrega Estimada</label>
              <input
                type="date"
                value={formData.fechaEntregaEstimada}
                onChange={(e) => setFormData({ ...formData, fechaEntregaEstimada: e.target.value })}
              />
            </div>
            
            <div className="form-group">
              <label>Observaciones</label>
              <textarea
                value={formData.observaciones}
                onChange={(e) => setFormData({ ...formData, observaciones: e.target.value })}
                rows="3"
              />
            </div>
            
            <div className="form-group">
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <label>Productos *</label>
                <button 
                  type="button" 
                  className="primary" 
                  onClick={() => setShowProductModal(true)}
                >
                  + Agregar Producto
                </button>
              </div>
              
              {formData.detalles.length === 0 ? (
                <p style={{ color: '#7f8c8d', fontStyle: 'italic', marginTop: '10px' }}>
                  No hay productos agregados
                </p>
              ) : (
                <table style={{ marginTop: '10px' }}>
                  <thead>
                    <tr>
                      <th>Producto</th>
                      <th>Cantidad</th>
                      <th>Precio Unit.</th>
                      <th>Subtotal</th>
                      <th>Acción</th>
                    </tr>
                  </thead>
                  <tbody>
                    {formData.detalles.map((detalle, index) => (
                      <tr key={index}>
                        <td>{detalle.productoNombre}</td>
                        <td>{detalle.cantidad}</td>
                        <td>${detalle.precioUnitario.toFixed(2)}</td>
                        <td>${detalle.subtotal.toFixed(2)}</td>
                        <td>
                          <button 
                            type="button" 
                            className="danger" 
                            onClick={() => handleRemoveProduct(index)}
                          >
                            Eliminar
                          </button>
                        </td>
                      </tr>
                    ))}
                    <tr style={{ fontWeight: 'bold', backgroundColor: '#f8f9fa' }}>
                      <td colSpan="3" style={{ textAlign: 'right' }}>TOTAL:</td>
                      <td colSpan="2">${calcularTotal().toFixed(2)}</td>
                    </tr>
                  </tbody>
                </table>
              )}
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

      {showProductModal && (
        <div className="modal-overlay">
          <div className="modal">
            <div className="modal-header">
              <h3>Agregar Producto</h3>
              <button className="modal-close" onClick={() => setShowProductModal(false)}>
                ×
              </button>
            </div>
            
            <div className="form-group">
              <label>Producto *</label>
              <select
                value={productModal.productoId}
                onChange={(e) => setProductModal({ ...productModal, productoId: e.target.value })}
              >
                <option value="">Selecciona un producto</option>
                {productos.map(prod => (
                  <option key={prod.id} value={prod.id}>
                    {prod.nombre} - ${prod.precio}
                  </option>
                ))}
              </select>
            </div>
            
            <div className="form-group">
              <label>Cantidad *</label>
              <input
                type="number"
                min="1"
                value={productModal.cantidad}
                onChange={(e) => setProductModal({ ...productModal, cantidad: e.target.value })}
              />
            </div>
            
            <div className="form-group">
              <label>Precio Unitario *</label>
              <input
                type="number"
                step="0.01"
                min="0"
                value={productModal.precioUnitario}
                onChange={(e) => setProductModal({ ...productModal, precioUnitario: e.target.value })}
              />
            </div>
            
            <div className="button-group">
              <button className="success" onClick={handleAddProduct}>
                Agregar
              </button>
              <button className="secondary" onClick={() => setShowProductModal(false)}>
                Cancelar
              </button>
            </div>
          </div>
        </div>
      )}

      {!showForm && (
        <div className="card">
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Proveedor</th>
                <th>Fecha Orden</th>
                <th>Fecha Entrega</th>
                <th>Total</th>
                <th>Estado</th>
                <th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              {ordenes.length === 0 ? (
                <tr>
                  <td colSpan="7" style={{ textAlign: 'center', padding: '40px', color: '#7f8c8d' }}>
                    No hay órdenes de compra registradas
                  </td>
                </tr>
              ) : (
                ordenes.map(orden => (
                  <tr key={orden.id}>
                    <td>#{orden.id}</td>
                    <td>{getProveedorNombre(orden.proveedorId)}</td>
                    <td>{new Date(orden.fechaOrden).toLocaleDateString()}</td>
                    <td>
                      {orden.fechaEntregaEstimada 
                        ? new Date(orden.fechaEntregaEstimada).toLocaleDateString() 
                        : '-'}
                    </td>
                    <td>${orden.total.toFixed(2)}</td>
                    <td>
                      <span className={`badge ${orden.estado.toLowerCase()}`}>
                        {orden.estado}
                      </span>
                    </td>
                    <td>
                      {orden.estado === 'PENDIENTE' && (
                        <>
                          <button className="warning" onClick={() => handleEdit(orden)}>
                            Editar
                          </button>
                          <button className="success" onClick={() => handleAprobar(orden.id)}>
                            Aprobar
                          </button>
                          <button className="danger" onClick={() => handleCancelar(orden.id)}>
                            Cancelar
                          </button>
                        </>
                      )}
                      
                      {orden.estado === 'APROBADA' && (
                        <>
                          <button className="primary" onClick={() => handleRecibir(orden.id)}>
                            Recibir
                          </button>
                          <button className="danger" onClick={() => handleCancelar(orden.id)}>
                            Cancelar
                          </button>
                        </>
                      )}
                      
                      {orden.estado === 'RECIBIDA' && (
                        <span style={{ color: '#27ae60', fontWeight: 'bold' }}>✓ Completada</span>
                      )}
                      
                      {orden.estado === 'CANCELADA' && (
                        <button className="danger" onClick={() => handleDelete(orden.id)}>
                          Eliminar
                        </button>
                      )}
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

export default OrdenesCompraPage
