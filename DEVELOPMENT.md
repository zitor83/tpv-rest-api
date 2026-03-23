# Guia de Desarrollo Backend - EasyTPV

Guia tecnica para desarrollar y mantener `tpv-backend` con arquitectura hexagonal, buenas practicas y flujo de trabajo estandar.

---

## Arquitectura Hexagonal (Puertos y Adaptadores)

El backend aplica Arquitectura Hexagonal para desacoplar la logica de negocio del framework y de los detalles de infraestructura.

### 1) Dominio (`domain`)
Nucleo de negocio, sin dependencias de Spring ni de JPA.
- **Models:** clases de negocio (`Product`, `Order`, `Expense`).
- **Ports:** interfaces que define el dominio para acceder a servicios externos (`ProductRepositoryPort`, etc.).

### 2) Aplicacion (`application`)
Orquesta casos de uso y transformaciones.
- **Use Cases:** interfaces e implementaciones de acciones del sistema (`CreateOrderUseCaseImpl`, etc.).
- **DTOs:** contratos de entrada/salida para API.
- **Mappers:** conversiones con MapStruct entre DTO, dominio y persistencia.

### 3) Infraestructura (`infrastructure`)
Implementaciones tecnicas.
- **REST Controllers:** exponen endpoints HTTP.
- **Persistence:** entidades JPA, repositorios Spring Data y adaptadores que implementan puertos.
- **Security:** JWT, filtros, configuracion y handlers de errores.

---

## Flujo de Datos (Request -> Response)

1. El cliente envia una peticion HTTP (por ejemplo, `POST /api/orders`).
2. El controlador recibe y valida el `RequestDTO` con Jakarta Validation.
3. El controlador llama al caso de uso.
4. El caso de uso mapea DTO -> modelo de dominio.
5. Se aplican reglas de negocio.
6. Se invoca un puerto del dominio.
7. El adaptador de persistencia transforma dominio -> entidad JPA.
8. Spring Data persiste en base de datos.
9. La respuesta vuelve como `ApiResponse<ResponseDTO>`.

---

## Herramientas y Patrones Clave

### Lombok
Se usa para reducir boilerplate en DTOs y servicios.
- Recomendado: `@Builder`, `@Getter`, `@RequiredArgsConstructor`.
- En entidades JPA evitar `@Data` y `@EqualsAndHashCode` para no romper proxies de Hibernate.

### MapStruct
Genera mapeadores en compilacion para conversiones tipadas y mantenibles.

### Jakarta Validation
Protege entradas con anotaciones (`@NotBlank`, `@NotNull`, `@Positive`, etc.) y retorna `400 Bad Request` cuando falla la validacion.

---

## Base de Datos

- **Local:** H2 en memoria con datos iniciales desde `src/main/resources/data.sql`.
- **Produccion:** PostgreSQL via perfiles de Spring.

---

## Seguridad y Autenticacion

- API stateless con JWT.
- Login en `POST /api/auth/login` para obtener token.
- Token requerido en header `Authorization: Bearer <token>`.
- Errores de seguridad estandarizados con `ApiResponse` mediante handlers custom.

---

## Testing

1. **Unitarios:** JUnit 5 + Mockito para casos de uso.
2. **Integracion:** `@SpringBootTest`, `@AutoConfigureMockMvc` y pruebas de persistencia sobre H2.

---

## Comandos Rapidos

```bash
# Arrancar servidor (puerto 8081)
./mvnw spring-boot:run

# Ejecutar test suite
./mvnw clean test

# Compilar sin tests
./mvnw clean compile
```

Documentacion interactiva:
- `http://localhost:8081/swagger-ui/index.html`
