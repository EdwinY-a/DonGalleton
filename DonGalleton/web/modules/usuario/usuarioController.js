let currentUser = JSON.parse(sessionStorage.getItem('currentUser'));
let logsUsuario = currentUser.nombreUsuario;

export function inicializar() {
    getAll();
    ocultarContrasenia();
}

$(document).ready(function() {
    getUser();
    
});

function getUser(){
    let usuario = new Object();
    let datos = null;
    let params = null;
    usuario.idUsuario = currentUser.idUsuario;
    datos = {
        datoUsuario: JSON.stringify(usuario) 
        };

    params = new URLSearchParams(datos);
    $.ajax({
    url:"../../api/log/getUsuario",
    type: "POST",
    data: params.toString(),
    contentType: "application/x-www-form-urlencoded;charset=UTF-8",
    success: function(response){
        let data = response;
        if(data.error){
            Swal.fire('', data.error, 'warning');
        } else if(data.exception){
            Swal.fire('', "Error interno del servidor. Intente mas tarde.", 'error');
            
        } else{ 
            if (typeof data.lastToken === 'undefined'){
                window.location.replace('../../index.html');
            }
            
            if (data.rol !== "admin") {
                console.log('ENTRO');
                $("#bodyTabla").remove();
            }
        }
    }
});
}

export function guardarUsuario() {

    let datos = null;
    let params = null;

    let usuario = new Object();
    usuario = {};

    let idUsuario = parseInt(document.getElementById("txtIdUsuario").value.trim().length);
    let idUsuarioEditar = parseInt(document.getElementById("txtIdUsuarioEditar").value.trim().length);
    console.log("NORMARL " + idUsuario + "EDITAR " + idUsuarioEditar);
    if (idUsuario === 0 && idUsuarioEditar === 0) {
        usuario.idUsuario = 0;

        usuario.nombreUsuario = sanitizar(document.getElementById("txtNombre").value);
        usuario.contrasenia = sanitizar(document.getElementById("txtContraseña").value);
        usuario.rol = sanitizar(document.getElementById("txtRol").value);
        if (usuario.nombreUsuario === "" || usuario.contrasenia === "" || usuario.rol === "") {
        Swal.fire('', 'Por favor, complete todos los campos (correo electrónico, contraseña o rol) para guardar el usuario.', 'warning');
        return;
    }
        usuario.logsUser = logsUsuario;
        
        datos = {
            datosUsuario: JSON.stringify(usuario)
        };

        console.log("datos: " + datos);
        params = new URLSearchParams(datos);
        console.log(params);
        fetch("../../api/log/save",
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

                    Swal.fire('', 'Usuario registrado correctamente', 'success');
                    getAll();
                    clean();
                    $('#modalUsuario .btn-secondary').click();

                });
    } else {
        //let idUsuarioEditar = parseInt(document.getElementById("txtIdUsuarioEditar").value);
        usuario.idUsuario = idUsuarioEditar;

        usuario.nombreUsuario = sanitizar(document.getElementById("txtNombreEditar").value);
        usuario.contrasenia = sanitizar(document.getElementById("txtContraseñaEditar").value);
        usuario.rol = sanitizar(document.getElementById("txtRolEditar").value);
        if (usuario.nombreUsuario === "" || usuario.contrasenia === "" || usuario.rol === "") {
        Swal.fire('', 'Por favor, complete todos los campos (correo electrónico, contraseña o rol) para guardar el usuario.', 'warning');
        return;
    }
        usuario.logsUser = logsUsuario;

        datos = {
            datosUsuario: JSON.stringify(usuario)
        };

        console.log("datos: " + datos);
        params = new URLSearchParams(datos);
        console.log(params);
        fetch("../../api/log/save",
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

                    Swal.fire('', 'Usuario actualizado correctamente', 'success');
                    getAll();
                    clean();
                    $('#modalUsuarioEditar .btn-secondary').click();
                });
    }
}

export function sanitizar(texto) {
    for (var i = 0;
    i < texto.length; i++) {
        texto = texto.replace("(", "");
        texto = texto.replace(")", "");
        texto = texto.replace(";", "");
        texto = texto.replace("'", "");
        texto = texto.replace("/", "");
        texto = texto.replace("-", "");
        texto = texto.replace("*", "");
        texto = texto.replace("%", "");
        texto = texto.replace("<", "");
        texto = texto.replace(">", "");
        texto = texto.replace(" ", "");
    }
    return texto;
}

