package com.example.lab2_20206466;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class StatsActivity extends AppCompatActivity {

    TextView textViewHistorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stats);

        // Habilitamos la opcion de volver
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        textViewHistorial = findViewById(R.id.textViewHistorial);

        SharedPreferences prefs = getSharedPreferences("historial", MODE_PRIVATE);
        int total = prefs.getInt("total_juegos", 0);

        SpannableStringBuilder builder = new SpannableStringBuilder();

        for (int i = 1; i <= total; i++) {
            String linea = prefs.getString("juego_" + i, "");

            SpannableString textoColor = new SpannableString(linea + "\n\n");

            if (linea.contains("Cancelado")) {
                textoColor.setSpan(new ForegroundColorSpan(Color.GRAY), 0, textoColor.length(), 0);
            } else if (linea.contains("Puntaje:")) {
                int index = linea.indexOf("Puntaje:");
                String puntajeStr = linea.substring(index + 9).trim()
                        .replace("s", "")
                        .replace(" ", "")
                        .replace("\n", "");

                int puntaje;
                try {
                    puntaje = Integer.parseInt(puntajeStr);
                } catch (NumberFormatException e) {
                    puntaje = 0;
                }

                int color = puntaje > 0 ? Color.GREEN : Color.RED;
                textoColor.setSpan(new ForegroundColorSpan(color), 0, textoColor.length(), 0);
            }

            builder.append(textoColor);
        }

        textViewHistorial.setText(builder);
    }

    // Flecha atrás del ActionBar
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}