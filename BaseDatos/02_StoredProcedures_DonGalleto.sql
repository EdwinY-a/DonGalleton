-- ---------------------------------------------------------------------------- --
-- Archivo: 02_StoredProcedures_DonGalleto.sql                      			--
-- Version: 1.0                                                     			--
-- Autor:   Francisco Javier Rocha Aldana   									--
-- Email:   rochaaldanafcojavier@gmail.com / 21000459@alumnos.utleon.edu.mx		--
-- Fecha de elaboracion: 19-11-2023                                 			--
-- ---------------------------------------------------------------------------- --

USE don_galleto;

-- INICIO TRIGGER LOGS --



-- FIN TRIGGER LOGS -- 

-- INICIO PROCEDURES USUARIO: INSERTAR, ACTUALIZAR, ACTIVAR, DESACTIVAR --

DROP PROCEDURE IF EXISTS insertarUsuario;
DELIMITER $$
CREATE PROCEDURE insertarUsuario(
								IN	var_nombreUsuario		VARCHAR(45),		-- 1
								IN	var_contrasenia			VARCHAR(80),		-- 2
                                IN 	var_rol					VARCHAR(30),		-- 3
                                IN  var_logs				VARCHAR(50),
    
								OUT	var_idUsuario			INT					-- 4
)
BEGIN
	declare prodecimiento VARCHAR(50);
	INSERT INTO usuario(nombreUsuario, contrasenia, rol)
				 VALUES(var_nombreUsuario, var_contrasenia, var_rol);

	SET var_idUsuario = LAST_INSERT_ID();
    SET prodecimiento = CONCAT('Se inserto usuario con ID: ', var_idUsuario);
    
    INSERT INTO logsUser (usuario, procedimiento)
				VALUES (var_logs, prodecimiento);
END $$
DELIMITER ;

CALL insertarUsuario('EdwinRive', '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4', 'admin', 'EdwinRive', @idUsuarioCreado);

SELECT @idUsuarioCreado AS idUsuarioCreado;

DROP PROCEDURE IF EXISTS actualizarUsuario;
DELIMITER $$
CREATE PROCEDURE actualizarUsuario(
									IN	var_idUsuario			INT,				-- 1
									IN	var_nombreUsuario		VARCHAR(45),		-- 2
									IN	var_contrasenia			VARCHAR(45),		-- 3
                                    IN 	var_rol					VARCHAR(30),			-- 4
                                    IN 	var_logs				VARCHAR(30)
)
BEGIN
declare prodecimiento VARCHAR(50);
	UPDATE	usuario
    SET		nombreUsuario = var_nombreUsuario, contrasenia = var_contrasenia, rol = var_rol
    WHERE	idUsuario = var_idUsuario;
    
    SET prodecimiento = CONCAT('Se actualizo usuario con ID: ', var_idUsuario);
    
    INSERT INTO logsUser (usuario, procedimiento)
				VALUES (var_logs, prodecimiento);
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS activarUsuario;
DELIMITER $$
CREATE PROCEDURE activarUsuario(
								IN	var_idUsuario		INT		-- 1
)
BEGIN
	UPDATE	usuario
    SET		estatus = 1
    WHERE	idUsuario = var_idUsuario;
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS desActivarUsuario;
DELIMITER $$
CREATE PROCEDURE desActivarUsuario(
									IN	var_idUsuario		INT,		-- 1
                                    IN 	var_logs			VARCHAR(30)
)
BEGIN
declare prodecimiento VARCHAR(50);
	UPDATE	usuario
    SET		estatus = 0
    WHERE	idUsuario = var_idUsuario;
    
    SET prodecimiento = CONCAT('Se desactivo usuario con ID: ', var_idUsuario);
    
    INSERT INTO logsUser (usuario, procedimiento)
				VALUES (var_logs, prodecimiento);
END $$
DELIMITER ;

-- FIN PROCEDURES USUARIO --

-- INICIO PROCEDURES MEDIDA: INSERTAR, ACTUALIZAR --

