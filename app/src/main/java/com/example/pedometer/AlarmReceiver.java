package com.example.pedometer;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class AlarmReceiver extends BroadcastReceiver {

    DictDbHelper dbHelper;
    int pasosDiarios;

    @Override
    public void onReceive(Context context, Intent intent){

        // Toast.makeText(MainActivity.class, "Número no válido", Toast.LENGTH_SHORT).show();

        dbHelper = new DictDbHelper(context);
        List<Pair<String, Integer>> dias = dbHelper.getDias();
        int pasosDiariosTotales = 0;
        for(Pair<String, Integer> dia : dias){
            pasosDiariosTotales += dia.second;
        }

        Log.d("Alarma", "pasosDiariosTotales: " + pasosDiariosTotales + "\n pasos sensor: " + intent.getIntExtra("pasos", -1));

        pasosDiarios = intent.getIntExtra("pasos", 0) - pasosDiariosTotales;

        int dia = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) - 1;
        int mes = (Calendar.getInstance().get(Calendar.MONTH) + 1);

        dbHelper.agregarDia(String.valueOf(dia), String.valueOf(mes), pasosDiarios);

        MainActivity.registroHecho = false;
    }

}
