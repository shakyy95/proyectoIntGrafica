package com.ebp.trabajointegrador.modelo;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Pedido {
    private int id;
    private LocalDateTime fechaHoraCreacion;
    private LocalDateTime fechaHoraEntrega;
    private String nombreCliente;

    private boolean pagado;
    private Factura factura;
    private EstadoPedido estadoPedido;
    private Set<DetallePedido> detallesPedidoSet;

    public Pedido() {
    }

    public Pedido(String nombreCliente) {
        this.nombreCliente = nombreCliente;
        this.fechaHoraCreacion = LocalDateTime.now();
        this.estadoPedido = new EstadoPedido(EstadoPedido.EstadoPedidoEnum.REGISTRADO);
        this.detallesPedidoSet = new HashSet<>();
    }

    public Pedido(int id, String nombreCliente, int estadoPedidoId, String estadoPedidoNombre, Factura factura, boolean pagado) {
        this.id = id;
        this.nombreCliente = nombreCliente;
        this.estadoPedido = new EstadoPedido(id, EstadoPedido.EstadoPedidoEnum.valueOf(estadoPedidoNombre));
        this.factura = factura;
        this.pagado = pagado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getFechaHoraCreacion() {
        return fechaHoraCreacion;
    }

    public void setFechaHoraCreacion(LocalDateTime fechaHoraCreacion) {
        this.fechaHoraCreacion = fechaHoraCreacion;
    }

    public LocalDateTime getFechaHoraEntrega() {
        return fechaHoraEntrega;
    }

    public void setFechaHoraEntrega(LocalDateTime fechaHoraEntrega) {
        this.fechaHoraEntrega = fechaHoraEntrega;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public EstadoPedido getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(EstadoPedido estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    public Set<DetallePedido> getDetallesPedidoSet() {
        return detallesPedidoSet;
    }

    public void setDetallesPedidoSet(Set<DetallePedido> detallesPedidoSet) {
        this.detallesPedidoSet = detallesPedidoSet;
    }

    public void agregarDetallePedido(DetallePedido detallePedido) {
        detallesPedidoSet.add(detallePedido);
    }

    public double calcularTotalPedido() {
        double total = 0.0;
        for (DetallePedido detalle : detallesPedidoSet) {
            total += detalle.calcularSubtotal();
        }
        return total;
    }

    public void cancelar() {
        estadoPedido = new EstadoPedido(EstadoPedido.EstadoPedidoEnum.CANCELADO);
    }

    public void confirmar() {
        estadoPedido = new EstadoPedido(EstadoPedido.EstadoPedidoEnum.REGISTRADO);
    }

    public boolean esPteFacturacion() {
        return getFactura() == null;
    }

    public Factura factura(int numeroFactura, Timestamp fecha) {
        factura = new Factura(numeroFactura, fecha);
        for (DetallePedido detalle : detallesPedidoSet) {
            factura.agregarDetallePedido(detalle);
        }
        //estadoPedido = new EstadoPedido(EstadoPedido.EstadoPedidoEnum.FACTURADO);
        return factura;
    }

    public Set<DetallePedido> getDetallePedido() {
        return detallesPedidoSet;
    }

    public EstadoPedido getEstado() {
        return estadoPedido;
    }

    public Set<DetallePedido> obtenerDetallesPedido() {
        return detallesPedidoSet;
    }

    public void setEstado(EstadoPedido estado) {
        estadoPedido = estado;
    }

    public void setPagado(boolean pagado) {
        this.pagado = pagado;
    }

    public boolean getPagado() {
        return this.pagado;
    }

    public void terminar() {
        estadoPedido = new EstadoPedido(EstadoPedido.EstadoPedidoEnum.ENTREGADO);
        fechaHoraEntrega = LocalDateTime.now();
    }

    public boolean estaEntregado() {
        return this.estadoPedido.getNombre() == EstadoPedido.EstadoPedidoEnum.ENTREGADO;
    }

    public boolean estaListoParaEntregar() {
        return this.estadoPedido.getNombre() == EstadoPedido.EstadoPedidoEnum.LISTO_PARA_ENTREGAR;
    }

    public boolean estaCancelado() {
        return this.estadoPedido.getNombre() == EstadoPedido.EstadoPedidoEnum.CANCELADO;
    }

    public boolean estaRegistrado() {
        return this.estadoPedido.getNombre() == EstadoPedido.EstadoPedidoEnum.REGISTRADO;
    }

    public boolean estaEnPreparacion() {
        return this.estadoPedido.getNombre() == EstadoPedido.EstadoPedidoEnum.PREPARACION;
    }
}

