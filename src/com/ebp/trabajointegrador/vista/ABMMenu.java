package com.ebp.trabajointegrador.vista;

import com.ebp.trabajointegrador.accesodatos.PizzaDAO;
import com.ebp.trabajointegrador.accesodatos.TamanioPizzaDAO;
import com.ebp.trabajointegrador.accesodatos.TipoPizzaDAO;
import com.ebp.trabajointegrador.accesodatos.VariedadPizzaDAO;
import com.ebp.trabajointegrador.helpers.DoubleDocumentFilter;
import com.ebp.trabajointegrador.helpers.DoubleInputVerifier;
import com.ebp.trabajointegrador.modelo.Pizza;
import com.ebp.trabajointegrador.modelo.TamanioPizza;
import com.ebp.trabajointegrador.modelo.TipoPizza;
import com.ebp.trabajointegrador.modelo.VariedadPizza;
import com.ebp.trabajointegrador.renders.CustomRenderer;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.List;

public class ABMMenu extends JDialog {
    private JPanel panel;
    private JTextField txtNombre;
    private JTextField txtPrecio;
    private JComboBox<VariedadPizza> cmbVariedad;
    private JComboBox<TipoPizza> cmbTipo;
    private JComboBox<TamanioPizza> cmbTamanio;
    private JCheckBox chkHabilitado;
    private JButton btnGuardar;

    private TipoPizzaDAO tipoPizzaDAO;
    private TamanioPizzaDAO tamanioPizzaDAO;

    private VariedadPizzaDAO variedadPizzaDAO;

    private PizzaDAO pizzaDAO;
    private int pizzaId = 0;

    public ABMMenu(Frame owner, Connection conn) {
        super(owner, "ABM de Menú", true);
        this.configurarForm(conn, "Agregar pizza");
    }

    public ABMMenu(Frame owner, Connection conn, Pizza pizza) {
        super(owner, "ABM de Menú", true);
        this.configurarForm(conn, "Editar pizza");
        cargarDatosPizza(pizza);
    }

    private void configurarForm(Connection conn, String titulo) {

        pizzaDAO = new PizzaDAO(conn);

        setTitle(titulo);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        tipoPizzaDAO = new TipoPizzaDAO(conn);
        variedadPizzaDAO = new VariedadPizzaDAO(conn);
        tamanioPizzaDAO = new TamanioPizzaDAO(conn);

        this.obtenerVariedades();
        this.obtenerTipos();
        this.obtenerTamanios();

        txtPrecio.setInputVerifier(new DoubleInputVerifier());
        ((AbstractDocument) txtPrecio.getDocument()).setDocumentFilter(new DoubleDocumentFilter());

        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarPizza();
            }
        });

        add(panel);
    }

    private void obtenerVariedades() {
        List<VariedadPizza> variedadesPizzas = variedadPizzaDAO.obtenerVariedadesPizzas();
        DefaultComboBoxModel<VariedadPizza> model = new DefaultComboBoxModel<>(variedadesPizzas.toArray(new VariedadPizza[0]));
        cmbVariedad.setModel(model);
        cmbVariedad.setRenderer(new CustomRenderer());
    }

    private void obtenerTipos() {
        List<TipoPizza> tiposPizzas = tipoPizzaDAO.obtenerTiposPizzas();
        DefaultComboBoxModel<TipoPizza> model = new DefaultComboBoxModel<>(tiposPizzas.toArray(new TipoPizza[0]));
        cmbTipo.setModel(model);
        cmbTipo.setRenderer(new CustomRenderer());
    }

    private void obtenerTamanios() {
        List<TamanioPizza> tamanioPizzas = tamanioPizzaDAO.obtenerTamaniosPizzas();
        DefaultComboBoxModel<TamanioPizza> model = new DefaultComboBoxModel<>(tamanioPizzas.toArray(new TamanioPizza[0]));
        cmbTamanio.setModel(model);
        cmbTamanio.setRenderer(new CustomRenderer());
    }

    private void cargarDatosPizza(Pizza pizza) {
        this.pizzaId = pizza.getId();

        // Cargar datos en los campos del formulario
        txtNombre.setText(pizza.getNombre());
        txtPrecio.setText(String.valueOf(pizza.getPrecio()));

        // Seleccionar la variedadPizza en el ComboBox
        for (int i = 0; i < cmbVariedad.getItemCount(); i++) {
            VariedadPizza variedadPizza = (VariedadPizza) cmbVariedad.getItemAt(i);
            if (variedadPizza.getId() == pizza.getVariedadPizza().getId()) {
                cmbVariedad.setSelectedIndex(i);
                break;
            }
        }

        // Seleccionar el tipoPizza en el ComboBox
        for (int i = 0; i < cmbTipo.getItemCount(); i++) {
            TipoPizza tipoPizza = (TipoPizza) cmbTipo.getItemAt(i);
            if (tipoPizza.getId() == pizza.getTipoPizza().getId()) {
                cmbTipo.setSelectedIndex(i);
                break;
            }
        }

        // Seleccionar el tamanioPizza en el ComboBox
        for (int i = 0; i < cmbTamanio.getItemCount(); i++) {
            TamanioPizza tamanioPizza = (TamanioPizza) cmbTamanio.getItemAt(i);
            if (tamanioPizza.getId() == pizza.getTamanioPizza().getId()) {
                cmbTamanio.setSelectedIndex(i);
                break;
            }
        }

        chkHabilitado.setSelected(pizza.isHabilitado());

    }

    private void guardarPizza() {
        // Obtener los datos ingresados por el usuario
        String nombre = txtNombre.getText();
        double precio = Double.parseDouble(txtPrecio.getText());
        Pizza pizza = getPizza(nombre, precio);

        if (pizzaId == 0) {
            // Si pizzaId es 0, es una nueva pizza, así que insertamos
            boolean exito = pizzaDAO.insertarPizza(pizza);
            if (exito) {
                JOptionPane.showMessageDialog(this, "Pizza guardada con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar la pizza", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // Si pizzaId es diferente de 0, es una edición, así que actualizamos
            boolean exito = pizzaDAO.actualizarPizza(pizza);
            if (exito) {
                JOptionPane.showMessageDialog(this, "Pizza actualizada con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar la pizza", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private Pizza getPizza(String nombre, double precio) {
        VariedadPizza variedadPizza = (VariedadPizza) cmbVariedad.getSelectedItem();
        TipoPizza tipoPizza = (TipoPizza) cmbTipo.getSelectedItem();
        TamanioPizza tamanioPizza = (TamanioPizza) cmbTamanio.getSelectedItem();
        boolean habilitado = chkHabilitado.isSelected();

        Pizza pizza = new Pizza();
        pizza.setId(pizzaId);

        // Establecer los atributos de la pizza
        pizza.setNombre(nombre);
        pizza.setPrecio(precio);
        pizza.setVariedadPizza(variedadPizza);
        pizza.setTipoPizza(tipoPizza);
        pizza.setTamanioPizza(tamanioPizza);
        pizza.setHabilitado(habilitado);
        return pizza;
    }

}
