import { Link, useLocation } from 'react-router-dom'
import './Layout.css'

function Layout({ children }) {
  const location = useLocation()

  const isActive = (path) => {
    return location.pathname === path ? 'active' : ''
  }

  return (
    <div className="layout">
      <nav className="navbar">
        <div className="nav-brand">
          <h2>Sistema de Inventario</h2>
        </div>
        <ul className="nav-links">
          <li>
            <Link to="/" className={isActive('/')}>
              Inicio
            </Link>
          </li>
          <li>
            <Link to="/dashboard" className={isActive('/dashboard')}>
              Dashboard
            </Link>
          </li>
          <li>
            <Link to="/productos" className={isActive('/productos')}>
              Productos
            </Link>
          </li>
          <li>
            <Link to="/proveedores" className={isActive('/proveedores')}>
              Proveedores
            </Link>
          </li>
          <li>
            <Link to="/bodegas" className={isActive('/bodegas')}>
              Bodegas
            </Link>
          </li>
          <li>
            <Link to="/inventario" className={isActive('/inventario')}>
              Inventario
            </Link>
          </li>
          <li>
            <Link to="/ordenes-compra" className={isActive('/ordenes-compra')}>
              Órdenes de Compra
            </Link>
          </li>
        </ul>
      </nav>
      <main className="main-content">
        {children}
      </main>
    </div>
  )
}

export default Layout
