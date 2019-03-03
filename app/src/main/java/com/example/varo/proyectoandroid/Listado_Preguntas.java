package com.example.varo.proyectoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Listado_Preguntas extends AppCompatActivity {

    ArrayList<Pregunta> listaPreguntas = new ArrayList<>();

    private static final String TAG = "Listado_Preguntas_Activity";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_general, menu);
        return true;


    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.action_buscar:
                MyLog.i("ActionBar", "Buscar!");
                return true;

            case R.id.action_preguntas:
                MyLog.i("ActionBar", "Listado_Preguntas!");
                return true;

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
        setContentView(R.layout.activity_preguntas);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Boton flotante
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Intent it = new Intent(Listado_Preguntas.this, Nueva_Pregunta.class);
                it.putExtra("editar", false );
                startActivity(it);
            }
        });
    }

    @Override
    protected void onResume() {

        MyLog.d(TAG, "Iniciando OnResume");

        super.onResume();

        // Se rellena el ArrayList de Preguntas con las recuperadas de la base de datos
        listaPreguntas = Repositorio.recuperarPreguntas(this);
        final RecyclerView recyclerView = findViewById(R.id.rv);

        // Si la lista de preguntas no es nula
        if (listaPreguntas != null){

            TextView informacion = findViewById(R.id.textView);
            Adapter ad = new Adapter(listaPreguntas);

            // Al seleccionar un card se pasa a la vista Nueva_Pregunta
            ad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Acción al pulsar el elemento
                    int position = recyclerView.getChildAdapterPosition(v);

                    Intent it = new Intent(Listado_Preguntas.this, Nueva_Pregunta.class);

                    it.putExtra("codigo", listaPreguntas.get(position).getCodigo() );
                    it.putExtra("editar", true );

                    startActivity(it);

                }
            });

            // Asocia el Adaptador al RecyclerView
            recyclerView.setAdapter(ad);

            // Muestra el RecyclerView en vertical
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

        } else {
            TextView informacion = findViewById(R.id.textView);
            informacion.setVisibility(View.VISIBLE);
        }

        MyLog.d(TAG, "Finalizando OnResume");

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
