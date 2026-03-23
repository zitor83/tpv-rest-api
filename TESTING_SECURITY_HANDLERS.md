# 🧪 TESTING GUIDE: CustomAuthenticationEntryPoint y CustomAccessDeniedHandler

## Escenario 1: Token Faltante (401 - Unauthorized)

### Prueba
```bash
curl -X GET http://localhost:8081/api/products
```

### Respuesta Esperada (ANTES)
```json
{
  "timestamp": "2026-03-23T12:00:00Z",
  "status": 401,
  "error": "Unauthorized",
  "path": "/api/products"
}
```

### Respuesta Esperada (DESPUÉS)
```json
{
  "success": false,
  "message": "No autorizado. Token faltante o inválido.",
  "data": null,
  "timestamp": "2026-03-23T12:00:00.000Z"
}
```

---

## Escenario 2: Token Inválido (401 - Unauthorized)

### Prueba
```bash
curl -X GET http://localhost:8081/api/products \
  -H "Authorization: Bearer invalid_token_xyz"
```

### Respuesta Esperada
```json
{
  "success": false,
  "message": "No autorizado. Token faltante o inválido.",
  "data": null,
  "timestamp": "2026-03-23T12:00:00.000Z"
}
```

---

## Escenario 3: Token Válido pero Permisos Insuficientes (403 - Forbidden)

### Pre-requisitos
1. Obtener token de ROLE_USER
2. Intentar acceder a endpoint que requiere ROLE_ADMIN

### Paso 1: Obtener token
```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user",
    "password": "user_password"
  }'
```

**Respuesta:**
```json
{
  "success": true,
  "message": "Autenticación correcta. JWT emitido.",
  "data": {
    "token": "eyJhbGc...",
    "username": "user",
    "role": "ROLE_USER"
  },
  "timestamp": "2026-03-23T12:00:00.000Z"
}
```

### Paso 2: Intentar acceder con permisos insuficientes
Supongamos que existe un endpoint protegido:
```java
@DeleteMapping("/{id}")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<ApiResponse<Void>> deleteExpense(@PathVariable String id) {
    // ...
}
```

**Prueba:**
```bash
curl -X DELETE http://localhost:8081/api/expenses/some-id \
  -H "Authorization: Bearer <token_del_usuario_user>"
```

### Respuesta Esperada (ANTES)
```json
{
  "timestamp": "2026-03-23T12:00:00Z",
  "status": 403,
  "error": "Access Denied",
  "path": "/api/expenses/some-id"
}
```

### Respuesta Esperada (DESPUÉS)
```json
{
  "success": false,
  "message": "Acceso denegado. No tienes permisos para acceder a este recurso.",
  "data": null,
  "timestamp": "2026-03-23T12:00:00.000Z"
}
```

---

## Escenario 4: Token Válido con Permisos Correctos (200 - OK)

### Prueba
```bash
curl -X GET http://localhost:8081/api/products \
  -H "Authorization: Bearer <token_del_usuario_admin>"
```

### Respuesta Esperada
```json
{
  "success": true,
  "message": "Productos obtenidos correctamente",
  "data": [
    {
      "id": "product-1",
      "name": "Pizza Margherita",
      "price": 12.99,
      ...
    }
  ],
  "timestamp": "2026-03-23T12:00:00.000Z"
}
```

---

## Resumen de Status HTTP

| Escenario | Status | Manejador | Estructura |
|-----------|--------|-----------|-----------|
| Token faltante/inválido | 401 | `CustomAuthenticationEntryPoint` | ApiResponse ✅ |
| Token válido, permisos insuficientes | 403 | `CustomAccessDeniedHandler` | ApiResponse ✅ |
| Token válido, permisos suficientes | 200/201 | Controlador | ApiResponse ✅ |
| Validación de entrada falla | 400 | `GlobalExceptionHandler` | ApiResponse ✅ |

---

## Verificación de Implementación

### Archivo 1: CustomAuthenticationEntryPoint
- ✅ Implementa `AuthenticationEntryPoint`
- ✅ Override `commence()`
- ✅ Escribe JSON con `ApiResponse.error()`
- ✅ Status 401

### Archivo 2: CustomAccessDeniedHandler
- ✅ Implementa `AccessDeniedHandler`
- ✅ Override `handle()`
- ✅ Escribe JSON con `ApiResponse.error()`
- ✅ Status 403

### Archivo 3: SecurityConfig
- ✅ Inyecta ambos handlers
- ✅ Registra en `.exceptionHandling()`
- ✅ `.authenticationEntryPoint(authenticationEntryPoint)`
- ✅ `.accessDeniedHandler(accessDeniedHandler)`

---

## Notas Importantes

### ObjectMapper estático
Ambos handlers usan un `ObjectMapper` estático para serializar `ApiResponse<Void>` a JSON.
Esto es seguro porque:
- `ObjectMapper` es thread-safe
- Se reutiliza en toda la aplicación
- Evita crear instancias innecesarias

### Content-Type
Ambos handlers establecen explícitamente:
```java
response.setContentType(MediaType.APPLICATION_JSON_VALUE); // "application/json"
```

Esto garantiza que el cliente interprete la respuesta como JSON.

### HttpStatus vs. HttpServletResponse
- `HttpServletResponse.SC_UNAUTHORIZED` = 401
- `HttpServletResponse.SC_FORBIDDEN` = 403

Se usa `HttpServletResponse` en lugar de `HttpStatus` porque estamos escribiendo directamente en el response en la capa de filtros, antes de que Spring construya la respuesta HTTP.

---

