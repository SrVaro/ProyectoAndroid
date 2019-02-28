package com.example.varo.proyectoandroid;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

public class Resumen extends AppCompatActivity {

    private TextView preguntasTotales;

    private TextView categoriasTotales;

    private Context myContext;

    private static final String TAG="Resumen";
    final private int CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 123;
    final private int CODE_CAMERA_PERMISSION = 1234;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_resumen, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.action_buscar:
                Log.i("ActionBar", "Buscar!");
                return true;

            case R.id.action_preguntas:
                Log.i("ActionBar", "Listado_Preguntas!");
                Intent it1 = new Intent(Resumen.this, Listado_Preguntas.class);
                startActivity(it1);
                return true;

            case R.id.action_acerca:
                Log.i("ActionBar", "Acerca de!");
                Intent it2 = new Intent(Resumen.this, AcercaDe.class);
                startActivity(it2);
                return true;

            case R.id.action_settings:
                Log.i("ActionBar", "Settings!");
                return true;

            case R.id.action_importar:
                Log.i("ActionBar", "Importar!");

                return true;

            case R.id.action_exportar:
                Log.i("ActionBar", "Exportar!");

                exportarXML();

                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_resumen);


        myContext = this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            verifyPermission();
        }



        preguntasTotales = findViewById(R.id.preguntasTotales);

        categoriasTotales = findViewById(R.id.categoriasTotales);


        importarXML();

        }

    @Override
    protected void onResume() {
        super.onResume();

        preguntasTotales.setText("Hay un total de " + Repositorio.consultarNumeroPreguntas(this) + " preguntas");

        categoriasTotales.setText("Hay un total de " + Repositorio.consultarNumeroCategorias(this) + " categorias");
    }



    public void importarXML(){

        //Creamos las variables de la pregunta que vamos a importar
        Pregunta p;
        String enunciado="";
        String categoria="";
        String correcta="";
        String incorrecta="";
        String incorrecta2="";
        String incorrecta3="";
        String foto=null;

        int contador=0;

        //Recibimos el intent
        Intent receivedIntent = getIntent();

        //Si el intent es distinto de null
        if(receivedIntent != null) {
            //Recogemos la accion del intent
            String receivedAction = receivedIntent.getAction();

            //Si la accion del inten es igual a android.intent.action.SEND
            //Se ejecutará la lectura del archivo
            if(receivedAction == "android.intent.action.SEND"){

                Uri data = receivedIntent.getParcelableExtra(Intent.EXTRA_STREAM);

                try {

                    InputStream fis = getContentResolver().openInputStream(data);
                    XmlPullParserFactory xppf = XmlPullParserFactory.newInstance();
                    xppf.setNamespaceAware(false);
                    XmlPullParser parser = xppf.newPullParser();
                    parser.setInput(fis, null);

                    parser.nextTag();
                    parser.require(XmlPullParser.START_TAG, null, "quiz");

                    //Leyendo el documento

                    int act;
                    String tag="";

                    while((act=parser.next()) != XmlPullParser.END_DOCUMENT) {

                        switch (act) {
                            case XmlPullParser.START_TAG:

                                tag = parser.getName();

                                break;

                            case XmlPullParser.TEXT:
                                if(tag.equals("text"))
                                {

                                    if(contador==0){
                                        categoria= parser.getText();
                                        System.out.println("categoria: "+ categoria);
                                        contador++;
                                    }
                                    else if(contador==1){
                                        enunciado= parser.getText();
                                        System.out.println("Enunciado: "+ enunciado);
                                        contador++;

                                    }
                                    else if(contador==2){

                                        contador++;
                                    }

                                    else if(contador==3){
                                        correcta= parser.getText();
                                        System.out.println("Correcta: "+ correcta);
                                        contador++;

                                    }
                                    else if(contador==4){
                                        incorrecta= parser.getText();
                                        System.out.println("Incorrecta1: "+ incorrecta);
                                        contador++;

                                    }
                                    else if(contador==5){
                                        incorrecta2= parser.getText();
                                        System.out.println("Incorrecta2: "+ incorrecta2);


                                        contador++;

                                    }
                                    else if(contador==6){
                                        incorrecta3= parser.getText();
                                        System.out.println("Incorrecta3: "+ incorrecta3);


                                        //Como es el último dato que recuperamos de la pregunta la añadimos a la base de datos

                                        if(foto == null){

                                            p= new Pregunta(enunciado,categoria,correcta,incorrecta,incorrecta2,incorrecta3);
                                            Repositorio.insertarPregunta(myContext ,p);
                                            System.out.println("Insertado correctamente en la BD");

                                        }else{
                                            p= new Pregunta(enunciado,categoria,correcta,incorrecta,incorrecta2,incorrecta3, foto);
                                            Repositorio.insertarPreguntaConFoto(myContext ,p);
                                            System.out.println("Insertado correctamente en la BD");

                                        }

                                        contador=0;

                                    }
                                    else{
                                        System.out.println("Error insertando a la BD");
                                    }

                                }

                                if(tag.equals("file"))
                                {
                                    foto= parser.getText();
                                    System.out.println("Imagen: "+ foto);

                                }


                                tag="";
                                break;

                            case XmlPullParser.END_TAG:
                                if(parser.getName().equals("question"))
                                {
                                    System.out.println("Archivo finalizado");
                                }
                                break;
                        }

                    }



                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
        }


    }




    private void exportarXML(){

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/preguntasExportar");
        String fname = "preguntas.xml";
        File file = new File (myDir, fname);
        try
        {


            if (!myDir.exists()) {
                myDir.mkdirs();

            }
            if (file.exists ())
                file.delete ();


            FileWriter fw=new FileWriter(file);
            //Escribimos en el fichero un String
            Log.d("aqui estoy",Repositorio.CreateXMLString(myContext));
            fw.write(Repositorio.CreateXMLString(myContext));


            //Cierro el stream
            fw.close();



        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al escribir fichero a memoria interna");
        }





        String cadena = myDir.getAbsolutePath()+"/"+fname;
        Uri path = Uri.parse("file://"+cadena);

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","aalluque@iesfranciscodelosrios.es", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Preguntas para Moodle");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Adjunto el archivo para importarlas a Moodle");
        emailIntent .putExtra(Intent.EXTRA_STREAM, path);
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void verifyPermission() {
        int permsRequestCode = 100;
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        int writePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int cameraPermission = checkSelfPermission(Manifest.permission.CAMERA);

        if (cameraPermission == PackageManager.PERMISSION_GRANTED && writePermission == PackageManager.PERMISSION_GRANTED) {
            //se realiza metodo si es necesario...
        } else {
            requestPermissions(perms, permsRequestCode);
        }
    }


    //Maneja la respuesta del compruebaPermisos
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {

            case 100:

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                } else {

                    Log.e("Permisos: ", "Rechazados");

                }

                if (grantResults.length > 0  &&  grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                } else {

                    Log.e("Permisos: ", "Rechazados");

                }

                break;
        }
    }


}
