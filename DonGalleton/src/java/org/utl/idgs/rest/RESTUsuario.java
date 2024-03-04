package org.utl.idgs.rest;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import org.utl.idgs.core.ControllerUsuario;
import org.utl.idgs.model.Usuario;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.utl.idgs.core.ControllerMateriaPrima;
import org.utl.idgs.model.MateriaPrima;

@Path("log")
public class RESTUsuario {

    @Path("in")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public Response login(@FormParam("datos") @DefaultValue("") String datos) throws Exception {
        String out = null;
        Gson gson = new Gson();
        Usuario u = new Usuario();
        ControllerUsuario cu = new ControllerUsuario();

        try {
            u = gson.fromJson(datos, Usuario.class);

            if (u == null) {
                out = """
                      {"error": "Debe ingresar usuario y contraseña para continuar."}
                      """;
                return Response.status(Response.Status.OK).entity(out).build();
            }
            if (u.getNombreUsuario() == null || u.getNombreUsuario() == "") {
                out = """
                      {"error": "Ingrese su nombre de usuario para continuar."}
                      """;
                return Response.status(Response.Status.OK).entity(out).build();
            }
            if (u.getContrasenia() == null || u.getContrasenia() == "") {
                out = """
                      {"error": "Ingrese su contraseña para continuar."}
                      """;
                return Response.status(Response.Status.OK).entity(out).build();
            }

            u = cu.login(u.getNombreUsuario(), u.getContrasenia());

            if (u == null) {
                out = """
                      {"error": "Usuario no encontrado, revise su usuario y contraseña."}
                      """;
            } else {
                if (u.getEstatus() == 1) {
                    out = gson.toJson(u);
                } else {
                    out = """
                        {"error": "El usuario esta inactivo."}
                    """;
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            out = """
                  {"exception": "%s"}
                  """;
            out = String.format(out, ex.getMessage());
        }

        return Response.status(Response.Status.OK).entity(out).build();

    }

    @GET
    @Path("getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@QueryParam("filtro") @DefaultValue("") String filtro) {
        String out = null;
        ControllerUsuario cu = null;
        List<Usuario> u = null;
        try {
            cu = new ControllerUsuario();
            u = cu.getAll(filtro);
            out = new Gson().toJson(u);
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"exception\":\"Error interno del servidor.\"}";
        }
        return Response.status(Response.Status.OK).header("Access-Control-Allow-Origin", "*").entity(out).build();

    }
//@Path("log")
//public class RESTUsuario {
////    private static final Set<String> ALLOWED_IPS = new HashSet<>();
////    static {
////        //ALLOWED_IPS.add("192.168.137.196"); //Jorge
////        //ALLOWED_IPS.add("192.168.137.227"); //Diego
////    }
////
////    @Path("in")
////    @Produces(MediaType.APPLICATION_JSON)
////    @POST
////    public Response login(@Context HttpServletRequest request, @FormParam("datos") String datos) throws Exception {
////        String out = null;
////        Gson gson = new Gson();
////        Usuario u = new Usuario();
////        ControllerUsuario cu = new ControllerUsuario();
////        
////        try {
////            u = gson.fromJson(datos, Usuario.class);
////
////            if (u == null) {
////                out = "{\"error\": \"Debe ingresar usuario y contraseña para continuar.\"}";
////                return Response.status(Response.Status.OK).entity(out).build();
////            }
////            if (u.getNombreUsuario() == null || u.getNombreUsuario().isEmpty()) {
////                out = "{\"error\": \"Ingrese su nombre de usuario para continuar.\"}";
////                return Response.status(Response.Status.OK).entity(out).build();
////            }
////            if (u.getContrasenia() == null || u.getContrasenia().isEmpty()) {
////                out = "{\"error\": \"Ingrese su contraseña para continuar.\"}";
////                return Response.status(Response.Status.OK).entity(out).build();
////            }
////
////            u = cu.login(u.getNombreUsuario(), u.getContrasenia());
////
////            if (u == null) {
////                out = "{\"error\": \"Usuario no encontrado, revise su usuario y contraseña.\"}";
////            } else {
////                if (u.getEstatus() == 1) {
////                    out = gson.toJson(u);
////                } else {
////                    out = "{\"error\": \"El usuario está inactivo.\"}";
////                }
////            }
////        } catch (Exception ex) {
////            ex.printStackTrace();
////            out = "{\"exception\": \"" + ex.getMessage() + "\"}";
////        }
////        
////        String clientIP = request.getRemoteAddr();
////        if (!ALLOWED_IPS.contains(clientIP)) {
////            return Response.status(Response.Status.FORBIDDEN).entity("{\"error\": \"Acceso no autorizado desde esta dirección IP.\"}").build();
////        }
//
//        return Response.status(Response.Status.OK).header("Access-Control-Allow-Origin", "*").entity(out).build();
//    }
}