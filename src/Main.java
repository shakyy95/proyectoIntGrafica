import com.ebp.trabajointegrador.config.DatabaseConnection;
import com.ebp.trabajointegrador.vista.LoginForm;

import javax.swing.*;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

public class Main {

    public static void main(String[] args) throws IOException {

        Properties properties = new Properties();
        properties.load(new FileReader("src/com/ebp/trabajointegrador/resources/config.properties"));

        //Creamos una conexion a la base de datos que será compartida por toda la app
        Connection conn = DatabaseConnection.obtenerConexionBaseDatos(properties);


        //Como primera medida abrimos el login
        SwingUtilities.invokeLater(() -> {
            LoginForm loginForm = null;
            try {
                loginForm = new LoginForm(conn);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            loginForm.setVisible(true);
        });
    }
}