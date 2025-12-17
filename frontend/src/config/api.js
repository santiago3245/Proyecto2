import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost';

const api = {
  productos: axios.create({
    baseURL: `${API_BASE_URL}:8081`,
    headers: { 'Content-Type': 'application/json' }
  }),
  proveedores: axios.create({
    baseURL: `${API_BASE_URL}:8082`,
    headers: { 'Content-Type': 'application/json' }
  }),
  inventario: axios.create({
    baseURL: `${API_BASE_URL}:8083`,
    headers: { 'Content-Type': 'application/json' }
  }),
  ordenesCompra: axios.create({
    baseURL: `${API_BASE_URL}:8084`,
    headers: { 'Content-Type': 'application/json' }
  })
};

export default api;
