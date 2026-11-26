package es.izquierdofp.heroforge.datos;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static String rutaBD = System.getProperty("user.home")
            + File.separator + ".heroforge" + File.separator + "heroforge.db";

    public static void setRutaBD(String ruta) {
        rutaBD = ruta;
    }

    public static Connection getConnection() throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:sqlite:" + rutaBD);
        try (Statement st = con.createStatement()) {
            st.execute("PRAGMA foreign_keys = ON;");
        }
        return con;
    }

    public static void inicializar() {
        new File(rutaBD).getAbsoluteFile().getParentFile().mkdirs();
        try (Connection con = getConnection()) {
            EsquemaBD.crear(con);
            if (EsquemaBD.estanVacias(con)) {
                DatosPrueba.insertar(con);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
