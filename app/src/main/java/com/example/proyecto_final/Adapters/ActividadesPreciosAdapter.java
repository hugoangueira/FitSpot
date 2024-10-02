package com.example.proyecto_final.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_final.Api.Actions.Interfaces.ActividadGimnasioInterface;
import com.example.proyecto_final.Api.Actions.Interfaces.UserInterface;
import com.example.proyecto_final.Api.Actions.UserActions;
import com.example.proyecto_final.R;
import com.example.proyecto_final.Webservice.PeticionesRed;
import com.example.proyecto_final.Webservice.modelo.GimnasioItem;
import com.example.proyecto_final.Webservice.modelo.Gimnasio_comentarios;
import com.example.proyecto_final.Webservice.modelo.Usuario;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ActividadesPreciosAdapter extends RecyclerView.Adapter<ActividadesPreciosAdapter.ActividadesPreciosViewHolder> implements ActividadGimnasioInterface {

    Context context;
    List<GimnasioItem>lista;
    public ActividadesPreciosAdapter(Context context, List<GimnasioItem>lista){
        this.context=context;
        this.lista=lista;
    }
    public static class ActividadesPreciosViewHolder extends RecyclerView.ViewHolder{

        TextView tx_precio,tx_actividades;
        public ActividadesPreciosViewHolder(@NonNull View itemView) {
            super(itemView);
            tx_actividades=itemView.findViewById(R.id.txt_actividades);
            tx_precio=itemView.findViewById(R.id.txt_precio);

        }
    }

    @NonNull
    @Override
    public ActividadesPreciosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_actividad_precio,parent,false);
        return new ActividadesPreciosViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ActividadesPreciosViewHolder holder, int position) {
        GimnasioItem gimnasioitem=lista.get(position);
        holder.tx_actividades.setText(gimnasioitem.getNombre());
        holder.tx_precio.setText(gimnasioitem.getPrecio_sesion()+"€/sesión");

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

}
