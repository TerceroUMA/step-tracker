package com.example.pedometer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SeguimientoActivity extends AppCompatActivity {

    Button pasosButton;
    Button croButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seguimiento);


        pasosButton = findViewById(R.id.pasosButton);
        croButton = findViewById(R.id.cronometroButton);


        pasosButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(SeguimientoActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        croButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(SeguimientoActivity.this, CronometroActivity.class);
                startActivity(intent);
            }
        });
    }
}