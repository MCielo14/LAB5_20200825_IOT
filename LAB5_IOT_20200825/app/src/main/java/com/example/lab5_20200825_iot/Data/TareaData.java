package com.example.lab5_20200825_iot.Data;

import java.io.Serializable;
import java.util.UUID;

public class TareaData implements Serializable {
    private String id;
    private String tituloTarea;
    private String descripcion;
    private long fechaVencimiento;
    private long horaVencimiento;
    private long fechaRecordatorio;
    private long horaRecordatorio;
    private boolean completa;

    public TareaData(String tituloTarea, String descripcion, long fechaVencimiento, long horaVencimiento, long fechaRecordatorio, long horaRecordatorio) {
       // Para poner el id con UUID.randomUUID().toString(); se hizo uso de ChatGPT
        this.id = UUID.randomUUID().toString();
        this.tituloTarea = tituloTarea;
        this.descripcion = descripcion;
        this.fechaVencimiento = fechaVencimiento;
        this.horaVencimiento = horaVencimiento;
        this.fechaRecordatorio = fechaRecordatorio;
        this.horaRecordatorio = horaRecordatorio;
        this.completa = false;
    }

    public String getId() {
        return id;
    }

    public String getTituloTarea() {
        return tituloTarea;
    }

    public void setTituloTarea(String tituloTarea) {
        this.tituloTarea = tituloTarea;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public long getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(long fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public long getHoraVencimiento() {
        return horaVencimiento;
    }

    public void setHoraVencimiento(long horaVencimiento) {
        this.horaVencimiento = horaVencimiento;
    }

    public long getFechaRecordatorio() {
        return fechaRecordatorio;
    }

    public void setFechaRecordatorio(long fechaRecordatorio) {
        this.fechaRecordatorio = fechaRecordatorio;
    }

    public long getHoraRecordatorio() {
        return horaRecordatorio;
    }

    public void setHoraRecordatorio(long horaRecordatorio) {
        this.horaRecordatorio = horaRecordatorio;
    }

    public boolean isCompleta() {
        return completa;
    }

    public void setCompleta(boolean completa) {
        this.completa = completa;
    }
    // Para poner el checkbox se hizo uso un listener en tiempo real se hizo con ChatGPT

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TareaData tareaData = (TareaData) obj;
        return tituloTarea.equals(tareaData.tituloTarea) &&
                descripcion.equals(tareaData.descripcion) &&
                fechaVencimiento == tareaData.fechaVencimiento &&
                horaVencimiento == tareaData.horaVencimiento &&
                fechaRecordatorio == tareaData.fechaRecordatorio &&
                horaRecordatorio == tareaData.horaRecordatorio;
    }
}
