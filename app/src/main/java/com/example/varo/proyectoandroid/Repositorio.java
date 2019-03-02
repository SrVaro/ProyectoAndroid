package com.example.varo.proyectoandroid;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

public class Repositorio {

    /**
     *
     * @param myContext
     * @param nuevaPregunta
     *
     * Inserta un pregunta (con fotografia) en la base de datos
     */
    public static void insertarPreguntaConFoto(Context myContext, Pregunta nuevaPregunta ) {

        // Se abre la conexion con la base de datos
        DatabaseSQLiteHelper DB = new DatabaseSQLiteHelper(myContext, ConstantesGlobales.nombreDB, null, 1);

        SQLiteDatabase db = DB.getWritableDatabase();

        // Se inserta la pregunta que se recibe como parametro en la base de datos
        db.execSQL("INSERT INTO '"+ConstantesGlobales.nombreTabla+"' (" +
                " enunciado," +
                " categoria," +
                " respuestaCorrecta," +
                " respuestaIncorrecta1," +
                " respuestaIncorrecta2," +
                " respuestaIncorrecta3," +
                " foto)"+

                "VALUES (" +
                "'" + nuevaPregunta.getEnunciado() + "'," +
                "'" + nuevaPregunta.getCategoria() + "'," +
                "'" + nuevaPregunta.getRespuestaCorrecta()+"'," +
                "'" + nuevaPregunta.getRespuestaIncorrecta1()+"'," +
                "'" + nuevaPregunta.getRespuestaIncorrecta2()+"'," +
                "'" + nuevaPregunta.getRespuestaIncorrecta3()+"'," +
                "'" + nuevaPregunta.getFoto()+"')");

        // Se cierra la conexion con la base de datos
        db.close();

    }


    /**
     *
     * @param myContext
     * @param nuevaPregunta
     *
     * Inserta un pregunta (sin fotografia) en la base de datos
     */
    public static void insertarPregunta(Context myContext, Pregunta nuevaPregunta){

        // Se abre la conexion con la base de datos
        DatabaseSQLiteHelper DBPreguntas = new DatabaseSQLiteHelper(myContext, ConstantesGlobales.nombreDB, null, 1);

        SQLiteDatabase db = DBPreguntas.getWritableDatabase();

        // Se insera la pregunta que se recibe como parametro en la base de datos
        db.execSQL("INSERT INTO " + ConstantesGlobales.nombreTabla
                + " (" + ConstantesGlobales.columnaEnunciado
                + ", " + ConstantesGlobales.columnaCategoria
                + ", " + ConstantesGlobales.columnaRespuestaCorrecta
                + ", " + ConstantesGlobales.columnaRespuestaIncorrecta1
                + ", " + ConstantesGlobales.columnaRespuestaIncorrecta2
                + ", " + ConstantesGlobales.columnaRespuestaIncorrecta3 +
                ") VALUES ('"
                + nuevaPregunta.getEnunciado() +"', '"
                + nuevaPregunta.getCategoria() +"', '"
                + nuevaPregunta.getRespuestaCorrecta() +"', '"
                + nuevaPregunta.getRespuestaIncorrecta1() +"', '"
                + nuevaPregunta.getRespuestaIncorrecta2() +"', '"
                + nuevaPregunta.getRespuestaIncorrecta3() +"')"
            );


        // Se cierra la conexion con la base de datos
        db.close();
    }

