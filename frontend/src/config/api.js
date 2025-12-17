import axios from 'axios'

const BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost'

export const productoAPI = axios.create({
  baseURL: `${BASE_URL}:8081/api/productos`,
  headers: {
    'Content-Type': 'application/json'
  }
})

export const proveedorAPI = axios.create({
  baseURL: `${BASE_URL}:8082/api/proveedores`,
  headers: {
    'Content-Type': 'application/json'
  }
})

export const inventarioAPI = axios.create({
  baseURL: `${BASE_URL}:8083/api`,
  headers: {
    'Content-Type': 'application/json'
  }
})

export const ordenCompraAPI = axios.create({
  baseURL: `${BASE_URL}:8084/api/ordenes-compra`,
  headers: {
    'Content-Type': 'application/json'
  }
})

// Interceptores para manejo de errores global
const errorInterceptor = (error) => {
  if (error.response) {
    console.error('Error de respuesta:', error.response.data)
  } else if (error.request) {
    console.error('Error de request:', error.request)
  } else {
    console.error('Error:', error.message)
  }
  return Promise.reject(error)
}

productoAPI.interceptors.response.use(response => response, errorInterceptor)
proveedorAPI.interceptors.response.use(response => response, errorInterceptor)
inventarioAPI.interceptors.response.use(response => response, errorInterceptor)
ordenCompraAPI.interceptors.response.use(response => response, errorInterceptor)