DROP PROCEDURE IF EXISTS insertarMedida;
DELIMITER $$
CREATE PROCEDURE insertarMedida(
								IN	var_tipoMedida		VARCHAR(15),		-- 1

								OUT	var_idMedida		INT					-- 2
)
BEGIN
	INSERT INTO medida(tipoMedida)
				VALUES(var_tipoMedida);

	SET	var_idMedida = LAST_INSERT_ID();
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS actualizarMedida;
DELIMITER $$
CREATE PROCEDURE actualizarMedida(
									IN	var_idMedida		INT,			-- 1
									IN	var_tipoMedida		VARCHAR(15)		-- 2
)
BEGIN
	UPDATE	medida
    SET		tipoMedida = var_tipoMedida
    WHERE	idMedida = var_idMedida;
END $$
DELIMITER ;

-- FIN PROCEDURES MEDIDA --

-- INICIO PROCEDURES MATERIA PRIMA: INSERTAR, ACTUALIZAR, ACTIVAR, DESACTIVAR, DESACTIVAR POR FECHA DE VENCIMIENTO --

DROP PROCEDURE IF EXISTS insertarMateriaPrima;
DELIMITER $$
CREATE PROCEDURE insertarMateriaPrima(
										IN	var_nombreMateria			VARCHAR(45),		-- 1
										IN	var_fechaCompra				VARCHAR(45),		-- 2
										IN	var_fechaVencimiento		VARCHAR(45),		-- 3
										IN	var_cantidadExistentes		DOUBLE,				-- 4
										IN	var_precioCompra			DOUBLE,				-- 5
										IN	var_porcentaje				INT,				-- 6
										IN	var_idMedida				INT,				-- 7
                                        IN  var_logs					VARCHAR(30),

										OUT	var_idMateriaPrima			INT					-- 8
)
BEGIN
declare prodecimiento VARCHAR(50);
	INSERT INTO materia_prima(nombreMateria, fechaCompra,
															fechaVencimiento, cantidadExistentes, precioCompra,
																												porcentaje, idMedida)
					   VALUES(var_nombreMateria, IFNULL(STR_TO_DATE(var_fechaCompra, '%d/%m/%Y'), CURRENT_TIMESTAMP),
									STR_TO_DATE(var_fechaVencimiento, '%d/%m/%Y'), var_cantidadExistentes, var_precioCompra,
																										var_porcentaje, var_idMedida);

	SET	var_idMateriaPrima = LAST_INSERT_ID();
    SET prodecimiento = CONCAT('Se Registro Materia Prima con ID: ', var_idMateriaPrima);
    
    INSERT INTO logsUser (usuario, procedimiento)
				VALUES (var_logs, prodecimiento);
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS actualizarMateriaPrima;
DELIMITER $$
CREATE PROCEDURE actualizarMateriaPrima(
										IN	var_idMateriaPrima			INT,				-- 1
										IN	var_nombreMateria			VARCHAR(45),		-- 2
										IN	var_fechaCompra				VARCHAR(45),		-- 3
										IN	var_fechaVencimiento		VARCHAR(45),		-- 4
										IN	var_cantidadExistentes		DOUBLE,				-- 5
										IN	var_precioCompra			DOUBLE,				-- 6
										IN	var_porcentaje				INT,				-- 7
                                        IN  var_logs					VARCHAR(30),
										IN	var_idMedida				INT					-- 8
)
BEGIN

