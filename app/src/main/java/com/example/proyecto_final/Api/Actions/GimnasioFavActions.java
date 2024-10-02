package com.example.proyecto_final.Api.Actions;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.proyecto_final.Api.Actions.Interfaces.GimnasioFavInterface;
import com.example.proyecto_final.Utilidades;
import com.example.proyecto_final.Webservice.PeticionesRed;
import com.example.proyecto_final.Webservice.WebService;
import com.example.proyecto_final.Webservice.modelo.Gimnasio_fav;
import com.example.proyecto_final.Webservice.respuestas.RespuestaListaGimnasioFav;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.List;

public class GimnasioFavActions {

    public static void getGimnasioFav(int id_usuario,PeticionesRed peticionesRed, String COLA_PETICIONES, final GimnasioFavInterface callback){

        HashMap<String,String> parametros= new HashMap<>();
        parametros.put("id_usuario",String.valueOf(id_usuario));
        String url= WebService.URL_Gimnasio_fav + Utilidades.generaParametrosURL(parametros);

        JsonObjectRequest json=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Gson gson= new GsonBuilder().create();
                RespuestaListaGimnasioFav respuestaListaGimnasioFav=gson.fromJson(jsonObject.toString(), RespuestaListaGimnasioFav.class);
                List<Gimnasio_fav> lista=respuestaListaGimnasioFav.data;
                callback.onSuccesGetFav(lista);


            }
        }, volleyError -> callback.onFaileureGetFav("Error: "+volleyError.getMessage()));

        json.setTag(COLA_PETICIONES);
        peticionesRed.anhadirPeticionACola(json);
    }
    public static void postGimnasioFav(int id_usuario,int id_gimnasio, PeticionesRed peticionesRed,String COLA_PETICIONES, final GimnasioFavInterface callback){
        JSONObject jsonAdd;
        Gimnasio_fav gimnasio_fav;

        try {
        gimnasio_fav=new Gimnasio_fav(id_gimnasio,id_usuario);
        Gson gson=new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        String textojson=gson.toJson(gimnasio_fav);
        jsonAdd=new JSONObject(textojson);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest json=new JsonObjectRequest(Request.Method.POST, WebService.URL_Gimnasio_fav, jsonAdd, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                if(jsonObject.length() == 0){
                    callback.onFaileurePostFav("El json esta vacio.");
                }
                else{
                    try {
                        String status=jsonObject.getString(WebService.JSON.STATUS);
                        switch (status){
                            case WebService.JSON.SUCCESS:
                                Log.e("fav añadido",gimnasio_fav.getId_gimnasio()+" "+gimnasio_fav.getId_usuario());
                                callback.onSuccesPostFav(gimnasio_fav);
                                break;
                            case WebService.JSON.ERROR:
                                Log.e("fallo json","Error: "+jsonObject.getString(WebService.JSON.MESSAGE));
                                break;
                            case WebService.JSON.FAIL:
                                Log.e("fallo json","Fallo: "+jsonObject.getString(WebService.JSON.DATA));
                                break;
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callback.onFaileurePostFav("Error: "+volleyError.getMessage());
            }
        });

        json.setTag(COLA_PETICIONES);
        peticionesRed.anhadirPeticionACola(json);
    }
    public static void deleteGimnasioFav(int id_usuario,int id_gimnasio,PeticionesRed peticionesRed,String COLA_PETICIONES,final GimnasioFavInterface callback){
        HashMap<String,String>parametros=new HashMap<>();
        parametros.put("id_gimnasio",String.valueOf(id_gimnasio));
        parametros.put("id_usuario",String.valueOf(id_usuario));

        String url=WebService.URL_Gimnasio_fav+Utilidades.generaParametrosURL(parametros);

        JsonObjectRequest json=new JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if(jsonObject.length()>0){
                        String status=jsonObject.getString(WebService.JSON.STATUS);
                        switch (status){
                            case WebService.JSON.SUCCESS:
                                callback.onSuccesDeleteFav(new Gimnasio_fav(id_gimnasio,id_usuario));
                                break;
                            case WebService.JSON.ERROR:
                                callback.onFaileureDeleteFav("Error: "+jsonObject.getString(WebService.JSON.MESSAGE));
                                break;
                            case WebService.JSON.FAIL:
                                callback.onFaileureDeleteFav("Error: "+jsonObject.getString(WebService.JSON.DATA));
                                break;
                        }
                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callback.onFaileureDeleteFav("Error: "+volleyError.getMessage());
            }
        });
        json.setTag(COLA_PETICIONES);
        peticionesRed.anhadirPeticionACola(json);
    }


}
