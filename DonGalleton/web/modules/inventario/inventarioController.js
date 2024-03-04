export function inicializar() {
    getAll();
}

export function guardarMateriaPrima() {

    let datos = null;
    let params = null;

    let materiaPrima = new Object();
    materiaPrima.medida = {};

    let idMateria = parseInt(document.getElementById("txtIdMateria").value.trim().length);
    let idMateriaEditar = parseInt(document.getElementById("txtIdMateriaEditar").value.trim().length);
    if (idMateria === 0 && idMateriaEditar === 0) {
        materiaPrima.idMateriaPrima = 0;

        materiaPrima.nombreMateria = document.getElementById("txtNobreMaterial").value;
        materiaPrima.fechaCompra = cambiarFormatoFecha(document.getElementById("txtFechaCompraMaterial").value);
        materiaPrima.fechaVencimiento = cambiarFormatoFecha(document.getElementById("txtFechaVenciMaterial").value);
        materiaPrima.cantidadExistentes = document.getElementById("txtCantidadMaterial").value;
        materiaPrima.precioCompra = document.getElementById("txtPrecioCompra").value;
        materiaPrima.porcentaje = document.getElementById("txtPorcentajeMaterial").value;
        materiaPrima.medida.idMedida = document.getElementById("txtMedidaMaterial").value;

        datos = {
            datosMateriaPrima: JSON.stringify(materiaPrima)
        };

        console.log("datos: " + datos);
        params = new URLSearchParams(datos);
        console.log(params);
        fetch("../../api/mprima/save",
                {method: "POST",

                    headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'},
                    body: params
                })

                .then(response => {
                    return response.json();
                })
                .then(function (data)
                {
                    if (data.exception != null)
                    {
                        Swal.fire('', "Error interno del servidor. Intente nuevamente más tarde", 'warning');

                        return;
                    }
                    if (data.error != null)
                    {
                        Swal.fire('', data.error, 'warning');

                        return;
                    }
                    if (data.errorperm != null)
                    {
                        Swal.fire('', "No tiene permiso para realizar esta operación.", 'warning');

                    }

                    Swal.fire('', 'Materia Prima registrada correctamente', 'success');
                    getAll();
                    clean();
                    //$('#modalMaterialPrima').modal('hide');
                    $('#modalMaterialPrima .btn-secondary').click();

                });
    } else {
        let idMaterial = parseInt(document.getElementById("txtIdMateriaEditar").value);
        materiaPrima.idMateriaPrima = idMaterial;

        materiaPrima.nombreMateria = document.getElementById("txtNobreMaterialEditar").value;
        materiaPrima.fechaCompra = cambiarFormatoFecha(document.getElementById("txtFechaCompraMaterialEditar").value);
        materiaPrima.fechaVencimiento = cambiarFormatoFecha(document.getElementById("txtFechaVenciMaterialEditar").value);
        materiaPrima.cantidadExistentes = document.getElementById("txtCantidadMaterialEditar").value;
        materiaPrima.precioCompra = document.getElementById("txtPrecioCompraEditar").value;
        materiaPrima.porcentaje = document.getElementById("txtPorcentajeMaterialEditar").value;
        materiaPrima.medida.idMedida = document.getElementById("txtMedidaMaterialEditar").value;

        datos = {
            datosMateriaPrima: JSON.stringify(materiaPrima)
        };

        console.log("datos: " + datos);
        params = new URLSearchParams(datos);
        console.log(params);
        fetch("../../api/mprima/save",
                {method: "POST",

                    headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'},
                    body: params
                })

                .then(response => {
                    return response.json();
                })
                .then(function (data)
                {
                    if (data.exception != null)
                    {
                        Swal.fire('', "Error interno del servidor. Intente nuevamente más tarde", 'warning');

                        return;
                    }
                    if (data.error != null)
                    {
                        Swal.fire('', data.error, 'warning');

                        return;
                    }
                    if (data.errorperm != null)
                    {
                        Swal.fire('', "No tiene permiso para realizar esta operación.", 'warning');

                    }

                    Swal.fire('', 'Materia Prima actualizada correctamente', 'success');
                    getAll();
                    clean();
                    //$('#modalMateriaEditar').modal('hide');
                    $('#modalMateriaEditar .btn-secondary').click();
                });
    }
}

export function editarProducto(data) {
    console.log("Editar", JSON.stringify(data));
    $('#modalMateriaEditar').modal('show');
    getProducto(data.idMateriaPrima);

}

