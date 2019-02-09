package com.example.varo.proyectoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.TextView;

public class Resumen extends AppCompatActivity {

    private TextView preguntasTotales;

    private TextView categoriasTotales;

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

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_resumen);

        preguntasTotales = findViewById(R.id.preguntasTotales);

        categoriasTotales = findViewById(R.id.categoriasTotales);


        }

    @Override
    protected void onResume() {
        super.onResume();

        preguntasTotales.setText("Hay un total de " + Repositorio.consultarNumeroPreguntas(this) + " preguntas");

        categoriasTotales.setText("Hay un total de " + Repositorio.consultarNumeroCategorias(this) + " categorias");
    }
}
