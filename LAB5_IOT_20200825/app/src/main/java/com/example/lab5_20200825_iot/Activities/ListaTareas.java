package com.example.lab5_20200825_iot.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab5_20200825_iot.Adapaters.TareaAdapter;
import com.example.lab5_20200825_iot.Data.TareaData;
import com.example.lab5_20200825_iot.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ListaTareas extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TareaAdapter tareaAdapter;
    private List<TareaData> tareaList;
    private String codigoPUCP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_tareas);

        Intent intent = getIntent();
        codigoPUCP = intent.getStringExtra("codigoPUCP");

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recycler_view_tareas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar lista de tareas
        tareaList = new ArrayList<>();
        cargarTareas(codigoPUCP);

        // Configurar adaptador
        tareaAdapter = new TareaAdapter(this, tareaList, codigoPUCP);
        recyclerView.setAdapter(tareaAdapter);

        // Configurar botones
        Button crearNuevaTarea = findViewById(R.id.crearNuevaTarea);
        ImageButton buttonhomesuper1 = findViewById(R.id.buttonhomesuper);

        crearNuevaTarea.setOnClickListener(v -> {
            Intent nuevaTareaIntent = new Intent(ListaTareas.this, NuevaTarea.class);
            nuevaTareaIntent.putExtra("codigoPUCP", codigoPUCP);
            startActivity(nuevaTareaIntent);
        });

        buttonhomesuper1.setOnClickListener(v -> {
            Intent intentHome = new Intent(ListaTareas.this, ListaTareas.class);
            intentHome.putExtra("codigoPUCP", codigoPUCP);
            startActivity(intentHome);
        });
    }

    private void cargarTareas(String codigoPUCP) {
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
            tareaList = gson.fromJson(sb.toString(), taskListType);

            // Notificar al adaptador que los datos han cambiado
            if (tareaAdapter != null) {
                tareaAdapter.updateData(tareaList);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
