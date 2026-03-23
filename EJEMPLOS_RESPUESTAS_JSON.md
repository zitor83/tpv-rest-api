# 📋 EJEMPLOS DE RESPUESTAS: Security Handlers

## 🎯 Objetivo
Este documento muestra ejemplos reales de cómo el API responde en cada escenario de seguridad.

---

## 1️⃣ Escenario: Token Faltante (401)

### Request
```bash
curl -X GET http://localhost:8081/api/products
```

### Response
```
HTTP/1.1 401 Unauthorized
Content-Type: application/json

{
  "success": false,
  "message": "No autorizado. Token faltante o inválido.",
  "data": null,
  "timestamp": "2026-03-23T14:00:00.000Z"
}
```

### Manejador
`CustomAuthenticationEntryPoint.commence()`

### Razón
El header `Authorization` no está presente en la petición.

---

## 2️⃣ Escenario: Token Expirado (401)

### Request
```bash
curl -X GET http://localhost:8081/api/products \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNjc0ODk0NDAwLCJleHAiOjE2NzQ4OTgwMDB9.EXPIRED_TOKEN"
```

### Response
```
HTTP/1.1 401 Unauthorized
Content-Type: application/json

{
  "success": false,
  "message": "No autorizado. Token faltante o inválido.",
  "data": null,
  "timestamp": "2026-03-23T14:00:00.000Z"
}
```

### Manejador
`CustomAuthenticationEntryPoint.commence()`

### Razón
El token está expirado. `TokenProviderPort.isValid()` retorna `false`.

---

## 3️⃣ Escenario: Token Inválido/Corrupto (401)

### Request
```bash
curl -X GET http://localhost:8081/api/products \
  -H "Authorization: Bearer not.a.valid.jwt.token"
```

### Response
```
HTTP/1.1 401 Unauthorized
Content-Type: application/json

{
  "success": false,
  "message": "No autorizado. Token faltante o inválido.",
  "data": null,
  "timestamp": "2026-03-23T14:00:00.000Z"
}
```

### Manejador
`CustomAuthenticationEntryPoint.commence()`

### Razón
El token no es un JWT válido. La firma no coincide o el formato es incorrecto.

---

## 4️⃣ Escenario: Token Válido, Rol Insuficiente (403)

### Prerequisitos
```bash
# 1. Usuario con ROLE_USER intenta eliminar un gasto
# 2. El endpoint DELETE /api/expenses/{id} tiene @PreAuthorize("hasRole('ADMIN')")
```

### Request
```bash
# Primero obtener token de usuario ROLE_USER
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user",
    "password": "user_password"
  }'

# Respuesta:
# {
#   "success": true,
#   "message": "Autenticación correcta. JWT emitido.",
#   "data": {
#     "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZSI6IlJPTEVfVVNFUiJ9.XYZABC",
#     "username": "user",
#     "role": "ROLE_USER"
#   },
#   "timestamp": "2026-03-23T14:00:00.000Z"
# }

# Luego intentar acceso ADMIN con token de USER
curl -X DELETE http://localhost:8081/api/expenses/expense-123 \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZSI6IlJPTEVfVVNFUiJ9.XYZABC"
```

### Response
```
HTTP/1.1 403 Forbidden
Content-Type: application/json

{
  "success": false,
  "message": "Acceso denegado. No tienes permisos para acceder a este recurso.",
  "data": null,
  "timestamp": "2026-03-23T14:00:00.000Z"
}
```

### Manejador
`CustomAccessDeniedHandler.handle()`

### Razón
El usuario tiene `ROLE_USER` pero el endpoint requiere `ROLE_ADMIN`. Spring Security evalúa `@PreAuthorize("hasRole('ADMIN')")` y falla.

---

## 5️⃣ Escenario: Token Válido, Rol Correcto (200/201)

### Request
```bash
# Usuario ROLE_ADMIN obtiene su token
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin_password"
  }'

# Respuesta:
# {
#   "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGUiOiJST0xFX0FETUlOIn0.ADMIN_TOKEN",
#   "username": "admin",
#   "role": "ROLE_ADMIN"
# }

# Acceder a endpoint protegido con rol correcto
curl -X GET http://localhost:8081/api/products \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGUiOiJST0xFX0FETUlOIn0.ADMIN_TOKEN"
```

