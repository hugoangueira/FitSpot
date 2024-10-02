package com.example.proyecto_final.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.proyecto_final.R;
import com.example.proyecto_final.Utilidades;
import com.example.proyecto_final.Webservice.modelo.Usuario;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    ImageView img_splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setStatusBarColor(ContextCompat.getColor(SplashActivity.this,R.color.white));
        getWindow().setNavigationBarColor(ContextCompat.getColor(SplashActivity.this,R.color.white));
        img_splash=findViewById(R.id.img_splash);

        if(img_splash!=null){
            Glide.with(SplashActivity.this).load(R.drawable.gift_logo).into(img_splash);
        }
        checkuser();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(img_splash!=null){
            Glide.with(SplashActivity.this).load(R.drawable.gift_logo).into(img_splash);
        }
        checkuser();
    }

    private void checkuser(){
        new Handler().postDelayed(()-> {
            SharedPreferences preferences= Utilidades.getPreferences(SplashActivity.this);
            String json=preferences.getString("user","");
            Intent intent;
            if(json.isEmpty()){
                intent=new Intent(SplashActivity.this,Login_Activity.class);
            }
            else{
                intent=new Intent(SplashActivity.this,MainActivity.class);
                try {
                    intent.putExtra("user", Usuario.toObject(new JSONObject(json)));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
            startActivity(intent);
            finish();
        },1300);
    }

}