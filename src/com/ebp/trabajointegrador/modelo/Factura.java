package com.ebp.trabajointegrador.modelo;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Factura {
    private Timestamp fechaHoraEmision;
    private int id;
    private Set<DetallePedido> detallePedidoSet;

    private String cliente;

    public Factura(int id, Timestamp fechaHoraEmision) {
        this.id = id;
        this.fechaHoraEmision = fechaHoraEmision;
        this.detallePedidoSet = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getFechaHoraEmision() {
        return fechaHoraEmision;
    }

    public void setFechaHoraEmision(Timestamp fechaHoraEmision) {
        this.fechaHoraEmision = fechaHoraEmision;
    }

    public void agregarDetallePedido(DetallePedido detallePedido) {
        detallePedidoSet.add(detallePedido);
    }

    public Set<DetallePedido> buscarItems() {
        return this.detallePedidoSet;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public double calcularTotalFactura() {
        double total = 0.0;
        for (DetallePedido detalle : detallePedidoSet) {
            total += detalle.calcularSubtotal();
        }
        return total;
    }

    public Set<DetallePedido> getDetalleFactura() {
        return detallePedidoSet;
    }

    public void agregarDetalles(Set<DetallePedido> detallePedidos) {
        this.detallePedidoSet = new HashSet<>(detallePedidos);
    }

    public String getFechaEmision() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date(fechaHoraEmision.getTime());
        return dateFormat.format(date);
    }

    public String getHoraEmision() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date(fechaHoraEmision.getTime());
        return timeFormat.format(date);
    }

}
