import { Routes, Route } from 'react-router-dom'
import Layout from './components/Layout'
import HomePage from './pages/HomePage'
import DashboardPage from './pages/DashboardPage'
import ProductosPage from './pages/ProductosPage'
import ProveedoresPage from './pages/ProveedoresPage'
import BodegasPage from './pages/BodegasPage'
import InventarioPage from './pages/InventarioPage'
import OrdenesCompraPage from './pages/OrdenesCompraPage'
import './App.css'

function App() {
  return (
    <Layout>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/dashboard" element={<DashboardPage />} />
        <Route path="/productos" element={<ProductosPage />} />
        <Route path="/proveedores" element={<ProveedoresPage />} />
        <Route path="/bodegas" element={<BodegasPage />} />
        <Route path="/inventario" element={<InventarioPage />} />
        <Route path="/ordenes-compra" element={<OrdenesCompraPage />} />
      </Routes>
    </Layout>
  )
}

export default App
