
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
import org.utl.idgs.core.ControllerMateriaPrima;
import org.utl.idgs.core.ControllerProducto;
import org.utl.idgs.model.MateriaPrima;
import org.utl.idgs.model.Producto;

/**
 *
 * @author Alda
 */
@Path("mprima")
public class RESTMateriaPrima {
    @GET
    @Path("getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@QueryParam("filtro") @DefaultValue("") String filtro) {
        String out = null;
        ControllerMateriaPrima cm = null;
        List<MateriaPrima> p = null;
        try {
            cm = new ControllerMateriaPrima();
            p = cm.getAll(filtro);
            out = new Gson().toJson(p);
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"exception\":\"Error interno del servidor.\"}";
        }
        return Response.status(Response.Status.OK).header("Access-Control-Allow-Origin", "*").entity(out).build();
        
    }
    
    @Path("getProducto")
    @POST 
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProducto(@FormParam("datoMateriaPrima") @DefaultValue("") String datoMateriaPrima){
        String out = null;
        Gson gson = new Gson();
        MateriaPrima mp = null;
        ControllerMateriaPrima cmp = new ControllerMateriaPrima();
        try 
        {
            mp = gson.fromJson(datoMateriaPrima, MateriaPrima.class);
            MateriaPrima pmp = cmp.getProducto(mp.getIdMateriaPrima());
            out = gson.toJson(pmp);
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
    
    @Path("save")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(@FormParam("datosMateriaPrima") @DefaultValue("") String datosMateriaPrima)
    {
        String out = null;
        Gson gson = new Gson();
        MateriaPrima materia = null;
        ControllerMateriaPrima cmp = new ControllerMateriaPrima();
        
        try 
        {
            materia = gson.fromJson(datosMateriaPrima, MateriaPrima.class);
            if (materia.getIdMateriaPrima()== 0)
            {
                cmp.insertarMateriaPrima(materia);
            }
            else
            {
                cmp.actualizarMateriaPrima(materia);
            }
            out = gson.toJson(materia);
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
    
    @Path("delete")
    @POST 
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@FormParam("datosMateriaPrima") @DefaultValue("") String datosMateriaPrima)
    {
        String out = null;
        Gson gson = new Gson();
        MateriaPrima mp = null;
        ControllerMateriaPrima cmp = new ControllerMateriaPrima();
        
        try 
        {
            mp = gson.fromJson(datosMateriaPrima, MateriaPrima.class);
            cmp.eliminarMateriaPrima(mp.getIdMateriaPrima());
            out = gson.toJson(mp);
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
}
