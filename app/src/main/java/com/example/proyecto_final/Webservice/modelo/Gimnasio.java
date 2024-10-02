package com.example.proyecto_final.Webservice.modelo;

import java.io.Serializable;

public class Gimnasio implements Serializable{

    private int id;
    private String nombre_gimnasio;
    private String descripcion;
    private String imagen;
    private String pagina_web;
    private double latitud;
    private double longitud;
    private double distancia;
    private String localidad;

    public Gimnasio(int id, String nombre_gimnasio, String descripcion, String imagen, String pagina_web, double latitud, double longitud,String localidad) {
        this.id = id;
        this.nombre_gimnasio = nombre_gimnasio;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.pagina_web = pagina_web;
        this.latitud = latitud;
        this.longitud = longitud;
        this.localidad = localidad;
    }
    public Gimnasio(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre_gimnasio() {
        return nombre_gimnasio;
    }

    public void setNombre_gimnasio(String nombre_gimnasio) {
        this.nombre_gimnasio = nombre_gimnasio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getPagina_web() {
        return pagina_web;
    }

    public void setPagina_web(String pagina_web) {
        this.pagina_web = pagina_web;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }
    public String getLocalidad(){return localidad;}
    public void setLocalidad(String localidad){this.localidad = localidad;}

    @Override
    public String toString() {
        return "Gimnasio{" +id + nombre_gimnasio + '\'' + descripcion + '\'' +pagina_web + '\'' +
                '}';
    }
}
