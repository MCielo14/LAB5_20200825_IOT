package com.example.lab5_20200825_iot.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.ArrayList;
import java.util.List;

public class Inicio extends AppCompatActivity {
    private EditText codigoPUCP;
    private List<TareaData> tareas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        codigoPUCP = findViewById(R.id.codigo);
        Button ingreso = findViewById(R.id.ingreso);

        ingreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codigo = codigoPUCP.getText().toString().trim();
                if (codigo.length() == 8 && codigo.matches("\\d+")) {
                    // Cargar las tareas y recordatorios aquí
                    cargarTareas(codigo);

                    // Iniciar la actividad de ListaTareas
                    Intent intent = new Intent(Inicio.this, ListaTareas.class);
                    intent.putExtra("codigoPUCP", codigo);
                    startActivity(intent);
                } else {
                    Toast.makeText(Inicio.this, "Ingrese un código PUCP válido de 8 dígitos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void cargarTareas(String codigoPUCP) {
        tareas = new ArrayList<>();
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

        } catch (IOException e) {
            // Si no se encuentran tareas, inicializar una lista vacía
            tareas = new ArrayList<>();
        }
    }

    // Método para guardar tareas (se llamará desde otra actividad)
    public void guardarTareas(String codigoPUCP, List<TareaData> tareas) {
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
