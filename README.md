# TPV Backend REST API

Backend REST para un TPV (Terminal Punto de Venta) diseñado con un enfoque en la mantenibilidad, escalabilidad y buenas prácticas de desarrollo, siguiendo los principios de la Arquitectura Hexagonal.

## ✅ Principios y Arquitectura

Este proyecto está organizado para desacoplar el dominio del negocio, los casos de uso de la aplicación y la infraestructura tecnológica.

- **Arquitectura de Capas Estricta**:
  - `domain`: Contiene los modelos de negocio puros y los puertos (interfaces) que definen cómo la aplicación interactúa con el exterior. No depende de ningún framework.
  - `application`: Orquesta la lógica de negocio a través de casos de uso (`use cases`), utiliza DTOs para la transferencia de datos y define mappers para las conversiones.
  - `infrastructure`: Implementa los adaptadores para tecnologías externas, como controladores REST, persistencia en base de datos y configuración de seguridad.

- **Seguridad por Defecto**: Configuración preparada para JWT y uso de variables de entorno para datos sensibles.
- **Java Moderno**: Utiliza características de Java 21 para un código más limpio y robusto.

## 🛠️ Stack Tecnológico

- **Lenguaje**: Java 21
- **Framework**: Spring Boot 3
- **API**: Spring Web, Spring Validation
- **Persistencia**: Spring Data JPA, Hibernate
- **Bases de Datos**:
  - **Desarrollo**: H2 (en memoria, activado por defecto)
  - **Producción**: PostgreSQL
- **Mapeo**: MapStruct
- **Utilidades**: Lombok
- **Documentación API**: OpenAPI 3 (springdoc)
- **Build Tool**: Maven

## 🚀 Cómo Empezar

Este proyecto está preconfigurado para ejecutarse directamente sin necesidad de instalar software adicional como bases de datos.

### Requisitos

- JDK 21 o superior.
- Git.

### 1. Clonar el Repositorio

```bash
git clone <URL_DEL_REPOSITORIO>
cd tpv-backend
```

### 2. Ejecutar la Aplicación

El proyecto utiliza el perfil `local` por defecto, que activa una base de datos H2 en memoria. No necesitas instalar ni configurar PostgreSQL para empezar.

**Usando el Maven Wrapper (recomendado):**

- En macOS/Linux:
  ```bash
  ./mvnw spring-boot:run
  ```
- En Windows:
  ```bash
  ./mvnw.cmd spring-boot:run
  ```

La API estará disponible en `http://localhost:8081`.

### 3. Acceder a las Herramientas de Desarrollo

Una vez que la aplicación esté en marcha, puedes usar los siguientes enlaces:

- **Swagger UI (Documentación Interactiva de la API)**:
  - [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)
- **Consola de la Base de Datos H2**:
  - [http://localhost:8081/h2-console](http://localhost:8081/h2-console)
  - **JDBC URL**: `jdbc:h2:mem:tpv`
  - **Username**: `sa`
  - **Password**: (dejar en blanco)

## 📖 API Endpoints

La API está documentada a través de OpenAPI y puede ser explorada con Swagger UI. A continuación se muestran los endpoints principales.

### Productos (`/api/products`)

#### `GET /api/products`

Lista todos los productos disponibles en el sistema.

- **Respuesta Exitosa (200 OK)**:
  ```json
  {
    "status": "SUCCESS",
    "message": "Productos obtenidos correctamente",
    "data": [
      {
        "id": 1,
        "nombre": "Producto de Ejemplo",
        "precio": 19.99,
        "stock": 100,
        "familia": "General"
      }
    ]
  }
  ```

- **Ejemplo con `curl`**:
  ```bash
  curl -X GET http://localhost:8081/api/products
  ```

#### `POST /api/products`

Crea un nuevo producto en el sistema.

- **Cuerpo de la Petición (Request Body)**:
  ```json
  {
    "nombre": "Nuevo Producto",
    "precio": 25.50,
    "stock": 50,
    "familia": "Electrónica"
  }
  ```

- **Respuesta Exitosa (201 Created)**:
  ```json
  {
    "status": "SUCCESS",
    "message": "Producto creado correctamente",
    "data": {
      "id": 2,
      "nombre": "Nuevo Producto",
      "precio": 25.50,
      "stock": 50,
      "familia": "Electrónica"
    }
  }
  ```

- **Ejemplo con `curl`**:
  ```bash
  curl -X POST http://localhost:8081/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Nuevo Producto",
    "precio": 25.50,
    "stock": 50,
    "familia": "Electrónica"
  }'
  ```

## 🧪 Cómo Ejecutar los Tests

El proyecto incluye tests unitarios que también utilizan una base de datos H2 en memoria.

- En macOS/Linux:
  ```bash
  ./mvnw test
  ```
- En Windows:
  ```bash
  ./mvnw.cmd test
  ```

## 🌐 Ejecución en un Entorno de Producción

Para ejecutar la aplicación contra una base de datos PostgreSQL real (por ejemplo, en un servidor de producción), necesitarás configurar las siguientes variables de entorno:

- `SPRING_PROFILES_ACTIVE`: `prod` (o cualquier otro perfil que no sea `local`)
- `DB_URL`: La URL JDBC de tu base de datos PostgreSQL.
- `DB_USERNAME`: El usuario de la base de datos.
- `DB_PASSWORD`: La contraseña del usuario.
- `JWT_SECRET`: Un secreto largo y seguro para firmar los tokens JWT.

## 🗺️ Próximos Pasos

- [ ] Implementar autenticación JWT completa (login, filtro, refresh token).
- [ ] Incorporar migraciones de base de datos (Flyway o Liquibase) para gestionar cambios en el esquema.
- [ ] Aumentar la cobertura de tests con tests de integración por endpoint.
- [ ] Endurecer la seguridad: roles/permisos, limitación de peticiones (rate limiting) y configuración de CORS por entorno.
