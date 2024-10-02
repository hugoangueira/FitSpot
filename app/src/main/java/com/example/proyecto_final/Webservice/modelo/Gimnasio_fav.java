package com.example.proyecto_final.Webservice.modelo;

public class Gimnasio_fav {

    private int id_gimnasio;
    private int id_usuario;

    public Gimnasio_fav(int id_gimnasio, int id_usuario) {
        this.id_gimnasio = id_gimnasio;
        this.id_usuario = id_usuario;
    }

    public int getId_gimnasio() {
        return id_gimnasio;
    }

    public void setId_gimnasio(int id_gimnasio) {
        this.id_gimnasio = id_gimnasio;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

}
