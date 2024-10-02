package com.example.proyecto_final.Api.Actions;


import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.proyecto_final.Api.Actions.Interfaces.ComentariosInterface;
import com.example.proyecto_final.Utilidades;
import com.example.proyecto_final.Webservice.PeticionesRed;
import com.example.proyecto_final.Webservice.WebService;
import com.example.proyecto_final.Webservice.modelo.Gimnasio;
import com.example.proyecto_final.Webservice.modelo.Gimnasio_comentarios;
import com.example.proyecto_final.Webservice.modelo.Usuario;
import com.example.proyecto_final.Webservice.respuestas.RespuestaListaGimnasioComentarios;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ComentariosActions {

    public static void getComentario(String orden,int id_gimnasio,PeticionesRed peticionesRed, String COLA_PETICIONES, final ComentariosInterface callback){

        HashMap<String,String>parametros=new HashMap<>();
        parametros.put("id_gimnasio",String.valueOf(id_gimnasio));
        parametros.put("orden",orden);
        String url= WebService.URL_Gimnasio_comentarios+ Utilidades.generaParametrosURL(parametros);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, jsonObject -> {
            Gson gson=new GsonBuilder().create();
            RespuestaListaGimnasioComentarios respuestaListaGimnasioComentarios=gson.fromJson(jsonObject.toString(), RespuestaListaGimnasioComentarios.class);

                List<Gimnasio_comentarios>lista=respuestaListaGimnasioComentarios.data;
                callback.onSuccesGetComentarios(lista);

        }, volleyError -> callback.onFaileureGetComentarios("Error: "+volleyError.getMessage()));
        jsonObjectRequest.setTag(COLA_PETICIONES);
        peticionesRed.anhadirPeticionACola(jsonObjectRequest);
    }

    public static void postComentario(int id_gimnasio,int id_usuario,String comentario,int valoracion,String COLA_PETICIONES,PeticionesRed peticionesRed,final ComentariosInterface callback){
        String urlPeticion= WebService.URL_Gimnasio_comentarios;
        JSONObject jsonNewCom;
        Gimnasio_comentarios newComentario;
        try {
            newComentario=new Gimnasio_comentarios(id_usuario,id_gimnasio,comentario,valoracion);
            Gson gson=new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            String textJsonUser=gson.toJson(newComentario);
            jsonNewCom= new JSONObject(textJsonUser);
        } catch (JSONException e) {
            callback.onFaileurePostComentario("Error al crear el json.");
            return;
        }

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, urlPeticion, jsonNewCom, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (jsonObject.length() == 0) {
                        callback.onFaileurePostComentario("Sin datos.");
                    } else {
                        String estado = jsonObject.getString(WebService.JSON.STATUS);
                        switch (estado){
                            case WebService.JSON.SUCCESS:
                                callback.onSuccesPostComentario(newComentario);
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
                    callback.onFaileurePostComentario("Error al convertir el json a java.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callback.onFaileurePostComentario("Error en la peticion: "+volleyError.toString()+" "+volleyError.getMessage());
                Log.e("josn Error","Error en la peticion: "+volleyError.toString()+" "+volleyError.getMessage());
            }
        });

        jsonObjectRequest.setTag(COLA_PETICIONES);
        peticionesRed.anhadirPeticionACola(jsonObjectRequest);
    }
    public static void deletecoment(String COLA_PETICIONES,PeticionesRed peticionesRed,int id_usuario,int id_comentario,final ComentariosInterface callback){

            HashMap<String,String>parametros=new HashMap<>();
            parametros.put("id_comentario",String.valueOf(id_comentario));
            parametros.put("id_usuario",String.valueOf(id_usuario));

            String url=WebService.URL_Gimnasio_comentarios+Utilidades.generaParametrosURL(parametros);
            JsonObjectRequest json=new JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    Log.e("falloo",jsonObject.toString());
                        callback.onSuccesDeleteComentario("Comentario Eliminado");

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.e("falloo",volleyError.getMessage());
                }
            });
            json.setTag(COLA_PETICIONES);
            peticionesRed.anhadirPeticionACola(json);
    }

}
