# Sistema de Gestión de Boletería

Aplicación web para la gestión de boletería de un teatro de mediana escala. Permite consultar la cartelera de obras y sus funciones, realizar reservas de boletas, y a los administradores gestionar la programación del teatro y validar las reservas.

## Tecnologías

- **Frontend:** Angular 17 + Angular Material
- **Backend:** Spring Boot 3.5.16 (Java 17)
- **Base de datos:** PostgreSQL 16
- **Migraciones:** Flyway
- **Autenticación:** JWT + Spring Security
- **Build:** Maven

## Requisitos previos

Tener instalado antes de comenzar:

- **Java 17** (JDK)
- **Node.js 18** o superior
- **Angular CLI 17** (`npm install -g @angular/cli@17`)
- **Docker** (para la base de datos PostgreSQL)
- **Git**

> **Nota:** Maven no requiere instalación. El proyecto incluye el Maven Wrapper (`./mvnw`).

## Instalación y ejecución

### 1. Clonar el repositorio

```bash
git clone https://github.com/JhonRamirez23/boleteria_spring_boot.git
cd prueba_codesa
```

### 2. Levantar la base de datos

Levantar el contenedor de PostgreSQL desde la raíz del proyecto:

```bash
docker compose up -d
```

Esto crea una instancia de PostgreSQL 16 con la base de datos `prueba` en el puerto `5432`.

> **Nota:** Para usar una instancia propia de PostgreSQL en lugar de Docker, crear una base de datos llamada `prueba` y ajustar las credenciales en `backend/src/main/resources/application.properties`. Flyway se encarga de crear todas las tablas automáticamente al arrancar el backend.

### 3. Arrancar el backend

**En Linux/Mac:**

```bash
cd backend
chmod +x mvnw
./mvnw spring-boot:run
```

**En Windows:**

```bash
cd backend
mvnw spring-boot:run
```

El backend queda disponible en `http://localhost:8080`.

> **Nota:** `chmod +x mvnw` solo es necesario la primera vez en Linux/Mac.

Al arrancar, el sistema automáticamente:

- Ejecuta las migraciones de Flyway y crea todas las tablas.
- Crea los usuarios de prueba (ADMIN y USER) si no existen.

### 4. Arrancar el frontend

Abrir otra terminal y ejecutar:

```bash
cd frontend
npm install
ng serve
```

El frontend queda disponible en `http://localhost:4200`.

### 5. Acceder a la aplicación

Abrir `http://localhost:4200` en el navegador e iniciar sesión con alguna de las credenciales de prueba.

## Credenciales de prueba

```text
ADMIN → admin@codesa.com  |  Admin1234
USER  → user@codesa.com   |  User1234
```

## Roles y permisos

```text
USER   → Consultar obras y funciones, crear reservas,
         ver y cancelar sus propias reservas.

ADMIN  → Todo lo anterior + gestionar obras y funciones,
         confirmar y rechazar reservas.
```

## Estructura del proyecto

```text
prueba_codesa/
├── backend/            → API REST en Spring Boot
├── frontend/           → aplicación Angular
├── database/           → script SQL de creación de la base de datos
├── docs/               → diagrama entidad-relación y colección Postman
├── compose.yaml        → definición del contenedor PostgreSQL
└── README.md
```

## Documentación adicional

- **Script SQL:** `database/schema.sql`
- **Diagrama entidad-relación:** `docs/ER.png`
- **Colección Postman:** `docs/postman_collection.json`

> **Nota:** Importar la colección Postman junto con el environment para probar los endpoints.

## Pruebas

**En Linux/Mac:**

```bash
cd backend
./mvnw test
```

**En Windows:**

```bash
cd backend
mvnw test
```

## Documentación técnica

Consultar el archivo [`docs/TECHNICAL.md`](docs/TECHNICAL.md) para detalles sobre la arquitectura, el modelo de datos, los endpoints de la API y las decisiones de diseño.
