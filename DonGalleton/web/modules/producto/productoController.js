let nombre;
let cantidadExistentes;
let totalCantidadExis;
let precioVenta;
let precioProduccion;
let idMedida = 1;
let result = "";
let resultImg = "";
let getProd = [];

$(document).ready(function () {
    
});


export function inicializar(){
    getAllIngredientes();
    getAll();
    
    $('#txtMedida').on('change', function () {
        var medidaSeleccionada = $(this).val();
        console.log(medidaSeleccionada);
        var filas = $('#tblProductos tbody tr');

        filas.each(function () {
            var cantidadOriginal = parseInt($(this).find('td:eq(1)').attr('valuepiezas'));
            if (cantidadOriginal !== 0) {
                var cantidadActual = parseInt($(this).find('td:eq(1)').text()); 
                if (medidaSeleccionada === 'caja1K') {
                    cantidadActual = Math.ceil(cantidadOriginal / 25);
                } else if (medidaSeleccionada === 'cajaMedioK') {
                    cantidadActual = Math.ceil(cantidadOriginal / 12.5);
                }else if (medidaSeleccionada === 'pieza'){
                    cantidadActual = Math.ceil(cantidadOriginal);
                }

                $(this).find('td:eq(1)').text(cantidadActual);
            }
        });
    });
    
    $("#txtGalleta").change(function () {
        var idProducto = $(this).find("option:selected").attr("idproducto");
        nombre = $(this).find("option:selected").attr("value");
        cantidadExistentes = $(this).find("option:selected").attr("cantidadExistentes");
        precioVenta = $(this).find("option:selected").attr("precioVenta");
        precioProduccion = $(this).find("option:selected").attr("precioProduccion");

        document.getElementById("txtIdProducto").value = idProducto; 
    });
}

export function guardarGalleta(){
    
    document.getElementById("txtIngredientes").value = "";
    document.getElementById("txtCantidadPorcion").value = "";
    var tbody = $('#tbodyIngredientes');
    tbody.empty(); // Vacía el tbody actual
    
    let datos = null;
    let params = null;

    let producto = new Object();
    producto.medida = {};

    let len = parseInt(document.getElementById("txtIdProducto").value.trim().length);
    let len1 = parseInt(document.getElementById("txtIdProductoEditar").value.trim().length);

    if (len1 === 0){
        producto.idProducto = 0;
    

        producto.nombreProducto = document.getElementById("txtNuevaGalleta").value;
        producto.cantidadExistentes = 0;
        producto.precioProduccion = document.getElementById("txtPrecioProdGalleta").value;
        producto.precioVenta = document.getElementById("txtPrecioVentaGalleta").value;
        producto.medida.idMedida = 1;
        producto.fotografia = document.getElementById("base64Output").value;

        datos = {
            datosProducto: JSON.stringify(producto)
        };
    
    console.log("datos: "+ datos);
    
    params = new URLSearchParams(datos);
    console.log(params);
    fetch("../../api/producto/save",
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
                console.log(arrayIngredientes);
                
                    // FETCH INGREDIENTES
                    for (var i = 0; i < arrayIngredientes.length; i++) {
                        let datosIng = null;
                        let paramsIng = null;

                        let crearProducto = new Object();
                        crearProducto.producto = {};
                        crearProducto.medida = {};
                        crearProducto.materiaPrima = {};

                        crearProducto.porcion = arrayIngredientes[i]['porcion'];
                        crearProducto.producto.idProducto = data['idProducto'];
                        crearProducto.medida.idMedida = 3;
                            console.log(arrayIngredientes[i]['idMateriaPrima']);
                        crearProducto.materiaPrima.idMateriaPrima = arrayIngredientes[i]['idMateriaPrima'];

                        datosIng = {
                            datosCrearProducto: JSON.stringify(crearProducto)
                        };

                    console.log(datosIng);

                    paramsIng = new URLSearchParams(datosIng);
                    console.log(paramsIng);
                    fetch("../../api/producto/guardarCrearProducto",
                            {method: "POST",

                                headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'},
                                body: paramsIng
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
                            });
                    }
                $('#modalProductosNuevos .btn-secondary').click();
                Swal.fire('', 'Galleta registrada correctamente', 'success');
                getAll();
                clean();
                  
            });
    }else{
        console.log("EDITAR");
        let idProduc = parseInt(document.getElementById("txtIdProductoEditar").value);
        producto.idProducto = idProduc;
    
        producto.nombreProducto = document.getElementById("txtEditarGalleta").value;
        producto.cantidadExistentes = document.getElementById("txtCantidadExisEditar").value;
        producto.precioProduccion = document.getElementById("txtPrecioProdEditar").value;
        producto.precioVenta = document.getElementById("txtPrecioVentaEditar").value;
        producto.medida.idMedida = 1;
        producto.fotografia = document.getElementById("base64OutputEditar").value;

        datos = {
            datosProducto: JSON.stringify(producto)
        };
    
    console.log("datos: "+ datos);
    params = new URLSearchParams(datos);
    console.log(params);
    fetch("../../api/producto/save",
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

                Swal.fire('', 'Galleta registrada correctamente', 'success');
                getAll();
                clean();
                $('#modalProductosEditar').modal('hide');
            });
    }
}

