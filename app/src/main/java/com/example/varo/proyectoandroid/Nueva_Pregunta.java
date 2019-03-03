package com.example.varo.proyectoandroid;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

public class Nueva_Pregunta extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Nueva_Pregunta_Activity";

    private Context myContext;

    private ConstraintLayout constraint;

    private Uri uri;

    private Bitmap bitmap;

    private ArrayAdapter<String> adapter;

    private ArrayList<String> categorias;

    private static final int REQUEST_CAPTURE_IMAGE = 200;
    private static final int REQUEST_SELECT_IMAGE = 201;

    final String pathFotos = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/demoAndroid/";

    private EditText tituloPregunta;
    private EditText respuestaCorrecta;
    private EditText respuestaIncorrecta1;
    private EditText respuestaIncorrecta2;
    private EditText respuestaIncorrecta3;

    private ImageView imageViewFoto;

    private Button botonAgregarCategoria;
    private Button botonGaleria;
    private Button botonCamara;
    private Button botonAceptar;
    private Button botonEliminar;

    private Spinner categoriaSpinner;

    private int codigo;

    private boolean editar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_general, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.action_acerca:
                MyLog.i("ActionBar", "Acerca de!");
                return true;

            case R.id.action_settings:
                MyLog.i("ActionBar", "Settings!");
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_pregunta);

        myContext = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Se rellena el ArrayList con todas las categorias de la base de datos
        categorias = Repositorio.consultaCategorias(myContext);

        // Obtenemos el booleano que de la pantalla anterior que nos indica si el usuario quiere editar o crear una nueva pregunta
        editar = getIntent().getExtras().getBoolean("editar");

        // Guardamos todos los elementos de la interfaz en variables para usarlas posteriormente
        constraint = findViewById(R.id.constraintLayoutMainActivity);

        tituloPregunta = findViewById(R.id.titulo);
        respuestaCorrecta = findViewById(R.id.respuesta_correcta);
        respuestaIncorrecta1 = findViewById(R.id.respuesta_incorrecta1);
        respuestaIncorrecta2 = findViewById(R.id.respuesta_incorrecta2);
        respuestaIncorrecta3 = findViewById(R.id.respuesta_incorrecta3);
        imageViewFoto = findViewById(R.id.foto);

        botonGaleria = findViewById(R.id.galeria);
        botonAgregarCategoria = findViewById(R.id.agregarCategoria);
        botonCamara = findViewById(R.id.camara);
        botonAceptar = findViewById(R.id.button_aceptar);
        botonEliminar = findViewById(R.id.button_eliminar);

        botonEliminar.setEnabled(false);

        categoriaSpinner = findViewById(R.id.categoria_spinner);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categorias);
        categoriaSpinner.setAdapter(adapter);

        botonAgregarCategoria.setOnClickListener(this);
        botonCamara.setOnClickListener(this);
        botonGaleria.setOnClickListener(this);
        botonAceptar.setOnClickListener(this);
        botonEliminar.setOnClickListener(this);
        imageViewFoto.setOnClickListener(this);

        // Si la aplicacion esta en modo editar, rellenamos los EditText con la informacion de la pregunta seleccionada
        if (editar) {

            // En modo editar se activa el boton de eliminar
            botonEliminar.setEnabled(true);

            // Se cambia el texto del boton aceptar por editar
            botonAceptar.setText(R.string.editar);

            // Obtenemos el codigo de la pregunta que el usuario ha seleccionado
            codigo = getIntent().getExtras().getInt("codigo");

            // Recuperamos la pregunta de la base de datos
            Pregunta preguntaEditar = Repositorio.recuperarPreguntaCodigo(myContext, codigo);

            // Rellenamos los campos de texto con la informacion de la pregunta recuperada
            tituloPregunta.setText(preguntaEditar.getEnunciado());
            respuestaCorrecta.setText(preguntaEditar.getRespuestaCorrecta());
            respuestaIncorrecta1.setText(preguntaEditar.getRespuestaIncorrecta1());
            respuestaIncorrecta2.setText(preguntaEditar.getRespuestaIncorrecta2());
            respuestaIncorrecta3.setText(preguntaEditar.getRespuestaIncorrecta3());
            categoriaSpinner.setSelection(categorias.indexOf(preguntaEditar.getCategoria()));

            // Decodificamos la cadena en base64 de la base de datos, la convertimos en un bitmap y se la asignamos al imageView
            byte[] decodedString = Base64.decode(preguntaEditar.getFoto(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageViewFoto.setImageBitmap(decodedByte);
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            // Acciones que se realizan al pulsar el boton de aceptar
            case R.id.button_aceptar:

                boolean camposVacios = false;

                // Se comprueba que los campos de texto no estan vacios
                camposVacios = NotEmpty(tituloPregunta, respuestaCorrecta, respuestaIncorrecta1, respuestaIncorrecta2, respuestaIncorrecta3);

                // Si el spinner o algun campo de texto esta vacio se crea un snack bar para darle informacion al usuario
                if (camposVacios == true || categorias.isEmpty()){

                    Snackbar.make(view, "Debes rellenar todos los campos", Snackbar.LENGTH_LONG).show();

                }else {

                    //Se inicializan las variables con la informacion de los campos de texto
                    String enunciado = tituloPregunta.getText().toString();
                    String resCorr = respuestaCorrecta.getText().toString();
                    String resIncorr1 = respuestaIncorrecta1.getText().toString();
                    String resIncorr2 = respuestaIncorrecta2.getText().toString();
                    String resIncorr3 = respuestaIncorrecta3.getText().toString();
                    String categoria = categoriaSpinner.getSelectedItem().toString();

                    ImageView imageView = findViewById(R.id.foto);

                    BitmapDrawable bmDr = (BitmapDrawable) imageView.getDrawable();

                    if (bmDr != null){
                        bitmap=bmDr.getBitmap();
                    }else{
                        bitmap=null;
                    }



                    // Si la app se encuentra en modo creacion, se crea una nueva pregunta y se guarda en el repositorio
                    if (editar == false) {

                        Animation animBtn = AnimationUtils.loadAnimation(myContext, R.anim.fadeout);

                        botonAceptar.startAnimation(animBtn);

                        Pregunta nuevaPregunta = new Pregunta(enunciado, categoria, resCorr,
                                resIncorr1, resIncorr2, resIncorr3, conversorImagen64(bitmap));

                        Repositorio.insertarPreguntaConFoto(myContext, nuevaPregunta);

                        finish();




                    }
                    //Si la app se encuentra en modo edicion, se crea una nueva pregunta se actualiza la antigua en el repositorio
                    else {

                        Animation animBtn = AnimationUtils.loadAnimation(myContext, R.anim.fadeout);

                        botonAceptar.startAnimation(animBtn);


                        Pregunta preguntaActualizada = new Pregunta(codigo, enunciado, categoria, resCorr,
                                resIncorr1, resIncorr2, resIncorr3, conversorImagen64(bitmap));

                        Repositorio.editarPregunta(myContext, preguntaActualizada);

                        finish();
                    }

                }

                break;

            case R.id.agregarCategoria:

                LayoutInflater layoutActivity = LayoutInflater.from(myContext);
                View viewAlertDialog = layoutActivity.inflate(R.layout.agregar_categoria, null);

                // Creacion del alert dialog
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(myContext);

                // Asignación del AlertDialog a la vista
                alertDialog.setView(viewAlertDialog);

                final EditText dialogInput = (EditText) viewAlertDialog.findViewById(R.id.nuevaCategoria);

                alertDialog
                        .setCancelable(false)
                        .setPositiveButton(getResources().getString(R.string.agregar),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {

                                        // Se añade la categoria introducida al ArrayList de categorias
                                        categorias.add(dialogInput.getText().toString());

                                        // Se eliminan posibles categorias duplicadas de la lista
                                        HashSet<String> quitarDuplicados = new HashSet<>(categorias);

                                        categorias =  new ArrayList<>(quitarDuplicados);

                                        // Inicializamos de nuevo el adapter con el nuevo array list de categorias
                                        adapter = new ArrayAdapter<String>(myContext, R.layout.support_simple_spinner_dropdown_item, categorias);

                                        // Se llena el spinner con la nueva categoria introducida
                                        categoriaSpinner.setAdapter(adapter);

                                        // Se selecciona la nueva categoria
                                        categoriaSpinner.setSelection(adapter.getPosition(dialogInput.getText().toString()));
                                    }
                                })
                        .setNegativeButton(getResources().getString(R.string.cancelar),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        dialogBox.cancel();
                                    }
                                })
                        .create()
                        .show();

                break;

            case R.id.camara:

                iniciarCamara();

                break;

            case R.id.galeria:

                iniciarGaleria();

                break;

            case R.id.button_eliminar:

               comprobarEliminar();

               break;

            case R.id.foto:

                ImageView imageView = findViewById(R.id.foto);

                imageView.setImageDrawable(Drawable.createFromPath("@android:drawable/ic_menu_gallery"));

                bitmap= null;

                break;



        }

    }

    /**
     * Se abre un alert dialog para que el usuario confirme la eliminacion de la pregunta
     */
    private void comprobarEliminar(){

        AlertDialog.Builder builder = new AlertDialog.Builder( myContext);

        builder.setCancelable(false);

        builder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Repositorio.eliminarPregunta(myContext, codigo);

                finish();

            }
        });
        builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });


        builder.setMessage(R.string.alert_eliminar)
                .setTitle(R.string.eliminar);

        AlertDialog dialog = builder.create();

        dialog.show();

    }


    /**
     * Se inicia la camara del dispositivo para tomar una fotografia
     */
    private void iniciarCamara() {

        try {

                // Se crea el directorio para las fotografías
                File dirFotos = new File(pathFotos);
                dirFotos.mkdirs();

                // Se crea el archivo para almacenar la fotografía
                File fileFoto = File.createTempFile(getFileCode(), ".jpg", dirFotos);

                // Se crea el objeto Uri a partir del archivo
                // A partir de la API 24 se debe utilizar FileProvider para proteger
                // con permisos los archivos creados
                // Con estas funciones podemos evitarlo
                // https://stackoverflow.com/questions/42251634/android-os-fileuriexposedexception-file-jpg-exposed-beyond-app-through-clipdata
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                uri = Uri.fromFile(fileFoto);
                MyLog.d("foto hecha", uri.getPath().toString());

                // Se crea la comunicación con la cámara
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                // Se le indica dónde almacenar la fotografía
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                // Se lanza la cámara y se espera su resultado
                startActivityForResult(cameraIntent, REQUEST_CAPTURE_IMAGE);

            } catch (IOException ex) {

                MyLog.d("Foto", "Error: " + ex);
                ConstraintLayout constraintLayout = findViewById(R.id.constraintLayoutMainActivity);
                Snackbar snackbar = Snackbar
                        .make(constraintLayout, "Error", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }

    /**
     * Se abre el intent de la galeria
     */
    private void iniciarGaleria(){
        // Se le pide al sistema una imagen del dispositivo
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(intent, "Elige una foto"),
                REQUEST_SELECT_IMAGE);
    }

    /**
     *
     * @return Devuelve un codigo a partir de la fecha y hora del telefono
     */
    private String getFileCode()
    {
        // Se crea un código a partir de la fecha y hora
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss", java.util.Locale.getDefault());
        String date = dateFormat.format(new Date());
        // Se devuelve el código
        return "pic_" + date;
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case (REQUEST_CAPTURE_IMAGE):

                if(resultCode == Activity.RESULT_OK){

                    ImageView imageView = findViewById(R.id.foto);

                    imageView.setImageURI(uri);

                    imageView.setRotation(90);


                    // Se le envía un broadcast a la Galería para que se actualice
                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    mediaScanIntent.setData(uri);
                    sendBroadcast(mediaScanIntent);

                } else if (resultCode == Activity.RESULT_CANCELED) {
                    // Se borra el archivo temporal
                    File file = new File(uri.getPath());
                    file.delete();
                }
                break;

            case (REQUEST_SELECT_IMAGE):
                if (resultCode == Activity.RESULT_OK) {
                    // Se carga la imagen desde un objeto Bitmap
                    Uri selectedImage = data.getData();
                    String selectedPath = selectedImage.getPath();

                    if (selectedPath != null) {
                        // Se leen los bytes de la imagen
                        InputStream imageStream = null;

                        try {

                            imageStream = getContentResolver().openInputStream(selectedImage);

                        } catch (FileNotFoundException e) {

                            e.printStackTrace();

                        }

                        // Se transformam los bytes de la imagen a un Bitmap
                        bitmap = BitmapFactory.decodeStream(imageStream);

                        // Se carga el Bitmap en el ImageView
                        ImageView imageView = findViewById(R.id.foto);
                        imageView.setImageBitmap(bitmap);

                    }
                }
                break;
        }
    }

    /**
     *
     * @param bm
     * @return Devuelve un string con la imagen en base64
     */
    public static String conversorImagen64(Bitmap bm){
        String encodedImage="";
        if(bm!=null) {
            Bitmap resized = Bitmap.createScaledBitmap(bm, 200, 200, true);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            resized.compress(Bitmap.CompressFormat.JPEG, 100, baos);//bmisthebitmapobject
            byte[] b = baos.toByteArray();
            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            return encodedImage;
        }else{
            return encodedImage;
        }

    }

    /**
     *
     * @param Field
     * @return
     *
     * Recibe una lista de EditText y comprueba uno a uno si estan vacios, en cuanto encuentra uno vacio
     * devuelve true, si no hay ninguno vacio devuelve falso.
     */
    public boolean NotEmpty(EditText ...Field) {
        for(EditText field : Field){
            if(field.getText().toString().isEmpty()){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    @Override
    protected void onStart() {
        MyLog.d(TAG, "Iniciando OnStart");
        super.onStart();
        MyLog.d(TAG, "Finalizando OnStart");
    }

    @Override
    protected void onResume() {
        MyLog.d(TAG, "Iniciando OnResume");
        super.onResume();
        MyLog.d(TAG, "Finalizando OnResume");
    }

    @Override
    protected void onPause() {
        MyLog.d(TAG, "Iniciando OnPause");
        super.onPause();
        MyLog.d(TAG, "Finalizando OnPause");
    }

    @Override
    protected void onStop() {
        MyLog.d(TAG, "Iniciando OnStop");
        super.onStop();
        MyLog.d(TAG, "Finalizando OnStop");
    }

    @Override
    protected void onRestart() {
        MyLog.d(TAG, "Iniciando OnRestart");
        super.onRestart();
        MyLog.d(TAG, "Finalizando OnRestart");
    }

    @Override
    protected void onDestroy() {
        MyLog.d(TAG, "Iniciando OnDestroy");
        super.onDestroy();
        MyLog.d(TAG, "Finalizando OnDestroy");
    }


}
