package com.example.pedometer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CronometroActivity extends AppCompatActivity {

    Button pasosButton;
    Button segButton;

    Chronometer chronometer;
    Button resetButton;

    SessionManager sessionManager;
    SimpleDateFormat format;
    String currentTime;

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

        chronometer = findViewById(R.id.chronometer);
        resetButton = findViewById(R.id.resetButton);

        sessionManager = new SessionManager(getApplicationContext());
        format = new SimpleDateFormat("hh:mm:ss aa");

        currentTime = format.format(new Date()); // Obtenemos el tiempo actual
        boolean flag = sessionManager.getFlag();

        if (!flag) {

            sessionManager.setCurrentTime(currentTime);
            sessionManager.setFlag(true);
            chronometer.start();

        } else {

            String sessionManagerCurrentTime = sessionManager.getCurrentTime();
            try {
                Date date1 = format.parse(sessionManagerCurrentTime);
                Date date2 = format.parse(currentTime);

                long milis = date2.getTime() - date1.getTime();
                chronometer.setBase(SystemClock.elapsedRealtime() - milis);
                chronometer.start();

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        resetButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                chronometer.setBase(SystemClock.elapsedRealtime());
                sessionManager.setCurrentTime(currentTime);
                chronometer.start();
            }
        });
    }
}