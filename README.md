# EasyTPV - Backend REST API

Backend de punto de venta (TPV) construido con **Java 21** y **Spring Boot 3**, disenado con **Arquitectura Hexagonal** para priorizar mantenibilidad, testing y desacoplamiento.

Este servicio expone una API REST para:
- Gestion de productos.
- Creacion y consulta de tickets.
- Control de gastos operativos.
- Reportes financieros diarios y mensuales.

---

## Caracteristicas Principales

- **Arquitectura Hexagonal:** separacion entre `domain`, `application` e `infrastructure`.
- **Seguridad Stateless:** autenticacion/autorizacion basada en JWT.
- **OpenAPI 3 + Swagger UI:** documentacion interactiva.
- **MapStruct:** conversiones entre entidades y DTOs.
- **Lombok:** reduccion de boilerplate.
- **Jakarta Validation:** validacion de entrada en DTOs.
- **Persistencia flexible:** H2 en local, PostgreSQL para entornos productivos.

---

## Quick Start

### Prerrequisitos
- JDK 21+
- Git
- Maven Wrapper incluido (`./mvnw`, `./mvnw.cmd`)

### 1) Clonar repositorio
```bash
git clone <tu-repositorio-url>
cd tpv-backend
```

### 2) Arrancar en local
El perfil local usa H2 en memoria y carga datos de `data.sql`.

```bash
# Linux / macOS
./mvnw spring-boot:run

# Windows
./mvnw.cmd spring-boot:run
```

Servidor por defecto: `http://localhost:8081`

### 3) Ejecutar pruebas
```bash
./mvnw clean test
```

---

## Enlaces de Desarrollo

- Swagger UI: `http://localhost:8081/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8081/v3/api-docs`
- Consola H2: `http://localhost:8081/h2-console`
  - JDBC URL: `jdbc:h2:mem:tpv`
  - User: `sa`
  - Password: vacio

---

## Documentacion del Proyecto

- `README.md`: vision general y arranque rapido.
- `DEVELOPMENT.md`: arquitectura, patrones y flujo de datos.
- `API_REFERENCE.md`: contrato oficial para frontend (auth, respuestas y errores).
- `CHANGELOG.md`: historial de cambios.

---

## Comandos Utiles

```bash
# Compilar proyecto
./mvnw clean compile

# Empaquetar JAR (sin tests)
./mvnw clean package -DskipTests

# Ejecutar JAR
java -jar target/tpv-backend-1.0.0-SNAPSHOT.jar
```

---

## Setup para Frontend

1. Arranca el backend en local.
2. Usa `POST /api/auth/login` para obtener JWT.
3. Envia `Authorization: Bearer <token>` en endpoints protegidos.
4. Consulta `API_REFERENCE.md` para contrato de respuestas y errores.

---

**Version:** 1.0.0
**Spring Boot:** 3.4.x
**Java:** 21
