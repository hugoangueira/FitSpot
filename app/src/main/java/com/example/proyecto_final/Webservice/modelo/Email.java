package com.example.proyecto_final.Webservice.modelo;

import java.io.Serializable;

public class Email implements Serializable {

    private String correo;


    public Email(String correo) {
        this.correo = correo;
    }


    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }

}
