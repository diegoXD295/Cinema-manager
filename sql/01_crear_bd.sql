DROP DATABASE IF EXISTS cinema_manager;
CREATE DATABASE cinema_manager CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE cinema_manager;

CREATE TABLE sesiones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pelicula_id INT NOT NULL,
    sala_id INT NOT NULL,
    fecha_hora DATETIME NOT NULL,
    precio DECIMAL(8,2) NOT NULL,
    plazas_disponibles INT NOT NULL
);

CREATE TABLE clientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL,
    email VARCHAR(160) NOT NULL UNIQUE,
    saldo DECIMAL(10,2) NOT NULL DEFAULT 0
);

CREATE TABLE ventas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT NULL,
    sesion_id INT NOT NULL,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_venta_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id),
    CONSTRAINT fk_venta_sesion FOREIGN KEY (sesion_id) REFERENCES sesiones(id)
);

CREATE TABLE entradas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    venta_id INT NOT NULL,
    sesion_id INT NOT NULL,
    asiento VARCHAR(8) NOT NULL,
    precio DECIMAL(8,2) NOT NULL,
    CONSTRAINT fk_entrada_venta FOREIGN KEY (venta_id) REFERENCES ventas(id),
    CONSTRAINT fk_entrada_sesion FOREIGN KEY (sesion_id) REFERENCES sesiones(id),
    CONSTRAINT uq_sesion_asiento UNIQUE (sesion_id, asiento)
);
