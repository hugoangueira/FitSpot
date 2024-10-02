package com.example.proyecto_final.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.proyecto_final.Adapters.GimnasioAdapter;
import com.example.proyecto_final.Api.Actions.GimnasioActions;
import com.example.proyecto_final.Api.Actions.GimnasioFavActions;
import com.example.proyecto_final.Api.Actions.Interfaces.GimnasioFavInterface;
import com.example.proyecto_final.Api.Actions.Interfaces.GimnasioInterface;
import com.example.proyecto_final.R;
import com.example.proyecto_final.Activities.Ventanagimnasio_Activity;
import com.example.proyecto_final.Utilidades;
import com.example.proyecto_final.Webservice.PeticionesRed;
import com.example.proyecto_final.Webservice.modelo.Gimnasio;
import com.example.proyecto_final.Webservice.modelo.Gimnasio_fav;
import com.example.proyecto_final.Webservice.modelo.Usuario;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/** @noinspection ALL*/
public class UserFragment extends Fragment implements GimnasioInterface, GimnasioFavInterface {

    private List<Gimnasio>gymList;
    private List<Gimnasio_fav> listafavoritos;
    private RecyclerView userRecyclerView;
    private GimnasioAdapter gimnasioAdapter;
    private TextView tv_usuario,tv_email,tv_no_favorites;
    private View root;
    private PeticionesRed peticionesRed;
    final static String COLA_PETICIONES="Peticiones User";
    private RelativeLayout rl_user;
    private Usuario usuario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root= inflater.inflate(R.layout.fragment_user, container, false);
        initvalues();

        return root;
    }


    @SuppressLint("SetTextI18n")
    public void initvalues(){
        userRecyclerView=root.findViewById(R.id.UserRecyclerView);
        tv_email=root.findViewById(R.id.tv_emailusuarioUser);
        tv_usuario=root.findViewById(R.id.tv_nombreusuarioUser);
        rl_user=root.findViewById(R.id.rl_user);
        tv_no_favorites=root.findViewById(R.id.tv_no_favorites);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        peticionesRed=PeticionesRed.getInstancia(getContext());

        try {
            SharedPreferences preferences=Utilidades.getPreferences(getContext());
            String json=preferences.getString("user","");
            usuario=Usuario.toObject(new JSONObject(json));
            tv_usuario.setText("Usuario: "+usuario.getNombre());
            tv_email.setText("Correo: "+usuario.getEmail());
            GimnasioFavActions .getGimnasioFav(usuario.getId_usuario(),peticionesRed,COLA_PETICIONES,this);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        GimnasioFavActions .getGimnasioFav(usuario.getId_usuario(),peticionesRed,COLA_PETICIONES,this);
    }

    private void clickgimnasio(int pos){
        Gimnasio gim=gymList.get(pos);
        Intent pasar=new Intent(getContext(), Ventanagimnasio_Activity.class);
        pasar.putExtra("gimnasio",gim);
        pasar.setAction("gimnasio");
        startActivity(pasar);
    }

    @Override
    public void onSuccesGetFav(List<Gimnasio_fav> gimnasio_fav) {
        GimnasioFavInterface.super.onSuccesGetFav(gimnasio_fav);
        listafavoritos=gimnasio_fav;
        GimnasioActions.getGimnasio(peticionesRed,COLA_PETICIONES,this);
    }

    @Override
    public void onSuccesGetGimnasio(List<Gimnasio> listagim) {
        GimnasioInterface.super.onSuccesGetGimnasio(listagim);
        gymList = new ArrayList<>();
        TextView tvNoFavorites = root.findViewById(R.id.tv_no_favorites);

        if (listafavoritos != null && !listafavoritos.isEmpty()) {
            for (Gimnasio_fav gim : listafavoritos) {
                for (Gimnasio gimnasio : listagim) {
                    if (gim.getId_gimnasio() == gimnasio.getId()) {
                        Gimnasio gimnasioAñadir = new Gimnasio(gimnasio.getId(), gimnasio.getNombre_gimnasio(), gimnasio.getDescripcion(), gimnasio.getImagen(), gimnasio.getPagina_web(), gimnasio.getLatitud(), gimnasio.getLongitud(), gimnasio.getLocalidad());
                        gymList.add(gimnasioAñadir);
                    }
                }
            }
            if (!gymList.isEmpty()) {
                gimnasioAdapter = new GimnasioAdapter(gymList, getContext(), "vacio", getActivity());
                gimnasioAdapter.setOnItemClick((v, position) -> clickgimnasio(position));
                userRecyclerView.setAdapter(gimnasioAdapter);
                tvNoFavorites.setVisibility(View.GONE);
                userRecyclerView.setVisibility(View.VISIBLE);
            } else {
                tvNoFavorites.setVisibility(View.VISIBLE);
                userRecyclerView.setVisibility(View.GONE);
            }
        } else {
            tvNoFavorites.setVisibility(View.VISIBLE);
            userRecyclerView.setVisibility(View.GONE);
        }
        rl_user.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFaileureGetFav(String error) {
        GimnasioFavInterface.super.onFaileureGetFav(error);
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailureGetGimnasio(String e) {
        GimnasioInterface.super.onFailureGetGimnasio(e);
        Toast.makeText(getContext(), e, Toast.LENGTH_SHORT).show();
    }
}