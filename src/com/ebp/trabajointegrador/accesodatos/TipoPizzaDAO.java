package com.ebp.trabajointegrador.accesodatos;

import com.ebp.trabajointegrador.modelo.TipoPizza;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TipoPizzaDAO {

    private final Connection conn;

    public TipoPizzaDAO(Connection conn) {
        this.conn = conn;
    }

    // Método para obtener todos los tipos de pizzas
    public List<TipoPizza> obtenerTiposPizzas() {
        List<TipoPizza> tiposPizzas = new ArrayList<>();
        String sql = "SELECT * FROM TipoPizza";
        try (PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                TipoPizza tipoPizza = new TipoPizza();
                tipoPizza.setId(resultSet.getInt("id"));
                tipoPizza.setNombre(resultSet.getString("nombre"));
                tipoPizza.setDescripcion(resultSet.getString("descripcion"));
                tipoPizza.setHabilitado(resultSet.getBoolean("habilitado"));
                tiposPizzas.add(tipoPizza);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tiposPizzas;
    }
}
