-- ---------------------------------------------------------------------------- --
-- Archivo: 03_Vistas_DonGalleto.sql               		       					--
-- Version: 1.0                                                     			--
-- Autor:   Francisco Javier Rocha Aldana   									--
-- Email:   rochaaldanafcojavier@gmail.com / 21000459@alumnos.utleon.edu.mx		--
-- Fecha de elaboracion: 19-11-2023                                 			--
-- ---------------------------------------------------------------------------- --

USE don_galleto;

-- INICIO VISTA USUARIOS --

DROP VIEW IF EXISTS v_usuario;
CREATE VIEW v_usuario AS
	SELECT	 u.idUsuario		-- 1
			,u.nombreUsuario	-- 2
            ,u.contrasenia		-- 3
            ,u.rol				-- 4
            ,u.estatus,			-- 5
            u.lastToken,
	    u.telefono,
            DATE_FORMAT(u.dateLastToken, '%d/%m/%Y %H:%i:%S') AS dateLastToken
    FROM	usuario u;
select * from v_usuario;
-- FIN VISTA USUARIOS --

-- INICIO VISTA MEDIDAS --

DROP VIEW IF EXISTS v_medida;
CREATE VIEW v_medida AS
	SELECT	 m.idMedida			-- 1
			,m.tipoMedida		-- 2
    FROM	medida m;

-- FIN VISTA MEDIDAS --

-- INICIO VISTA MATERIA PRIMA --

DROP VIEW IF EXISTS v_materia_prima;
CREATE VIEW v_materia_prima AS
	SELECT	 	 mp.idMateriaPrima											-- 1
				,mp.nombreMateria											-- 2
                ,CONVERT(mp.fechaCompra, CHAR) AS fechaCompra				-- 3
                ,CONVERT(mp.fechaVencimiento, CHAR) AS fechaVencimiento		-- 4
                ,mp.estatus													-- 5
                ,mp.cantidadExistentes										-- 6
                ,mp.precioCompra											-- 7
                ,mp.porcentaje												-- 8
                ,mp.idMedida												-- 9

				,m.tipoMedida												-- 10
    FROM		materia_prima mp
    INNER JOIN	medida m
    ON			mp.idMedida = m.idMedida;

-- FIN VISTA MATERIA PRIMA --

-- INICIO VISTA PRODUCTO --

DROP VIEW IF EXISTS v_producto;
CREATE VIEW v_producto AS
	SELECT		 p.idProducto				-- 1
				,p.nombreProducto			-- 2
                ,p.cantidadExistentes		-- 3
                ,p.precioVenta				-- 4
                ,p.precioProduccion			-- 5
                ,p.idMedida					-- 6
                ,p.fotografia				-- 7

				,m.tipoMedida				-- 8
    FROM		producto p
    INNER JOIN	medida m
    ON			p.idMedida = m.idMedida;

-- FIN VISTA PRODUCTO --

-- INICIO VISTA CREAR PRODUCTO --

DROP VIEW IF EXISTS v_crear_producto;
CREATE VIEW v_crear_producto AS
	SELECT		 cp.idCrearProducto												-- 1
				,cp.porcion														-- 2
                ,cp.idProducto													-- 3
                ,cp.idMedida													-- 4
                ,cp.idMateriaPrima												-- 5

				,p.nombreProducto												-- 6
                ,p.cantidadExistentes AS cantidadExistentesProductos			-- 7
                ,p.precioVenta													-- 8
                ,p.precioProduccion												-- 9
                ,p.idMedida AS idMedidaProducto									-- 10
                ,p.fotografia													-- 11

				,mp.tipoMedida AS tipoMedidaProducto							-- 12

				,m.tipoMedida AS tipoMedidaCrearProducto						-- 13

				,matpri.nombreMateria											-- 14
                ,CONVERT(matpri.fechaCompra, CHAR) AS fechaCompra				-- 15
                ,CONVERT(matpri.fechaVencimiento, CHAR) AS fechaVencimiento		-- 16
                ,matpri.estatus													-- 17
                ,matpri.cantidadExistentes AS cantidadExistentesMateriaPrima	-- 18
                ,matpri.precioCompra											-- 19
                ,matpri.porcentaje												-- 20
                ,matpri.idMedida AS idMedidaMateriaPrima						-- 21

				,mmatpri.tipoMedida AS tipoMedidaMateriaPrima					-- 22
    FROM		crear_producto cp
    INNER JOIN	producto p
    ON			cp.idProducto = p.idProducto
    INNER JOIN	medida mp
    ON			p.idMedida = mp.idMedida
    INNER JOIN	medida m
    ON			cp.idMedida = m.idMedida
    INNER JOIN	materia_prima matpri
    ON			cp.idMateriaPrima = matpri.idMateriaPrima
    INNER JOIN	medida mmatpri
    ON			matpri.idMedida = mmatpri.idMedida;

