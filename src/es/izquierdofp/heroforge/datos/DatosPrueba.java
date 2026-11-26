package es.izquierdofp.heroforge.datos;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

final class DatosPrueba {

    private DatosPrueba() {
    }

    static void insertar(Connection con) throws SQLException {
        try (Statement st = con.createStatement()) {
            st.execute("""
                INSERT INTO clase (nombre) VALUES
                    ('Guerrero'), ('Mago'), ('Pícaro'), ('Clérigo');
                """);

            st.execute("""
                INSERT INTO rareza (nombre) VALUES
                    ('Común'), ('Rara'), ('Épica'), ('Legendaria');
                """);

            st.execute("""
                INSERT INTO personaje (nombre, clase_id, nivel, fuerza, agilidad, inteligencia) VALUES
                    ('Thorin', 1, 8, 18, 10, 6),
                    ('Gandalfo', 2, 10, 8, 9, 19),
                    ('Sombra', 3, 6, 9, 18, 11),
                    ('Elara', 4, 7, 12, 10, 16),
                    ('Brakka', 1, 4, 16, 12, 7),
                    ('Lyra', 2, 3, 7, 12, 15);
                """);

            st.execute("""
                INSERT INTO objeto (nombre, tipo, rareza_id, peso, bonus_fuerza, bonus_agilidad, bonus_inteligencia) VALUES
                    ('Espada larga', 'Arma', 1, 3.0, 2, 0, 0),
                    ('Daga de sombra', 'Arma', 2, 1.0, 0, 3, 0),
                    ('Bastón arcano', 'Arma', 3, 2.5, 0, 0, 4),
                    ('Maza sagrada', 'Arma', 2, 4.0, 1, 0, 1),
                    ('Hacha de guerra', 'Arma', 3, 5.0, 4, 0, 0),
                    ('Cota de malla', 'Armadura', 1, 10.0, 1, 0, 0),
                    ('Túnica de mago', 'Armadura', 2, 2.0, 0, 0, 2),
                    ('Coraza de dragón', 'Armadura', 4, 18.0, 3, 0, 0),
                    ('Poción de vida', 'Poción', 1, 0.5, 0, 0, 0),
                    ('Poción de maná', 'Poción', 1, 0.5, 0, 0, 1),
                    ('Amuleto de la suerte', 'Amuleto', 2, 0.2, 0, 2, 0),
                    ('Amuleto del archimago', 'Amuleto', 4, 0.1, 0, 0, 5);
                """);

            st.execute("""
                INSERT INTO inventario (personaje_id, objeto_id, cantidad, equipado) VALUES
                    (1, 1, 1, 1), (1, 8, 1, 1), (1, 9, 3, 0),
                    (2, 3, 1, 1), (2, 7, 1, 1), (2, 10, 4, 0), (2, 12, 1, 1),
                    (3, 2, 1, 1), (3, 11, 1, 1), (3, 9, 2, 0),
                    (4, 4, 1, 1), (4, 6, 1, 1), (4, 9, 2, 0),
                    (5, 5, 1, 1), (5, 6, 1, 0),
                    (6, 3, 1, 0), (6, 10, 2, 0);
                """);

            st.execute("""
                INSERT INTO habilidad (personaje_id, nombre, nivel, descripcion) VALUES
                    (1, 'Golpe poderoso', 3, 'Ataque cuerpo a cuerpo con daño extra'),
                    (1, 'Grito de guerra', 2, 'Mejora la moral del grupo'),
                    (1, 'Escudo férreo', 4, 'Reduce el daño recibido'),
                    (2, 'Bola de fuego', 5, 'Daño de área a distancia'),
                    (2, 'Escudo arcano', 3, 'Barrera mágica temporal'),
                    (3, 'Ataque furtivo', 4, 'Daño crítico desde las sombras'),
                    (3, 'Abrir cerraduras', 2, 'Abre cerrojos y trampas'),
                    (4, 'Curación', 4, 'Restaura puntos de vida'),
                    (4, 'Bendición', 2, 'Mejora las tiradas aliadas'),
                    (4, 'Expulsar no-muertos', 3, 'Aleja a los muertos vivientes'),
                    (4, 'Luz sagrada', 1, 'Ilumina y daña a lo impío'),
                    (5, 'Carga', 2, 'Embestida contra un enemigo'),
                    (5, 'Resistencia', 1, 'Aguanta más golpes'),
                    (6, 'Rayo de hielo', 2, 'Ralentiza al objetivo');
                """);
        }
    }
}
