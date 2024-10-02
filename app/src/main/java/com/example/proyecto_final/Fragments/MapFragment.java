package com.example.proyecto_final.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.example.proyecto_final.Activities.Ventanagimnasio_Activity;
import com.example.proyecto_final.Adapters.GimnasioAdapterMap;
import com.example.proyecto_final.Api.Actions.ActividadGimnasioActions;
import com.example.proyecto_final.Api.Actions.GimnasioActions;
import com.example.proyecto_final.Api.Actions.Interfaces.ActividadGimnasioInterface;
import com.example.proyecto_final.Api.Actions.Interfaces.GimnasioInterface;
import com.example.proyecto_final.Webservice.modelo.GimnasioItem;
import com.example.proyecto_final.R;
import com.example.proyecto_final.Utilidades;
import com.example.proyecto_final.Webservice.PeticionesRed;
import com.example.proyecto_final.Webservice.modelo.Gimnasio;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback, GimnasioInterface,ActividadGimnasioInterface {

    private GoogleMap googleMap;
    private Marker ultimoMarker;
    private List<Gimnasio> listagimnasios;
    private List<GimnasioItem> listagimnasiositem;
    private List<Marker>allMarkers;
    PeticionesRed peticionesRed;
    private String actividad,COLA_PETICIONES = "PeticionesMAP";;
    private boolean isMapReady = false;
    private Utilidades utilidades;
    private LocationManager locationManager;
    private boolean ignoreNextLocationUpdate=false;
    private RecyclerView rv_gimnasios;
    private GimnasioAdapterMap gimnasioAdapterMap;
    private LatLng ubicacionActual;
    private Bitmap bitmap;
    private View rootView;
    private FrameLayout framemap;

    public MapFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_map, container, false);

        initValues();

        return rootView;
    }
    private void initValues(){
        rv_gimnasios=rootView.findViewById(R.id.rv_gimnasiosCerca);
        framemap=rootView.findViewById(R.id.framemap);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        rv_gimnasios.setLayoutManager(linearLayoutManager);

        peticionesRed = PeticionesRed.getInstancia(getContext());
        utilidades=new Utilidades();
        if(!Utilidades.tienePermiso(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)){
            Utilidades.solicitarPermisosUbicacion(getActivity());
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.contenedor_mapa);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        Bitmap bitmapOriginal=BitmapFactory.decodeResource(requireContext().getResources(),R.drawable.marcador_mapa_gimnasio);
        bitmap= Bitmap.createScaledBitmap(bitmapOriginal,130,140,false);

        fillList();
    }
    private void fillList(){
        SharedPreferences preferences=Utilidades.getPreferences(getContext());
        actividad = preferences.getString("actividad","");
        String localidad=preferences.getString("localidad","");
        float latitud= preferences.getFloat("latitud",0);
        float longitud=preferences.getFloat("longitud",0);

        if (!preferences.getString("actividad", "").isEmpty() && preferences.getString("localidad","").isEmpty()) {
            ActividadGimnasioActions.getGimnasioFiltrar(COLA_PETICIONES,peticionesRed,actividad,"",latitud,longitud,this);
        }
        else if (!preferences.getString("localidad","").isEmpty() && preferences.getString("actividad","").isEmpty()){
            GimnasioActions.getGimnasioFiltrar3(COLA_PETICIONES,peticionesRed,localidad,latitud,longitud,this);
        }
        else if(!preferences.getString("localidad","").isEmpty() && !preferences.getString("actividad","").isEmpty()){
            ActividadGimnasioActions.getGimnasioFiltrar4(COLA_PETICIONES,peticionesRed,localidad,actividad,this);
        }
        else {
            GimnasioActions.getGimnasiosOrdenated(latitud,longitud,peticionesRed, COLA_PETICIONES, this);
        }
    }

    @Override
    public void onSuccesGetGimnasiosOrdenated(List<Gimnasio> listagim) {
        GimnasioInterface.super.onSuccesGetGimnasiosOrdenated(listagim);
        listagimnasios = listagim;

        if (isMapReady) {
            setMarkersOnMap();
            List<Marker>marcadores=new ArrayList<>();
            marcadores=getNearbyMarkers(ubicacionActual,allMarkers,5000);
            if(!marcadores.isEmpty()){
                List<Gimnasio>listagimnasiosCerca=new ArrayList<>();
                for(Marker marker: marcadores){
                    for(Gimnasio gimnasio: listagim){
                        LatLng pos = new LatLng(gimnasio.getLatitud(), gimnasio.getLongitud());
                        if(marker.getPosition().equals(pos)){
                            listagimnasiosCerca.add(gimnasio);
                        }
                    }
                }
                gimnasioAdapterMap= new GimnasioAdapterMap(listagimnasiosCerca,getContext(),"vacio",getActivity());
                gimnasioAdapterMap.setOnItemClick(new GimnasioAdapterMap.OnItemClickLis() {
                    @Override
                    public void onItemClick(View v, int position) {
                        clickgimnasio(position,listagimnasiosCerca,null);
                    }
                });
            }
            else{
                try{
                    List<Gimnasio>listavacia=new ArrayList<>();
                    listavacia.add(new Gimnasio());
                    gimnasioAdapterMap = new GimnasioAdapterMap(listavacia,"texto",getContext(),getActivity());
                    ViewGroup.LayoutParams layoutParams=rv_gimnasios.getLayoutParams();
                    int newHeight=80;
                    int newHeightInPx = (int) (newHeight * getActivity().getResources().getDisplayMetrics().density + 0.5f);
                    layoutParams.height=newHeightInPx;
                    rv_gimnasios.setLayoutParams(layoutParams);
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
            rv_gimnasios.setAdapter(gimnasioAdapterMap);
            framemap.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSuccesGetFiltrarLocalidadMap(List<Gimnasio> lista) {
        GimnasioInterface.super.onSuccesGetFiltrarLocalidadMap(lista);
        listagimnasios=lista;
        if(isMapReady){
            setMarkersOnMap();
            List<Marker> marcadores=getNearbyMarkers(ubicacionActual,allMarkers,3000);
            if(marcadores != null ){
                List<Gimnasio>listagimnasiosCerca=new ArrayList<>();
                for(Marker marker: marcadores){
                    for(Gimnasio gimnasio: listagimnasios){
                        LatLng pos = new LatLng(gimnasio.getLatitud(), gimnasio.getLongitud());
                        if(marker.getPosition().equals(pos)){
                            listagimnasiosCerca.add(gimnasio);
                        }
                    }
                }
                gimnasioAdapterMap = new GimnasioAdapterMap(listagimnasios,getContext(),"vacio",getActivity());
                gimnasioAdapterMap.setOnItemClick(new GimnasioAdapterMap.OnItemClickLis() {
                    @Override
                    public void onItemClick(View v, int position) {
                        clickgimnasio(position,listagimnasios,null);
                    }
                });
            }
            else{
                gimnasioAdapterMap = new GimnasioAdapterMap(new ArrayList<>(),"texto",getContext(),getActivity());
            }

            rv_gimnasios.setAdapter(gimnasioAdapterMap);
            framemap.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSuccesGetFiltrarActividadPrecio(List<GimnasioItem> lista, String actividad) {
        ActividadGimnasioInterface.super.onSuccesGetFiltrarActividadPrecio(lista, actividad);
        putAdapterItem(lista);
        framemap.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccesGetFiltrarLocalidadActividadMap(List<GimnasioItem> lista) {
        ActividadGimnasioInterface.super.onSuccesGetFiltrarLocalidadActividadMap(lista);
        putAdapterItem(lista);
        framemap.setVisibility(View.VISIBLE);
    }
    private void putAdapterItem(List<GimnasioItem>lista){
        listagimnasiositem=lista;
        if(isMapReady){
            setMarkersOnMapItem();
            List<Marker> marcadores=getNearbyMarkers(ubicacionActual,allMarkers,200000);
            if(marcadores != null ){
                List<GimnasioItem>listagimnasiosCerca=new ArrayList<>();
                for(Marker marker: marcadores){
                    for(GimnasioItem gimnasio: listagimnasiositem){
                        LatLng pos = new LatLng(gimnasio.getLatitud(), gimnasio.getLongitud());
                        if(marker.getPosition().equals(pos)){
                            listagimnasiosCerca.add(gimnasio);
                        }
                    }
                }
                gimnasioAdapterMap = new GimnasioAdapterMap(listagimnasiositem,getContext(),"filtrando",actividad,getActivity());
                gimnasioAdapterMap.setOnItemClick(new GimnasioAdapterMap.OnItemClickLis() {
                    @Override
                    public void onItemClick(View v, int position) {
                        clickgimnasio(position,null,listagimnasiosCerca);
                    }
                });
            }
            else{
                gimnasioAdapterMap = new GimnasioAdapterMap(new ArrayList<>(),"texto",getContext(),getActivity());
            }

            rv_gimnasios.setAdapter(gimnasioAdapterMap);
        }
    }

    @SuppressLint("ServiceCast")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        configmap();

        this.locationManager =(LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        isMapReady=true;
        mostrarUbicacionUsuario();
        if(listagimnasios !=null){
            setMarkersOnMap();
        }
        else if(listagimnasiositem != null){
            setMarkersOnMapItem();
        }

    }
    private void setMarkersOnMap() {
        allMarkers=new ArrayList<>();
        try{
            Bitmap bitmapOriginal=BitmapFactory.decodeResource(requireContext().getResources(), R.drawable.marcador_mapa_gimnasio);
            Bitmap bitmap=Bitmap.createScaledBitmap(bitmapOriginal,130,140,false);
            for(Gimnasio gim : listagimnasios) {
                LatLng pos = new LatLng(gim.getLatitud(), gim.getLongitud());
                Marker marca = googleMap.addMarker(new MarkerOptions().position(pos).title(gim.getNombre_gimnasio()).snippet("Click para ver mas información.").icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
                allMarkers.add(marca);
            }
            //map.setOnInfoWindowClickListener(marker -> openMaps(marker.getTitle()));
            googleMap.setOnInfoWindowClickListener(marker -> clickgimnasio(marker.getTitle()));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    private void setMarkersOnMapItem() {
        allMarkers = new ArrayList<>();
        Bitmap bitmapOriginal=BitmapFactory.decodeResource(requireContext().getResources(), R.drawable.marcador_mapa_gimnasio);
        Bitmap bitmap=Bitmap.createScaledBitmap(bitmapOriginal,130,140,false);
        for(GimnasioItem gim : listagimnasiositem) {
            LatLng pos = new LatLng(gim.getLatitud(), gim.getLongitud());
            Marker marca = googleMap.addMarker(new MarkerOptions().position(pos).title(gim.getNombre_gimnasio()).snippet("Click para ver mas información.").icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
            allMarkers.add(marca);
        }
        //map.setOnInfoWindowClickListener(marker -> openMaps(marker.getTitle()));
        googleMap.setOnInfoWindowClickListener(marker -> clickgimnasio(marker.getTitle()));
    }
    private void clickgimnasio(int pos,List<Gimnasio> listacerca,List<GimnasioItem> listacercaitem){
        Intent pasar=new Intent(getContext(), Ventanagimnasio_Activity.class);
        if( listagimnasiositem == null ){
            Gimnasio gim=listacerca.get(pos);
            pasar.setAction("gimnasio");
            pasar.putExtra("gimnasio",gim);
        }
        else if( listagimnasios == null){
            GimnasioItem gim=listacercaitem.get(pos);
            pasar.setAction("gimnasioitem");
            pasar.putExtra("gimnasio",gim);
        }
        startActivity(pasar);
    }

    private void clickgimnasio(String title){
        if(listagimnasios != null){
            for (Gimnasio gim :listagimnasios){
                if (gim.getNombre_gimnasio().equals(title)){
                    Intent pasar=new Intent(getContext(), Ventanagimnasio_Activity.class);
                    pasar.setAction("gimnasio");
                    pasar.putExtra("gimnasio",gim);
                    startActivity(pasar);
                }
            }
        }
        else if(listagimnasiositem != null){
            for (GimnasioItem gim :listagimnasiositem){
                if (gim.getNombre_gimnasio().equals(title)){
                    Intent pasar=new Intent(getContext(), Ventanagimnasio_Activity.class);
                    pasar.setAction("gimnasioitem");
                    pasar.putExtra("gimnasio",gim);
                    startActivity(pasar);
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void mostrarUbicacionUsuario() {
        if (!Utilidades.tienePermiso(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            Utilidades.solicitarPermisosUbicacion(getActivity());
            return;
        }

        LocationListener mLocationListener = new LocationListener() {

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {

            }

            @Override
            public void onLocationChanged(@NonNull Location location) {


                if (!ignoreNextLocationUpdate) {
                    actualizarUbicacion(location);
                    ignoreNextLocationUpdate = true;
                }

            }
        };
        LocationListener mLocationListener2 = new LocationListener() {

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
            }

            @Override
            public void onLocationChanged(@NonNull Location location) {

                if (!ignoreNextLocationUpdate) {
                    actualizarUbicacion(location);
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
                actualizarUbicacion(location);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String[] permissions,@NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        utilidades.onRequestPermissionsResult(requestCode, permissions, grantResults, getActivity());
    }
    private List<Marker> getNearbyMarkers(LatLng ubicacioncentral,List<Marker> all,double radio){
        List<Marker> markersNearby=new ArrayList<>();
        for (Marker marker: all){
            LatLng ubicacionmarker=marker.getPosition();
            float[] result=new float[1];
            Location.distanceBetween(ubicacioncentral.latitude,ubicacioncentral.longitude,ubicacionmarker.latitude,ubicacionmarker.longitude,result);
            if(result[0] <= radio){
                markersNearby.add(marker);
            }
        }
        return markersNearby;
    }
    private void actualizarUbicacion(Location location) {
        if (isAdded()) { // Check if the fragment is attached
            ubicacionActual = new LatLng(location.getLatitude(), location.getLongitude());
            obtenerDireccionConNominatim(ubicacionActual);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionActual, 14));

            if (ultimoMarker != null) {
                ultimoMarker.remove();
            }

            // Access resources only when the fragment is attached
            Bitmap bitmapOriginal = BitmapFactory.decodeResource(requireContext().getResources(), R.drawable.icono_miubicacion);
            Bitmap bitmap = Bitmap.createScaledBitmap(bitmapOriginal, 130, 140, false);

            ultimoMarker = googleMap.addMarker(new MarkerOptions()
                    .position(ubicacionActual)
                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                    .title("Yo"));
            CircleOptions circleOptions=new CircleOptions()
                    .center(ubicacionActual)
                    .radius(3000)
                    .visible(false);
            googleMap.addCircle(circleOptions);

        }
    }
    private void configmap(){
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.getUiSettings().setTiltGesturesEnabled(false);
        googleMap.getUiSettings().setRotateGesturesEnabled(false);
        googleMap.getUiSettings().setCompassEnabled(false);
        googleMap.setBuildingsEnabled(false);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
    }
    private void obtenerDireccionConNominatim(LatLng latLng) {
        new Thread(() -> {
            try {
                URL url = new URL("https://nominatim.openstreetmap.org/reverse?format=json&lat="
                        + latLng.latitude + "&lon=" + latLng.longitude);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", "MyApp");
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}