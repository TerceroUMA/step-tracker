package com.example.pedometer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class AlarmReceiver extends BroadcastReceiver {

    DictDbHelper dbHelper;
    int pasosDiarios;

    @Override
    public void onReceive(Context context, Intent intent){

        dbHelper = new DictDbHelper(context.getApplicationContext());
        List<Pair<String, Integer>> dias = dbHelper.getDias();
        int pasosDiariosTotales = 0;
        for(Pair<String, Integer> dia : dias){
            pasosDiariosTotales += dia.second;
        }

        pasosDiarios = intent.getIntExtra("pasos", 0) - pasosDiariosTotales;
        int hora = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int mes = Calendar.getInstance().get(Calendar.MONTH) + 1;

        dbHelper.agregarDia(String.valueOf(hora), String.valueOf(mes), pasosDiarios);

    }

}