declare prodecimiento VARCHAR(50);
	IF var_porcentaje > 0 THEN
		UPDATE	materia_prima
        SET		estatus = 1
        WHERE	idMateriaPrima = var_idMateriaPrima;
	ELSE
		UPDATE	materia_prima
        SET		estatus = 0
        WHERE	idMateriaPrima = var_idMateriaPrima;
	END IF;
    
    UPDATE	materia_prima
    SET		nombreMateria = var_nombreMateria, fechaCompra = STR_TO_DATE(var_fechaCompra, '%d/%m/%Y'),
			fechaVencimiento = STR_TO_DATE(var_fechaVencimiento, '%d/%m/%Y'), cantidadExistentes = var_cantidadExistentes,
            precioCompra = var_precioCompra, porcentaje = var_porcentaje, idMedida = var_idMedida
	WHERE	idMateriaPrima = var_idMateriaPrima;
    
    SET prodecimiento = CONCAT('Se Modifico Materia Prima con ID: ', var_idMateriaPrima);
    
    INSERT INTO logsUser (usuario, procedimiento)
				VALUES (var_logs, prodecimiento);
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS activarMateriaPrima;
DELIMITER $$
CREATE PROCEDURE activarMateriaPrima(
									IN	var_idMateriaPrima		INT,		-- 1
                                    IN	var_porcentaje			INT			-- 2
)
BEGIN
	UPDATE	materia_prima
    SET		estatus = 1, porcentaje = var_porcentaje
    WHERE	idMateriaPrima = var_idMateriaPrima;
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS desActivarMateriaPrima;
DELIMITER $$
CREATE PROCEDURE desActivarMateriaPrima(
										IN	var_idMateriaPrima		INT,		-- 1
                                        IN	var_logs				VARCHAR(30)
)
BEGIN
	UPDATE	materia_prima
    SET		estatus = 0
    WHERE	idMateriaPrima = var_idMateriaPrima;
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS desActivarMateriaPrimaPorFechaVencimiento;
DELIMITER $$
CREATE PROCEDURE desActivarMateriaPrimaPorFechaVencimiento(
															IN	var_fechaActual		VARCHAR(45)		-- 1
)
BEGIN


	UPDATE	materia_prima
    SET		estatus = 0
    WHERE	fechaVencimiento < STR_TO_DATE(var_fechaActual, '%d/%m/%Y');
    
   
END $$
DELIMITER ;

-- FIN PROCEDURES MATERIA PRIMA --

-- INICIO PROCEDURES PRODUCTO: INSERTAR, ACTUALIZAR --

DROP PROCEDURE IF EXISTS insertarProducto;
DELIMITER $$
CREATE PROCEDURE insertarProducto(
								IN	var_nombreProducto			VARCHAR(50),		-- 1
								IN	var_cantidadExistentes		DOUBLE,				-- 2
								IN	var_precioVenta				DOUBLE,				-- 3
								IN	var_precioProduccion		DOUBLE,				-- 4
								IN	var_idMedida				INT,				-- 5
                                IN	var_fotografia				LONGTEXT,			-- 6

                                OUT	var_idProducto				INT					-- 7
)
BEGIN
	INSERT INTO producto(nombreProducto, cantidadExistentes, precioVenta, precioProduccion, idMedida, fotografia)
				  VALUES(var_nombreProducto, var_cantidadExistentes, var_precioVenta, var_precioProduccion, var_idMedida, var_fotografia);

	SET var_idProducto = LAST_INSERT_ID();
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS actualizarProducto;
DELIMITER $$
CREATE PROCEDURE actualizarProducto(
									IN	var_idProducto				INT,				-- 1
									IN	var_nombreProducto			VARCHAR(50),		-- 2
									IN	var_cantidadExistentes		DOUBLE,				-- 3
									IN	var_precioVenta				DOUBLE,				-- 4
									IN	var_precioProduccion		DOUBLE,				-- 5
									IN	var_idMedida				INT,				-- 6
                                    IN	var_fotografia				LONGTEXT			-- 7
)
BEGIN
	UPDATE	producto
    SET		nombreProducto = var_nombreProducto, cantidadExistentes = var_cantidadExistentes, precioVenta = var_precioVenta,
			precioProduccion = var_precioProduccion, idMedida = var_idMedida, fotografia = var_fotografia
	WHERE	idProducto = var_idProducto;
END $$
DELIMITER ;

-- FIN PROCEDURES PRODUCTO --

