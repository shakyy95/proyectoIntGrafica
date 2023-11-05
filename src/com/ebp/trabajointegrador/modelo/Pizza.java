package com.ebp.trabajointegrador.modelo;

public class Pizza {

    private int id;
    private String nombre;
    private double precio;
    private TipoPizza tipoPizza;
    private VariedadPizza variedadPizza;
    private TamanioPizza tamanioPizza;
    private boolean habilitado;

    public  Pizza(){}

    public Pizza(int id, String nombre, TipoPizza tipoPizza, VariedadPizza variedadPizza, TamanioPizza tamanioPizza) {
        this.id = id;
        this.nombre = nombre;
        this.tipoPizza = tipoPizza;
        this.variedadPizza = variedadPizza;
        this.tamanioPizza = tamanioPizza;
    }
    public Pizza(String nombre, double precio, TipoPizza tipoPizza, VariedadPizza variedadPizza, TamanioPizza tamanioPizza, boolean habilitado) {
        this.nombre = nombre;
        this.precio = precio;
        this.tipoPizza = tipoPizza;
        this.variedadPizza = variedadPizza;
        this.tamanioPizza = tamanioPizza;
        this.habilitado = habilitado;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public TipoPizza getTipoPizza() {
        return tipoPizza;
    }

    public void setTipoPizza(TipoPizza tipoPizza) {
        this.tipoPizza = tipoPizza;
    }

    public VariedadPizza getVariedadPizza() {
        return variedadPizza;
    }

    public void setVariedadPizza(VariedadPizza variedadPizza) {
        this.variedadPizza = variedadPizza;
    }

    public TamanioPizza getTamanioPizza() {
        return tamanioPizza;
    }

    public void setTamanioPizza(TamanioPizza tamanioPizza) {
        this.tamanioPizza = tamanioPizza;
    }

    public boolean isHabilitado() {
        return habilitado;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }

}
