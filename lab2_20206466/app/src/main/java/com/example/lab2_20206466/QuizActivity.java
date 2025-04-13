package com.example.lab2_20206466;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;

public class QuizActivity extends AppCompatActivity {

    private Button btnOpcionA, btnOpcionB, btnOpcionC;
    private Button btnSiguiente, btnAnterior;
    private TextView txtPregunta, txtPuntaje;

    private ManejadorQuiz manejador;
    private String temaActual;
    private long tiempoInicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Configurar botón de retroceso en la barra
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Inicializar elementos de la interfaz
        inicializarVistas();

        // Preparar el quiz
        iniciarQuiz();
    }

    private void inicializarVistas() {
        btnOpcionA = findViewById(R.id.buttonA);
        btnOpcionB = findViewById(R.id.buttonB);
        btnOpcionC = findViewById(R.id.buttonC);
        btnSiguiente = findViewById(R.id.buttonSiguiente);
        btnAnterior = findViewById(R.id.buttonAnterior);
        txtPregunta = findViewById(R.id.textViewPregunta);
        txtPuntaje = findViewById(R.id.textViewPuntaje);
    }

    private void iniciarQuiz() {
        tiempoInicio = System.currentTimeMillis();
        temaActual = getIntent().getStringExtra("tema");

        // Inicializar el manejador del quiz con el tema seleccionado
        manejador = new ManejadorQuiz(temaActual);

        // Configuramos los listeners de los botones
        configurarListeners();

        // Mostrar la primera pregunta
        mostrarPreguntaActual();
    }

    private void configurarListeners() {
        // Listener para las opciones de respuesta
        View.OnClickListener listenerOpciones = v -> {
            Button botonSeleccionado = (Button) v;
            int indiceSeleccionado = obtenerIndiceOpcion(botonSeleccionado);

            manejador.responderPregunta(indiceSeleccionado);
            actualizarUIRespuesta(indiceSeleccionado);
        };

        btnOpcionA.setOnClickListener(listenerOpciones);
        btnOpcionB.setOnClickListener(listenerOpciones);
        btnOpcionC.setOnClickListener(listenerOpciones);

        // Listener para botón Siguiente
        btnSiguiente.setOnClickListener(v -> {
            if (manejador.siguientePregunta()) {
                mostrarPreguntaActual();
            } else {
                terminarQuiz();
            }
        });

        // Listener para botón Anterior
        btnAnterior.setOnClickListener(v -> {
            manejador.anteriorPregunta();
            mostrarPreguntaActual();
        });
    }

    private int obtenerIndiceOpcion(Button boton) {
        if (boton == btnOpcionA) return 0;
        if (boton == btnOpcionB) return 1;
        return 2;
    }

    private void actualizarUIRespuesta(int indiceSeleccionado) {
        // Deshabilitar botones de opciones
        btnOpcionA.setEnabled(false);
        btnOpcionB.setEnabled(false);
        btnOpcionC.setEnabled(false);

        // Habilitar botón Siguiente
        btnSiguiente.setEnabled(true);

        // Actualizar puntaje
        txtPuntaje.setText("Puntaje: " + manejador.obtenerPuntaje());

        // Resaltar respuestas
        resaltarRespuestas(indiceSeleccionado);
    }

    private void resaltarRespuestas(int indiceSeleccionado) {
        resetearColoresBotones();

        if (manejador.esRespuestaCorrecta(indiceSeleccionado)) {
            obtenerBotonPorIndice(indiceSeleccionado).setBackgroundColor(Color.GREEN);
        } else {
            obtenerBotonPorIndice(indiceSeleccionado).setBackgroundColor(Color.RED);
            obtenerBotonPorIndice(manejador.obtenerIndiceRespuestaCorrecta())
                    .setBackgroundColor(Color.GREEN);
        }
    }

    private Button obtenerBotonPorIndice(int indice) {
        switch (indice) {
            case 0: return btnOpcionA;
            case 1: return btnOpcionB;
            default: return btnOpcionC;
        }
    }

    private void resetearColoresBotones() {
        btnOpcionA.setBackgroundColor(Color.LTGRAY);
        btnOpcionB.setBackgroundColor(Color.LTGRAY);
        btnOpcionC.setBackgroundColor(Color.LTGRAY);
    }

    private void mostrarPreguntaActual() {
        // Obtener pregunta actual
        String pregunta = manejador.obtenerPreguntaActual();
        String[] opciones = manejador.obtenerOpcionesActuales();

        // Mostrar en pantalla
        txtPregunta.setText("Pregunta " + (manejador.obtenerNumeroPregunta() + 1) + ": " + pregunta);
        btnOpcionA.setText(opciones[0]);
        btnOpcionB.setText(opciones[1]);
        btnOpcionC.setText(opciones[2]);

        // Resetear UI
        resetearUIPregunta();

        // Si ya fue respondida, mostrar respuesta
        if (manejador.preguntaRespondida()) {
            mostrarRespuestaAnterior();
        }
    }

    private void resetearUIPregunta() {
        btnOpcionA.setEnabled(true);
        btnOpcionB.setEnabled(true);
        btnOpcionC.setEnabled(true);
        btnSiguiente.setEnabled(false);
        resetearColoresBotones();
        btnAnterior.setEnabled(manejador.hayPreguntaAnterior());
    }

    private void mostrarRespuestaAnterior() {
        btnOpcionA.setEnabled(false);
        btnOpcionB.setEnabled(false);
        btnOpcionC.setEnabled(false);
        btnSiguiente.setEnabled(true);

        resaltarRespuestas(manejador.obtenerRespuestaUsuario());
    }

    private void terminarQuiz() {
        long tiempoTranscurrido = (System.currentTimeMillis() - tiempoInicio) / 1000;
        guardarResultado(tiempoTranscurrido);

        Intent intent = new Intent(this, ScoreActivity.class);
        intent.putExtra("tiempo", tiempoTranscurrido);
        intent.putExtra("puntaje", manejador.obtenerPuntaje());
        intent.putExtra("tema", temaActual);
        startActivity(intent);
        finish();
    }

    private void guardarResultado(long tiempo) {
        SharedPreferences prefs = getSharedPreferences("historial", MODE_PRIVATE);
        int numeroJuego = prefs.getInt("total_juegos", 0) + 1;

        prefs.edit()
                .putString("juego_" + numeroJuego, "Juego " + numeroJuego + ": " + temaActual)
                .putInt("juego_" + numeroJuego + "_puntaje", manejador.obtenerPuntaje())
                .putLong("juego_" + numeroJuego + "_tiempo", tiempo)
                .putInt("total_juegos", numeroJuego)
                .apply();
    }

    // Métodos del menú y botón de retroceso
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_question, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_estadisticas) {
            startActivity(new Intent(this, StatsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        registrarCancelacion();
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        registrarCancelacion();
        super.onBackPressed();
    }

    private void registrarCancelacion() {
        SharedPreferences prefs = getSharedPreferences("historial", MODE_PRIVATE);
        int numeroJuego = prefs.getInt("total_juegos", 0) + 1;

        prefs.edit()
                .putString("juego_" + numeroJuego, "Juego " + numeroJuego + ": Canceló")
                .putInt("total_juegos", numeroJuego)
                .apply();
    }
}