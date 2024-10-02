package com.example.proyecto_final.Api.Actions.Interfaces;

import android.widget.TextView;

import com.example.proyecto_final.Webservice.modelo.Email;
import com.example.proyecto_final.Webservice.modelo.Usuario;

public interface UserInterface {

    default void onSuccesLogin(Usuario usuario){

    }
    default void onFailureLogin(String error){

    }
    default void onSuccesCreate(Usuario usuario){

    }
    default void onFailureCreate(String error){

    }
    default void onSuccesReset(Email email){

    }
    default void onFailureReset(String error){

    }
    default void onSuccesGetUsuarioID(Usuario usuario, TextView tv_usuario){

    }
    default void onFaileureGetUsuarioID(String error){

    }




}
