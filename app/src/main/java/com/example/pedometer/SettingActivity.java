package com.example.pedometer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends AppCompatActivity {

    EditText cmInput;
    EditText pasosInput;
    Button ajustesButton;
    Button returnButton;
    DictDbHelper dbHelper ;
    int pasosObjetivos = - 1;
    int cm = -1;
    int pasos;
    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        returnButton = findViewById(R.id.returnButton);
        cmInput = findViewById(R.id.cmInput);
        pasosInput = findViewById(R.id.pasosInput);
        ajustesButton = findViewById(R.id.ajustesButton);
        dbHelper = new DictDbHelper(getApplicationContext());


        ajustesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if ((cmInput == null || cmInput.getText().toString().equals("") || Integer.valueOf(cmInput.getText().toString()) < 0 ) &&
                        (pasosInput == null || pasosInput.getText().toString().equals("") || Integer.valueOf(pasosInput.getText().toString()) < 0)) {
                    Toast.makeText(SettingActivity.this, "Número no válido", Toast.LENGTH_SHORT).show();
                } else {
                    // cm = Integer.valueOf(cmInput.getText().toString());
                    // NumberFormat nf = new DecimalFormat("#0.000");
                    // pasosObjetivos = Integer.valueOf(pasosInput.getText().toString());
                    // pieChart.setCenterText(pasos + " /\n" + pasosObjetivos);
                    // loadPieChartData();

                    //Insertar en la bd los ajustes
                    cm = Integer.valueOf(cmInput.getText().toString());
                    pasosObjetivos = Integer.valueOf(pasosInput.getText().toString());

                    dbHelper.agregarAjustes(cm, pasosObjetivos);
                    List<Pair<String, String>> ls = dbHelper.getAjustes();
                    Pair<String, String> par = ls.get(ls.size()-1);
                }
            }
        });

        returnButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        List<Pair<String, String>> ls = dbHelper.getAjustes();
        Pair<String, String> par = ls.get(ls.size()-1);

        cmInput.setHint(par.first);
        pasosInput.setHint(par.second);

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
}