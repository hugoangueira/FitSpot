package com.example.proyecto_final.Webservice.modelo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Usuario implements Serializable {
    //  @SerializedName("email")
    private int id_usuario;
    private String email;
    private String contraseña;
    private String nombre;

    public Usuario(String email, String contraseña, String nombre) {
        this.email = email;
        this.contraseña = contraseña;
        this.nombre = nombre;
    }
    public Usuario(String email, int id, String nombre) {
        this.email = email;
        this.id_usuario = id;
        this.nombre = nombre;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "email='" + email + '\'' +
                ", clave='" + contraseña + '\'' +
                ", nombre='" + nombre + '\'' +
                '}';
    }
    public static Usuario toObject(JSONObject json){
        Usuario user=null;
        String name=json.optString("nombre","");
        int id_user=json.optInt("id_user",0);
        String email=json.optString("email","");

        user=new Usuario(email,id_user,name);
        return user;
    }
    public JSONObject toJson(){
        JSONObject json=new JSONObject();

        try {
            json.put("id_user",this.id_usuario);
            json.put("email",this.email);
            json.put("contraseña",this.contraseña);
            json.put("nombre",this.nombre);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return json;
    }


}
