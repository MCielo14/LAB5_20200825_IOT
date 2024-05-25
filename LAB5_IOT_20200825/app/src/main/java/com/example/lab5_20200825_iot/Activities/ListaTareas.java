package com.example.lab5_20200825_iot.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab5_20200825_iot.Adapaters.TareaAdapter;
import com.example.lab5_20200825_iot.Data.TareaData;
import com.example.lab5_20200825_iot.Notificaciones.NotificationHelper;
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

    private ActivityResultLauncher<Intent> tareaDetailsLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    cargarTareas(codigoPUCP);
                    tareaAdapter.updateData(tareaList);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_tareas);

        Intent intent = getIntent();
        codigoPUCP = intent.getStringExtra("codigoPUCP");


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
        }


        recyclerView = findViewById(R.id.recycler_view_tareas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        tareaList = new ArrayList<>();
        cargarTareas(codigoPUCP);


        tareaAdapter = new TareaAdapter(this, tareaList, codigoPUCP, tareaDetailsLauncher);
        recyclerView.setAdapter(tareaAdapter);


        Button crearNuevaTarea = findViewById(R.id.crearNuevaTarea);
        ImageButton buttonhomesuper1 = findViewById(R.id.buttonhomesuper);

        crearNuevaTarea.setOnClickListener(v -> {
            Intent nuevaTareaIntent = new Intent(ListaTareas.this, NuevaTarea.class);
            nuevaTareaIntent.putExtra("codigoPUCP", codigoPUCP);
            startActivity(nuevaTareaIntent);
        });

        buttonhomesuper1.setOnClickListener(v -> {
            Intent intent1 = new Intent(ListaTareas.this, Inicio.class);
            startActivity(intent1);
        });


        NotificationHelper.createNotificationChannels(this);
        sendPersistentNotifications();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarTareas(codigoPUCP);
        tareaAdapter.updateData(tareaList);
        sendPersistentNotifications();
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
            Type taskListType = new TypeToken<ArrayList<TareaData>>() {}.getType();
            tareaList = gson.fromJson(sb.toString(), taskListType);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendPersistentNotifications() {
        int tareasEnCurso = tareaList.size();
        String usuario = "Usuario logueado: " + codigoPUCP;
        String title = "Tareas en curso: " + tareasEnCurso;

        NotificationHelper.sendPersistentNotification(this, title, usuario);
    }
}
