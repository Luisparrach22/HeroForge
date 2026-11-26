package es.izquierdofp.heroforge.datos;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
            crearTablas(con);
            if (estanVacias(con)) {
                con.setAutoCommit(false);
                try {
                    insertarDatosPrueba(con);
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

    private static void crearTablas(Connection con) throws SQLException {
        String[] ddl = {
            """
            CREATE TABLE IF NOT EXISTS clase (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL UNIQUE
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS rareza (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL UNIQUE
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS personaje (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL UNIQUE,
                clase_id INTEGER NOT NULL REFERENCES clase(id) ON DELETE RESTRICT,
                nivel INTEGER NOT NULL CHECK (nivel BETWEEN 1 AND 20),
                fuerza INTEGER NOT NULL CHECK (fuerza BETWEEN 1 AND 20),
                agilidad INTEGER NOT NULL CHECK (agilidad BETWEEN 1 AND 20),
                inteligencia INTEGER NOT NULL CHECK (inteligencia BETWEEN 1 AND 20),
                avatar TEXT
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS objeto (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL UNIQUE,
                tipo TEXT NOT NULL CHECK (tipo IN ('Arma','Armadura','Poción','Amuleto')),
                rareza_id INTEGER NOT NULL REFERENCES rareza(id) ON DELETE RESTRICT,
                peso REAL NOT NULL CHECK (peso >= 0),
                bonus_fuerza INTEGER NOT NULL DEFAULT 0,
                bonus_agilidad INTEGER NOT NULL DEFAULT 0,
                bonus_inteligencia INTEGER NOT NULL DEFAULT 0
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS inventario (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                personaje_id INTEGER NOT NULL REFERENCES personaje(id) ON DELETE CASCADE,
                objeto_id INTEGER NOT NULL REFERENCES objeto(id) ON DELETE CASCADE,
                cantidad INTEGER NOT NULL CHECK (cantidad >= 1),
                equipado INTEGER NOT NULL DEFAULT 0 CHECK (equipado IN (0,1)),
                UNIQUE (personaje_id, objeto_id)
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS habilidad (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                personaje_id INTEGER NOT NULL REFERENCES personaje(id) ON DELETE CASCADE,
                nombre TEXT NOT NULL,
                nivel INTEGER NOT NULL CHECK (nivel BETWEEN 1 AND 5),
                descripcion TEXT,
                UNIQUE (personaje_id, nombre)
            )
            """
        };
        try (Statement st = con.createStatement()) {
            for (String sql : ddl) {
                st.execute(sql);
            }
        }
    }

    private static boolean estanVacias(Connection con) throws SQLException {
        String[] tablas = {"clase", "rareza", "personaje", "objeto", "inventario", "habilidad"};
        try (Statement st = con.createStatement()) {
            for (String t : tablas) {
                try (ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM " + t)) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static void insertarDatosPrueba(Connection con) throws SQLException {
        // Catalogos (el orden de insercion fija los ids)
        for (String c : new String[]{"Guerrero", "Mago", "Pícaro", "Clérigo"}) {
            ejecutar(con, "INSERT INTO clase(nombre) VALUES (?)", c);
        }
        for (String r : new String[]{"Común", "Rara", "Épica", "Legendaria"}) {
            ejecutar(con, "INSERT INTO rareza(nombre) VALUES (?)", r);
        }

        // personaje: nombre, clase_id, nivel, fuerza, agilidad, inteligencia, avatar
        Object[][] personajes = {
            {"Thorin", 1, 8, 18, 10, 6, null},
            {"Gandalfo", 2, 10, 8, 9, 19, null},
            {"Sombra", 3, 6, 9, 18, 11, null},
            {"Elara", 4, 7, 12, 10, 16, null},
            {"Brakka", 1, 4, 16, 12, 7, null},
            {"Lyra", 2, 3, 7, 12, 15, null}
        };
        for (Object[] p : personajes) {
            ejecutar(con, "INSERT INTO personaje(nombre, clase_id, nivel, fuerza, agilidad,"
                    + " inteligencia, avatar) VALUES (?,?,?,?,?,?,?)", p);
        }

        // objeto: nombre, tipo, rareza_id, peso, bonus_fuerza, bonus_agilidad, bonus_inteligencia
        Object[][] objetos = {
            {"Espada larga", "Arma", 1, 3.0, 2, 0, 0},                 // 1
            {"Daga de sombra", "Arma", 2, 1.0, 0, 3, 0},               // 2
            {"Bastón arcano", "Arma", 3, 2.5, 0, 0, 4},                // 3
            {"Maza sagrada", "Arma", 2, 4.0, 1, 0, 1},                 // 4
            {"Hacha de guerra", "Arma", 3, 5.0, 4, 0, 0},              // 5
            {"Cota de malla", "Armadura", 1, 10.0, 1, 0, 0},           // 6
            {"Túnica de mago", "Armadura", 2, 2.0, 0, 0, 2},           // 7
            {"Coraza de dragón", "Armadura", 4, 18.0, 3, 0, 0},        // 8
            {"Poción de vida", "Poción", 1, 0.5, 0, 0, 0},             // 9
            {"Poción de maná", "Poción", 1, 0.5, 0, 0, 1},             // 10
            {"Amuleto de la suerte", "Amuleto", 2, 0.2, 0, 2, 0},      // 11
            {"Amuleto del archimago", "Amuleto", 4, 0.1, 0, 0, 5}      // 12
        };
        for (Object[] o : objetos) {
            ejecutar(con, "INSERT INTO objeto(nombre, tipo, rareza_id, peso, bonus_fuerza,"
                    + " bonus_agilidad, bonus_inteligencia) VALUES (?,?,?,?,?,?,?)", o);
        }

        // inventario: personaje_id, objeto_id, cantidad, equipado
        // Respeta peso <= fuerza*5 y como maximo 1 arma y 1 armadura equipadas.
        int[][] inventario = {
            {1, 1, 1, 1}, {1, 8, 1, 1}, {1, 9, 3, 0},
            {2, 3, 1, 1}, {2, 7, 1, 1}, {2, 10, 4, 0}, {2, 12, 1, 1},
            {3, 2, 1, 1}, {3, 11, 1, 1}, {3, 9, 2, 0},
            {4, 4, 1, 1}, {4, 6, 1, 1}, {4, 9, 2, 0},
            {5, 5, 1, 1}, {5, 6, 1, 0},
            {6, 3, 1, 0}, {6, 10, 2, 0}
        };
        for (int[] i : inventario) {
            ejecutar(con, "INSERT INTO inventario(personaje_id, objeto_id, cantidad, equipado)"
                    + " VALUES (?,?,?,?)", i[0], i[1], i[2], i[3]);
        }

        // habilidad: personaje_id, nombre, nivel, descripcion (n.º <= nivel del personaje)
        Object[][] habilidades = {
            {1, "Golpe poderoso", 3, "Ataque cuerpo a cuerpo con daño extra"},
            {1, "Grito de guerra", 2, "Mejora la moral del grupo"},
            {1, "Escudo férreo", 4, "Reduce el daño recibido"},
            {2, "Bola de fuego", 5, "Daño de área a distancia"},
            {2, "Escudo arcano", 3, "Barrera mágica temporal"},
            {3, "Ataque furtivo", 4, "Daño crítico desde las sombras"},
            {3, "Abrir cerraduras", 2, "Abre cerrojos y trampas"},
            {4, "Curación", 4, "Restaura puntos de vida"},
            {4, "Bendición", 2, "Mejora las tiradas aliadas"},
            {4, "Expulsar no-muertos", 3, "Aleja a los muertos vivientes"},
            {4, "Luz sagrada", 1, "Ilumina y daña a lo impío"},
            {5, "Carga", 2, "Embestida contra un enemigo"},
            {5, "Resistencia", 1, "Aguanta más golpes"},
            {6, "Rayo de hielo", 2, "Ralentiza al objetivo"}
        };
        for (Object[] h : habilidades) {
            ejecutar(con, "INSERT INTO habilidad(personaje_id, nombre, nivel, descripcion)"
                    + " VALUES (?,?,?,?)", h);
        }
    }

    private static void ejecutar(Connection con, String sql, Object... params) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            ps.executeUpdate();
        }
    }
}
