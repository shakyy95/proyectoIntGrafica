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

        Connection conn = DatabaseConnection.obtenerConexionBaseDatos(properties);

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