
/*
 Por defecto debe ir:
 °   Login
 °   Logout
 °   Santitizar
 °   Normalizar
 */


$(document).ready(function () {


    async function sanitizar(texto) {
        for (var i = 0; i < texto.length; i++) {
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

    async function normalizar(texto) {
        texto = texto.toUpperCase();
        for (var i = 0; i < texto.length; i++) {
            texto = texto.replace("Á", "A");
            texto = texto.replace("É", "E");
            texto = texto.replace("Í", "I");
            texto = texto.replace("Ó", "O");
            texto = texto.replace("Ú", "U");
        }
        return texto;
    }


    function activarAyudas() {
        $("#divUsuario").addClass("was-validated");
        $("#divContrasenia").addClass("was-validated");

        $("#divMensajeUsuario").removeClass("d-none");
        $("#divMensajeContrasenia").removeClass("d-none");
    }

    function mostrarCargando() {
        $(".spinner-container").removeClass("d-none");
    }

    function ocultarCargando() {
        $(".spinner-container").addClass("d-none");
    }

    function limpiar() {
        $("#idUsuario").val("");
        $("#idPassword").val("");
    }
    let intentosFallidos = 0;
    const bloqueoDuracion = 1 * 60 * 1000;

    function reiniciarIntentosFallidos() {
        setTimeout(() => {
            intentosFallidos = 0;
        }, bloqueoDuracion);
    }

    async function entrar() {
        console.log(intentosFallidos);
        mostrarCargando();
        if (intentosFallidos >= 3) {
            Swal.fire('', 'Ha excedido el número máximo de intentos de inicio de sesión. Intente nuevamente más tarde.', 'error');
            ocultarCargando();
            return;
        }

        let usuario = await sanitizar($("#idUsuario").val());
        let contrasenia = await sanitizar($("#idPassword").val());

        if (usuario === contrasenia) {
            Swal.fire('', 'No pueden ser igual Usuario y Contraseña', 'warning');
            return;
        }
        if (!validarContraseña(contrasenia)) {
            Swal.fire('', 'Dato Incorrecto.', 'warning');
            return;
        } else {

                        
                        let datos = JSON.stringify({nombreUsuario: usuario, contrasenia: contrasenia});
                        let params = new URLSearchParams({datos: datos});
                        console.log(params);
                        $.ajax({
                            url: "api/log/in",
                            type: "POST",
                            data: params.toString(),
                            contentType: "application/x-www-form-urlencoded;charset=UTF-8",
                            success: function (response) {
                                let data = response;

                                if (data.error) {
                                    intentosFallidos++;
                                    Swal.fire('', data.error, 'warning');
                                    ocultarCargando();
                                    activarAyudas();
                                    limpiar();
                                } else if (data.exception) {
                                    Swal.fire('', "Error interno del servidor. Intente mas tarde.", 'error');
                                    ocultarCargando();
                                    activarAyudas();
                                    limpiar();
                                } else {
                                console.log(data);
                                
                                intentosFallidos = 0;
                                ocultarCargando();
                                Swal.fire('', "Bienvenido " + data.nombreUsuario, 'success');
                                sessionStorage.setItem('currentUser', JSON.stringify(data));
                                ocultarCargando();
                                Swal.fire({
                                title: "Inserta tu código de verificación",
                                input: "text",
                                inputAttributes: {
                                  autocapitalize: "off"
                                },
                                showCancelButton: true,
                                confirmButtonText: "Enviar",
                                showLoaderOnConfirm: true,
                                preConfirm: async (codigo) => {
                                    console.log(codigo);
                                    let params = new URLSearchParams({codigo: codigo});
                                    console.log(params);
                                    $.ajax({
                                        url: "api/log/verifCode",
                                        type: "POST",
                                        data: params.toString(),
                                        contentType: "application/x-www-form-urlencoded;charset=UTF-8",
                                        success: function(response){
                                            console.log(response['response']);
                                                if (response.response === "Correcto") {
                                                    window.location.replace("modules/main/vista_main.html");  
                                                }else{
                                                    cerrarSesion1();
                                                    Swal.fire({
                                                        icon: "error",
                                                        title: "Oops...",
                                                        text: "Inicio de sesión incorrecto.",
                                                      });
                                                }
                                        },
                                        error: function(error){
                                            console.log(error);
                                        }
                                    });
                                }
                              });
                            }
                        });
        }

    }

    $("#btnLogin").click(function () {
        entrar();
    });

    $("#idPassword").change(function () {
        entrar();
    });
});

function validarContraseña(contraseña) {
    const contraseñasInseguras = [
        "123456",
        "password",
        "contraseña",
        "admin",
        "987654321",
        "qwerty",
        "abc123",
        "letmein",
        "football",
        "123123",
        "princess",
        "welcome",
        "sunshine",
        "login",
        "monkey",
        "iloveyou"
    ];

    return !contraseñasInseguras.includes(contraseña.toLowerCase());
}

function cerrarSesion() {
    mostrarCargando();
    let us = sessionStorage.getItem('currentUser');
    let usuario = {"usuario": us};

    let params = new URLSearchParams(usuario);

    fetch("../../api/log/out", {
        method: "POST",
        headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'},
        body: params
    })
            .then(response => {
                return response.json();
            })
            .then(function (data) {
                if (data.exception) {
                    Swal.fire('', 'Error interno del servidor. Intente nuevamente más tarde.', 'error');
                    return;
                }
                if (data.error) {
                    Swal.fire('', data.error, 'warning');
                    return;
                } else {
                    Swal.fire('', 'Sesión cerrada con éxito', 'success');
                    sessionStorage.removeItem('currentUser');
                    window.location.replace('../../index.html');
                }
            })
            .catch(error => {
                console.error('Error al cerrar sesión:', error);
                Swal.fire('', 'Error al cerrar sesión. Intente nuevamente más tarde.', 'error');
            });
}


function cerrarSesion1() {
  let us = sessionStorage.getItem('currentUser');
  console.log(us);
  let usuario = { "usuario": us };
  
  let params = new URLSearchParams(usuario);

  fetch("api/log/out", {
    method: "POST",
    headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' },
    body: params
  })
    .then(response => {
      return response.json();
    })
    .then(function (data) {
      if (data.exception) {
        Swal.fire('', 'Error interno del servidor. Intente nuevamente más tarde.', 'error');
        return;
      }
      if (data.error) {
        Swal.fire('', data.error, 'warning');
        return;
      } else {
        sessionStorage.removeItem('currentUser');
        window.location.replace('index.html');
      }
    })
    .catch(error => {
      console.error('Error al cerrar sesión:', error);
      Swal.fire('', 'Error al cerrar sesión. Intente nuevamente más tarde.', 'error');
    });
}

$("#cerrarSesion").click(function () {
    cerrarSesion();
});