export function togglePasswordVisibility() {
    var passwordField = document.getElementById("txtContraseña");
    var toggleButton = document.getElementById("toggleButton");

    if (passwordField.type === "password") {
        passwordField.type = "text";
        toggleButton.textContent = "Ocultar";
    } else {
        passwordField.type = "password";
        toggleButton.textContent = "Mostrar";
    }
}

export function togglePasswordVisibility2() {
    var passwordField = document.getElementById("txtContraseñaEditar");
    var toggleButton = document.getElementById("toggleButton");

    if (passwordField.type === "password") {
        passwordField.type = "text";
        toggleButton.textContent = "Ocultar";
    } else {
        passwordField.type = "password";
        toggleButton.textContent = "Mostrar";
    }
}

document.getElementById('txtContraseña').addEventListener('input', function () {
    var botonGuardar = document.getElementById('btnSaveUser');
    botonGuardar.disabled = true;
    var contrasena = this.value;
    var condicionesContrasena = document.getElementById('condicionesContrasena');
    var mensaje = '';

    // Evaluar las condiciones de la contraseña
    if (contrasena.length < 8) {
        mensaje += 'La contraseña debe tener al menos 8 caracteres.<br>';
    }
    if (!/[A-Z]/.test(contrasena)) {
        mensaje += 'La contraseña debe contener al menos una letra mayúscula.<br>';
    }
    if (!/[a-z]/.test(contrasena)) {
        mensaje += 'La contraseña debe contener al menos una letra minúscula.<br>';
    }
    if (!/[0-9]/.test(contrasena)) {
        mensaje += 'La contraseña debe contener al menos un número.<br>';
    }
    if (!/[@#$^&_+[\]{}'"\\|,\?]/.test(contrasena)) {
        mensaje += 'La contraseña debe contener al menos un carácter especial (@#$^&_+[]{}\'"\\|,?).<br>';
    }

    condicionesContrasena.innerHTML = mensaje;

    var botonGuardar = document.getElementById('btnSaveUser');
    if (mensaje !== '') {
        botonGuardar.disabled = true;
    } else {
        botonGuardar.disabled = false;
    }
});

document.getElementById('txtContraseñaEditar').addEventListener('input', function () {
    var botonGuardar = document.getElementById('btnEditarGalleta');
    botonGuardar.disabled = true;
    var contrasena = this.value;
    var condicionesContrasena = document.getElementById('condicionesContrasenaEditar');
    var mensaje = '';

    // Evaluar las condiciones de la contraseña
    if (contrasena.length < 8) {
        mensaje += 'La contraseña debe tener al menos 8 caracteres.<br>';
    }
    if (!/[A-Z]/.test(contrasena)) {
        mensaje += 'La contraseña debe contener al menos una letra mayúscula.<br>';
    }
    if (!/[a-z]/.test(contrasena)) {
        mensaje += 'La contraseña debe contener al menos una letra minúscula.<br>';
    }
    if (!/[0-9]/.test(contrasena)) {
        mensaje += 'La contraseña debe contener al menos un número.<br>';
    }
    if (!/[@#$^&_+[\]{}'"\\|,\?]/.test(contrasena)) {
        mensaje += 'La contraseña debe contener al menos un carácter especial (@#$^&_+[]{}\'"\\|,?).<br>';
    }

    condicionesContrasena.innerHTML = mensaje;

    var botonGuardar = document.getElementById('btnEditarGalleta');
    if (mensaje !== '') {
        botonGuardar.disabled = true;
    } else {
        botonGuardar.disabled = false;
    }
});

export function editarUsuario(data) {
    console.log("Editar", JSON.stringify(data));
    $('#modalUsuarioEditar').modal('show');
    getUsuario(data.idUsuario);

}

export function ocultarContrasenia() {
    var table = document.getElementById("tblUsuario");
    var rows = table.getElementsByTagName("tr");

    for (var i = 0; i < rows.length; i++) {
        var cells = rows[i].getElementsByTagName("td");
        for (var j = 0; j < cells.length; j++) {
            if (cells[j].textContent === "Contraseña") {
                cells[j].textContent = "********";
            }
        }
    }
}

export function cargarTabla(data) {
    console.log(data);

    $('#tblUsuario').on('click', '.btnEliminar', deleteUsuario);
    $('#tblUsuario').on('click', '.btnEditar', function () {
        let rowIndex = $(this).closest('tr').index();
        let datos = data[rowIndex];
        editarUsuario(datos);
    });

    // Destruye la tabla
    if ($.fn.DataTable.isDataTable('#tblUsuario')) {
        $('#tblUsuario').DataTable().destroy();
        $('#tblUsuario tbody').empty(); // Limpiar el contenido del tbody
    }

    // generar tabla dinamica
    for (let i = 0; i < data.length; i++) {
        let fila = `<tr>
                        <td>${data[i]['nombreUsuario']}</td>
                        <td>
                            <span class="ocultar-contrasenia">${data[i]['contrasenia'].replace(/./g, '*')}</span>
                        </td>
                        <td>${data[i]['rol']}</td>
                        <td>${data[i]['estatus']}</td>
                        <td><button data-idUsuario="${data[i]['idUsuario']}" class="btnEliminar"><i class="fa-solid fa-trash" style="color: #c12525;"></i></button></td>
                        <td><button data-idUsuario="${data[i]['idUsuario']}" id="btnEditar" class="btnEditar"><i class="fa-solid fa-pen-to-square" style="color: #57351f;"></i></button></td>
                    </tr>`;

        $('#tblUsuario tbody').append(fila);
    }

    // Inicializar DataTable
    $('#tblUsuario').DataTable({
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
            lengthMenu: "Mostrar _MENU_ Entradas",
            loadingRecords: "Cargando...",
            processing: "Procesando...",
            search: " ",
            searchPlaceholder: "Buscar",
            zeroRecords: "Sin resultados encontrados",
            paginate: {
                first: "Primero",
                last: "Ultimo",
                next: "Siguiente",
                previous: "Anterior",
            }
        },
        ordering: false,
        retrieve: true
    });
    
    var currentUserData = sessionStorage.getItem('currentUser');
    var currentUser = JSON.parse(currentUserData);    


    $('#agregarUsuario').on('click', 'button', function () {
        $('#modalUsuario').modal('show');
    });

}


export function deleteUsuario() {

    Swal.fire({
        title: "¿Estás seguro?",
        text: "El Usuario se eliminará",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Sí, eliminarlo"
    }).then((result) => {
        if (result.isConfirmed) {
            const idUsuario = $(this).data('idUsuario');
            console.log("DATOS "+$(this).data('idUsuario'));
            console.log(idUsuario);
            let datos = null;
            let params = null;

            let usuario = new Object();
            usuario.idUsuario = idUsuario;
            usuario.logsUser = logsUsuario;
            datos = {
                datosUsuario: JSON.stringify(usuario)
            };

            params = new URLSearchParams(datos);

            fetch("../../api/log/delete",
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
                text: "El Usuario ha sido eliminado.",
                icon: "success"
            });
            getAll();
        }
    });
}

function getAll() {
    let url = "../../api/log/getAll";
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

function getUsuario(idUsuario) {
    console.log(idUsuario);
    let usuario = new Object();
    let datos = null;
    let params = null;
    usuario.idUsuario = idUsuario;
    usuario.logsUser = logsUsuario;

    datos = {
        datoUsuario: JSON.stringify(usuario)
    };
    console.log(datos);
    params = new URLSearchParams(datos);
    fetch("../../api/log/getUsuario",
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


                console.log('data: '+data);
                document.getElementById("txtNombreEditar").value = data['nombreUsuario'];
                document.getElementById("txtContraseñaEditar").value = data['contrasenia'];
                document.getElementById("txtRolEditar").value = data['rol'];
                document.getElementById("txtIdUsuarioEditar").value = data['idUsuario'];

            });
}

export function clean() {

    document.getElementById("txtNombre").value = "";
    document.getElementById("txtContraseña").value = "";
    document.getElementById("txtRol").value = "";
    document.getElementById("txtIdUsuario").value = "";

    document.getElementById("txtNombreEditar").value = "";
    document.getElementById("txtContraseñaEditar").value = "";
    document.getElementById("txtRolEditar").value = "";
    document.getElementById("txtIdUsuarioEditar").value = "";

}
