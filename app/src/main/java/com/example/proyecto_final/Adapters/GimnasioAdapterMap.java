package com.example.proyecto_final.Adapters;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_final.Api.Actions.ActividadActions;
import com.example.proyecto_final.Api.Actions.Interfaces.Actividadinterface;
import com.example.proyecto_final.R;
import com.example.proyecto_final.Utilidades;
import com.example.proyecto_final.Webservice.PeticionesRed;
import com.example.proyecto_final.Webservice.modelo.Gimnasio;
import com.example.proyecto_final.Webservice.modelo.GimnasioItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GimnasioAdapterMap extends RecyclerView.Adapter<GimnasioAdapterMap.GimnasioViewHolder> implements Actividadinterface {

    private List<Gimnasio>lista=new ArrayList<>();
    Context contexto;
    String filtrado,actividad;
    static final String COLA_PETICIONES="PeticionesGimnasios";
    private PeticionesRed peticionesRed;
    List<GimnasioItem>listafiltrar=new ArrayList<>();
    private Activity activity;
    private LocationListener mLocationListener,mLocationListener2;
    private LocationManager locationManager;
    private boolean ignoreNextLocationUpdate=false;
    private double longitudActual,latitudActual;
    private Location ubicacionActual;

    public GimnasioAdapterMap(List<Gimnasio> lista, Context contexto, String filtrado, Activity activity) {
        this.lista = lista;
        this.contexto=contexto;
        this.filtrado=filtrado;
        this.activity=activity;
    }
    public GimnasioAdapterMap(List<GimnasioItem>listafiltrar, Context contexto, String filtrado, String actividad, Activity activity){
        this.listafiltrar=listafiltrar;
        this.contexto=contexto;
        this.filtrado=filtrado;
        this.actividad=actividad;
        this.activity=activity;
    }
    public GimnasioAdapterMap(List<Gimnasio>listafiltrar, String filtrado, Context contexto, Activity activity){
        this.lista=listafiltrar;
        this.contexto=contexto;
        this.filtrado=filtrado;
        this.activity=activity;
    }
    public static class GimnasioViewHolder extends RecyclerView.ViewHolder{
        ImageView img,imgno;
        TextView tv_nombre,tv_actividades,tv_km,tv_precio,tv_vacia;
        RelativeLayout layout_parent;

        public GimnasioViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.GymImage_map);
            imgno=itemView.findViewById(R.id.GymImageno_map);
            tv_nombre=itemView.findViewById(R.id.tv_GymName_map);
            layout_parent=itemView.findViewById(R.id.layoutparent);
            tv_vacia=itemView.findViewById(R.id.tv_vacia_map);
            tv_actividades=itemView.findViewById(R.id.tv_activities_map);
            tv_km=itemView.findViewById(R.id.tv_km_map);
            tv_precio=itemView.findViewById(R.id.tv_precio_map);

            itemView.setBackgroundColor(Color.WHITE);

            itemView.setOnClickListener(v -> {
               if(listener!=null) listener.onItemClick(v,getAdapterPosition());
            });

        }
    }

    @NonNull
    @Override
    public GimnasioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gimnasio_mapa,parent,false);
        return new GimnasioViewHolder(itemView);
    }

    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull GimnasioViewHolder holder, int position) {
            peticionesRed=PeticionesRed.getInstancia(contexto);
            this.locationManager =(LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
            mostrarUbicacionUsuario();
            ubicacionActual=new Location("actual");
            ubicacionActual.setLatitude(latitudActual);
            ubicacionActual.setLongitude(longitudActual);

            if(filtrado.equals("filtrando")){
                GimnasioItem gymitem=listafiltrar.get(position);
                Location ubicacion_gimnasio=new Location("gimnasio");
                ubicacion_gimnasio.setLatitude(gymitem.getLatitud());
                ubicacion_gimnasio.setLongitude(gymitem.getLongitud());

                holder.tv_km.setText(rellenarkm(ubicacion_gimnasio));
                Picasso.with(contexto).load(gymitem.getImagen()).into(holder.img);
                holder.tv_nombre.setText(gymitem.getNombre_gimnasio());
                holder.tv_actividades.setText(actividad);
                holder.tv_precio.setVisibility(View.VISIBLE);
                holder.tv_precio.setText("Precio sesión: "+gymitem.getPrecio_sesion());
            }
            else if (filtrado.equals("vacio")) {
                Gimnasio gymitem=lista.get(position);
                Location ubicacion_gimnasio=new Location("gimnasio");
                ubicacion_gimnasio.setLatitude(gymitem.getLatitud());
                ubicacion_gimnasio.setLongitude(gymitem.getLongitud());

                holder.tv_km.setText(rellenarkm(ubicacion_gimnasio));
                Picasso.with(contexto).load(gymitem.getImagen()).into(holder.img);
                holder.tv_nombre.setText(gymitem.getNombre_gimnasio());
                holder.tv_actividades.setText("");
                holder.tv_precio.setVisibility(View.GONE);
                ActividadActions.getItemGimnasio(holder.tv_actividades,gymitem.getId(),COLA_PETICIONES,peticionesRed, this);
            }
            else if(filtrado.equals("texto")){
                Gimnasio gimnasioitem=lista.get(position);
                holder.imgno.setVisibility(View.VISIBLE);
                holder.imgno.setImageResource(R.drawable.triste);
                holder.tv_actividades.setVisibility(View.GONE);
                ViewGroup.LayoutParams layoutParams=holder.layout_parent.getLayoutParams();
                int newHeight=60;
                int newHeightInPx = (int) (newHeight * activity.getResources().getDisplayMetrics().density + 0.5f);
                layoutParams.height=newHeightInPx;
                holder.layout_parent.setLayoutParams(layoutParams);

                holder.tv_nombre.setVisibility(View.GONE);
                holder.img.setVisibility(View.GONE);
                holder.tv_vacia.setVisibility(View.VISIBLE);
                holder.tv_vacia.setText("No hay gimnasios cerca");
            }
    }

    private String rellenarkm(Location ubicacion_gimnasio){
        double distancia = ubicacionActual.distanceTo(ubicacion_gimnasio);
        double distancia_km = distancia/1000.0;

        return String.format(Locale.US, "%.1f km de tu ubicación", distancia_km);
    }
    @Override
    public int getItemCount() {
        if(lista.isEmpty()){
            return listafiltrar.size();
        }
        else{
            return lista.size();
        }
    }

    public interface OnItemClickLis{
        void onItemClick(View v,int position);
    }
    static OnItemClickLis listener;
    public void setOnItemClick(OnItemClickLis listener){this.listener=listener;}


    @Override
    public void onSuccesGet(List<GimnasioItem> lista, TextView tv) {
        Actividadinterface.super.onSuccesGet(lista,tv);
        StringBuilder actividades=new StringBuilder();
        for (int i=0;i<lista.size(); i++){
            GimnasioItem gim=lista.get(i);
            actividades.append(gim.getNombre());
            if(i<lista.size()-1){
                actividades.append(", ");
            }
        }
        tv.setText(actividades);
    }

    @Override
    public void onFaileureGet(String e) {
        Actividadinterface.super.onFaileureGet(e);
        Toast.makeText(contexto, e, Toast.LENGTH_SHORT).show();
    }
    @SuppressLint("MissingPermission")
    private void mostrarUbicacionUsuario() {
        if (!Utilidades.tienePermiso(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Utilidades.solicitarPermisosUbicacion(activity);
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
                    ignoreNextLocationUpdate = true;
                }
            }
        };


        if (!Utilidades.tienePermiso(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Utilidades.solicitarPermisosUbicacion(activity);
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
            }
        }
    }

}

