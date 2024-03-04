
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
import org.utl.idgs.model.CrearProducto;
import org.utl.idgs.model.MateriaPrima;
import org.utl.idgs.model.Producto;

/**
 *
 * @author jorgemorales
 */
@Path("producto")
public class RESTProducto {
    @Path("save")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(@FormParam("datosProducto") @DefaultValue("") String datosProducto)
    {
        String out = null;
        Gson gson = new Gson();
        Producto prod = null;
        ControllerProducto cp = new ControllerProducto();
        
        try 
        {
            prod = gson.fromJson(datosProducto, Producto.class);
            if (prod.getIdProducto()== 0)
            {
                cp.insertarProducto(prod);
            }
            else
            {
                cp.actualizarProducto(prod);
            }
            out = gson.toJson(prod);
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
    
    @Path("guardarCrearProducto")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response guardarCrearProducto(@FormParam("datosCrearProducto") @DefaultValue("") String datosCrearProducto)
    {
        String out = null;
        Gson gson = new Gson();
        CrearProducto prod = null;
        ControllerProducto cp = new ControllerProducto();
        
        try 
        {
            prod = gson.fromJson(datosCrearProducto, CrearProducto.class);
            if (prod != null)
            {
                cp.insertarCrearProducto(prod);
            }
            
            out = gson.toJson(prod);
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
    
    @Path("actualizarMateria")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarMateria(@FormParam("datosMateria") @DefaultValue("") String datosMateria)
    {
        String out = null;
        Gson gson = new Gson();
        MateriaPrima mp = null;
        ControllerProducto cp = new ControllerProducto();
        
        try 
        {
            mp = gson.fromJson(datosMateria, MateriaPrima.class);
            if (mp != null)
            {
                cp.actualizarMateriaPrima(mp);
            }
            
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
    
    @GET
    @Path("getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@QueryParam("filtro") @DefaultValue("") String filtro) {
        String out = null;
        ControllerProducto cp = null;
        List<Producto> p = null;
        try {
            cp = new ControllerProducto();
            p = cp.getAll(filtro);
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
    public Response getProducto(@FormParam("datoProducto") @DefaultValue("") String datoProducto){
        String out = null;
        Gson gson = new Gson();
        Producto p = null;
        ControllerProducto cp = new ControllerProducto();
        try 
        {
            p = gson.fromJson(datoProducto, Producto.class);
            Producto pro = cp.getProducto(p.getIdProducto());
            out = gson.toJson(pro);
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
    public Response delete(@FormParam("datosProducto") @DefaultValue("") String datosProducto)
    {
        String out = null;
        Gson gson = new Gson();
        Producto p = null;
        ControllerProducto cp = new ControllerProducto();
        
        try 
        {
            p = gson.fromJson(datosProducto, Producto.class);
            cp.eliminarProducto(p.getIdProducto());
            out = gson.toJson(p);
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
    
    @Path("getProductoCreado")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductoCreado(@QueryParam("datosProducto") @DefaultValue("0") int datosProducto){
        System.out.println("REST creado: "+datosProducto);
        String out = null;
        Gson gson = new Gson();
        List<CrearProducto> p = null;
        ControllerProducto cp = new ControllerProducto();
        try 
        {
            //p = gson.fromJson(datosProducto, CrearProducto.class);
            p = cp.getProductoCreado(datosProducto);
            out = gson.toJson(p);
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
    @Path("getAllIngredientes")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllIngredientes(@QueryParam("filtro") @DefaultValue("") String filtro) {
        String out = null;
        ControllerProducto cp = null;
        List<MateriaPrima> mp = null;
        try {
            cp = new ControllerProducto();
            mp = cp.getAllIngredientes(filtro);
            out = new Gson().toJson(mp);
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"exception\":\"Error interno del servidor.\"}";
        }
        return Response.status(Response.Status.OK).header("Access-Control-Allow-Origin", "*").entity(out).build();
        
    }
}
