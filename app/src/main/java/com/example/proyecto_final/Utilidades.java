package com.example.proyecto_final;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.proyecto_final.Api.Actions.Interfaces.PermisoInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;


public class Utilidades {

    public static final int REQUEST_LOCATION_PERMISSIONS=101;
    public PermisoInterface callback;
    private static boolean isDialogShowing=false;

    public static boolean hayConexionInternet(Context contexto){
        ConnectivityManager cm =
                (ConnectivityManager) contexto.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }



    /* Genera los parámatros a añadir a una url teniendo en cuenta símbolos ?, & y condificación utf-8 */
    public static String generaParametrosURL(HashMap<String,String> listaParametros) {

        String parametrosURL = "";

        if (!listaParametros.isEmpty()) {
            parametrosURL += "?";
            try {
                for (Map.Entry<String, String> p : listaParametros.entrySet()) {
                    String parametroString = p.getKey() + "="
                            + URLEncoder.encode(p.getValue(), "UTF-8");
                    if (parametrosURL.length() > 1) {
                        parametrosURL += "&" + parametroString;
                    } else {
                        parametrosURL += parametroString;
                    }
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        return parametrosURL;
   }



    /* Convierte un array de bytes en su representación Hexadecimal String */
    public static String bytesAHexString(byte[] bytes){
        StringBuilder sb = new StringBuilder();
        for(byte b : bytes){
            sb.append(String.format("%02x", b&0xff));
        }
        return sb.toString();
    }

    /* Encripta los bytes UTF-8 de un String a MD5 hexadecimal  */
    public static String encriptaMD5(String s)
    {
        MessageDigest digest;
        try
        {
            digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes(StandardCharsets.UTF_8));
            return bytesAHexString(digest.digest());
        }
        catch(Exception e){
            return "";
        }

    }

    /**** Otros métodos útiles ****/

    // Oculta el Teclado virtual
    public static void ocultarTeclado(Activity activity){
        InputMethodManager inputMethodManager = (InputMethodManager)
                activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
				}

    // Prerequisito: Usar Justo antes de setContentView
    public static void  eliminarBarraSuperior(AppCompatActivity a){
        a.requestWindowFeature(Window.FEATURE_NO_TITLE); // Sin ActionBar
        ActionBar actionBar = a.getSupportActionBar(); // Con ActionBar
        if (actionBar!=null) actionBar.hide();
    }

    // Evita que la Activity se cierre y se vuelva a abrir (perdiendo los datos en memoria) cuando el dispositivo gira a posición horiontal
    public static void evitarCambioOrientacionApaisado(Activity a){
        a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


    public static void visualizar_url(Context contexto, String url){
        Uri uri_url = Uri.parse(url);
        Intent it = new Intent(Intent.ACTION_VIEW, uri_url);
        contexto.startActivity(it);
    }

    public interface DialogResponseListener {
        void onResponse(boolean result);
    }

    public static void showCustomDialogWithConfirmation(Context context, String title, String message, DialogResponseListener listener) {
        if (isDialogShowing) {
            return;
        }
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context, R.style.CustomAlertDialog);

        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("Aceptar", (dialog, id) -> {
                    listener.onResponse(true);
                    isDialogShowing = false;
                })
                .setNegativeButton("Cancelar", (dialog, id) -> {
                    listener.onResponse(false);
                    isDialogShowing = false;
                });

        builder.setCancelable(true);

        AlertDialog dialog = builder.create();
        dialog.setOnCancelListener(dialog1 -> {
            if (listener != null) {
                listener.onResponse(false);
                isDialogShowing = false;
            }
        });
        dialog.show();
        isDialogShowing = true;
    }
    public static void showCustomDialogWithConfiguracion(Context context, String title, String message, DialogResponseListener listener) {
        if (isDialogShowing) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);

        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("Configuración", (dialog, id) -> {
                    listener.onResponse(true);
                    isDialogShowing = false;
                })
                .setNegativeButton("Cancelar", (dialog, id) -> {
                    listener.onResponse(false);
                    isDialogShowing = false;
                });

        builder.setCancelable(true);

        AlertDialog dialog = builder.create();
        dialog.setOnCancelListener(dialog1 -> {
            if (listener != null) {
                listener.onResponse(false);
                isDialogShowing = false;
            }
        });
        dialog.show();
        isDialogShowing = true;
    }

    public static SharedPreferences getPreferences(Context context){
        SharedPreferences prefs = context.getSharedPreferences("general",Context.MODE_PRIVATE);
        return prefs;
    }
    public static boolean tienePermiso(Context context,String permiso){
        return ContextCompat.checkSelfPermission(context,permiso) == PackageManager.PERMISSION_GRANTED;
    }
    public static void solicitarPermisosUbicacion(Activity activity){
        ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSIONS);
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, Activity activity) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            callback.onPermisoConcecido(requestCode);
            Log.e("falloo","concedido");
        } else if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            boolean shouldShowRationale = false;
            for (String perm : permissions) {
                shouldShowRationale = shouldShowRationale || ActivityCompat.shouldShowRequestPermissionRationale(activity, perm);
            }
            Log.e("falloo","concedidono");
            callback.onPermisoDenegeado(requestCode, shouldShowRationale);
        }
    }

    public static void mostrarPersimoDenegado(Activity activity,int requestCode, boolean shouldShowRationale){
        String mensaje="Este permiso es fundamental para el correcto funcionamiento de la app. En caso de no aceptarlo, no podras hacer uso de la app. Al pulsar en 'Aceptar' se volvera a pedir.";

        if(!activity.isFinishing()){
            if (shouldShowRationale){
                showCustomDialogWithConfirmation(activity,"Permiso necesario",mensaje,result -> {
                    if(result){
                        if(requestCode == REQUEST_LOCATION_PERMISSIONS){
                            solicitarPermisosUbicacion(activity);
                        }
                    }
                });
            }
        }else {
            if(requestCode == REQUEST_LOCATION_PERMISSIONS){
                mensaje="El permiso de ubicacion es esencial para el funcionamiento de la aplicación, en caso de no aceptarlo no podrás utilizar la aplicación.";
            }
        }

    }









    public static String obtenTextoNormalizado (String texto) {
        return  Normalizer.normalize(texto, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }
    public static String getJSONFromAPI (String url){
        String output = "";
        try {
            URL apiEnd = new URL(url);
            int responseCode;
            HttpURLConnection connection;
            InputStream is;

            connection = (HttpURLConnection) apiEnd.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.connect();

            responseCode = connection.getResponseCode();
            if(responseCode < HttpURLConnection.HTTP_BAD_REQUEST){
                is = connection.getInputStream();
            }else {
                is = connection.getErrorStream();
            }

            output = convertISToString(is);
            is.close();
            connection.disconnect();

        }  catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return output;
    }

    private static String convertISToString(InputStream is){
        StringBuffer buffer = new StringBuffer();

        try {

            BufferedReader br;
            String row;

            br = new BufferedReader(new InputStreamReader(is));
            while ((row = br.readLine())!= null){
                buffer.append(row);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }
    public String getInfo(String end){

        String json;
        String output;
        json = getJSONFromAPI(end);
        output = parseJson(json);


        return output;
    }

    private String parseJson(String json){
        try {
            JSONObject distanceJson = new JSONObject(json)
                    .getJSONArray("rows")
                    .getJSONObject(0)
                    .getJSONArray("elements")
                    .getJSONObject(0)
                    .getJSONObject("distance");
            Double distanceDouble = null ;
            String distance = distanceJson.get("text").toString();
            if (distance.contains("km")){
                distanceDouble = Double.parseDouble(distance.replace("km", ""));

            }else {
                distanceDouble = Double.parseDouble("0." + distance.replace("m", ""));
            }

            String deliverydata=distanceDouble.toString();

            return deliverydata;
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }
}