function cambiarFormatoFecha(fechaIngresada) {

    var fecha = new Date(fechaIngresada);

    var dia = fecha.getDate();
    var mes = fecha.getMonth() + 1;
    var anio = fecha.getFullYear();

    dia = dia < 10 ? '0' + dia : dia;
    mes = mes < 10 ? '0' + mes : mes;

    return dia + '/' + mes + '/' + anio;
}

function cambiarFormatoFechaTabla(fechaIngresada) {

    var fecha = new Date(fechaIngresada);

    var dia = fecha.getDate();
    var mes = fecha.getMonth() + 1;
    var anio = fecha.getFullYear();

    dia = dia < 10 ? '0' + dia : dia;
    mes = mes < 10 ? '0' + mes : mes;

    return dia + '-' + mes + '-' + anio;
}

export function cargarTabla(data) {
    console.log(data);



    $('#tblMateria').on('click', '.btnEliminar', deleteProducto);
    $('#tblMateria').on('click', '.btnEditar', function () {
        // Obtener el índice de la fila seleccionada
        let rowIndex = $(this).closest('tr').index();

        // Obtener los datos del producto específico
        let materia = data[rowIndex];

        // Llamar a la función editarProducto con los datos del producto seleccionado
        editarProducto(materia);
    });
    
//    $(document).ready(function() {
//    // Initialize DataTable
//    $('#tblMateria').DataTable();
//
//    // Check if DataTable exists
//    if ($.fn.DataTable.isDataTable('#tblMateria')) {
//        // Destroy DataTable and empty tbody
//        $('#tblMateria').DataTable().destroy();
//        $('#tblMateria tbody').empty();
//    }
//});
    
    // Destruye la tabla
    if ($.fn.DataTable.isDataTable('#tblMateria')) {
        $('#tblMateria').DataTable().destroy();
        $('#tblMateria tbody').empty();
    }

    // generar tabla dinamica
    for (let i = 0; i < data.length; i++) {
        const fechaCompraSubstring = cambiarFormatoFechaTabla(data[i]['fechaCompra'].substring(0, 10));
        const fechaVencimientoSubstring = cambiarFormatoFechaTabla(data[i]['fechaVencimiento'].substring(0, 10));

        var precioCompraConSigno = data[i]['precioCompra'] + ' MXN ';
        var porcentajeConSigno = data[i]['porcentaje'] + ' % ';



        let fila = `<tr>
        <td>${data[i]['nombreMateria']}</td>
        <td>${data[i]['cantidadExistentes']}</td>
        <td>${data[i]['medida']['tipoMedida']}</td>
        <td>${precioCompraConSigno}</td>
        <td>${fechaCompraSubstring}</td>
        <td>${fechaVencimientoSubstring}</td>
        <td>${porcentajeConSigno}</td>
        <td><button data-idmateriaprima="${data[i]['idMateriaPrima']}" class="btnEliminar"><i class="fa-solid fa-trash" style="color: #c12525;"></i></button></td>
        <td><button data-idmateriaprima="${data[i]['idMateriaPrima']}" id="btnEditar" class="btnEditar"><i class="fa-solid fa-pen-to-square" style="color: #57351f;"></i></button></td>
    </tr>`;

        $('#tblMateria tbody').append(fila);
    }



    $('#tblMateria').DataTable({
        dom: "<'row' <'col-sm-6'l><'col-sm-5'f>>  <'row' <'col-sm-12'tr>>  <'row' <'col-4'i><'col'p>>",
        initComplete: function () {
            $('.dataTables_filter').addClass('text-end');
        },
        language: {
            decimal: "",
            emptyTable: "No hay información",
            info: "Mostrando _START_ a _END_ de _TOTAL_ Entradas",
            infoEmpty: "Mostrando 0 Entradas",
            infoFiltered: "",
            infoPostFix: "",
            thousands: ",",
            lengthMenu: "Mostrar   _MENU_  Entradas",
            loadingRecords: "Cargando...",
            processing: "Procesando...",
            search: " ",
            searchPlaceholder: "Buscar",
            zeroRecords: "Sin resultados encontrados",
            paginate: {
                first: "Primero",
                last: "Ultimo",
                next: "Siguiente",
                previous: "Anterior",
            }
        },

        "ordering": false,
        retrieve: true
    });


    $('#agregarMaterialPrima').on('click', 'button', function () {
        $('#modalMaterialPrima').modal('show');
    });


}
;