export function agregarGalletaHorneada(){
    let datos = null;
    let params = null;

    let producto = {};
    producto.medida = {};
    
    let len = parseInt(document.getElementById("txtIdProducto").value.trim().length);
    if (len === 0){
            producto.idProducto = 0;
    } else{
        console.log("ACTUALIZAR");
    producto.idProducto = parseInt(document.getElementById("txtIdProducto").value);
    }
            
    var valueMerma = $('#txtMerma').val();
    console.log(valueMerma);
        if (valueMerma !== "") {
            let total = 120 - parseInt(valueMerma);
            totalCantidadExis = parseInt(cantidadExistentes) + total;
    } else {
            totalCantidadExis = (parseInt(cantidadExistentes) + 120);
    }
        console.log(totalCantidadExis);

        
        producto.nombreProducto = nombre;
        producto.cantidadExistentes = totalCantidadExis;
        producto.precioVenta = precioVenta;
        producto.precioProduccion = precioProduccion;
        
        producto.medida.idMedida = idMedida;
            
        datos = {
            datosProducto: JSON.stringify(producto) 
        };
        
        console.log(datos);

        params = new URLSearchParams(datos);
        fetch("../../api/producto/save",
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

                document.getElementById("txtIdProducto").value = data.idProducto;
                
                //FETCH OBTENER MATERIA PRIMA
                
                let paramsMateria = null;
                let datosProducto =null;
                
                datosProducto = data['idProducto'];
                console.log(datosProducto);
                
                paramsMateria = new URLSearchParams(datosProducto);
                fetch("../../api/producto/getProductoCreado?datosProducto="+datosProducto,
                    {method: "POST",
                        headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'},
                        //body: datosProducto
                    })

                    .then(response => {
                        return response.json();
                    })
                    .then(function (data){
                        console.log(data);
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
                        
                    //FETCH DESCONTAR EN MATERIA PRIMA
                    
                    let paramsMateriaPrima = null;
                    let datosMateria =null;
                    
                    let materiaPrima = {};
                    materiaPrima.medida = {};
                    
                        for (var i = 0; i < data.length; i++) {
                            var fechaObj = new Date(data[i]['materiaPrima']['fechaCompra']);
                            var dia = fechaObj.getDate();
                            var mes = fechaObj.getMonth() + 1;
                            var anio = fechaObj.getFullYear();
                            var fechaCompra = dia + '/' + mes + '/' + anio;

                            var fechaVen = new Date(data[i]['materiaPrima']['fechaVencimiento']);
                            var dia = fechaVen.getDate();
                            var mes = fechaVen.getMonth() + 1;
                            var anio = fechaVen.getFullYear();
                            var fechaVencimiento = dia + '/' + mes + '/' + anio;

                            let porcion = data[i]['porcion'];
                            let existenciaMateria = data[i]['materiaPrima']['cantidadExistentes'];

                            let actualizarExistencia = parseFloat(existenciaMateria - porcion);

                            materiaPrima.idMateriaPrima = data[i]['materiaPrima']['idMateriaPrima'];
                            materiaPrima.nombreMateria = data[i]['materiaPrima']['nombreMateria'];
                            materiaPrima.fechaCompra = fechaCompra;
                           materiaPrima.fechaVencimiento = fechaVencimiento;
                            materiaPrima.cantidadExistentes = parseFloat(actualizarExistencia);
                            materiaPrima.precioCompra = data[i]['materiaPrima']['precioCompra'];
                            materiaPrima.porcentaje = data[i]['materiaPrima']['porcentaje'];
                            materiaPrima.medida.idMedida = data[i]['medida']['idMedida'];                                     

                            datosMateria = {
                                datosMateria: JSON.stringify(materiaPrima) 
                            };

                            console.log(datosMateria);

                            paramsMateriaPrima = new URLSearchParams(datosMateria
                                    );
                            fetch("../../api/producto/actualizarMateria",
                                {method: "POST",
                                    headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'},
                                    body: paramsMateriaPrima
                                })

                                .then(response => {
                                    return response.json();
                                })
                                .then(function (data){
                                    console.log(data);
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
                                });
                        }

                    });          
                
                Swal.fire('', 'Se han agregado las galletas horneadas correctamente', 'success');
                getAll();
                clean();
                $('#modalProductos .btn-secondary').click();
            });
}

