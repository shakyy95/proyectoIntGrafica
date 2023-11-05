package com.ebp.trabajointegrador.modelo;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Factura {
    private Timestamp fechaHoraEmision;
    private int id;
    private Set<DetallePedido> detallePedidoSet;

    public Factura(int id, Timestamp fechaHoraEmision) {
        this.id = id;
        this.fechaHoraEmision = fechaHoraEmision;
        this.detallePedidoSet = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public  void setId(int id )
    {
        this.id = id;
    }

    public Timestamp getFechaHoraEmision() {
        return fechaHoraEmision;
    }

    public void setFechaHoraEmision(Timestamp fechaHoraEmision)
    {
        this.fechaHoraEmision = fechaHoraEmision;
    }

    public void agregarDetallePedido(DetallePedido detallePedido) {
        detallePedidoSet.add(detallePedido);
    }

    public Set<DetallePedido> buscarItems() {
        return this.detallePedidoSet;
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


}
