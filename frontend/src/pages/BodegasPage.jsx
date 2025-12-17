import { useState, useEffect } from 'react'
import { getBodegas, createBodega, updateBodega, deleteBodega } from '../services/api.service'
import { getErrorMessage } from '../utils/errorHandler'

function BodegasPage() {
  const [bodegas, setBodegas] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [showForm, setShowForm] = useState(false)
  const [editingId, setEditingId] = useState(null)
  const [formData, setFormData] = useState({
    nombre: '',
    ubicacion: '',
    capacidad: ''
  })

  useEffect(() => {
    loadBodegas()
  }, [])

  const loadBodegas = async () => {
    try {
      setLoading(true)
      setError(null)
      const response = await getBodegas()
      setBodegas(response.data)
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
        capacidad: parseInt(formData.capacidad)
      }
      
      if (editingId) {
        await updateBodega(editingId, data)
      } else {
        await createBodega(data)
      }
      
      setShowForm(false)
      setEditingId(null)
      setFormData({ nombre: '', ubicacion: '', capacidad: '' })
      loadBodegas()
    } catch (err) {
      setError(getErrorMessage(err))
    }
  }

  const handleEdit = (bodega) => {
    setEditingId(bodega.id)
    setFormData({
      nombre: bodega.nombre,
      ubicacion: bodega.ubicacion || '',
      capacidad: bodega.capacidad.toString()
    })
    setShowForm(true)
  }

  const handleDelete = async (id) => {
    if (!confirm('¿Estás seguro de eliminar esta bodega?')) return
    
    try {
      setError(null)
      await deleteBodega(id)
      loadBodegas()
    } catch (err) {
      setError(getErrorMessage(err))
    }
  }

  const handleCancel = () => {
    setShowForm(false)
    setEditingId(null)
    setFormData({ nombre: '', ubicacion: '', capacidad: '' })
    setError(null)
  }

  if (loading) {
    return <div className="loading">Cargando bodegas...</div>
  }

  return (
    <div className="container">
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
        <h1>Bodegas</h1>
        {!showForm && (
          <button className="primary" onClick={() => setShowForm(true)}>
            + Nueva Bodega
          </button>
        )}
      </div>

      {error && <div className="error-message">{error}</div>}

      {showForm && (
        <div className="card">
          <h2>{editingId ? 'Editar Bodega' : 'Nueva Bodega'}</h2>
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
              <label>Ubicación</label>
              <input
                type="text"
                value={formData.ubicacion}
                onChange={(e) => setFormData({ ...formData, ubicacion: e.target.value })}
              />
            </div>
            
            <div className="form-group">
              <label>Capacidad *</label>
              <input
                type="number"
                min="0"
                value={formData.capacidad}
                onChange={(e) => setFormData({ ...formData, capacidad: e.target.value })}
                required
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
                <th>Ubicación</th>
                <th>Capacidad</th>
                <th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              {bodegas.length === 0 ? (
                <tr>
                  <td colSpan="5" style={{ textAlign: 'center', padding: '40px', color: '#7f8c8d' }}>
                    No hay bodegas registradas
                  </td>
                </tr>
              ) : (
                bodegas.map(bodega => (
                  <tr key={bodega.id}>
                    <td>{bodega.id}</td>
                    <td>{bodega.nombre}</td>
                    <td>{bodega.ubicacion || '-'}</td>
                    <td>{bodega.capacidad}</td>
                    <td>
                      <button className="warning" onClick={() => handleEdit(bodega)}>
                        Editar
                      </button>
                      <button className="danger" onClick={() => handleDelete(bodega.id)}>
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

export default BodegasPage
