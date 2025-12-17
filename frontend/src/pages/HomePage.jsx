import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { productoService, proveedorService, inventarioService, ordenCompraService } from '../services/api.service';
import './HomePage.css';

function HomePage() {
  const [loading, setLoading] = useState(true);
  const [stats, setStats] = useState({
    productos: 0,
    proveedores: 0,
    inventario: 0,
    ordenesPendientes: 0,
    productosActivos: 0,
    proveedoresActivos: 0
  });

  useEffect(() => {
    loadStats();
  }, []);

  const loadStats = async () => {
    try {
      setLoading(true);
      const [productos, proveedores, inventario, ordenes] = await Promise.all([
        productoService.getAll(),
        proveedorService.getAll(),
        inventarioService.getAll(),
        ordenCompraService.getAll(),
      ]);

      const ordenesPendientes = ordenes.data.filter(o => o.estado === 'PENDIENTE').length;
      const productosActivos = productos.data.filter(p => p.estado === 'ACTIVO').length;
      const proveedoresActivos = proveedores.data.filter(p => p.estado === 'ACTIVO').length;

      setStats({
        productos: productos.data.length,
        proveedores: proveedores.data.length,
        inventario: inventario.data.length,
        ordenesPendientes,
        productosActivos,
        proveedoresActivos
      });
    } catch (error) {
      console.error('Error al cargar estadísticas', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <div className="loading">Cargando estadísticas...</div>;

  return (
    <div className="home-container">
      {/* Hero Section */}
      <div className="hero-section">
        <div className="hero-content">
          <h1 className="hero-title">
            <span className="gradient-text">Sistema de Gestión</span>
            <br />
            de Inventario
          </h1>
          <p className="hero-subtitle">
            Controla tu inventario, proveedores y órdenes de compra desde una plataforma centralizada y moderna
          </p>
        </div>
        <div className="hero-decoration">
          <div className="floating-icon">📦</div>
          <div className="floating-icon delay-1">🚚</div>
          <div className="floating-icon delay-2">📊</div>
        </div>
      </div>

      {/* Stats Cards */}
      <div className="modern-stats-grid">
        <div className="modern-stat-card blue">
          <div className="stat-icon">📦</div>
          <div className="stat-content">
            <div className="stat-number">{stats.productos}</div>
            <div className="stat-label">Total Productos</div>
            <div className="stat-detail">{stats.productosActivos} activos</div>
          </div>
          <div className="stat-trend">↗</div>
        </div>

        <div className="modern-stat-card purple">
          <div className="stat-icon">🚚</div>
          <div className="stat-content">
            <div className="stat-number">{stats.proveedores}</div>
            <div className="stat-label">Proveedores</div>
            <div className="stat-detail">{stats.proveedoresActivos} activos</div>
          </div>
          <div className="stat-trend">↗</div>
        </div>

        <div className="modern-stat-card green">
          <div className="stat-icon">📊</div>
          <div className="stat-content">
            <div className="stat-number">{stats.inventario}</div>
            <div className="stat-label">Registros Inventario</div>
            <div className="stat-detail">En todas las bodegas</div>
          </div>
          <div className="stat-trend">―</div>
        </div>

        <div className="modern-stat-card orange">
          <div className="stat-icon">⏰</div>
          <div className="stat-content">
            <div className="stat-number">{stats.ordenesPendientes}</div>
            <div className="stat-label">Órdenes Pendientes</div>
            <div className="stat-detail">Por aprobar</div>
          </div>
          <div className="stat-trend">{stats.ordenesPendientes > 0 ? '↑' : '―'}</div>
        </div>
      </div>

      {/* Quick Actions */}
      <div className="quick-actions-section">
        <h2 className="section-title">Acciones Rápidas</h2>
        <div className="actions-grid">
          <Link to="/productos" className="action-card">
            <div className="action-icon-wrapper blue-bg">
              <span className="action-icon">📦</span>
            </div>
            <h3 className="action-title">Gestionar Productos</h3>
            <p className="action-description">Administra tu catálogo de productos</p>
            <span className="action-arrow">→</span>
          </Link>

          <Link to="/proveedores" className="action-card">
            <div className="action-icon-wrapper purple-bg">
              <span className="action-icon">🚚</span>
            </div>
            <h3 className="action-title">Gestionar Proveedores</h3>
            <p className="action-description">Control de proveedores y contactos</p>
            <span className="action-arrow">→</span>
          </Link>

          <Link to="/inventario" className="action-card">
            <div className="action-icon-wrapper green-bg">
              <span className="action-icon">📊</span>
            </div>
            <h3 className="action-title">Ver Inventario</h3>
            <p className="action-description">Consulta stock en todas las bodegas</p>
            <span className="action-arrow">→</span>
          </Link>

          <Link to="/ordenes" className="action-card">
            <div className="action-icon-wrapper orange-bg">
              <span className="action-icon">🛒</span>
            </div>
            <h3 className="action-title">Órdenes de Compra</h3>
            <p className="action-description">Gestiona el flujo de compras</p>
            <span className="action-arrow">→</span>
          </Link>
        </div>
      </div>

      {/* Features Section */}
      <div className="features-section">
        <h2 className="section-title">Características del Sistema</h2>
        <div className="features-grid">
          <div className="feature-item">
            <div className="feature-icon">⚡</div>
            <h4>Actualización en Tiempo Real</h4>
            <p>Stock actualizado automáticamente al recibir órdenes</p>
          </div>
          <div className="feature-item">
            <div className="feature-icon">🔒</div>
            <h4>Flujo de Aprobación</h4>
            <p>Sistema de aprobación para órdenes de compra</p>
          </div>
          <div className="feature-item">
            <div className="feature-icon">📍</div>
            <h4>Ubicación Automática</h4>
            <p>Asignación automática de ubicaciones en bodega</p>
          </div>
          <div className="feature-item">
            <div className="feature-icon">📈</div>
            <h4>Reportes Avanzados</h4>
            <p>Análisis de gastos, productos críticos y más</p>
          </div>
        </div>
      </div>
    </div>
  );
}

export default HomePage;
