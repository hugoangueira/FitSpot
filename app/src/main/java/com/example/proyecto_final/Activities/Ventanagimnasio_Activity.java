package com.example.proyecto_final.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_final.Adapters.ActividadesPreciosAdapter;
import com.example.proyecto_final.Adapters.ComentarioAdapter;
import com.example.proyecto_final.Adapters.GimnasioAdapter;
import com.example.proyecto_final.Api.Actions.ActividadActions;
import com.example.proyecto_final.Api.Actions.ActividadGimnasioActions;
import com.example.proyecto_final.Api.Actions.ComentariosActions;
import com.example.proyecto_final.Api.Actions.GimnasioFavActions;
import com.example.proyecto_final.Api.Actions.Interfaces.ActividadGimnasioInterface;
import com.example.proyecto_final.Api.Actions.Interfaces.Actividadinterface;
import com.example.proyecto_final.Api.Actions.Interfaces.ComentariosInterface;
import com.example.proyecto_final.Api.Actions.Interfaces.GimnasioFavInterface;
import com.example.proyecto_final.Webservice.modelo.GimnasioItem;
import com.example.proyecto_final.R;
import com.example.proyecto_final.Utilidades;
import com.example.proyecto_final.Webservice.PeticionesRed;
import com.example.proyecto_final.Webservice.modelo.Gimnasio;
import com.example.proyecto_final.Webservice.modelo.Gimnasio_comentarios;
import com.example.proyecto_final.Webservice.modelo.Gimnasio_fav;
import com.example.proyecto_final.Webservice.modelo.Usuario;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

public class Ventanagimnasio_Activity extends AppCompatActivity implements Actividadinterface, ComentariosInterface, GimnasioFavInterface, ActividadGimnasioInterface {

