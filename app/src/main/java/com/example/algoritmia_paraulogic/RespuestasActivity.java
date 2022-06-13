package com.example.algoritmia_paraulogic;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author Álvaro Pimentel Lorente
 */

public class RespuestasActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respuestas);
        // Asignamos un scrollbar al textview para visualizar
        // todas las soluciones si es necsario
        TextView t = findViewById(R.id.todas_posibles);
        t.setMovementMethod(new ScrollingMovementMethod());

        // Recuperamos el intent que ha lanzado la actividad
        Intent intent = getIntent();

        // Extraemos el texto del intent
        String message = intent.getStringExtra(MainActivity.RESPUESTAS);

        // Asignamos el texto al textview
        t.setText(Html.fromHtml(message));
}
}
