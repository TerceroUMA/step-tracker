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

    // Esta clase representa el recibidor de una alarma, que ejecuta el código en onReceive() cuando la alarma puesta en
    // setAlarm() de MainActivity hace trigger.

    DictDbHelper dbHelper;
    int pasosDiarios;

    @Override
    public void onReceive(Context context, Intent intent){

        dbHelper = new DictDbHelper(context);
        List<Pair<String, Integer>> dias = dbHelper.getDias();
        int pasosDiariosTotales = 0;
        for(Pair<String, Integer> dia : dias){
            pasosDiariosTotales += dia.second;
        }
        // Se calculan los pasos que se han dado en el día pasado por parámetros.
        pasosDiarios = MainActivity.pasos - pasosDiariosTotales;
        String dia = intent.getStringExtra("dia");
        String mes = intent.getStringExtra("mes");

        // Se añade el registro en la BD.
        dbHelper.agregarDia(dia, mes, pasosDiarios);

        // Indica que podemos setear otra alarma para el día siguiente.
        MainActivity.registroHecho = false;
    }

}
