package com.example.proyecto_final.Api.Actions.Interfaces;

import com.example.proyecto_final.Webservice.modelo.Gimnasio;
import com.example.proyecto_final.Webservice.modelo.GimnasioItem;

import java.util.List;

public interface GimnasioInterface {

    default void onSuccesGetGimnasio(List<Gimnasio> listagim){

    }
    default void onFailureGetGimnasio(String e){

    }
    default void onSuccesGetGimnasioID(List<Gimnasio>listagimnasioid){

    }
    default void onFaileureGimnasioID(String error){

    }
    default void onSuccesGetGimnasiosOrdenated(List<Gimnasio> listagim){

    }
    default void onFailureGetGimnasiosOrdenated(String error){

    }
    default void onSuccesGetFiltrarLocalidadMap(List<Gimnasio>lista){

    }
    default void onFailureGetFiltrarLocalidadMap(String error){

    }
    default void onSuccesGetGimnasioLocalidad(List<Gimnasio> listagim){

    }
    default void onFaileureGetGimnasioLocalidad(String error){

    }

}
