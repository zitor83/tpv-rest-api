
# 📡 Referencia de la API REST - EasyTPV

Documento oficial de contrato entre backend y frontend para consumo de la API.

---

## 📦 1. Estructura Estándar de Respuesta (`ApiResponse`)

Todas las respuestas del backend (tanto de éxito como de error) se envuelven siempre en el objeto `ApiResponse<T>`.

**Campos base del contrato:**
- `success` (boolean): Indica si la operación fue exitosa.
- `message` (string): Mensaje legible para el usuario o desarrollador.
- `data` (object | array | string | null): El payload real (datos solicitados o detalles del error).
- `timestamp` (ISO-8601 UTC): Fecha y hora exacta de la respuesta del servidor.

> **💡 Nota técnica:** El objeto `ApiResponse` también soporta un campo `meta` de forma opcional (útil para paginación o resúmenes), pero solo aparece cuando aplica.

### ✅ Ejemplo de éxito (200 OK / 201 Created)

    {
      "success": true,
      "message": "Operación completada",
      "data": {
        "id": 1,
        "name": "Producto de ejemplo"
      },
      "timestamp": "2026-03-23T18:00:00.123Z"
    }

### ❌ Ejemplo de error

    {
      "success": false,
      "message": "Error de validación",
      "data": {
        "name": "El nombre es obligatorio",
        "price": "Debe ser mayor que 0"
      },
      "timestamp": "2026-03-23T18:00:00.123Z"
    }

---

## 🔐 2. Autenticación (JWT)

Nuestra API es **stateless**: no utiliza sesiones en memoria del servidor. Todo se basa en el token.

### Obtener el Token (Login)
- **Endpoint:** `POST /api/auth/login`
- **Resultado:** Si las credenciales son correctas, el token JWT vendrá dentro del objeto `data` de la respuesta.

### Uso del Token en peticiones siguientes
Para acceder a los endpoints protegidos, debes inyectar el JWT en la cabecera HTTP de tu petición exactamente con este formato:

    Authorization: Bearer <tu_token_aqui>

---

## 🚨 3. Manejo de Errores (HTTP Codes)

El backend está diseñado para devolver respuestas coherentes según el tipo de fallo.

### `400 Bad Request` (Validaciones fallidas)
Se lanza cuando falla la validación de Jakarta o hay errores de entrada mal formada.
El `GlobalExceptionHandler` intercepta el fallo y retorna un mapa dentro de `data` donde la clave es el nombre del campo y el valor es el error.

    {
      "success": false,
      "message": "Error de validación",
      "data": {
        "email": "Debe tener un formato válido",
        "password": "No puede estar vacío"
      },
      "timestamp": "2026-03-23T18:00:00.123Z"
    }

### `401 Unauthorized` (Problemas de Autenticación)
Falta el token en la cabecera, tiene un formato incorrecto o ya ha expirado.
Gestionado por nuestro `CustomAuthenticationEntryPoint`.

    {
      "success": false,
      "message": "No autorizado. Token faltante o inválido.",
      "data": null,
      "timestamp": "2026-03-23T18:00:00.123Z"
    }

### `403 Forbidden` (Problemas de Autorización)
El token es perfectamente válido, pero el usuario no tiene los permisos o el rol necesario para ejecutar esa acción específica.
Gestionado por nuestro `CustomAccessDeniedHandler`.

    {
      "success": false,
      "message": "Acceso denegado. No tienes permisos para acceder a este recurso.",
      "data": null,
      "timestamp": "2026-03-23T18:00:00.123Z"
    }

### `404 Not Found` (Recurso no encontrado)
El ID solicitado no existe en la base de datos (por ejemplo, al lanzar un `ResourceNotFoundException` desde un Caso de Uso).

    {
      "success": false,
      "message": "Recurso no encontrado",
      "data": null,
      "timestamp": "2026-03-23T18:00:00.123Z"
    }

### `500 Internal Server Error` (Fallo crítico)
Error inesperado del servidor (caída de base de datos, null pointer, etc.).
El `GlobalExceptionHandler` lo atrapa para evitar exponer la traza de Java completa y devuelve un mensaje genérico.

    {
      "success": false,
      "message": "Error interno del servidor",
      "data": "Detalle técnico del error (solo en desarrollo)",
      "timestamp": "2026-03-23T18:00:00.123Z"
    }

---

## 📖 4. Documentación Interactiva

Para ver el catálogo completo de endpoints, explorar los esquemas exactos de los DTOs y probar las peticiones en vivo contra el servidor local:

Visita: `http://localhost:8081/swagger-ui/index.html`

---

## ⚙️ 5. Referencia Técnica de Implementación

Para los desarrolladores backend, este contrato se alinea con el comportamiento actual implementado en las siguientes clases de infraestructura:
- `infrastructure/rest/ApiResponse.java`
- `infrastructure/config/SecurityConfig.java`
- `infrastructure/security/CustomAuthenticationEntryPoint.java`
- `infrastructure/security/CustomAccessDeniedHandler.java`
- `infrastructure/rest/GlobalExceptionHandler.java`