package com.ebp.trabajointegrador.vista;

import com.ebp.trabajointegrador.accesodatos.ApiGeoRef;
import com.ebp.trabajointegrador.accesodatos.PedidoDAO;
import com.ebp.trabajointegrador.accesodatos.PizzaDAO;
import com.ebp.trabajointegrador.modelo.*;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class AltaPedidoForm extends JDialog {
    private JTable table;
    private JButton btnAceptar;
    private JButton btnCancelar;
    private DefaultTableModel tableModel;
    private PizzaDAO pizzaDAO;
    private List<Pizza> pizzas;
    private JPanel panel;

    private final Connection connection;
    private JTextField txtCliente;
    private JScrollPane scrollPane1;
    private JTextField txtMontoTotal;
    private JComboBox cmbMunicipios;
    private JComboBox cmbProvincia;

    private double montoTotal = 0.0;
    public boolean exito = false;

    private final DefaultTableCellRenderer defaultRenderer = new DefaultTableCellRenderer();
    private final ApiGeoRef apiGeoRef;

    public AltaPedidoForm(Frame owner, Connection conn) {
        super(owner, "Alta de Pedido", true);

        setMinimumSize(new Dimension(800, 600));

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        this.apiGeoRef = new ApiGeoRef();

        this.connection = conn;

        pizzas = new ArrayList<>();
        pizzaDAO = new PizzaDAO(connection);

        createTable();
        buscarPizzas();

        agregarPizzasAModel();

        actualizarFormatoTabla();

        buscarProvincias();

        btnAceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (montoTotal > 0 && !txtCliente.getText().isEmpty()) {
                    agregarPedido();
                } else {
                    JOptionPane.showMessageDialog(AltaPedidoForm.this, "Por favor, ingresa un cliente y agrega al menos un producto.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cmbProvincia.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    JComboBox comboBox = (JComboBox) e.getSource();
                    Provincia selectedProvincia = (Provincia) comboBox.getSelectedItem();

                    // Cargar los municipios correspondientes a la provincia seleccionada
                    buscarMunicipios(selectedProvincia.getId());
                }
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        TableCellRenderer tableCellRenderer = (table, value, isSelected, hasFocus, row, column) -> {
            int cantidad = 0;
            try {
                cantidad = Integer.parseInt(table.getValueAt(row, 1).toString());
            } catch (Exception ignored) {
                table.setValueAt(cantidad, row, 1);
            }

            Component renderer = defaultRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // Cambia el color de fondo de toda la fila según la cantidad en la columna "Cant"
            if (cantidad > 0) {
                renderer.setBackground(new Color(191, 255, 191));  // Verde claro si cantidad > 0
            } else {
                renderer.setBackground(Color.WHITE);
            }

            if (value instanceof Integer) {
                defaultRenderer.setHorizontalAlignment(JLabel.CENTER);
            } else if (value instanceof Double) {
                defaultRenderer.setHorizontalAlignment(JLabel.RIGHT);
            }

            return renderer;
        };


        table.setDefaultRenderer(Object.class, tableCellRenderer);
        table.setDefaultRenderer(Double.class, tableCellRenderer);
        table.setDefaultRenderer(Integer.class, tableCellRenderer);


        tableModel.addTableModelListener(e -> {
            int row = e.getFirstRow();
            int col = e.getColumn();


            if (col == 1) {
                Object cantidadObj = tableModel.getValueAt(row, col);
                Object precioObj = tableModel.getValueAt(row, 6);

                int cantidad = 0;
                double precio = 0.0;

                if (cantidadObj instanceof Number) {
                    cantidad = ((Number) cantidadObj).intValue();
                } else if (cantidadObj instanceof String) {
                    try {
                        cantidad = Integer.parseInt((String) cantidadObj);
                    } catch (NumberFormatException ignored) {
                    }

                    tableModel.setValueAt(cantidad, row, 1);
                }

                if (precioObj instanceof Number) {
                    precio = ((Number) precioObj).doubleValue();
                } else if (precioObj instanceof String) {
                    try {
                        precio = Double.parseDouble((String) precioObj);
                    } catch (NumberFormatException ignored) {
                    }
                }

                double subtotal = cantidad * precio;
                tableModel.setValueAt(subtotal, row, 7);

                montoTotal = calcularTotal();
            }
        });

        add(panel);

        pack();
    }

    private void agregarPizzasAModel() {
        tableModel.setRowCount(0);
        for (Pizza pizza : pizzas) {
            Object[] rowData = {
                    pizza.getId(),
                    0,
                    pizza.getNombre(),
                    pizza.getTipoPizza().getNombre(),
                    pizza.getVariedadPizza().getNombre(),
                    pizza.getTamanioPizza().getCantPorciones(),
                    pizza.getPrecio(),
                    0.0
            };
            tableModel.addRow(rowData);
        }

        resizeColumnWidth(table);
        calcularTotal();
    }

    private void createTable() {
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Cant", "Pizza", "Tipo", "Variedad", "Tamaño", "Precio", "SubTotal"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1;  // Solo la columna "Cant" es editable
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 6 || columnIndex == 7) {
                    return Double.class;  // Establece el tipo de columna como Double para "Precio" y "SubTotal"
                }
                return super.getColumnClass(columnIndex);
            }
        };

        table.setModel(tableModel);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Habilita la ordenación predeterminada
        table.setAutoCreateRowSorter(true);

        table.getTableHeader().setReorderingAllowed(false);
    }

    private double calcularTotal() {
        double montoTotal = 0.0;

        for (int row = 0; row < tableModel.getRowCount(); row++) {
            double subtotal = (double) tableModel.getValueAt(row, 7);
            if (subtotal > 0) {
                montoTotal += subtotal;
            }
        }

        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String montStr = formatter.format(montoTotal);
        txtMontoTotal.setText(montStr);

        return montoTotal;
    }

    private void buscarPizzas() {
        pizzas = pizzaDAO.obtenerPizzas(true);
    }

    public void resizeColumnWidth(JTable table) {
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 15; // Min width
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width + 1, width);
            }
            columnModel.getColumn(column).setPreferredWidth(width + 50);
        }
    }

    private void actualizarFormatoTabla() {
        // Oculta la columna "ID"
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setPreferredWidth(0);
    }

    private void agregarPedido() {
        Pedido pedido = new Pedido(txtCliente.getText());

        for (int row = 0; row < tableModel.getRowCount(); row++) {
            int cantidad = (int) tableModel.getValueAt(row, 1);
            if (cantidad > 0) {
                int idPizza = (int) tableModel.getValueAt(row, 0);
                double precioPizza = (double) tableModel.getValueAt(row, 6);

                // Crea un DetallePedido y agrégalo al pedido.
                DetallePedido detalle = new DetallePedido(cantidad, idPizza, precioPizza);
                pedido.agregarDetallePedido(detalle);
            }
        }
        pedido.setProvincia(cmbProvincia.getSelectedItem().toString());
        pedido.setMunicipio(cmbMunicipios.getSelectedItem().toString());

        PedidoDAO pedidoDAO = new PedidoDAO(connection);

        boolean agregado = pedidoDAO.insertarPedido(pedido);

        if (!agregado) {
            JOptionPane.showMessageDialog(AltaPedidoForm.this, "Se produjo un error al crear el pedido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(AltaPedidoForm.this, "Pedido registrado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        exito = true;
        this.dispose();
    }

    private void buscarProvincias() {
        Provincia[] provincias = this.apiGeoRef.obtenerProvincias();
        cmbProvincia.removeAllItems();

        for (Provincia provincia : provincias) cmbProvincia.addItem(provincia);
        this.buscarMunicipios(provincias[0].getId());
    }

    private void buscarMunicipios(String provinciaId) {
        Municipio[] municipios = this.apiGeoRef.obtenerMunicipios(provinciaId);
        cmbMunicipios.removeAllItems();

        for (Municipio municipio : municipios) cmbMunicipios.addItem(municipio);
    }

}