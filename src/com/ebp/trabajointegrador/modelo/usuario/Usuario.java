package com.ebp.trabajointegrador.modelo.usuario;

public class Usuario {
    private int id;
    private String nombre;
    private Rol rol;
    private int rolId;

    public Usuario(int id, String nombre, int rolId) {
        this.id = id;
        this.nombre = nombre;
        this.rolId = rolId;
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

    public int getRolId() {
        return this.rolId;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
}