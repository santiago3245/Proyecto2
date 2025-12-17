import { useState, useEffect } from 'react';
import { bodegaService } from '../services/api.service';
import { getErrorMessage } from '../utils/errorHandler';
import './CrudPage.css';

function BodegasPage() {
  const [bodegas, setBodegas] = useState([]);
  const [bodegasFiltradas, setBodegasFiltradas] = useState([]);
  const [capacidadInfo, setCapacidadInfo] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [editingBodega, setEditingBodega] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [formData, setFormData] = useState({
    codigo: '',
    nombre: '',
    direccion: '',
    ciudad: '',
    telefono: '',
    capacidadMaxima: '',
    unidadCapacidad: 'm²',
    estado: 'ACTIVO'
  });
  const [message, setMessage] = useState({ type: '', text: '' });

  useEffect(() => {
    loadBodegas();
  }, []);

  useEffect(() => {
    setBodegasFiltradas(bodegas);
  }, [bodegas]);

  const loadBodegas = async () => {
    try {
      const [bodegasRes, capacidadRes] = await Promise.all([
        bodegaService.getAll(),
        bodegaService.getCapacidad()
      ]);
      setBodegas(bodegasRes.data);
      setCapacidadInfo(capacidadRes.data);
    } catch (error) {
      setMessage({ type: 'error', text: 'Error al cargar bodegas' });
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = () => {
    if (!searchTerm.trim()) {
      setBodegasFiltradas(bodegas);
      return;
    }
    const filtered = bodegas.filter(b => 
      b.nombre?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      b.codigo?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      b.ciudad?.toLowerCase().includes(searchTerm.toLowerCase())
    );
    setBodegasFiltradas(filtered);
  };

  const handleSearchKeyPress = (e) => {
    if (e.key === 'Enter') {
      handleSearch();
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingBodega) {
        await bodegaService.update(editingBodega.id, formData);
        setMessage({ type: 'success', text: 'Bodega actualizada exitosamente' });
      } else {
        await bodegaService.create(formData);
        setMessage({ type: 'success', text: 'Bodega creada exitosamente' });
      }
      resetForm();
      loadBodegas();
    } catch (error) {
      setMessage({ type: 'error', text: getErrorMessage(error, 'Error al guardar bodega') });
    }
  };

  const handleEdit = (bodega) => {
    setEditingBodega(bodega);
    setFormData(bodega);
    setShowForm(true);
  };

  const handleDelete = async (id) => {
    if (window.confirm('¿Está seguro de eliminar esta bodega?')) {
      try {
        await bodegaService.delete(id);
        setMessage({ type: 'success', text: 'Bodega eliminada exitosamente' });
        loadBodegas();
      } catch (error) {
        setMessage({ type: 'error', text: 'Error al eliminar bodega' });
      }
    }
  };

  const resetForm = () => {
    setFormData({
      codigo: '',
      nombre: '',
      direccion: '',
      ciudad: '',
      telefono: '',
      capacidadMaxima: '',
      unidadCapacidad: 'm²',
      estado: 'ACTIVO'
    });
    setEditingBodega(null);
    setShowForm(false);
  };

  if (loading) return <div className="loading">Cargando bodegas...</div>;

  return (
    <div className="page-container">
      {message.text && (
        <div className={`alert alert-${message.type}`}>
          {message.text}
          <button onClick={() => setMessage({ type: '', text: '' })}>✕</button>
        </div>
      )}

      <div className="page-header-section">
        <div className="page-header-content">
          <div>
            <h1 className="page-title">🏪 Gestión de Bodegas</h1>
            <p className="page-subtitle">Control de centros de almacenamiento</p>
          </div>
          <div className="header-actions">
            <button className="btn-primary" onClick={() => setShowForm(!showForm)}>
              {showForm ? '✕ Cancelar' : '+ Nueva Bodega'}
            </button>
          </div>
        </div>
      </div>

      {!showForm && (
        <div className="search-section">
          <div className="search-wrapper">
            <input
              type="text"
              className="search-input"
              placeholder="Buscar por nombre, código o ciudad..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              onKeyPress={handleSearchKeyPress}
            />
            <button className="btn-search" onClick={handleSearch}>
              🔍 Buscar
            </button>
          </div>
        </div>
      )}

      {showForm && (
        <div className="form-section">
          <h3 className="form-title">
            {editingBodega ? '✏️ Editar Bodega' : '➕ Nueva Bodega'}
          </h3>
          <form onSubmit={handleSubmit}>
            <div className="form-row">
              <div className="form-group">
                <label>Código *</label>
                <input
                  type="text"
                  value={formData.codigo}
                  onChange={(e) => setFormData({ ...formData, codigo: e.target.value })}
                  required
                />
              </div>
              <div className="form-group">
                <label>Nombre *</label>
                <input
                  type="text"
                  value={formData.nombre}
                  onChange={(e) => setFormData({ ...formData, nombre: e.target.value })}
                  required
                />
              </div>
            </div>

            <div className="form-row">
              <div className="form-group">
                <label>Dirección *</label>
                <input
                  type="text"
                  value={formData.direccion}
                  onChange={(e) => setFormData({ ...formData, direccion: e.target.value })}
                  required
                />
              </div>
              <div className="form-group">
                <label>Ciudad *</label>
                <input
                  type="text"
                  value={formData.ciudad}
                  onChange={(e) => setFormData({ ...formData, ciudad: e.target.value })}
                  required
                />
              </div>
            </div>

            <div className="form-row">
              <div className="form-group">
                <label>Teléfono</label>
                <input
                  type="text"
                  value={formData.telefono}
                  onChange={(e) => setFormData({ ...formData, telefono: e.target.value })}
                />
              </div>
              <div className="form-group">
                <label>Capacidad Máxima</label>
                <input
                  type="number"
                  value={formData.capacidadMaxima}
                  onChange={(e) => setFormData({ ...formData, capacidadMaxima: e.target.value })}
                  placeholder="Ej: 12"
                />
              </div>
              <div className="form-group">
                <label>Unidad de Capacidad</label>
                <select
                  value={formData.unidadCapacidad}
                  onChange={(e) => setFormData({ ...formData, unidadCapacidad: e.target.value })}
                >
                  <option value="m²">m² (metros cuadrados)</option>
                  <option value="m³">m³ (metros cúbicos)</option>
                  <option value="pallets">Pallets</option>
                  <option value="toneladas">Toneladas</option>
                  <option value="unidades">Miles de unidades</option>
                </select>
              </div>
            </div>

            <div className="form-row">
              <div className="form-group">
                <label>Estado</label>
                <select
                  value={formData.estado}
                  onChange={(e) => setFormData({ ...formData, estado: e.target.value })}
                >
                  <option value="ACTIVO">ACTIVO</option>
                  <option value="INACTIVO">INACTIVO</option>
                </select>
              </div>
            </div>

            <div className="form-actions">
              <button type="submit" className="btn-success">
                {editingBodega ? '💾 Actualizar' : '✓ Crear'} Bodega
              </button>
              <button type="button" className="btn-cancel" onClick={resetForm}>
                Cancelar
              </button>
            </div>
          </form>
        </div>
      )}

      {!showForm && (
        <div className="table-section">
          <div className="table-header">
            <h3 className="table-title">Listado de Bodegas</h3>
            <span className="table-count">{bodegasFiltradas.length} bodegas</span>
          </div>

          {bodegasFiltradas.length === 0 ? (
            <div className="empty-state">
              <div className="empty-state-icon">🏪</div>
              <h3>No hay bodegas registradas</h3>
              <p>Comienza creando una nueva bodega</p>
            </div>
          ) : (
            <table className="modern-table">
              <thead>
                <tr>
                  <th>Código</th>
                  <th>Nombre</th>
                  <th>Dirección</th>
                  <th>Ciudad</th>
                  <th>Teléfono</th>
                  <th>Capacidad</th>
                  <th>Utilización</th>
                  <th>Estado</th>
                  <th>Acciones</th>
                </tr>
              </thead>
              <tbody>
                {bodegasFiltradas.map((bodega) => {
                  const capacidad = capacidadInfo.find(c => c.bodegaId === bodega.id);
                  return (
                    <tr key={bodega.id}>
                      <td><strong>{bodega.codigo}</strong></td>
                      <td>{bodega.nombre}</td>
                      <td>{bodega.direccion}</td>
                      <td>{bodega.ciudad}</td>
                      <td>{bodega.telefono || '-'}</td>
                      <td>
                        {bodega.capacidadMaxima ? 
                          `${bodega.capacidadMaxima} ${bodega.unidadCapacidad || 'm²'}` : 
                          '-'
                        }
                      </td>
                      <td>
                        {capacidad && bodega.capacidadMaxima ? (
                          <div style={{display: 'flex', alignItems: 'center', gap: '8px'}}>
                            <div style={{
                              width: '100px',
                              height: '20px',
                              backgroundColor: '#f0f0f0',
                              borderRadius: '10px',
                              overflow: 'hidden'
                            }}>
                              <div style={{
                                width: `${Math.min(capacidad.porcentajeUtilizacion, 100)}%`,
                                height: '100%',
                                backgroundColor: 
                                  capacidad.nivelAlerta === 'ROJO' ? '#ff4444' :
                                  capacidad.nivelAlerta === 'AMARILLO' ? '#ffaa00' :
                                  '#28a745',
                                transition: 'width 0.3s ease'
                              }}></div>
                            </div>
                            <span style={{
                              fontSize: '13px',
                              fontWeight: 'bold',
                              color: 
                                capacidad.nivelAlerta === 'ROJO' ? '#ff4444' :
                                capacidad.nivelAlerta === 'AMARILLO' ? '#ffaa00' :
                                '#28a745'
                            }}>
                              {capacidad.porcentajeUtilizacion.toFixed(1)}%
                            </span>
                            {capacidad.nivelAlerta === 'ROJO' && <span title="Capacidad crítica">⚠️</span>}
                          </div>
                        ) : '-'}
                      </td>
                      <td>
                        <span className={`status-badge status-${bodega.estado.toLowerCase()}`}>
                          {bodega.estado}
                        </span>
                      </td>
                      <td>
                        <div className="action-buttons">
                          <button className="btn-edit" onClick={() => handleEdit(bodega)}>✏️ Editar</button>
                          <button className="btn-delete" onClick={() => handleDelete(bodega.id)}>🗑️ Eliminar</button>
                        </div>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          )}
        </div>
      )}
    </div>
  );
}

export default BodegasPage;
