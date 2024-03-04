export function inicializar() {
    getAll();
}
let datosFilas = [];

const imagenesSeleccionadas = new Set();


// Toda la funcionalidad del punto de venta
function getAll() {
    let url = "../../api/producto/getAll";
    fetch(url)
            .then(response => {
                return response.json();
            })
            .then(function (data) {
                cargarTabla(data);
                if (data.exception != null) {
                    Swal.fire("", "Error interno del servidor. Intente nuevamente más tarde.", "error");
                    return;
                }

                if (data.error != null) {
                    Swal.fire("", data.error, "warning");
                    return;
                }

                if (data.errorsec != null) {
                    Swal.fire("", data.errorsec, "error");
                }
            });
}

function cargarTabla(data) {
    if (data && data.length > 0) {
        const contenedor = document.getElementById('contenedorGalletas');
        contenedor.innerHTML = ''; // Vacía el contenedor antes de agregar nuevos elementos

        data.forEach(producto => {
            const columna = document.createElement('div');
            columna.classList.add('col-md-3', 'mt-2');

            const fila = document.createElement('div');
            fila.classList.add('row');

            const divImagen = document.createElement('div');
            divImagen.classList.add('col-md-6');

            const imagen = mostrarImg(producto.fotografia); // Llama a mostrarImg y obtén el elemento <img>

            divImagen.appendChild(imagen);

            const divCantidad = document.createElement('div');
            divCantidad.classList.add('col-md-6');

            const labelCantidad = document.createElement('label');
            labelCantidad.setAttribute('for', 'txtCantGall');
            labelCantidad.textContent = 'Cantidad';
            columna.setAttribute('data-id', producto.idProducto);

            const inputCantidad = document.createElement('input');
            inputCantidad.setAttribute('type', 'text');
            inputCantidad.setAttribute('id', 'txtCantGall');
            inputCantidad.setAttribute('value', producto.cantidadExistentes); // Supongamos que tu objeto producto tiene una propiedad 'cantidad'
            inputCantidad.setAttribute('readonly', 'readonly');

            divCantidad.appendChild(labelCantidad);
            divCantidad.appendChild(inputCantidad);

            fila.appendChild(divImagen);
            fila.appendChild(divCantidad);
            columna.appendChild(fila);
            contenedor.appendChild(columna);

            const selectMedida = document.getElementById('txtMedida');

            // Añadir evento 'click' a la imagen generada
            imagen.addEventListener('click', function () {
                if (!imagenesSeleccionadas.has(producto.nombreProducto)) {
                    if (producto.cantidadExistentes === 0) {
                        Swal.fire('', 'No hay producto suficientes', 'warning');
                        // Deshabilitar la interacción con el producto y aplicar estilo visual indicando su estado

                    } else {
                        mostrarNombreYPrecio(producto.nombreProducto, producto.precioVenta, producto.idProducto);
                        imagenesSeleccionadas.add(producto.nombreProducto);

                        // Resaltar la fila seleccionada
                        const filas = document.querySelectorAll('.fila-producto');
                        filas.forEach(fila => fila.classList.remove('fila-seleccionada'));
                        columna.classList.add('fila-seleccionada');
                    }
                }
            });
        });
    }
}



