package com.example.varo.proyectoandroid;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Clase para almacenar el adaptador con los datos
 * de los acontecimientos que va a mostrar
 * el RecyclerView
 *
 * Hay que añadir al proyecto la siguiente
 * dependencia en el archivo /app/build.gradle
 * 'com.android.support:recyclerview-v7:+
 */

public class Adapter extends RecyclerView.Adapter<Adapter.AcontecimientoViewHolder>
        implements View.OnClickListener {

    private ArrayList<Pregunta> items;
    private View.OnClickListener listener;

    // Clase interna:
    // Se implementa el ViewHolder que se encargara
    // de almacenar la vista del elemento y sus datos.
    public static class AcontecimientoViewHolder
            extends RecyclerView.ViewHolder {

        private TextView TextView_categoria;
        private TextView TextView_pregunta;

        public AcontecimientoViewHolder(View itemView) {
            super(itemView);
            TextView_categoria = (TextView) itemView.findViewById(R.id.categoria);
            TextView_pregunta = (TextView) itemView.findViewById(R.id.enunciado);
        }

        public void AcontecimientoBind(Pregunta item) {
            TextView_categoria.setText(item.getCategoria());
            TextView_pregunta.setText(item.getEnunciado());
        }
    }

    // Contruye el objeto adaptador recibiendo la lista de datos
    public Adapter(@NonNull ArrayList<Pregunta> items) {
        this.items = items;
    }

    // Se encarga de crear los nuevos objetos ViewHolder necesarios
    // para los elementos de la coleccion.
    // Infla la vista del layout, crea y devuelve el objeto ViewHolder
    @Override
    public AcontecimientoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row, parent, false);
        row.setOnClickListener(this);

        AcontecimientoViewHolder avh = new AcontecimientoViewHolder(row);
        return avh;
    }

    // Se encarga de actualizar los datos de un ViewHolder ya existente.
    @Override
    public void onBindViewHolder(AcontecimientoViewHolder viewHolder, int position) {
        Pregunta item = items.get(position);
        viewHolder.AcontecimientoBind(item);
    }

    // Indica el numero de elementos de la coleccion de datos.
    @Override
    public int getItemCount() {
        return items.size();
    }

    // Asigna un listener al elemento
    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if(listener != null)
            listener.onClick(view);
    }

}