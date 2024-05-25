package com.example.lab5_20200825_iot.Data;

import java.io.Serializable;

public class TareaData implements Serializable {
    private String tituloTarea;
    private String descripcion;
    private long fechaVencimiento;
    private long horaVencimiento;
    private long fechaRecordatorio;
    private long horaRecordatorio;

    public TareaData(String tituloTarea, String descripcion, long fechaVencimiento, long horaVencimiento, long fechaRecordatorio, long horaRecordatorio) {
        this.tituloTarea = tituloTarea;
        this.descripcion = descripcion;
        this.fechaVencimiento = fechaVencimiento;
        this.horaVencimiento = horaVencimiento;
        this.fechaRecordatorio = fechaRecordatorio;
        this.horaRecordatorio = horaRecordatorio;
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
}
