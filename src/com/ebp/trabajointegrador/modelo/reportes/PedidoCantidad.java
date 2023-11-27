package com.ebp.trabajointegrador.modelo.reportes;

public class PedidoCantidad {
    private String variedadPizza;
    private String tipoPizza;
    private int cantidadPedidos;

    // Constructor
    public PedidoCantidad(String variedadPizza, String tipoPizza, int cantidadPedidos) {
        this.variedadPizza = variedadPizza;
        this.tipoPizza = tipoPizza;
        this.cantidadPedidos = cantidadPedidos;
    }

    // Getters y Setters
    public String getVariedadPizza() {
        return variedadPizza;
    }

    public void setVariedadPizza(String variedadPizza) {
        this.variedadPizza = variedadPizza;
    }

    public String getTipoPizza() {
        return tipoPizza;
    }

    public void setTipoPizza(String tipoPizza) {
        this.tipoPizza = tipoPizza;
    }

    public int getCantidadPedidos() {
        return cantidadPedidos;
    }

    public void setCantidadPedidos(int cantidadPedidos) {
        this.cantidadPedidos = cantidadPedidos;
    }
}

