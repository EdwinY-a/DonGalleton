let moduloProducto;
let moduloEstadoDeCuenta;
let moduloPuntoDeVenta;
let moduloInventario;
let moduloUsuario;
let moduloLogs;

let currentUser = JSON.parse(sessionStorage.getItem('currentUser'));
if (currentUser !== null){
    let logsUsuario = currentUser.nombreUsuario;
}

function inicializar(){
    cargarModuloPuntoDeVenta();
    getUser();
}

function getUser(){
    let usuario = new Object();
    let datos = null;
    let params = null;
    if (currentUser === null){
        currentUser = '';
    }
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
        }
    }
});
}
// Aqui van las funciones que mandan a llamar a las vistas solamente

function mostrarCargando(){
    $(".spinner-container").removeClass("d-none");
}
    
function ocultarCargando(){
    $(".spinner-container").addClass("d-none");
}

function cargarModuloProductos() {
    fetch("../../modules/producto/vista_producto.html")
            .then(
                    function (response) {
                        console.log(response);
                        return response.text();
                    }
            )
            .then(
                    function (html) {
                        document.getElementById("contenedorPrincipal").innerHTML = html;
                        import("../producto//productoController.js").then(
                                function (controller) {
                                    moduloProducto = controller;
                                    moduloProducto.inicializar();
                                }
                        );
                    }
            );
}

function cargarModuloEstadoDeCuenta(){
    fetch("../estadodecuenta/vista_estadodecuenta.html")
            .then(
                function(response){
                    return response.text();
                }
            )
                .then(
                    function(html){
                        document.getElementById("contenedorPrincipal").innerHTML = html;
                        import("../estadodecuenta/estadodecuentaController.js")
                                .then(
                                    function(controller){
                                        moduloEstadoDeCuenta = controller;
                                        moduloEstadoDeCuenta.inicializar();
                                    }
                                );
                    }
                );
}

function cargarModuloPuntoDeVenta(){
    fetch("../puntodeventa/vista_puntodeventa.html")
            .then(
                function(response){
                    return response.text();
                }
            )
                .then(
                    function(html){
                        document.getElementById("contenedorPrincipal").innerHTML = html;
                        import("../puntodeventa/puntodeventaController.js")
                                .then(
                                    function(controller){
                                        moduloPuntoDeVenta = controller;
                                        moduloPuntoDeVenta.inicializar();
                                    }
                                );
                    }
                );
}

function cargarModuloInventario(){
    fetch("../inventario/vista_inventario.html")
            .then(
                function(response){
                    return response.text();
                }
            )
                .then(
                    function(html){
                        document.getElementById("contenedorPrincipal").innerHTML = html;
                        import("../inventario/inventarioController.js")
                                .then(
                                    function(controller){
                                        moduloInventario = controller;
                                        moduloInventario.inicializar();
                                    }
                                );
                    }
                );
}

function cargarModuloUsuario(){
    fetch("../usuario/vista_usuario.html")
            .then(
                function(response){
                    return response.text();
                }
            )
                .then(
                    function(html){
                        document.getElementById("contenedorPrincipal").innerHTML = html;
                        import("../usuario/usuarioController.js")
                                .then(
                                    function(controller){
                                        moduloUsuario = controller;
                                        moduloUsuario.inicializar();
                                    }
                                );
                    }
                );
}

function cargarModuloLogs(){
    fetch("../logs/vista_logs.html")
            .then(
                function(response){
                    return response.text();
                }
            )
                .then(
                    function(html){
                        document.getElementById("contenedorPrincipal").innerHTML = html;
                        import("../logs/logsController.js")
                                .then(
                                    function(controller){
                                        moduloLogs = controller;
                                        moduloLogs.inicializar();
                                    }
                                );
                    }
                );
}

function cerrarSesion(){
        mostrarCargando();
        sessionStorage.removeItem('currentUser');
        Swal.fire('', "Sesion cerrada con exito.", 'success');
        window.location.replace("../../index.html");
}

$("#cerrarSesion").click(function(){
    cerrarSesion();
});

let inactividadTimer;


function cerrarSesionPorInactividad() {
    mostrarCargando();
    sessionStorage.removeItem('currentUser');

    ocultarCargando();
    Swal.fire('', "Sesión cerrada por inactividad.", 'info');
    window.location.replace("../../index.html");
}

function reiniciarTimerInactividad() {
    if (inactividadTimer) {
        clearTimeout(inactividadTimer);
    }
    inactividadTimer = setTimeout(cerrarSesionPorInactividad, 60000);
}

function manejarInteraccionUsuario() {
    reiniciarTimerInactividad();
}

document.addEventListener('mousemove', manejarInteraccionUsuario);
document.addEventListener('keypress', manejarInteraccionUsuario);

reiniciarTimerInactividad();