-- INICIO PROCEDURES CREAR PRODUCTO: INSERTAR, ACTUALIZAR --

DROP PROCEDURE IF EXISTS insertarCrearProducto;
DELIMITER $$
CREATE PROCEDURE insertarCrearProducto(
										IN	var_porcion				DOUBLE,		-- 1
										IN	var_idProducto			INT,		-- 2
										IN	var_idMedida			INT,		-- 3
										IN	var_idMateriaPrima		INT,		-- 4

										OUT	var_idCrearProducto		INT			-- 5
)
BEGIN
	INSERT INTO crear_producto(porcion, idProducto,	idMedida, idMateriaPrima)
						VALUES(var_porcion, var_idProducto, var_idMedida, var_idMateriaPrima);

	SET var_idCrearProducto = LAST_INSERT_ID();
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS actualizarCrearProducto;
DELIMITER $$
CREATE PROCEDURE actualizarCrearProducto(
										IN	var_idCrearProducto		INT,		-- 1
										IN	var_porcion				DOUBLE,		-- 2
										IN	var_idProducto			INT,		-- 3
										IN	var_idMedida			INT,		-- 4
										IN	var_idMateriaPrima		INT			-- 5
)
BEGIN
	UPDATE	crear_producto
    SET		porcion = var_porcion, idProducto = var_idProducto,
			idMedida = var_idMedida, idMateriaPrima = var_idMateriaPrima
	WHERE	idCrearProducto = var_idCrearProducto;
END $$
DELIMITER ;

-- FIN PROCEDURES CREAR PRODUCTO --

-- INICIO PROCEDURES Venta: INSERTAR, ACTUALIZAR --

DROP PROCEDURE IF EXISTS insertarVenta;
DELIMITER $$
CREATE PROCEDURE insertarVenta(
								IN	var_fechaVenta		VARCHAR(50),		-- 1
								IN	var_total			DOUBLE,				-- 2

								OUT	var_idVenta			INT					-- 3
)
BEGIN
	INSERT INTO venta(fechaVenta, total)
			   VALUES(IFNULL(STR_TO_DATE(var_fechaVenta, '%d/%m/%Y'), CURRENT_TIMESTAMP), var_total);

	SET var_idVenta = LAST_INSERT_ID();
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS actualizarVenta;
DELIMITER $$
CREATE PROCEDURE actualizarVenta(
								IN	var_idVenta			INT,				-- 1
								IN	var_fechaVenta		VARCHAR(50),		-- 2
								IN	var_total			DOUBLE				-- 3
)
BEGIN
	UPDATE	venta
    SET		fechaVenta = STR_TO_DATE(var_fechaVenta, '%d/%m/%Y'), total = var_total
    WHERE	idVenta = var_idVenta;
END $$
DELIMITER ;

-- FIN PROCEDURES Venta --

-- INICIO PROCEDURES Detalle Venta: INSERTAR, ACTUALIZAR --

DROP PROCEDURE IF EXISTS insertarDetalleVenta;
DELIMITER $$
CREATE PROCEDURE insertarDetalleVenta(
										IN	var_cantidad			DOUBLE,		-- 1
										IN	var_subtotal			DOUBLE,		-- 2
										IN	var_idVenta				INT,		-- 3
										IN	var_idProducto			INT,		-- 4
										IN	var_idMedida			INT,		-- 5

										OUT	var_idDetalleVenta		INT			-- 6
)
BEGIN
	INSERT INTO detalle_venta(cantidad, subtotal, idVenta, idProducto, idMedida)
					   VALUES(var_cantidad, var_subtotal, var_idVenta, var_idProducto, var_idMedida);

	SET var_idDetalleVenta = LAST_INSERT_ID();
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS actualizarDetalleVenta;
DELIMITER $$
CREATE PROCEDURE actualizarDetalleVenta(
										IN	var_idDetalleVenta		INT,		-- 1
										IN	var_cantidad			DOUBLE,		-- 2
										IN	var_subtotal			DOUBLE,		-- 3
										IN	var_idVenta				INT,		-- 4
										IN	var_idProducto			INT,		-- 5
										IN	var_idMedida			INT			-- 6
)
BEGIN
	UPDATE	detalle_venta
    SET		cantidad = var_cantidad, subtotal = var_subtotal, idVenta = var_idVenta,
			idProducto = var_idProducto, idMedida = var_idMedida
	WHERE	idDetalleVenta = var_idDetalleVenta;
