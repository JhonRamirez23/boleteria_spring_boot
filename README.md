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

1. Asegúrate de que el contenedor de PostgreSQL esté corriendo:
```bash
docker compose up -d
```

2. Navega a la carpeta del backend:
```bash
cd backend
```

3. Da permisos de ejecución al wrapper de Maven (solo la primera vez):
```bash
chmod +x mvnw
```

4. Arranca el backend:
```bash
./mvnw spring-boot:run
```

El backend estará disponible en `http://localhost:8080`. Las migraciones de Flyway se ejecutan automáticamente al arrancar, y los usuarios de prueba se crean si no existen.

### Frontend

1. Navega a la carpeta del frontend:
```bash
cd frontend
```

2. Instala las dependencias:
```bash
npm install
```

3. Arranca el servidor de desarrollo:
```bash
ng serve
```

El frontend estará disponible en `http://localhost:4200`.

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

La autenticación se implementa mediante **JWT (JSON Web Token)** con Spring Security. El token se genera al hacer login y debe enviarse en el header `Authorization: Bearer <token>` en cada petición protegida. Los tokens expiran en 24 horas.

### Roles

```text
USER: puede consultar obras y funciones, crear reservas, ver y cancelar sus propias reservas.
ADMIN: puede hacer todo lo anterior, además de crear/editar/eliminar obras y funciones, y confirmar/rechazar reservas.
```

### Rutas protegidas

`POST /api/auth/login` | Público 
`GET /api/obras` | Público 
`GET /api/funciones/**` | Público 
`POST /api/obras` | Solo ADMIN 
`PUT/DELETE /api/obras/**` | Solo ADMIN 
`POST/PUT/DELETE /api/funciones/**` | Solo ADMIN 
`GET /api/reservas` | Solo ADMIN 
`POST /api/reservas` | Autenticado 
`PATCH /api/reservas/{id}/confirmar` | Solo ADMIN 
`PATCH /api/reservas/{id}/rechazar` | Solo ADMIN 
`PATCH /api/reservas/{id}/cancelar` | Autenticado (propietario) 

## API / Endpoints

La colección completa de Postman está disponible en `docs/postman_collection.json`. Importa el archivo en Postman junto con el environment `Boletería local` para probar todos los endpoints.

### Autenticación

| Método    | Endpoint | Acceso | Descripción   |
| POST      | `/api/auth/login` | Público       | Genera token JWT |

### Obras

| Método    | Endpoint              | Acceso    | Descripción
| GET       | `/api/obras`          | Público   | Lista obras activas con paginación y búsqueda por título
| GET       | `/api/obras/admin`    | ADMIN     | Lista todas las obras incluyendo inactivas
| GET       | `/api/obras/{id}`     | Público   | Obtiene una obra por ID
| POST      | `/api/obras`          | ADMIN     | Crea una nueva obra
| PUT       | `/api/obras/{id}`     | ADMIN     | Actualiza una obra
| DELETE | `/api/obras/{id}`        | ADMIN     | Elimina una obra

### Funciones

| Método    | Endpoint                          | Acceso    | Descripción
| GET       | `/api/funciones/obra/{obraId}`    | Público   | Lista funciones de una obra con paginación
| GET       | `/api/funciones/{id}`             | Público   | Obtiene una función por ID
| POST      | `/api/funciones`                  | ADMIN     | Crea una nueva función
| PUT       | `/api/funciones/{id}`             | ADMIN     | Actualiza una función
| DELETE    | `/api/funciones/{id}`             | ADMIN     | Elimina una función (si no tiene reservas activas)

### Reservas

| Método    | Endpoint                              | Acceso        | Descripción
| GET       | `/api/reservas`                       | ADMIN         | Lista todas las reservas con paginación
| GET       | `/api/reservas/mis-reservas`          | Autenticado   | Lista las reservas del usuario autenticado
| GET       | `/api/reservas/estado/{estado}`       | ADMIN         | Lista reservas filtradas por estado
| GET       | `/api/reservas/funcion/{funcionId}`   | ADMIN         | Lista reservas de una función
| POST      | `/api/reservas`                       | Autenticado   | Crea una reserva (valida aforo disponible)
| PATCH     | `/api/reservas/{id}/confirmar`        | ADMIN         | Confirma una reserva PENDIENTE
| PATCH     | `/api/reservas/{id}/rechazar`         | ADMIN         | Rechaza una reserva PENDIENTE
| PATCH     | `/api/reservas/{id}/cancelar`         | Autenticado   | Cancela una reserva PENDIENTE propia

