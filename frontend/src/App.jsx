import { Routes, Route } from 'react-router-dom';
import Layout from './components/Layout';
import ProductosPage from './pages/ProductosPage';
import ProveedoresPage from './pages/ProveedoresPage';
import InventarioPage from './pages/InventarioPage';
import OrdenesCompraPage from './pages/OrdenesCompraPage';
import BodegasPage from './pages/BodegasPage';
import HomePage from './pages/HomePage';
import DashboardPage from './pages/DashboardPage';

function App() {
  return (
    <Layout>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/dashboard" element={<DashboardPage />} />
        <Route path="/productos" element={<ProductosPage />} />
        <Route path="/proveedores" element={<ProveedoresPage />} />
        <Route path="/inventario" element={<InventarioPage />} />
        <Route path="/bodegas" element={<BodegasPage />} />
        <Route path="/ordenes" element={<OrdenesCompraPage />} />
      </Routes>
    </Layout>
  );
}

export default App;
