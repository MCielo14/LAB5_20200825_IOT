package com.example.lab5_20200825_iot.Adapaters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab5_20200825_iot.Activities.DetallesTarea;
import com.example.lab5_20200825_iot.Data.TareaData;
import com.example.lab5_20200825_iot.R;

import java.util.List;

public class TareaAdapter extends RecyclerView.Adapter<TareaAdapter.ViewHolder> {
    private List<TareaData> tareaList;
    private Context context;
    private String codigoPUCP;
    private final ActivityResultLauncher<Intent> tareaDetailsLauncher;

    public TareaAdapter(Context context, List<TareaData> tareaList, String codigoPUCP, ActivityResultLauncher<Intent> tareaDetailsLauncher) {
        this.context = context;
        this.tareaList = tareaList;
        this.codigoPUCP = codigoPUCP;
        this.tareaDetailsLauncher = tareaDetailsLauncher;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tarea_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TareaData tarea = tareaList.get(position);
        holder.tituloTarea.setText(tarea.getTituloTarea());

        holder.detallesButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetallesTarea.class);
            intent.putExtra("tareaData", tarea);
            intent.putExtra("codigoPUCP", codigoPUCP);
            tareaDetailsLauncher.launch(intent);
        });
    }

    @Override
    public int getItemCount() {
        return tareaList.size();
    }

    public void updateData(List<TareaData> newTareaList) {
        this.tareaList = newTareaList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tituloTarea;
        Button detallesButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tituloTarea = itemView.findViewById(R.id.Titulo);
            detallesButton = itemView.findViewById(R.id.detallesButton);
        }
    }
}
