export const getErrorMessage = (error) => {
  if (error.response) {
    // El servidor respondió con un código de estado fuera del rango 2xx
    const { data, status } = error.response
    
    // Si el backend devuelve un mensaje de error estructurado
    if (data && data.message) {
      return data.message
    }
    
    // Si el backend devuelve un error como string
    if (typeof data === 'string') {
      return data
    }
    
    // Si hay un campo 'error' en la respuesta
    if (data && data.error) {
      return data.error
    }
    
    // Mensajes por código de estado HTTP
    switch (status) {
      case 400:
        return 'Datos inválidos. Por favor verifica la información ingresada.'
      case 404:
        return 'Recurso no encontrado.'
      case 409:
        return 'Conflicto: El recurso ya existe o hay una restricción que impide la operación.'
      case 500:
        return 'Error interno del servidor. Por favor intenta de nuevo más tarde.'
      default:
        return `Error del servidor (código ${status})`
    }
  } else if (error.request) {
    // La solicitud fue hecha pero no se recibió respuesta
    return 'No se pudo conectar con el servidor. Verifica tu conexión.'
  } else {
    // Algo sucedió al configurar la solicitud
    return error.message || 'Error desconocido al procesar la solicitud.'
  }
}
