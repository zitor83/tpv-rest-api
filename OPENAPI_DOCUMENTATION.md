# 📚 OpenAPI 3.0 Documentation - TPV Backend

## Overview

Todos los controladores REST del TPV Backend han sido documentados completamente con **springdoc-openapi** siguiendo el estándar OpenAPI 3.0. Esta documentación se genera automáticamente en **Swagger UI** y **OpenAPI JSON**.

## 📍 Acceso a la Documentación

- **Swagger UI interactivo**: `http://localhost:8081/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8081/v3/api-docs`
- **OpenAPI YAML**: `http://localhost:8081/v3/api-docs.yaml`

## 🏷️ Categorías de Endpoints

### 1. **Authentication** (`/api/auth`)
Gestión de autenticación y sesiones con JWT.

| Método | Endpoint | Descripción | Autenticación |
|--------|----------|-------------|---------------|
| POST | `/login` | Iniciar sesión y obtener JWT | ❌ Pública |
| GET | `/me` | Validar sesión actual | ✅ JWT Requerido |

**Respuestas Documentadas:**
- ✅ 200/201: Operación exitosa
- ❌ 400: Validación fallida
- ❌ 401: No autenticado/Token inválido
- ❌ 403: Acceso denegado

---

### 2. **Products** (`/api/products`)
Gestión del catálogo de productos.

| Método | Endpoint | Descripción | Autenticación |
|--------|----------|-------------|---------------|
| GET | `/` | Listar todos los productos | ❌ Pública |
| POST | `/` | Crear nuevo producto | ❌ Pública |

**Ejemplos de Payloads:**

**POST /api/products**
```json
{
  "name": "Hamburguesa Clásica",
  "price": 8.50,
  "description": "Pan integral, carne 150g, queso, lechuga",
  "imageUrl": "https://cdn.example.com/burger.jpg",
  "available": true,
  "defaultIngredients": ["Pan integral", "Carne", "Queso", "Lechuga"]
}
```

**GET /api/products**
```json
{
  "success": true,
  "message": "Productos obtenidos correctamente",
  "data": [
    {
      "id": "prod-uuid-1",
      "name": "Hamburguesa Clásica",
      "price": 8.50,
      "description": "...",
      "imageUrl": "...",
      "available": true,
      "defaultIngredients": [...]
    }
  ],
  "timestamp": "2026-03-23T10:30:00"
}
```

---

### 3. **Orders** (`/api/orders`)
Gestión de tickets/órdenes de venta.

| Método | Endpoint | Descripción | Autenticación |
|--------|----------|-------------|---------------|
| POST | `/` | Crear nuevo ticket | ✅ JWT Requerido |
| GET | `/{id}` | Obtener detalle de ticket | ✅ JWT Requerido |
| GET | `/` | Listar todos los tickets | ✅ JWT Requerido |

**Ejemplos de Payloads:**

**POST /api/orders**
```json
{
  "lines": [
    {
      "productId": "prod-uuid-1",
      "quantity": 2
    },
    {
      "productId": "prod-uuid-3",
      "quantity": 1
    }
  ]
}
```

**GET /api/orders (Respuesta)**
```json
{
  "success": true,
  "message": "Ticket creado correctamente",
  "data": {
    "id": "order-uuid-1",
    "createdAt": "2026-03-23T10:30:00",
    "totalAmount": 20.50,
    "tableNumber": "Mesa 5",
    "isForTakeaway": false,
    "customerName": null,
    "status": "active",
    "paymentMethod": null,
    "tip": 0.00,
    "lines": [
      {
        "productId": "prod-uuid-1",
        "productName": "Hamburguesa Clásica",
        "quantity": 2,
        "unitPrice": 8.50,
        "subTotal": 17.00,
        "notes": null,
        "selectedIngredients": {}
      }
    ]
  },
  "timestamp": "2026-03-23T10:30:00"
}
```

**Errores Específicos:**
- 404 Not Found: Producto en orden no existe en catálogo
- 400 Bad Request: Líneas vacías o cantidades inválidas

---

### 4. **Expenses** (`/api/expenses`)
Registro de gastos operacionales.

| Método | Endpoint | Descripción | Autenticación |
|--------|----------|-------------|---------------|
| POST | `/` | Registrar nuevo gasto | ✅ JWT Requerido |
| DELETE | `/{id}` | Eliminar gasto | ✅ JWT Requerido |

**Ejemplos de Payloads:**

**POST /api/expenses**
```json
{
  "amount": 150.00,
  "description": "Compra de suministros de limpieza",
  "date": "2026-03-23T10:00:00"
}
```

**Respuesta:**
```json
{
  "success": true,
  "message": "Gasto creado correctamente",
  "data": {
    "id": "exp-uuid-1",
    "amount": 150.00,
    "description": "Compra de suministros de limpieza",
    "date": "2026-03-23T10:00:00",
    "createdAt": "2026-03-23T10:30:00"
  },
  "timestamp": "2026-03-23T10:30:00"
}
```

---

### 5. **Financial Reports** (`/api/reports`)
Generación de reportes financieros.

| Método | Endpoint | Descripción | Autenticación |
|--------|----------|-------------|---------------|
| GET | `/daily/{date}` | Reporte diario | ✅ JWT Requerido |
| GET | `/monthly?year=&month=` | Reporte mensual | ✅ JWT Requerido |

