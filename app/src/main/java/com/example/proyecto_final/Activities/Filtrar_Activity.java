package com.example.proyecto_final.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.proyecto_final.Api.Actions.ActividadActions;
import com.example.proyecto_final.Api.Actions.GimnasioActions;
import com.example.proyecto_final.Api.Actions.Interfaces.Actividadinterface;
import com.example.proyecto_final.Api.Actions.Interfaces.GimnasioInterface;
import com.example.proyecto_final.Fragments.HomeFragment;
import com.example.proyecto_final.R;
import com.example.proyecto_final.Webservice.PeticionesRed;
import com.example.proyecto_final.Webservice.modelo.Gimnasio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class Filtrar_Activity extends AppCompatActivity implements Actividadinterface, GimnasioInterface {

    Spinner spinner_precio;
    Button btn_aceptar,btn_atras;
    PeticionesRed peticionesRed;
    static final String COLA_PETICIONES="PeticionesFiltrar";
    int pos=0;
    TextView spinner_actividad,spinner_localidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtrar);
        getWindow().setStatusBarColor(ContextCompat.getColor(Filtrar_Activity.this,R.color.grisFondo));

        initvalues();
        initActions();

    }
    public void initvalues(){
        spinner_actividad=findViewById(R.id.spinner_actividad);
        spinner_localidad=findViewById(R.id.spinner_localidad);
        spinner_precio=findViewById(R.id.spinner_precio);
        btn_aceptar=findViewById(R.id.btn_aceptar);
        btn_atras=findViewById(R.id.btn_atras);

        peticionesRed=PeticionesRed.getInstancia(Filtrar_Activity.this);


        String[]precios={"","Menor a mayor","Mayor a menor"};
        ArrayAdapter<String>adaptador_precio=new ArrayAdapter<>(Filtrar_Activity.this, android.R.layout.simple_spinner_dropdown_item,precios);
        spinner_precio.setAdapter(adaptador_precio);
        ActividadActions.getActividad(peticionesRed,COLA_PETICIONES,this);
        GimnasioActions.getGimnasioLocalidad(peticionesRed,COLA_PETICIONES,this);

    }
    public void initActions(){
        btn_atras.setOnClickListener(view -> {
            setResult(RESULT_CANCELED);
                finish();
        });
        btn_aceptar.setOnClickListener(view-> {
                Intent abrir = new Intent(Filtrar_Activity.this, HomeFragment.class);
                String actividad = (String) spinner_actividad.getText();
                String localidad= (String) spinner_localidad.getText();
                String precio = "";
                if (spinner_precio.getSelectedItem().equals("Menor a mayor")) {
                    precio = "ASC";
                } else if (spinner_precio.getSelectedItem().equals("Mayor a menor")) {
                    precio = "DESC";
                } else {
                    precio = "";
                }
                abrir.putExtra("localidad",localidad);
                abrir.putExtra("actividad", actividad);
                abrir.putExtra("precio", precio);
                setResult(RESULT_OK, abrir);
                finish();
        });
    }

    @Override
    public void onSuccesGetActividad(List<String> listaactividad) {
        Actividadinterface.super.onSuccesGetActividad(listaactividad);
        String []actividades=listaactividad.toArray(new String[0]);
        ArrayAdapter<String> adaptador_actividades=new ArrayAdapter<>(Filtrar_Activity.this, android.R.layout.simple_dropdown_item_1line,actividades);
        spinner_actividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder a=new AlertDialog.Builder(Filtrar_Activity.this);
                a.setAdapter(adaptador_actividades, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        spinner_actividad.setText(actividades[which]);
                    }
                });
                a.create().show();
            }
        });
    }

    @Override
    public void onSuccesGetGimnasioLocalidad(List<Gimnasio> listagim) {
        GimnasioInterface.super.onSuccesGetGimnasioLocalidad(listagim);
        HashSet<String> lista_localidades = new HashSet<>();
        lista_localidades.add("");
        for (Gimnasio gimnasio : listagim) {
            lista_localidades.add(gimnasio.getLocalidad());
        }

        // Ordenar las localidades
        List<String> localidadesOrdenadas = new ArrayList<>(lista_localidades);
        Collections.sort(localidadesOrdenadas, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                if (s1.startsWith("a") && !s2.startsWith("a")) {
                    return -1; // s1 viene antes que s2
                } else if (!s1.startsWith("a") && s2.startsWith("a")) {
                    return 1; // s2 viene antes que s1
                } else {
                    return s1.compareTo(s2); // Orden alfabético normal
                }
            }
        });

        String[] localidades = localidadesOrdenadas.toArray(new String[0]);
        ArrayAdapter<String> adaptador_localidad = new ArrayAdapter<>(Filtrar_Activity.this, android.R.layout.simple_dropdown_item_1line, localidades);

        spinner_localidad.setOnClickListener(v -> {
            AlertDialog.Builder a = new AlertDialog.Builder(Filtrar_Activity.this);
            a.setAdapter(adaptador_localidad, (dialog, which) -> spinner_localidad.setText(localidades[which]));
            a.create().show();
        });
    }
}