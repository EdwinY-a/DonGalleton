-- ---------------------------------------------------------------------------- --
-- Archivo: 04_DatosBase_DonGalleto.sql                      					--
-- Version: 1.0                                                     			--
-- Autor:   Francisco Javier Rocha Aldana   									--
-- Email:   rochaaldanafcojavier@gmail.com / 21000459@alumnos.utleon.edu.mx		--
-- Fecha de elaboracion: 19-11-2023                                 			--
-- ---------------------------------------------------------------------------- --

USE don_galleto;

-- INICIO PRUEBA DE PROCEDURES Y VISTAS E INSERTAR DATOS BASE EN TABLA USUARIO --

CALL insertarUsuario("Alda", MD5("Alda1234"), @out1);
CALL insertarUsuario("Jorge", MD5("Jorge1234"), @out1);
CALL insertarUsuario("Edwin", MD5("Edwin1234"), @out1);
CALL insertarUsuario("Diego", MD5("Diego1234"), @out1);
CALL insertarUsuario("dongalleto", MD5("dongalleto"), @out1);

SELECT * FROM usuario;

CALL actualizarUsuario(5, "DonGalleto", MD5("DonGalleto"));

SELECT * FROM v_usuario;

CALL desActivarUsuario(1);

SELECT * FROM usuario;

CALL activarUsuario(1);

SELECT * FROM v_usuario;

-- FIN DE PRUEBAS EN TABLA USUARIO --

-- INICIO PRUEBA DE PROCEDURES Y VISTAS E INSERTAR DATOS BASE EN TABLA MEDIDA --

CALL insertarMedida("PZ", @out1);
CALL insertarMedida("G", @out1);
CALL insertarMedida("KG", @out1);
CALL insertarMedida("LB", @out1);
CALL insertarMedida("OZ", @out1);
CALL insertarMedida("ML", @out1);
CALL insertarMedida("L", @out1);
CALL insertarMedida("GAL", @out1);
CALL insertarMedida("CU", @out1);

SELECT * FROM medida;

CALL actualizarMedida(9, "CUP");

SELECT * FROM v_medida;

-- FIN DE PRUEBAS EN TABLA MEDIDA --

-- INICIO PRUEBA DE PROCEDURES Y VISTAS E INSERTAR DATOS BASE EN TABLA MATERIA PRIMA --

CALL insertarMateriaPrima("Harina", "20/10/2023", "30/10/2023", 1.5, 20.50, 100, 3, @out1);
CALL insertarMateriaPrima("Azucar", "19/11/2023", "29/10/2023", 2, 10, 100, 3, @out1);
CALL insertarMateriaPrima("Mantequilla", "20/10/2023", "18/11/2023", 5, 15, 100, 1, @out1);
CALL insertarMateriaPrima("Huevo", "15/11/2023", "25/11/2023", 10, 30.50, 100, 1, @out1);
CALL insertarMateriaPrima("Chocolate en polvo", "05/09/2023", "30/11/2023", 3, 5.50, 100, 3, @out1);
CALL insertarMateriaPrima("Bicarbonato de sodio", "01/10/2023", "15/11/2023", 3, 5.50, 100, 3, @out1);
CALL insertarMateriaPrima("Sal", "10/11/2023", "10/11/2024", 10, 105.50, 100, 3, @out1);

SELECT * FROM materia_prima;

CALL actualizarMateriaPrima(3, "Mantequilla", "20/10/2023", "18/11/2023", 5, 15, 0, 1);

SELECT * FROM v_materia_prima;

CALL desActivarMateriaPrima(3);

SELECT * FROM materia_prima;

CALL activarMateriaPrima(3, 50);

SELECT * FROM v_materia_prima;

CALL desActivarMateriaPrimaPorFechaVencimiento("19/11/2023");

SELECT * FROM materia_prima;

-- FIN DE PRUEBAS EN TABLA MATERIA PRIMA --

