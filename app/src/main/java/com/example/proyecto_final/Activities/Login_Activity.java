package com.example.proyecto_final.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.example.proyecto_final.Api.Actions.Interfaces.UserInterface;
import com.example.proyecto_final.Api.Actions.UserActions;
import com.example.proyecto_final.R;
import com.example.proyecto_final.Utilidades;
import com.example.proyecto_final.Webservice.PeticionesRed;
import com.example.proyecto_final.Webservice.modelo.Email;
import com.example.proyecto_final.Webservice.modelo.Usuario;
import org.json.JSONObject;




/** @noinspection ALL*/
public class Login_Activity extends AppCompatActivity implements UserInterface {

    private EditText edt_email,edt_contraseña;
    private TextView tv_recuperarcontraseña,btn_crearCuenta;
    private CheckBox cb_iniciada;
    private Button btn_login;
    PeticionesRed peticionesRed;
    static final String COLA_PETICIONES="PeticionesPrueba";
    String email,contraseña;
    private ImageView img_password;
    private CardView img_login;
    private RelativeLayout rl_form;
    private LinearLayout recuperar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setStatusBarColor(ContextCompat.getColor(Login_Activity.this,R.color.white));
        if (!Utilidades.tienePermiso(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Utilidades.solicitarPermisosUbicacion(this);
        }
        if(Utilidades.hayConexionInternet(Login_Activity.this)){
            initvalues();
            initActions();
        }
        else{
            Toast.makeText(this, "No tienes conexión a internet.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(peticionesRed!=null){
            peticionesRed.getColaPeticiones().cancelAll(COLA_PETICIONES);
        }
    }
    public void initvalues(){
        btn_crearCuenta=findViewById(R.id.btn_crearCuenta);
        edt_contraseña=findViewById(R.id.edt_contraseña);
        edt_email=findViewById(R.id.edt_email);
        btn_login=findViewById(R.id.btn_iniciarSesion);
        tv_recuperarcontraseña=findViewById(R.id.tv_recuperarcontraseña);
        img_password=findViewById(R.id.img_password);
        img_login=findViewById(R.id.img_login);
        rl_form=findViewById(R.id.rl_form);
        recuperar=findViewById(R.id.recuperar);
        email=edt_email.getText().toString();
        contraseña=edt_contraseña.getText().toString();

        peticionesRed=PeticionesRed.getInstancia(Login_Activity.this);

    }
    public void initActions(){
        recuperar.setOnClickListener(view ->{
           enviarCorreo();
        });

        btn_crearCuenta.setOnClickListener(view -> {
            Intent lanzar_Create=new Intent(Login_Activity.this, CreateAccount_Activity.class);
            startActivity(lanzar_Create);
        });

        btn_login.setOnClickListener(view -> {
            if(validateLogin()){
                UserActions.login(Login_Activity.this,email,contraseña,COLA_PETICIONES,peticionesRed,this);
            }
            else{notifyWrong();}
        });
        visiblepass();
    }
    private void visiblepass(){
        boolean[] contraseñavisible={false};
        img_password.setOnClickListener(view -> {
            contraseñavisible[0]=!contraseñavisible[0];
            if(contraseñavisible[0]){
                edt_contraseña.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                img_password.setImageResource(R.drawable.passwordvisible);
            }
            else{
                edt_contraseña.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                img_password.setImageResource(R.drawable.passwordnovisible);
            }
        });
    }
    private void openMain(Usuario usuario){
        Intent lanzar_home=new Intent(Login_Activity.this, MainActivity.class);
        lanzar_home.putExtra("user",usuario);
        startActivity(lanzar_home);
    }
    private void saveUser(Usuario usuario) {
        JSONObject json = usuario.toJson();
        SharedPreferences preferencia = Utilidades.getPreferences(Login_Activity.this);
        SharedPreferences.Editor editor = preferencia.edit();
        editor.putString("user", json.toString());
        editor.apply();
    }
    private Boolean validateLogin(){
        this.email=edt_email.getText().toString();
        this.contraseña=edt_contraseña.getText().toString();
        return validateFields(email,contraseña);
    }
    private Boolean validateFields(String email,String pass){
        return validateEmail(email)&& validatePass(pass);
    }
    private Boolean validateEmail(String email){return !TextUtils.isEmpty(email);}
    private Boolean validatePass(String pass){return !TextUtils.isEmpty(pass);}
    private void notifyWrong(){
        Toast.makeText(this, "Debes rellenar todos los campos.", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onSuccesLogin(Usuario usuario) {
        UserInterface.super.onSuccesLogin(usuario);
        openMain(usuario);
        saveUser(usuario);
        finish();
    }

    @Override
    public void onFailureLogin(String error) {
        UserInterface.super.onFailureLogin(error);
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onSuccesReset(Email email) {
        UserInterface.super.onSuccesReset(email);
        Toast.makeText(this, "Email enviado al correo: "+email.getCorreo(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailureReset(String error) {
        UserInterface.super.onFailureReset(error);
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    public void enviarCorreo() {
        Utilidades.visualizar_url(Login_Activity.this,"http://fitspot.sportsontheweb.net/fitspot_web/?section=change-password");
    }
}

