package com.ebp.trabajointegrador.helpers;

import javax.swing.*;

public class DoubleInputVerifier extends InputVerifier {
    @Override
    public boolean verify(JComponent input) {
        JTextField textField = (JTextField) input;
        String text = textField.getText().trim();
        try {
            // Intentar convertir el texto a double
            double value = Double.parseDouble(text);
            if (value >= 0) {
                return true;  // La entrada es un número double válido
            }
        } catch (NumberFormatException e) {
            return false; // La entrada no es un número double válido
        }
        return false;
    }
}