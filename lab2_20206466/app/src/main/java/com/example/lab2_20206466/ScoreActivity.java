package com.example.lab2_20206466;


import android.graphics.Color;
import android.widget.Button;
import android.widget.TextView;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;

public class ScoreActivity extends AppCompatActivity {
    Button buttonAnterior, buttonSiguiente;
    TextView textViewPuntajeTotal, textViewTema;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        // Inicializamos
        buttonAnterior = findViewById(R.id.buttonAnterior);
        buttonSiguiente = findViewById(R.id.buttonSiguiente);
        textViewPuntajeTotal = findViewById(R.id.textViewPuntajeTotal);
        textViewTema = findViewById(R.id.textViewTema);


        // obtenemos los datos del otro activity
        int puntajeTotal = getIntent().getIntExtra("puntaje", 0);
        String tema = getIntent().getStringExtra("tema");
        long tiempoTotal = getIntent().getLongExtra("tiempo", 0);

        // Establecemos el nombre del tema y el puntaje obtenido
        textViewTema.setText(tema);
        textViewPuntajeTotal.setText(String.valueOf(puntajeTotal));

        // Cambiar el color  de fondo según el puntaje. Si es mayor a cero debe ser verde.
        if (puntajeTotal > 0) {
            textViewPuntajeTotal.setBackgroundColor(Color.GREEN);
            textViewPuntajeTotal.setTextColor(Color.WHITE);
        } else {
            textViewPuntajeTotal.setBackgroundColor(Color.RED);
            textViewPuntajeTotal.setTextColor(Color.WHITE);
        }


        /*guardamos el historial de partidas (no funciono :c)*/
        SharedPreferences prefs = getSharedPreferences("historial", MODE_PRIVATE);
        int i = prefs.getInt("total_juegos", 0) + 1;
        String nuevo = "Juego " + i + ": " + tema + " | Tiempo: " + tiempoTotal + "s | Puntaje: " + puntajeTotal;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("juego_" + i, nuevo);
        editor.putInt("total_juegos", i);
        editor.apply();

        /*El boton siguiente se deshabilita, pues no hay mas vistas*/
        buttonAnterior.setOnClickListener(v -> {
            finish();
        });
        buttonSiguiente.setEnabled(false);
    }
}