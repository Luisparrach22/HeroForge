package es.izquierdofp.heroforge.datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Define el esquema de la base de datos (las 6 tablas).
 */
final class EsquemaBD {

    private EsquemaBD() {
    }

    /** Crea las tablas que no existan. */
    static void crear(Connection con) throws SQLException {
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

    /** Indica si todas las tablas estan vacias. */
    static boolean estanVacias(Connection con) throws SQLException {
        String[] tablas = {"clase", "rareza", "personaje", "objeto", "inventario", "habilidad"};
        try (Statement st = con.createStatement()) {
            for (String t : tablas) {
                ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM " + t);
                if (rs.getInt(1) > 0) {
                    return false;
                }
            }
        }
        return true;
    }
}
