import { useState, useEffect } from 'react';
import { proveedorService } from '../services/api.service';
import { getErrorMessage } from '../utils/errorHandler';
import './CrudPage.css';

function ProveedoresPage() {
  const [proveedores, setProveedores] = useState([]);
  const [proveedoresFiltrados, setProveedoresFiltrados] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [editingProveedor, setEditingProveedor] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [formData, setFormData] = useState({
    ruc: '',
    razonSocial: '',
    nombreComercial: '',
    pais: '',
    direccion: '',
    telefono: '',
    email: '',
    contacto: '',
    estado: 'ACTIVO'
  });
  const [message, setMessage] = useState({ type: '', text: '' });

  useEffect(() => {
    loadProveedores();
  }, []);

  useEffect(() => {
    setProveedoresFiltrados(proveedores);
  }, [proveedores]);

  const loadProveedores = async () => {
    try {
      const response = await proveedorService.getAll();
      setProveedores(response.data);
    } catch (error) {
      setMessage({ type: 'error', text: 'Error al cargar proveedores' });
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = () => {
    if (!searchTerm.trim()) {
      setProveedoresFiltrados(proveedores);
      return;
    }
    const filtered = proveedores.filter(p => 
      p.razonSocial?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      p.ruc?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      p.pais?.toLowerCase().includes(searchTerm.toLowerCase())
    );
    setProveedoresFiltrados(filtered);
  };

  const handleSearchKeyPress = (e) => {
    if (e.key === 'Enter') {
      handleSearch();
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingProveedor) {
        await proveedorService.update(editingProveedor.id, formData);
        setMessage({ type: 'success', text: 'Proveedor actualizado exitosamente' });
      } else {
        await proveedorService.create(formData);
        setMessage({ type: 'success', text: 'Proveedor creado exitosamente' });
      }
      resetForm();
      loadProveedores();
    } catch (error) {
      setMessage({ type: 'error', text: getErrorMessage(error, 'Error al guardar proveedor') });
    }
  };

  const handleEdit = (proveedor) => {
    setEditingProveedor(proveedor);
    setFormData(proveedor);
    setShowForm(true);
  };

  const handleDelete = async (id) => {
    if (window.confirm('¿Está seguro de eliminar este proveedor?')) {
      try {
        await proveedorService.delete(id);
        setMessage({ type: 'success', text: 'Proveedor eliminado exitosamente' });
        loadProveedores();
      } catch (error) {
        setMessage({ type: 'error', text: 'Error al eliminar proveedor' });
      }
    }
  };

  const resetForm = () => {
    setFormData({
      ruc: '',
      razonSocial: '',
      nombreComercial: '',
      pais: '',
      direccion: '',
      telefono: '',
      email: '',
      contacto: '',
      estado: 'ACTIVO'
    });
    setEditingProveedor(null);
    setShowForm(false);
  };

  if (loading) return <div className="loading">Cargando proveedores...</div>;

  return (
    <div className="page-container">
      {message.text && (
        <div className={`alert alert-${message.type}`}>
          {message.text}
          <button onClick={() => setMessage({ type: '', text: '' })}>✕</button>
        </div>
      )}

      {/* Header Section */}
      <div className="page-header-section">
        <div className="page-header-content">
          <div>
            <h1 className="page-title">🚚 Gestión de Proveedores</h1>
            <p className="page-subtitle">Administra tu red de proveedores y contactos</p>
          </div>
          <div className="header-actions">
            <button className="btn-primary" onClick={() => setShowForm(!showForm)}>
              {showForm ? '✕ Cancelar' : '+ Nuevo Proveedor'}
            </button>
          </div>
        </div>
      </div>

      {/* Search Section - Solo mostrar si NO está el formulario */}
      {!showForm && (
        <div className="search-section">
          <div className="search-wrapper">
            <input
              type="text"
              className="search-input"
              placeholder="Buscar por razón social, RUC o país..."
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

      {/* Form Section */}
      {showForm && (
        <div className="form-section">
          <h3 className="form-title">
            {editingProveedor ? '✏️ Editar Proveedor' : '➕ Nuevo Proveedor'}
          </h3>
          <form onSubmit={handleSubmit}>
            <div className="form-row">
              <div className="form-group">
                <label>RUC/NIT *</label>
                <input
                  type="text"
                  value={formData.ruc}
                  onChange={(e) => setFormData({ ...formData, ruc: e.target.value })}
                  required
                  placeholder="Ej: 1234567890001"
                />
              </div>
              <div className="form-group">
                <label>Razón Social *</label>
                <input
                  type="text"
                  value={formData.razonSocial}
                  onChange={(e) => setFormData({ ...formData, razonSocial: e.target.value })}
                  required
                  placeholder="Nombre legal de la empresa"
                />
              </div>
            </div>

            <div className="form-row">
              <div className="form-group">
                <label>Nombre Comercial</label>
                <input
                  type="text"
                  value={formData.nombreComercial}
                  onChange={(e) => setFormData({ ...formData, nombreComercial: e.target.value })}
                  placeholder="Nombre comercial (opcional)"
                />
              </div>
              <div className="form-group">
                <label>País *</label>
                <input
                  type="text"
                  value={formData.pais}
                  onChange={(e) => setFormData({ ...formData, pais: e.target.value })}
                  required
                  placeholder="Ej: Ecuador, China, USA"
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
                  placeholder="Ej: +593 99 999 9999"
                />
              </div>
              <div className="form-group">
                <label>Email</label>
                <input
                  type="email"
                  value={formData.email}
                  onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                  placeholder="email@proveedor.com"
                />
              </div>
            </div>

            <div className="form-row">
              <div className="form-group">
                <label>Contacto</label>
                <input
                  type="text"
                  value={formData.contacto}
                  onChange={(e) => setFormData({ ...formData, contacto: e.target.value })}
                  placeholder="Nombre del contacto principal"
                />
              </div>
              <div className="form-group">
                <label>Estado *</label>
                <select
                  value={formData.estado}
                  onChange={(e) => setFormData({ ...formData, estado: e.target.value })}
                  required
                >
                  <option value="ACTIVO">ACTIVO</option>
                  <option value="INACTIVO">INACTIVO</option>
                  <option value="BLOQUEADO">BLOQUEADO</option>
                </select>
              </div>
            </div>

            <div className="form-group">
              <label>Dirección</label>
              <textarea
                value={formData.direccion}
                onChange={(e) => setFormData({ ...formData, direccion: e.target.value })}
                rows="2"
                placeholder="Dirección completa del proveedor..."
              />
            </div>

            <div className="form-actions">
              <button type="submit" className="btn-success">
                {editingProveedor ? '💾 Actualizar' : '✓ Crear'} Proveedor
              </button>
              <button type="button" className="btn-cancel" onClick={resetForm}>
                Cancelar
              </button>
            </div>
          </form>
        </div>
      )}

      {/* Table Section - Solo mostrar si NO está el formulario */}
      {!showForm && (
        <div className="table-section">
          <div className="table-header">
            <h3 className="table-title">Listado de Proveedores</h3>
            <span className="table-count">{proveedoresFiltrados.length} proveedores</span>
          </div>

          {proveedoresFiltrados.length === 0 ? (
            <div className="empty-state">
              <div className="empty-state-icon">🚚</div>
              <h3>No hay proveedores registrados</h3>
              <p>Comienza creando un nuevo proveedor</p>
            </div>
          ) : (
            <table className="modern-table">
              <thead>
                <tr>
                  <th>RUC</th>
                  <th>Razón Social</th>
                  <th>País</th>
                  <th>Teléfono</th>
                  <th>Email</th>
                  <th>Estado</th>
                  <th>Acciones</th>
                </tr>
              </thead>
              <tbody>
                {proveedoresFiltrados.map((proveedor) => (
                  <tr key={proveedor.id}>
                    <td><strong>{proveedor.ruc}</strong></td>
                    <td>{proveedor.razonSocial}</td>
                    <td>{proveedor.pais}</td>
                    <td>{proveedor.telefono || '-'}</td>
                    <td>{proveedor.email || '-'}</td>
                    <td>
                      <span className={`status-badge status-${proveedor.estado.toLowerCase()}`}>
                        {proveedor.estado}
                      </span>
                    </td>
                    <td>
                      <div className="action-buttons">
                        <button className="btn-edit" onClick={() => handleEdit(proveedor)}>
                          ✏️ Editar
                        </button>
                        <button className="btn-delete" onClick={() => handleDelete(proveedor.id)}>
                          🗑️ Eliminar
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      )}
    </div>
  );
}

export default ProveedoresPage;
