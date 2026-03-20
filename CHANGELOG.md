# Changelog

Todos los cambios importantes de este proyecto se documentaran en este archivo.

El formato se basa en [Keep a Changelog](https://keepachangelog.com/es-ES/1.1.0/),
y este proyecto sigue [Semantic Versioning](https://semver.org/lang/es/).

## [Unreleased]

## [0.1.0] - 2026-03-20

### Added
- Estructura base del proyecto con Spring Boot, Java 21 y Maven.
- Arquitectura hexagonal inicial organizada en `domain`, `application` e `infrastructure`.
- Configuracion inicial de seguridad con Spring Security y base para JWT.
- Configuracion de OpenAPI/Swagger y endpoint de documentacion.
- Configuracion de persistencia con Spring Data JPA y PostgreSQL.
- Soporte de pruebas con JUnit 5, Mockito y H2 en memoria.

### Product Module
- Modelo de dominio `Product` sin anotaciones JPA, con validaciones defensivas y `equals/hashCode` por id.
- Puerto de dominio `ProductRepositoryPort` con operaciones `save`, `findById` y `findAll`.
- DTOs de aplicacion `ProductRequestDTO` y `ProductResponseDTO`.
- Caso de uso de creacion de productos (`CreateProductUseCase` y `CreateProductUseCaseImpl`).
- Caso de uso de listado de productos (`ListProductsUseCase` y `ListProductsUseCaseImpl`).
- Mappers con MapStruct para conversiones API/dominio y dominio/persistencia.
- Entidad JPA `ProductEntity` mapeada a la tabla `products`.
- Repositorio Spring Data `ProductJpaRepository`.
- Adaptador de persistencia `ProductRepositoryAdapter` implementando el puerto del dominio.
- Controlador REST `ProductController` con endpoints `GET /api/products` y `POST /api/products`.
- Wrapper de respuesta `ApiResponse<T>` para respuestas estandarizadas.

[Unreleased]: https://github.com/TU_USUARIO/tpv-rest-api/compare/v0.1.0...HEAD
[0.1.0]: https://github.com/TU_USUARIO/tpv-rest-api/releases/tag/v0.1.0

