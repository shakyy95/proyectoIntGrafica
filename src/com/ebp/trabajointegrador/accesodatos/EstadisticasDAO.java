package com.ebp.trabajointegrador.accesodatos;

import com.ebp.trabajointegrador.modelo.reportes.IngresosPeriodo;
import com.ebp.trabajointegrador.modelo.reportes.PedidoCantidad;
import com.ebp.trabajointegrador.modelo.reportes.ResumenPeriodo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstadisticasDAO {

    private Connection connection;

    public EstadisticasDAO(Connection connection) {
        this.connection = connection;
    }
    public List<PedidoCantidad> obtenerCantidadPedidosPorPizza() {
        List<PedidoCantidad> pedidosCantidades = new ArrayList<>();
        String query = "SELECT vp.nombre AS VariedadPizza, tp.nombre AS TipoPizza, COUNT(dp.id) AS CantidadPedidos " +
                "FROM detallepedido dp " +
                "JOIN pedido pe ON dp.pedidoId = pe.id " +
                "JOIN pizza p ON dp.pizzaId = p.id " +
                "JOIN variedadpizza vp ON p.variedadPizzaId = vp.id " +
                "JOIN tipopizza tp ON p.tipoPizzaId = tp.id " +
                "WHERE pe.estadoPedidoId <> 7 " +
                "GROUP BY vp.nombre, tp.nombre " +
                "ORDER BY CantidadPedidos DESC;";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String variedad = rs.getString("VariedadPizza");
                String tipo = rs.getString("TipoPizza");
                int cantidad = rs.getInt("CantidadPedidos");

                PedidoCantidad pedidoCantidad = new PedidoCantidad(variedad, tipo,cantidad);

                pedidosCantidades.add(pedidoCantidad);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pedidosCantidades;
    }

    public List<IngresosPeriodo> obtenerPedidosPorPeriodo() {
        List<IngresosPeriodo> pedidos = new ArrayList<>();
        String query = "SELECT DATE_FORMAT(p.fechaHoraCreacion, '%m-%Y') AS Periodo, SUM(d.precio * d.cantidad) AS Ingresos " +
                "FROM pedido p " +
                "JOIN detallepedido d ON p.id = d.pedidoId " +
                "WHERE p.estadoPedidoId <> 7 " +
                "GROUP BY DATE_FORMAT(p.fechaHoraCreacion, '%m-%Y') " +
                "ORDER BY Periodo;";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String periodo = rs.getString("Periodo");
                double ingresos = rs.getDouble("Ingresos");

                IngresosPeriodo pedidosPeriodo = new IngresosPeriodo(periodo, ingresos);

                pedidos.add(pedidosPeriodo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pedidos;
    }

    public List<ResumenPeriodo> obtenerResumenPeriodo() {
        List<ResumenPeriodo> resumenPeriodos = new ArrayList<>();
        String query = "SELECT  " +
                "    DATE_FORMAT(p.fechaHoraCreacion, '%m-%Y') AS Periodo, " +
                "    COUNT(DISTINCT p.id) AS CantidadPedidos, " +
                "    SUM(d.precio * d.cantidad) AS MontoPedidos " +
                "FROM  " +
                "    pedido p " +
                "JOIN  " +
                "    detallepedido d ON p.id = d.pedidoId " +
                "WHERE  " +
                "    p.estadoPedidoId <> 7 " +
                "GROUP BY  " +
                "    DATE_FORMAT(p.fechaHoraCreacion, '%m-%Y') " +
                "ORDER BY  " +
                "    Periodo;";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String periodo = rs.getString("Periodo");
                double monto = rs.getDouble("MontoPedidos");
                int cantidadPedidos = rs.getInt("CantidadPedidos");

                ResumenPeriodo resumenPeriodo = new ResumenPeriodo(periodo,cantidadPedidos,monto);

                resumenPeriodos.add(resumenPeriodo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resumenPeriodos;
    }



}
