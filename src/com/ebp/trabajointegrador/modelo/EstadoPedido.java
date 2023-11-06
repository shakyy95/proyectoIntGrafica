package com.ebp.trabajointegrador.modelo;

public class EstadoPedido {
    private int id;
    private EstadoPedidoEnum nombre;
    private String descripcion;

    public EstadoPedido() {
    }

    public EstadoPedido(int id, EstadoPedidoEnum nombre) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = obtenerDescripcionPorNombre(nombre);
    }

    public EstadoPedido(EstadoPedidoEnum nombre) {
        this.nombre = nombre;
        this.descripcion = obtenerDescripcionPorNombre(nombre);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public EstadoPedidoEnum getNombre() {
        return nombre;
    }

    public void setNombre(EstadoPedidoEnum nombre) {
        this.nombre = nombre;
        this.descripcion = obtenerDescripcionPorNombre(nombre);
    }

    public String getDescripcion() {
        return descripcion;
    }

    private String obtenerDescripcionPorNombre(EstadoPedidoEnum nombre) {
        switch (nombre) {
            case REGISTRADO:
                return "Pedido registrado";
            case PREPARACION:
                return "En preparación";
            case LISTO_PARA_ENTREGAR:
                return "Listo para entregar";
            case ENTREGADO:
                return "Entregado";
            case PENDIENTE_PAGO:
                return "Pendiente de pago";
            case PAGADO:
                return "Pagado";
            case CANCELADO:
                return "Pedido cancelado";
            default:
                return "";
        }
    }


    public enum EstadoPedidoEnum {
        REGISTRADO,
        PREPARACION,
        LISTO_PARA_ENTREGAR,
        ENTREGADO,
        PENDIENTE_PAGO,
        PAGADO,
        CANCELADO
    }
}