    /**
     *
     * @param myContext
     * @return Devuelve todas las preguntas de la base de datos
     *
     */
    public static ArrayList<Pregunta> recuperarPreguntas(Context myContext){

        // Se abre la conexion con la base de datos
        DatabaseSQLiteHelper DBPreguntas = new DatabaseSQLiteHelper(myContext, ConstantesGlobales.nombreDB, null, 1);

        SQLiteDatabase db = DBPreguntas.getReadableDatabase();

        // Se crea una lista donde se guardaran todas las preguntas de la base de datos
        ArrayList<Pregunta> listadoPreguntas = new ArrayList<>();

        // Se consultan los valores de la tabla preguntas
        Cursor c = db.rawQuery(" SELECT * FROM " + ConstantesGlobales.nombreTabla + "", null);

        // Se recorren los resultados de la sentencia desde el primero hasta el final
        if(c.moveToFirst()){

            do {
                // Se recogen los datos de las filas obtenidas en la consulta
                int codigo = Integer.parseInt(c.getString( c.getColumnIndex(ConstantesGlobales.columnaCodigo)));
                String enunciado = c.getString(c.getColumnIndex(ConstantesGlobales.columnaEnunciado));
                String categoria = c.getString(c.getColumnIndex(ConstantesGlobales.columnaCategoria));
                String respCorr = c.getString(c.getColumnIndex(ConstantesGlobales.columnaRespuestaCorrecta));
                String respIncorr1 = c.getString(c.getColumnIndex(ConstantesGlobales.columnaRespuestaIncorrecta1));
                String respIncorr2 = c.getString(c.getColumnIndex(ConstantesGlobales.columnaRespuestaIncorrecta2));
                String respIncorr3 = c.getString(c.getColumnIndex(ConstantesGlobales.columnaRespuestaIncorrecta3));
                String foto = c.getString(c.getColumnIndex(ConstantesGlobales.columnaFoto));

                // Se usan los datos recogidos para crear una pregunta
                Pregunta preguntaRecogida = new Pregunta(codigo, enunciado, categoria, respCorr, respIncorr1, respIncorr2, respIncorr3, foto);

                // Se almacena la nueva pregunta en la lista anteriormente creada
                listadoPreguntas.add(preguntaRecogida);

            } while(c.moveToNext());

        }

        // Se cierra la conexion con la base de datos
        db.close();

        return listadoPreguntas;
    }

    /**
     *
     * @param myContext
     * @param codigo
     * @return Devuelve una sola pregunta
     *
     *  Recupera una sola preguta de la base de dato, correspondiente al codigo que recibe como parametro
     */
    public static Pregunta recuperarPreguntaCodigo(Context myContext, int codigo){

        // Se abre la conexion con la base de datos
        DatabaseSQLiteHelper DBPreguntas = new DatabaseSQLiteHelper(myContext, ConstantesGlobales.nombreDB, null, 1);

        SQLiteDatabase db = DBPreguntas.getReadableDatabase();

        // Se hace una consulta para obetener la pregunta de la base de datos correspondiente al codigo recibido como parametro
        Cursor c = db.rawQuery(" SELECT * FROM " + ConstantesGlobales.nombreTabla + " WHERE " + ConstantesGlobales.codigoPregunta + " = " + codigo, null);


        Pregunta preguntaRecuperada = null;

        if(c.moveToFirst()){

                String enunciado = c.getString(c.getColumnIndex(ConstantesGlobales.columnaEnunciado));
                String categoria = c.getString(c.getColumnIndex(ConstantesGlobales.columnaCategoria));
                String respCorr = c.getString(c.getColumnIndex(ConstantesGlobales.columnaRespuestaCorrecta));
                String respIncorr1 = c.getString(c.getColumnIndex(ConstantesGlobales.columnaRespuestaIncorrecta1));
                String respIncorr2 = c.getString(c.getColumnIndex(ConstantesGlobales.columnaRespuestaIncorrecta2));
                String respIncorr3 = c.getString(c.getColumnIndex(ConstantesGlobales.columnaRespuestaIncorrecta3));
                String foto = c.getString(c.getColumnIndex(ConstantesGlobales.columnaFoto));

                // Se crea la pregunta obtenida de la base de datos
                preguntaRecuperada = new Pregunta(codigo, enunciado, categoria, respCorr, respIncorr1, respIncorr2, respIncorr3, foto);


        }

        //Se cierra la conexion con la base de datos
        db.close();

        return preguntaRecuperada;
    }