function mostrarNombreYPrecio(nombre, precio, idProducto) {
    const tabla = document.getElementById('tblProductos').getElementsByTagName('tbody')[0];
    const newRow = tabla.insertRow();

    // Insertar celdas con nombre, medida, precio y total
    const cellNombre = newRow.insertCell();
    const cellPiezas = newRow.insertCell();
    const cellCaja = newRow.insertCell();
    const cellGramos = newRow.insertCell();
    const cellDinero = newRow.insertCell();
    const cellPrecio = newRow.insertCell();
    const cellTotal = newRow.insertCell();
    const cellBtn = newRow.insertCell();

    cellNombre.textContent = nombre;
    cellPrecio.textContent = precio;

    const inputPiezas = document.createElement('input');
    inputPiezas.type = 'number';
    inputPiezas.classList.add('input-medida'); // Agrega una clase al input para identificarlo
    inputPiezas.setAttribute('data-id-producto', idProducto); // Agregar el atributo data-id-producto con el valor de idProducto
    inputPiezas.id = 'inputPiezas';
    cellPiezas.appendChild(inputPiezas);

    const inputCaja = document.createElement('input');
    inputCaja.classList.add('input-medida');
    inputCaja.type = 'number';
    inputCaja.id = 'inputCaja'; // Puedes añadir un ID para identificar este input
    cellCaja.appendChild(inputCaja);

    const inputGramos = document.createElement('input');
    inputGramos.classList.add('input-medida');
    inputGramos.type = 'number';
    inputGramos.id = 'inputGramos'; // Puedes añadir un ID para identificar este input
    cellGramos.appendChild(inputGramos);

    const inputDinero = document.createElement('input');
    inputDinero.classList.add('input-medida');
    inputDinero.type = 'number';
    inputDinero.id = 'inputDinero'; // Puedes añadir un ID para identificar este input
    cellDinero.appendChild(inputDinero);

    const botonEliminar = document.createElement('button');
    botonEliminar.classList.add('btnEliminar');
    botonEliminar.innerHTML = '<i class="fa-solid fa-trash" style="color: #c20003;"></i>';

    // Agregar evento 'click' al botón para eliminar la fila
    botonEliminar.addEventListener('click', function () {
        const fila = this.parentNode.parentNode; // Obtener la fila que contiene el botón
        const nombreProducto = fila.cells[0].textContent; // Obtener el nombre del producto en la primera celda

        // Eliminar el nombre del producto del conjunto de imágenes seleccionadas
        imagenesSeleccionadas.delete(nombreProducto);

        // Eliminar la fila de la tabla
        fila.parentNode.removeChild(fila);

        // Recalcular y mostrar el total actualizado
        mostrarTotal();
    });

    cellBtn.appendChild(botonEliminar);

    // Agregar evento 'change' al input para capturar la medida ingresada
    inputPiezas.addEventListener('change', function (event) {
        actualizarFila(event);
    });
    inputCaja.addEventListener('change', function (event) {
        actualizarFila(event);
    });

    inputGramos.addEventListener('change', function (event) {
        actualizarFila(event);
    });

    inputDinero.addEventListener('change', function (event) {
        actualizarFila(event);
    });
    // No recorras las filas y agregues al arreglo aquí, sino en otro lugar donde sea apropiado.
}
let totalPiezas = 0;
// Luego, en otro lugar apropiado de tu código, recorre las filas y agrega los valores al arreglo
function actualizarFila(event) {
    const tabla = document.getElementById('tblProductos');
    const filas = tabla.getElementsByTagName('tbody')[0].getElementsByTagName('tr');

    for (let i = 0; i < filas.length; i++) {
        const fila = filas[i];

        // Obtener valores de los inputs de la fila actual
        const valorPieza = parseFloat(fila.querySelector('#inputPiezas').value) || 0;
        const valorCaja = parseFloat(fila.querySelector('#inputCaja').value) || 0;
        const valorGramos = parseFloat(fila.querySelector('#inputGramos').value) || 0;
        const valorDinero = parseFloat(fila.querySelector('#inputDinero').value) || 0;

        // Convertir todas las medidas a piezas
        const piezasDesdeCaja = valorCaja * 40; // Reemplaza factorConversionCajaAPiezas por el valor de conversión
        const piezasDesdeGramos = Math.floor(valorGramos / 25); // Redondear hacia abajo para obtener un número entero
        const precioVentaPorPieza = parseFloat(fila.getElementsByTagName('td')[5].textContent) || 0;
        const piezasDesdeDinero = Math.floor(valorDinero / precioVentaPorPieza); // Redondear hacia abajo para obtener un número entero

        // Sumar todas las cantidades convertidas a piezas
        const totalEnPiezas = valorPieza + piezasDesdeCaja + piezasDesdeGramos + piezasDesdeDinero;
        const total = totalEnPiezas * precioVentaPorPieza;

        // Actualizar los datos en el array datosFilas
        datosFilas[i] = {
            cantidad: {
                piezas: totalEnPiezas // No se necesitan decimales para piezas
            },
            subtotal: total.toFixed(2),
            id: fila.querySelector('.input-medida').getAttribute('data-id-producto')
                    // ... otros valores actualizados
        };

        // Actualizar el subtotal en la tabla
        const celdaSubtotal = fila.getElementsByTagName('td')[6]; // Índice 6 para la columna "Subtotal"
        celdaSubtotal.textContent = total.toFixed(2);
    }

    // Mostrar los datos y llamar a las funciones necesarias después de la actualización
    console.log('datosFilas:', datosFilas);
    calcularTotal();
    mostrarTotal();
}






function calcularTotalPiezas() {
    const tabla = document.getElementById('tblProductos');
    const filas = tabla.getElementsByTagName('tbody')[0].getElementsByTagName('tr');

    // Reiniciar totalPiezas antes de calcular
    totalPiezas = 0;

    for (let i = 0; i < filas.length; i++) {
        const fila = filas[i];
        const valorPiezas = parseFloat(fila.querySelector('#inputPiezas').value) || 0;
        const valorCaja = parseFloat(fila.querySelector('#inputCaja').value) || 0;
        const valorGramos = parseFloat(fila.querySelector('#inputGramos').value) || 0;
        const valorDinero = parseFloat(fila.querySelector('#inputDinero').value) || 0;

        totalPiezas += valorPiezas + valorCaja + valorGramos + valorDinero;
    }

    console.log('Total de piezas:', totalPiezas);

    // Devolver totalPiezas al final de la función
    return totalPiezas;
}


