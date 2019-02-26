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

    //Metodo que inserta registros en la BD con imagen
    public static boolean insertarPreguntaConFoto(Context contexto, Pregunta p ) {

        boolean valor = true;

        //Abrimos la base de datos 'DBUsuarios' en modo escritura
        DatabaseSQLiteHelper DB =
                new DatabaseSQLiteHelper(contexto, ConstantesGlobales.nombreDB, null, 1);

        SQLiteDatabase db = DB.getWritableDatabase();

        //Si hemos abierto correctamente la base de datos
        if (db != null) {

            db.execSQL("INSERT INTO '"+ConstantesGlobales.nombreTabla+"' (enunciado, categoria, respuestaCorrecta, respuestaIncorrecta1," +
                    " respuestaIncorrecta2, respuestaIncorrecta3, foto)"+
                    "VALUES ('" + p.getEnunciado() + "', '" + p.getCategoria() + "', '"+ p.getRespuestaCorrecta()+"', '"+ p.getRespuestaIncorrecta1()+"', '"+p.getRespuestaIncorrecta2()+"', '"+p.getRespuestaIncorrecta3()+"','"+p.getFoto()+"')");

            //Cerramos la base de datos
            db.close();
        } else {
            valor = false;
        }

        return valor;
    }


    /**
     *
     * @param myContext
     * @param nuevaPregunta
     *
     * Recibe una pregunta y la inserta en la base de datos
     */
    public static void insertarPregunta(Context myContext, Pregunta nuevaPregunta){

        DatabaseSQLiteHelper DBPreguntas = new DatabaseSQLiteHelper(myContext, ConstantesGlobales.nombreDB, null, 1);

        SQLiteDatabase db = DBPreguntas.getWritableDatabase();

        if (db != null) {
            //Insertamos los datos en la tabla Preguntas
            db.execSQL("INSERT INTO " + ConstantesGlobales.nombreTabla + " (" + ConstantesGlobales.columnaEnunciado
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
        }

        //Cerramos la base de datos
        db.close();
    }

    /**
     *
     * @param myContext
     * @return
     *
     *  Devuelve todas las preguntas de la base de datos
     */
    public static ArrayList<Pregunta> recuperarPreguntas(Context myContext){

        DatabaseSQLiteHelper DBPreguntas = new DatabaseSQLiteHelper(myContext, ConstantesGlobales.nombreDB, null, 1);

        SQLiteDatabase db = DBPreguntas.getReadableDatabase();

        ArrayList<Pregunta> listadoPreguntas = new ArrayList<>();

        Cursor c = db.rawQuery(" SELECT * FROM " + ConstantesGlobales.nombreTabla + "", null);

        if(c.moveToFirst()){

            do {
                int codigo = Integer.parseInt(c.getString(0));
                String enunciado = c.getString(1);
                String categoria = c.getString(2);
                String respCorr = c.getString(3);
                String respIncorr1 = c.getString(4);
                String respIncorr2 = c.getString(5);
                String respIncorr3 = c.getString(6);
                String foto = c.getString(7);

                Pregunta preguntaRecogida = new Pregunta(codigo, enunciado, categoria, respCorr, respIncorr1, respIncorr2, respIncorr3, foto);

                listadoPreguntas.add(preguntaRecogida);

            } while(c.moveToNext());

        }

        //Cerramos la base de datos
        db.close();

        return listadoPreguntas;
    }

    /**
     *
     * @param myContext
     * @param codigo
     * @return
     *
     *  Recibe un codigo para recuperar de la base de datos y devuelver la pregunta correspondiente a ese codigo
     */
    public static Pregunta recuperarPreguntaCodigo(Context myContext, int codigo){

        DatabaseSQLiteHelper DBPreguntas = new DatabaseSQLiteHelper(myContext, ConstantesGlobales.nombreDB, null, 1);

        SQLiteDatabase db = DBPreguntas.getReadableDatabase();

        Cursor c = db.rawQuery(" SELECT * FROM " + ConstantesGlobales.nombreTabla + " WHERE " + ConstantesGlobales.codigoPregunta + " = " + codigo, null);

        Pregunta preguntaRecuperada = null;

        if(c.moveToFirst()){

                String enunciado = c.getString(1);
                String categoria = c.getString(2);
                String respCorr = c.getString(3);
                String respIncorr1 = c.getString(4);
                String respIncorr2 = c.getString(5);
                String respIncorr3 = c.getString(6);
                String foto = c.getString(7);

                preguntaRecuperada = new Pregunta(codigo, enunciado, categoria, respCorr, respIncorr1, respIncorr2, respIncorr3, foto);


        }

        //Cerramos la base de datos
        db.close();

        return preguntaRecuperada;
    }

    /**
     *
     * @param myContext
     * @param pregunta
     *
     *  Recibe una pregunta y la actualiza en la base de datos
     */
    public static void editarPregunta(Context myContext, Pregunta pregunta){

        DatabaseSQLiteHelper DBPreguntas = new DatabaseSQLiteHelper(myContext, ConstantesGlobales.nombreDB, null, 1);

        SQLiteDatabase db = DBPreguntas.getWritableDatabase();

        if (db != null) {
            //Insertamos los datos en la tabla Usuarios
            db.execSQL("UPDATE " + ConstantesGlobales.nombreTabla + " SET " + ConstantesGlobales.columnaEnunciado + " = " + "'" + pregunta.getEnunciado()
                                                                            + "', " + ConstantesGlobales.columnaCategoria + " = " + "'" + pregunta.getCategoria()
                                                                            + "', " + ConstantesGlobales.columnaRespuestaCorrecta + " = " + "'" + pregunta.getRespuestaCorrecta()
                                                                            + "', " + ConstantesGlobales.columnaRespuestaIncorrecta1 + " = " + "'" + pregunta.getRespuestaIncorrecta1()
                                                                            + "', " + ConstantesGlobales.columnaRespuestaIncorrecta2 + " = " + "'" + pregunta.getRespuestaIncorrecta2()
                                                                            + "', " + ConstantesGlobales.columnaRespuestaIncorrecta3 + " = " + "'" + pregunta.getRespuestaIncorrecta3()
                                                                            + "', " + ConstantesGlobales.columnaFoto + " = " + "'" + pregunta.getFoto()
                    + "' WHERE " + ConstantesGlobales.codigoPregunta + " = " + pregunta.getCodigo());
        }

        db.close();

    }

    /**
     *
     * @param myContext
     * @return
     *
     * Devuelve todas las categorias distintas que existan en la base de datos
     */
    public static ArrayList<String> consultaCategorias(Context myContext) {

        ArrayList<String> categorias = new ArrayList<>();

        DatabaseSQLiteHelper DBPreguntas = new DatabaseSQLiteHelper(myContext, "DBPreguntas", null, 1);

        SQLiteDatabase db = DBPreguntas.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT DISTINCT " + ConstantesGlobales.columnaCategoria + " FROM " + ConstantesGlobales.nombreTabla + " ", null);

        if(c.moveToFirst()) {
            do{
                String categoria = c.getString(c.getColumnIndex("categoria"));

                categorias.add(categoria);

            } while (c.moveToNext());
        }

        return categorias;

    }


    @SuppressWarnings("null")
    public static String CreateXMLString(Context myContext) throws IllegalArgumentException, IllegalStateException, IOException
    {
        ArrayList<Pregunta> preguntasXML = new ArrayList<Pregunta>();
        preguntasXML= recuperarPreguntas(myContext);


        XmlSerializer xmlSerializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();

        xmlSerializer.setOutput(writer);

        //Start Document
        xmlSerializer.startDocument("UTF-8", true);
        xmlSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);




        //Open Tag <file>
        xmlSerializer.startTag("", "quiz");

        for (Pregunta p: preguntasXML) {
            //Categoria de cada pregunta

            xmlSerializer.startTag("", "question");
            xmlSerializer.attribute("", "type", p.getCategoria());

            xmlSerializer.startTag("", "category");
            xmlSerializer.text(p.getCategoria());
            xmlSerializer.endTag("", "category");

            xmlSerializer.endTag("", "question");

            //Pregunta de eleccion multiple

            xmlSerializer.startTag("", "question");
            xmlSerializer.attribute("", "type", "multichoice");

            xmlSerializer.startTag("", "name");
            xmlSerializer.text(p.getEnunciado());
            xmlSerializer.endTag("", "name");

            xmlSerializer.startTag("","questiontext");
            xmlSerializer.attribute("", "format", "html");
            xmlSerializer.text(p.getEnunciado());
            xmlSerializer.startTag("","file");
            xmlSerializer.attribute("", "name", p.getFoto());
            xmlSerializer.attribute("", "path", "/");
            xmlSerializer.attribute("", "encoding", "base64");
            xmlSerializer.endTag("", "file");
            xmlSerializer.endTag("", "questiontext");

            xmlSerializer.startTag("","answernumbering");
            xmlSerializer.endTag("", "answernumbering");

            xmlSerializer.startTag("","answer");
            xmlSerializer.attribute("","fraction", "100");
            xmlSerializer.attribute("", "format", "html");
            xmlSerializer.text(p.getRespuestaCorrecta());
            xmlSerializer.endTag("", "answer");

            xmlSerializer.startTag("","answer");
            xmlSerializer.attribute("","fraction", "0");
            xmlSerializer.attribute("", "format", "html");
            xmlSerializer.text(p.getRespuestaIncorrecta1());
            xmlSerializer.endTag("", "answer");

            xmlSerializer.startTag("","answer");
            xmlSerializer.attribute("","fraction", "0");
            xmlSerializer.attribute("", "format", "html");
            xmlSerializer.text(p.getRespuestaIncorrecta2());
            xmlSerializer.endTag("", "answer");

            xmlSerializer.startTag("","answer");
            xmlSerializer.attribute("","fraction", "0");
            xmlSerializer.attribute("", "format", "html");
            xmlSerializer.text(p.getRespuestaIncorrecta3());
            xmlSerializer.endTag("", "answer");

            xmlSerializer.endTag("","question");
        }

        //end tag <file>
        xmlSerializer.endTag("","quiz");



        xmlSerializer.endDocument();

        return writer.toString();


    }



    public static int consultarNumeroPreguntas(Context myContext){

        int total = 0;

        DatabaseSQLiteHelper DBPreguntas = new DatabaseSQLiteHelper(myContext, ConstantesGlobales.nombreDB, null, 1);

        SQLiteDatabase db = DBPreguntas.getReadableDatabase();


        Cursor c = db.rawQuery(" SELECT COUNT(*) FROM " + ConstantesGlobales.nombreTabla + "", null);

        c.moveToFirst();

        total = c.getInt(0);

        //Cerramos la base de datos
        db.close();

        return total;

    }

    public static int consultarNumeroCategorias(Context myContext) {

        DatabaseSQLiteHelper DBPreguntas = new DatabaseSQLiteHelper(myContext, "DBPreguntas", null, 1);

        SQLiteDatabase db = DBPreguntas.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT DISTINCT COUNT(*) " + ConstantesGlobales.columnaCategoria + " FROM " + ConstantesGlobales.nombreTabla + " ", null);

        c.moveToFirst();

        return c.getInt(0);

    }


}
