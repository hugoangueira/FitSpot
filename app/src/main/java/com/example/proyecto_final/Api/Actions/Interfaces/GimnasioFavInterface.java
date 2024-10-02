package com.example.proyecto_final.Api.Actions.Interfaces;

import com.example.proyecto_final.Webservice.modelo.Gimnasio_fav;

import java.util.List;

public interface GimnasioFavInterface {

    default void onSuccesGetFav(List<Gimnasio_fav> gimnasio_fav){

    }
    default void onFaileureGetFav(String error){

    }
    default void onSuccesPostFav(Gimnasio_fav gimnasio_fav){

    }
    default void onFaileurePostFav(String error){

    }
    default void onSuccesDeleteFav(Gimnasio_fav gimnasioFav){

    }
    default void onFaileureDeleteFav(String error){

    }



}
