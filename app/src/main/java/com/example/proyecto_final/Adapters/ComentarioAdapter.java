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

import com.example.proyecto_final.Api.Actions.Interfaces.UserInterface;
import com.example.proyecto_final.Api.Actions.UserActions;
import com.example.proyecto_final.R;
import com.example.proyecto_final.Webservice.PeticionesRed;
import com.example.proyecto_final.Webservice.modelo.Gimnasio_comentarios;
import com.example.proyecto_final.Webservice.modelo.Usuario;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ComentarioAdapter extends RecyclerView.Adapter<ComentarioAdapter.ComentarioViewHolder> implements UserInterface {

    Context context;
    List<Gimnasio_comentarios>lista;
    final static String COLA_PETICIONES="PeticionComentario";
    private PeticionesRed peticionesRed;
    public ComentarioAdapter(Context context, List<Gimnasio_comentarios>lista){
        this.context=context;
        this.lista=lista;
    }

    public static class ComentarioViewHolder extends RecyclerView.ViewHolder{

        TextView tv_nombre,tv_valoracion,tv_comentario,tv_fecha;

        public ComentarioViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_nombre=itemView.findViewById(R.id.tv_nombreUsuario);
            tv_comentario=itemView.findViewById(R.id.tv_comentario);
            tv_fecha=itemView.findViewById(R.id.tv_fecha);
            tv_valoracion=itemView.findViewById(R.id.tv_valoracion);

            itemView.setBackgroundColor(Color.WHITE);

            itemView.setOnClickListener(v -> {
                if(listener!=null) listener.onItemClick(v,getAdapterPosition());
            });

        }
    }

    @NonNull
    @Override
    public ComentarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comentario,parent,false);
        return new ComentarioViewHolder(itemview);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ComentarioViewHolder holder, int position) {
        peticionesRed=PeticionesRed.getInstancia(context);
        Gimnasio_comentarios comentario=lista.get(position);
        holder.tv_valoracion.setText("Valoración: "+String.valueOf(comentario.getValoracion()));
        holder.tv_comentario.setText(comentario.getComentario());

        Date fechapublicacion=comentario.getFecha();
        Date fechaActual = new Date(); // Obtiene la fecha y hora actual


        // Definir el formato de fecha que coincide con el formato de fecha de MySQL
        DateFormat formatoFechaMySQL = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            String fechapublicacionText= formatoFechaMySQL.format(fechapublicacion);
            String fechaActualText=formatoFechaMySQL.format(fechaActual);

            // Parsear las cadenas de fecha a objetos Date
            Date fechapublicacionformat = formatoFechaMySQL.parse(fechapublicacionText);
            Date fechaactualformat = formatoFechaMySQL.parse(fechaActualText);

            long diferenciaMilisegundos=fechaactualformat.getTime()-fechapublicacionformat.getTime();
            long segundos=diferenciaMilisegundos/1000;
            long minutos=segundos/60;
            long horas = minutos / 60;
            long dias = horas / 24;
            long semana = dias / 7;

            if(minutos <60){
                holder.tv_fecha.setText(" hace "+minutos+" min");
            }
            else if(horas>0 && horas <24){
                holder.tv_fecha.setText(" hace "+horas+" h");
            }
            else if(dias >0 && dias<7){
                holder.tv_fecha.setText(" hace "+dias+" d.");
            }
            else if(semana>0 && semana<=4){
                holder.tv_fecha.setText(" hace "+semana+" sem.");
            }
            else if(semana>4 && semana<48){
                long mes=semana/4;
                holder.tv_fecha.setText(" hace "+mes+" m");
            }
        }
        catch (Exception e ){
            e.printStackTrace();
        }
        UserActions.getID(COLA_PETICIONES,peticionesRed,comentario.getId_usuario(),holder.tv_nombre,this);

    }

    @Override
    public int getItemCount() {
        if(lista !=null){
            return lista.size();
        }
        else {return 0;}
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSuccesGetUsuarioID(Usuario usuario, TextView tv_usuario) {
        UserInterface.super.onSuccesGetUsuarioID(usuario, tv_usuario);
        tv_usuario.setText("@"+usuario.getNombre());
    }

    @Override
    public void onFaileureGetUsuarioID(String error) {
        UserInterface.super.onFaileureGetUsuarioID(error);
        Toast.makeText(context, "Error: "+error, Toast.LENGTH_SHORT).show();
    }

    public interface OnItemClickLis{
        void onItemClick(View v,int position);
    }
    static GimnasioAdapter.OnItemClickLis listener;
    public void setOnItemClick(GimnasioAdapter.OnItemClickLis listener){this.listener=listener;}


}