-- INICIO PRUEBA DE PROCEDURES Y VISTAS E INSERTAR DATOS BASE EN TABLA PRODUCTO --

CALL insertarProducto("Chocolate", 100, 10.50, 5, 1, "", @out1);
CALL insertarProducto("Vainilla", 150, 15.50, 8, 1, "", @out1);
CALL insertarProducto("Chispas de choc", 200, 20, 10, 1, "", @out1);

SELECT * FROM producto;

CALL actualizarProducto(3, "Chispas de chocolate", 200, 20, 10, 1, "");

SELECT * FROM v_producto;

-- FIN DE PRUEBAS EN TABLA PRODUCTO --

-- INICIO PRUEBA DE PROCEDURES Y VISTAS E INSERTAR DATOS BASE EN TABLA CREAR PRODUCTO --

CALL insertarCrearProducto(100,	1, 2, 1, @out1);
CALL insertarCrearProducto(50, 1, 2, 2, @out1);
CALL insertarCrearProducto(1, 1, 1, 3, @out1);
CALL insertarCrearProducto(3, 1, 1, 4, @out1);
CALL insertarCrearProducto(50, 1, 2, 5, @out1);
CALL insertarCrearProducto(10, 1, 2, 6, @out1);
CALL insertarCrearProducto(5, 1, 1, 7, @out1);

SELECT * FROM crear_producto;

CALL actualizarCrearProducto(7, 5, 1, 2, 7);

SELECT * FROM v_crear_producto;

-- FIN DE PRUEBAS EN TABLA CREAR PRODUCTO --

-- INICIO PRUEBA DE PROCEDURES Y VISTAS E INSERTAR DATOS BASE EN TABLA VENTA --

CALL insertarVenta("14/11/2023", 500, @out1);

SELECT * FROM venta;

CALL actualizarVenta(1, "14/11/2023", 46);

SELECT * FROM v_venta;

-- FIN DE PRUEBAS EN TABLA VENTA --

-- INICIO PRUEBA DE PROCEDURES Y VISTAS E INSERTAR DATOS BASE EN TABLA DETALLE VENTA --

CALL insertarDetalleVenta(1, 10.50, 1, 1, 1, @out1);
CALL insertarDetalleVenta(1, 15.50, 1, 2, 1, @out1);
CALL insertarDetalleVenta(1, 200.00, 1, 3, 1, @out1);

SELECT * FROM detalle_venta;

CALL actualizarDetalleVenta(3, 1, 20.00, 1, 3, 1);

SELECT * FROM v_detalle_venta;

-- FIN DE PRUEBAS EN TABLA DETALLE VENTA --

-- INICIO PRUEBA DE PROCEDURES Y VISTAS E INSERTAR DATOS BASE EN TABLA MOVIMIENTO --

CALL insertarMovimientoIngreso("14/11/2023", "Ingreso", 500, 1, @out1);
CALL insertarMovimientoEgreso("20/10/2023", "Egreso", 500, 1, @out1);
CALL insertarMovimientoIngreso("01/10/2023", "Ingreso", 500, 1, @out1);

SELECT * FROM movimiento;

CALL actualizarMovimientoIngreso("14/11/2023", "Ingreso", 46, 1);

SELECT * FROM v_movimiento;

CALL actualizarMovimientoEgreso("20/10/2023", "Egreso", 5.50, 1);

SELECT * FROM movimiento;

SELECT nombreMateria AS mas FROM (SELECT nombreMateria, SUM(cantidadExistentes) AS cantidad FROM materia_prima GROUP BY nombreMateria) subconsulta ORDER BY cantidad DESC LIMIT 1;

SELECT * FROM materia_prima;

SELECT nombreProducto AS mas FROM producto p INNER JOIN detalle_venta dv ON p.idProducto = dv.idProducto GROUP BY nombreProducto ORDER BY COUNT(*) DESC LIMIT 1;

-- FIN DE PRUEBAS EN TABLA DETALLE MOVIMIENTO --