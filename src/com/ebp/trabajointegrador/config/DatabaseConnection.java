package com.ebp.trabajointegrador.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    public static Connection obtenerConexionBaseDatos(Properties properties) {
        Connection conn = null;
        String strCnn = properties.getProperty("strCnn");
        try {
            // Establece la conexión a la base de datos
            conn = DriverManager.getConnection(strCnn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void cerrarConexionBaseDatos(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
