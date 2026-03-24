

# 🚀 Guía de Despliegue en Producción - EasyTPV Backend

Este documento define el procedimiento oficial para desplegar `tpv-backend` en producción, migrando el entorno desde H2 local hacia PostgreSQL.

---

## 📦 1. Build de Producción

Genera el artefacto ejecutable `.jar` con Maven Wrapper omitiendo los tests (ya que deberían haberse ejecutado en la fase de CI/CD):


./mvnw clean package -DskipTests


**Resultado esperado:**
- JAR generado en la ruta `target/tpv-backend-1.0.0-SNAPSHOT.jar` (la versión puede variar según el `pom.xml`).

---

## 🔐 2. Variables de Entorno Obligatorias

Estas variables son **críticas** y deben definirse en el entorno del servidor antes de arrancar la aplicación.

* `SPRING_PROFILES_ACTIVE=prod`
    * Activa el perfil de producción.
    * Evita usar la configuración de desarrollo local orientada a la base de datos H2 en memoria.
* `DB_URL`
    * URL JDBC de conexión a PostgreSQL.
    * *Ejemplo:* `jdbc:postgresql://<host>:5432/<database>`
* `DB_USERNAME`
    * Usuario con permisos sobre la base de datos de producción.
* `DB_PASSWORD`
    * Contraseña del usuario de base de datos.
    * ⚠️ *Debe gestionarse desde un secret manager o variables seguras del entorno, nunca en código.*
* `JWT_SECRET`
    * Clave secreta para la firma de tokens JWT.
    * ⚠️ *Requisito mínimo:* 256 bits (32 bytes) de entropía real. No usar claves por defecto ni hardcodeadas.
* `CORS_ORIGIN`
    * URL pública real del frontend Angular en producción (sin wildcard `*`).
    * *Ejemplo:* `https://app.easytpv.com`
    * Debe limitarse a dominios confiables para evitar accesos cross-origin no autorizados.

> **💡 Nota operativa:** En la configuración actual existe un origen local fijo para desarrollo (`http://localhost:4200`). En producción, la política CORS debe alinearse estrictamente con el `CORS_ORIGIN` del entorno de despliegue.

---

## ▶️ 3. Ejecución del JAR en Producción

Ejemplo de arranque inyectando las variables directamente en la línea de comandos (útil para pruebas manuales en servidores Linux):


SPRING_PROFILES_ACTIVE=prod \
DB_URL="jdbc:postgresql://db-prod:5432/tpv" \
DB_USERNAME="tpv_user" \
DB_PASSWORD="<secure-password>" \
JWT_SECRET="<min-32-bytes-random-secret>" \
CORS_ORIGIN="[https://app.easytpv.com](https://app.easytpv.com)" \
java -jar target/tpv-backend-1.0.0-SNAPSHOT.jar


---

## ✅ 4. Checklist Mínimo de Producción

Antes de dar el despliegue por válido, verificaremos lo siguiente:
- [ ] Base de datos PostgreSQL accesible desde la red del backend.
- [ ] Credenciales y secretos almacenados de forma segura (no en texto plano).
- [ ] `SPRING_PROFILES_ACTIVE=prod` correctamente definido.
- [ ] CORS restringido exclusivamente al dominio real del frontend.
- [ ] Monitoreo y logs habilitados en el entorno de destino.

---

## 🐳 5. Docker (Próximos pasos)

Como siguiente iteración de la plataforma se incorporará:
- Un archivo `Dockerfile` optimizado para empaquetar el backend en una imagen ligera.
- Un `docker-compose.yml` para orquestar el backend junto con su base de datos PostgreSQL.

Esto simplificará enormemente los despliegues, haciéndolos repetibles entre entornos y reduciendo la fricción entre Desarrollo, QA y Producción.
```

