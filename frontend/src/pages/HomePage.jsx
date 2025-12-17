import { Link } from 'react-router-dom'

function HomePage() {
  return (
    <div className="container">
      <div className="card" style={{ textAlign: 'center', padding: '60px' }}>
        <h1>Bienvenido al Sistema de Gestión de Inventario</h1>
        <p style={{ fontSize: '18px', color: '#7f8c8d', margin: '30px 0' }}>
          Gestiona productos, proveedores, bodegas, inventario y órdenes de compra en un solo lugar.
        </p>
        
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '20px', marginTop: '40px' }}>
          <Link to="/dashboard" style={{ textDecoration: 'none' }}>
            <div className="card" style={{ padding: '30px', backgroundColor: '#3498db', color: 'white', cursor: 'pointer' }}>
              <h3 style={{ color: 'white', marginBottom: '10px' }}>📊 Dashboard</h3>
              <p style={{ margin: 0 }}>Ver estadísticas y reportes</p>
            </div>
          </Link>
          
          <Link to="/productos" style={{ textDecoration: 'none' }}>
            <div className="card" style={{ padding: '30px', backgroundColor: '#9b59b6', color: 'white', cursor: 'pointer' }}>
              <h3 style={{ color: 'white', marginBottom: '10px' }}>📦 Productos</h3>
              <p style={{ margin: 0 }}>Gestionar catálogo</p>
            </div>
          </Link>
          
          <Link to="/proveedores" style={{ textDecoration: 'none' }}>
            <div className="card" style={{ padding: '30px', backgroundColor: '#e67e22', color: 'white', cursor: 'pointer' }}>
              <h3 style={{ color: 'white', marginBottom: '10px' }}>🏢 Proveedores</h3>
              <p style={{ margin: 0 }}>Gestionar proveedores</p>
            </div>
          </Link>
          
          <Link to="/bodegas" style={{ textDecoration: 'none' }}>
            <div className="card" style={{ padding: '30px', backgroundColor: '#16a085', color: 'white', cursor: 'pointer' }}>
              <h3 style={{ color: 'white', marginBottom: '10px' }}>🏭 Bodegas</h3>
              <p style={{ margin: 0 }}>Gestionar ubicaciones</p>
            </div>
          </Link>
          
          <Link to="/inventario" style={{ textDecoration: 'none' }}>
            <div className="card" style={{ padding: '30px', backgroundColor: '#27ae60', color: 'white', cursor: 'pointer' }}>
              <h3 style={{ color: 'white', marginBottom: '10px' }}>📋 Inventario</h3>
              <p style={{ margin: 0 }}>Consultar stock</p>
            </div>
          </Link>
          
          <Link to="/ordenes-compra" style={{ textDecoration: 'none' }}>
            <div className="card" style={{ padding: '30px', backgroundColor: '#c0392b', color: 'white', cursor: 'pointer' }}>
              <h3 style={{ color: 'white', marginBottom: '10px' }}>🛒 Órdenes</h3>
              <p style={{ margin: 0 }}>Gestionar compras</p>
            </div>
          </Link>
        </div>
      </div>
    </div>
  )
}

export default HomePage
