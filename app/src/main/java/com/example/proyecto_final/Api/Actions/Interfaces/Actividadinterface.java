package com.example.proyecto_final.Api.Actions.Interfaces;

import android.widget.TextView;

import com.example.proyecto_final.Webservice.modelo.GimnasioItem;

import java.util.List;

public interface Actividadinterface {
    default void onSuccesGet(List<GimnasioItem> lista, TextView tv){

    }
    default void onFaileureGet(String e){

    }
    default void onSuccesGetPrecio(List<GimnasioItem> lista){

    }
    default void onFaileureGetPrecio(String e){

    }

    default void onSuccesGetActividad(List<String> listaactividad){

    }
    default void onFailureGetActividad(String e){

    }


}
