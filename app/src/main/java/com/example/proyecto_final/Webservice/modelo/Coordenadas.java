package com.example.proyecto_final.Webservice.modelo;

public class Coordenadas {

    private int id_gimnasio;
    private double latitud;
    private double longitud;

    public Coordenadas(int id_gimnasio, double latitud, double longitud) {
        this.id_gimnasio = id_gimnasio;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public int getId_gimnasio() {
        return id_gimnasio;
    }

    public void setId_gimnasio(int id_gimnasio) {
        this.id_gimnasio = id_gimnasio;
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

    @Override
    public String toString() {
        return "Coordenadas{" +
                "id_gimnasio=" + id_gimnasio +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                '}';
    }
}
