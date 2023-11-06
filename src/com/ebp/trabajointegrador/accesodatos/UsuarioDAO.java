package com.ebp.trabajointegrador.accesodatos;


import com.ebp.trabajointegrador.modelo.usuario.Permiso;
import com.ebp.trabajointegrador.modelo.usuario.Rol;
import com.ebp.trabajointegrador.modelo.usuario.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    private final Connection conn;

    public UsuarioDAO(Connection conn) {
        this.conn = conn;
    }

    public List<String> obtenerUsuariosHabilitados() {
        List<String> usuariosHabilitados = new ArrayList<>();
        String query = "SELECT nombre FROM Usuario WHERE habilitado = 1";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String nombreUsuario = resultSet.getString("nombre");
                usuariosHabilitados.add(nombreUsuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuariosHabilitados;
    }

    public Usuario autenticarUsuario(String nombreUsuario, String contrasena) {
        // Realiza una consulta a la base de datos para verificar las credenciales
        // Debes adaptar esta consulta a tu esquema de base de datos
        String query = "SELECT id, nombre, rol_id FROM Usuario WHERE nombre = ? AND clave = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, nombreUsuario);
            statement.setString(2, contrasena);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // La autenticación fue exitosa
                    int idUsuarioDB = resultSet.getInt("id");
                    String nombreUsuarioDB = resultSet.getString("nombre");
                    int rolIdUsuarioDB = resultSet.getInt("rol_id");

                    return new Usuario(idUsuarioDB, nombreUsuarioDB, rolIdUsuarioDB);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Si la autenticación falla
    }

    public Rol obtenerRolPorId(int rolId) {
        String query = "SELECT id, nombre FROM Rol WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, rolId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {

                    int idRolDB = resultSet.getInt("id");
                    String nombreRolDB = resultSet.getString("nombre");

                    return new Rol(idRolDB, nombreRolDB);

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Si el rol no se encuentra
    }

    public List<Permiso> obtenerPermisos(int rolId) {
        List<Permiso> permisos = new ArrayList<>();

        String query = "SELECT p.id, p.nombre FROM Permiso p JOIN Permiso_Rol pr ON p.id = pr.permiso_id JOIN Rol r ON r.id = pr.rol_id WHERE r.id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, rolId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int permisoId = resultSet.getInt("id");
                    String nombrePermiso = resultSet.getString("nombre");
                    Permiso permiso = new Permiso(permisoId, nombrePermiso);
                    permisos.add(permiso);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return permisos;
    }
}