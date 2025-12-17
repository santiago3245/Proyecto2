// Función para extraer mensajes de error de las respuestas del servidor
export const getErrorMessage = (error, defaultMessage = 'Error en la operación') => {
  // Si no hay respuesta del servidor
  if (!error.response) {
    return 'Error de conexión con el servidor';
  }

  const { data } = error.response;

  // Si data es un string simple, retornarlo
  if (typeof data === 'string') {
    return data;
  }

  // Si data es un objeto ErrorResponse del backend
  if (data && typeof data === 'object') {
    // Si tiene el campo 'errors' con múltiples errores de validación
    if (data.errors && typeof data.errors === 'object') {
      const errorMessages = Object.entries(data.errors)
        .map(([field, message]) => `${field}: ${message}`)
        .join('; ');
      return errorMessages || data.error || defaultMessage;
    }

    // Si solo tiene el campo 'error'
    if (data.error) {
      return data.error;
    }

    // Si tiene un campo 'message'
    if (data.message) {
      return data.message;
    }
  }

  // Mensaje por defecto
  return defaultMessage;
};
