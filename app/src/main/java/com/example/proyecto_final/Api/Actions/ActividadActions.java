package com.example.proyecto_final.Api.Actions;

import android.content.Context;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.proyecto_final.Api.Actions.Interfaces.Actividadinterface;
import com.example.proyecto_final.Utilidades;
import com.example.proyecto_final.Webservice.PeticionesRed;
import com.example.proyecto_final.Webservice.WebService;
import com.example.proyecto_final.Webservice.modelo.Actividad;
import com.example.proyecto_final.Webservice.respuestas.RespuestaListaActGimnasio;
import com.example.proyecto_final.Webservice.respuestas.RespuestaListaActividades;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActividadActions {

    public static void getItemGimnasio(TextView tv, int id, String COLA_PETICIONES, PeticionesRed peticionesRed, final Actividadinterface callback){

        HashMap<String,String>parametros=new HashMap<>();
        parametros.put("id_gimnasio",String.valueOf(id));

        String url_peticion=WebService.URL_Actividades+ Utilidades.generaParametrosURL(parametros);
        JsonObjectRequest json=new JsonObjectRequest(Request.Method.GET, url_peticion, null, response -> {
            Gson gson=new GsonBuilder().create();
            RespuestaListaActGimnasio respuestaListaActGimnasio=gson.fromJson(response.toString(),RespuestaListaActGimnasio.class);
            if(respuestaListaActGimnasio.data != null){
                callback.onSuccesGet(respuestaListaActGimnasio.data,tv);
            }



        }, volleyError -> callback.onFaileureGet("Error en la peticion."));
        json.setTag(COLA_PETICIONES);
        peticionesRed.anhadirPeticionACola(json);
    }
    public static void getItemGimnasioPrecio(int id, String COLA_PETICIONES, PeticionesRed peticionesRed, final Actividadinterface callback){

        HashMap<String,String>parametros=new HashMap<>();
        parametros.put("id_gimnasio",String.valueOf(id));

        String url_peticion=WebService.URL_Actividades+ Utilidades.generaParametrosURL(parametros);
        JsonObjectRequest json=new JsonObjectRequest(Request.Method.GET, url_peticion, null, response -> {
            Gson gson=new GsonBuilder().create();
            RespuestaListaActGimnasio respuestaListaActGimnasio=gson.fromJson(response.toString(),RespuestaListaActGimnasio.class);
            if(respuestaListaActGimnasio.data != null){
                callback.onSuccesGetPrecio(respuestaListaActGimnasio.data);
            }



        }, volleyError -> callback.onFaileureGet("Error en la peticion."));
        json.setTag(COLA_PETICIONES);
        peticionesRed.anhadirPeticionACola(json);
    }
    public static void getActividad(PeticionesRed peticionesRed,String COLA_PETICIONES, final Actividadinterface callback){
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, WebService.URL_Actividades, null, jsonObject -> {
            Gson gson=new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            RespuestaListaActividades respuestaListaActividades=gson.fromJson(jsonObject.toString(), RespuestaListaActividades.class);
            List<Actividad> lista=respuestaListaActividades.data;
            List<String>nombreactividades=new ArrayList<>();
            for (Actividad actividad:lista){
                nombreactividades.add(actividad.getNombre());
            }
           callback.onSuccesGetActividad(nombreactividades);
        }, volleyError -> callback.onFailureGetActividad( "Error al generar el texto json."));
        jsonObjectRequest.setTag(COLA_PETICIONES);
        peticionesRed.anhadirPeticionACola(jsonObjectRequest);
    }

}
