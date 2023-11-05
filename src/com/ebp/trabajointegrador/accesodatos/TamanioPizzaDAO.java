package com.ebp.trabajointegrador.accesodatos;

import com.ebp.trabajointegrador.modelo.TamanioPizza;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TamanioPizzaDAO {
    private final Connection conn;
    public TamanioPizzaDAO(Connection conn) {
        this.conn = conn;
    }

    public List<TamanioPizza> obtenerTamaniosPizzas() {
        List<TamanioPizza> tamaniosPizzas = new ArrayList<>();

        String sql = "SELECT id, nombre, cantPorciones, habilitado FROM TamanioPizza";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    TamanioPizza tamanioPizza = new TamanioPizza();
                    tamanioPizza.setId(resultSet.getInt("id"));
                    tamanioPizza.setNombre(resultSet.getString("nombre"));
                    tamanioPizza.setCantPorciones(resultSet.getInt("cantPorciones"));
                    tamanioPizza.setHabilitado(resultSet.getBoolean("habilitado"));

                    tamaniosPizzas.add(tamanioPizza);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tamaniosPizzas;
    }
}