export function editarProducto(data){
    console.log("Editar", JSON.stringify(data));
    $('#modalProductosEditar').modal('show');
    getProducto(data.idProducto);
}

export function cargarTabla(data) {
    console.log(data);
    
    $('#txtGalleta').empty();
    
    // agregar galletas horneadas dinamico en select
    let fila = `<option selected>Elige la galleta</option>`;
    for (let i = 0; i <data.length; i++) {
        fila += `<option precioVenta="${data[i]['precioVenta']}" precioProduccion="${data[i]['precioProduccion']}" cantidadExistentes="${data[i]['cantidadExistentes']}" idProducto="${data[i]['idProducto']}" value="${data[i]['nombreProducto']}">${data[i]['nombreProducto']}</option>`;
    }
    $('#txtGalleta').append(fila);
    
        $('#tblProductos').on('click', '.btnEliminar', deleteProducto);
        $('#tblProductos').on('click', '.btnEditar', function() {
        // Obtener el índice de la fila seleccionada
        let rowIndex = $(this).closest('tr').index();

        // Obtener los datos del producto específico
        let producto = data[rowIndex];

        // Llamar a la función editarProducto con los datos del producto seleccionado
        editarProducto(producto);
    });
    // Destruye la tabla
    if ($.fn.DataTable.isDataTable('#tblProductos')) {
        $('#tblProductos').DataTable().destroy();
        $('#tblProductos tbody').empty();
    }

    // generar tabla dinamica
    for (let i = 0; i <data.length; i++) {        
        let fila = `<tr>
            <td>${data[i]['nombreProducto']}</td>
            <td valuePiezas="${data[i]['cantidadExistentes']}" >${data[i]['cantidadExistentes']}</td>
            <td>${data[i]['precioProduccion']}</td>
            <td>${data[i]['precioVenta']}</td>
            <td><button data-idproducto="${data[i]['idProducto']}" class="btnEliminar"><i class="fa-solid fa-trash" style="color: #c12525;"></i></button></td>
            <td><button data-idproducto="${data[i]['idProducto']}" id="btnEditar" class="btnEditar"><i class="fa-solid fa-pen-to-square" style="color: #57351f;"></i></button></td>`;

        $('#tblProductos tbody').append(fila);
    }

    $('#tblProductos').DataTable({
        dom: "<'row' <'col-sm-6'l><'col-sm-5'f>>  <'row' <'col-sm-12'tr>>  <'row' <'col-4'i><'col'p>>",
        initComplete: function(){
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
                previous: "Anterior"
            }
        },

        "ordering": false,
        retrieve: true
    }); 

    $('#agregarGalletasHorneadas').on('click', 'button', function () {
        $('#modalProductos').modal('show');
    });

    $('#agregarGalletaNueva').on('click', 'button', function () {
        $('#modalProductosNuevos').modal('show');
    });
    

};

export function cargarIngredientes(dataIng){
    console.log(dataIng);
    
     // agregar ingredientes dinamico en select
    let filaIngredientes = `<option selected>Elige el ingrediente</option>`;
    for (let i = 0; i <dataIng.length; i++) {
        filaIngredientes += `<option idMateriaPrima="${dataIng[i]['idMateriaPrima']}">${dataIng[i]['nombreMateria']}</option>`;
    }
    $('#txtIngredientes').append(filaIngredientes);
}

var arrayIngredientes = [];

