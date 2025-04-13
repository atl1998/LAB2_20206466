package com.example.lab2_20206466;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button buttonRedes=findViewById(R.id.buttonRedes);
        Button buttonCiber=findViewById(R.id.buttonCiber);
        Button buttonMicro=findViewById(R.id.buttonMicro);

        buttonRedes.setOnClickListener(view ->generarQuiz("Redes"));
        buttonCiber.setOnClickListener(view ->generarQuiz("Ciberseguridad"));
        buttonMicro.setOnClickListener(view ->generarQuiz("Microondas"));

    }
    private void generarQuiz(String tema){
        Intent intent=new Intent(this, QuizActivity.class);
        intent.putExtra("tema",tema);
        startActivity(intent);
    }

}