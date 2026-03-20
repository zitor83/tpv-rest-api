# 🧾 TPV Backend

Backend de un sistema TPV (Terminal Punto de Venta) desarrollado con **Spring Boot**.

## 🧱 Arquitectura

Arquitectura hexagonal por capas:

- `domain/model`
- `domain/port`
- `application/usecase`
- `application/dto`
- `infrastructure/rest`
- `infrastructure/persistence`
- `infrastructure/config`

## 🚀 Estado del proyecto

🟡 En desarrollo (fase inicial)

## 📦 Funcionalidades (WIP)

* [ ] Gestión de productos
* [ ] Gestión de ventas
* [ ] Autenticación de usuarios
* [ ] Control de stock

## 🛠️ Tecnologías

* Java 21
* Spring Boot
* Spring Web
* Spring Data JPA
* Spring Validation
* Spring Security (base para JWT)
* MapStruct + Lombok
* OpenAPI/Swagger
* PostgreSQL
* Maven

## ⚙️ Ejecución

```bash
# Clonar repositorio
git clone https://github.com/TU_USUARIO/tpv-backend.git

# Entrar al proyecto
cd tpv-backend

# Ejecutar
./mvnw spring-boot:run
```

## 📡 API

Swagger UI: `http://localhost:8080/swagger-ui.html`

OpenAPI JSON: `http://localhost:8080/api-docs`

## 👤 Autor

José Antonio Ortiz
