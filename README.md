# 🍔 EasyTPV - Backend REST API

[![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4-brightgreen?style=for-the-badge&logo=springboot)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Neon.tech-blue?style=for-the-badge&logo=postgresql)](https://neon.tech/)

Backend profesional para un sistema de **Punto de Venta (TPV)**. Diseñado con **Arquitectura Hexagonal** y **Domain-Driven Design (DDD)** para garantizar un desacoplamiento total entre la lógica de negocio y la infraestructura.

---

## 🚀 Lo que hace especial a este proyecto

Este no es un simple CRUD. Se ha construido siguiendo estándares de industria:

- **🛡️ Seguridad Robusta:** Implementación de **JWT (Stateless)** con seguridad configurada por perfiles (`local` para integración fluida vs `prod` blindado).
- **🏗️ Arquitectura Hexagonal:** Separación estricta de capas (`domain`, `application`, `infrastructure`). El dominio es POJO puro, sin dependencias de frameworks.
- **☁️ Cloud Ready:** Configurado para persistencia en **PostgreSQL Serverless (Neon.tech)** con gestión de secretos mediante variables de entorno.
- **✅ Calidad de Código:** +40 tests de integración y unidad, validación defensiva con Jakarta y mapeo estricto con **MapStruct**.
- **📖 Auto-Documentado:** Contrato de API siempre actualizado mediante **Swagger UI** y OpenAPI 3.

---

## 🛠️ Stack Tecnológico

* **Core:** Java 21, Spring Boot 3.4
* **Seguridad:** Spring Security, JWT (jjwt)
* **Persistencia:** Spring Data JPA, Hibernate, PostgreSQL, H2 (Memoria)
* **Productividad:** Lombok, MapStruct
* **Documentación:** Springdoc OpenAPI
* **Testing:** JUnit 5, AssertJ, Mockito

---

## 🏃 Quick Start

### 1. Variables de Entorno
Crea un archivo `.env` basado en `.env.example` o configura las siguientes variables en tu IDE:
- `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`
- `JWT_SECRET` (Mínimo 32 caracteres)
- `CORS_ORIGIN` (Opcional, defecto: `http://localhost:4200`)

### 2. Ejecución
```bash
# Arrancar en modo local (Usa H2 y seguridad permisiva para integración)
./mvnw spring-boot:run -Dspring-boot.run.profiles=local

# Ejecutar tests
./mvnw clean test

Acceso: http://localhost:8081

Swagger: http://localhost:8081/swagger-ui.html

📂 Estructura del Proyecto

com.ortiz.tpv_backend
├── domain          # Lógica pura: Modelos, Excepciones y Puertos (Interfaces)
├── application     # Casos de uso, DTOs y Mappers
└── infrastructure  # Adaptadores: REST Controllers, JPA Repositories, Security Config

