package com.ebp.trabajointegrador.modelo.usuario;

import java.util.List;

public class SistemaControlAcceso {
    private List<Usuario> usuarios;
    private List<Rol> roles;

    public SistemaControlAcceso(List<Usuario> usuarios, List<Rol> roles) {
        this.usuarios = usuarios;
        this.roles = roles;
    }

    public boolean tienePermiso(int usuarioId, String permiso) {
        for (Usuario usuario : usuarios) {
            if (usuario.getId() == usuarioId) {
                Rol rolUsuario = usuario.getRol();
                for (Permiso permisoRol : rolUsuario.getPermisos()) {
                    if (permisoRol.getNombre().equals(permiso)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public List<Rol> getRoles() {
        return roles;
    }

    public void setRoles(List<Rol> roles) {
        this.roles = roles;
    }
}