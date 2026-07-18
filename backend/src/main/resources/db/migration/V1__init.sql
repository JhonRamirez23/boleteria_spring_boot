-- V1__init.sql
-- Sistema de Gestión de Boletería

-- ROLES
CREATE TABLE roles (
    id     BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

-- USUARIOS
CREATE TABLE usuarios (
    id       BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre   VARCHAR(100) NOT NULL,
    email    VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol_id   BIGINT NOT NULL,
    CONSTRAINT fk_usuarios_rol FOREIGN KEY (rol_id) REFERENCES roles(id)
);

-- OBRAS
CREATE TABLE obras (
    id               BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    titulo           VARCHAR(200) NOT NULL,
    descripcion      TEXT,
    genero           VARCHAR(100),
    duracion_minutos INTEGER,
    activa           BOOLEAN NOT NULL DEFAULT TRUE
);

-- FUNCIONES
CREATE TABLE funciones (
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    obra_id     BIGINT NOT NULL,
    fecha_hora  TIMESTAMP NOT NULL,
    sala        VARCHAR(100),
    aforo_total INTEGER NOT NULL,
    precio      NUMERIC(10, 2) NOT NULL,
    CONSTRAINT fk_funciones_obra FOREIGN KEY (obra_id) REFERENCES obras(id)
);

-- RESERVAS
CREATE TABLE reservas (
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    usuario_id    BIGINT NOT NULL,
    funcion_id    BIGINT NOT NULL,
    cantidad      INTEGER NOT NULL,
    estado        VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE',
    fecha_reserva TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_reservas_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    CONSTRAINT fk_reservas_funcion FOREIGN KEY (funcion_id) REFERENCES funciones(id),
    CONSTRAINT chk_reservas_estado CHECK (estado IN ('PENDIENTE', 'CONFIRMADA', 'RECHAZADA', 'CANCELADA')),
    CONSTRAINT chk_reservas_cantidad CHECK (cantidad > 0)
);

-- ÍNDICES
CREATE INDEX idx_funciones_fecha_hora ON funciones(fecha_hora);
CREATE INDEX idx_funciones_obra_id    ON funciones(obra_id);
CREATE INDEX idx_reservas_usuario_id  ON reservas(usuario_id);

-- SEED: ROLES
INSERT INTO roles (nombre) VALUES ('ADMIN');
INSERT INTO roles (nombre) VALUES ('USER');