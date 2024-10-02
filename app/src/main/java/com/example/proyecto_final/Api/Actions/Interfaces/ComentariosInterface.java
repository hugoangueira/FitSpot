package com.example.proyecto_final.Api.Actions.Interfaces;

import com.example.proyecto_final.Webservice.modelo.Gimnasio_comentarios;

import java.util.List;

public interface ComentariosInterface {

    default void onSuccesGetComentarios(List<Gimnasio_comentarios>lista){

    }
    default void onFaileureGetComentarios(String error){

    }
    default void onSuccesPostComentario(Gimnasio_comentarios gimnasio_comentarios){

    }
    default void onFaileurePostComentario(String error){

    }
    default void onSuccesDeleteComentario(String comentario){

    }
    default void onFaileureDeleteComentario(String error){

    }


}
