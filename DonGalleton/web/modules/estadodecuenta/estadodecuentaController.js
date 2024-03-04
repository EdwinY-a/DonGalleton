let movimientos = [];
let total = 0;

export async function inicializar(){
    await traerMateriaMasComprada();
    await traerProductoMasVendido();
}

// Toda la funcionalidad del estado de cuenta

export function traerDatos(){
    mostrarCargando();
    
    let txtDesde = $("#txtDesde").val();
    let txtHasta = $("#txtHasta").val();
    
    let partesDesde = txtDesde.split('-');
    let desde = partesDesde[2] + '/' + partesDesde[1] + '/' + partesDesde[0];

    let partesHasta = txtHasta.split('-');
    let hasta = partesHasta[2] + '/' + partesHasta[1] + '/' + partesHasta[0];

    $.ajax({
        url: "../../api/movimiento/buscar?desde='" + desde + "'&hasta='" + hasta + "'",
        type: "GET",
        contentType: "application/x-www-form-urlencoded;charset=UTF-8",
        success: function (response){
            let data = response;
            
            if(data.error){
                Swal.fire('', data.error, 'warning');
                ocultarCargando();
            } else if(data.exception){
                Swal.fire('', "Error interno del servidor. Intente mas tarde.", 'error');
                ocultarCargando();
            } else{
                llenarTabla(data);
            }
        }
    });
}

export function llenarTabla(data){
    movimientos = data;
    
    let fila = '';
    for (let i = 0; i < data.length; i++) {
        let partesFecha = data[i].fechaMovimiento.split('-');
        let quitarHoras = partesFecha[2].substring(0, 2);
        let fecha = quitarHoras + "/" + partesFecha[1] + "/" + partesFecha[0];
        
        let color = data[i].tipoMovimiento === "Ingreso" ? "green" : "red";
        
        data[i].tipoMovimiento === "Ingreso" ? total += data[i].monto : total -= data[i].monto;
        
        fila += `<tr>
                    <td>${fecha}</td>
                    <td style="color: ` + color + `">${data[i].tipoMovimiento}</td>
                    <td>$${data[i].monto}</td>
                </tr>`;
    }
    let colorTotal = total > 0 ? "green" : "red";
    colorTotal = total === 0 ? "gray" : colorTotal;
    fila += `<tr>
                <td>BALANCE TOTAL:</td>
                <td></td>
                <td style="color: ` + colorTotal + `">$${total}</td>
             </tr>`;
    $("#tblMovimientos tbody").html(fila);

    
    $("#tblMovimientos").DataTable({
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
    
    ocultarCargando();
    document.getElementById("cmbFiltroIngEg").disabled = false;
}

export function filtrar(){
    let option = parseInt($("#cmbFiltroIngEg").val());
    
    if(option === 1){
        total = 0;
        let fila = '';
        for (let i = 0; i < movimientos.length; i++) {
            let partesFecha = movimientos[i].fechaMovimiento.split('-');
            let quitarHoras = partesFecha[2].substring(0, 2);
            let fecha = quitarHoras + "/" + partesFecha[1] + "/" + partesFecha[0];
        
            let color = movimientos[i].tipoMovimiento === "Ingreso" ? "green" : "red";
        
            movimientos[i].tipoMovimiento === "Ingreso" ? total += movimientos[i].monto : total -= movimientos[i].monto;
        
            fila += `<tr>
                        <td>${fecha}</td>
                        <td style="color: ` + color + `">${movimientos[i].tipoMovimiento}</td>
                        <td>$${movimientos[i].monto}</td>
                    </tr>`;
        }
        let colorTotal = total > 0 ? "green" : "red";
        colorTotal = total === 0 ? "gray" : colorTotal;
        fila += `<tr>
                    <td>BALANCE TOTAL:</td>
                    <td></td>
                    <td style="color: ` + colorTotal + `">$${total}</td>
                </tr>`;
        $("#tblMovimientos tbody").html(fila);
    } else if(option === 2){
        total = 0;
        let fila = '';
        for (let i = 0; i < movimientos.length; i++) {
            if(movimientos[i].tipoMovimiento === "Ingreso"){
                let partesFecha = movimientos[i].fechaMovimiento.split('-');
                let quitarHoras = partesFecha[2].substring(0, 2);
                let fecha = quitarHoras + "/" + partesFecha[1] + "/" + partesFecha[0];
        
                let color = movimientos[i].tipoMovimiento === "Ingreso" ? "green" : "red";
        
                movimientos[i].tipoMovimiento === "Ingreso" ? total += movimientos[i].monto : total -= movimientos[i].monto;
        
                fila += `<tr>
                            <td>${fecha}</td>
                            <td style="color: ` + color + `">${movimientos[i].tipoMovimiento}</td>
                            <td>$${movimientos[i].monto}</td>
                        </tr>`;
            }
        }
        let colorTotal = total > 0 ? "green" : "red";
        colorTotal = total === 0 ? "gray" : colorTotal;
        fila += `<tr>
                    <td>BALANCE TOTAL:</td>
                    <td></td>
                    <td style="color: ` + colorTotal + `">$${total}</td>
                </tr>`;
        $("#tblMovimientos tbody").html(fila);
    } else {
        total = 0;
        let fila = '';
        for (let i = 0; i < movimientos.length; i++) {
            if(movimientos[i].tipoMovimiento === "Egreso"){
                let partesFecha = movimientos[i].fechaMovimiento.split('-');
                let quitarHoras = partesFecha[2].substring(0, 2);
                let fecha = quitarHoras + "/" + partesFecha[1] + "/" + partesFecha[0];
        
                let color = movimientos[i].tipoMovimiento === "Ingreso" ? "green" : "red";
        
                movimientos[i].tipoMovimiento === "Ingreso" ? total += movimientos[i].monto : total -= movimientos[i].monto;
        
                fila += `<tr>
                            <td>${fecha}</td>
                            <td style="color: ` + color + `">${movimientos[i].tipoMovimiento}</td>
                            <td>$${movimientos[i].monto}</td>
                        </tr>`;
            }
        }
        let colorTotal = total > 0 ? "green" : "red";
        colorTotal = total === 0 ? "gray" : colorTotal;
        fila += `<tr>
                    <td>BALANCE TOTAL:</td>
                    <td></td>
                    <td style="color: ` + colorTotal + `">$${total}</td>
                </tr>`;
        $("#tblMovimientos tbody").html(fila);
    }
}

export async function traerMateriaMasComprada(){
    $.ajax({
        url: "../../api/movimiento/traerMateriaMasComprada",
        type: "GET",
        contentType: "application/x-www-form-urlencoded;charset=UTF-8",
        success: function (response){
            let data = JSON.parse(response);

            if(data.error){
                Swal.fire('', data.error, 'warning');
                ocultarCargando();
            } else if(data.exception){
                Swal.fire('', "Error interno del servidor. Intente mas tarde.", 'error');
                ocultarCargando();
            } else{
                document.getElementById("txtCompra").value = data.nombre;
            }
        }
    });
}

export async function traerProductoMasVendido(){
    $.ajax({
        url: "../../api/movimiento/traerProductoMasVendido",
        type: "GET",
        contentType: "application/x-www-form-urlencoded;charset=UTF-8",
        success: function (response){
            let data = JSON.parse(response);
            
            if(data.error){
                Swal.fire('', data.error, 'warning');
                ocultarCargando();
            } else if(data.exception){
                Swal.fire('', "Error interno del servidor. Intente mas tarde.", 'error');
                ocultarCargando();
            } else{
                document.getElementById("txtVenta").value = data.nombre;
            }
        }
    });
}