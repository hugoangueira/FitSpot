package com.example.proyecto_final.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.example.proyecto_final.Api.Actions.Interfaces.UserInterface;
import com.example.proyecto_final.Api.Actions.UserActions;
import com.example.proyecto_final.R;
import com.example.proyecto_final.Utilidades;
import com.example.proyecto_final.Webservice.PeticionesRed;
import com.example.proyecto_final.Webservice.modelo.Usuario;
import org.json.JSONObject;

/** @noinspection ALL*/
public class CreateAccount_Activity extends AppCompatActivity implements UserInterface {

    private EditText edt_email,edt_contraseña,edt_nombreUsuario;
    private Button btn_cancelar,btn_crear;
    PeticionesRed peticion;
    static final String COLA_PETICIONES_PRUEBA = "PeticionesPrueba";
    private String email,contraseña,nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        getWindow().setStatusBarColor(ContextCompat.getColor(CreateAccount_Activity.this,R.color.white));

        initvalues();
        initActions();
    }
    public void initvalues(){
        edt_email=findViewById(R.id.edt_newemail);
        edt_contraseña=findViewById(R.id.edt_new_contraseña);
        edt_nombreUsuario=findViewById(R.id.edt_nuevoUsuario);
        btn_cancelar=findViewById(R.id.btn_cancelar);
        btn_crear=findViewById(R.id.btn_crear);

        peticion=PeticionesRed.getInstancia(CreateAccount_Activity.this);

    }
    public void initActions(){
        btn_cancelar.setOnClickListener(view -> {
            finish();
        });

        btn_crear.setOnClickListener(view -> {
            if(validateCreate()){
                UserActions.create(email,contraseña,nombre,COLA_PETICIONES_PRUEBA,peticion,this);
            }
            else{
                notifyWrong();
            }

        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(peticion!=null){
            peticion.getColaPeticiones().cancelAll(COLA_PETICIONES_PRUEBA);
        }
    }

    private boolean validateCreate(){
        this.email=edt_email.getText().toString();
        this.contraseña=edt_contraseña.getText().toString();
        this.nombre=edt_nombreUsuario.getText().toString();
        return validateFields(email,contraseña,nombre);
    }
    private boolean validateFields(String email,String contraseña,String nombre){
        return validateEmail(email) && validateNombre(nombre) && validatePass(contraseña);
    }
    private boolean validateEmail(String email){return !TextUtils.isEmpty(email);}
    private boolean validatePass(String pass){return !TextUtils.isEmpty(pass);}
    private boolean validateNombre(String nombre){return !TextUtils.isEmpty(nombre);}
    private void notifyWrong(){
        Toast.makeText(this, "Debes rellenar todos los campos.", Toast.LENGTH_SHORT).show();
    }

    private void open(Usuario user){
        Intent open=new Intent(CreateAccount_Activity.this,Login_Activity.class);
        open.putExtra("user",user);
        startActivity(open);
    }
    private void saveUser(Usuario user){
        JSONObject json=user.toJson();
        SharedPreferences preferences=Utilidades.getPreferences(CreateAccount_Activity.this);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("user",json.toString());
        editor.apply();
    }

    @Override
    public void onSuccesCreate(Usuario usuario) {
        UserInterface.super.onSuccesCreate(usuario);
        saveUser(usuario);
        open(usuario);
    }

    @Override
    public void onFailureCreate(String error) {
        UserInterface.super.onFailureLogin(error);
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}