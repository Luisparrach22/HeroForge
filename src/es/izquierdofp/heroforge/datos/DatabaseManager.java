package es.izquierdofp.heroforge.datos;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Gestiona la conexion a SQLite, la creacion del esquema y los datos de prueba.
 */
public class DatabaseManager {

    private static String rutaBD = rutaPorDefecto();

    private DatabaseManager() {
    }

    private static String rutaPorDefecto() {
        return System.getProperty("user.home") + File.separator + ".heroforge"
                + File.separator + "heroforge.db";
    }

    /** Cambia la ruta del archivo .db (la usan las pruebas). */
    public static void setRutaBD(String ruta) {
        rutaBD = ruta;
    }

    /** Abre una conexion con las claves ajenas activadas. */
    public static Connection getConnection() throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:sqlite:" + rutaBD);
        try (Statement st = con.createStatement()) {
            st.execute("PRAGMA foreign_keys = ON;");
        } catch (SQLException e) {
            con.close();
            throw e;
        }
        return con;
    }

    /** Crea la carpeta y las tablas e inserta datos de prueba si estan vacias. */
    public static void inicializar() {
        File padre = new File(rutaBD).getAbsoluteFile().getParentFile();
        if (padre != null && !padre.exists() && !padre.mkdirs()) {
            throw new IllegalStateException("No se pudo crear el directorio " + padre);
        }
        try (Connection con = getConnection()) {
            EsquemaBD.crear(con);
            if (EsquemaBD.estanVacias(con)) {
                con.setAutoCommit(false);
                try {
                    DatosPrueba.insertar(con);
                    con.commit();
                } catch (SQLException e) {
                    con.rollback();
                    throw e;
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error al inicializar la base de datos", e);
        }
    }
}