-- FIN VISTA PRODUCTO --

-- INICIO VISTA VENTA --

DROP VIEW IF EXISTS v_venta;
CREATE VIEW v_venta AS
	SELECT	 v.idVenta										-- 1
			,CONVERT(v.fechaVenta, CHAR) AS fechaVenta		-- 2
            ,v.total										-- 3
    FROM	venta v;

-- FIN VISTA VENTA --

-- INICIO VISTA DETALLE VENTA --

DROP VIEW IF EXISTS v_detalle_venta;
CREATE VIEW v_detalle_venta AS
	SELECT		 dv.idDetalleVenta								-- 1
				,dv.cantidad									-- 2
                ,dv.subtotal									-- 3
                ,dv.idVenta										-- 4
                ,dv.idProducto									-- 5
                ,dv.idMedida									-- 6

				,CONVERT(v.fechaVenta, CHAR) AS fechaVenta		-- 7
                ,v.total										-- 8

				,p.nombreProducto								-- 9
                ,p.cantidadExistentes							-- 10
                ,p.precioVenta									-- 11
                ,p.precioProduccion								-- 12
                ,p.idMedida AS idMedidaProducto					-- 13
                ,p.fotografia									-- 14

				,mp.tipoMedida AS tipoMedidaProducto			-- 15

				,m.tipoMedida AS tipoMedidaDetalleVenta			-- 16
    FROM		detalle_venta dv
    INNER JOIN	venta v
    ON			dv.idVenta = v.idVenta
    INNER JOIN	producto p
    ON			dv.idProducto = p.idProducto
    INNER JOIN	medida mp
    ON			p.idMedida = mp.idMedida
    INNER JOIN	medida m
    ON			dv.idMedida = m.idMedida;
    
-- FIN VISTA DETALLE VENTA --

-- INICIO VISTA MOVIMIENTO --
    
DROP VIEW IF EXISTS v_movimiento;
CREATE VIEW v_movimiento AS
	SELECT		 m.idMovimiento
				,CONVERT(m.fechaMovimiento, CHAR) AS fechaMovimiento
                ,m.tipoMovimiento
                ,m.monto
                ,m.idVenta
                ,m.idMateriaPrima

				,CONVERT(v.fechaVenta, CHAR) AS fechaVenta
                ,v.total

				,mp.nombreMateria
                ,CONVERT(mp.fechaCompra, CHAR) AS fechaCompra
                ,CONVERT(mp.fechaVencimiento, CHAR) AS fechaVencimiento
                ,mp.estatus
                ,mp.cantidadExistentes
                ,mp.precioCompra
                ,mp.porcentaje
                ,mp.idMedida

				,mmp.tipoMedida
    FROM		movimiento m
    LEFT JOIN	venta v
    ON			m.idVenta = v.idVenta
    LEFT JOIN	materia_prima mp
    ON			m.idMateriaPrima = mp.idMateriaPrima
    LEFT JOIN	medida mmp
    ON			mp.idMedida = mmp.idMedida;

-- FIN VISTA MOVIMIENTO --
