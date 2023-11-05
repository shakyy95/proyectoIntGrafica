package com.ebp.trabajointegrador.config;

import com.ebp.trabajointegrador.modelo.usuario.Usuario;

public class SesionUsuario {
    private static SesionUsuario instancia;
    private Usuario usuarioSesion;

    private SesionUsuario() {
    }

    public static SesionUsuario getInstancia() {
        if (instancia == null) {
            instancia = new SesionUsuario();
        }
        return instancia;
    }

    public Usuario getUsuarioSesion() {
        return usuarioSesion;
    }

    public void setUsuarioSesion(Usuario usuario) {
        this.usuarioSesion = usuario;
    }
}
