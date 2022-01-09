package com.example.pedometer;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.example.pedometer.AlarmReceiver;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager sensorManager;
    TextView kmText;
    boolean running = false;
    int pasos;
    int cmZancada;
    int  pasosObjetivos;
    TextView numPasos;
    PieChart pieChart;
    DictDbHelper dbHelper;

    Button segButton;
    Button cronButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        dbHelper = new DictDbHelper(getApplicationContext());

        kmText = findViewById(R.id.kmText);
        numPasos =  findViewById(R.id.numPasos);
        pieChart = findViewById(R.id.pieChart);

        List<Pair<String, String>> ls = dbHelper.getAjustes();
        Pair<String, String> par = ls.get(ls.size()-1);

        cmZancada = Integer.valueOf(par.first);
        pasosObjetivos = Integer.valueOf(par.second);

        setupPieChart();
        loadPieChartData();
        setAlarm(getApplicationContext());


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){ // Esto es para pedir permisos de actividad física
            //ask for permission
            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
        }

        segButton = findViewById(R.id.seguimientoButton);
        cronButton = findViewById(R.id.cronometroButton);

        segButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SeguimientoActivity.class);
                startActivity(intent);            }
        });

        cronButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CronometroActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
            /*return true;*/
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        running = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "No se encuentra el sensor", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        running = false;

        //Si haces un unRegisterListener dejas de contar los pasos al salir de la app
        //sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (running) {
            pasos = (int) event.values[0];
            numPasos.setText(String.valueOf(pasos));
            if ( cmZancada != -1) {
                kmText.setText("Distancia: " + ((pasos / cmZancada) / 10000.0) + " km");
            }
            if (pasosObjetivos != -1) {
                NumberFormat nf = new DecimalFormat("#0.000");
                kmText.setText("Distancia: " + nf.format((pasos * cmZancada) / 100000.0) + " km");
                loadPieChartData();
            }
            loadPieChartData();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void loadPieChartData() {

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(pasos, ""));
        entries.add(new PieEntry(pasosObjetivos - pasos, ""));

        ArrayList<Integer> colores = new ArrayList<>();
        colores.add(Color.parseColor("#00C49A"));
        colores.add(Color.parseColor("#F75B50"));

        PieDataSet dataset = new PieDataSet(entries, "Step counter");
        dataset.setColors(colores);

        PieData data = new PieData(dataset);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(12f);
        /*data.setValueTextColor(Color.BLACK);*/
        data.setValueTextColor(Color.TRANSPARENT);

        pieChart.setData(data);
        pieChart.setCenterText(pasos + " /\n" + pasosObjetivos);
        pieChart.invalidate();
        pieChart.animateY(1400, Easing.EaseInOutQuad);
    }

    private void setupPieChart() {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setCenterTextSize(14);
        pieChart.getDescription().setEnabled(false);

        Legend l = pieChart.getLegend();
        LegendEntry l1 = new LegendEntry("Pasos dados", Legend.LegendForm.CIRCLE, 10f, 2f, null, Color.parseColor("#00C49A"));
        LegendEntry l2 = new LegendEntry("Pasos restantes", Legend.LegendForm.CIRCLE, 10f, 2f, null, Color.parseColor("#F75B50"));
        LegendEntry[] array = new LegendEntry[2];
        array[0] = l1;
        array[1] = l2;
        l.setCustom(array);
        l.setEnabled(true);
    }

    private void setAlarm(Context context){

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, AlarmReceiver.class);
        i.putExtra("pasos", pasos);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);

    }

    /*
    public static void showToast(){
        Toast.makeText(MainActivity, "Alarma", Toast.LENGTH_SHORT).show();
    }
     */

}