END $$
DELIMITER ;

-- FIN PROCEDURES Detalle Venta --

-- INICIO PROCEDURES Movimiento: INSERTAR, ACTUALIZAR POR INGRESO Y ACTUALIZAR POR EGRESO --

DROP PROCEDURE IF EXISTS insertarMovimientoIngreso;
DELIMITER $$
CREATE PROCEDURE insertarMovimientoIngreso(
									IN	var_fechaMovimiento		VARCHAR(50),		-- 1
									IN	var_tipoMovimiento		VARCHAR(45),		-- 2
									IN	var_monto				DOUBLE,				-- 3
									IN	var_idVenta				INT,				-- 4

									OUT	var_idMovimiento		INT					-- 5
)
BEGIN
	INSERT INTO movimiento(fechaMovimiento,
											tipoMovimiento, monto, idVenta, idMateriaPrima)
					VALUES(IFNULL(STR_TO_DATE(var_fechaMovimiento, '%d/%m/%Y'), CURRENT_TIMESTAMP),
											var_tipoMovimiento, var_monto, var_idVenta, NULL);

	SET	var_idMovimiento = LAST_INSERT_ID();
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS insertarMovimientoEgreso;
DELIMITER $$
CREATE PROCEDURE insertarMovimientoEgreso(
									IN	var_fechaMovimiento		VARCHAR(50),		-- 1
									IN	var_tipoMovimiento		VARCHAR(45),		-- 2
									IN	var_monto				DOUBLE,				-- 3
									IN	var_idMateriaPrima		INT,				-- 4

									OUT	var_idMovimiento		INT					-- 5
)
BEGIN
	INSERT INTO movimiento(fechaMovimiento,
											tipoMovimiento, monto, idVenta, idMateriaPrima)
					VALUES(IFNULL(STR_TO_DATE(var_fechaMovimiento, '%d/%m/%Y'), CURRENT_TIMESTAMP),
											var_tipoMovimiento, var_monto, NULL, var_idMateriaPrima);

	SET	var_idMovimiento = LAST_INSERT_ID();
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS actualizarMovimientoIngreso;
DELIMITER $$
CREATE PROCEDURE actualizarMovimientoIngreso(
										IN	var_fechaMovimiento		VARCHAR(50),		-- 1
										IN	var_tipoMovimiento		VARCHAR(45),		-- 2
										IN	var_monto				DOUBLE,				-- 3
										IN	var_idVenta				INT					-- 4
)
BEGIN
	UPDATE	movimiento
    SET		fechaMovimiento = STR_TO_DATE(var_fechaMovimiento, '%d/%m/%Y'), tipoMovimiento = var_tipoMovimiento, monto = var_monto
	WHERE	idVenta = var_idVenta;
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS actualizarMovimientoEgreso;
DELIMITER $$
CREATE PROCEDURE actualizarMovimientoEgreso(
										IN	var_fechaMovimiento		VARCHAR(50),		-- 1
										IN	var_tipoMovimiento		VARCHAR(45),		-- 2
										IN	var_monto				DOUBLE,				-- 3
										IN	var_idMateriaPrima		INT					-- 4
)
BEGIN
	UPDATE	movimiento
    SET		fechaMovimiento = STR_TO_DATE(var_fechaMovimiento, '%d/%m/%Y'), tipoMovimiento = var_tipoMovimiento, monto = var_monto
	WHERE	idMateriaPrima = var_idMateriaPrima;
END $$
DELIMITER ;

-- FIN PROCEDURES Movimiento --
