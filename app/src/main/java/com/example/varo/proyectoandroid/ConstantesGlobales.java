package com.example.varo.proyectoandroid;

public class ConstantesGlobales {

    public static final int CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 123;
    public static final int CODE_CAMERA_PERMISSION= 1234;

    public static String creacionDB = "CREATE TABLE Preguntas (codigo INTEGER PRIMARY KEY AUTOINCREMENT, enunciado TEXT, categoria TEXT, respuestaCorrecta TEXT, respuestaIncorrecta1 TEXT, respuestaIncorrecta2 TEXT, respuestaIncorrecta3 TEXT, foto TEXT)";

    public static String nombreDB = "DBPreguntas";

    public static String nombreTabla = "Preguntas";

    public static String codigoPregunta = "codigo";

    public static String columnaEnunciado = "enunciado";
    public static String columnaCategoria = "categoria";
    public static String columnaRespuestaCorrecta = "respuestaCorrecta";
    public static String columnaRespuestaIncorrecta1 = "respuestaIncorrecta1";
    public static String columnaRespuestaIncorrecta2 = "respuestaIncorrecta2";
    public static String columnaRespuestaIncorrecta3 = "respuestaIncorrecta3";

    public static String columnaFoto = "foto";


}
