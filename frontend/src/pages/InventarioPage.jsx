import { useState, useEffect } from 'react';
import { inventarioService, productoService, bodegaService } from '../services/api.service';
import './CrudPage.css';

function InventarioPage() {
  const [inventario, setInventario] = useState([]);
  const [productos, setProductos] = useState([]);
  const [bodegas, setBodegas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState({ type: '', text: '' });

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      setLoading(true);
      const [inventarioRes, productosRes, bodegasRes] = await Promise.all([
        inventarioService.getAll(),
        productoService.getAll(),
        bodegaService.getAll()
      ]);
      setInventario(inventarioRes.data);
      setProductos(productosRes.data);
      setBodegas(bodegasRes.data);
    } catch (error) {
      setMessage({ type: 'error', text: 'Error al cargar datos' });
    } finally {
      setLoading(false);
    }
  };

  const getProductoNombre = (productoId) => {
    const producto = productos.find(p => p.id === productoId);
    return producto ? producto.nombre : productoId;
  };

  const getBodegaNombre = (bodegaId) => {
    const bodega = bodegas.find(b => b.id === bodegaId);
    return bodega ? bodega.nombre : 'Bodega #' + bodegaId;
  };

  if (loading) return <div className="loading">Cargando inventario...</div>;

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
            <h1 className="page-title">📊 Consulta de Inventario</h1>
            <p className="page-subtitle">Visualiza el stock disponible en todas las bodegas</p>
          </div>
        </div>
      </div>
        
      {/* Table Section */}
      <div className="table-section">
        <div className="table-header">
          <h3 className="table-title">Stock por Bodega</h3>
          <span className="table-count">{inventario.length} registros</span>
        </div>

        {inventario.length === 0 ? (
          <div className="empty-state">
            <div className="empty-state-icon">📊</div>
            <h3>No hay inventario registrado</h3>
            <p>El inventario se actualiza automáticamente al recibir órdenes de compra</p>
          </div>
        ) : (
          <table className="modern-table">
            <thead>
              <tr>
                <th>Producto</th>
                <th>Bodega</th>
                <th>Ubicación</th>
                <th>Cantidad Disponible</th>
              </tr>
            </thead>
            <tbody>
              {inventario.map((item) => (
                <tr key={item.id}>
                  <td><strong>{getProductoNombre(item.productoId)}</strong></td>
                  <td>{getBodegaNombre(item.bodegaId)}</td>
                  <td>{item.ubicacion || '-'}</td>
                  <td>
                    <span style={{
                      color: '#2196F3', 
                      fontWeight: 'bold', 
                      fontSize: '18px',
                      background: '#e3f2fd',
                      padding: '6px 12px',
                      borderRadius: '8px'
                    }}>
                      {item.cantidadDisponible}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}

export default InventarioPage;