export function mostrarImg(base64data) {
    const imagen = document.createElement('img');
    imagen.classList.add('img-thumbnail', 'imagen-pequena');
    imagen.src = base64data;
    return imagen;
}
function calcularTotal() {
    const tabla = document.getElementById('tblProductos').getElementsByTagName('tbody')[0];
    const filas = tabla.getElementsByTagName('tr');
    let sumaTotal = 0;

    for (let i = 0; i < filas.length; i++) {
        const celdaTotal = filas[i].getElementsByTagName('td')[6]; // Índice 3 para la columna "Total"
        const valorTotal = parseFloat(celdaTotal.textContent);

        if (!isNaN(valorTotal)) {
            sumaTotal += valorTotal;
        }
    }

    return sumaTotal.toFixed(2);
}

// Función para mostrar el total actualizado debajo de la tabla
function mostrarTotal() {
    const totalInput = document.getElementById('totalInput');
    totalInput.value = calcularTotal(); // Actualiza el valor del input con el total calculado
}



export function guardarVenta() {
    let datos = null;
    let params = null;

    const totalVenta = calcularTotal();

    var fecha = new Date();
    var dia = fecha.getDate();
    var mes = fecha.getMonth() + 1;
    var anio = fecha.getFullYear();

    // Asegurar que el día y el mes tengan dos dígitos
    var diaFormateado = dia < 10 ? '0' + dia : dia;
    var mesFormateado = mes < 10 ? '0' + mes : mes;

    var fechaFormateada = diaFormateado + '/' + mesFormateado + '/' + anio;

    // Crear objeto con los datos de la venta principal
    let venta = {
        total: calcularTotal(),
        fechaVenta: fechaFormateada
    };

    let datosVenta = {
        datosVenta: JSON.stringify(venta)
    };

    let paramsVenta = new URLSearchParams(datosVenta);

    // Enviar la solicitud para guardar la venta principal
    fetch("../../api/venta/save", {
        method: "POST",
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
        },
        body: paramsVenta
    })
            .then(response => {
                return response.json();
            })
            .then(function (data) {
                // Verificar el éxito de la operación y proceder con el detalle de venta
                if (data.idVenta) {
                    console.log(data);
                    // Crear objeto con los datos del detalle de venta
                    console.log("DatosFilas" + datosFilas);

                    let detalleVentaExitoso = true; // Bandera para controlar si el detalle de venta se guardó exitosamente

                    for (var i = 0; i < datosFilas.length; i++) {
                        console.log("DatosFilasIn" + datosFilas[i]);
                        let detalleVenta = {
                            cantidad: parseInt(datosFilas[i].cantidad.piezas),
                            subtotal: parseFloat(datosFilas[i]['subtotal']),
                            venta: {
                                idVenta: data.idVenta
                            },
                            producto: {
                                idProducto: parseInt(datosFilas[i]['id'])
                            },
                            medida: {
                                idMedida: 1
                            }
                            // Otras propiedades del detalle de venta...
                        };

                        let datosDetalleVenta = {
                            datosDetalleVenta: JSON.stringify(detalleVenta)
                        };

                        let paramsDetalleVenta = new URLSearchParams(datosDetalleVenta);

                        // Enviar la solicitud para guardar el detalle de venta
                        fetch("../../api/detalleventa/save", {
                            method: "POST",
                            headers: {
                                'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
                            },
                            body: paramsDetalleVenta
                        })
                                .then(response => {
                                    return response.json();
                                })
                                .then(function (dataDetalle) {
                                    if (!dataDetalle.idDetalleVenta) {
                                        detalleVentaExitoso = false; // Si falla el detalle de venta, cambia la bandera a false
                                        if (dataDetalle.exception === "java.lang.Exception: No hay suficientes existencias para la venta.") {
                                            // Mostrar un mensaje al usuario indicando que no hay suficientes existencias para la venta
                                            Swal.fire('', 'No hay suficientes existencias para completar la venta.', 'warning');
                                        } else {
                                            // Si la excepción no es por falta de existencias, mostrar un mensaje genérico de error
                                            Swal.fire('', 'Error al realizar la venta', 'error');
                                        }
                                    } else {
                                        console.log(dataDetalle);
                                        Swal.fire('', 'Compra realizada correctamente', 'success');
                                        limpiarTabla();
                                        getAll();
                                    }
                                });
                    }

                    // Verifica la bandera detalleVentaExitoso antes de guardar la venta principal
                    if (!detalleVentaExitoso) {
                        console.log("No se puede guardar la venta principal porque el detalle de venta falló");
                        return;
                    }

                    // Código para guardar la venta principal
                    // ... (código para guardar la venta principal)

                } else {
                    // Manejar el caso en que la venta principal no se haya guardado correctamente
                    console.log("Error al guardar la venta principal");
                }
            });
}


export function limpiarTabla() {
    const tabla = document.getElementById('tblProductos').getElementsByTagName('tbody')[0];
    tabla.innerHTML = ''; // Vacía el contenido del cuerpo de la tabla
    mostrarTotal(); // Actualiza el total después de limpiar la tabla
}