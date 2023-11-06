package com.ebp.trabajointegrador.vista;

import com.ebp.trabajointegrador.accesodatos.PedidoDAO;
import com.ebp.trabajointegrador.modelo.DetallePedido;
import com.ebp.trabajointegrador.modelo.Pedido;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CocinaForm  extends  JFrame{
    private JPanel panel;
    private JTable tablaPedidos;
    private JScrollPane scrollDetalle;
    private JTable tablaDetallesPedido;
    private JButton btnProcesar;
    private JButton btnEntregar;
    private JButton btnActualizar;


    private DefaultTableModel pedidosTableModel;
    private DefaultTableModel detallesPedidoTableModel;

    private Connection connection;
    private PedidoDAO pedidoDAO;

    private List<Pedido> pedidos = new ArrayList<>();

    private Pedido pedidoSeleccionado;
    private final Timer timer;

    public CocinaForm(Connection conn) {
        this.connection = conn;
        this.pedidoDAO = new PedidoDAO(conn);

        setTitle("Pizzería - Pedidos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        crearTablaPedidos();
        crearTablaDetalles();

        cargarPedidosEnTabla();

        btnProcesar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pedidoSeleccionado != null) {

                        int confirmacion = JOptionPane.showConfirmDialog(CocinaForm.this,
                                "¿Desea procesar el pedido N° " + pedidoSeleccionado.getId() +"?",
                                "Comenzar a elaborar",
                                JOptionPane.YES_NO_OPTION);

                        if (confirmacion == JOptionPane.YES_OPTION) {
                            boolean procesado = pedidoDAO.procesarPedido(pedidoSeleccionado);
                            if (procesado) {
                                actualizarPedidos();
                                JOptionPane.showMessageDialog(CocinaForm.this, "Pedido procesado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(CocinaForm.this, "Error al procesar el pedido.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                }
            }
        });
        btnEntregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pedidoSeleccionado != null) {

                    int confirmacion = JOptionPane.showConfirmDialog(CocinaForm.this,
                            "¿Desea entregar el pedido N° " + pedidoSeleccionado.getId() +" al área de ventas?",
                            "Despachar pedido",
                            JOptionPane.YES_NO_OPTION);

                    if (confirmacion == JOptionPane.YES_OPTION) {
                        boolean despachado = pedidoDAO.despacharPedido(pedidoSeleccionado);
                        if (despachado) {
                            actualizarPedidos();
                            JOptionPane.showMessageDialog(CocinaForm.this, "Pedido despachado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(CocinaForm.this, "Error al despachado el pedido.", "Error", JOptionPane.ERROR_MESSAGE);
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


        timer = new Timer(30000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarPedidos();
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                // Detener el temporizador cuando se cierra la ventana
                timer.stop();
            }
        });

        // Inicia el temporizador
        timer.start();

        add(panel);
        setVisible(true);
    }

    private void crearTablaPedidos() {
        pedidosTableModel = new DefaultTableModel(new Object[]{"Número", "Fecha", "Hora", "Cliente", "Estado"}, 0) {
        };

        tablaPedidos.setModel(pedidosTableModel);

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
        detallesPedidoTableModel = new DefaultTableModel(new Object[]{"Cantidad", "Pizza", "Tipo", "Variedad", "Tamaño"}, 0);
        tablaDetallesPedido.setModel(detallesPedidoTableModel);
        tablaDetallesPedido.setDefaultEditor(Object.class, null);
    }

    private void cargarPedidosEnTabla() {
        DefaultTableModel pedidosTableModel = (DefaultTableModel) tablaPedidos.getModel();
        pedidosTableModel.setRowCount(0);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        pedidos = pedidoDAO.obtenerPedidosCocinaAyerYHoy();

        for (Pedido pedido : pedidos) {
            LocalDateTime fechaHoraCreacion = pedido.getFechaHoraCreacion();
            String fecha = fechaHoraCreacion.format(dateFormatter);
            String hora = fechaHoraCreacion.format(timeFormatter);

            pedidosTableModel.addRow(new Object[]{
                    pedido.getId(),
                    fecha,
                    hora,
                    pedido.getNombreCliente(),
                    pedido.getEstado().getDescripcion(),
                    pedido.getEstado().getNombre()
            });
        }
    }

    private void cargarDetallesPedidoEnTabla(int selectedRow) {
        if (selectedRow >= 0) {
            pedidoSeleccionado = pedidos.get(selectedRow);

            btnProcesar.setEnabled(pedidoSeleccionado.estaRegistrado());

            btnEntregar.setEnabled(pedidoSeleccionado.estaEnPreparacion());

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
                });
            }

            pedidoSeleccionado.setDetallesPedidoSet(new HashSet<>(detallesPedido));
        }
    }

    private void actualizarPedidos() {
        cargarPedidosEnTabla();
        int selectedRow = tablaPedidos.getSelectedRow();
        if (selectedRow >= 0) {
            cargarDetallesPedidoEnTabla(selectedRow);
        } else {
            btnEntregar.setEnabled(false);

            DefaultTableModel detallesPedidoTableModel = (DefaultTableModel) tablaDetallesPedido.getModel();
            detallesPedidoTableModel.setRowCount(0);
        }
    }

}


