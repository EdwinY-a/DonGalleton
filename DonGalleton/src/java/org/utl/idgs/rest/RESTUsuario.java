package org.utl.idgs.rest;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
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
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.utl.idgs.core.ControllerUsuario;
import org.utl.idgs.model.Usuario;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Path("log")
public class RESTUsuario {

    private static final Set<String> ALLOWED_IPS = new HashSet<>();

    static {
        ALLOWED_IPS.add("127.0.0.1");
        //ALLOWED_IPS.add("192.168.137.72");
    }

    private static String encrypt(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Path("in")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public Response login(@Context HttpServletRequest request, @FormParam("datos") String datos) throws Exception {
        String out = null;
        Gson gson = new Gson();
        Usuario u = new Usuario();
        ControllerUsuario cu = new ControllerUsuario();
        String textoEncriptado = "";

        try {
            u = gson.fromJson(datos, Usuario.class);

            if (u == null) {
                out = "{\"error\": \"Debe ingresar usuario y contraseña para continuar.\"}";
                return Response.status(Response.Status.OK).entity(out).build();
            }
            if (u.getNombreUsuario() == null || u.getNombreUsuario().isEmpty()) {
                out = "{\"error\": \"Ingrese su nombre de usuario para continuar.\"}";
                return Response.status(Response.Status.OK).entity(out).build();
            }
            if (encrypt(u.getContrasenia()) == null || encrypt(u.getContrasenia()).isEmpty()) {
                out = "{\"error\": \"Ingrese su contraseña para continuar.\"}";
                return Response.status(Response.Status.OK).entity(out).build();
            }

            System.out.println("CONTRASEÑA ------ ---" + encrypt(u.getContrasenia()) + " ------ ----------");
            textoEncriptado = encrypt(u.getContrasenia());
            u = cu.login(u.getNombreUsuario(), textoEncriptado);

            if (u == null) {
                out = "{\"error\": \"Usuario no encontrado, revise su usuario y contraseña.\"}";
            } else {
                if (u.getEstatus() == 1) {
                    u.setLastToken();
                    System.out.println("Token " + u.getLastToken());
                    cu.generarToken(u.getIdUsuario(), u.getLastToken());
                    out = gson.toJson(u);
                } else {
                    out = "{\"error\": \"El usuario está inactivo.\"}";
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            out = "{\"exception\": \"" + ex.getMessage() + "\"}";
        }

        String clientIP = request.getRemoteAddr();
        if (!ALLOWED_IPS.contains(clientIP)) {
            registrarEventoSospechoso("Intento de acceso desde IP no autorizada: " + clientIP);
            return Response.status(Response.Status.FORBIDDEN).entity("{\"error\": \"Acceso no autorizado desde esta dirección IP.\"}").build();
        }

        return Response.status(Response.Status.OK).header("Access-Control-Allow-Origin", "*").entity(out).build();
    }

    private void registrarEventoSospechoso(String evento) {
        System.out.println("EVENTO------------------ " + evento + " ------------------");

        // Ruta completa del archivo en el que deseas escribir los eventos
        String rutaArchivo = "C:\\Users\\Usuario\\Desktop\\DonGalleton\\DonGalleton\\src\\java\\org\\utl\\idgs\\rest\\eventos_sospechosos.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo, true))) {
            writer.write(evento);
            writer.newLine();
            System.out.println("Datos insertados correctamente en el archivo.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al escribir en el archivo.");
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("out")
    public Response logOut(@FormParam("usuario") @DefaultValue("") String u) throws Exception {
        String out = null;
        Usuario usu = null;
        ControllerUsuario cu = new ControllerUsuario();
        Gson gson = new Gson();
        usu = gson.fromJson(u, Usuario.class);

        try {
            cu = new ControllerUsuario();

            if (cu.eliminarToken(usu)) {
                out = "{\"ok\":\"Eliminación de Token Correcta\"}";
            } else {
                out = "{\"error\":\"Eliminación de token no realizada\"}";
            }

        } catch (JsonParseException jpe) {
            out = "{\"error\":\"Eliminación no concedida\"}";
            jpe.printStackTrace();
        }

        return Response.status(Response.Status.OK).entity(out).build();
    }

    @Path("save")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(@FormParam("datosUsuario") @DefaultValue("") String datosUsuario) {
        String out = null;
        Gson gson = new Gson();
        Usuario user = null;
        ControllerUsuario cu = new ControllerUsuario();

        try {
            user = gson.fromJson(datosUsuario, Usuario.class);
            if (user.getIdUsuario() == 0) {
                cu.insertarUsuario(user);
            } else {
                cu.actualizarUsuario(user);
            }
            out = gson.toJson(user);
        } catch (JsonParseException jpe) {
            jpe.printStackTrace();
            out = """
                  {"exception":"Formato JSON de Datos Incorrectos."}
                  """;
        } catch (Exception e) //Cualquier otra excpetion
        {
            e.printStackTrace();
            out = """
                 {"exception":"%s"}
                 """;
            out = String.format(out, e.toString());
        }
        return Response.status(Response.Status.OK).entity(out).build();
    }

    @Path("getUsuario")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsuario(@FormParam("datoUsuario") @DefaultValue("") String datosUsuario) {
        String out = null;
        Gson gson = new Gson();
        Usuario u = null;
        ControllerUsuario cu = new ControllerUsuario();
        try {
            u = gson.fromJson(datosUsuario, Usuario.class);
            Usuario user = cu.getUsuario(u.getIdUsuario());
            out = gson.toJson(user);
        } catch (JsonParseException jpe) {
            jpe.printStackTrace();
            out = """
                  {"exception":"Formato JSON de Datos Incorrectos."}
                  """;
        } catch (Exception e) {
            e.printStackTrace();
            out = """
                 {"exception":"%s"}
                 """;
            out = String.format(out, e.toString());
        }
        return Response.status(Response.Status.OK).entity(out).build();
    }

    @Path("delete")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@FormParam("datosUsuario") @DefaultValue("") String datosUsuario) {
        String out = null;
        Gson gson = new Gson();
        Usuario u = null;
        ControllerUsuario cu = new ControllerUsuario();

        try {
            u = gson.fromJson(datosUsuario, Usuario.class);
            cu.desactivarUsuario(u.getIdUsuario(), u.getLogsUser());
            out = gson.toJson(u);
        } catch (JsonParseException jpe) {
            jpe.printStackTrace();
            out = """
                  {"exception":"Formato JSON de Datos Incorrectos."}
                  """;
        } catch (Exception e) //Cualquier otra excpetion
        {
            e.printStackTrace();
            out = """
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

}
