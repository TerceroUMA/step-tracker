package com.example.pedometer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class SeguimientoActivity extends AppCompatActivity {

    BarChart grafica;
    ArrayList<BarEntry> barEntryArrayList;
    ArrayList<String> diasLabel;
    List<Pair<String, Integer>> dias;
    DictDbHelper dbHelper;
    Button pasosButton;
    Button croButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new DictDbHelper(getApplicationContext());

        // Se comprueba si hay registros o no y se asigna una view
        if(dbHelper.getDias().isEmpty()){
            setContentView(R.layout.activity_seguimiento_vacia);
        } else{
            setContentView(R.layout.activity_seguimiento);
            setupGrafica();
        }

        // Se añaden eventListeners a los botones para cambiar de página
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

    private void setupGrafica(){

        // Crea una gráfica de barras customizada cargando los datos del registro personal existentes en la BD.

        grafica = findViewById(R.id.barChart);
        barEntryArrayList = new ArrayList<>();
        diasLabel = new ArrayList<>();

        dias = dbHelper.getDias();

        for(int i=0; i<dias.size(); i++){
            String fecha = dias.get(i).first;
            Integer pasos = dias.get(i).second;

            barEntryArrayList.add(new BarEntry(i, pasos));
            diasLabel.add(String.valueOf(fecha));
        }

        BarDataSet barDataSet = new BarDataSet(barEntryArrayList, getResources().getString(R.string.pasos));
        barDataSet.setColor(Color.BLUE);
        Description desc = new Description();
        desc.setText(getResources().getString(R.string.dias));
        grafica.setDescription(desc);
        BarData barData = new BarData(barDataSet);
        grafica.setData(barData);

        XAxis ejeX = grafica.getXAxis();
        ejeX.setValueFormatter(new IndexAxisValueFormatter((diasLabel)));
        ejeX.setPosition(XAxis.XAxisPosition.BOTTOM);
        ejeX.setDrawGridLines(false);
        ejeX.setDrawAxisLine(false);
        ejeX.setGranularity(1f);
        ejeX.setLabelCount(diasLabel.size());
        ejeX.setLabelRotationAngle(0);
        grafica.animateY(2000);
        grafica.invalidate();

    }

}