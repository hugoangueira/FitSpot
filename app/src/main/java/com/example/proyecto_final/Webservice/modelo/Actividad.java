package com.example.proyecto_final.Webservice.modelo;

public class Actividad {

    private int id_actividad;
    private String nombre;
    private String descripcion;

    public Actividad(int id_actividad, String nombre, String descripicon) {
        this.id_actividad = id_actividad;
        this.nombre = nombre;
        this.descripcion = descripicon;
    }

    public int getId_actividad() {
        return id_actividad;
    }

    public void setId_actividad(int id_actividad) {
        this.id_actividad = id_actividad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Actividades{" +
                "id_actividad=" + id_actividad +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
