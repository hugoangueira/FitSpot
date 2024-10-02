package com.example.proyecto_final.Api.Actions;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.proyecto_final.Api.Actions.Interfaces.ActividadGimnasioInterface;
import com.example.proyecto_final.Api.Actions.Interfaces.GimnasioInterface;
import com.example.proyecto_final.Utilidades;
import com.example.proyecto_final.Webservice.PeticionesRed;
import com.example.proyecto_final.Webservice.WebService;
import com.example.proyecto_final.Webservice.modelo.Gimnasio;
import com.example.proyecto_final.Webservice.modelo.GimnasioItem;
import com.example.proyecto_final.Webservice.respuestas.RespuestaListaActGimnasio;
import com.example.proyecto_final.Webservice.respuestas.RespuestaListaGimnasios;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class GimnasioActions {

    public static void getGimnasio(PeticionesRed peticionesRed,String COLA_PETICIONES, final GimnasioInterface callback){

        JsonObjectRequest json = new JsonObjectRequest(Request.Method.GET, WebService.URL_Gimnasios, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject res) {
                        Gson gson = new GsonBuilder().create();
                        RespuestaListaGimnasios respuestaListaGimnasios = gson.fromJson(res.toString(), RespuestaListaGimnasios.class);
                        List<Gimnasio>listagimnasios = respuestaListaGimnasios.data;
                        callback.onSuccesGetGimnasio(listagimnasios);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        callback.onFailureGetGimnasio("Error en el json:" + volleyError.toString());
                    }
                }
        );

        json.setTag(COLA_PETICIONES);
        peticionesRed.anhadirPeticionACola(json);

    }
    public static void getGimnasioLocalidad(PeticionesRed peticionesRed,String COLA_PETICIONES, final GimnasioInterface callback){

        HashMap<String,String>parametros=new HashMap<>();
        parametros.put("localidad","localidad");

        String url=WebService.URL_Gimnasios+Utilidades.generaParametrosURL(parametros);

        JsonObjectRequest json = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject res) {
                        Gson gson = new GsonBuilder().create();
                        RespuestaListaGimnasios respuestaListaGimnasios = gson.fromJson(res.toString(), RespuestaListaGimnasios.class);
                        List<Gimnasio>listagimnasios = respuestaListaGimnasios.data;
                        Log.e("falloo lista",listagimnasios.get(0).getLocalidad());
                        callback.onSuccesGetGimnasioLocalidad(listagimnasios);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        callback.onFaileureGetGimnasioLocalidad("Error en el json:" + volleyError.toString());
                    }
                }
        );

        json.setTag(COLA_PETICIONES);
        peticionesRed.anhadirPeticionACola(json);

    }
    public static void getGimnasioID(int id_gimnasio,PeticionesRed peticionesRed,String COLA_PETICIONES, final GimnasioInterface callback){
        HashMap<String,String>parametros=new HashMap<>();
        parametros.put("id",String.valueOf(id_gimnasio));

        String url=WebService.URL_Gimnasios + Utilidades.generaParametrosURL(parametros);

        JsonObjectRequest json=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject res) {
                Gson gson=new GsonBuilder().create();
                Log.e("fallooo",res.toString());
                RespuestaListaGimnasios respuestaListaGimnasios= gson.fromJson(res.toString(), RespuestaListaGimnasios.class);
                List<Gimnasio>lista=respuestaListaGimnasios.data;
                callback.onSuccesGetGimnasioID(lista);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callback.onFaileureGimnasioID("Error: "+volleyError.getMessage());
            }
        });

        json.setTag(COLA_PETICIONES);
        peticionesRed.anhadirPeticionACola(json);

    }


    public static void getGimnasiosOrdenated(double latitud,double longitud,PeticionesRed peticionesRed,String COLA_PETICIONES, final GimnasioInterface callback){

        HashMap<String,String> parametros=new HashMap<>();
        parametros.put("latitud",String.valueOf(latitud));
        parametros.put("longitud",String.valueOf(longitud));

        String url=WebService.URL_Gimnasios + Utilidades.generaParametrosURL(parametros);

        JsonObjectRequest json = new JsonObjectRequest(Request.Method.GET,url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject res) {
                        Gson gson = new GsonBuilder().create();
                        RespuestaListaGimnasios respuestaListaGimnasios = gson.fromJson(res.toString(), RespuestaListaGimnasios.class);
                        Log.e("falloo",res.toString()+" as");
                        List<Gimnasio>listagimnasios = respuestaListaGimnasios.data;
                        callback.onSuccesGetGimnasiosOrdenated(listagimnasios);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        callback.onFailureGetGimnasiosOrdenated("Error en el json:" + volleyError.toString());
                    }
                }
        );

        json.setTag(COLA_PETICIONES);
        peticionesRed.anhadirPeticionACola(json);

    }
    public static void getGimnasioFiltrar3(String COLA_PETICIONES,PeticionesRed peticionesRed,String localidad,float latitud,float longitud,final GimnasioInterface callback){
        HashMap<String,String>parametros=new HashMap<>();
        parametros.put("localidad",localidad);
        parametros.put("latitud",String.valueOf(latitud));
        parametros.put("longitud",String.valueOf(longitud));

        String url=WebService.URL_Gimnasios+Utilidades.generaParametrosURL(parametros);

        Log.e("falloo lista","d");
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Gson gson=new GsonBuilder().create();
                Log.e("falloo",jsonObject.toString()+" ");
                RespuestaListaGimnasios respuestaListaActGimnasio=gson.fromJson(jsonObject.toString(), RespuestaListaGimnasios.class);
                Log.e("falloo",respuestaListaActGimnasio.data.size() +" ");
                List<Gimnasio>lista=respuestaListaActGimnasio.data;
                Log.e("falloo lista",respuestaListaActGimnasio.data.size()+"  ");
                callback.onSuccesGetFiltrarLocalidadMap(lista);

            }
        }, volleyError -> {
            callback.onFailureGetFiltrarLocalidadMap("Error: "+volleyError);
            Log.e("falloo lista","error: "+volleyError);
        });
        jsonObjectRequest.setTag(COLA_PETICIONES);
        peticionesRed.anhadirPeticionACola(jsonObjectRequest);
    }


}
