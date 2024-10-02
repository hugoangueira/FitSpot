package com.example.proyecto_final.Webservice.modelo;

import java.io.Serializable;

public class GimnasioItem implements Serializable {
    public String nombre_gimnasio;
    public String nombre;
    public String paginaweb;
    public String descripcion;
    public String imagen;
    public int id_actividad;
    public int id_gimnasio;

    public int id;
    public double precio_sesion;
    public String distancia;
    public double latitud;
    public double longitud;

    public GimnasioItem(String nombre_gimnasio, String paginaweb, String descripcion, String imagen,int id_gimnasio, double latitud, double longitud) {
        this.nombre_gimnasio = nombre_gimnasio;
        this.paginaweb = paginaweb;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.id_gimnasio = id_gimnasio;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public GimnasioItem(String gymname, String pagina_web, String descripcion, String activities, String url_image) {
        this.nombre_gimnasio = gymname;
        this.paginaweb = pagina_web;
        this.descripcion = descripcion;
        this.nombre = activities;
        this.imagen = url_image;
    }

    public GimnasioItem() {

    }


    public String getGymname() {
        return nombre_gimnasio;
    }

    public void setGymname(String gymname) {
        nombre_gimnasio = gymname;
    }

    public String getPagina_web() {
        return paginaweb;
    }

    public void setPagina_web(String pagina_web) {
        this.paginaweb = pagina_web;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUrl_image() {
        return imagen;
    }

    public void setUrl_image(String url_image) {
        this.imagen = url_image;
    }

    public int getId_actividad() {
        return id_actividad;
    }

    public void setId_actividad(int id_actividad) {
        this.id_actividad = id_actividad;
    }

    public int getId_gimnasio() {
        return id_gimnasio;
    }

    public void setId_gimnasio(int id_gimnasio) {
        this.id_gimnasio = id_gimnasio;
    }

    public double getPrecio_sesion() {
        return precio_sesion;
    }

    public void setPrecio_sesion(double precio_sesion) {
        this.precio_sesion = precio_sesion;
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

    public String getNombre_gimnasio() {
        return nombre_gimnasio;
    }

    public void setNombre_gimnasio(String nombre_gimnasio) {
        this.nombre_gimnasio = nombre_gimnasio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPaginaweb() {
        return paginaweb;
    }

    public void setPaginaweb(String paginaweb) {
        this.paginaweb = paginaweb;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDistancia() {
        return distancia;
    }

    public void setDistancia(String distancia) {
        this.distancia = distancia;
    }
}
