package es.izquierdofp.heroforge.datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Datos de prueba que se cargan en el primer arranque.
 */
final class DatosPrueba {

    private DatosPrueba() {
    }

    /** Inserta el juego completo de datos de prueba. */
    static void insertar(Connection con) throws SQLException {
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

    static void ejecutar(Connection con, String sql, Object... params) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            ps.executeUpdate();
        }
    }
}
