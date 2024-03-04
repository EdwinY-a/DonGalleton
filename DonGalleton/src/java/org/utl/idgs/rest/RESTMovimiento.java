
package org.utl.idgs.rest;

import com.google.gson.Gson;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import org.utl.idgs.core.ControllerMovimiento;
import org.utl.idgs.model.Movimiento;
import org.utl.idgs.model.Resultado;

/**
 *
 * @author Alda
 */
@Path("movimiento")
public class RESTMovimiento {
    @Path("buscar")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Response buscar(@QueryParam("desde") @DefaultValue("") String desde,
                           @QueryParam("hasta") @DefaultValue("") String hasta) throws Exception{
        String out = null;
        Gson gson = new Gson();
        List<Movimiento> lm = new ArrayList<>();
        ControllerMovimiento cm = new ControllerMovimiento();
        
        try{
            if((desde != null || desde != "") && (hasta != null || hasta != "")){System.out.println("desde:" +desde);
                lm = cm.getMovimientos(desde, hasta);

                out = gson.toJson(lm);
            } else{
                out = """
                      {"error": "Debes llenar las fechas para realizar la busqueda."};
                      """;
            }
        } catch(Exception ex){
            ex.printStackTrace();
            out = """
                  {"exception": "%s"}
                  """;
            out = String.format(out, ex.getMessage());
        }
        return Response.status(Response.Status.OK).entity(out).build();
    }
    
    @Path("traerMateriaMasComprada")
    @Produces
    @GET
    public Response traerMateriaMasComprada(@QueryParam("nada") @DefaultValue("") String nada) throws Exception{
        String out = null;
        Gson gson = new Gson();
        Resultado r = new Resultado();
        ControllerMovimiento cm = new ControllerMovimiento();
        
        try{
            r = cm.getMateriaMasComprada();
            
            out = gson.toJson(r);
        } catch(Exception ex){
            ex.printStackTrace();
            out = """
                  {"exception": "%s"}
                  """;
            out = String.format(out, ex.getMessage());
        }
        return Response.status(Response.Status.OK).entity(out).build();
    }
    
    @Path("traerProductoMasVendido")
    @Produces
    @GET
    public Response traerProductoMasVendido(@QueryParam("nada") @DefaultValue("") String nada) throws Exception{
        String out = null;
        Gson gson = new Gson();
        Resultado r = new Resultado();
        ControllerMovimiento cm = new ControllerMovimiento();
        
        try{
            r = cm.getProductoMasVendido();
            
            out = gson.toJson(r);
        } catch(Exception ex){
            ex.printStackTrace();
            out = """
                  {"exception": "%s"}
                  """;
            out = String.format(out, ex.getMessage());
        }
        return Response.status(Response.Status.OK).entity(out).build();
    }
}
