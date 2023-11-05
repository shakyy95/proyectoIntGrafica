package com.ebp.trabajointegrador.accesodatos;

import com.ebp.trabajointegrador.modelo.Pizza;
import com.ebp.trabajointegrador.modelo.TamanioPizza;
import com.ebp.trabajointegrador.modelo.TipoPizza;
import com.ebp.trabajointegrador.modelo.VariedadPizza;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PizzaDAO {

    private final Connection conn;

    public PizzaDAO(Connection conn) {
        this.conn = conn;
    }

    public Pizza obtenerPizzaPorId(int pizzaId) {
        String sql = "SELECT " +
                "p.id AS pizza_id, " +
                "p.nombre AS pizza_nombre, " +
                "p.precio AS pizza_precio, " +
                "p.habilitado AS pizza_habilitado, " +
                "v.id AS variedad_id, " +
                "v.nombre AS variedad_nombre, " +
                "v.ingredientes AS variedad_ingredientes, " +
                "v.habilitado AS variedad_habilitado, " +
                "t.id AS tipo_id, " +
                "t.nombre AS tipo_nombre, " +
                "t.habilitado AS tipo_habilitado, " +
                "tam.id AS tamanio_id, " +
                "tam.nombre AS tamanio_nombre, " +
                "tam.cantPorciones AS tamanio_cantPorciones, " +
                "tam.habilitado AS tamanio_habilitado " +
                "FROM Pizza p " +
                "JOIN VariedadPizza v ON p.variedadPizzaId = v.id " +
                "JOIN TipoPizza t ON p.tipoPizzaId = t.id " +
                "JOIN TamanioPizza tam ON p.tamanioPizzaId = tam.id " +
                "WHERE p.id = ?"; // Agregar la condición para buscar por ID

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, pizzaId); // Establecer el ID de la pizza a buscar
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Pizza pizza = new Pizza();
                    pizza.setId(resultSet.getInt("pizza_id"));
                    pizza.setNombre(resultSet.getString("pizza_nombre"));
                    pizza.setPrecio(resultSet.getDouble("pizza_precio"));

                    VariedadPizza variedadPizza = new VariedadPizza();
                    variedadPizza.setId(resultSet.getInt("variedad_id"));
                    variedadPizza.setNombre(resultSet.getString("variedad_nombre"));
                    variedadPizza.setHabilitado(resultSet.getBoolean("variedad_habilitado"));

                    TipoPizza tipoPizza = new TipoPizza();
                    tipoPizza.setId(resultSet.getInt("tipo_id"));
                    tipoPizza.setNombre(resultSet.getString("tipo_nombre"));
                    tipoPizza.setHabilitado(resultSet.getBoolean("tipo_habilitado"));

                    TamanioPizza tamanioPizza = new TamanioPizza();
                    tamanioPizza.setId(resultSet.getInt("tamanio_id"));
                    tamanioPizza.setNombre(resultSet.getString("tamanio_nombre"));
                    tamanioPizza.setCantPorciones(resultSet.getInt("tamanio_cantPorciones"));
                    tamanioPizza.setHabilitado(resultSet.getBoolean("tamanio_habilitado"));

                    pizza.setVariedadPizza(variedadPizza);
                    pizza.setTipoPizza(tipoPizza);
                    pizza.setTamanioPizza(tamanioPizza);
                    pizza.setHabilitado(resultSet.getBoolean("pizza_habilitado"));

                    return pizza; // Devolver la pizza encontrada
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Retornar null si no se encontró la pizza
    }

    public List<Pizza> obtenerPizzas() {
        List<Pizza> pizzas = new ArrayList<>();

        String sql = "SELECT " +
                "p.id AS pizza_id, " +
                "p.nombre AS pizza_nombre, " +
                "p.precio AS pizza_precio, " +
                "p.habilitado AS pizza_habilitado, " +
                "v.id AS variedad_id, " +
                "v.nombre AS variedad_nombre, " +
                "v.ingredientes AS variedad_ingredientes, " +
                "v.habilitado AS variedad_habilitado, " +
                "t.id AS tipo_id, " +
                "t.nombre AS tipo_nombre, " +
                "t.habilitado AS tipo_habilitado, " +
                "tam.id AS tamanio_id, " +
                "tam.nombre AS tamanio_nombre, " +
                "tam.cantPorciones AS tamanio_cantPorciones, " +
                "tam.habilitado AS tamanio_habilitado " +
                "FROM Pizza p " +
                "JOIN VariedadPizza v ON p.variedadPizzaId = v.id " +
                "JOIN TipoPizza t ON p.tipoPizzaId = t.id " +
                "JOIN TamanioPizza tam ON p.tamanioPizzaId = tam.id ";
        // "WHERE p.habilitado = ? " +
        // "AND v.habilitado = ? " +
        // "AND t.habilitado = ? " +
        // "AND tam.habilitado = ?";


        try (PreparedStatement statement = conn.prepareStatement(sql)) {

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Pizza pizza = new Pizza();
                    pizza.setId(resultSet.getInt("pizza_id"));
                    pizza.setNombre(resultSet.getString("pizza_nombre"));
                    pizza.setPrecio(resultSet.getDouble("pizza_precio"));

                    VariedadPizza variedadPizza = new VariedadPizza();
                    variedadPizza.setId(resultSet.getInt("variedad_id"));
                    variedadPizza.setNombre(resultSet.getString("variedad_nombre"));
                    variedadPizza.setHabilitado(resultSet.getBoolean("variedad_habilitado"));

                    TipoPizza tipoPizza = new TipoPizza();
                    tipoPizza.setId(resultSet.getInt("tipo_id"));
                    tipoPizza.setNombre(resultSet.getString("tipo_nombre"));
                    tipoPizza.setHabilitado(resultSet.getBoolean("tipo_habilitado"));

                    TamanioPizza tamanioPizza = new TamanioPizza();
                    tamanioPizza.setId(resultSet.getInt("tamanio_id"));
                    tamanioPizza.setNombre(resultSet.getString("tamanio_nombre"));
                    tamanioPizza.setCantPorciones(resultSet.getInt("tamanio_cantPorciones"));
                    tamanioPizza.setHabilitado(resultSet.getBoolean("tamanio_habilitado"));

                    pizza.setVariedadPizza(variedadPizza);
                    pizza.setTipoPizza(tipoPizza);
                    pizza.setTamanioPizza(tamanioPizza);
                    pizza.setHabilitado(resultSet.getBoolean("pizza_habilitado"));

                    pizzas.add(pizza);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pizzas;
    }

        public List<Pizza> obtenerPizzas(boolean habilitado) {
            List<Pizza> pizzas = new ArrayList<>();

            String sql = "SELECT " +
                    "p.id AS pizza_id, " +
                    "p.nombre AS pizza_nombre, " +
                    "p.precio AS pizza_precio, " +
                    "p.habilitado AS pizza_habilitado, " +
                    "v.id AS variedad_id, " +
                    "v.nombre AS variedad_nombre, " +
                    "v.ingredientes AS variedad_ingredientes, " +
                    "v.habilitado AS variedad_habilitado, " +
                    "t.id AS tipo_id, " +
                    "t.nombre AS tipo_nombre, " +
                    "t.habilitado AS tipo_habilitado, " +
                    "tam.id AS tamanio_id, " +
                    "tam.nombre AS tamanio_nombre, " +
                    "tam.cantPorciones AS tamanio_cantPorciones, " +
                    "tam.habilitado AS tamanio_habilitado " +
                    "FROM Pizza p " +
                    "JOIN VariedadPizza v ON p.variedadPizzaId = v.id " +
                    "JOIN TipoPizza t ON p.tipoPizzaId = t.id " +
                    "JOIN TamanioPizza tam ON p.tamanioPizzaId = tam.id " +
                    "WHERE p.habilitado = ? " +
                    "AND v.habilitado = ? " +
                    "AND t.habilitado = ? " +
                    "AND tam.habilitado = ?";


            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setBoolean(1, habilitado);
                statement.setBoolean(2, habilitado);
                statement.setBoolean(3, habilitado);
                statement.setBoolean(4, habilitado);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Pizza pizza = new Pizza();
                        pizza.setId(resultSet.getInt("pizza_id"));
                        pizza.setNombre(resultSet.getString("pizza_nombre"));
                        pizza.setPrecio(resultSet.getDouble("pizza_precio"));

                        VariedadPizza variedadPizza = new VariedadPizza();
                        variedadPizza.setId(resultSet.getInt("variedad_id"));
                        variedadPizza.setNombre(resultSet.getString("variedad_nombre"));
                        variedadPizza.setHabilitado(resultSet.getBoolean("variedad_habilitado"));

                        TipoPizza tipoPizza = new TipoPizza();
                        tipoPizza.setId(resultSet.getInt("tipo_id"));
                        tipoPizza.setNombre(resultSet.getString("tipo_nombre"));
                        tipoPizza.setHabilitado(resultSet.getBoolean("tipo_habilitado"));

                        TamanioPizza tamanioPizza = new TamanioPizza();
                        tamanioPizza.setId(resultSet.getInt("tamanio_id"));
                        tamanioPizza.setNombre(resultSet.getString("tamanio_nombre"));
                        tamanioPizza.setCantPorciones(resultSet.getInt("tamanio_cantPorciones"));
                        tamanioPizza.setHabilitado(resultSet.getBoolean("tamanio_habilitado"));

                        pizza.setVariedadPizza(variedadPizza);
                        pizza.setTipoPizza(tipoPizza);
                        pizza.setTamanioPizza(tamanioPizza);
                        pizza.setHabilitado(resultSet.getBoolean("pizza_habilitado"));

                        pizzas.add(pizza);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return pizzas;
        }

    public boolean insertarPizza(Pizza pizza) {
        String sql = "INSERT INTO Pizza (nombre, precio, variedadPizzaId, tipoPizzaId, tamanioPizzaId, habilitado) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, pizza.getNombre());
            statement.setDouble(2, pizza.getPrecio());
            statement.setInt(3, pizza.getVariedadPizza().getId());
            statement.setInt(4, pizza.getTipoPizza().getId());
            statement.setInt(5, pizza.getTamanioPizza().getId());
            statement.setBoolean(6, pizza.isHabilitado());

            int filasInsertadas = statement.executeUpdate();
            return filasInsertadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarPizza(Pizza pizza) {
        String sql = "UPDATE Pizza SET nombre = ?, precio = ?, variedadPizzaId = ?, tipoPizzaId = ?, tamanioPizzaId = ?, habilitado = ? WHERE id = ?";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, pizza.getNombre());
            statement.setDouble(2, pizza.getPrecio());
            statement.setInt(3, pizza.getVariedadPizza().getId());
            statement.setInt(4, pizza.getTipoPizza().getId());
            statement.setInt(5, pizza.getTamanioPizza().getId());
            statement.setBoolean(6, pizza.isHabilitado());
            statement.setInt(7, pizza.getId());

            int filasActualizadas = statement.executeUpdate();
            return filasActualizadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
