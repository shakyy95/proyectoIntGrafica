package com.ebp.trabajointegrador.modelo.reportes;

public class ResumenPeriodo {
    private String periodo;
    private int cantidadPedidos;
    private double montoPedidos;

    // Constructor
    public ResumenPeriodo(String periodo, int cantidad, double monto) {
        this.periodo = periodo;
        this.montoPedidos = monto;
        this.cantidadPedidos = cantidad;
    }

    // Getters y Setters
    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public double getMontoPedidos() {
        return montoPedidos;
    }

    public void setMontoPedidos(double montoPedidos) {
        this.montoPedidos = montoPedidos;
    }

    public int getCantidadPedidos() {
        return cantidadPedidos;
    }

    public void setCantidadPedidos(int cantidadPedidos) {
        this.cantidadPedidos = cantidadPedidos;
    }
}
