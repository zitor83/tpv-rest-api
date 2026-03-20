# tpv-rest-api

Backend REST para un TPV (Terminal Punto de Venta) orientado a mantenibilidad y escalabilidad, siguiendo arquitectura hexagonal y buenas practicas de separacion por capas.

## Tecnologias

- Java 21
- Spring Boot 3
- Spring Web
- Spring Data JPA
- Spring Validation
- Spring Security (base para JWT)
- MapStruct
- Lombok
- OpenAPI/Swagger (`springdoc`)
- PostgreSQL
- Maven
- JUnit 5 + Mockito (testing)

## Arquitectura hexagonal

El proyecto esta organizado para desacoplar dominio, casos de uso e infraestructura:

```text
src/main/java/com/ortiz/tpv_backend/
  domain/
	model/
	port/
  application/
	dto/
	usecase/
	mapper/
  infrastructure/
	rest/
	persistence/
	config/
```

Principios clave:

- El dominio no depende de frameworks.
- Los casos de uso orquestan la logica de aplicacion.
- Los adaptadores de infraestructura implementan puertos del dominio.

## Como ejecutar

Requisitos:

- JDK 21
- Maven Wrapper (`mvnw`/`mvnw.cmd`)
- PostgreSQL (solo para ejecucion normal)

Opciones de ejecucion:

- `default`: usa PostgreSQL.
- `local`: usa H2 en memoria (no requiere PostgreSQL).

Configura variables de entorno opcionales:

- `DB_URL` (por defecto: `jdbc:postgresql://localhost:5432/tpv`)
- `DB_USERNAME` (por defecto: `postgres`)
- `DB_PASSWORD` (por defecto: `postgres`)
- `JWT_SECRET`, `JWT_EXPIRATION_MILLIS`, `JWT_ISSUER`

Ejecucion local:

```bash
./mvnw spring-boot:run
```

Ejecucion con perfil `local` (H2):

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

En Windows:

```bash
./mvnw.cmd spring-boot:run
```

En Windows con perfil `local` (H2):

```bash
./mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=local
```

Documentacion API:

- Swagger UI: `http://localhost:8081/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8081/api-docs`
- H2 Console (perfil `local`): `http://localhost:8081/h2-console`

## Como ejecutar tests

```bash
./mvnw test
```

En Windows:

```bash
./mvnw.cmd test
```

Nota: los tests usan H2 en memoria para no depender de PostgreSQL local.

## Proximos pasos

- Implementar autenticacion JWT completa (login, filtro, refresh token).
- Anadir manejo global de errores con formato uniforme de respuesta.
- Incorporar migraciones de base de datos (Flyway o Liquibase).
- Aumentar cobertura con tests de integracion por endpoint.
- Endurecer seguridad (roles/permisos, rate limiting, CORS por entorno).
