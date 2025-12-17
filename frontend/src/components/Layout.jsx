import { Link, useLocation } from 'react-router-dom';

function Layout({ children }) {
  const location = useLocation();

  const isActive = (path) => {
    return location.pathname === path ? 'active' : '';
  };

  return (
    <>
      <header>
        <h1>Sistema de Gestión de Inventario</h1>
      </header>

      <nav>
        <ul>
          <li><Link to="/" className={isActive('/')}>Home</Link></li>
          <li><Link to="/dashboard" className={isActive('/dashboard')}>📊 Dashboard</Link></li>
          <li><Link to="/productos" className={isActive('/productos')}>Productos</Link></li>
          <li><Link to="/proveedores" className={isActive('/proveedores')}>Proveedores</Link></li>
          <li><Link to="/inventario" className={isActive('/inventario')}>Inventario</Link></li>
          <li><Link to="/bodegas" className={isActive('/bodegas')}>Bodegas</Link></li>
          <li><Link to="/ordenes" className={isActive('/ordenes')}>Órdenes de Compra</Link></li>
        </ul>
      </nav>

      <div className="container">
        {children}
      </div>
    </>
  );
}

export default Layout;
