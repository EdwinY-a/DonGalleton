/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
import org.utl.idgs.core.ControllerDetalleVenta;
import org.utl.idgs.core.ControllerVenta;
import org.utl.idgs.model.DetalleVenta;
import org.utl.idgs.model.Venta;

/**
 *
 * @author DiegoJC
 */
@Path("detalleventa")
public class RESTDetalleVenta {
    @Path("save")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(@FormParam("datosDetalleVenta") @DefaultValue("") String datosDetalleVenta)
    {
        String out = null;
        Gson gson = new Gson();
        DetalleVenta deven = null;
        ControllerDetalleVenta cdv = new ControllerDetalleVenta();
        
        try 
        {
            deven = gson.fromJson(datosDetalleVenta, DetalleVenta.class);
            if (deven.getIdDetalleVenta()== 0)
            {
                cdv.insertarDetalleVenta(deven);
            }
            else
            {
                cdv.actualizarDetalleVenta(deven);
            }
            out = gson.toJson(deven);
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
        ControllerDetalleVenta cdv = null;
        List<DetalleVenta> dv = null;
        try {
            cdv = new ControllerDetalleVenta();
            dv = cdv.getAll(filtro);
            out = new Gson().toJson(dv);
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"exception\":\"Error interno del servidor.\"}";
        }
        return Response.status(Response.Status.OK).header("Access-Control-Allow-Origin", "*").entity(out).build();
        
    }
}
