package com.example.proyecto_final.Webservice.modelo;

public class Gimnasio_actividad {
    private int id_gimnasio;
    private int id_actividad;
    private String nombre_actividad;

    public Gimnasio_actividad(int id_actividad,int id_gimnasio, String nombre_actividad) {
        this.id_gimnasio = id_gimnasio;
        this.id_actividad= id_actividad;
        this.nombre_actividad = nombre_actividad;
    }

    public int getId_gimnasio() {
        return id_gimnasio;
    }

    public void setId_gimnasio(int id_gimnasio) {
        this.id_gimnasio = id_gimnasio;
    }

    public String getNombre_actividad() {
        return nombre_actividad;
    }

    public void setNombre_actividad(String nombre_actividad) {
        this.nombre_actividad = nombre_actividad;
    }

    public int getId_actividad() {
        return id_actividad;
    }

    public void setId_actividad(int id_actividad) {
        this.id_actividad = id_actividad;
    }

    @Override
    public String toString() {
        return "Gimnasio_actividad{" +
                "id_gimnasio=" + id_gimnasio +
                ", nombre_actividad='" + nombre_actividad + '\'' +
                '}';
    }
}
