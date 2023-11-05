package com.ebp.trabajointegrador.renders;

import com.ebp.trabajointegrador.modelo.TamanioPizza;
import com.ebp.trabajointegrador.modelo.TipoPizza;
import com.ebp.trabajointegrador.modelo.VariedadPizza;

import javax.swing.*;
import java.awt.*;

public class CustomRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value instanceof TipoPizza) {
            value = ((TipoPizza) value).getNombre();
        }
        if (value instanceof VariedadPizza) {
            value = ((VariedadPizza) value).getNombre();
        }
        if (value instanceof TamanioPizza) {
            value = ((TamanioPizza) value).getNombre();
        }
        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }
}