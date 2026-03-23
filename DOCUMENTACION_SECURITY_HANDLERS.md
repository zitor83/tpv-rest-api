# 📚 DOCUMENTACIÓN TÉCNICA: Handlers de Seguridad Personalizados

## 📋 Tabla de Contenidos
1. [Visión General](#visión-general)
2. [CustomAuthenticationEntryPoint](#customauthenticationentrypoint)
3. [CustomAccessDeniedHandler](#customaccessdeniedhandler)
4. [Integración en SecurityConfig](#integración-en-securityconfig)
5. [Arquitectura de Flujo](#arquitectura-de-flujo)
6. [Casos de Uso Prácticos](#casos-de-uso-prácticos)

---

## Visión General

### El Problema Original

En Spring Security, cuando ocurren errores de autenticación o autorización, el framework devuelve respuestas HTTP estándar que **no coinciden** con nuestra estructura `ApiResponse`:

```json
// ❌ Respuesta por defecto de Spring Security
{
  "timestamp": "2026-03-23T12:00:00Z",
  "status": 401,
  "error": "Unauthorized",
  "path": "/api/products"
}

// ✅ Nuestra estructura ApiResponse
{
  "success": false,
  "message": "No autorizado. Token faltante o inválido.",
  "data": null,
  "timestamp": "2026-03-23T12:00:00.000Z"
}
```

### La Solución

Implementar dos componentes que interceptan errores de seguridad **antes** de que Spring Security genere su respuesta estándar:

1. **CustomAuthenticationEntryPoint** → Intercepta fallos de autenticación (401)
2. **CustomAccessDeniedHandler** → Intercepta fallos de autorización (403)

---

## CustomAuthenticationEntryPoint

### Propósito
Maneja errores cuando la **autenticación falla** (no hay usuario identificado).

### Casos que Intercepta
- ❌ Token ausente en el header `Authorization`
- ❌ Token expirado
- ❌ Token inválido (formato incorrecto, firma falsa, etc.)
- ❌ Token corrupto o no legible

### Implementación

```java
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException authException
    ) throws IOException, ServletException {

        // 1. Construir respuesta de error
        ApiResponse<Void> apiResponse = ApiResponse.error(
            "No autorizado. Token faltante o inválido.",
            null
        );

        // 2. Configurar HTTP status
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401

        // 3. Configurar content-type
        response.setContentType(MediaType.APPLICATION_JSON_VALUE); // "application/json"

        // 4. Escribir JSON en el body
        response.getWriter().write(
            objectMapper.writeValueAsString(apiResponse)
        );
    }
}
```

### Flow de Ejecución

```
Petición HTTP (GET /api/products)
    ↓
JwtAuthenticationFilter
    ├─ Token ausente o inválido
    ├─ No autentica al usuario
    └─ Continúa con filterChain
        ↓
Autorización requerida (@anyRequest().authenticated())
    ├─ No encuentra usuario en SecurityContext
    └─ Lanza AuthenticationException
        ↓
CustomAuthenticationEntryPoint.commence()
    ├─ Intercepts AuthenticationException
    └─ Responde con JSON + 401
```

### Punto de Inyección en SecurityConfig

```java
http.exceptionHandling(exceptions -> exceptions
    .authenticationEntryPoint(authenticationEntryPoint)
    // ↑ Cuando falla autenticación, llama a este handler
);
```

---

## CustomAccessDeniedHandler

### Propósito
Maneja errores cuando la **autorización falla** (usuario autenticado, pero sin permisos).

### Casos que Intercepta
- ✅ Usuario tiene token válido
- ❌ Pero su rol no cumple la autorización requerida

**Ejemplo:**
```java
@DeleteMapping("/{id}")
@PreAuthorize("hasRole('ADMIN')")  // ← Requiere rol ADMIN
public ResponseEntity<ApiResponse<Void>> deleteExpense(@PathVariable String id) { ... }
```

Si un usuario con `ROLE_USER` intenta acceder → **AccessDeniedException** → `CustomAccessDeniedHandler`

### Implementación

```java
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(
        HttpServletRequest request,
        HttpServletResponse response,
        AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {

        // 1. Construir respuesta de error
        ApiResponse<Void> apiResponse = ApiResponse.error(
            "Acceso denegado. No tienes permisos para acceder a este recurso.",
            null
        );

        // 2. Configurar HTTP status
        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403

        // 3. Configurar content-type
        response.setContentType(MediaType.APPLICATION_JSON_VALUE); // "application/json"

        // 4. Escribir JSON en el body
        response.getWriter().write(
            objectMapper.writeValueAsString(apiResponse)
        );
    }
}
```

### Flow de Ejecución

```
Petición HTTP (DELETE /api/expenses/id)
    + Header: Authorization: Bearer <token_valido_user>
    ↓
JwtAuthenticationFilter
    ├─ Token válido
    ├─ Extrae username y rol (ROLE_USER)
    └─ Autentica al usuario
        ↓
Autorización requerida (@PreAuthorize("hasRole('ADMIN')"))
    ├─ Usuario: ROLE_USER
    ├─ Requerido: ROLE_ADMIN
    └─ Lanza AccessDeniedException
        ↓
CustomAccessDeniedHandler.handle()
    ├─ Intercepts AccessDeniedException
    └─ Responde con JSON + 403
```

### Punto de Inyección en SecurityConfig

```java
http.exceptionHandling(exceptions -> exceptions
    .accessDeniedHandler(accessDeniedHandler)
    // ↑ Cuando falla autorización, llama a este handler
);
```

---

## Integración en SecurityConfig

### Configuración Completa

```java
@Bean
public SecurityFilterChain filterChain(
    HttpSecurity http,
    JwtAuthenticationFilter jwtFilter,
    CustomAuthenticationEntryPoint authenticationEntryPoint,
    CustomAccessDeniedHandler accessDeniedHandler
) throws Exception {
    http.csrf(csrf -> csrf.disable());
    http.cors(Customizer.withDefaults());

    http.sessionManagement(session ->
        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    http.authorizeHttpRequests(auth -> auth
        .requestMatchers("/v3/**", "/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
        .requestMatchers("/h2-console/**").permitAll()
        .requestMatchers("/api/auth/login").permitAll()
        .anyRequest().authenticated());

    http.headers(headers ->
        headers.frameOptions(frame -> frame.disable()));

    // ⭐ Registrar handlers personalizados
    http.exceptionHandling(exceptions -> exceptions
        .authenticationEntryPoint(authenticationEntryPoint)    // 401
        .accessDeniedHandler(accessDeniedHandler));            // 403

    http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}
```

### Inyección de Dependencias

```java
@Bean
public SecurityFilterChain filterChain(
    HttpSecurity http,
    JwtAuthenticationFilter jwtFilter,
    CustomAuthenticationEntryPoint authenticationEntryPoint,  // ← Inyectado
    CustomAccessDeniedHandler accessDeniedHandler            // ← Inyectado
) throws Exception {
```

Ambos handlers son `@Component`, por lo que Spring los registra en el ApplicationContext y los inyecta automáticamente.

---

## Arquitectura de Flujo

### Diagrama de Decisión

```
                    ┌─ Petición HTTP
                    │
                    ▼
            ┌──────────────────┐
            │ SecurityFilters  │
            └──────────────────┘
                    │
        ┌───────────┴───────────┐
        │                       │
        ▼                       ▼
    ┌────────────┐      ┌────────────┐
    │ JWT Filter │      │   Authz    │
    │  válido?   │      │  Filters   │
    └────────────┘      └────────────┘
        │                       │
    ┌───┴───┐         ┌─────────┴─────────┐
    │       │         │                   │
    ▼       ▼         ▼                   ▼
   SÍ      NO    Rol OK?           Rol Insufficient?
    │       │       ▼                   ▼
    │       │      SÍ        ┌─────────────────────┐
    │       │       │        │ AccessDeniedException
    │       │       │        │     (403)           │
    │       │       │        └─────────────────────┘
    │       │       │                 │
    │       │       │                 ▼
    │   ┌───┴───┐   │      ┌──────────────────────┐
    │   │NO: 401│   │      │CustomAccessDeniedHandler
    │   │Auth   │   │      │.handle()             │
    │   │Entry  │   │      │→ JSON + 403          │
    │   │Point  │   │      └──────────────────────┘
    │   └───────┘   │
    │               │
    └───────┬───────┘
            │
            ▼
    ┌───────────────┐
    │ Controlador   │
    │ o Método      │
    └───────────────┘
```

---

## Casos de Uso Prácticos

### Caso 1: Token Expirado

**Petición:**
```bash
curl -X GET http://localhost:8081/api/products \
  -H "Authorization: Bearer eyJhbGc...EXPIRED"
```

**Flujo:**
1. JwtAuthenticationFilter extrae el token
2. TokenProviderPort.isValid() retorna `false` (expirado)
3. El filtro no autentica (SecurityContext vacío)
4. Autorización requiere autenticación → AuthenticationException
5. CustomAuthenticationEntryPoint.commence() es llamado
6. Respuesta: 401 + JSON

**Respuesta:**
```json
{
  "success": false,
  "message": "No autorizado. Token faltante o inválido.",
  "data": null,
  "timestamp": "2026-03-23T13:00:00.000Z"
}
```

---

### Caso 2: Usuario Intenta Operación Admin

**Petición:**
```bash
# User (ROLE_USER) intenta borrar gastos (requiere ROLE_ADMIN)
curl -X DELETE http://localhost:8081/api/expenses/123 \
  -H "Authorization: Bearer eyJhbGc...VALID_USER_TOKEN"
```

**Flujo:**
1. JwtAuthenticationFilter extrae token válido
2. TokenProviderPort.isValid() retorna `true`
3. TokenProviderPort.extractRole() retorna `ROLE_USER`
4. JwtAuthenticationFilter autentica con `ROLE_USER` en SecurityContext
5. Controlador tiene `@PreAuthorize("hasRole('ADMIN')")`
6. Spring Security evalúa: `ROLE_USER` ≠ `ROLE_ADMIN` → AccessDeniedException
7. CustomAccessDeniedHandler.handle() es llamado
8. Respuesta: 403 + JSON

**Respuesta:**
```json
{
  "success": false,
  "message": "Acceso denegado. No tienes permisos para acceder a este recurso.",
  "data": null,
  "timestamp": "2026-03-23T13:00:00.000Z"
}
```

---

### Caso 3: Operación Exitosa

**Petición:**
```bash
# Admin (ROLE_ADMIN) borra gastos
curl -X DELETE http://localhost:8081/api/expenses/123 \
  -H "Authorization: Bearer eyJhbGc...VALID_ADMIN_TOKEN"
```

**Flujo:**
1. JwtAuthenticationFilter autentica con `ROLE_ADMIN`
2. `@PreAuthorize("hasRole('ADMIN')")` pasa ✅
3. Controlador ejecuta lógica
4. Respuesta: 200/204 + JSON de éxito

**Respuesta:**
```json
{
  "success": true,
  "message": "Gasto eliminado correctamente",
  "data": null,
  "timestamp": "2026-03-23T13:00:00.000Z"
}
```

---

## 🎯 Resumen de Beneficios

| Beneficio | Descripción |
|-----------|------------|
| **Consistencia** | Todas las respuestas usan `ApiResponse` |
| **UX Predecible** | El cliente siempre recibe JSON con estructura conocida |
| **Debugging** | Mensajes claros en cada escenario |
| **Integración** | Se integra perfectamente con `GlobalExceptionHandler` |
| **Seguridad** | Ninguna información sensible se expone |
| **Trazabilidad** | Logs podem capturar `authException` y `accessDeniedException` |

---

## 📝 Notas de Implementación

### ObjectMapper Thread-Safe
```java
private static final ObjectMapper objectMapper = new ObjectMapper();
```
✅ Es seguro usarlo como static porque ObjectMapper es thread-safe desde Jackson 2.0+

### Direct Response Writing
```java
response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
```
✅ Necesario escribir directamente porque estamos en capa de filtros, antes de que Spring construya la respuesta

### UTF-8 Encoding
```java
response.setContentType(MediaType.APPLICATION_JSON_VALUE); // "application/json; charset=UTF-8"
```
✅ Spring establece UTF-8 por defecto cuando estableces `application/json`

---

