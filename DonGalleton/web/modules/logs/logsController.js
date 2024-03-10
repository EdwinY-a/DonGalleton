let currentUser = JSON.parse(sessionStorage.getItem('currentUser'));
let logsUsuario = currentUser.nombreUsuario;
console.log(currentUser);

export function inicializar() {
    getAll();
    getUser();
}

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
            if (data.rol !== "admin") {
                console.log('ENTRO');
                $("#bodyTabla").remove();
            }
        }
    }
});
}


export function cargarTabla(data) {
    console.log(data);

//    $('#tblUsuario').on('click', '.btnEliminar', deleteUsuario);
//    $('#tblUsuario').on('click', '.btnEditar', function () {
//        let rowIndex = $(this).closest('tr').index();
//        let datos = data[rowIndex];
//        editarUsuario(datos);
//    });

    // Destruye la tabla
    if ($.fn.DataTable.isDataTable('#tblUsuario')) {
        $('#tblUsuario').DataTable().destroy();
        $('#tblUsuario tbody').empty(); // Limpiar el contenido del tbody
    }

    // generar tabla dinamica
    for (let i = 0; i < data.length; i++) {
        let fila = `<tr>
                        <td>${data[i]['usuario']}</td>
                        <td>${data[i]['procedimiento']}</td>
                        <td>${data[i]['fecha']}</td>
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

    $('#agregarUsuario').on('click', 'button', function () {
        $('#modalUsuario').modal('show');
    });

}

function getAll() {
    let url = "../../api/logs/getAll";
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
