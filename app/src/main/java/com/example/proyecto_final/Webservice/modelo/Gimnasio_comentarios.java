package com.example.proyecto_final.Webservice.modelo;

import java.util.Date;

public class Gimnasio_comentarios {

    private int id_comentario;
    private int id_usuario;
    private int id_gimnasio;
    private String comentario;
    private int valoracion;
    private Date fecha;

    public Gimnasio_comentarios(int id_usuario, int id_gimnasio, String comentario, int valoracion) {
        this.id_usuario = id_usuario;
        this.id_gimnasio = id_gimnasio;
        this.comentario = comentario;
        this.valoracion = valoracion;
    }

    public Gimnasio_comentarios(int id_usuario, int id_gimnasio, String comentario, int valoracion, Date fecha) {
        this.id_usuario = id_usuario;
        this.id_gimnasio = id_gimnasio;
        this.comentario = comentario;
        this.valoracion = valoracion;
        this.fecha = fecha;
    }

    public int getId_comentario() {
        return id_comentario;
    }

    public void setId_comentario(int id_comentario) {
        this.id_comentario = id_comentario;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getId_gimnasio() {
        return id_gimnasio;
    }

    public void setId_gimnasio(int id_gimnasio) {
        this.id_gimnasio = id_gimnasio;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public int getValoracion() {
        return valoracion;
    }

    public void setValoracion(int valoracion) {
        this.valoracion = valoracion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Gimnasio_comentarios{" +
                "id_comentario=" + id_comentario +
                "id_usuario=" + id_usuario +
                ", id_gimnasio=" + id_gimnasio +
                ", ocmentario='" + comentario + '\'' +
                ", valoracion=" + valoracion +
                ", fecha=" + fecha +
                '}';
    }
}
