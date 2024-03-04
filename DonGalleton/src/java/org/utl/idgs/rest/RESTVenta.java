
package org.utl.idgs.rest;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.utl.idgs.core.ControllerProducto;
import org.utl.idgs.core.ControllerVenta;
import org.utl.idgs.model.Producto;
import org.utl.idgs.model.Venta;

/**
 *
 * @author Alda
 */
@Path("venta")
public class RESTVenta {
    @Path("save")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(@FormParam("datosVenta") @DefaultValue("") String datosVenta)
    {
        String out = null;
        Gson gson = new Gson();
        Venta ven = null;
        ControllerVenta cv = new ControllerVenta();
        
        try 
        {
            ven = gson.fromJson(datosVenta, Venta.class);
            if (ven.getIdVenta()== 0)
            {
                cv.insertarVenta(ven);
            }
            else
            {
                cv.actualizarVenta(ven);
            }
            out = gson.toJson(ven);
        }
        catch (JsonParseException jpe)
        {
            jpe.printStackTrace();
            out = """
                  {"exception":"Formato JSON de Datos Incorrectos."}
                  """;
        }
        catch (Exception e) //Cualquier otra excpetion
        {
            e.printStackTrace();
            out ="""
                 {"exception":"%s"}
                 """;
            out = String.format(out, e.toString());
        }
        return Response.status(Response.Status.OK).entity(out).build();
    }
    
    @GET
    @Path("getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@QueryParam("filtro") @DefaultValue("") String filtro) {
        String out = null;
        ControllerVenta cv = null;
        List<Venta> v = null;
        try {
            cv = new ControllerVenta();
            v = cv.getAll(filtro);
            out = new Gson().toJson(v);
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"exception\":\"Error interno del servidor.\"}";
        }
        return Response.status(Response.Status.OK).header("Access-Control-Allow-Origin", "*").entity(out).build();
        
    }
}
