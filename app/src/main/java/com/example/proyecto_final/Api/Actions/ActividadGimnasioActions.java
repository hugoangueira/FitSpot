package com.example.proyecto_final.Api.Actions;

import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.proyecto_final.Api.Actions.Interfaces.ActividadGimnasioInterface;
import com.example.proyecto_final.Utilidades;
import com.example.proyecto_final.Webservice.PeticionesRed;
import com.example.proyecto_final.Webservice.WebService;
import com.example.proyecto_final.Webservice.modelo.Actividad;
import com.example.proyecto_final.Webservice.respuestas.RespuestaListaActGimnasio;
import com.example.proyecto_final.Webservice.respuestas.RespuestaListaActividades;
import com.example.proyecto_final.Webservice.respuestas.RespuestaListaGimnasioActividad;
import com.example.proyecto_final.Webservice.respuestas.RespuestaListaGimnasios;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import javax.xml.xpath.XPathFunctionResolver;

public class ActividadGimnasioActions {

    public static void getGimnasioFiltrar(String COLA_PETICIONES,PeticionesRed peticionesRed,String actividad,String precio,double latitud,double longitud,final ActividadGimnasioInterface callback){
        HashMap<String,String>parametros=new HashMap<>();
        parametros.put("nombre",actividad);
        parametros.put("precio",precio);
        parametros.put("latitud",String.valueOf(latitud));
        parametros.put("longitud",String.valueOf(longitud));

        String url=WebService.URL_Gimnasio_Actividad+Utilidades.generaParametrosURL(parametros);

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e("falloo",jsonObject.toString());
                Gson gson=new GsonBuilder().create();
                RespuestaListaActGimnasio respuestaListaActGimnasio=gson.fromJson(jsonObject.toString(), RespuestaListaActGimnasio.class);
                if(respuestaListaActGimnasio.data !=null){
                    callback.onSuccesGetFiltrarActividadPrecio(respuestaListaActGimnasio.data,actividad);
                }
                else{
                    callback.onFailureGetFiltrarActividadPrecio("No hay ningun gimnasio para ese filtro.");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callback.onFailureGetFiltrarActividadPrecio("Error: "+volleyError);
            }
        });
        jsonObjectRequest.setTag(COLA_PETICIONES);
        peticionesRed.anhadirPeticionACola(jsonObjectRequest);
    }
    public static void getGimnasioFiltrarLocalidad(String COLA_PETICIONES,PeticionesRed peticionesRed,String localidad,double latitud,double longitud,final ActividadGimnasioInterface callback){
        HashMap<String,String>parametros=new HashMap<>();
        parametros.put("localidad",localidad);
        parametros.put("latitud",String.valueOf(latitud));
        parametros.put("longitud",String.valueOf(longitud));

        String url=WebService.URL_Gimnasio_Actividad+Utilidades.generaParametrosURL(parametros);

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Gson gson=new GsonBuilder().create();
                RespuestaListaActGimnasio respuestaListaActGimnasio=gson.fromJson(jsonObject.toString(), RespuestaListaActGimnasio.class);
                if(respuestaListaActGimnasio.data !=null){
                    callback.onSuccesGetFiltrarLocalidad(respuestaListaActGimnasio.data,localidad);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callback.onFailureGetFiltrarActividadPrecio("Error: "+volleyError);
            }
        });
        jsonObjectRequest.setTag(COLA_PETICIONES);
        peticionesRed.anhadirPeticionACola(jsonObjectRequest);
    }
    public static void getGimnasioFiltrarLocalidadActividad(String COLA_PETICIONES,PeticionesRed peticionesRed,String actividad,String localidad,String precio,double latitud,double longitud,final ActividadGimnasioInterface callback){
        HashMap<String,String>parametros=new HashMap<>();
        parametros.put("nombre",actividad);
        parametros.put("localidad",localidad);
        parametros.put("precio",precio);
        parametros.put("latitud",String.valueOf(latitud));
        parametros.put("longitud",String.valueOf(longitud));

        String url=WebService.URL_Gimnasio_Actividad+Utilidades.generaParametrosURL(parametros);

        JsonObjectRequest json=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Gson gson=new GsonBuilder().create();
                RespuestaListaActGimnasio respuestaListaActGimnasio=gson.fromJson(jsonObject.toString(), RespuestaListaActGimnasio.class);
                callback.onSuccesGetFiltrarLocalidadActividad(respuestaListaActGimnasio.data,localidad,actividad);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callback.onFaileureGetFiltrarLocalidadActividad(volleyError.getMessage());
            }
        });

        json.setTag(COLA_PETICIONES);
        peticionesRed.anhadirPeticionACola(json);

    }


    public static void getGimnasioFiltrar2(String COLA_PETICIONES,PeticionesRed peticionesRed,String actividad,double latitud,double longitud,final ActividadGimnasioInterface callback){
        HashMap<String,String>parametros=new HashMap<>();
        parametros.put("nombre",actividad);
        parametros.put("latitud",String.valueOf(latitud));
        parametros.put("longitud",String.valueOf(longitud));

        String url=WebService.URL_Gimnasio_Actividad+Utilidades.generaParametrosURL(parametros);


        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Gson gson=new GsonBuilder().create();
                Log.e("falloo",jsonObject.toString());
                RespuestaListaActGimnasio respuestaListaActGimnasio=gson.fromJson(jsonObject.toString(), RespuestaListaActGimnasio.class);
                if(respuestaListaActGimnasio.data !=null){
                    callback.onSuccesGetFiltrarActividad(respuestaListaActGimnasio.data,actividad);
                }

            }
        }, volleyError -> {
            callback.onFailureGetFiltrarActividad("Error: "+volleyError);
            Log.e("falloo","error: "+volleyError.getMessage());
        });
        jsonObjectRequest.setTag(COLA_PETICIONES);
        peticionesRed.anhadirPeticionACola(jsonObjectRequest);
    }
    public static void getGimnasioFiltrar4(String COLA_PETICIONES,PeticionesRed peticionesRed,String localidad,String actividad,final ActividadGimnasioInterface callback){
        HashMap<String,String>parametros=new HashMap<>();
        parametros.put("localidad",localidad);
        parametros.put("nombre",actividad);

        String url=WebService.URL_Gimnasio_Actividad+Utilidades.generaParametrosURL(parametros);

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Gson gson=new GsonBuilder().create();
                RespuestaListaActGimnasio respuestaListaActGimnasio=gson.fromJson(jsonObject.toString(), RespuestaListaActGimnasio.class);
                if(respuestaListaActGimnasio.data !=null){
                    callback.onSuccesGetFiltrarLocalidadActividadMap(respuestaListaActGimnasio.data);
                }

            }
        }, volleyError -> {
            callback.onFaileureGetFiltrarLocalidadActividadMAp("Error: "+volleyError);
        });
        jsonObjectRequest.setTag(COLA_PETICIONES);
        peticionesRed.anhadirPeticionACola(jsonObjectRequest);
    }



}
