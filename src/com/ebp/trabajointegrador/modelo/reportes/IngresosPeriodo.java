package com.ebp.trabajointegrador.modelo.reportes;

public class IngresosPeriodo {
    private String periodo;
    private double ingresos;

    public IngresosPeriodo(String periodo, double ingresos) {
        this.periodo = periodo;
        this.ingresos = ingresos;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public double getIngresos() {
        return ingresos;
    }

    public void setIngresos(double ingresos) {
        this.ingresos = ingresos;
    }

}