export function llenarTablaIngredientes() {        
    let ing = document.getElementById("txtIngredientes").value;
    let cantidadPorcion = document.getElementById("txtCantidadPorcion").value;
    var optionElement = $('#txtIngredientes option:selected');
    var idMateriaPrima = optionElement.attr('idmateriaprima');

    arrayIngredientes.push(
            {idMateriaPrima:idMateriaPrima, ingrediente: ing, porcion: cantidadPorcion }
    );
    
    console.log(arrayIngredientes);
    var tbody = $('#tbodyIngredientes');
    //tbody.empty(); // Vacía el tbody actual

    var fila = $('<tr>');
        fila.append('<td>' + ing + '</td>');
        fila.append('<td>' + cantidadPorcion + ' KG</td>');
    tbody.append(fila);
}

export function deleteProducto() {
    Swal.fire({
        title: "¿Estás seguro?",
        text: "La galleta se eliminará",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Sí, eliminarlo"
    }).then((result) => {
        if (result.isConfirmed) {  
            const idProducto = $(this).data('idproducto');
            console.log(idProducto);
            let datos = null;
            let params = null;

            let producto = new Object();
            producto.idProducto = idProducto;
            datos = {
                datosProducto: JSON.stringify(producto)
            };

            params = new URLSearchParams(datos);

            fetch("../../api/producto/delete",
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
                        text: "Tu galleta ha sido eliminada.",
                        icon: "success"
                    });
                    getAll();
                }
            }); 
}

function getAll(){
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

function getAllIngredientes(){
    let url = "../../api/producto/getAllIngredientes";
    fetch(url)
        .then(response => {
            return response.json();
        })
        .then(function (data) {
            cargarIngredientes(data);
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

function getProducto(idProducto){
    let producto = new Object();
    let datos = null;
    let params = null;
    producto.idProducto = idProducto;
    
    datos = {
        datoProducto: JSON.stringify(producto) 
        };
        
    params = new URLSearchParams(datos);
    fetch("../../api/producto/getProducto",
        {method: "POST",
            headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'},
            body: params
        })

        .then(response => {
            return response.json();
        })
        .then(function (data){
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
            console.log(data);
            document.getElementById("txtEditarGalleta").value = data['nombreProducto'];
            document.getElementById("txtPrecioProdEditar").value = data['precioProduccion'];
            document.getElementById("txtPrecioVentaEditar").value = data['precioVenta'];
            document.getElementById("fotografiaEditar").src = data['fotografia'];
            document.getElementById("txtIdProductoEditar").value = data['idProducto'];
            document.getElementById("txtCantidadExisEditar").value = data['cantidadExistentes'];
        });
}

export function convertirPDF() {
    const input = document.getElementById('fotografiaInput');
    
    const file = input.files[0];

    if (file) {
        const reader = new FileReader();
        reader.onloadend = function () {
            const base64data = reader.result; // Usar reader.result en lugar de reader.result1
            document.getElementById("base64Output").value = base64data;
            mostrarImg(base64data);
            result = base64data;
            console.log(base64data);
        }
        reader.readAsDataURL(file);
    }
}

export function mostrarImg(base64data) {
    console.log(base64data);
    document.getElementById("base64Output").value = base64data;
    const imagen = document.getElementById('fotografia');
    imagen.src = base64data;
    resultImg = imagen;
}

export function convertirPDFEditar() {
    const input = document.getElementById('fotografiaInputEditar');
    
    const file = input.files[0];

    if (file) {
        const reader = new FileReader();
        reader.onloadend = function () {
            const base64data = reader.result; // Usar reader.result en lugar de reader.result1
            document.getElementById("base64OutputEditar").value = base64data;
            mostrarImgEditar(base64data);
            result = base64data;
        }
        reader.readAsDataURL(file);
    }
}

export function mostrarImgEditar(base64data) {
    document.getElementById("base64OutputEditar").value = base64data;
    const imagen = document.getElementById('fotografiaEditar');
    
    imagen.src = base64data;
    resultImg = imagen;
}

export function clean(){
    //Limpiar campos de registro de galletas
    document.getElementById("txtNuevaGalleta").value = "";
    document.getElementById("txtPrecioProdGalleta").value = "";
    document.getElementById("txtPrecioVentaGalleta").value = "";
    document.getElementById("fotografiaInput").value = "";
    document.getElementById("base64Output").value = "";
    document.getElementById("fotografia").src = "";
    
    document.getElementById("txtEditarGalleta").value = "";
    document.getElementById("txtPrecioProdEditar").value = "";
    document.getElementById("txtPrecioVentaEditar").value = "";
    document.getElementById("fotografiaEditar").src = "";
    document.getElementById("txtIdProductoEditar").value = "";
    document.getElementById("txtCantidadExisEditar").value = "";

}