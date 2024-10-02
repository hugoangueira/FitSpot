package com.example.proyecto_final.Fragments;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.proyecto_final.Activities.Filtrar_Activity;
import com.example.proyecto_final.Adapters.GimnasioAdapter;
import com.example.proyecto_final.Api.Actions.ActividadGimnasioActions;
import com.example.proyecto_final.Api.Actions.GimnasioActions;
import com.example.proyecto_final.Api.Actions.Interfaces.ActividadGimnasioInterface;
import com.example.proyecto_final.Api.Actions.Interfaces.GimnasioInterface;
import com.example.proyecto_final.Api.Actions.Interfaces.PermisoInterface;
import com.example.proyecto_final.Webservice.modelo.GimnasioItem;
import com.example.proyecto_final.R;
import com.example.proyecto_final.Activities.Ventanagimnasio_Activity;
import com.example.proyecto_final.Utilidades;
import com.example.proyecto_final.Webservice.PeticionesRed;
import com.example.proyecto_final.Webservice.modelo.Gimnasio;
import com.example.proyecto_final.Webservice.modelo.Usuario;


import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements GimnasioInterface,ActividadGimnasioInterface,PermisoInterface {
    private List<Gimnasio>gymList =new ArrayList<>();
    private List<GimnasioItem>gymListitem =new ArrayList<>();
    private RecyclerView homeRecyclerView;
    private GimnasioAdapter gimnasioAdapter;
    static final String COLA_PETICIONES="PeticionesGimnasios";
    private PeticionesRed peticionesRed;
    AutoCompleteTextView autoCompleteTextView;
    RelativeLayout filtrar,rl_home;
    ImageView img7,btn_filtrar;
    String modo="vacio";
    private View rootView;
    private LocationListener mLocationListener,mLocationListener2;
    private LocationManager locationManager;
    private boolean ignoreNextLocationUpdate=false;
    private double longitudActual,latitudActual;
    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;
    private Utilidades utilidades;
    private TextView tv_vacio;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_home, container, false);
        utilidades=new Utilidades();

        if(Utilidades.hayConexionInternet(requireContext())){
            initvalues();
            initActions();
        }else{
            Toast.makeText(requireContext(), "No tienes conexión a internet.", Toast.LENGTH_SHORT).show();
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        autoCompleteTextView.setText("");
    }

    @Override
    public void onStop() {
        super.onStop();
        if(peticionesRed!=null){
            peticionesRed.getColaPeticiones().cancelAll(COLA_PETICIONES);
        }
    }

    public void initvalues(){
        filtrar=rootView.findViewById(R.id.filtrar);
        autoCompleteTextView =rootView.findViewById(R.id.edt_filtrar);
        img7=rootView.findViewById(R.id.imageView7);
        homeRecyclerView=rootView.findViewById(R.id.homeRecyclerView);
        btn_filtrar=rootView.findViewById(R.id.btn_filtrar);
        rl_home=rootView.findViewById(R.id.rl_home);
        tv_vacio=rootView.findViewById(R.id.tv_vacio);
        homeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        peticionesRed=PeticionesRed.getInstancia(getContext());

        preferences= Utilidades.getPreferences(getContext());
        editor=preferences.edit();

        this.locationManager =(LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        mostrarUbicacionUsuario();

    }
    public void initActions(){
        btn_filtrar.setOnClickListener(view -> {
                Intent filtrar=new Intent(getContext(), Filtrar_Activity.class);
                filtrar.setAction("filtrar");
                startActivityForResult(filtrar,3000);
        });
    }
    private void clickgimnasio(int pos){
        Intent pasar=new Intent(getContext(), Ventanagimnasio_Activity.class);
        if( gymListitem == null ){
            Gimnasio gim=gymList.get(pos);
            pasar.setAction("gimnasio");
            pasar.putExtra("gimnasio",gim);
        }
        else if( gymList == null){
            GimnasioItem gim=gymListitem.get(pos);
            pasar.setAction("gimnasioitem");
            pasar.putExtra("gimnasio",gim);
        }
        startActivity(pasar);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==3000 && resultCode==RESULT_OK){
            if(data != null){
                String actividad=data.getStringExtra("actividad");
                String precio=data.getStringExtra("precio");
                String localidad=data.getStringExtra("localidad");
                autoCompleteTextView.setHint("Filtrando por "+actividad);

                if(!actividad.equals("") && localidad.equals("")){
                    modo="filtrando";
                    autoCompleteTextView.setHint("Filtrando por "+actividad);
                    ActividadGimnasioActions.getGimnasioFiltrar(COLA_PETICIONES,peticionesRed,actividad,precio,latitudActual,longitudActual,this);
                }
                else if(actividad.equals("") && !localidad.equals("")){
                    modo="filtrando_localidad";
                    autoCompleteTextView.setHint(localidad);
                    ActividadGimnasioActions.getGimnasioFiltrarLocalidad(COLA_PETICIONES,peticionesRed,localidad,latitudActual,longitudActual,this);
                }
                else if(!localidad.equals("") && !actividad.equals("")){
                    modo="filtrando";
                    autoCompleteTextView.setTextSize(12);
                    autoCompleteTextView.setHint(localidad+"-"+actividad);
                    ActividadGimnasioActions.getGimnasioFiltrarLocalidadActividad(COLA_PETICIONES,peticionesRed,actividad,localidad,precio,latitudActual,longitudActual,this);
                }

            }
        }
        else if(requestCode == 3000 && resultCode == RESULT_CANCELED){
            autoCompleteTextView.setHint("Filtrar");
            modo="vacio";
            GimnasioActions.getGimnasio(peticionesRed,COLA_PETICIONES,this);
        }
    }
    public void updateInterfaz(List<Gimnasio> listagim) {
        gymList = listagim != null ? listagim : new ArrayList<>();
        gymListitem = null;

        if (gymList.isEmpty()) {
            homeRecyclerView.setVisibility(View.GONE);
            tv_vacio.setVisibility(View.VISIBLE);
        } else {
            homeRecyclerView.setVisibility(View.VISIBLE);
            tv_vacio.setVisibility(View.GONE);
        }

        gimnasioAdapter = new GimnasioAdapter(gymList, getContext(), modo, getActivity());
        gimnasioAdapter.setOnItemClick((v, position) -> clickgimnasio(position));
        homeRecyclerView.setAdapter(gimnasioAdapter);
    }
    private void updateInterfazFiltrar(List<GimnasioItem> listagim, String actividad) {
        gymList = null;
        gymListitem = listagim != null ? listagim : new ArrayList<>();

        if (actividad != null) {
            if (gymListitem.isEmpty()) {
                homeRecyclerView.setVisibility(View.GONE);
                tv_vacio.setVisibility(View.VISIBLE);
            } else {
                homeRecyclerView.setVisibility(View.VISIBLE);
                tv_vacio.setVisibility(View.GONE);
            }

            gimnasioAdapter = new GimnasioAdapter(gymListitem, getContext(), modo, actividad, getActivity());
        } else {
            if (gymListitem.isEmpty()) {
                homeRecyclerView.setVisibility(View.GONE);
                tv_vacio.setVisibility(View.VISIBLE);
            } else {
                homeRecyclerView.setVisibility(View.VISIBLE);
                tv_vacio.setVisibility(View.GONE);
            }

            gimnasioAdapter = new GimnasioAdapter(gymListitem, getContext(), modo, null, getActivity());
        }

        gimnasioAdapter.setOnItemClick((v, position) -> clickgimnasio(position));
        homeRecyclerView.setAdapter(gimnasioAdapter);
    }


    private void rellenarAutocomplete(List<Gimnasio>listagim){
        List<String> nombres=new ArrayList<>();
        for (Gimnasio gim:listagim ) {
            nombres.add(gim.getNombre_gimnasio());
        }
        try {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_dropdown_item_1line, nombres);
            autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
                String nombre = (String) parent.getItemAtPosition(position);
                for (Gimnasio g : listagim) {
                    if (nombre.equals(g.getNombre_gimnasio())) {
                        Intent pasar = new Intent(getContext(), Ventanagimnasio_Activity.class);
                        pasar.setAction("gimnasio");
                        pasar.putExtra("gimnasio", g);
                        startActivity(pasar);
                    }
                }
            });
            autoCompleteTextView.setAdapter(adapter);
        }
        catch(Exception e){e.printStackTrace();}
    }
    @SuppressLint("MissingPermission")
    private void mostrarUbicacionUsuario() {
        if (!Utilidades.tienePermiso(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            Utilidades.solicitarPermisosUbicacion(getActivity());
            return;
        }
        mLocationListener = new LocationListener() {

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onLocationChanged(@NonNull Location location) {
                // TODO Auto-generated method stub


                if (!ignoreNextLocationUpdate) {
                    getGimnasio(location.getLatitude(),location.getLongitude());
                    ignoreNextLocationUpdate = true;
                }

            }
        };
        mLocationListener2 = new LocationListener() {
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onLocationChanged(@NonNull Location location) {
                // TODO Auto-generated method stub

                if (!ignoreNextLocationUpdate) {
                    getGimnasio(location.getLatitude(),location.getLongitude());
                    ignoreNextLocationUpdate = true;
                }
            }
        };
        if (!Utilidades.tienePermiso(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            Utilidades.solicitarPermisosUbicacion(getActivity());
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,
                    1, mLocationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
                    0, mLocationListener2);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            if (location != null) {
                longitudActual=location.getLongitude();
                latitudActual=location.getLatitude();
               getGimnasio(latitudActual,longitudActual);
            }
        }
    }
    public void getGimnasio(double latitud,double longitud){
        GimnasioActions.getGimnasiosOrdenated(latitud,longitud,peticionesRed,COLA_PETICIONES,this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        utilidades.onRequestPermissionsResult(requestCode, permissions, grantResults, getActivity());
    }

    @Override
    public void onSuccesGetGimnasiosOrdenated(List<Gimnasio> listagim) {
        GimnasioInterface.super.onSuccesGetGimnasiosOrdenated(listagim);
        updateInterfaz(listagim);
        rellenarAutocomplete(listagim);
        rl_home.setVisibility(View.VISIBLE);

        editor.remove("actividad");
        editor.remove("localidad");
        editor.apply();
    }
    @Override
    public void onFailureGetGimnasiosOrdenated(String error) {
        GimnasioInterface.super.onFailureGetGimnasiosOrdenated(error);
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccesGetFiltrarActividadPrecio(List<GimnasioItem> lista, String actividad) {
        ActividadGimnasioInterface.super.onSuccesGetFiltrarActividadPrecio(lista, actividad);
        updateInterfazFiltrar(lista,actividad);
        rl_home.setVisibility(View.VISIBLE);
        editor.putString("actividad",actividad);
        editor.remove("localidad");
        editor.apply();

    }

    @Override
    public void onFailureGetFiltrarActividadPrecio(String error) {
        ActividadGimnasioInterface.super.onFailureGetFiltrarActividadPrecio(error);
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
        autoCompleteTextView.setHint("No hay gimnasios para ese filtro");
    }

    @Override
    public void onSuccesGetFiltrarLocalidad(List<GimnasioItem> lista, String localidad) {
        ActividadGimnasioInterface.super.onSuccesGetFiltrarLocalidad(lista, localidad);
        updateInterfazFiltrar(lista,null);
        rl_home.setVisibility(View.VISIBLE);
        editor.putString("localidad",localidad);
        editor.remove("actividad");
        editor.putFloat("latitud", (float) latitudActual);
        editor.putFloat("longitud", (float) longitudActual);
        editor.apply();
    }

    @Override
    public void onFaileureGetFiltrarLocalidad(String Error) {
        ActividadGimnasioInterface.super.onFaileureGetFiltrarLocalidad(Error);
        Toast.makeText(getContext(), Error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccesGetFiltrarLocalidadActividad(List<GimnasioItem> lista, String localidad, String actividad) {
        ActividadGimnasioInterface.super.onSuccesGetFiltrarLocalidadActividad(lista, localidad, actividad);
        updateInterfazFiltrar(lista,actividad);
        rl_home.setVisibility(View.VISIBLE);
        editor.putString("localidad",localidad);
        editor.putString("actividad",actividad);
        editor.apply();
    }

    @Override
    public void onPermisoConcecido(int requestCode) {
    }

    @Override
    public void onPermisoDenegeado(int requestCode, boolean shouldShowRationale) {

    }
}