import { useState, useEffect } from 'react';
import { ordenCompraService, proveedorService, productoService, bodegaService } from '../services/api.service';
import { getErrorMessage } from '../utils/errorHandler';
import './CrudPage.css';

function OrdenesCompraPage() {
  const [ordenes, setOrdenes] = useState([]);
  const [proveedores, setProveedores] = useState([]);
  const [productos, setProductos] = useState([]);
  const [bodegas, setBodegas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [formData, setFormData] = useState({
    numeroOrden: '',
    proveedorId: '',
    bodegaId: '',
    fechaEntregaEsperada: '',
    estado: 'PENDIENTE',
    observaciones: '',
    detalles: []
  });
  const [detalleTemp, setDetalleTemp] = useState({
    productoId: '',
    cantidad: '1',
    precioUnitario: ''
  });
  const [message, setMessage] = useState({ type: '', text: '' });

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      const [ordenesRes, proveedoresRes, productosRes, bodegasRes] = await Promise.all([
        ordenCompraService.getAll(),
        proveedorService.getAll(),
        productoService.getAll(),
        bodegaService.getAll()
      ]);
      setOrdenes(ordenesRes.data);
      setProveedores(proveedoresRes.data);
      setProductos(productosRes.data);
      setBodegas(bodegasRes.data);
    } catch (error) {
      setMessage({ type: 'error', text: 'Error al cargar datos' });
    } finally {
      setLoading(false);
    }
  };

  const agregarDetalle = () => {
    if (!detalleTemp.productoId || !detalleTemp.cantidad || !detalleTemp.precioUnitario) {
      alert('Complete todos los campos del detalle');
      return;
    }

    const producto = productos.find(p => p.id === parseInt(detalleTemp.productoId));
    setFormData({
      ...formData,
      detalles: [...formData.detalles, {
        ...detalleTemp,
        productoId: parseInt(detalleTemp.productoId),
        cantidad: parseInt(detalleTemp.cantidad),
        precioUnitario: parseFloat(detalleTemp.precioUnitario),
        productoNombre: producto?.nombre
      }]
    });

    setDetalleTemp({
      productoId: '',
      cantidad: '1',
      precioUnitario: ''
    });
  };

  const eliminarDetalle = (index) => {
    const nuevosDetalles = formData.detalles.filter((_, i) => i !== index);
    setFormData({ ...formData, detalles: nuevosDetalles });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (formData.detalles.length === 0) {
      alert('Debe agregar al menos un detalle a la orden');
      return;
    }

    try {
      const ordenData = {
        ...formData,
        proveedorId: parseInt(formData.proveedorId),
        bodegaId: parseInt(formData.bodegaId)
      };
      
      if (isEditing) {
        await ordenCompraService.update(editingId, ordenData);
        setMessage({ type: 'success', text: 'Orden de compra actualizada exitosamente' });
      } else {
        await ordenCompraService.create(ordenData);
        setMessage({ type: 'success', text: 'Orden de compra creada exitosamente' });
      }
      
      resetForm();
      loadData();
    } catch (error) {
      setMessage({ type: 'error', text: getErrorMessage(error, isEditing ? 'Error al actualizar orden' : 'Error al crear orden') });
    }
  };

  const handleEditar = (orden) => {
    setIsEditing(true);
    setEditingId(orden.id);
    setFormData({
      numeroOrden: orden.numeroOrden,
      proveedorId: orden.proveedorId,
      bodegaId: orden.bodegaId,
      fechaEntregaEsperada: orden.fechaEntregaEsperada || '',
      estado: orden.estado,
      observaciones: orden.observaciones || '',
      detalles: orden.detalles.map(d => ({
        productoId: d.productoId,
        cantidad: d.cantidad,
        precioUnitario: d.precioUnitario,
        productoNombre: productos.find(p => p.id === d.productoId)?.nombre || 'Producto'
      }))
    });
    setShowForm(true);
  };

  const handleAprobar = async (id) => {
    try {
      await ordenCompraService.aprobar(id);
      setMessage({ type: 'success', text: 'Orden aprobada exitosamente' });
      loadData();
    } catch (error) {
      setMessage({ type: 'error', text: getErrorMessage(error, 'Error al aprobar orden') });
    }
  };

  const handleRecibir = async (id) => {
    if (window.confirm('¿Confirma la recepción de esta orden? Se actualizará el inventario automáticamente.')) {
      try {
        await ordenCompraService.recibir(id);
        setMessage({ type: 'success', text: 'Orden recibida e inventario actualizado exitosamente' });
        loadData();
      } catch (error) {
        setMessage({ type: 'error', text: getErrorMessage(error, 'Error al recibir orden') });
      }
    }
  };

  const handleCancelar = async (id) => {
    if (window.confirm('¿Está seguro de cancelar esta orden?')) {
      try {
        await ordenCompraService.cancelar(id);
        setMessage({ type: 'success', text: 'Orden cancelada exitosamente' });
        loadData();
      } catch (error) {
        setMessage({ type: 'error', text: getErrorMessage(error, 'Error al cancelar orden') });
      }
    }
  };

  const resetForm = () => {
    setFormData({
      numeroOrden: '',
      proveedorId: '',
      bodegaId: '',
      fechaEntregaEsperada: '',
      estado: 'PENDIENTE',
      observaciones: '',
      detalles: []
    });
    setDetalleTemp({
      productoId: '',
      cantidad: '1',
      precioUnitario: ''
    });
    setShowForm(false);
    setIsEditing(false);
    setEditingId(null);
  };

  const getProveedorNombre = (proveedorId) => {
    const proveedor = proveedores.find(p => p.id === proveedorId);
    return proveedor ? proveedor.razonSocial : 'N/A';
  };

  const getBodegaNombre = (bodegaId) => {
    const bodega = bodegas.find(b => b.id === bodegaId);
    return bodega ? bodega.nombre : 'N/A';
  };

  const getBadgeClass = (estado) => {
    const classes = {
      'PENDIENTE': 'badge-warning',
      'APROBADA': 'badge-info',
      'RECIBIDA': 'badge-success',
      'CANCELADA': 'badge-danger'
    };
    return classes[estado] || 'badge-info';
  };

  if (loading) return <div className="loading">Cargando órdenes de compra...</div>;

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
            <h1 className="page-title">🛒 Gestión de Órdenes de Compra</h1>
            <p className="page-subtitle">Control completo del flujo de compras</p>
          </div>
          <div className="header-actions">
            <button className="btn-primary" onClick={() => setShowForm(!showForm)}>
              {showForm ? '✕ Cancelar' : '+ Nueva Orden'}
            </button>
          </div>
        </div>
      </div>

      {showForm && (
        <div className="form-section">
          <h3 className="form-title">{isEditing ? '✏️ Editar Orden de Compra' : '➕ Nueva Orden de Compra'}</h3>
          <form onSubmit={handleSubmit}>
            <div className="form-row">
              <div className="form-group">
                <label>Número de Orden *</label>
                <input
                  type="text"
                  value={formData.numeroOrden}
                  onChange={(e) => setFormData({ ...formData, numeroOrden: e.target.value })}
                  placeholder="Ej: OC-2024-001"
                  required
                />
              </div>
              <div className="form-group">
                <label>Proveedor *</label>
                <select
                  value={formData.proveedorId}
                  onChange={(e) => setFormData({ ...formData, proveedorId: e.target.value })}
                  required
                >
                  <option value="">Seleccione un proveedor</option>
                  {proveedores.filter(p => p.estado === 'ACTIVO').map((proveedor) => (
                    <option key={proveedor.id} value={proveedor.id}>
                      {proveedor.razonSocial}
                    </option>
                  ))}
                </select>
              </div>
            </div>

            <div className="form-row">
              <div className="form-group">
                <label>Bodega Destino *</label>
                <select
                  value={formData.bodegaId}
                  onChange={(e) => setFormData({ ...formData, bodegaId: e.target.value })}
                  required
                >
                  <option value="">Seleccione una bodega</option>
                  {bodegas.filter(b => b.estado === 'ACTIVO').map((bodega) => (
                    <option key={bodega.id} value={bodega.id}>
                      {bodega.nombre}
                    </option>
                  ))}
                </select>
              </div>
              <div className="form-group">
                <label>Fecha Entrega Esperada</label>
                <input
                  type="datetime-local"
                  value={formData.fechaEntregaEsperada}
                  onChange={(e) => setFormData({ ...formData, fechaEntregaEsperada: e.target.value })}
                />
              </div>
            </div>

            <div className="form-group">
              <label>Observaciones</label>
              <textarea
                value={formData.observaciones}
                onChange={(e) => setFormData({ ...formData, observaciones: e.target.value })}
                rows="2"
              />
            </div>

            <div className="detalles-section">
              <h4 className="detalles-header">📦 Detalles de la Orden</h4>

              <div className="detalle-input-row">
                <div className="form-group">
                  <label>Producto</label>
                  <select
                    value={detalleTemp.productoId}
                    onChange={(e) => {
                      const producto = productos.find(p => p.id === parseInt(e.target.value));
                      setDetalleTemp({
                        ...detalleTemp,
                        productoId: e.target.value,
                        precioUnitario: producto?.precio || ''
                      });
                    }}
                  >
                    <option value="">Seleccione un producto</option>
                    {productos.filter(p => p.estado === 'ACTIVO').map((producto) => (
                      <option key={producto.id} value={producto.id}>
                        {producto.codigo} - {producto.nombre} (${producto.precio})
                      </option>
                    ))}
                  </select>
                </div>
                <div className="form-group">
                  <label>Cantidad</label>
                  <input
                    type="number"
                    value={detalleTemp.cantidad}
                    onChange={(e) => setDetalleTemp({ ...detalleTemp, cantidad: e.target.value })}
                    min="1"
                  />
                </div>
                <div className="form-group">
                  <label>Precio Unitario</label>
                  <input
                    type="number"
                    step="0.01"
                    value={detalleTemp.precioUnitario}
                    onChange={(e) => setDetalleTemp({ ...detalleTemp, precioUnitario: e.target.value })}
                  />
                </div>
                <div style={{ display: 'flex', alignItems: 'flex-end' }}>
                  <button type="button" className="btn-primary" onClick={agregarDetalle}>+ Agregar</button>
                </div>
              </div>

              {formData.detalles.length > 0 && (
                <table className="detalles-table">
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
                      <td>${(detalle.cantidad * detalle.precioUnitario).toFixed(2)}</td>
                      <td>
                        <button type="button" className="danger" onClick={() => eliminarDetalle(index)}>
                          Eliminar
                        </button>
                      </td>
                    </tr>
                  ))}
                  <tr>
                    <td colSpan="3" style={{ textAlign: 'right', fontWeight: 'bold' }}>Total:</td>
                    <td style={{ textAlign: 'right' }}>
                      <span style={{ fontSize: '18px', fontWeight: 'bold', color: '#667eea' }}>
                        ${formData.detalles.reduce((sum, d) => sum + (d.cantidad * d.precioUnitario), 0).toFixed(2)}
                      </span>
                    </td>
                    <td></td>
                  </tr>
                </tbody>
              </table>
              )}
            </div>

            <div className="form-actions">
              <button type="submit" className="btn-success">✓ Crear Orden</button>
              <button type="button" className="btn-cancel" onClick={resetForm}>Cancelar</button>
            </div>
          </form>
        </div>
      )}

      {!showForm && (
        <div className="table-section">
          <div className="table-header">
            <h3 className="table-title">Listado de Órdenes</h3>
            <span className="table-count">{ordenes.length} órdenes</span>
          </div>

          {ordenes.length === 0 ? (
            <div className="empty-state">
              <div className="empty-state-icon">🛒</div>
              <h3>No hay órdenes de compra registradas</h3>
              <p>Comienza creando una nueva orden</p>
            </div>
          ) : (
            <table className="modern-table">
            <thead>
              <tr>
                <th>Número</th>
                <th>Proveedor</th>
                <th>Bodega</th>
                <th>Total</th>
                <th>Estado</th>
                <th>Acciones</th>
              </tr>
            </thead>
              <tbody>
                {ordenes.map((orden) => (
                  <tr key={orden.id}>
                    <td><strong>{orden.numeroOrden}</strong></td>
                    <td>{getProveedorNombre(orden.proveedorId)}</td>
                    <td>{getBodegaNombre(orden.bodegaId)}</td>
                    <td><strong>${orden.total?.toFixed(2)}</strong></td>
                    <td>
                      <span className={`status-badge status-${orden.estado.toLowerCase()}`}>
                        {orden.estado}
                      </span>
                    </td>
                    <td>
                      <div className="action-buttons">
                        {orden.estado === 'PENDIENTE' && (
                          <>
                            <button className="btn-edit" onClick={() => handleEditar(orden)}>✏️ Editar</button>
                            <button className="btn-approve" onClick={() => handleAprobar(orden.id)}>✓ Aprobar</button>
                            <button className="btn-delete" onClick={() => handleCancelar(orden.id)}>✕ Cancelar</button>
                          </>
                        )}
                        {orden.estado === 'APROBADA' && (
                          <>
                            <button className="btn-receive" onClick={() => handleRecibir(orden.id)}>📦 Recibir</button>
                            <button className="btn-delete" onClick={() => handleCancelar(orden.id)}>✕ Cancelar</button>
                          </>
                        )}
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

export default OrdenesCompraPage;
