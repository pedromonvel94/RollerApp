# RollerSpeed

Plataforma web monolítica para la escuela de patinaje **Roller Speed** (Santa Marta, Colombia). El proyecto automatiza la inscripción de aspirantes, la gestión básica de alumnos e instructores y la consulta de información institucional, integrando autenticación con Spring Security y tokens JWT.

## Equipo de trabajo

- Juan Pedro Montoya Vélez  
- Luis Alberto Cabezas Sánchez  
- Juan Diego Johnson Posada  
- Cristian Felipe Silva Castiblanco  

---

## Tabla de contenido

1. [Características actuales](#características-actuales)
2. [Stack tecnológico](#stack-tecnológico)
3. [Requisitos previos](#-requisitos-previos)
4. [Configuración inicial](#-configuración-inicial)
5. [Ejecución](#-ejecución)
6. [Roles y flujo de autenticación](#roles-y-flujo-de-autenticación)
7. [Documentación de la API](#-documentación-de-la-api-springdoc-openapi)
8. [Estructura del proyecto](#estructura-del-proyecto)
9. [Comandos útiles](#comandos-útiles)
10. [Estado del proyecto](#estado-del-proyecto)

---

## Características actuales

- Sitio web público con misión, visión, valores, servicios y eventos.
- Registro en línea para aspirantes (`ALUMNO`) e instructores (`INSTRUCTOR`).
- Gestión de alumnos e instructores con validaciones y cifrado de contraseñas.
- Autenticación JWT (stateless) + autorización por rol con anotaciones `@PreAuthorize`.
- Navbar dinámico que muestra opciones según el rol autenticado.
- Semilla automática de un usuario administrador (`ADMIN`) para acceder a funcionalidades avanzadas.
- Documentación de la API expuesta con **Springdoc OpenAPI** y Swagger UI.

> ⚠️ Módulos como pagos, asignación de clases, reportes y control de asistencia están planificados pero aún no implementados.

---

## Stack tecnológico

- **Java 17** · **Spring Boot 3.3.8**
- Spring Web, Spring Data JPA, Spring Validation
- Spring Security 6 + JSON Web Token (JJWT 0.11.5)
- Thymeleaf + Bootstrap 5
- MySQL 8 (puede adaptarse a PostgreSQL con ajustes mínimos)
- Springdoc OpenAPI (Swagger UI)
- Maven Wrapper (`mvnw`)

---

## 🚀 Requisitos previos

- [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [MySQL 8](https://dev.mysql.com/downloads/) (o distribución compatible)
- Git
- Editor recomendado: VS Code con extensión **Spring Boot Dashboard**

---

## ⚙️ Configuración inicial

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/tu-usuario/rollerspeed.git
   cd rollerspeed
   ```

2. **Crear la base de datos y el usuario (MySQL)**
   ```sql
   CREATE DATABASE speed;
   CREATE USER 'userspeedroller'@'localhost' IDENTIFIED BY '123456';
   GRANT ALL PRIVILEGES ON speed.* TO 'userspeedroller'@'localhost';
   FLUSH PRIVILEGES;
   ```

3. **Configurar `src/main/resources/application.properties`**
   ```properties
   spring.application.name=rollerspeed
   server.port=8080

   spring.datasource.url=jdbc:mysql://localhost:3306/speed
   spring.datasource.username=userspeedroller
   spring.datasource.password=123456
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
   spring.jpa.show-sql=true

   springdoc.api-docs.path=/doc
   springdoc.swagger-ui.path=/swagger-ui.html

   # JWT (usar una cadena Base64 segura en producción)
   rollerspeed.security.jwt.secret=c2VjcmV0S2V5Um9sbGVyU3BlZWQxMjM0NTY3ODkwMTIzNDU2Nzg5MA==
   rollerspeed.security.jwt.expiration=3600000
   rollerspeed.security.jwt.issuer=RollerSpeed-API
   ```

   > 🔒 **Recomendado:** Sobrescribir estas propiedades mediante variables de entorno o un archivo `application-local.properties` para entornos reales.

---

## ▶️ Ejecución

**Desde la terminal**
```bash
./mvnw spring-boot:run
```

**O usando Maven**
```bash
./mvnw clean package
java -jar target/rollerspeed-0.0.1-SNAPSHOT.jar
```

---

## Roles y flujo de autenticación

| Rol          | Acceso destacado                                                                 |
|--------------|-----------------------------------------------------------------------------------|
| `ADMIN`      | Gestión completa de alumnos, instructores y acceso administrativo al sistema.     |
| `INSTRUCTOR` | Acceso a listados de alumnos/clases y navegación preparada para su propio módulo. |
| `ALUMNO`     | Consulta de clases, calendarios y contenidos personalizados (en progreso).        |
| Público      | Vistas institucionales (misión, visión, valores, servicios, eventos).             |

- Autenticación disponible vía API (`POST /api/auth/login`) o formulario web (`/auth/login`).
- Tras un login exitoso se genera un JWT con vigencia de 60 minutos y se almacena en la cookie `HttpOnly` **`rollerspeed-token`**.
- Los endpoints protegidos leen el token desde el header `Authorization` o desde la cookie.
- Registros públicos:
  - `/alumnos/nuevo` → crea un usuario con rol `ALUMNO` y redirige a login tras guardar.
  - `/instructores/nuevo` → crea un usuario con rol `INSTRUCTOR` y también redirige a login.
- Usuario administrador creado automáticamente:
  - **Correo:** `admin@rollerspeed.com`
  - **Contraseña:** `Admin123!`

### Seguridad interna

- `SecurityConfig` define un `SecurityFilterChain` stateless, el filtro JWT personalizado y las rutas abiertas.
- `JwtAuthenticationFilter` verifica tokens en cada request y reconstruye el contexto de seguridad.
- Controladores anotados con `@PreAuthorize` para reforzar permisos en capa de servicio/presentación.

---

## 📑 Documentación de la API (Springdoc OpenAPI)

- Bean `OpenApiConfig` describe la API como **SpeedRoller API 1.0**.
- Endpoints útiles:
  - `GET /doc` → redirige a Swagger UI.
  - `GET /swagger-ui.html` → interfaz Swagger estándar.
- Para probar endpoints protegidos desde Swagger:
  1. Autenticarse (API o formulario) y copiar el token JWT.
  2. En Swagger, pulsar **Authorize** e ingresar `Bearer <tu-token>`.

---

## Estructura del proyecto

```bash
rollerspeed/
├── src/
│   ├── main/
│   │   ├── java/com/rollerspeed/rollerspeed
│   │   │   ├── config/        # Seguridad, OpenAPI, seeds
│   │   │   ├── controller/    # Controladores MVC + Auth REST
│   │   │   ├── Model/         # Entidades JPA (Alumno, Instructor, Clase)
│   │   │   ├── Repository/    # Repositorios Spring Data JPA
│   │   │   └── Service/       # Reglas de negocio
│   │   └── resources/
│   │       ├── templates/     # Vistas Thymeleaf (pages, fragments)
│   │       └── application.properties
│   └── test/                  # Espacio reservado para pruebas
├── pom.xml                    # Dependencias y plugins Maven
└── README.md                  # Documentación del proyecto
```

---

## Comandos útiles

| Descripción                     | Comando                                    |
|---------------------------------|--------------------------------------------|
| Ejecutar con live reload        | `./mvnw spring-boot:run`                   |
| Compilar (sin pruebas)          | `./mvnw clean package -DskipTests`         |
| Ejecutar pruebas unitarias      | `./mvnw test`                              |
| Ver dependencias del proyecto   | `./mvnw dependency:tree`                  |

---

## Estado del proyecto

- ✅ Información institucional, registro de alumnos/instructores, autenticación JWT y documentación Swagger.
- ⚙️ En desarrollo: paneles especializados por rol, control de asistencia, módulo de pagos, reportes y asignación detallada de clases.
- 🗺️ Próximos pasos sugeridos:
  1. Implementar el panel de instructor (gestión de alumnos, horarios y asistencia).
  2. Exponer el módulo de pagos y estado financiero del alumno.
  3. Añadir reportes (pagos, asistencia) para el rol administrador.
  4. Completar galería multimedia y testimonios según el caso de estudio.

---

> Si usas este repositorio como base, recuerda ajustar credenciales y secretos antes de desplegarlo en un entorno productivo.
