package com.ebp.trabajointegrador.modelo;

public class TamanioPizza {
    private  int id;
    private String nombre;
    private int cantPorciones;
    private boolean habilitado;

    public TamanioPizza(){}
    public TamanioPizza(String nombre, int cantPorciones) {
        this.nombre = nombre;
        this.cantPorciones = cantPorciones;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantPorciones() {
        return cantPorciones;
    }

    public void setCantPorciones(int cantPorciones) {
        this.cantPorciones = cantPorciones;
    }

    public boolean isHabilitado() {
        return habilitado;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }

}