package com.example.varo.proyectoandroid;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Log.d("Debug", "Actividad iniciada");

        // Pasa a la actividad Resumen al pasar 5000 ms (5s)
        new Handler().postDelayed(new Runnable(){
            public void run(){
                Intent it = new Intent(SplashScreen.this, Resumen.class);
                startActivity(it);
                finish();
            }
        },5000);


    }
}