    /**
     *
     * @param myContext
     * @param pregunta
     *
     * Recibe una pregunta y la actualiza en la base de datos
     */
    public static void editarPregunta(Context myContext, Pregunta pregunta){

        //Se abre la conexion con la base de datos
        DatabaseSQLiteHelper DBPreguntas = new DatabaseSQLiteHelper(myContext, ConstantesGlobales.nombreDB, null, 1);

        SQLiteDatabase db = DBPreguntas.getWritableDatabase();


        //Se actualizan los datos de la pregunta en la base de datos
        db.execSQL("UPDATE " + ConstantesGlobales.nombreTabla +
                " SET " + ConstantesGlobales.columnaEnunciado + " = " + "'" + pregunta.getEnunciado()
                + "', " + ConstantesGlobales.columnaCategoria + " = " + "'" + pregunta.getCategoria()
                + "', " + ConstantesGlobales.columnaRespuestaCorrecta + " = " + "'" + pregunta.getRespuestaCorrecta()
                + "', " + ConstantesGlobales.columnaRespuestaIncorrecta1 + " = " + "'" + pregunta.getRespuestaIncorrecta1()
                + "', " + ConstantesGlobales.columnaRespuestaIncorrecta2 + " = " + "'" + pregunta.getRespuestaIncorrecta2()
                + "', " + ConstantesGlobales.columnaRespuestaIncorrecta3 + " = " + "'" + pregunta.getRespuestaIncorrecta3()
                + "', " + ConstantesGlobales.columnaFoto + " = " + "'" + pregunta.getFoto()
                + "' WHERE " + ConstantesGlobales.codigoPregunta + " = " + pregunta.getCodigo());


        db.close();

    }

    /**
     *
     * @param myContext
     * @return Devuelve todas las categorias distintas que existan en la base de datos
     *
     */
    public static ArrayList<String> consultaCategorias(Context myContext) {

        // Se abre la conexion con la base de datos
        DatabaseSQLiteHelper DBPreguntas = new DatabaseSQLiteHelper(myContext, "DBPreguntas", null, 1);

        SQLiteDatabase db = DBPreguntas.getReadableDatabase();

        // Se crea una lista donde se guardaran las categorias
        ArrayList<String> categorias = new ArrayList<>();

        // Se ejecuta la consulta en la base de datos
        Cursor c = db.rawQuery("SELECT DISTINCT " + ConstantesGlobales.columnaCategoria + " FROM " + ConstantesGlobales.nombreTabla + " ", null);

        if(c.moveToFirst()) {
            do{
                String categoria = c.getString(c.getColumnIndex(ConstantesGlobales.columnaCategoria));

                categorias.add(categoria);

            } while (c.moveToNext());
        }

        return categorias;

    }


    /**
     *
     * @param myContext
     * @return Devuelve el documento creado como un string
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     * @throws IOException
     */
    @SuppressWarnings("null")
    public static String CreateXMLString(Context myContext) throws IllegalArgumentException, IllegalStateException, IOException
    {
        // Se crea una lista para almacear las preguntas que se guardaran en la base de datos
        ArrayList<Pregunta> preguntas;

        // Se rellena la lista con todas las preguntas de la base de datos
        preguntas = recuperarPreguntas(myContext);


        XmlSerializer xmlSerializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();

        xmlSerializer.setOutput(writer);

        // Comenzamos el documeto XML
        xmlSerializer.startDocument("UTF-8", true);
        xmlSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);




        // Se empieza el documento con el TAG "quiz" (root tag)
        xmlSerializer.startTag("", "quiz");

