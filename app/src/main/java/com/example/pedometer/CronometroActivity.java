package com.example.pedometer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class CronometroActivity extends AppCompatActivity {

    Button pasosButton;
    Button segButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cronometro);

        pasosButton = findViewById(R.id.pasosButton);
        segButton = findViewById(R.id.seguimientoButton);


        pasosButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(CronometroActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        segButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(CronometroActivity.this, SeguimientoActivity.class);
                startActivity(intent);
            }
        });
    }
}