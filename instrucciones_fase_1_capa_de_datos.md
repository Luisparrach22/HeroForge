# Contexto y Objetivo
Actúa como un desarrollador Senior de Java. Tu objetivo es implementar **únicamente la Primera Parte (Fase 1: Arquitectura base, Modelos, Base de Datos SQLite y Capa de Datos DAO)** de una aplicación de escritorio en Java.

**IMPORTANTE:** No generes todavía la interfaz gráfica (Swing/JFrame/JDialog), ni el empaquetado (JAR/jpackage), ni la documentación final. Céntrate exclusivamente en que la base de datos, los modelos y los DAOs sean 100% funcionales, limpios y cumplan estrictamente con el contrato técnico especificado más abajo.

---

# Stack Tecnológico y Entorno
* **Lenguaje:** JDK 21
* **IDE / Estructura:** Proyecto Apache NetBeans (`Java with Ant`)
* **Base de datos:** SQLite local mediante `sqlite-jdbc` (ubicado en `lib/`)
* **Estilos futuros (solo referencia):** FlatLaf 3.7.2

---

# Temáticas Disponibles
*(Nota para el usuario: Antes de ejecutar, indica cuál de estas 4 temáticas has elegido y adjunta el Anexo de esa temática con las columnas exactas)*:
1. **TourneyManager** (`tourneymanager`): `juego`, `rol` · `equipo`, `torneo` · `inscripcion` · `jugador`
2. **RetroVid** (`retrovid`): `formato`, `genero` · `socio`, `pelicula` · `alquiler` · `valoracion`
3. **BandManager** (`bandmanager`): `genero`, `instrumento` · `banda`, `concierto` · `actuacion` · `musico`
4. **HeroForge** (`heroforge`): `clase`, `rareza` · `personaje`, `objeto` · `inventario` · `habilidad`

---

# Contrato Estricto del Código (Nombres Obligatorios)
La capa de datos se validará mediante pruebas unitarias automatizadas, por lo que **los nombres de paquetes, clases y firmas de métodos deben respetarse al pie de la letra**.

## 1. Estructura de Paquetes
Paquete raíz: `es.izquierdofp.<tematica_en_minusculas>`
* `es.izquierdofp.<tema>.Main` (Clase principal de entrada)
* `es.izquierdofp.<tema>.modelo` (Clases POJO de las 6 tablas)
* `es.izquierdofp.<tema>.datos` (`DatabaseManager`, los 6 DAOs correspondientes a las tablas y `EstadisticasDAO`)
* `es.izquierdofp.<tema>.vista` *(Dejar vacío o pendiente para la Fase 2)*

## 2. Clases del Modelo (`modelo/`)
* Crear una clase por cada una de las 6 tablas (en singular y PascalCase, ej: `Equipo`, `Inscripcion`).
* Un atributo `private` por cada columna de la tabla, nombrado en `camelCase` (ej: `equipo_id` $\rightarrow$ `equipoId`).
* **Tipos de datos:**
  * Fechas: `String` con formato estricto `AAAA-MM-DD`.
  * Columnas booleanas (0/1 en SQLite): `boolean` en Java.
* Incluir constructor vacío, constructor con parámetros (opcional/útil), getters y setters estándar (usando `isX()` para atributos `boolean`).

## 3. Gestión de Base de Datos (`DatabaseManager.java`)
Debe implementar exactamente estos métodos estáticos:
* `public static void setRutaBD(String ruta)`: Cambia la ruta del archivo `.db` (necesario para tests). Por defecto debe apuntar a `<user.home>/.<tematica>/<tematica>.db`.
* `public static Connection getConnection() throws SQLException`: Abre y devuelve la conexión a SQLite ejecutando obligatoriamente `PRAGMA foreign_keys = ON;` en cada conexión.
* `public static void inicializar()`:
  1. Crea el directorio padre si no existe.
  2. Crea las 6 tablas exactamente según el esquema (PK, FK con `ON DELETE CASCADE` o `RESTRICT`, `UNIQUE` y `CHECK`).
  3. Comprueba si las tablas están vacías; si lo están, inserta un juego completo de datos de prueba coherentes que cubran todos los casos de uso y reglas de negocio.

## 4. Clases DAO (`datos/`)
Cada una de las 6 tablas tendrá su clase `<Entidad>DAO` con los siguientes métodos obligatorios (siendo `X` la clase del modelo):
* `public List<X> listar()`: Devuelve todos los registros ordenados según el criterio de la entidad.
* `public List<X> buscar(String texto)`: Búsqueda insensible a mayúsculas (`LOWER` / `LIKE`) en los campos principales de texto. *(Opcional en DAOs de catálogos y relación, obligatorio en entidades principales y detalle)*.
* `public X obtener(int id)`: Devuelve el objeto con ese `id`, o `null` si no existe.
* `public int insertar(X x)`: Inserta el registro, asigna el ID autogenerado al objeto `x` y devuelve dicho ID. Devuelve `-1` si falla o si se incumple alguna regla de negocio.
* `public boolean actualizar(X x)`: Modifica la fila correspondiente al `id` de `x`. Devuelve `true` si tuvo éxito, `false` si falla o incumple una regla.
* `public boolean eliminar(int id)`: Elimina la fila por `id`. Debe respetar el borrado en cascada donde corresponda y devolver `false` (sin lanzar excepción no controlada) si se intenta borrar un valor de catálogo que está en uso (`RESTRICT`).
* `public int contar()`: Devuelve el número total de registros en la tabla.

**Métodos adicionales obligatorios:**
* **DAOs de Relación y Detalle:** Implementar los métodos específicos de filtrado por clave ajena (ej. `listarPorEquipo(int equipoId)`, `listarPorTorneo(int torneoId)`, etc.).
* **`EstadisticasDAO`:** Implementar todos los métodos de consulta agregada y reglas de negocio específicas de la temática.

---

# Instrucciones de Ejecución para ti
1. **Espera mi confirmación** de la temática elegida y los detalles del anexo de tablas (si aún no te los he pasado en este mensaje).
2. Genera primero `DatabaseManager.java` y todas las clases del paquete `modelo`.
3. Genera a continuación los 6 DAOs y `EstadisticasDAO`, asegurándote de manejar correctamente las excepciones `SQLException` para devolver `-1` o `false` según pide el contrato.
4. Crea un `Main.java` temporal que invoque a `DatabaseManager.inicializar()` y ejecute una batería rápida de pruebas por consola (CRUD, restricción de catálogos, cascadas y reglas de negocio) para verificar que toda la Fase 1 compila y funciona antes de pasar a la interfaz gráfica.