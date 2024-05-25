package com.example.lab5_20200825_iot.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab5_20200825_iot.Data.TareaData;
import com.example.lab5_20200825_iot.R;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DetallesTarea extends AppCompatActivity {
    private TextView tituloDetalle, descripcionDetalle, fechaVencimientoDetalle, horaVencimientoDetalle, fechaRecordatorioDetalle, horaRecordatorioDetalle;
    private CheckBox tareaCompletaCheckBox;
    private Button editarButton, borrarButton;
    private String codigoPUCP;

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
            intent.putExtra("tareaData", getIntent().getSerializableExtra("tareaData")); // Pasar la tarea
            startActivity(intent);
        });

        Intent intent = getIntent();
        TareaData tareaData = (TareaData) intent.getSerializableExtra("tareaData");
        codigoPUCP = intent.getStringExtra("codigoPUCP"); // Obtener el código PUCP
        if (tareaData != null) {
            displayTaskDetails(tareaData);
        }
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
        tareaCompletaCheckBox.setChecked(false); // Cambiar según el estado de la tarea
    }
}
