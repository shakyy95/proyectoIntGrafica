package com.ebp.trabajointegrador.modelo;

public class EstadoFactura {
    private final EstadoFacturaEnum nombre;

    public EstadoFactura(EstadoFacturaEnum nombre) {
        this.nombre = nombre;
    }

    public boolean esGenerada() {
        return nombre == EstadoFacturaEnum.GENERADA;
    }

    public boolean esPteFacturacion() {
        return nombre == EstadoFacturaEnum.PENDIENTE_FACTURACION;
    }

    public enum EstadoFacturaEnum {
        GENERADA("Generada"),
        PENDIENTE_FACTURACION("Pendiente de Facturación");

        private String valor;

        EstadoFacturaEnum(String valor) {
            this.valor = valor;
        }

        public String getValor() {
            return valor;
        }
    }
}