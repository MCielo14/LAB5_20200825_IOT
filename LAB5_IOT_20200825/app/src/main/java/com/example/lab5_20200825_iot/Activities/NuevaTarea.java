package com.example.lab5_20200825_iot.Activities;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.lab5_20200825_iot.Data.TareaData;
import com.example.lab5_20200825_iot.R;
import com.example.lab5_20200825_iot.Notificaciones.ReminderReciever;
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
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class NuevaTarea extends AppCompatActivity {
    private EditText tituloTarea, descripcionTarea;
    private Button fechaVencimientoButton, horaVencimientoButton, fechaRecordatorioButton, horaRecordatorioButton, guardarTarea;
    private String codigoPUCP;
    private long fechaVencimiento, horaVencimiento, fechaRecordatorio, horaRecordatorio;
    private Calendar calendarVencimiento, calendarRecordatorio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_tarea);

        tituloTarea = findViewById(R.id.titulonuevo);
        descripcionTarea = findViewById(R.id.descricionnuevo);
        fechaVencimientoButton = findViewById(R.id.buttonvencimientonuevo);
        horaVencimientoButton = findViewById(R.id.buttonhoravencimientonuevo);
        fechaRecordatorioButton = findViewById(R.id.button);
        horaRecordatorioButton = findViewById(R.id.button2);
        guardarTarea = findViewById(R.id.Guardarbutton);
        ImageButton buttonhomesuper1 = findViewById(R.id.buttonhomesuper);

        Intent intent = getIntent();
        codigoPUCP = intent.getStringExtra("codigoPUCP");


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
        }

        calendarVencimiento = Calendar.getInstance();
        calendarRecordatorio = Calendar.getInstance();

        fechaVencimientoButton.setOnClickListener(v -> showDatePickerDialog(calendarVencimiento, true));
        horaVencimientoButton.setOnClickListener(v -> showTimePickerDialog(calendarVencimiento, true));
        fechaRecordatorioButton.setOnClickListener(v -> showDatePickerDialog(calendarRecordatorio, false));
        horaRecordatorioButton.setOnClickListener(v -> showTimePickerDialog(calendarRecordatorio, false));

        guardarTarea.setOnClickListener(v -> {
            String titulo = tituloTarea.getText().toString();
            String descripcion = descripcionTarea.getText().toString();

            if (!titulo.isEmpty() && !descripcion.isEmpty() && fechaVencimiento > 0 && horaVencimiento > 0 && fechaRecordatorio > 0 && horaRecordatorio > 0) {
                TareaData nuevaTarea = new TareaData(titulo, descripcion, fechaVencimiento, horaVencimiento, fechaRecordatorio, horaRecordatorio);
                guardarTarea(codigoPUCP, nuevaTarea);
                programarRecordatorio(nuevaTarea);

                Intent listaTareasIntent = new Intent(NuevaTarea.this, ListaTareas.class);
                listaTareasIntent.putExtra("codigoPUCP", codigoPUCP);
                startActivity(listaTareasIntent);
                finish();
            } else {
                Toast.makeText(NuevaTarea.this, "Ingrese todos los datos válidos", Toast.LENGTH_SHORT).show();
            }
        });

        buttonhomesuper1.setOnClickListener(v -> {
            Intent intent1 = new Intent(NuevaTarea.this, ListaTareas.class);
            intent1.putExtra("codigoPUCP", codigoPUCP);
            startActivity(intent1);
        });
    }

    private void showDatePickerDialog(Calendar calendar, boolean isVencimiento) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    if (isVencimiento) {
                        fechaVencimiento = calendar.getTimeInMillis();
                        fechaVencimientoButton.setText(dateFormat.format(calendar.getTime()));
                    } else {
                        fechaRecordatorio = calendar.getTimeInMillis();
                        fechaRecordatorioButton.setText(dateFormat.format(calendar.getTime()));
                    }
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void showTimePickerDialog(Calendar calendar, boolean isVencimiento) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                    if (isVencimiento) {
                        horaVencimiento = calendar.getTimeInMillis();
                        horaVencimientoButton.setText(timeFormat.format(calendar.getTime()));
                    } else {
                        horaRecordatorio = calendar.getTimeInMillis();
                        horaRecordatorioButton.setText(timeFormat.format(calendar.getTime()));
                    }
                },
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    private void guardarTarea(String codigoPUCP, TareaData nuevaTarea) {
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
        } catch (IOException e) {
            tareas = new ArrayList<>();
        }

        tareas.add(nuevaTarea);

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

    private void programarRecordatorio(TareaData nuevaTarea) {
        Intent intent = new Intent(NuevaTarea.this, ReminderReciever.class);
        intent.putExtra("title", nuevaTarea.getTituloTarea());
        intent.putExtra("message", "Recordatorio de la tarea: " + nuevaTarea.getTituloTarea());


        long tiempoRestante = (nuevaTarea.getFechaVencimiento() + nuevaTarea.getHoraVencimiento()) - (nuevaTarea.getFechaRecordatorio() + nuevaTarea.getHoraRecordatorio());
        int priority;
        if (tiempoRestante <= 3 * 60 * 60 * 1000) {
            priority = NotificationManagerCompat.IMPORTANCE_HIGH;
        } else if (tiempoRestante <= 6 * 60 * 60 * 1000) {
            priority = NotificationManagerCompat.IMPORTANCE_DEFAULT;
        } else {
            priority = NotificationManagerCompat.IMPORTANCE_LOW;
        }
        intent.putExtra("priority", priority);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(NuevaTarea.this, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, nuevaTarea.getFechaRecordatorio() + nuevaTarea.getHoraRecordatorio(), pendingIntent);
        }
    }

}
