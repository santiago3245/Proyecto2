import { useState, useEffect } from 'react';
import { productoService, inventarioService } from '../services/api.service';
import { getErrorMessage } from '../utils/errorHandler';
import './CrudPage.css';

function ProductosPage() {
  const [productos, setProductos] = useState([]);
  const [productosFiltrados, setProductosFiltrados] = useState([]);
  const [inventario, setInventario] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [editingProducto, setEditingProducto] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [formData, setFormData] = useState({
    codigo: '',
    nombre: '',
    descripcion: '',
    categoria: '',
    precio: '',
    unidadMedida: 'UNIDAD',
    stockMinimo: '0',
    estado: 'ACTIVO'
  });
  const [message, setMessage] = useState({ type: '', text: '' });

  useEffect(() => {
    loadProductos();
  }, []);

  useEffect(() => {
    setProductosFiltrados(productos);
  }, [productos]);

  const loadProductos = async () => {
    try {
      const [productosRes, inventarioRes] = await Promise.all([
        productoService.getAll(),
        inventarioService.getAll()
      ]);
      setProductos(productosRes.data);
      setInventario(inventarioRes.data);
    } catch (error) {
      setMessage({ type: 'error', text: 'Error al cargar productos' });
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = () => {
    if (!searchTerm.trim()) {
      setProductosFiltrados(productos);
      return;
    }
    const filtered = productos.filter(p => 
      p.nombre?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      p.codigo?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      p.categoria?.toLowerCase().includes(searchTerm.toLowerCase())
    );
    setProductosFiltrados(filtered);
  };

  const handleSearchKeyPress = (e) => {
    if (e.key === 'Enter') {
      handleSearch();
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingProducto) {
        await productoService.update(editingProducto.id, formData);
        setMessage({ type: 'success', text: 'Producto actualizado exitosamente' });
      } else {
        await productoService.create(formData);
        setMessage({ type: 'success', text: 'Producto creado exitosamente' });
      }
      resetForm();
      loadProductos();
    } catch (error) {
      setMessage({ type: 'error', text: getErrorMessage(error, 'Error al guardar producto') });
    }
  };

  const handleEdit = (producto) => {
    setEditingProducto(producto);
    setFormData(producto);
    setShowForm(true);
  };

  const handleDelete = async (id) => {
    if (window.confirm('¿Está seguro de eliminar este producto?')) {
      try {
        await productoService.delete(id);
        setMessage({ type: 'success', text: 'Producto eliminado exitosamente' });
        loadProductos();
      } catch (error) {
        setMessage({ type: 'error', text: 'Error al eliminar producto' });
      }
    }
  };

  const resetForm = () => {
    setFormData({
      codigo: '',
      nombre: '',
      descripcion: '',
      categoria: '',
      precio: '',
      unidadMedida: 'UNIDAD',
      stockMinimo: '0',
      estado: 'ACTIVO'
    });
    setEditingProducto(null);
    setShowForm(false);
  };

  const getStockTotal = (productoId) => {
    const stocks = inventario.filter(inv => inv.productoId === productoId);
    return stocks.reduce((total, inv) => total + inv.cantidadDisponible, 0);
  };

  if (loading) return <div className="loading">Cargando productos...</div>;

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
            <h1 className="page-title">📦 Gestión de Productos</h1>
            <p className="page-subtitle">Administra tu catálogo completo de productos</p>
          </div>
          <div className="header-actions">
            <button className="btn-primary" onClick={() => setShowForm(!showForm)}>
              {showForm ? '✕ Cancelar' : '+ Nuevo Producto'}
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
              placeholder="Buscar por nombre, código o categoría..."
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
            {editingProducto ? '✏️ Editar Producto' : '➕ Nuevo Producto'}
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
                  placeholder="Ej: PROD-001"
                />
              </div>
              <div className="form-group">
                <label>Nombre *</label>
                <input
                  type="text"
                  value={formData.nombre}
                  onChange={(e) => setFormData({ ...formData, nombre: e.target.value })}
                  required
                  placeholder="Nombre del producto"
                />
              </div>
            </div>

            <div className="form-row">
              <div className="form-group">
                <label>Categoría *</label>
                <input
                  type="text"
                  value={formData.categoria}
                  onChange={(e) => setFormData({ ...formData, categoria: e.target.value })}
                  required
                  placeholder="Ej: Electrónica, Alimentos"
                />
              </div>
              <div className="form-group">
                <label>Precio *</label>
                <input
                  type="number"
                  step="0.01"
                  value={formData.precio}
                  onChange={(e) => setFormData({ ...formData, precio: e.target.value })}
                  required
                  placeholder="0.00"
                />
              </div>
            </div>

            <div className="form-row">
              <div className="form-group">
                <label>Unidad de Medida</label>
                <input
                  type="text"
                  value={formData.unidadMedida}
                  onChange={(e) => setFormData({ ...formData, unidadMedida: e.target.value })}
                  placeholder="Ej: UNIDAD, CAJA, KG"
                />
              </div>
              <div className="form-group">
                <label>Stock Mínimo</label>
                <input
                  type="number"
                  value={formData.stockMinimo}
                  onChange={(e) => setFormData({ ...formData, stockMinimo: e.target.value })}
                  placeholder="0"
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
                  <option value="DESCONTINUADO">DESCONTINUADO</option>
                </select>
              </div>
            </div>

            <div className="form-group">
              <label>Descripción</label>
              <textarea
                value={formData.descripcion}
                onChange={(e) => setFormData({ ...formData, descripcion: e.target.value })}
                rows="3"
                placeholder="Descripción detallada del producto..."
              />
            </div>

            <div className="form-actions">
              <button type="submit" className="btn-success">
                {editingProducto ? '💾 Actualizar' : '✓ Crear'} Producto
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
            <h3 className="table-title">Listado de Productos</h3>
            <span className="table-count">{productosFiltrados.length} productos</span>
          </div>

          {productosFiltrados.length === 0 ? (
            <div className="empty-state">
              <div className="empty-state-icon">📦</div>
              <h3>No hay productos registrados</h3>
              <p>Comienza creando un nuevo producto</p>
            </div>
          ) : (
            <table className="modern-table">
              <thead>
                <tr>
                  <th>Código</th>
                  <th>Nombre</th>
                  <th>Categoría</th>
                  <th>Precio</th>
                  <th>Stock Total</th>
                  <th>Stock Mínimo</th>
                  <th>Estado</th>
                  <th>Acciones</th>
                </tr>
              </thead>
              <tbody>
                {productosFiltrados.map((producto) => {
                  const stockTotal = getStockTotal(producto.id);
                  const stockBajo = stockTotal < producto.stockMinimo;
                  return (
                    <tr key={producto.id}>
                      <td><strong>{producto.codigo}</strong></td>
                      <td>{producto.nombre}</td>
                      <td>{producto.categoria}</td>
                      <td>${producto.precio?.toFixed(2)}</td>
                      <td>
                        <span className={`stock-indicator ${stockBajo ? 'stock-low' : 'stock-ok'}`}>
                          {stockBajo && <span className="stock-warning-icon">⚠️</span>}
                          <strong>{stockTotal}</strong>
                        </span>
                      </td>
                      <td>{producto.stockMinimo}</td>
                      <td>
                        <span className={`status-badge status-${producto.estado.toLowerCase()}`}>
                          {producto.estado}
                        </span>
                      </td>
                      <td>
                        <div className="action-buttons">
                          <button className="btn-edit" onClick={() => handleEdit(producto)}>
                            ✏️ Editar
                          </button>
                          <button className="btn-delete" onClick={() => handleDelete(producto.id)}>
                            🗑️ Eliminar
                          </button>
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

export default ProductosPage;
