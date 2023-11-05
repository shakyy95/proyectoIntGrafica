package com.ebp.trabajointegrador.vista;

import com.ebp.trabajointegrador.accesodatos.PedidoDAO;
import com.ebp.trabajointegrador.modelo.DetallePedido;
import com.ebp.trabajointegrador.modelo.Factura;
import com.ebp.trabajointegrador.modelo.Pedido;
import com.ebp.trabajointegrador.renders.PedidoCellRenderer;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.Connection;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class VentasForm extends JFrame {
    private JPanel panel;
    private JTable tablaPedidos;
    private JButton btnAgregarPedido;
    private JTable tablaDetallesPedido;
    private JScrollPane scrollDetalle;
    private JTextField txtMontoTotal;
    private JButton btnFacturar;
    private JButton btnCancelar;
    private JButton btnEntregar;
    private JButton btnRegistrarPago;
    private JButton btnActualizar;

    private DefaultTableModel pedidosTableModel;
    private DefaultTableModel detallesPedidoTableModel;

    private Connection connection;
    private PedidoDAO pedidoDAO;

    private List<Pedido> pedidos = new ArrayList<>();

    private Pedido pedidoSeleccionado;

    public VentasForm(Connection conn) {
        this.connection = conn;
        this.pedidoDAO = new PedidoDAO(conn);

        setTitle("Pizzería - Pedidos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        crearTablaPedidos();
        crearTablaDetalles();

        cargarPedidosEnTabla();


        btnAgregarPedido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    AltaPedidoForm altaPedidoForm = new AltaPedidoForm(VentasForm.this, conn);
                    altaPedidoForm.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            if (altaPedidoForm.exito) {
                                actualizarPedidos();
                            }
                        }
                    });
                    altaPedidoForm.setLocationRelativeTo(VentasForm.this);
                    altaPedidoForm.pack();
                    altaPedidoForm.setVisible(true);
                });
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pedidoSeleccionado != null) {
                    int confirmacion = JOptionPane.showConfirmDialog(VentasForm.this,
                            "¿Desea cancelar el pedido N° " + pedidoSeleccionado.getId() + " del cliente " + pedidoSeleccionado.getNombreCliente() + "?",
                            "Confirmar Cancelación",
                            JOptionPane.YES_NO_OPTION);

                    if (confirmacion == JOptionPane.YES_OPTION) {
                        boolean cancelado = pedidoDAO.cancelarPedido(pedidoSeleccionado);
                        if (cancelado) {
                            actualizarPedidos();
                            JOptionPane.showMessageDialog(VentasForm.this, "Pedido cancelado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(VentasForm.this, "Error al cancelar el pedido.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });

        btnFacturar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pedidoSeleccionado != null) {
                    if(pedidoSeleccionado.getFactura() != null)
                    {
                        JOptionPane.showMessageDialog(VentasForm.this, "Muestro la factura.. En desarrollo.", "Factura N°: " + pedidoSeleccionado.getFactura().getId(), JOptionPane.INFORMATION_MESSAGE);
                    }else
                    {
                        int confirmacion = JOptionPane.showConfirmDialog(VentasForm.this,
                                "¿Desea generar la factura para el pedido N° " + pedidoSeleccionado.getId() + " del cliente " + pedidoSeleccionado.getNombreCliente() + "?",
                                "Confirmar Cancelación",
                                JOptionPane.YES_NO_OPTION);

                        if (confirmacion == JOptionPane.YES_OPTION) {
                           boolean generada = pedidoDAO.generarFactura(pedidoSeleccionado);
                            if (generada) {
                                int confirmacionAbrir = JOptionPane.showConfirmDialog(VentasForm.this,
                                        "Factura generada con éxito. ¿Desea visualizarla?",
                                        "Factura generada",
                                        JOptionPane.YES_NO_OPTION);
                                if (confirmacionAbrir == JOptionPane.YES_OPTION) {
                                    JOptionPane.showMessageDialog(VentasForm.this, "Muestro la factura.. En desarrollo.", "Factura N°:" + pedidoSeleccionado.getFactura().getId(), JOptionPane.INFORMATION_MESSAGE);


                                }

                                actualizarPedidos();
                            } else {
                                JOptionPane.showMessageDialog(VentasForm.this, "Error al generar la factura.", "Error", JOptionPane.ERROR_MESSAGE);
                            }

                        }
                    }
                }
            }
        });

        btnRegistrarPago.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pedidoSeleccionado != null) {
                    int confirmacion = JOptionPane.showConfirmDialog(VentasForm.this,
                            "¿Desea registrar el pago del pedido N° " + pedidoSeleccionado.getId() + " del cliente " + pedidoSeleccionado.getNombreCliente() + "?",
                            "Confirmar Cancelación",
                            JOptionPane.YES_NO_OPTION);

                    if (confirmacion == JOptionPane.YES_OPTION) {
                        boolean registrado = pedidoDAO.registrarPago(pedidoSeleccionado);
                        if (registrado) {
                            actualizarPedidos();
                            JOptionPane.showMessageDialog(VentasForm.this, "Pago registrado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(VentasForm.this, "Error al registrar el pago del pedido.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });

        btnEntregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pedidoSeleccionado != null) {
                    if (!pedidoSeleccionado.getPagado()) {
                        JOptionPane.showMessageDialog(VentasForm.this, "El pedido debe estar pagado para realizar la entrega.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        int confirmacion = JOptionPane.showConfirmDialog(VentasForm.this,
                                "¿Desea entregar el pedido N° " + pedidoSeleccionado.getId() + " al cliente " + pedidoSeleccionado.getNombreCliente() + "?",
                                "Confirmar Entrega",
                                JOptionPane.YES_NO_OPTION);

                        if (confirmacion == JOptionPane.YES_OPTION) {
                            boolean entregado = pedidoDAO.entregarPedido(pedidoSeleccionado);
                            if (entregado) {
                                actualizarPedidos();
                                JOptionPane.showMessageDialog(VentasForm.this, "Pedido entregado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(VentasForm.this, "Error al entregar el pedido.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            }
        });

        btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               actualizarPedidos();
            }
        });

        add(panel);
        setVisible(true);
    }

    private void crearTablaPedidos() {
        pedidosTableModel = new DefaultTableModel(new Object[]{"Número", "Fecha", "Hora", "Cliente", "Facturado", "Pagado", "Estado", "EstadoCode"}, 0) {
        };

        tablaPedidos.setModel(pedidosTableModel);

        tablaPedidos.getColumnModel().getColumn(6).setMinWidth(0);
        tablaPedidos.getColumnModel().getColumn(6).setMaxWidth(0);
        tablaPedidos.getColumnModel().getColumn(6).setPreferredWidth(0);

        tablaPedidos.setDefaultRenderer(Object.class, new PedidoCellRenderer());

        tablaPedidos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = tablaPedidos.getSelectedRow();
                if (selectedRow >= 0) {
                    cargarDetallesPedidoEnTabla(selectedRow);
                }
            }
        });

        tablaDetallesPedido.setCellSelectionEnabled(false);
        tablaDetallesPedido.setRowSelectionAllowed(true);

        tablaPedidos.setDefaultEditor(Object.class, null);
    }

    private void crearTablaDetalles() {
        detallesPedidoTableModel = new DefaultTableModel(new Object[]{"Cantidad", "Pizza", "Tipo", "Variedad", "Tamaño", "Precio", "Subtotal"}, 0);
        tablaDetallesPedido.setModel(detallesPedidoTableModel);
        tablaDetallesPedido.setDefaultEditor(Object.class, null);
    }

    private void cargarPedidosEnTabla() {
        DefaultTableModel pedidosTableModel = (DefaultTableModel) tablaPedidos.getModel();
        pedidosTableModel.setRowCount(0);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        pedidos = pedidoDAO.obtenerPedidosDeAyerYHoy();

        for (Pedido pedido : pedidos) {
            LocalDateTime fechaHoraCreacion = pedido.getFechaHoraCreacion();
            String fecha = fechaHoraCreacion.format(dateFormatter);
            String hora = fechaHoraCreacion.format(timeFormatter);

            String facturado = "NO";
            if (pedido.getFactura() != null) {
                facturado = "SI";
            }

            String pagado = "NO";
            if (pedido.getPagado()) {
                pagado = "SI";
            }

            pedidosTableModel.addRow(new Object[]{
                    pedido.getId(),
                    fecha,
                    hora,
                    pedido.getNombreCliente(),
                    facturado,
                    pagado,
                    pedido.getEstado().getDescripcion(),
                    pedido.getEstado().getNombre()
            });
        }
    }

    private void cargarDetallesPedidoEnTabla(int selectedRow) {
        if (selectedRow >= 0) {
            pedidoSeleccionado = pedidos.get(selectedRow);

            btnCancelar.setEnabled(!pedidoSeleccionado.estaEntregado() && !pedidoSeleccionado.estaListoParaEntregar() && !pedidoSeleccionado.estaCancelado() && !pedidoSeleccionado.estaEnPreparacion());

            btnFacturar.setEnabled(!pedidoSeleccionado.estaCancelado());

            btnRegistrarPago.setEnabled(!pedidoSeleccionado.getPagado());

            btnEntregar.setEnabled(pedidoSeleccionado.estaListoParaEntregar());


           if(pedidoSeleccionado.getFactura() != null) btnFacturar.setText("Mostrar factura");
           else btnFacturar.setText("Generar factura");

            List<DetallePedido> detallesPedido = pedidoDAO.obtenerDetallesPedido(pedidoSeleccionado);

            DefaultTableModel detallesPedidoTableModel = (DefaultTableModel) tablaDetallesPedido.getModel();
            detallesPedidoTableModel.setRowCount(0);

            NumberFormat formatter = NumberFormat.getCurrencyInstance();

            for (DetallePedido detalle : detallesPedido) {
                detallesPedidoTableModel.addRow(new Object[]{
                        detalle.getCantidad(),
                        detalle.getPizza().getNombre(),
                        detalle.getPizza().getTipoPizza().getNombre(),
                        detalle.getPizza().getVariedadPizza().getNombre(),
                        detalle.getPizza().getTamanioPizza().getCantPorciones(),
                        formatter.format(detalle.getPrecio()),
                        formatter.format(detalle.calcularSubtotal()),
                });
            }

            pedidoSeleccionado.setDetallesPedidoSet(new HashSet<>(detallesPedido));

            String montStr = formatter.format(pedidoSeleccionado.calcularTotalPedido());
            txtMontoTotal.setText(montStr);
        }
    }

    private void actualizarPedidos() {
        cargarPedidosEnTabla();
        int selectedRow = tablaPedidos.getSelectedRow();
        if (selectedRow >= 0) {
            cargarDetallesPedidoEnTabla(selectedRow);
        }
        else {
            btnRegistrarPago.setEnabled(false);
            btnCancelar.setEnabled(false);
            btnEntregar.setEnabled(false);
            btnFacturar.setEnabled(false);
            btnFacturar.setText("Generar factura");

            DefaultTableModel detallesPedidoTableModel = (DefaultTableModel) tablaDetallesPedido.getModel();
            detallesPedidoTableModel.setRowCount(0);

            txtMontoTotal.setText("");
        }
    }

    private void abrirFactura(Factura factura)
    {

    }
}