### Response
```
HTTP/1.1 200 OK
Content-Type: application/json

{
  "success": true,
  "message": "Productos obtenidos correctamente",
  "data": [
    {
      "id": "product-1",
      "name": "Pizza Margherita",
      "price": 12.99,
      "category": "Pizzas",
      "description": "Pizza clásica italiana",
      "imageUrl": "https://example.com/pizza.jpg",
      "available": true,
      "defaultIngredients": ["tomate", "mozzarella", "albahaca"]
    },
    {
      "id": "product-2",
      "name": "Pasta Carbonara",
      "price": 14.50,
      "category": "Pastas",
      "description": "Pasta cremosa",
      "imageUrl": "https://example.com/carbonara.jpg",
      "available": true,
      "defaultIngredients": ["huevo", "queso", "jamón"]
    }
  ],
  "timestamp": "2026-03-23T14:00:00.000Z"
}
```

### Manejador
Controlador (`ProductController.listProducts()`)

### Razón
El usuario está autenticado con `ROLE_ADMIN` (o cualquier rol permitido), y no hay restricción de autorización en este endpoint. La petición se procesa normalmente.

---

## 6️⃣ Escenario: Error de Validación (400)

### Request
```bash
# Crear producto sin campos requeridos
curl -X POST http://localhost:8081/api/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token_valido>" \
  -d '{
    "price": 12.99
    # Faltan: name, category, description, imageUrl, defaultIngredients
  }'
```

### Response
```
HTTP/1.1 400 Bad Request
Content-Type: application/json

{
  "success": false,
  "message": "Error de validacion",
  "data": {
    "name": "El nombre no puede estar vacío",
    "category": "El campo categoría es obligatorio",
    "description": "La descripción es requerida",
    "imageUrl": "La URL de la imagen es obligatoria",
    "defaultIngredients": "Los ingredientes por defecto son requeridos"
  },
  "timestamp": "2026-03-23T14:00:00.000Z"
}
```

### Manejador
`GlobalExceptionHandler.handleValidationErrors()`

### Razón
Spring valida la anotación `@Valid` en el DTO y falla la validación de campos anotados con `@NotBlank`, `@NotNull`, etc.

---

## 7️⃣ Escenario: Recurso No Encontrado (404)

### Request
```bash
# Obtener un producto que no existe
curl -X GET http://localhost:8081/api/products/non-existent-id \
  -H "Authorization: Bearer <token_valido>"
```

### Response
```
HTTP/1.1 404 Not Found
Content-Type: application/json

{
  "success": false,
  "message": "Producto no encontrado con id: non-existent-id",
  "data": null,
  "timestamp": "2026-03-23T14:00:00.000Z"
}
```

### Manejador
`GlobalExceptionHandler.handleResourceNotFound()`

### Razón
El Use Case lanzó `ResourceNotFoundException` cuando el producto no existe en la base de datos.

---

## 8️⃣ Escenario: Error Interno del Servidor (500)

### Request
```bash
# Una operación que causa un error inesperado
curl -X POST http://localhost:8081/api/orders \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token_valido>" \
  -d '{
    "tableNumber": "1",
    "isForTakeaway": false,
    "orderLines": [{"productId": "product-1", "quantity": 1}]
  }'
# (Si la BD tiene un error inesperado)
```

### Response
```
HTTP/1.1 500 Internal Server Error
Content-Type: application/json

{
  "success": false,
  "message": "Error interno del servidor",
  "data": "Connection timeout connecting to localhost:5432",
  "timestamp": "2026-03-23T14:00:00.000Z"
}
```

### Manejador
`GlobalExceptionHandler.handleGeneralException()`

### Razón
Se lanzó una excepción no controlada. Se captura y devuelve como error interno (sin exponer detalles sensibles en producción).

---

## 📊 Tabla Resumen

| Escenario | Status | Manejador | Primer Campo |
|-----------|--------|-----------|----------------|
| Sin token | 401 | `CustomAuthenticationEntryPoint` | "No autorizado..." |
| Token expirado | 401 | `CustomAuthenticationEntryPoint` | "No autorizado..." |
| Token inválido | 401 | `CustomAuthenticationEntryPoint` | "No autorizado..." |
| Rol insuficiente | 403 | `CustomAccessDeniedHandler` | "Acceso denegado..." |
| Validación falla | 400 | `GlobalExceptionHandler` | "Error de validacion" |
| Recurso no existe | 404 | `GlobalExceptionHandler` | "Producto no encontrado..." |
| Error interno | 500 | `GlobalExceptionHandler` | "Error interno del servidor" |
| Éxito | 200/201 | Controlador | "Productos obtenidos..." |

---

## ✅ Estructura ApiResponse (Siempre)

```json
{
  "success": boolean,           // true en éxito, false en error
  "message": string,            // Descripción clara
  "data": object/array/null,    // Datos devueltos (null si no hay)
  "timestamp": ISO8601          // "2026-03-23T14:00:00.000Z"
}
```

**Conclusión:** Todas las respuestas (2xx, 4xx, 5xx) usan la misma estructura JSON predecible. ✅


