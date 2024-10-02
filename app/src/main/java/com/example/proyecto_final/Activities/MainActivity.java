package com.example.proyecto_final.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.proyecto_final.R;
import com.example.proyecto_final.Fragments.HomeFragment;
import com.example.proyecto_final.Fragments.MapFragment;
import com.example.proyecto_final.Fragments.UserFragment;
import com.example.proyecto_final.Utilidades;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;
    private Toolbar toolbar;
    private TextView tv_conexion;
    private FrameLayout fragment_container;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.grisFondo));
        bottomNavigationView=findViewById(R.id.bottom_navigation);
        toolbar=findViewById(R.id.toolbar);
        tv_conexion=findViewById(R.id.tv_conexion);
        fragment_container=findViewById(R.id.fragment_container);
        bottomNavigationView.setBackground(null);

        AppCompatActivity activity = MainActivity.this;
        activity.setSupportActionBar(toolbar);

        if(Utilidades.hayConexionInternet(MainActivity.this)){
            bottomNavigationView.setOnItemSelectedListener(menuItem -> {
                int itemid= menuItem.getItemId();
                if(itemid == R.id.bottom_home){
                    HomeFragment homeFragment=new HomeFragment();
                    openFragment(homeFragment);
                    return true;
                }
                else if(itemid == R.id.bottom_map){
                    openFragment(new MapFragment());
                    return true;
                }
                else if(itemid== R.id.bottom_user){
                    openFragment(new UserFragment());
                    return true;
                }
                return false;
            });
            fragmentManager=getSupportFragmentManager();
            openFragment(new HomeFragment());
            fragment_container.setVisibility(View.VISIBLE);
            tv_conexion.setVisibility(View.GONE);
        }
        else{
            fragment_container.setVisibility(View.GONE);
            tv_conexion.setVisibility(View.VISIBLE);
        }


    }
    private void closeSession(){
        SharedPreferences preferences= Utilidades.getPreferences(MainActivity.this);
        SharedPreferences.Editor editor= preferences.edit();
        editor.remove("user");
        editor.apply();
        Intent cerrarSesion=new Intent(MainActivity.this, Login_Activity.class);
        startActivity(cerrarSesion);
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.closeSession){
            closeSession();
            return true;
        }
        return false;
    }

    private void openFragment(Fragment fragment){
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container,fragment);
        transaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }


}