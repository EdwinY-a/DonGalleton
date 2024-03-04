
/*
    Por defecto debe ir:
    °   Login
    °   Logout
    °   Santitizar
    °   Normalizar
 */

$(document).ready(function(){
    async function encriptar(texto){   
        const encoder = new TextEncoder();
        const data = encoder.encode(texto);
        const hash = await crypto.subtle.digest('SHA-256', data);
        const hashArray = Array.from(new Uint8Array(hash));
        const hashHex = hashArray.map((b) => b.toString(16).padStart(2, '0')).join('');
        return hashHex;
    }
    
    async function sanitizar(texto){
        for(var i = 0; i < texto.length; i++){
            texto=texto.replace("(", "");
            texto=texto.replace(")", "");
            texto=texto.replace(";", "");
            texto=texto.replace("'", "");
            texto=texto.replace("/", "");
            texto=texto.replace("-", "");
            texto=texto.replace("*", "");
            texto=texto.replace("%", "");
            texto=texto.replace("<", "");
            texto=texto.replace(">", "");
            texto=texto.replace(" ", "");
        }
        return texto;
    }
    
    async function normalizar(texto){
        texto = texto.toUpperCase();
        for(var i = 0; i < texto.length; i++){
            texto=texto.replace("Á", "A");
            texto=texto.replace("É", "E");
            texto=texto.replace("Í", "I");
            texto=texto.replace("Ó", "O");
            texto=texto.replace("Ú", "U");
        }
        return texto;
    }
    
    
    function activarAyudas() {
        $("#divUsuario").addClass("was-validated");
        $("#divContrasenia").addClass("was-validated");

        $("#divMensajeUsuario").removeClass("d-none");
        $("#divMensajeContrasenia").removeClass("d-none");
    }
    
    function mostrarCargando(){
        $(".spinner-container").removeClass("d-none");
    }
    
    function ocultarCargando(){
        $(".spinner-container").addClass("d-none");
    }
    
    function limpiar(){
        $("#idUsuario").val("");
        $("#idPassword").val("");
    }
    
    async function entrar(){
        mostrarCargando();
        
        let usuario = await sanitizar($("#idUsuario").val());
        let contrasenia = await sanitizar($("#idPassword").val());
        
        encriptar(contrasenia)
                .then((textoEncriptado) => {
                    let datos = JSON.stringify({nombreUsuario: usuario, contrasenia: textoEncriptado});
                    let params = new URLSearchParams({datos: datos});
                    console.log(datos);
                    console.log(params);
                    
                    $.ajax({
                        url: "api/log/in",
                        type: "POST",
                        data: params.toString(),
                        contentType: "application/x-www-form-urlencoded;charset=UTF-8",
                        success: function(response){
                            let data = response;
                            
                            if(data.error){
                                Swal.fire('', data.error, 'warning');
                                ocultarCargando();
                                activarAyudas();
                                limpiar();
                            } else if(data.exception){
                                Swal.fire('', "Error interno del servidor. Intente mas tarde.", 'error');
                                ocultarCargando();
                                activarAyudas();
                                limpiar();
                            } else{
                                Swal.fire('', "Bienvenido " + usuario, 'success');
                                sessionStorage.setItem('currentUser', JSON.stringify(data));
                                ocultarCargando();
                                window.location.replace("modules/main/vista_main.html");
                            }
                        }
                    });
        });
    }
    
    $("#btnLogin").click(function(){
        entrar();
    });
    
    $("#idPassword").change(function(){
        entrar();
    });
});
