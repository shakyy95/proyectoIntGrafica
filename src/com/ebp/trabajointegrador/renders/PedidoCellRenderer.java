package com.ebp.trabajointegrador.renders;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class PedidoCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Obtén el valor de la columna Estado
        Object estadoValue = table.getValueAt(row, 7);

        // Asigna un color de fondo diferente según el estado
        if (estadoValue != null) {
            String estado = estadoValue.toString();
            if (estado.equals("REGISTRADO")) {
                cellComponent.setBackground(Color.WHITE);
            } else if (estado.equals("PREPARACION")) {
                cellComponent.setBackground(Color.YELLOW);
            } else if (estado.equals("LISTO_PARA_ENTREGAR")) {
                cellComponent.setBackground(Color.ORANGE);
            } else if (estado.equals("ENTREGADO")) {
                cellComponent.setBackground(Color.BLUE);
            } else if (estado.equals("CANCELADO")) {
                cellComponent.setBackground(Color.GRAY);
            } else {
                // Otro estado, usa un color predeterminado
                cellComponent.setBackground(Color.WHITE);
            }
        }

        return cellComponent;
    }
}