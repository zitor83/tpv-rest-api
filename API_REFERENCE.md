# Referencia de la API REST - EasyTPV

Documento oficial de contrato entre backend y frontend para consumo de la API.

---

## 1) Estructura Estandar de Respuesta (`ApiResponse`)

Todas las respuestas del backend (exito y error) se envuelven en `ApiResponse<T>`.

Campos base del contrato:
- `success` (boolean)
- `message` (string)
- `data` (object | array | string | null)
- `timestamp` (ISO-8601 UTC)

> Nota tecnica: el objeto `ApiResponse` tambien soporta `meta` de forma opcional, pero solo aparece cuando aplica.

### Ejemplo de exito
```json
{
  "success": true,
  "message": "Operacion completada",
  "data": {
    "id": 1,
    "name": "Producto ejemplo"
  },
  "timestamp": "2026-03-23T18:00:00.123Z"
}
```

### Ejemplo de error
```json
{
  "success": false,
  "message": "Error de validacion",
  "data": {
    "name": "El nombre es obligatorio",
    "price": "Debe ser mayor que 0"
  },
  "timestamp": "2026-03-23T18:00:00.123Z"
}
```

---

## 2) Autenticacion (JWT)

### Login para obtener token
- Endpoint: `POST /api/auth/login`
- Resultado: token JWT en el payload de respuesta del login.

### Uso del token en peticiones siguientes
Enviar el JWT en la cabecera HTTP:

```http
Authorization: Bearer <token>
```

La API es stateless: no usa sesion de servidor.

---

## 3) Manejo de Errores (HTTP Codes)

### `400 Bad Request`
Errores de validacion (Jakarta Validation) y errores de entrada mal formada.

En validaciones, `GlobalExceptionHandler` retorna un mapa en `data` con formato:
- clave: nombre del campo
- valor: mensaje de validacion

Ejemplo:
```json
{
  "success": false,
  "message": "Error de validacion",
  "data": {
    "email": "Debe tener un formato valido",
    "password": "No puede estar vacio"
  },
  "timestamp": "2026-03-23T18:00:00.123Z"
}
```

### `401 Unauthorized`
Falta token o el token es invalido/expirado.

Gestionado por `CustomAuthenticationEntryPoint` en la capa de seguridad.

Ejemplo de mensaje:
```json
{
  "success": false,
  "message": "No autorizado. Token faltante o inválido.",
  "data": null,
  "timestamp": "2026-03-23T18:00:00.123Z"
}
```

### `403 Forbidden`
El token es valido pero el usuario no tiene permisos suficientes para ese recurso.

Gestionado por `CustomAccessDeniedHandler`.

Ejemplo de mensaje:
```json
{
  "success": false,
  "message": "Acceso denegado. No tienes permisos para acceder a este recurso.",
  "data": null,
  "timestamp": "2026-03-23T18:00:00.123Z"
}
```

### `404 Not Found`
Recurso no encontrado (por ejemplo, `ResourceNotFoundException`).

Ejemplo:
```json
{
  "success": false,
  "message": "Recurso no encontrado",
  "data": null,
  "timestamp": "2026-03-23T18:00:00.123Z"
}
```

### `500 Internal Server Error`
Error inesperado del servidor.

`GlobalExceptionHandler` devuelve mensaje generico y detalle tecnico en `data`.

Ejemplo:
```json
{
  "success": false,
  "message": "Error interno del servidor",
  "data": "Detalle tecnico del error",
  "timestamp": "2026-03-23T18:00:00.123Z"
}
```

---

## 4) Documentacion Interactiva

Para ver el catalogo completo de endpoints, payloads y probar peticiones en vivo:

- `http://localhost:8081/swagger-ui/index.html`

---

## 5) Referencia Tecnica de Implementacion

Este contrato se alinea con el comportamiento actual implementado en:
- `src/main/java/com/ortiz/tpv_backend/infrastructure/rest/ApiResponse.java`
- `src/main/java/com/ortiz/tpv_backend/infrastructure/config/SecurityConfig.java`
- `src/main/java/com/ortiz/tpv_backend/infrastructure/security/CustomAuthenticationEntryPoint.java`
- `src/main/java/com/ortiz/tpv_backend/infrastructure/security/CustomAccessDeniedHandler.java`
- `src/main/java/com/ortiz/tpv_backend/infrastructure/rest/GlobalExceptionHandler.java`
