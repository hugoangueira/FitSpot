package com.example.proyecto_final.Api.Actions.Interfaces;

import com.example.proyecto_final.Webservice.modelo.GimnasioItem;

import java.util.List;

public interface ActividadGimnasioInterface {

    default void onSuccesGetFiltrarActividadPrecio(List<GimnasioItem>lista,String actividad){

    }
    default void onFailureGetFiltrarActividadPrecio(String error){

    }
    default void onSuccesGetFiltrarActividad(List<GimnasioItem>lista,String actividad){

    }
    default void onFailureGetFiltrarActividad(String error){

    }
    default void onSuccesGetFiltrarLocalidad(List<GimnasioItem>lista,String localidad){

    }
    default void onFaileureGetFiltrarLocalidad(String Error){

    }
    default void onSuccesGetFiltrarLocalidadActividad(List<GimnasioItem> lista,String localidad,String actividad){

    }
    default void onFaileureGetFiltrarLocalidadActividad(String error){

    }
    default void onSuccesGetFiltrarLocalidadActividadMap(List<GimnasioItem>lista){

    }
    default void onFaileureGetFiltrarLocalidadActividadMAp(String error){

    }

}
