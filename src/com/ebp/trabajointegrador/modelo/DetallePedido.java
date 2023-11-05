package com.ebp.trabajointegrador.modelo;

import java.util.ArrayList;
import java.util.List;

public class DetallePedido {
    private int id;
    private int cantidad;
    private double precio;
    private Pizza pizza;

    private int pizzaId;
    public DetallePedido() {}

    public DetallePedido(int cantidad, int pizzaId, double precio) {
        this.cantidad = cantidad;
        this.precio = precio;
        this.pizzaId = pizzaId;
    }

    public DetallePedido(int id,int cantidad, double precio, Pizza pizza) {
        this.id = id;
        this.cantidad = cantidad;
        this.precio = precio;
        this.pizza = pizza;
    }




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Pizza getPizza() {
        return pizza;
    }

    public void setPizza(Pizza pizza) {
        this.pizza = pizza;
    }
    public int getPizzaId()
    {
        return this.pizzaId;
    }

    public void setPizzaId(int pizzaId)
    {
        this.pizzaId = pizzaId;
    }
    public double calcularSubtotal() {
        return precio * cantidad;
    }
}
