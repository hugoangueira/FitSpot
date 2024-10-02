package com.example.proyecto_final.Webservice;


public class WebService {
    public static final String CARPETA ="/web_service_Proyecto";
    public static final String SERVIDOR = "fitspot.sportsontheweb.net/"; // Localhost en anfitrión del Emulador
    public static final String URL_Gimnasios ="http://" + SERVIDOR + CARPETA + "/gimnasio.php";
    public static final String URL_Reset ="http://" + SERVIDOR + CARPETA + "/sendemail.php";
    public static final String URL_Actividades ="http://" + SERVIDOR + CARPETA + "/actividades.php";
    public static final String URL_Gimnasio_comentarios ="http://" + SERVIDOR + CARPETA + "/gimnasio_comentario.php";
    public static final String URL_Gimnasio_fav ="http://" + SERVIDOR + CARPETA + "/gimnasio_favorito.php";
    public static final String URL_Usuarios ="http://" + SERVIDOR + CARPETA + "/usuario.php";
    public static final String URL_Gimnasio_Actividad =  "http://" + SERVIDOR +  CARPETA + "/gimnasio_actividad.php";

    public final static class JSON {
        // Constantes para mensajes json. Formato: https://github.com/omniti-labs/jsend
        public final static String STATUS = "status"; // Puede ser: ERROR, FAIL, SUCCESS
        public final static String ERROR = "error"; // Error grave al intentar procesar la petición.
        public final static String MESSAGE = "message"; // Mensaje de error.
        public final static String CODE = "code"; // Código de error (opcional).
        public final static int ERROR_EN_SINTAXIS_PETICION = 400;

        public final static String FAIL = "fail"; // No se ha podido satisfacer la petición, mensaje en DATA.
        public final static String SUCCESS = "success"; // Todo OK
        public final static String DATA = "data"; // Se envían datos de respuesta con FAIL Y SUCCESS.
        public final static String SINDATOS = null;

    }
}
