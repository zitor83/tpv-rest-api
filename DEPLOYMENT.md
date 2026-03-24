# Guia de Despliegue en Produccion - EasyTPV Backend

Este documento define el procedimiento oficial para desplegar `tpv-backend` en produccion, migrando el entorno desde H2 local hacia PostgreSQL.

---

## 1) Build de Produccion

Genera el artefacto ejecutable `.jar` con Maven Wrapper:

```bash
./mvnw clean package -DskipTests
```

Resultado esperado:
- JAR generado en `target/tpv-backend-0.0.1-SNAPSHOT.jar`

---

## 2) Variables de Entorno Obligatorias

Estas variables deben definirse antes de arrancar la aplicacion en produccion.

### `SPRING_PROFILES_ACTIVE=prod`
- Activa el perfil de produccion.
- Evita usar la configuracion de desarrollo local orientada a H2.

### `DB_URL`
- URL JDBC de PostgreSQL.
- Ejemplo: `jdbc:postgresql://<host>:5432/<database>`

### `DB_USERNAME`
- Usuario con permisos sobre la base de datos de produccion.

### `DB_PASSWORD`
- Password del usuario de base de datos.
- Debe gestionarse desde secret manager o variables seguras del entorno.

### `JWT_SECRET`
- Clave secreta para firma de tokens JWT.
- Requisito minimo: 256 bits (32 bytes) de entropia real.
- No usar claves por defecto ni hardcodeadas.

### `CORS_ORIGIN`
- URL publica real del frontend Angular en produccion (sin wildcard).
- Ejemplo: `https://app.easytpv.com`
- Debe limitarse a dominios confiables para evitar accesos cross-origin no autorizados.

> Nota operativa: en la configuracion actual existe un origen local fijo para desarrollo. En produccion, la politica CORS debe alinearse con `CORS_ORIGIN` del entorno de despliegue.

---

## 3) Ejecucion del JAR en Produccion

Ejemplo de arranque inyectando variables en una sola linea:

```bash
SPRING_PROFILES_ACTIVE=prod \
DB_URL="jdbc:postgresql://db-prod:5432/tpv" \
DB_USERNAME="tpv_user" \
DB_PASSWORD="<secure-password>" \
JWT_SECRET="<min-32-bytes-random-secret>" \
CORS_ORIGIN="https://app.easytpv.com" \
java -jar target/tpv-backend-0.0.1-SNAPSHOT.jar
```

---

## 4) Checklist Minimo de Produccion

- Base de datos PostgreSQL accesible desde el backend.
- Credenciales y secretos almacenados de forma segura.
- `SPRING_PROFILES_ACTIVE=prod` definido en el entorno.
- CORS restringido al dominio real del frontend.
- Monitoreo y logs habilitados en el entorno destino.

---

## 5) Docker (Proximos pasos)

Como siguiente iteracion de plataforma se incorporara:
- `Dockerfile` para empaquetar el backend.
- `docker-compose.yml` para orquestar backend + PostgreSQL.

Esto simplificara despliegues repetibles entre entornos y reducira diferencias entre QA y Produccion.

