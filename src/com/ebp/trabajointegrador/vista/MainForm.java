package com.ebp.trabajointegrador.vista;

import com.ebp.trabajointegrador.config.SesionUsuario;
import com.ebp.trabajointegrador.modelo.usuario.Permiso;
import com.ebp.trabajointegrador.modelo.usuario.Rol;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class MainForm extends JFrame {
    private JPanel panel1;

    class MenuListener implements ActionListener {
        private final Connection connection;

        public MenuListener(Connection conn) {
            this.connection = conn;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            switch (e.getActionCommand()) {
                case "REGISTRAR_MENU":
                    SwingUtilities.invokeLater(() -> {
                        MenuForm menuForm = null;
                        menuForm = new MenuForm(connection);
                        menuForm.setVisible(true);
                    });
                    break;
                case "VENTAS_PEDIDOS":
                    SwingUtilities.invokeLater(() -> {
                        VentasForm ventasForm = null;
                        ventasForm = new VentasForm(connection);
                        ventasForm.setLocationRelativeTo(MainForm.this);
                        ventasForm.setVisible(true);
                    });
                    break;
                case "CERRAR_SESION":
                    int confirm = JOptionPane.showConfirmDialog(
                            MainForm.this,
                            "¿Está seguro que desea cerrar la sesión?",
                            "Confirmación",
                            JOptionPane.YES_NO_OPTION
                    );

                    if (confirm == JOptionPane.YES_OPTION) {
                        SwingUtilities.invokeLater(() -> {
                            try {
                                LoginForm loginForm = new LoginForm(connection);
                                loginForm.setVisible(true);
                                dispose();
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        });
                    }
                    break;
            }

        }
    }


    private final JMenuBar menuBar = new JMenuBar();

    private final MenuListener menuListener;

    public MainForm(Image icon, Connection conn) {

        this.menuListener = new MenuListener(conn);
        try {

            setTitle("Pizzeria - Menú Principal");
            setIconImage(icon);
            setSize(800, 600);

            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setResizable(true);

            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            setJMenuBar(menuBar);

            // Obtener el rol y permisos del usuario en sesión
            SesionUsuario usuarioSesion = SesionUsuario.getInstancia();
            Rol rol = usuarioSesion.getUsuarioSesion().getRol();
            List<Permiso> permisos = rol.getPermisos();

            agregarMenus(permisos);
            add(panel1);

        } catch (Exception e) {

        }
    }

    private void agregarMenus(List<Permiso> permisos) {

        if (permisos.stream().anyMatch(permiso -> permiso.getId() == 1 || permiso.getId() == 2 || permiso.getId() == 3)) {
            JMenu menu = new JMenu("Ventas");
            menuBar.add(menu);

            JMenuItem menuItemPedido = new JMenuItem("Pedidos");
            menuItemPedido.setActionCommand("VENTAS_PEDIDOS");
            menuItemPedido.addActionListener(menuListener);
            menu.add(menuItemPedido);

         /*   if (permisos.stream().anyMatch(permiso -> permiso.getId() == 1)) {
                JMenuItem menuItem = new JMenuItem("Generar factura");
                menuItem.setActionCommand("GENERAR_FACTURA");
                menuItem.addActionListener(menuListener);
                menu.add(menuItem);
            }

            if (permisos.stream().anyMatch(permiso -> permiso.getId() == 2)) {
                JMenuItem menuItem = new JMenuItem("Registrar pedido");
                menuItem.setActionCommand("REGISTRAR_PEDIDO");
                menuItem.addActionListener(menuListener);
                menu.add(menuItem);
            }

            if (permisos.stream().anyMatch(permiso -> permiso.getId() == 3)) {
                JMenuItem menuItem = new JMenuItem("Cancelar pedido");
                menuItem.setActionCommand("CANCELAR_PEDIDO");
                menuItem.addActionListener(menuListener);
                menu.add(menuItem);
            }


          */

        }

        if (permisos.stream().anyMatch(permiso -> permiso.getId() == 4 || permiso.getId() == 5)) {
            JMenu menu = new JMenu("Cocina");
            menuBar.add(menu);

            if (permisos.stream().anyMatch(permiso -> permiso.getId() == 4)) {
                JMenuItem menuItem = new JMenuItem("Confirmar pedido");
                menuItem.setActionCommand("CONFIRMAR_PEDIDO");
                menuItem.addActionListener(menuListener);
                menu.add(menuItem);
            }

            if (permisos.stream().anyMatch(permiso -> permiso.getId() == 5)) {
                JMenuItem menuItem = new JMenuItem("Informar cierre pedido");
                menuItem.setActionCommand("CERRAR_PEDIDO");
                menuItem.addActionListener(menuListener);
                menu.add(menuItem);
            }
        }

        if (permisos.stream().anyMatch(permiso -> permiso.getId() == 6 || permiso.getId() == 7)) {
            JMenu menu = new JMenu("Administración");
            menuBar.add(menu);

            if (permisos.stream().anyMatch(permiso -> permiso.getId() == 6)) {
                JMenuItem menuItem = new JMenuItem("Gestionar menú");
                menuItem.setActionCommand("REGISTRAR_MENU");
                menuItem.addActionListener(menuListener);
                menu.add(menuItem);
            }
        }

        if (permisos.stream().anyMatch(permiso -> permiso.getId() == 8)) {
            JMenu menu = new JMenu("Estadísticas");
            menuBar.add(menu);

            JMenuItem menuItem = new JMenuItem("Ver estadísticas");
            menuItem.setActionCommand("VER_ESTADISTICAS");
            menuItem.addActionListener(menuListener);
            menu.add(menuItem);
        }

        menuBar.add(Box.createHorizontalGlue()); // Espaciado horizontal elástico para empujar a la derecha

        JMenu menu = new JMenu(SesionUsuario.getInstancia().getUsuarioSesion().getNombre());
        menuBar.add(menu);

        JMenuItem menuItemLogout = new JMenuItem("Cerrar sesión");
        menuItemLogout.setActionCommand("CERRAR_SESION");
        menuItemLogout.addActionListener(menuListener);
        menu.add(menuItemLogout);
    }
}