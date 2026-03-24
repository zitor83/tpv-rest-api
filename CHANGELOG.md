Changelog
Registro de cambios relevantes de tpv-backend.

Este archivo sigue Keep a Changelog y versionado Semantic Versioning.

Unreleased
Added
Módulo de Gastos (Expenses)

Endpoint GET /api/expenses con soporte para filtrado por fecha exacta (date) o mes/año (month, year).

Resúmenes automáticos (totalExpenses y count) incrustados en el campo meta de la respuesta al aplicar filtros.

Endpoint PUT /api/expenses/{id} para la actualización de gastos existentes.

DTO ExpenseUpdateRequestDTO con validaciones de Jakarta (@Positive, @NotBlank).

Módulo de Reportes (Reports)

Endpoint GET /api/reports/summary para la generación de reportes financieros personalizados por rango de fechas (startDate, endDate).

Cálculos matemáticos avanzados y seguros usando BigDecimal para ventas totales, media de tickets, gastos y margen de beneficio neto.

Nuevo SummaryReportResponseDTO con estructura anidada (period, sales, expenses, profit).

Módulo de Pedidos (Orders)

Endpoint PUT /api/orders/{id} para modificaciones post-creación y checkout (pago y propina).

Caso de uso UpdateOrderUseCase que recalcula automáticamente los totales del pedido al modificar las líneas de productos por seguridad.

DTO OrderUpdateRequestDTO con validación estricta de items.

Autenticación y Seguridad (Auth)

Nuevo LoginResponseDTO con estructura anidada para el perfil del usuario (user.id, user.username, user.role) y tiempo de expiración (expiresIn), alineado al contrato del frontend.

Changed
Refactorización de la interfaz LoginUseCase y su implementación para devolver la nueva estructura de datos de sesión.

Actualización de la batería de tests unitarios (LoginUseCaseImplTest, AuthControllerValidationTest) para cumplir con el nuevo contrato de respuesta del Login.

Solución de contexto de Spring en OrderControllerValidationTest inyectando el mock faltante de UpdateOrderUseCase.

0.1.0 - 2026-03-20
Added
Estructura base del proyecto con Spring Boot, Java 21 y Maven.

Arquitectura hexagonal inicial organizada en domain, application e infrastructure.

Configuracion inicial de seguridad con Spring Security y base para JWT.

Configuracion de OpenAPI/Swagger y endpoint de documentacion.

Configuracion de persistencia con Spring Data JPA y PostgreSQL.

Soporte de pruebas con JUnit 5, Mockito y H2 en memoria.

Product Module
Modelo de dominio Product sin anotaciones JPA, con validaciones defensivas y equals/hashCode por id.

Puerto de dominio ProductRepositoryPort con operaciones save, findById y findAll.

DTOs de aplicacion ProductRequestDTO y ProductResponseDTO.

Caso de uso de creacion de productos (CreateProductUseCase y CreateProductUseCaseImpl).

Caso de uso de listado de productos (ListProductsUseCase y ListProductsUseCaseImpl).

Mappers con MapStruct para conversiones API/dominio y dominio/persistencia.

Entidad JPA ProductEntity mapeada a la tabla products.

Repositorio Spring Data ProductJpaRepository.

Adaptador de persistencia ProductRepositoryAdapter implementando el puerto del dominio.

Controlador REST ProductController con endpoints GET /api/products y POST /api/products.

Wrapper de respuesta ApiResponse<T> para respuestas estandarizadas.