**Ejemplos:**

**GET /api/reports/daily/2026-03-23**
```json
{
  "success": true,
  "message": "Reporte diario obtenido correctamente",
  "data": {
    "date": "2026-03-23",
    "totalSales": 500.50,
    "totalExpenses": 150.00,
    "netProfit": 350.50,
    "orderCount": 10,
    "expenseCount": 3
  },
  "timestamp": "2026-03-23T23:59:59"
}
```

**GET /api/reports/monthly?year=2026&month=3**
```json
{
  "success": true,
  "message": "Reporte mensual obtenido correctamente",
  "data": {
    "month": 3,
    "year": 2026,
    "totalSales": 15000.00,
    "totalExpenses": 4500.00,
    "netProfit": 10500.00,
    "orderCount": 300,
    "dailyBreakdown": []
  },
  "timestamp": "2026-03-31T23:59:59"
}
```

---

## 🔐 Autenticación

Todos los endpoints protegidos requieren un token JWT en el header `Authorization`:

```http
GET /api/orders HTTP/1.1
Host: localhost:8081
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### Obtener Token

1. **POST /api/auth/login** con credenciales:
```json
{
  "username": "admin",
  "password": "password123"
}
```

2. **Respuesta contiene el JWT:**
```json
{
  "success": true,
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "username": "admin",
    "role": "ROLE_ADMIN"
  }
}
```

3. **Usar token en Swagger UI:** Botón "Authorize" → Pega token como `Bearer <token>`

---

## 📊 Estructura de Respuestas

Todas las respuestas siguen el patrón `ApiResponse<T>`:

```java
{
  "success": boolean,           // true si fue exitosa, false si hubo error
  "message": string,            // Mensaje legible del resultado
  "data": T,                    // Dato real (DTO específico o null)
  "timestamp": ISO8601-datetime // Timestamp del servidor
}
```

### Ejemplos:

**Respuesta Exitosa (200 OK):**
```json
{
  "success": true,
  "message": "Producto creado correctamente",
  "data": {
    "id": "prod-123",
    "name": "Café Americano",
    "price": 3.50
  },
  "timestamp": "2026-03-23T10:30:45.123Z"
}
```

**Respuesta de Error (400 Bad Request):**
```json
{
  "success": false,
  "message": "Validación fallida",
  "data": null,
  "timestamp": "2026-03-23T10:30:45.123Z"
}
```

---

## 🧪 Probar Endpoints en Swagger UI

1. Abre `http://localhost:8081/swagger-ui.html`
2. Haz clic en "Authorize" e inyecta tu token JWT (con prefijo "Bearer ")
3. Expande cualquier endpoint (ej: "POST /api/products")
4. Haz clic en "Try it out"
5. Rellena los parámetros/body y presiona "Execute"
6. Verás respuesta, headers y código de estado

---

## 📝 Convenciones de Documentación

### Por Tipo de Endpoint:

| Tipo | Method | Códigos | Autenticación |
|------|--------|---------|---------------|
| **CREATE** | POST | 201, 400, 401 | ✅ Generalmente sí |
| **READ** | GET | 200, 404, 401 | ⚠️ Depende del recurso |
| **DELETE** | DELETE | 200, 404, 401 | ✅ Generalmente sí |
| **LIST** | GET | 200, 401 | ⚠️ Depende del recurso |

### Códigos HTTP Estándar:

- **200 OK**: Solicitud exitosa (GET, PUT, DELETE)
- **201 Created**: Recurso creado exitosamente (POST)
- **400 Bad Request**: Validación o parámetros inválidos
- **401 Unauthorized**: Token faltante, inválido o expirado
- **403 Forbidden**: Token válido pero sin permisos
- **404 Not Found**: Recurso no existe
- **500 Internal Server Error**: Error del servidor

---

## 🔄 Flujo de Ejemplo: Crear Venta

```
1. POST /api/auth/login
   ↓ (Recibe JWT)

2. POST /api/orders (con JWT en header)
   Request: { lines: [{ productId: "...", quantity: 2 }] }
   ↓
   Backend:
   - Busca producto en BD
   - Extrae precio real
   - Crea OrderLine
   - Calcula total
   ↓
   Response: { data: { id, totalAmount, lines: [...] } }

3. GET /api/reports/daily/2026-03-23 (con JWT)
   ↓
   Response: { data: { totalSales: 500.50, ... } }
```

---

## 📚 Referencias

- [springdoc-openapi Official Docs](https://springdoc.org/)
- [OpenAPI 3.0 Specification](https://spec.openapis.org/oas/v3.0.3)
- [Swagger UI User Guide](https://swagger.io/tools/swagger-ui/)

---

## ✅ Checklist de Documentación por Controller

- [x] **ProductController**: @Tag, @Operation, @ApiResponses
- [x] **AuthController**: @Tag, @Operation, @SecurityRequirement, @ApiResponses
- [x] **OrderController**: @Tag, @Operation, @SecurityRequirement, @ApiResponses
- [x] **ExpenseController**: @Tag, @Operation, @SecurityRequirement, @ApiResponses
- [x] **ReportController**: @Tag, @Operation, @SecurityRequirement, @ApiResponses

---

**Último Commit:** `docs(api): add comprehensive openapi 3.0 documentation to all REST controllers`

**Generado:** 23 de Marzo de 2026

