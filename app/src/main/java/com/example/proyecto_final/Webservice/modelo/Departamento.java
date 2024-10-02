package com.example.proyecto_final.Webservice.modelo;

public class Departamento {
    /* Importante respetar mayúsculas y minúsculas iguales al objeto JSON recibido*/
    /* Equivalencias tipos de datos MYSQL <--> JAVA: https://dev.mysql.com/doc/ndbapi/en/mccj-using-clusterj-mappings.html */
    private int num_departamento;
    private String nombre;
    private String localidad;

    /* El constructor para gson no es necesario */
    public Departamento(int num_departamento, String nombre, String localidad) {
        this.num_departamento = num_departamento;
        this.nombre = nombre;
        this.localidad = localidad;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getNum_departamento() {
        return num_departamento;
    }

    public void setNum_departamento(int num_departamento) {
        this.num_departamento = num_departamento;
    }

    @Override
    public String toString() {
       return nombre + "(" + num_departamento + ")" + " de " + localidad;
    }
}
