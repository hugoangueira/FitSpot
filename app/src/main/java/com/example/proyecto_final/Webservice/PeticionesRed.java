package com.example.proyecto_final.Webservice;


import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.collection.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Objects;


public class PeticionesRed {
    private static PeticionesRed mInstance;
    private RequestQueue mcolaPeticiones;
    private final ImageLoader mImageLoader;
    private final Context mCtx;

    private final HashMap<String, ContadorPeticionYListener> contadorPeticiones; // Para saber cuando finalizan X nº de peticiones

    private PeticionesRed(Context context) {
        mCtx = context;
        mcolaPeticiones = getColaPeticiones();

        mImageLoader = new ImageLoader(mcolaPeticiones,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });

        contadorPeticiones = new HashMap<>();
    }

    public static synchronized PeticionesRed getInstancia(Context context) {
        if (mInstance == null) {
            mInstance = new PeticionesRed(context);
        }
        return mInstance;
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }


    public RequestQueue getColaPeticiones() {
        if (mcolaPeticiones == null) {
            /* getApplicationContext() es importante, evita perder el objeto si el
              contexto es activity o BroadcastReceiver
             */
            mcolaPeticiones = Volley.newRequestQueue(mCtx.getApplicationContext());

        }

        return mcolaPeticiones;
    }

    public <T> void anhadirPeticionACola(@NonNull Request<T> req) {
        getColaPeticiones().add(req);
    }



    /* CONTEO DE PETICIONES */

    public interface OnFinTodasPeticionesListener {
        void finTodasPeticiones(String etiqueta);
    }

    public static class ExceptionContadorPeticionesValorTotalIncorrecto extends Exception {
        ExceptionContadorPeticionesValorTotalIncorrecto(String texto_error) {
            super(texto_error);
        }
    }

    private static class ContadorPeticionYListener {
        OnFinTodasPeticionesListener listener;
        int contador_peticiones_finalizadas;
        int total_peticiones;

        ContadorPeticionYListener(@NonNull OnFinTodasPeticionesListener listener, int total_peticiones) throws ExceptionContadorPeticionesValorTotalIncorrecto {
            this.listener = listener;
            contador_peticiones_finalizadas = 0;
            if (total_peticiones<=0) throw new ExceptionContadorPeticionesValorTotalIncorrecto("El total debe ser mayor que cero");
            this.total_peticiones = total_peticiones;
        }
    }

    public static class ExceptionContadorPeticionesNoExiste extends Exception {
        ExceptionContadorPeticionesNoExiste(String texto_error) {
            super(texto_error);
        }
    }

    public void iniciarContadorPeticiones(@NonNull String etiqueta, int total_peticiones, @NonNull OnFinTodasPeticionesListener listener) throws ExceptionContadorPeticionesValorTotalIncorrecto {
        contadorPeticiones.put(etiqueta, new ContadorPeticionYListener(listener, total_peticiones));
    }

    public void eliminarFinTodasPeticionesListener(@NonNull String etiqueta) {
        if (contadorPeticiones.containsKey(etiqueta)) Objects.requireNonNull(contadorPeticiones.get(etiqueta)).listener = null;
    }


    public <T> void anhadirPeticionAColaConContadorPeticiones(@NonNull Request<T> req, @NonNull String etiqueta) throws ExceptionContadorPeticionesNoExiste {
        if (!contadorPeticiones.containsKey(etiqueta))
            throw new ExceptionContadorPeticionesNoExiste("No existe contador para la etiqueta: " + etiqueta);

        req.setTag(etiqueta);
        getColaPeticiones().add(req);

    }

    public synchronized int getContadorPeticiones(@NonNull String etiqueta) {
        return Objects.requireNonNull(contadorPeticiones.get(etiqueta)).contador_peticiones_finalizadas;
    }

    public synchronized void finPeticionConContadorPeticiones(@NonNull String etiqueta) throws ExceptionContadorPeticionesNoExiste {
        if (!contadorPeticiones.containsKey(etiqueta))
            throw new ExceptionContadorPeticionesNoExiste("No existe contador para la etiqueta: " + etiqueta);

        Objects.requireNonNull(contadorPeticiones.get(etiqueta)).contador_peticiones_finalizadas++;


        if (Objects.requireNonNull(contadorPeticiones.get(etiqueta)).contador_peticiones_finalizadas == contadorPeticiones.get(etiqueta).total_peticiones) {
            Objects.requireNonNull(contadorPeticiones.get(etiqueta)).listener.finTodasPeticiones(etiqueta);
        }

    }

    /* FIN CONTEO DE PETICIONES */


}

