# 🍔 EasyTPV - Backend REST API

Backend de punto de venta (TPV) construido con **Java 21** y **Spring Boot 3**, diseñado siguiendo **Arquitectura Hexagonal** para priorizar la mantenibilidad, el testing y el desacoplamiento.

Este servicio expone una API REST para:
- Gestión de productos
- Creación y consulta de tickets
- Control de gastos operativos
- Generación de reportes financieros diarios y mensuales

---

## ✨ Características principales

- **Arquitectura hexagonal:** separación estricta entre `domain`, `application` e `infrastructure`
- **Seguridad stateless:** autenticación y autorización basada en JWT
- **OpenAPI 3 + Swagger UI:** documentación 100% interactiva
- **MapStruct:** conversión automática entre entidades y DTOs
- **Lombok:** reducción de código repetitivo (boilerplate)
- **Jakarta Validation:** validación defensiva en los DTOs
- **Persistencia flexible:** H2 en local para desarrollo y PostgreSQL para entornos productivos

---

## 🚀 Quick Start

### Prerrequisitos
- JDK 21 o superior
- Git
- Maven Wrapper incluido (`./mvnw`, `./mvnw.cmd`)

### 1) Clonar el repositorio

    git clone https://github.com/zitor83/tpv-rest-api.git
    cd tpv-backend

### 2) Arrancar en local

El perfil local utiliza H2 en memoria y carga automáticamente los datos de prueba desde `data.sql`.

    # Linux / macOS
    ./mvnw spring-boot:run

    # Windows
    ./mvnw.cmd spring-boot:run

Servidor por defecto: `http://localhost:8081`

### 3) Ejecutar pruebas

La suite valida casos de uso, seguridad y persistencia:

    ./mvnw clean test

---

## 🔗 Enlaces de desarrollo

Una vez arrancado el servidor, tendrás acceso a:

- 📖 **Swagger UI:** `http://localhost:8081/swagger-ui/index.html`
- 📄 **OpenAPI JSON:** `http://localhost:8081/v3/api-docs`
- 🗄️ **Consola H2:** `http://localhost:8081/h2-console`
  - **JDBC URL:** jdbc:h2:mem:tpv
  - **User:** sa
  - **Password:** *(vacío)*

---

## 📚 Documentación del proyecto

- `README.md`: visión general y arranque rápido (este archivo)
- `DEVELOPMENT.md`: arquitectura, patrones utilizados y flujo de datos
- `API_REFERENCE.md`: contrato oficial para el frontend (auth, payloads, respuestas y errores)
- `DEPLOYMENT.md`: instrucciones para despliegue y variables de entorno
- `CHANGELOG.md`: historial de cambios y versiones

---

## 🛠️ Comandos útiles

    # Compilar el proyecto limpiando builds anteriores
    ./mvnw clean compile

    # Empaquetar JAR (omitiendo tests)
    ./mvnw clean package -DskipTests

    # Ejecutar el JAR generado
    java -jar target/tpv-backend-1.0.0-SNAPSHOT.jar

---

## 👥 Setup para frontend

Si necesitas conectar tu aplicación frontend a este backend:

1. Arranca el backend en local (`./mvnw spring-boot:run`)
2. Usa `POST /api/auth/login` con tus credenciales para obtener el JWT
3. Envía la cabecera `Authorization: Bearer <token>` en todos los endpoints protegidos
4. Consulta `API_REFERENCE.md` para entender el contrato de respuestas y el manejo de errores

---

**Versión:** 1.0.0  
**Spring Boot:** 3.4.x  
**Java:** 21