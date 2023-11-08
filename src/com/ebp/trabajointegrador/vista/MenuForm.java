package com.ebp.trabajointegrador.vista;

import com.ebp.trabajointegrador.accesodatos.PizzaDAO;
import com.ebp.trabajointegrador.modelo.Pizza;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.util.List;

public class MenuForm extends JFrame {
    private JPanel panel1;
    private JTable tablaPizzas;
    private JScrollPane scrollPane;
    private JButton btnAgregarPizza;
    private final Connection connection;
    private final PizzaDAO pizzaDAO;

    // Listener para manejar el evento de cierre del formulario de edición/agregación de pizza
    private final WindowAdapter listenerCloseABM = new WindowAdapter() {
        @Override
        public void windowClosed(WindowEvent e) {
            llenarTablaConPizzas();
        }
    };

    public MenuForm(Connection conn) {
        this.connection = conn;
        this.pizzaDAO = new PizzaDAO(conn);

        setTitle("Menú de Pizzas");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        llenarTablaConPizzas();

        btnAgregarPizza.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Abrir el formulario de edición/agregación de pizza (ABMMenu)
                SwingUtilities.invokeLater(() -> {
                    ABMMenu abmMenu = new ABMMenu(MenuForm.this, conn);
                    abmMenu.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
                    abmMenu.pack();
                    abmMenu.setLocationRelativeTo(MenuForm.this);
                    abmMenu.addWindowListener(listenerCloseABM);
                    abmMenu.setVisible(true);
                });
            }
        });

        add(panel1);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private static DefaultTableModel getDefaultTableModel() {
        // Configura un modelo de tabla predeterminado con una columna "ID" oculta
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        model.addColumn("ID"); // Columna "id" oculta
        model.addColumn("Nombre");
        model.addColumn("Precio");
        model.addColumn("Tipo");
        model.addColumn("Variedad");
        model.addColumn("Tamaño");
        model.addColumn("Editar");
        return model;
    }

    private void llenarTablaConPizzas() {
        DefaultTableModel model = (DefaultTableModel) tablaPizzas.getModel();
        model.setRowCount(0);
        List<Pizza> pizzas = pizzaDAO.obtenerPizzas();

        for (Pizza pizza : pizzas) {
            model.addRow(new Object[]{
                    pizza.getId(),
                    pizza.getNombre(),
                    pizza.getPrecio(),
                    pizza.getTipoPizza().getNombre(),
                    pizza.getVariedadPizza().getNombre(),
                    pizza.getTamanioPizza().getNombre(),
                    "Editar"
            });
        }
    }

    private void createUIComponents() {
        // Configura la tabla de pizzas y agrega un botón "Editar" en las filas
        DefaultTableModel model = getDefaultTableModel();
        tablaPizzas = new JTable(model) {
            @Override
            public TableCellRenderer getCellRenderer(int row, int column) {
                if (column == 6) {
                    return new ButtonRenderer();
                }
                return super.getCellRenderer(row, column);
            }
        };

        // Configurar la columna "ID" como invisible
        TableColumn idColumn = tablaPizzas.getColumnModel().getColumn(0);
        idColumn.setMinWidth(0);
        idColumn.setMaxWidth(0);
        idColumn.setPreferredWidth(0);
        idColumn.setResizable(false);

        tablaPizzas.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int column = tablaPizzas.getColumnModel().getColumnIndexAtX(e.getX());
                int row = e.getY() / tablaPizzas.getRowHeight();

                if (row < tablaPizzas.getRowCount() && row >= 0 && column == 6) {
                    // Manejar el evento de clic en el botón "Editar"
                    int pizzaId = (int) tablaPizzas.getValueAt(row, 0);
                    Pizza pizza = pizzaDAO.obtenerPizzaPorId(pizzaId);

                    // Abre el formulario de edición/agregación de pizza (ABMMenu) con los datos de la pizza
                    SwingUtilities.invokeLater(() -> {
                        ABMMenu abmMenu = new ABMMenu(MenuForm.this, connection, pizza);
                        abmMenu.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
                        abmMenu.pack();
                        abmMenu.setLocationRelativeTo(MenuForm.this);
                        abmMenu.addWindowListener(listenerCloseABM);
                        abmMenu.setVisible(true);
                    });
                }
            }
        });
    }

    static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(UIManager.getColor("Button.background"));
            }
            setText("Editar");
            return this;
        }
    }
}


