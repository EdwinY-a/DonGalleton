-- ---------------------------------------------------------------------------- --
-- Archivo: 01_DDL_DonGalleto.sql												--
-- Version: 1.0                                                     			--
-- Autor:   Francisco Javier Rocha Aldana   									--
-- Email:   rochaaldanafcojavier@gmail.com / 21000459@alumnos.utleon.edu.mx		--
-- Fecha de elaboracion: 19-11-2023                                 			--
-- ---------------------------------------------------------------------------- --

DROP DATABASE IF EXISTS don_galleto;

CREATE DATABASE IF NOT EXISTS don_galleto;

USE don_galleto;

drop table if exists usuario;
CREATE TABLE usuario(
	idUsuario			INT NOT NULL PRIMARY KEY AUTO_INCREMENT,		-- 1
    nombreUsuario		VARCHAR(45) NOT NULL DEFAULT "",				-- 2
    contrasenia			VARCHAR(80) NOT NULL DEFAULT "",				-- 3
    rol					VARCHAR(30) NOT NULL,							-- 4
    estatus				INT NOT NULL DEFAULT 1							-- 5
);

ALTER TABLE usuario
ADD COLUMN lastToken VARCHAR(65) NOT NULL DEFAULT '',
ADD COLUMN dateLastToken DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP;


drop table if exists logsUser;
CREATE TABLE IF NOT EXISTS logsUser (
    id 				INT AUTO_INCREMENT PRIMARY KEY,
    fecha 			TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario 		VARCHAR(255) NOT NULL,
    procedimiento 	VARCHAR(255) NOT NULL
);

SELECT * FROM logsUser;

CREATE TABLE medida(
	idMedida			INT NOT NULL PRIMARY KEY AUTO_INCREMENT,		-- 1
	tipoMedida			VARCHAR(15)										-- 2
);

CREATE TABLE materia_prima(
	idMateriaPrima		INT NOT NULL PRIMARY KEY AUTO_INCREMENT,		-- 1
	nombreMateria		VARCHAR(45) NOT NULL DEFAULT "",				-- 2
	fechaCompra			DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,	-- 3
	fechaVencimiento	DATETIME,										-- 4
	estatus				INT NOT NULL DEFAULT 1,							-- 5
	cantidadExistentes	DOUBLE NOT NULL DEFAULT 0.0,					-- 6
	precioCompra		DOUBLE NOT NULL DEFAULT 0.0,					-- 7
	porcentaje			INT NOT NULL DEFAULT 100,						-- 8
	idMedida			INT NOT NULL,									-- 9
    CONSTRAINT materia_prima_idMedida_fk FOREIGN KEY (idMedida) REFERENCES medida(idMedida)
);

CREATE TABLE producto(
	idProducto			INT NOT NULL PRIMARY KEY AUTO_INCREMENT,		-- 1
	nombreProducto		VARCHAR(50) NOT NULL DEFAULT "",				-- 2
	cantidadExistentes	DOUBLE NOT NULL DEFAULT 0.0,					-- 3
	precioVenta			DOUBLE NOT NULL DEFAULT 0.0,					-- 4
	precioProduccion	DOUBLE NOT NULL DEFAULT 0.0,					-- 5
	idMedida			INT NOT NULL,									-- 6
    fotografia			LONGTEXT,										-- 7
	CONSTRAINT producto_idMedida_fk FOREIGN KEY (idMedida) REFERENCES medida(idMedida)
);

CREATE TABLE crear_producto(
	idCrearProducto		INT NOT NULL PRIMARY KEY AUTO_INCREMENT,		-- 1
	porcion				DOUBLE NOT NULL DEFAULT 0.0,					-- 2
	idProducto			INT NOT NULL,									-- 3
	idMedida			INT NOT NULL,									-- 4
    idMateriaPrima		INT NOT NULL,									-- 5
    CONSTRAINT crear_producto_idProducto_fk FOREIGN KEY (idProducto) REFERENCES producto(idProducto),
    CONSTRAINT crear_producto_idMedida_fk FOREIGN KEY (idMedida) REFERENCES medida(idMedida),
    CONSTRAINT crear_producto_idMateriaPrima_fk FOREIGN KEY (idMateriaPrima) REFERENCES materia_prima(idMateriaPrima)
);

CREATE TABLE venta(
	idVenta				INT NOT NULL PRIMARY KEY AUTO_INCREMENT,		-- 1
	fechaVenta			DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,	-- 2
	total				DOUBLE NOT NULL DEFAULT 0.0						-- 3
);

CREATE TABLE detalle_venta(
	idDetalleVenta		INT NOT NULL PRIMARY KEY AUTO_INCREMENT,		-- 1
	cantidad			DOUBLE NOT NULL DEFAULT 0.0,					-- 2
	subtotal			DOUBLE NOT NULL DEFAULT 0.0,					-- 3
	idVenta				INT NOT NULL,									-- 4
	idProducto			INT NOT NULL,									-- 5
	idMedida			INT NOT NULL,									-- 6
    CONSTRAINT detalle_venta_idVenta_fk FOREIGN KEY (idVenta) REFERENCES venta(idVenta),
    CONSTRAINT detalle_venta_idProducto_fk FOREIGN KEY (idProducto) REFERENCES producto(idProducto),
    CONSTRAINT detalle_venta_idMedida_fk FOREIGN KEY (idMedida) REFERENCES medida(idMedida)
);

CREATE TABLE movimiento(
	idMovimiento		INT NOT NULL PRIMARY KEY AUTO_INCREMENT,		-- 1
	fechaMovimiento		DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,	-- 2
	tipoMovimiento		VARCHAR(45) NOT NULL DEFAULT "",				-- 3
	monto				DOUBLE NOT NULL DEFAULT 0.0,					-- 4
    idVenta				INT,											-- 5
    idMateriaPrima		INT,											-- 6
    CONSTRAINT movimiento_idVenta_fk FOREIGN KEY (idVenta) REFERENCES venta(idVenta),
    CONSTRAINT movimiento_idMateriaPrima_fk FOREIGN KEY (idMateriaPrima) REFERENCES materia_prima(idMateriaPrima)
);