export function deleteProducto() {
    Swal.fire({
        title: "¿Estás seguro?",
        text: "La materia prima se eliminará",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Sí, eliminarlo"
    }).then((result) => {
        if (result.isConfirmed) {
            const idMateria = $(this).data('idmateriaprima');
            console.log(idMateria);
            let datos = null;
            let params = null;

            let materiaPrima = new Object();
            materiaPrima.idMateriaPrima = idMateria;
            datos = {
                datosMateriaPrima: JSON.stringify(materiaPrima)
            };

            params = new URLSearchParams(datos);

            fetch("../../api/mprima/delete",
                    {method: "POST",
                        headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'},
                        body: params
                    })
                    .then(response => {
                        return response.json();
                    })
                    .then(function (data)
                    {
                        if (data.exception != null)
                        {
                            Swal.fire('', "Error interno del servidor. Intente nuevamente más tarde", 'warning');
                            return;
                        }
                        if (data.error != null)
                        {
                            alert(data.error);
                            return;
                        }
                        if (data.errorperm != null)
                        {
                            Swal.fire('', "No tiene permiso para realizar esta operación.", 'warning');
                        }
                    });

            Swal.fire({
                title: "¡Eliminado!",
                text: "Tu materia prima ha sido eliminada.",
                icon: "success"
            });
            getAll();
        }
    });
}

function getAll() {
    let url = "../../api/mprima/getAll";
    fetch(url)
            .then(response => {
                return response.json();
            })
            .then(function (data) {
                console.log(data);
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

function getProducto(idMateriaPrima) {
    console.log(idMateriaPrima);
    let materiaPrima = new Object();
    let datos = null;
    let params = null;
    materiaPrima.idMateriaPrima = idMateriaPrima;

    datos = {
        datoMateriaPrima: JSON.stringify(materiaPrima)
    };
    console.log(datos);
    params = new URLSearchParams(datos);
    fetch("../../api/mprima/getProducto",
            {method: "POST",
                headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'},
                body: params
            })

            .then(response => {
                return response.json();
            })
            .then(function (data) {
                if (data.exception != null)
                {
                    Swal.fire('', "Error interno del servidor. Intente nuevamente más tarde", 'warning');

                    return;
                }
                if (data.error != null)
                {
                    Swal.fire('', data.error, 'warning');

                    return;
                }
                if (data.errorperm != null)
                {
                    Swal.fire('', "No tiene permiso para realizar esta operación.", 'warning');

                }

                var fechaCompra = new Date(data['fechaCompra']);
                var fechaCompraVencimiento = new Date(data['fechaVencimiento']);

                var fechaFormateada = fechaCompra.toISOString().split('T')[0];
                var fechaVencimientoFormateada = fechaCompraVencimiento.toISOString().split('T')[0];


                console.log(data);
                document.getElementById("txtNobreMaterialEditar").value = data['nombreMateria'];
                document.getElementById("txtFechaCompraMaterialEditar").value = fechaFormateada;
                document.getElementById("txtFechaVenciMaterialEditar").value = fechaVencimientoFormateada;
                document.getElementById("txtCantidadMaterialEditar").value = data['cantidadExistentes'];
                document.getElementById("txtPrecioCompraEditar").value = data['precioCompra'];
                document.getElementById("txtPorcentajeMaterialEditar").value = data['porcentaje'];
                document.getElementById("txtIdMateriaEditar").value = data['idMateriaPrima'];

                var medida = data['medida'];
                var tipoMedida = medida['tipoMedida'];

                var selectMedida = document.getElementById("txtMedidaMaterialEditar");

                for (var i = 0; i < selectMedida.options.length; i++) {
                    if (selectMedida.options[i].text === tipoMedida) {
                        selectMedida.options[i].selected = true;
                        break;
                    }
                }

            });
}

export function clean() {

    document.getElementById("txtNobreMaterial").value = "";
    document.getElementById("txtFechaCompraMaterial").value = "";
    document.getElementById("txtFechaVenciMaterial").value = "";
    document.getElementById("txtCantidadMaterial").value = "";
    document.getElementById("txtPrecioCompra").value = "";
    document.getElementById("txtPorcentajeMaterial").value = "";
    document.getElementById("txtMedidaMaterial").value = "";
    document.getElementById("txtIdMateria").value = "";

    document.getElementById("txtNobreMaterialEditar").value = "";
    document.getElementById("txtFechaCompraMaterialEditar").value = "";
    document.getElementById("txtFechaVenciMaterialEditar").value = "";
    document.getElementById("txtCantidadMaterialEditar").value = "";
    document.getElementById("txtPrecioCompraEditar").value = "";
    document.getElementById("txtPorcentajeMaterialEditar").value = "";
    document.getElementById("txtMedidaMaterialEditar").value = "";
    document.getElementById("txtIdMateriaEditar").value = "";

}