import { useState, useEffect } from 'react'
import { getProveedores, createProveedor, updateProveedor, deleteProveedor } from '../services/api.service'
import { getErrorMessage } from '../utils/errorHandler'

function ProveedoresPage() {
  const [proveedores, setProveedores] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [showForm, setShowForm] = useState(false)
  const [editingId, setEditingId] = useState(null)
  const [formData, setFormData] = useState({
    nombre: '',
    contacto: '',
    telefono: '',
    email: '',
    direccion: ''
  })

  useEffect(() => {
    loadProveedores()
  }, [])

  const loadProveedores = async () => {
    try {
      setLoading(true)
      setError(null)
      const response = await getProveedores()
      setProveedores(response.data)
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
      if (editingId) {
        await updateProveedor(editingId, formData)
      } else {
        await createProveedor(formData)
      }
      
      setShowForm(false)
      setEditingId(null)
      setFormData({ nombre: '', contacto: '', telefono: '', email: '', direccion: '' })
      loadProveedores()
    } catch (err) {
      setError(getErrorMessage(err))
    }
  }

  const handleEdit = (proveedor) => {
    setEditingId(proveedor.id)
    setFormData({
      nombre: proveedor.nombre,
      contacto: proveedor.contacto || '',
      telefono: proveedor.telefono || '',
      email: proveedor.email || '',
      direccion: proveedor.direccion || ''
    })
    setShowForm(true)
  }

  const handleDelete = async (id) => {
    if (!confirm('¿Estás seguro de eliminar este proveedor?')) return
    
    try {
      setError(null)
      await deleteProveedor(id)
      loadProveedores()
    } catch (err) {
      setError(getErrorMessage(err))
    }
  }

  const handleCancel = () => {
    setShowForm(false)
    setEditingId(null)
    setFormData({ nombre: '', contacto: '', telefono: '', email: '', direccion: '' })
    setError(null)
  }

  if (loading) {
    return <div className="loading">Cargando proveedores...</div>
  }

  return (
    <div className="container">
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
        <h1>Proveedores</h1>
        {!showForm && (
          <button className="primary" onClick={() => setShowForm(true)}>
            + Nuevo Proveedor
          </button>
        )}
      </div>

      {error && <div className="error-message">{error}</div>}

      {showForm && (
        <div className="card">
          <h2>{editingId ? 'Editar Proveedor' : 'Nuevo Proveedor'}</h2>
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
              <label>Contacto</label>
              <input
                type="text"
                value={formData.contacto}
                onChange={(e) => setFormData({ ...formData, contacto: e.target.value })}
              />
            </div>
            
            <div className="form-group">
              <label>Teléfono</label>
              <input
                type="tel"
                value={formData.telefono}
                onChange={(e) => setFormData({ ...formData, telefono: e.target.value })}
              />
            </div>
            
            <div className="form-group">
              <label>Email</label>
              <input
                type="email"
                value={formData.email}
                onChange={(e) => setFormData({ ...formData, email: e.target.value })}
              />
            </div>
            
            <div className="form-group">
              <label>Dirección</label>
              <textarea
                value={formData.direccion}
                onChange={(e) => setFormData({ ...formData, direccion: e.target.value })}
                rows="3"
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
                <th>Contacto</th>
                <th>Teléfono</th>
                <th>Email</th>
                <th>Dirección</th>
                <th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              {proveedores.length === 0 ? (
                <tr>
                  <td colSpan="7" style={{ textAlign: 'center', padding: '40px', color: '#7f8c8d' }}>
                    No hay proveedores registrados
                  </td>
                </tr>
              ) : (
                proveedores.map(proveedor => (
                  <tr key={proveedor.id}>
                    <td>{proveedor.id}</td>
                    <td>{proveedor.nombre}</td>
                    <td>{proveedor.contacto || '-'}</td>
                    <td>{proveedor.telefono || '-'}</td>
                    <td>{proveedor.email || '-'}</td>
                    <td>{proveedor.direccion || '-'}</td>
                    <td>
                      <button className="warning" onClick={() => handleEdit(proveedor)}>
                        Editar
                      </button>
                      <button className="danger" onClick={() => handleDelete(proveedor.id)}>
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

export default ProveedoresPage
