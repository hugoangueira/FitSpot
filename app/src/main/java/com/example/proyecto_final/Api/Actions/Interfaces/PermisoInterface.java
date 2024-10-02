package com.example.proyecto_final.Api.Actions.Interfaces;

public interface PermisoInterface {

     void onPermisoConcecido(int requestCode);
     void onPermisoDenegeado(int requestCode,boolean shouldShowRationale);

}
