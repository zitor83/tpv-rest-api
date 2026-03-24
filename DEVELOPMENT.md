

# 🏗️ Guía de Desarrollo Backend - EasyTPV

> **💡 Resumen:** Guía técnica para desarrollar y mantener `tpv-backend` con arquitectura hexagonal, buenas prácticas y flujo de trabajo estándar.

---

## 🏛️ Arquitectura Hexagonal (Puertos y Adaptadores)

El backend aplica Arquitectura Hexagonal para desacoplar la lógica de negocio del framework y de los detalles de infraestructura.

### 1️⃣ Dominio (`domain`)
**El núcleo del negocio.** Cero dependencias de Spring o JPA.
* **Models:** Clases puras de negocio (`Product`, `Order`, `Expense`).
* **Ports:** Interfaces que definen qué necesita el dominio del mundo exterior (ej. `ProductRepositoryPort`).

### 2️⃣ Aplicación (`application`)
**El director de orquesta.** Conecta el exterior con el dominio.
* **Use Cases:** Ejecutan las acciones del sistema (`CreateOrderUseCaseImpl`).
* **DTOs:** Contratos de entrada y salida para la API REST.
* **Mappers:** Transformaciones automáticas con MapStruct.

### 3️⃣ Infraestructura (`infrastructure`)
**Los detalles técnicos y frameworks**.
* **REST Controllers:** Puntos de entrada HTTP.
* **Persistence:** Entidades JPA, Repositorios Spring Data y Adaptadores.
* **Security:** JWT, filtros y manejadores de errores personalizados.

---

## 🔄 Flujo de Datos (Request ➔ Response)

El viaje exacto de una petición a través de nuestras capas:

1.  🌐 **Cliente:** Envía petición HTTP (`POST /api/orders`).
2.  🎛️ **Controller:** Recibe JSON y valida automáticamente en un `RequestDTO`.
3.  ⚙️ **Use Case:** El controlador delega la acción a la capa de aplicación.
4.  🔄 **Mapper:** Se transforma el `DTO` a un Modelo de Dominio puro.
5.  🧩 **Domain:** Se aplican las reglas de negocio.
6.  🔌 **Port ➔ Adapter:** El caso de uso llama a la interfaz, el adaptador lo intercepta y mapea a Entidad JPA.
7.  🗄️ **Database:** Spring Data guarda la información.
8.  🚀 **Response:** El dato vuelve envuelto en nuestro estándar `ApiResponse<ResponseDTO>`.

---

## 🛠️ Herramientas y Patrones Clave

* **Lombok (Menos Boilerplate):** Usamos `@Builder`, `@Getter` y `@RequiredArgsConstructor`.
    * ⚠️ *Peligro JPA:* NUNCA usar `@Data` ni `@EqualsAndHashCode` en entidades para evitar bucles infinitos en Hibernate.
* **MapStruct:** Mapeadores generados en compilación para conversiones seguras y súper rápidas.
* **Jakarta Validation:** Escudo protector en los controladores (`@NotBlank`, `@NotNull`, `@Positive`). Retorna un `400 Bad Request` automático si algo falla.

---

## 🗄️ Entornos y Base de Datos

* 💻 **Local:** H2 en memoria. Se regenera en cada reinicio cargando datos desde `data.sql`.
* 🌍 **Producción:** PostgreSQL gestionado vía `application-prod.properties`.

---

## 🔐 Seguridad (JWT)

Nuestra API es **Stateless**. No hay sesiones en memoria.
* Obtén tu pase en: `POST /api/auth/login`.
* Úsalo inyectando la cabecera: `Authorization: Bearer <token>`.
* Respuestas de error unificadas en `ApiResponse` para 401 (No Autorizado) y 403 (Prohibido).

---

## 🧪 Testing

Aseguramos la calidad en dos frentes:
1.  **Unitarios:** `JUnit 5` + `Mockito` (Lógica pura, sin levantar la base de datos).
2.  **Integración:** `@SpringBootTest` + `@AutoConfigureMockMvc` + `@DataJpaTest` (Pruebas reales contra H2).

---

## 🚀 Comandos Rápidos

    # Arrancar el servidor local (puerto 8081)
    ./mvnw spring-boot:run

    # Ejecutar la suite completa de tests
    ./mvnw clean test

    # Compilar el proyecto omitiendo los tests
    ./mvnw clean compile

**Documentación interactiva de la API:**
Visita: `http://localhost:8081/swagger-ui/index.html`