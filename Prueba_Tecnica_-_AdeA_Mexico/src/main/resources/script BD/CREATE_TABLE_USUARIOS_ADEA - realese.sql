-- Crea la base de datos
CREATE DATABASE usuarios_adea;

-- Utiliza la base de datos recíen creada
USE usuarios_adea;

-- Permitir los Updates/Deletes sin la "proteccion" de MySQL del "Safe Update mode"
SET SQL_SAFE_UPDATES = 0;

-- Crea la tabla
CREATE TABLE USUARIO(
	LOGIN VARCHAR(20) NOT NULL PRIMARY KEY,
    -- Tamaño cambiado porque "SHA-256 + Base64" es mas grande que el tamaño de 30
    PASS VARCHAR(60) NOT NULL,
    NOMBRE VARCHAR(50) NOT NULL,
    CLIENTE FLOAT NOT NULL,
    
    EMAIL VARCHAR(50),
    FECHAALTA DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FECHABAJA DATETIME,
    ESTATUS CHAR(1) NOT NULL DEFAULT 'A',
    
    INTENTOS FLOAT NOT NULL DEFAULT 0,
    FECHAREVOCADO DATETIME,
    FECHA_VIGENCIA DATETIME,
    -- No puede tener un valor vacio o null, por tanto, por defecto puse 0
    NO_ACCESO INTEGER DEFAULT 0,
    
    APELLIDO_PATERNO VARCHAR(50),
    APELLIDO_MATERNO VARCHAR(50),
    AREA NUMERIC(4),
    FECHAMODIFICACION DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);