package com.example.lab5_20200825_iot.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab5_20200825_iot.Data.TareaData;
import com.example.lab5_20200825_iot.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DetallesTarea extends AppCompatActivity {
    private TextView tituloDetalle, descripcionDetalle, fechaVencimientoDetalle, horaVencimientoDetalle, fechaRecordatorioDetalle, horaRecordatorioDetalle;
    private CheckBox tareaCompletaCheckBox;
    private Button editarButton, borrarButton;
    private String codigoPUCP;
    private TareaData tareaData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_tarea);

        tituloDetalle = findViewById(R.id.titulodetalle);
        descripcionDetalle = findViewById(R.id.descriciondetalle);
        fechaVencimientoDetalle = findViewById(R.id.fechaVencimientoDetalle);
        horaVencimientoDetalle = findViewById(R.id.HoraVencimientoDetalle);
        fechaRecordatorioDetalle = findViewById(R.id.FecharecordatorioDetalle);
        horaRecordatorioDetalle = findViewById(R.id.HoraRecordatorioDetalle);
        tareaCompletaCheckBox = findViewById(R.id.checkBox);
        editarButton = findViewById(R.id.Editarbutton);
        borrarButton = findViewById(R.id.BorrarButton);

        ImageButton buttonhomesuper1 = findViewById(R.id.buttonhomesuper);
        buttonhomesuper1.setOnClickListener(v -> {
            Intent intent = new Intent(DetallesTarea.this, ListaTareas.class);
            intent.putExtra("codigoPUCP", codigoPUCP); // Pasar el código PUCP
            startActivity(intent);
        });

        editarButton.setOnClickListener(v -> {
            Intent intent = new Intent(DetallesTarea.this, EditarTarea.class);
            intent.putExtra("codigoPUCP", codigoPUCP); // Pasar el código PUCP
            intent.putExtra("tareaData", tareaData); // Pasar la tarea
            startActivity(intent);
        });

        Intent intent = getIntent();
        tareaData = (TareaData) intent.getSerializableExtra("tareaData");
        codigoPUCP = intent.getStringExtra("codigoPUCP"); // Obtener el código PUCP
        if (tareaData != null) {
            displayTaskDetails(tareaData);
        }

        tareaCompletaCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            tareaData.setCompleta(isChecked);
            guardarTarea(codigoPUCP, tareaData);
        });
    }

    private void displayTaskDetails(TareaData tareaData) {
        tituloDetalle.setText(tareaData.getTituloTarea());
        descripcionDetalle.setText(tareaData.getDescripcion());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        fechaVencimientoDetalle.setText(dateFormat.format(tareaData.getFechaVencimiento()));
        horaVencimientoDetalle.setText(timeFormat.format(tareaData.getHoraVencimiento()));
        fechaRecordatorioDetalle.setText(dateFormat.format(tareaData.getFechaRecordatorio()));
        horaRecordatorioDetalle.setText(timeFormat.format(tareaData.getHoraRecordatorio()));
        tareaCompletaCheckBox.setChecked(tareaData.isCompleta());
    }

    private void guardarTarea(String codigoPUCP, TareaData tareaData) {
        List<TareaData> tareas = new ArrayList<>();
        try {
            FileInputStream fis = openFileInput("tasks_" + codigoPUCP + ".json");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            isr.close();
            fis.close();

            Gson gson = new Gson();
            Type taskListType = new TypeToken<ArrayList<TareaData>>(){}.getType();
            tareas = gson.fromJson(sb.toString(), taskListType);
        } catch (Exception e) {
            // Si no se encuentran tareas, inicializar una lista vacía
            tareas = new ArrayList<>();
        }

        // Buscar y actualizar la tarea existente
        for (int i = 0; i < tareas.size(); i++) {
            if (tareas.get(i).equals(tareaData)) {
                tareas.set(i, tareaData);
                break;
            }
        }

        try {
            FileOutputStream fos = openFileOutput("tasks_" + codigoPUCP + ".json", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = gson.toJson(tareas);
            fos.write(json.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
