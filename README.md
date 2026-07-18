# Sistema de Gestión de Boletería

Aplicación web para la gestión de boletería de un teatro de mediana escala. Permite a los usuarios consultar la cartelera de obras y sus funciones, realizar reservas de boletas, y a los administradores gestionar la programación del teatro y validar las reservas.

## Tabla de contenidos

🚧 *Se completará al fijar todas las secciones.*

## Stack tecnológico

- Frontend: Angular + Angular Material
- Backend: Spring Boot 3.5.16 (Java 17)
- Base de datos: PostgreSQL 16
- Migraciones: Flyway
- Build backend: Maven
- Autenticación: JWT + Spring Security
- Contenedores: Docker / Docker Compose


## Requisitos previos

Para ejecutar el proyecto se necesita tener instalado:

- **Java 17** (JDK)
- **Docker** y Docker Compose
- **Node.js** y Angular CLI (para el frontend)
- **Git**

Maven no requiere instalación global: el proyecto incluye el Maven Wrapper (`./mvnw`).

## Estructura del repositorio

```text
prueba_codesa/
├── backend/            → API REST en Spring Boot
├── frontend/           → aplicación Angular
├── database/           → script SQL de creación de la base de datos
├── docs/               → diagrama entidad-relación y colección Postman
├── compose.yaml        → definición del contenedor PostgreSQL
└── README.md
```

El backend está organizado por **dominios** (monolito modular), manteniendo la arquitectura en capas (Controller → Service → Repository) dentro de cada dominio:

```text
com.codesa.prueba
├── auth/          → autenticación, usuarios y roles
├── obras/         → gestión de obras
├── funciones/     → gestión de funciones (presentaciones)
├── reservas/      → flujo de reservas
└── shared/        → configuración, seguridad y manejo de excepciones transversal
```

## Configuración y ejecución

### Base de datos

Levantar el contenedor de PostgreSQL desde la raíz del proyecto:

```bash
docker compose up -d
```

Esto crea una instancia de PostgreSQL 16 con la base de datos `prueba` en el puerto `5432`. Las migraciones de Flyway se ejecutan automáticamente al arrancar el backend.

### Backend

 *Pendiente.*

### Frontend

 *Pendiente.*

## Modelo de datos

El modelo relacional se compone de cinco entidades:

- **roles** — catálogo de roles del sistema (USER, ADMIN).
- **usuarios** — usuarios registrados; cada uno asociado a un rol.
- **obras** — obras de teatro disponibles en la cartelera.
- **funciones** — presentaciones de una obra en una fecha y hora específicas, con aforo y precio.
- **reservas** — reservas de boletas que realiza un usuario sobre una función.

**Flujo de estados de una reserva:**

`PENDIENTE` → `CONFIRMADA` | `RECHAZADA` | `CANCELADA`

Una reserva nace en estado PENDIENTE cuando el usuario la crea, apartando el cupo correspondiente. El administrador puede confirmarla o rechazarla; el usuario puede cancelar su propia reserva pendiente. Las reservas en estado PENDIENTE y CONFIRMADA descuentan aforo disponible; las RECHAZADA y CANCELADA lo liberan.

Diagrama entidad-relación: *Pendiente (docs/).*

## Autenticación y roles

 *Pendiente.*

## API / Endpoints

 *Pendiente (ver colección Postman en docs/).*

## Credenciales de prueba

 *Pendiente.*

## Pruebas

 *Pendiente.*

## Decisiones de diseño

 *Pendiente.* Sección donde se justificarán las decisiones arquitectónicas principales: organización por dominios (monolito modular), uso de Flyway para migraciones versionadas, y el modelo de estados de las reservas.

## Entregables

- [x] Repositorio Git con historial de commits
- [ ] Proyecto backend completo
- [ ] Proyecto frontend completo
- [ ] Script SQL
- [ ] README con instrucciones de instalación y ejecución
- [ ] Diagrama entidad-relación
- [ ] Colección Postman