### Formato de errores

Todos los errores devuelven el siguiente formato JSON:

```json
{
    "status": 400,
    "error": "Error de validación",
    "mensaje": "Los datos enviados no son válidos",
    "detalles": ["El título es obligatorio"],
    "timestamp": "20-07-2026 14:30:00"
}
```

## Credenciales de prueba

Rol: ADMIN  | Correo: admin@codesa.com  | Contraseña: Admin1234
Rol: USER   | Correo: user@codesa.com   | Contraseña: User1234

## Pruebas

El proyecto incluye pruebas unitarias sobre `ReservaValidacionService`, que cubre la lógica de negocio más crítica del sistema.

Para ejecutar las pruebas:

```bash
cd backend
./mvnw test
```

### Cobertura de pruebas

Clase                           | Pruebas   | Descripción
`ValidarReservaDuplicadaTest`   | 2         | Valida que un usuario no pueda reservar dos veces la misma función
`ValidarAforoTest`              | 4         | Valida disponibilidad de aforo incluyendo casos borde
`ValidarTransicionEstadoTest`   | 4         | Valida transiciones de estado de reservas
`ValidarPropietarioTest`        | 3         | Valida que solo el propietario pueda cancelar su reserva

## Decisiones de diseño

### Arquitectura: Monolito modular

Aunque el enunciado menciona "división de dominios en microservicios", los requisitos concretos describen un monolito en capas con un solo backend, un solo frontend y una sola base de datos relacional con FK cruzadas. Se optó por un **monolito modular**: el código está organizado por dominios (`auth`, `obras`, `funciones`, `reservas`, `shared`), con separación clara de capas (Controller → Service → Repository) dentro de cada dominio. Esta estructura es el paso previo natural a una eventual extracción de microservicios, y es la decisión arquitectónica correcta para el alcance de la prueba.

### Migraciones con Flyway

Se usa Flyway para gestionar el esquema de base de datos con migraciones versionadas (`V1__init.sql`). Hibernate se configura con `ddl-auto=validate`, lo que garantiza que las entidades JPA coincidan con el esquema real. Los usuarios iniciales se crean con un `ApplicationRunner` (DataSeeder) que hashea las contraseñas con BCrypt en tiempo de ejecución, evitando hashes hardcodeados en el SQL.

### Modelo de estados de reservas

Las reservas siguen un flujo de estados controlado:
- **PENDIENTE** → estado inicial al crear una reserva. Aparta cupo del aforo.
- **CONFIRMADA** → el ADMIN aprueba la reserva.
- **RECHAZADA** → el ADMIN rechaza la reserva. Libera el cupo.
- **CANCELADA** → el USER cancela su propia reserva pendiente. Libera el cupo.

El aforo disponible se calcula en tiempo real restando del aforo total la suma de boletas de todas las reservas en estado PENDIENTE o CONFIRMADA.

### CORS

CORS está configurado para permitir solo el origen `http://localhost:4200` con headers explícitos (`Authorization`, `Content-Type`, `Accept`, `X-Requested-With`, `Origin`). En producción se reemplazaría el origen por el dominio real del frontend.

### Mejoras futuras documentadas

- **Expiración automática de reservas:** implementar un `@Scheduled` que marque como EXPIRADAS las reservas PENDIENTES que superen un tiempo límite sin confirmación.
- **Edición de reservas:** permitir al USER modificar la cantidad de boletas de una reserva PENDIENTE.


## Entregables

- [x] Repositorio Git con historial de commits
- [x] Proyecto backend completo
- [x] Proyecto frontend completo
- [x] Script SQL (`database/schema.sql`)
- [x] README con instrucciones de instalación y ejecución
- [x] Diagrama entidad-relación (`docs/ER.png`)
- [ ] Colección Postman (`docs/postman_collection.json`)