package com.ebp.trabajointegrador.helpers;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.text.DecimalFormat;

public class DoubleDocumentFilter extends DocumentFilter {
    private static final DecimalFormat decimalFormat = new DecimalFormat("#.0");

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        StringBuilder text = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
        text.insert(offset, string);

        if (isDouble(text.toString())) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        StringBuilder currentText = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
        currentText.replace(offset, offset + length, text);

        if (isDouble(currentText.toString())) {
            super.replace(fb, offset, length, text, attrs);
        }
    }

    private boolean isDouble(String text) {
        try {
            // Intentar convertir el texto a double
            double value = Double.parseDouble(text);
            if (value >= 0) {
                return true;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return false;
    }
}
