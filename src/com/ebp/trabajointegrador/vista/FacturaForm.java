package com.ebp.trabajointegrador.vista;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FacturaForm extends JDialog {
    private JPanel contentPane;
    private JButton btnAceptar;

    public FacturaForm() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(btnAceptar);

        btnAceptar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                onOK();
            }
        });
    }

    private void onOK() {
        // add your code here
        dispose();
    }
}
