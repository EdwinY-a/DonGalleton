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
import org.utl.idgs.core.ControllerUsuario;
import org.utl.idgs.model.Usuario;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import java.util.Random;


@Path("log")
public class RESTUsuario {
    private static String CODE_VERIF = null;
    public static final String ACCOUNT_SID = "ACbf3cb0cc4b01773961bf588245fdfa3b";
    public static final String AUTH_TOKEN = "ce6d2af16e4af12567fa93e3cb2ad887";

    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }
    
    private static final Set<String> ALLOWED_IPS = new HashSet<>();
    static {
        ALLOWED_IPS.add("127.0.0.1"); 
        // ALLOWED_IPS.add("192.168.137.227"); 
    }
    
    @Path("verifCode")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public Response verifCode( @FormParam("codigo") String codigo) throws Exception {
        String out = null;
        String code = null;
        Gson gson = new Gson();
        
        try {
            System.out.println("CODIGOOO: "+CODE_VERIF);
            if (CODE_VERIF.equals(codigo)){
                System.out.println("CORRECTO");
                out = "{\"response\": \"Correcto\"}";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            out = "{\"exception\": \"" + ex.getMessage() + "\"}";
        }
        
       

        return Response.status(Response.Status.OK).header("Access-Control-Allow-Origin", "*").entity(out).build();
    }
    
    @Path("in")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public Response login(@Context HttpServletRequest request, @FormParam("datos") String datos) throws Exception {
        String out = null;
        Gson gson = new Gson();
        Usuario u = new Usuario();
        ControllerUsuario cu = new ControllerUsuario();
        String code = null;
        
        try {
            u = gson.fromJson(datos, Usuario.class);
            System.out.println("USUARIO:"+ u);
            if (u == null) {
                out = "{\"error\": \"Debe ingresar usuario y contraseña para continuar.\"}";
                return Response.status(Response.Status.OK).entity(out).build();
            }
            if (u.getNombreUsuario() == null || u.getNombreUsuario().isEmpty()) {
                out = "{\"error\": \"Ingrese su nombre de usuario para continuar.\"}";
                return Response.status(Response.Status.OK).entity(out).build();
            }
            if (u.getContrasenia() == null || u.getContrasenia().isEmpty()) {
                out = "{\"error\": \"Ingrese su contraseña para continuar.\"}";
                return Response.status(Response.Status.OK).entity(out).build();
            }
            CODE_VERIF = generarCodigo();
            u = cu.login(u.getNombreUsuario(), u.getContrasenia());
            System.out.println("TELEFONO2"+ u.getTelefono());
            //enviarMensajeTexto(u.getTelefono(), CODE_VERIF);

            if (u == null) {
                out = "{\"error\": \"Usuario no encontrado, revise su usuario y contraseña.\"}";
            } else {
                if (u.getEstatus() == 1) {
                                        
                    //Al objeto emp se ejecuta el metodo settoken donde crea el token
                    u.setLastToken();
                    System.out.println("Token "+u.getLastToken());
                    //Usamos el metodo generar token del controller para asignarle el token al usuario en la base de datos
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
            return Response.status(Response.Status.FORBIDDEN).entity("{\"error\": \"Acceso no autorizado desde esta dirección IP.\"}").build();
        }

        return Response.status(Response.Status.OK).header("Access-Control-Allow-Origin", "*").entity(out).build();
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
    public Response save(@FormParam("datosUsuario") @DefaultValue("") String datosUsuario)
    {
        String out = null;
        Gson gson = new Gson();
        Usuario user = null;
        ControllerUsuario cu = new ControllerUsuario();
        
        try 
        {
            user = gson.fromJson(datosUsuario, Usuario.class);
            if (user.getIdUsuario()== 0)
            {
                cu.insertarUsuario(user);
            }
            else
            {
                cu.actualizarUsuario(user);
            }
            out = gson.toJson(user);
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
    
    @Path("getUsuario")
    @POST 
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsuario(@FormParam("datoUsuario") @DefaultValue("") String datosUsuario){
        String out = null;
        Gson gson = new Gson();
        Usuario u = null;
        ControllerUsuario cu = new ControllerUsuario();
        try 
        {
            u = gson.fromJson(datosUsuario, Usuario.class);
            Usuario user = cu.getUsuario(u.getIdUsuario());
            out = gson.toJson(user);
        }
        catch (JsonParseException jpe)
        {
            jpe.printStackTrace();
            out = """
                  {"exception":"Formato JSON de Datos Incorrectos."}
                  """;
        }
        catch (Exception e)
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
    public Response delete(@FormParam("datosUsuario") @DefaultValue("") String datosUsuario)
    {
        String out = null;
        Gson gson = new Gson();
        Usuario u = null;
        ControllerUsuario cu = new ControllerUsuario();
        
        try 
        {
            u = gson.fromJson(datosUsuario, Usuario.class);
            cu.desactivarUsuario(u.getIdUsuario(),u.getLogsUser());
            out = gson.toJson(u);
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
    
    // Método para enviar mensajes de texto
    private void enviarMensajeTexto(String telefono, String mensaje) {
        Message message = Message.creator(
                new PhoneNumber(telefono), // Tu número de Twilio
                new PhoneNumber("+13392367606"), // Tu número de Twilio
                
                mensaje)
            .create();

        System.out.println("Mensaje SID: " + message.getSid());
    }
    
    private String generarCodigo() {
        Random random = new Random();
        int codigo = 100000 + random.nextInt(900000);
        return String.valueOf(codigo);
    }

}