        // Por cada pregunta en la base de datos se crea una estructura de etiquetas con toda sus datos
        for (Pregunta p: preguntas) {

            // Aqui se guardara el nombre de la categoria de la pregunta
            xmlSerializer.startTag("", "question");
            xmlSerializer.attribute("", "type", "category");

                xmlSerializer.startTag("", "category");
                    xmlSerializer.startTag("", "text");
                        xmlSerializer.text( p.getCategoria());
                    xmlSerializer.endTag("", "text");
                xmlSerializer.endTag("", "category");

            xmlSerializer.endTag("", "question");

            ///////////////////////////////////////////////////////////////////////////////////////////

            xmlSerializer.startTag("", "question");
            xmlSerializer.attribute("", "type", "multichoice");

                // Aqui se guardara el enunciado de la pregunta
                xmlSerializer.startTag("", "name");
                    xmlSerializer.startTag("", "text");
                        xmlSerializer.text( p.getEnunciado());
                    xmlSerializer.endTag("", "text");
                xmlSerializer.endTag("", "name");

                xmlSerializer.startTag("","questiontext");
                xmlSerializer.attribute("", "format", "html");
                        xmlSerializer.startTag("", "text");
                            xmlSerializer.text( p.getEnunciado());
                        xmlSerializer.endTag("", "text");

                    // Aqui se guardara la foto de la pregunta en Base64
                    xmlSerializer.startTag("","file");
                    xmlSerializer.attribute("", "name", "");
                    xmlSerializer.attribute("", "path", "");
                    xmlSerializer.attribute("", "encoding", "base64");
                        xmlSerializer.text(p.getFoto());
                    xmlSerializer.endTag("", "file");
                xmlSerializer.endTag("", "questiontext");

                xmlSerializer.startTag("","answernumbering");
                xmlSerializer.endTag("", "answernumbering");

                // Aqui se guardaran las respuestas de la pregunta
                xmlSerializer.startTag("","answer");
                xmlSerializer.attribute("","fraction", "100");
                xmlSerializer.attribute("", "format", "html");
                    xmlSerializer.startTag("", "text");
                        xmlSerializer.text(p.getRespuestaCorrecta());
                    xmlSerializer.endTag("", "text");
                xmlSerializer.endTag("", "answer");

                xmlSerializer.startTag("","answer");
                xmlSerializer.attribute("","fraction", "0");
                xmlSerializer.attribute("", "format", "html");
                    xmlSerializer.startTag("", "text");
                        xmlSerializer.text(p.getRespuestaIncorrecta1());
                    xmlSerializer.endTag("", "text");
                xmlSerializer.endTag("", "answer");

                xmlSerializer.startTag("","answer");
                xmlSerializer.attribute("","fraction", "0");
                xmlSerializer.attribute("", "format", "html");
                    xmlSerializer.startTag("", "text");
                        xmlSerializer.text(p.getRespuestaIncorrecta2());
                    xmlSerializer.endTag("", "text");
                xmlSerializer.endTag("", "answer");

                xmlSerializer.startTag("","answer");
                xmlSerializer.attribute("","fraction", "0");
                xmlSerializer.attribute("", "format", "html");
                    xmlSerializer.startTag("", "text");
                        xmlSerializer.text(p.getRespuestaIncorrecta3());
                    xmlSerializer.endTag("", "text");
                xmlSerializer.endTag("", "answer");

            xmlSerializer.endTag("","question");
        }

        xmlSerializer.endTag("","quiz");

        // Se cierra el documento
        xmlSerializer.endDocument();

        return writer.toString();
    }


    /**
     *
     * @param myContext
     * @return Devuelve el numero de preguntas totales en la base de datos
     */
    public static int consultarNumeroPreguntas(Context myContext){

        // Se abre la conxion con la base de datos
        DatabaseSQLiteHelper DBPreguntas = new DatabaseSQLiteHelper(myContext, ConstantesGlobales.nombreDB, null, 1);

        SQLiteDatabase db = DBPreguntas.getReadableDatabase();

        int total;

        // Se ejecuta la consulta de la base de datos
        Cursor c = db.rawQuery(" SELECT COUNT(*) FROM " + ConstantesGlobales.nombreTabla + "", null);

        c.moveToFirst();

        total = c.getInt(0);

        //Se cierra la conexion con la base de datos
        db.close();

        return total;

    }

    /**
     *
     * @param myContext
     * @param codigo
     *
     * Se elimina la pregunta correspondiente al codigo recibido de la base de datos
     */
    public static void eliminarPregunta(Context myContext, int codigo){

        // Se abre la conexion con la base de datos
        DatabaseSQLiteHelper DBPreguntas = new DatabaseSQLiteHelper(myContext, ConstantesGlobales.nombreDB, null, 1);

        SQLiteDatabase db = DBPreguntas.getWritableDatabase();

        db.execSQL("DELETE FROM '" + ConstantesGlobales.nombreTabla + "' WHERE codigo='" + codigo + "' ");

        // Se cierra la conexion con la base de datos
        db.close();

    }

    /**
     *
     * @param myContext
     * @return Devuelve el numero de categorias distintas que existan
     */
    public static int consultarNumeroCategorias(Context myContext) {

        // Se abre la conexion con la base de datos
        DatabaseSQLiteHelper DBPreguntas = new DatabaseSQLiteHelper(myContext, "DBPreguntas", null, 1);

        SQLiteDatabase db = DBPreguntas.getReadableDatabase();

        int total;

        // Se ejecuta la consulta de la base de datos
        Cursor c = db.rawQuery("SELECT DISTINCT COUNT(*) " + ConstantesGlobales.columnaCategoria + " FROM " + ConstantesGlobales.nombreTabla + " ", null);

        c.moveToFirst();

        total = c.getInt(0);

        return total;

    }


}
