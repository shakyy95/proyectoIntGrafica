package com.ebp.trabajointegrador.vista;

import com.ebp.trabajointegrador.accesodatos.UsuarioDAO;
import com.ebp.trabajointegrador.config.SesionUsuario;
import com.ebp.trabajointegrador.modelo.usuario.Permiso;
import com.ebp.trabajointegrador.modelo.usuario.Rol;
import com.ebp.trabajointegrador.modelo.usuario.Usuario;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.List;
import java.util.Objects;

public class LoginForm extends JFrame {
    private JPanel panel1;
    private JComboBox<String> cmbUsuario;
    private JPasswordField txtPassword;
    private JButton btnIniciarSesion;

    private final Connection connection;

    public LoginForm(Connection conn) throws IOException {
        this.connection = conn;

        UsuarioDAO usuarioDAO = new UsuarioDAO(conn);

        // Cargar la imagen del icono
        InputStream iconStream = getClass().getResourceAsStream("/com/ebp/trabajointegrador/resources/logo_pizzeria_112_100.png");
        BufferedImage myImg = null;
        if (iconStream != null) {
            myImg = ImageIO.read(iconStream);
            setIconImage(myImg);
        }

        // Configuración de la ventana de inicio de sesión
        setTitle("Pizzeria - Inicio de Sesión");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Rellenar el ComboBox con usuarios habilitados
        List<String> usuariosHabilitados = usuarioDAO.obtenerUsuariosHabilitados();
        for (String usuario : usuariosHabilitados) {
            cmbUsuario.addItem(usuario);
        }

        add(panel1);

        BufferedImage finalMyImg = myImg;

        // Manejo del evento del botón "Iniciar Sesión"
        btnIniciarSesion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Obtener el nombre de usuario y contraseña
                String nombreUsuario = Objects.requireNonNull(cmbUsuario.getSelectedItem()).toString();
                char[] passwordChars = txtPassword.getPassword();
                String password = new String(passwordChars);

                // Validación: Asegurarse de que se haya ingresado una contraseña
                if (password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Debes ingresar una contraseña.");
                    return;
                }

                // Intentar autenticar al usuario
                Usuario usuarioSesion = usuarioDAO.autenticarUsuario(nombreUsuario, password);

                if (usuarioSesion != null) {
                    // Autenticación exitosa
                    // Obtener el rol y permisos desde la base de datos
                    Rol rol = usuarioDAO.obtenerRolPorId(usuarioSesion.getRolId());
                    List<Permiso> permisos = usuarioDAO.obtenerPermisos(rol.getId());

                    // Actualiza la instancia de UsuarioSesion con el rol y permisos
                    rol.setPermisos(permisos);
                    usuarioSesion.setRol(rol);

                    // Almacena la instancia de UsuarioSesion en la sesión
                    SesionUsuario.getInstancia().setUsuarioSesion(usuarioSesion);

                    // Abrir la ventana principal
                    openMainForm(finalMyImg);
                } else {
                    // Autenticación fallida
                    JOptionPane.showMessageDialog(null, "Inicio de sesión fallido. Verifica tus credenciales.");
                }
            }
        });
    }

    // Método para abrir la ventana principal
    private void openMainForm(Image myImg) {

        SwingUtilities.invokeLater(() -> {
            MainForm mainForm = null;
            mainForm = new MainForm(myImg, connection);

            this.dispose(); // Cerrar la ventana de inicio de sesión actual
            mainForm.setVisible(true);  // Mostrar la ventana principal
        });
    }
}