    ImageView img;
    TextView txt_nombre,txt_descripcion,txt_comentario,tv_color;
    FrameLayout frameLayout_comentario;
    ImageView ruta,atras,img_favorito;
    static final String COLA_PETICIONES="PeticionesGimnasios";
    static final String COLA_PETICIONES_COMENTARIO="PeticionesComentarios";
    private PeticionesRed peticionesRed;
    private GimnasioItem gimnasioItem;
    RecyclerView rv_comentarios,rv_actividades_precios;
    FrameLayout btn_desplegable;
    RatingBar valoracion;
    EditText edt_comentario;
    ComentarioAdapter comentarioAdapter;
    private int id_gimnasio;
    private Spinner spinner_comentario;
    private final String[] ordenar={"Más reciente","Mejor valoracion"};
    private BottomSheetBehavior<RelativeLayout>bottomSheetBehavior;
    private RelativeLayout bottomsheet,relative;
    private Usuario usuario;
    private SharedPreferences preferences;
    private List<Gimnasio_fav>lista_favoritos;
    private boolean favorito;
    private ScrollView scroll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventanagimnasio);
        getWindow().setStatusBarColor(ContextCompat.getColor(Ventanagimnasio_Activity.this,R.color.grisFondo));

        initvalues();
        initActions();

    }

    private void initvalues(){
        frameLayout_comentario=findViewById(R.id.framelayout_anhadir_comentario);
        tv_color=findViewById(R.id.tv_color);
        txt_comentario=findViewById(R.id.txt_comentario);
        img=findViewById(R.id.imagen);
        img_favorito=findViewById(R.id.img_favorito);
        txt_descripcion=findViewById(R.id.txt_descripcion);
        txt_nombre=findViewById(R.id.txt_nombregim);
        ruta=findViewById(R.id.ruta);
        atras=findViewById(R.id.atras);
        scroll=findViewById(R.id.scroll);
        relative=findViewById(R.id.relative);
        btn_desplegable=findViewById(R.id.framelayout_comentarios);
        rv_comentarios=findViewById(R.id.rv_comentarios);
        rv_actividades_precios=findViewById(R.id.rv_actividades_precios);
        spinner_comentario=findViewById(R.id.spinner_comentario);
        bottomsheet=findViewById(R.id.rl_bottom_sheet);


        peticionesRed=PeticionesRed.getInstancia(Ventanagimnasio_Activity.this);

        bottomSheetBehavior=BottomSheetBehavior.from(bottomsheet);
        bottomSheetBehavior.setDraggable(true);
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        rv_comentarios.setLayoutManager(new LinearLayoutManager(Ventanagimnasio_Activity.this));
        rv_actividades_precios.setLayoutManager(new GridLayoutManager(Ventanagimnasio_Activity.this,2));


        preferences= Utilidades.getPreferences(Ventanagimnasio_Activity.this);
        String json=preferences.getString("user","");
        try {
            usuario=Usuario.toObject(new JSONObject(json));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        GimnasioFavActions.getGimnasioFav(usuario.getId_usuario(),peticionesRed,COLA_PETICIONES,this);
        updateLista(0);
    }

    private void initActions(){
        atras.setOnClickListener(v -> finish());
        ruta.setOnClickListener(v -> openruta());

        ArrayAdapter<String>adapter=new ArrayAdapter<>(Ventanagimnasio_Activity.this, android.R.layout.simple_dropdown_item_1line,ordenar);
        spinner_comentario.setAdapter(adapter);
        spinner_comentario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateLista(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btn_desplegable.setOnClickListener(v-> {
                bottomsheet.setVisibility(View.VISIBLE);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        });
        frameLayout_comentario.setOnClickListener(view -> AddCommentListener());

        img_favorito.setOnClickListener(v -> {
            favorito=!favorito;
            if(favorito){
                img_favorito.setImageResource(R.drawable.favorito);
                GimnasioFavActions.postGimnasioFav(usuario.getId_usuario(), id_gimnasio, peticionesRed, COLA_PETICIONES, new GimnasioFavInterface() {});
            }
            else{
                img_favorito.setImageResource(R.drawable.no_favorito);
                GimnasioFavActions.deleteGimnasioFav(usuario.getId_usuario(), id_gimnasio, peticionesRed, COLA_PETICIONES, new GimnasioFavInterface() {});
            }
        });

    }
    @SuppressLint("SetTextI18n")
    public void initlisteners(String nombre, String imagen, String descripcion, String paginaweb){
        Picasso.with(Ventanagimnasio_Activity.this).load(imagen).into(img);
        if(nombre.length()>24){
            txt_nombre.setTextSize(19);
            txt_nombre.setText(nombre);
        }
        else  txt_nombre.setText(nombre);
        txt_descripcion.setText(descripcion);
        txt_nombre.setOnClickListener(view -> {
            Intent abrirURL=new Intent(Intent.ACTION_VIEW, Uri.parse(paginaweb));
            startActivity(abrirURL);
        });

    }
    private void updateLista(int posicionSeleccionada) {
        String opcionSeleccionada = ordenar[posicionSeleccionada];
        if (opcionSeleccionada.equals("Más reciente")) {
            getGimnasioAndComentarios("fecha");
        } else if (opcionSeleccionada.equals("Mejor valoracion")) {
            getGimnasioAndComentarios("valoracion");
        }
    }
    private void openruta(){
        Uri gmmIntentUri = Uri.parse("geo:" + gimnasioItem.getLatitud() + "," + gimnasioItem.getLongitud() + "?z=15&q="+Uri.encode(gimnasioItem.getNombre_gimnasio()));

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(mapIntent, 0);
        boolean isIntentSafe = !activities.isEmpty();

        if (isIntentSafe) {
            startActivity(mapIntent);
        } else {
            Toast.makeText(getApplicationContext(), "No se encontró la aplicación de Google Maps", Toast.LENGTH_SHORT).show();
        }
    }
    private void AddCommentListener(){
        final Dialog dialog=new Dialog(Ventanagimnasio_Activity.this);
        dialog.setContentView(R.layout.layout_dialog);

        edt_comentario=dialog.findViewById(R.id.edt_comentarios);
        valoracion = dialog.findViewById(R.id.rb_valoracion);
        Button btn_comentar=dialog.findViewById(R.id.btn_comentar);
        Button btn_cancelar=dialog.findViewById(R.id.btn_cancelarComentario);

        btn_comentar.setOnClickListener(v->{
            AddComment();
            Toast.makeText(this, "Comentario publicado.", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        btn_cancelar.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
    private void AddComment(){
        if(!edt_comentario.getText().toString().isEmpty() && valoracion.getRating()>0){
            ComentariosActions.postComentario(id_gimnasio,usuario.getId_usuario(),edt_comentario.getText().toString(), (int) valoracion.getRating(),COLA_PETICIONES_COMENTARIO,peticionesRed,this);
            valoracion.setRating(0);
            edt_comentario.setText("");
        }
    }
    private void getGimnasioAndComentarios(String orden){
        Intent obtener=getIntent();
        if(obtener.getAction().equals("gimnasio")){
            Gimnasio gimnasio= (Gimnasio) obtener.getSerializableExtra("gimnasio");
            if (gimnasio != null) {
                ActividadActions.getItemGimnasioPrecio(gimnasio.getId(),COLA_PETICIONES,peticionesRed,this);
                ComentariosActions.getComentario(orden,gimnasio.getId(),peticionesRed,COLA_PETICIONES_COMENTARIO,this);
                id_gimnasio=gimnasio.getId();
                relative.setVisibility(View.VISIBLE);
            }
        }
        else if(obtener.getAction().equals("gimnasioitem")){
            GimnasioItem gimnasio= (GimnasioItem) obtener.getSerializableExtra("gimnasio");
            if (gimnasio != null) {
                ActividadActions.getItemGimnasioPrecio(gimnasio.getId(),COLA_PETICIONES,peticionesRed,this);
                ComentariosActions.getComentario(orden,gimnasio.getId(),peticionesRed,COLA_PETICIONES_COMENTARIO,this);
                id_gimnasio=gimnasio.getId_gimnasio();
                relative.setVisibility(View.VISIBLE);
            }
        }
    }
    private void viewComentarios(List<Gimnasio_comentarios>lista){
        comentarioAdapter=new ComentarioAdapter(Ventanagimnasio_Activity.this,lista);
        /* comentarioAdapter.setOnItemClick((v, position) -> {
            Gimnasio_comentarios comentarios=lista.get(position);
            String nombre_usuario="";
            if(comentarios.getId_usuario() == usuario.getId_usuario()) {
                nombre_usuario = usuario.getNombre();
            }
            AlertDialog.Builder alertDialog= new AlertDialog.Builder(Ventanagimnasio_Activity.this);
            alertDialog.setTitle("Comentario de "+nombre_usuario);
            alertDialog.setMessage("Valoración: "+comentarios.getValoracion()+"\n"+comentarios.getComentario());
            alertDialog.setNegativeButton("Borrar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deletecoment(comentarios);
                    dialog.dismiss();
                }
            });
            alertDialog.create().show();
        });*/
        rv_comentarios.setAdapter(comentarioAdapter);
        if(lista == null){
            tv_color.setVisibility(View.VISIBLE);
            spinner_comentario.setVisibility(View.GONE);
            txt_comentario.setText("No hay comentarios");
        }
        else if(lista.size() == 1){
            tv_color.setVisibility(View.VISIBLE);
            spinner_comentario.setVisibility(View.VISIBLE);
            txt_comentario.setText("Comentarios");
        }
        else {
            tv_color.setVisibility(View.GONE);
            spinner_comentario.setVisibility(View.VISIBLE);
            txt_comentario.setText("Comentarios");
        }
    }
    @Override
    public void onSuccesGetPrecio(List<GimnasioItem> lista) {
        Actividadinterface.super.onSuccesGetPrecio(lista);
        StringBuilder actividades=new StringBuilder();
        StringBuilder precios=new StringBuilder();
        gimnasioItem=lista.get(0);
        for (GimnasioItem gim: lista){
            actividades.append(gim.getNombre()).append("\n").append("\n");
            if(gim.precio_sesion <10){
                precios.append("  ").append(gim.precio_sesion).append("€/sesión").append("\n").append("\n");
            }
            else{
                precios.append(gim.precio_sesion).append("€/sesión").append("\n").append("\n");
            }
        }
        String nombre=gimnasioItem.nombre_gimnasio;
        String des=gimnasioItem.descripcion;
        String imagen=gimnasioItem.getImagen();
        String pagina=gimnasioItem.getPagina_web();

        ActividadesPreciosAdapter adapter=new ActividadesPreciosAdapter(this,lista);
        rv_actividades_precios.setAdapter(adapter);

        initlisteners(nombre,imagen,des,pagina);
        scroll.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFaileureGet(String e) {
        Actividadinterface.super.onFaileureGet(e);
        Toast.makeText(Ventanagimnasio_Activity.this,e, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSuccesGetComentarios(List<Gimnasio_comentarios> lista) {
        ComentariosInterface.super.onSuccesGetComentarios(lista);
        viewComentarios(lista);

    }
    private void deletecoment(Gimnasio_comentarios comentarios){
        ComentariosActions.deletecoment(COLA_PETICIONES_COMENTARIO,peticionesRed,comentarios.getId_usuario(),comentarios.getId_comentario(),this);
    }

    @Override
    public void onFaileureGetComentarios(String error) {
        ComentariosInterface.super.onFaileureGetComentarios(error);
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onSuccesPostComentario(Gimnasio_comentarios gimnasio_comentarios) {
        ComentariosInterface.super.onSuccesPostComentario(gimnasio_comentarios);
        ComentariosActions.getComentario("fecha",gimnasio_comentarios.getId_gimnasio(),peticionesRed,COLA_PETICIONES_COMENTARIO,this);
        comentarioAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFaileurePostComentario(String error) {
        ComentariosInterface.super.onFaileurePostComentario(error);
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccesPostFav(Gimnasio_fav gimnasio_fav) {
        GimnasioFavInterface.super.onSuccesPostFav(gimnasio_fav);
    }

    @Override
    public void onFaileurePostFav(String error) {
        GimnasioFavInterface.super.onFaileurePostFav(error);
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccesGetFav(List<Gimnasio_fav> gimnasio_fav) {
        GimnasioFavInterface.super.onSuccesGetFav(gimnasio_fav);
        lista_favoritos=gimnasio_fav;
        if(gimnasio_fav != null) {
            for (Gimnasio_fav fav : gimnasio_fav) {
                if (id_gimnasio == fav.getId_gimnasio()) {
                    img_favorito.setImageResource(R.drawable.favorito);
                    favorito = true;
                } else {
                    favorito = false;
                }
            }
        }
    }

    @Override
    public void onFaileureGetFav(String error) {
        GimnasioFavInterface.super.onFaileureGetFav(error);
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccesDeleteFav(Gimnasio_fav gimnasioFav) {
        GimnasioFavInterface.super.onSuccesDeleteFav(gimnasioFav);
        Toast.makeText(this, "Gimnasio con id "+gimnasioFav.getId_gimnasio()+" elimminado de favoritos.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFaileureDeleteFav(String error) {
        GimnasioFavInterface.super.onFaileureDeleteFav(error);
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }


}