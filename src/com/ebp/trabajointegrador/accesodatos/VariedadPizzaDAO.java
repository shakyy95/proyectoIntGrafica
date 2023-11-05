package com.ebp.trabajointegrador.accesodatos;

import com.ebp.trabajointegrador.modelo.VariedadPizza;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VariedadPizzaDAO {
    private final Connection conn;

    public VariedadPizzaDAO(Connection conn) {
        this.conn = conn;
    }

    public List<VariedadPizza> obtenerVariedadesPizzas() {
        List<VariedadPizza> variedadesPizzas = new ArrayList<>();

        String sql = "SELECT id, nombre, ingredientes, habilitado FROM VariedadPizza";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    VariedadPizza variedadPizza = new VariedadPizza();
                    variedadPizza.setId(resultSet.getInt("id"));
                    variedadPizza.setNombre(resultSet.getString("nombre"));
                    variedadPizza.setIngredientes(resultSet.getString("ingredientes"));
                    variedadPizza.setHabilitado(resultSet.getBoolean("habilitado"));

                    variedadesPizzas.add(variedadPizza);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return variedadesPizzas;
    }
}
