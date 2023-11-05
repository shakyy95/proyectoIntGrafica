package com.ebp.trabajointegrador.accesodatos;


import com.ebp.trabajointegrador.modelo.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PedidoDAO {
    private Connection connection;

    public PedidoDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean insertarPedido(Pedido pedido) {
        String insertPedidoQuery = "INSERT INTO pedido (fechaHoraCreacion, nombreCliente, estadoPedidoId) VALUES (?, ?, ?)";
        Set<DetallePedido> detallesPedido = pedido.getDetallesPedidoSet();
        int pedidoId = -1;

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement stmt = connection.prepareStatement(insertPedidoQuery, Statement.RETURN_GENERATED_KEYS)) {
                LocalDateTime fechaHoraActual = LocalDateTime.now();
                Timestamp timestamp = Timestamp.valueOf(fechaHoraActual);
                stmt.setTimestamp(1, timestamp);
                stmt.setString(2, pedido.getNombreCliente());
                stmt.setInt(3, 1);

                int filasAfectadas = stmt.executeUpdate();

                if (filasAfectadas == 0) {
                    connection.rollback();
                    return false;
                }

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        pedidoId = generatedKeys.getInt(1);

                        for (DetallePedido detalle : detallesPedido) {
                            if (!insertarDetallePedido(detalle, pedidoId)) {
                                // Si no se pudo insertar un detalle, hacemos un rollback y retornamos false
                                connection.rollback();
                                return false;
                            }
                        }
                    }
                }

                return true;
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
                return false;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<DetallePedido> obtenerDetallesPedido(Pedido pedido) {
        List<DetallePedido> detallesPedido = new ArrayList<>();
        String query = "SELECT dp.id, dp.cantidad, dp.precio, " +
                "p.id AS pizzaId, p.nombre AS pizzaNombre, " +
                "t.id AS tipoId, t.nombre AS tipoNombre, " +
                "v.id AS variedadId, v.nombre AS variedadNombre, " +
                "ta.id AS tamanioId,ta.nombre AS tamanioNombre, ta.cantPorciones AS tamanioCantidadPorciones " +
                "FROM detallepedido dp " +
                "INNER JOIN pizza p ON dp.pizzaId = p.id " +
                "INNER JOIN tipopizza t ON p.tipoPizzaId = t.id " +
                "INNER JOIN variedadpizza v ON p.variedadPizzaId = v.id " +
                "INNER JOIN tamaniopizza ta ON p.tamanioPizzaId = ta.id " +
                "WHERE dp.pedidoId = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, pedido.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int cantidad = rs.getInt("cantidad");
                double precio = rs.getDouble("precio");

                int pizzaId = rs.getInt("pizzaId");
                String pizzaNombre = rs.getString("pizzaNombre");

                int tipoId = rs.getInt("tipoId");
                String tipoNombre = rs.getString("tipoNombre");

                int variedadId = rs.getInt("variedadId");
                String variedadNombre = rs.getString("variedadNombre");

                int tamanioId = rs.getInt("tamanioId");
                String tamanioNombre = rs.getString("tamanioNombre");
                int tamanioCantidadPorciones = rs.getInt("tamanioCantidadPorciones");

                TipoPizza tipoPizza = new TipoPizza();
                tipoPizza.setId(tipoId);
                tipoPizza.setNombre(tipoNombre);

                VariedadPizza variedad = new VariedadPizza();
                variedad.setId(variedadId);
                variedad.setNombre(variedadNombre);

                TamanioPizza tamanio = new TamanioPizza();
                tamanio.setId(tamanioId);
                tamanio.setCantPorciones(tamanioCantidadPorciones);
                tamanio.setNombre(tamanioNombre);


                Pizza pizza = new Pizza(pizzaId, pizzaNombre, tipoPizza, variedad, tamanio);

                DetallePedido detallePedido = new DetallePedido(id, cantidad, precio, pizza);
                detallesPedido.add(detallePedido);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return detallesPedido;
    }

    public List<Pedido> obtenerPedidos() {
        List<Pedido> pedidos = new ArrayList<>();
        String query = "SELECT " +
                "p.id AS pedidoId, " +
                "p.fechaHoraCreacion, " +
                "p.nombreCliente, " +
                "p.pagado, " +
                "ep.id AS estadoPedidoId, " +
                "ep.nombre AS estadoPedidoNombre, " +
                "f.id AS facturaId, " +
                "f.fechaHoraEmision AS facturaFechaHoraEmision " +
                "FROM pedido p " +
                "INNER JOIN estadopedido ep ON p.estadoPedidoId = ep.id " +
                "LEFT JOIN factura f ON p.facturaId = f.id";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int pedidoId = rs.getInt("pedidoId");
                Timestamp fechaHoraCreacion = rs.getTimestamp("fechaHoraCreacion");
                Timestamp facturaFechaHoraEmision = rs.getTimestamp("facturaFechaHoraEmision");
                String nombreCliente = rs.getString("nombreCliente");
                int estadoPedidoId = rs.getInt("estadoPedidoId");
                String estadoPedidoNombre = rs.getString("estadoPedidoNombre");
                int facturaId = rs.getInt("facturaId");
                boolean pagado = rs.getBoolean("pagado");


                Factura factura = (facturaId != 0) ? new Factura(facturaId,facturaFechaHoraEmision) : null;

                Pedido pedido = new Pedido(pedidoId, nombreCliente, estadoPedidoId, estadoPedidoNombre, factura,pagado);
                pedido.setFechaHoraCreacion(fechaHoraCreacion.toLocalDateTime());

                pedidos.add(pedido);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pedidos;
    }

    public boolean cancelarPedido(Pedido pedido) {
        String estadoCanceladoNombre = EstadoPedido.EstadoPedidoEnum.CANCELADO.toString();

        String query = "UPDATE pedido SET estadoPedidoId = (SELECT id FROM estadopedido WHERE nombre = ?) WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, estadoCanceladoNombre);
            stmt.setInt(2, pedido.getId());
            int rowsAffected = stmt.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean entregarPedido(Pedido pedido) {
        String estado = EstadoPedido.EstadoPedidoEnum.ENTREGADO.name();
        LocalDateTime fechaHoraEntrega = LocalDateTime.now();

        String query = "UPDATE pedido SET estadoPedidoId = (SELECT id FROM estadopedido WHERE nombre = ?), fechaHoraEntrega = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, estado);
            stmt.setTimestamp(2, Timestamp.valueOf(fechaHoraEntrega));
            stmt.setInt(3, pedido.getId());
            int rowsAffected = stmt.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean registrarPago(Pedido pedido) {
        String query = "UPDATE pedido SET pagado = true WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, pedido.getId());
            int rowsAffected = stmt.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Pedido> obtenerPedidosDeAyerYHoy() {
        List<Pedido> pedidos = new ArrayList<>();

        LocalDateTime ayer = LocalDateTime.now().minusDays(1);
        LocalDateTime hoy = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);

        String query = "SELECT " +
                "p.id AS pedidoId, " +
                "p.fechaHoraCreacion, " +
                "p.nombreCliente, " +
                "p.pagado, " +
                "ep.id AS estadoPedidoId, " +
                "ep.nombre AS estadoPedidoNombre, " +
                "f.id AS facturaId, " +
                "f.fechaHoraEmision AS facturaFechaHoraEmision " +
                "FROM pedido p " +
                "INNER JOIN estadopedido ep ON p.estadoPedidoId = ep.id " +
                "LEFT JOIN factura f ON p.facturaId = f.id " +
                "WHERE p.fechaHoraCreacion BETWEEN ? AND ? " +
                "ORDER BY p.id desc";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setTimestamp(1, Timestamp.valueOf(ayer));
            stmt.setTimestamp(2, Timestamp.valueOf(hoy));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int pedidoId = rs.getInt("pedidoId");
                    Timestamp fechaHoraCreacion = rs.getTimestamp("fechaHoraCreacion");
                    Timestamp facturaFechaHoraEmision = rs.getTimestamp("facturaFechaHoraEmision");
                    String nombreCliente = rs.getString("nombreCliente");
                    int estadoPedidoId = rs.getInt("estadoPedidoId");
                    String estadoPedidoNombre = rs.getString("estadoPedidoNombre");
                    int facturaId = rs.getInt("facturaId");
                    boolean pagado = rs.getBoolean("pagado");


                    Factura factura = (facturaId != 0) ? new Factura(facturaId,facturaFechaHoraEmision) : null;

                    Pedido pedido = new Pedido(pedidoId, nombreCliente, estadoPedidoId, estadoPedidoNombre, factura,pagado);
                    pedido.setFechaHoraCreacion(fechaHoraCreacion.toLocalDateTime());

                    pedidos.add(pedido);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pedidos;
    }

    public boolean generarFactura(Pedido pedido) {
        String insertFacturaQuery = "INSERT INTO factura (fechaHoraEmision) VALUES (?)";
        String updatePedidoQuery = "UPDATE pedido SET facturaId = ? WHERE id = ?";

        try (         PreparedStatement insertFacturaStmt = connection.prepareStatement(insertFacturaQuery, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement updatePedidoStmt = connection.prepareStatement(updatePedidoQuery)) {

Timestamp fechaFactura = Timestamp.valueOf(LocalDateTime.now());
            // Insertar la nueva factura
            insertFacturaStmt.setTimestamp(1, fechaFactura); // Fecha actual
            insertFacturaStmt.executeUpdate();

            // Obtener el ID de la factura generada
            ResultSet generatedKeys = insertFacturaStmt.getGeneratedKeys();
            int facturaId = 0;
            if (generatedKeys.next()) {
                facturaId = generatedKeys.getInt(1);
            }


            // Actualizar el pedido con la factura generada
            updatePedidoStmt.setInt(1, facturaId);
            updatePedidoStmt.setInt(2, pedido.getId());
            int rowsAffected = updatePedidoStmt.executeUpdate();

            pedido.setFactura(new Factura(facturaId, fechaFactura));

            return rowsAffected > 0; // Devuelve true si se actualizó al menos una fila
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejo de errores (puedes lanzar una excepción personalizada si es necesario)
            return false;
        }
    }

    public boolean actualizarEstadoPedido(int pedidoId, int estadoPedidoId) {
        String query = "UPDATE pedido SET estadoPedidoId = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, estadoPedidoId);
            stmt.setInt(2, pedidoId);

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean insertarDetallePedido(DetallePedido detalle, int pedidoId) {
        String insertDetallePedidoQuery = "INSERT INTO detallepedido (cantidad, precio, pizzaId, pedidoId) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(insertDetallePedidoQuery)) {
            stmt.setInt(1, detalle.getCantidad());
            stmt.setDouble(2, detalle.getPrecio());
            stmt.setInt(3, detalle.getPizzaId());
            stmt.setInt(4, pedidoId);

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
