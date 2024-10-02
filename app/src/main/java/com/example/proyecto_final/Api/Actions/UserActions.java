package com.example.proyecto_final.Api.Actions;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.proyecto_final.Api.Actions.Interfaces.UserInterface;
import com.example.proyecto_final.Utilidades;
import com.example.proyecto_final.Webservice.PeticionesRed;
import com.example.proyecto_final.Webservice.WebService;
import com.example.proyecto_final.Webservice.modelo.Email;
import com.example.proyecto_final.Webservice.modelo.Usuario;
import com.example.proyecto_final.Webservice.respuestas.RespuestaUsuario;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class UserActions {

    public static void login(Context context, String email, String contreseña, String COLA_PETICIONES, PeticionesRed peticiones, final UserInterface callback){
        HashMap<String,String> parametros=new HashMap<>();
        parametros.put("email",email);
        parametros.put("contraseña", Utilidades.encriptaMD5(contreseña));

        String urlPeticion= WebService.URL_Usuarios+Utilidades.generaParametrosURL(parametros);

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, urlPeticion, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("falloo",response.toString());
                try{
                    Gson gson=new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                    RespuestaUsuario respuestaUsuario=gson.fromJson(response.toString(), RespuestaUsuario.class);
                    Usuario usuario=respuestaUsuario.data;
                    if(respuestaUsuario.data != null){
                        callback.onSuccesLogin(usuario);
                    }
                    else{
                        Log.e("falloo","email contraesña incorrectos.");
                        callback.onFailureLogin("Correo o contraseña incorrectos.");
                    }

                }
                catch (Exception e){
                    e.printStackTrace();
                    try {
                        String estado=response.getString(WebService.JSON.STATUS);
                        switch (estado){
                            case WebService.JSON.ERROR:
                                callback.onFailureLogin(response.getString(WebService.JSON.ERROR));
                                break;
                            case WebService.JSON.FAIL:
                               callback.onFailureLogin(response.getString(WebService.JSON.DATA));
                        }

                    } catch (JSONException ex) {
                        callback.onFailureLogin("Error al procesar la peticion de login.");
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
               callback.onFailureLogin("Error en la peticion JSON: ");

                Log.e("falloo",volleyError.getMessage()+" ");
            }
        });

        objectRequest.setTag(COLA_PETICIONES);
        peticiones.anhadirPeticionACola(objectRequest);
    }
    public static void create(String email,String contraseña,String nombre,String COLA_PETICIONES_PRUEBA,PeticionesRed peticion,final UserInterface callback){
        String urlPeticion= WebService.URL_Usuarios;
        JSONObject jsonNewUser;
        Usuario newUser;
        try {
            newUser=new Usuario(email, Utilidades.encriptaMD5(contraseña),nombre);
            Gson gson=new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            String textJsonUser=gson.toJson(newUser);
            jsonNewUser= new JSONObject(textJsonUser);
        } catch (JSONException e) {
            callback.onFailureCreate("Error al crear el json.");
            return;
        }

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, urlPeticion, jsonNewUser, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (jsonObject.length() == 0) {
                      callback.onFailureCreate("Sin datos.");
                    } else {
                        String estado = jsonObject.getString(WebService.JSON.STATUS);
                        switch (estado){
                            case WebService.JSON.SUCCESS:
                                callback.onSuccesCreate(newUser);
                                break;
                            case WebService.JSON.ERROR:
                                Log.e("fallo json","Error: "+jsonObject.getString(WebService.JSON.MESSAGE));
                                break;
                            case WebService.JSON.FAIL:
                               Log.e("fallo json","Fallo: "+jsonObject.getString(WebService.JSON.DATA));
                                break;
                        }

                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                   callback.onFailureCreate("Error al convertir el json a java.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callback.onFailureCreate("Error en la peticion: "+volleyError.toString());
            }
        });

        jsonObjectRequest.setTag(COLA_PETICIONES_PRUEBA);
        peticion.anhadirPeticionACola(jsonObjectRequest);

    }
    public static void reset_pass(String email, String COLA_PETICIONES_PRUEBA, PeticionesRed peticion, final UserInterface callback) {
        JSONObject jsonNewUser;
        Email email1;
        try {
            email1 = new Email(email);
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            String textJsonUser = gson.toJson(email1);
            jsonNewUser = new JSONObject(textJsonUser);
        } catch (JSONException e) {
            callback.onFailureReset("Error al crear el JSON.");
            Log.e("fallo", e.toString());
            return;
        }

        Log.e("fallo correo", jsonNewUser+" ");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebService.URL_Reset, jsonNewUser, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (jsonObject.length() == 0) {
                        callback.onFailureReset("Sin datos.");
                    } else {
                        String estado = jsonObject.getString(WebService.JSON.STATUS);
                        switch (estado) {
                            case WebService.JSON.SUCCESS:
                                callback.onSuccesReset(email1);
                                break;
                            case WebService.JSON.ERROR:
                                Log.e("fallo json", "Error: " + jsonObject.getString(WebService.JSON.MESSAGE));
                                callback.onFailureReset(jsonObject.getString(WebService.JSON.MESSAGE));
                                break;
                            case WebService.JSON.FAIL:
                                Log.e("fallo json", "Fallo: " + jsonObject.getString(WebService.JSON.DATA));
                                callback.onFailureReset(jsonObject.getString(WebService.JSON.DATA));
                                break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailureReset("Error al convertir el JSON a Java.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // Registro detallado del error
                Log.e("fallo volley", "Volley Error: " + volleyError.toString());
                Log.e("fallo volley", "Message: " + volleyError.getMessage());
                if (volleyError.networkResponse != null) {
                    Log.e("fallo volley", "Status Code: " + volleyError.networkResponse.statusCode);
                    Log.e("fallo volley", "Response Data: " + new String(volleyError.networkResponse.data));
                }
                callback.onFailureReset("Error en la solicitud: " + volleyError.getMessage());
            }
        });

        jsonObjectRequest.setTag(COLA_PETICIONES_PRUEBA);
        peticion.anhadirPeticionACola(jsonObjectRequest);
    }

    public static void getID(String COLA_PETICIONES, PeticionesRed peticionesRed, int id_usuario, TextView tv_usuario, final UserInterface callback){
        HashMap<String,String>parametros=new HashMap<>();
        parametros.put("id_usuario",String.valueOf(id_usuario));

        String url=WebService.URL_Usuarios+Utilidades.generaParametrosURL(parametros);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Gson gson=new GsonBuilder().create();
                RespuestaUsuario respuestaUsuario=gson.fromJson(jsonObject.toString(), RespuestaUsuario.class);
                Usuario usuario=respuestaUsuario.data;
                callback.onSuccesGetUsuarioID(usuario,tv_usuario);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callback.onFaileureGetUsuarioID(volleyError.toString());
            }
        });
        jsonObjectRequest.setTag(COLA_PETICIONES);
        peticionesRed.anhadirPeticionACola(jsonObjectRequest);
    }



}
