import React, { useState, useEffect } from 'react';
import { productoService, ordenCompraService, inventarioService, proveedorService } from '../services/api.service';
import '../styles/Dashboard.css';

const DashboardPage = () => {
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [estadisticas, setEstadisticas] = useState({});
  const [productosCriticos, setProductosCriticos] = useState([]);
  const [gastosPorProveedor, setGastosPorProveedor] = useState([]);
  const [historialOrdenes, setHistorialOrdenes] = useState([]);
  const [inventario, setInventario] = useState([]);
  const [proveedores, setProveedores] = useState([]);

  useEffect(() => {
    loadDashboardData();
  }, []);

  const loadDashboardData = async () => {
    try {
      setLoading(true);
      const [
        productosRes,
        inventarioRes,
        ordenesRes,
        estadisticasRes,
        gastosRes,
        proveedoresRes
      ] = await Promise.all([
        productoService.getProductosCriticos(),
        inventarioService.getAll(),
        ordenCompraService.getHistorial(),
        ordenCompraService.getEstadisticas(),
        ordenCompraService.getGastosPorProveedor(),
        proveedorService.getAll()
      ]);

      // Extraer data de las respuestas (axios envuelve en .data)
      const productos = Array.isArray(productosRes.data) ? productosRes.data : [];
      const inventario = Array.isArray(inventarioRes.data) ? inventarioRes.data : [];
      const ordenes = Array.isArray(ordenesRes.data) ? ordenesRes.data : [];
      const stats = estadisticasRes.data || {};
      const gastos = Array.isArray(gastosRes.data) ? gastosRes.data : [];
      const proveedoresData = Array.isArray(proveedoresRes.data) ? proveedoresRes.data : [];

      setInventario(inventario);
      setProveedores(proveedoresData);
      
      // Calcular stock total por producto
      const stockPorProducto = {};
      inventario.forEach(item => {
        if (!stockPorProducto[item.productoId]) {
          stockPorProducto[item.productoId] = 0;
        }
        stockPorProducto[item.productoId] += item.cantidadDisponible;
      });

      // Filtrar productos críticos (con stock bajo o cero)
      const criticos = productos.filter(p => {
        const stock = stockPorProducto[p.id] || 0;
        return stock < p.stockMinimo;
      });

      setProductosCriticos(criticos);
      setHistorialOrdenes(ordenes.slice(0, 10)); // Últimas 10 órdenes
      setEstadisticas(stats);
      setGastosPorProveedor(gastos.slice(0, 5)); // Top 5 proveedores
      setError(null);
    } catch (error) {
      console.error('Error cargando dashboard:', error);
      setError('Error al cargar los datos del dashboard');
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <div className="loading">Cargando dashboard...</div>;
  }

  if (error) {
    return <div className="error-message">{error}</div>;
  }

  const getProveedorNombre = (proveedorId) => {
    const proveedor = proveedores.find(p => p.id === proveedorId);
    return proveedor ? proveedor.razonSocial : `Proveedor #${proveedorId}`;
  };

  const formatearFecha = (fecha) => {
    if (!fecha) return 'Sin fecha';
    try {
      const date = new Date(fecha);
      if (isNaN(date.getTime())) return 'Fecha inválida';
      return date.toLocaleDateString('es-EC');
    } catch (error) {
      return 'Fecha inválida';
    }
  };

  return (
    <div className="dashboard-container">
      <h1>📊 Dashboard de Reportes</h1>
      
      {/* Tarjetas de estadísticas */}
      <div className="stats-cards">
        <div className="stat-card blue">
          <div className="stat-icon">📦</div>
          <div className="stat-content">
            <h3>Total Órdenes</h3>
            <p className="stat-number">{estadisticas.totalOrdenes || 0}</p>
          </div>
        </div>

        <div className="stat-card green">
          <div className="stat-icon">✅</div>
          <div className="stat-content">
            <h3>Recibidas</h3>
            <p className="stat-number">{estadisticas.ordenesRecibidas || 0}</p>
          </div>
        </div>

        <div className="stat-card yellow">
          <div className="stat-icon">⏳</div>
          <div className="stat-content">
            <h3>Pendientes</h3>
            <p className="stat-number">{estadisticas.ordenesPendientes || 0}</p>
          </div>
        </div>

        <div className="stat-card red">
          <div className="stat-icon">⚠️</div>
          <div className="stat-content">
            <h3>Productos Críticos</h3>
            <p className="stat-number">{productosCriticos.length}</p>
          </div>
        </div>

        <div className="stat-card purple">
          <div className="stat-icon">💰</div>
          <div className="stat-content">
            <h3>Total Gastado</h3>
            <p className="stat-number">${(estadisticas.totalGastado || 0).toLocaleString('es-EC', {minimumFractionDigits: 2, maximumFractionDigits: 2})}</p>
          </div>
        </div>
      </div>

      {/* Grid de reportes */}
      <div className="reports-grid">
        {/* Productos Críticos */}
        <div className="report-card">
          <h2>🚨 Productos Críticos (Stock Bajo)</h2>
          {productosCriticos.length === 0 ? (
            <p className="empty-state">✅ No hay productos con stock crítico</p>
          ) : (
            <div className="table-container">
              <table>
                <thead>
                  <tr>
                    <th>Código</th>
                    <th>Producto</th>
                    <th>Stock Actual</th>
                    <th>Stock Mínimo</th>
                    <th>Estado</th>
                  </tr>
                </thead>
                <tbody>
                  {productosCriticos.map((producto) => {
                    const stockTotal = inventario
                      .filter(i => i.productoId === producto.id)
                      .reduce((sum, i) => sum + i.cantidadDisponible, 0);
                    
                    return (
                      <tr key={producto.id}>
                        <td>{producto.codigo}</td>
                        <td>{producto.nombre}</td>
                        <td>
                          <strong style={{color: stockTotal === 0 ? '#dc3545' : '#ff9800'}}>
                            {stockTotal}
                          </strong>
                        </td>
                        <td>{producto.stockMinimo}</td>
                        <td>
                          <span className="badge badge-danger">
                            {stockTotal === 0 ? 'SIN STOCK' : 'BAJO'}
                          </span>
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          )}
        </div>

        {/* Gastos por Proveedor */}
        <div className="report-card">
          <h2>💵 Top 5 Gastos por Proveedor</h2>
          {gastosPorProveedor.length === 0 ? (
            <p className="empty-state">📭 No hay datos de gastos</p>
          ) : (
            <div className="chart-container">
              {gastosPorProveedor.map((gasto, index) => {
                const maxGasto = Math.max(...gastosPorProveedor.map(g => g.totalGastado));
                const percentage = (gasto.totalGastado / maxGasto) * 100;
                
                return (
                  <div key={index} className="bar-chart-item">
                    <div className="bar-label">
                      <span className="bar-rank">#{index + 1}</span>
                      <span className="bar-name">{gasto.proveedorNombre}</span>
                      <span className="bar-value">
                        ${gasto.totalGastado.toLocaleString('es-EC', {minimumFractionDigits: 2})}
                      </span>
                    </div>
                    <div className="bar-wrapper">
                      <div 
                        className="bar-fill" 
                        style={{width: `${percentage}%`}}
                        title={`${gasto.cantidadOrdenes} órdenes`}
                      >
                        <span className="bar-orders">{gasto.cantidadOrdenes} órdenes</span>
                      </div>
                    </div>
                  </div>
                );
              })}
            </div>
          )}
        </div>

        {/* Historial de Órdenes */}
        <div className="report-card full-width">
          <h2>📋 Historial Reciente de Órdenes de Compra</h2>
          {historialOrdenes.length === 0 ? (
            <p className="empty-state">📭 No hay órdenes registradas</p>
          ) : (
            <div className="table-container">
              <table>
                <thead>
                  <tr>
                    <th>Número</th>
                    <th>Fecha</th>
                    <th>Proveedor</th>
                    <th>Total</th>
                    <th>Estado</th>
                  </tr>
                </thead>
                <tbody>
                  {historialOrdenes.map((orden) => (
                    <tr key={orden.id}>
                      <td><strong>{orden.numeroOrden}</strong></td>
                      <td>{formatearFecha(orden.fechaOrden)}</td>
                      <td><strong>{getProveedorNombre(orden.proveedorId)}</strong></td>
                      <td><strong>${orden.total.toLocaleString('es-EC', {minimumFractionDigits: 2})}</strong></td>
                      <td>
                        <span className={`badge badge-${
                          orden.estado === 'RECIBIDA' ? 'success' : 
                          orden.estado === 'APROBADA' ? 'primary' : 
                          orden.estado === 'PENDIENTE' ? 'warning' : 'danger'
                        }`}>
                          {orden.estado}
                        </span>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>

      <div className="dashboard-footer">
        <button onClick={loadDashboardData} className="btn-refresh">
          🔄 Actualizar Dashboard
        </button>
      </div>
    </div>
  );
};

export default DashboardPage;
