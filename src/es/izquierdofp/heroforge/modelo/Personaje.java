/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.izquierdofp.heroforge.modelo;

/**
 *
 * @author 2DAM
 */
public class Personaje {
    private int id;
    private String nombre;
    private int clase_id;
    private int nivel;
    private int fuerza;
    private int agilidad;
    private int inteligencia;

    public Personaje(int id, String nombre, int clase_id, int nivel, int fuerza, int agilidad, int inteligencia) {
        this.id = id;
        this.nombre = nombre;
        this.clase_id = clase_id;
        this.nivel = nivel;
        this.fuerza = fuerza;
        this.agilidad = agilidad;
        this.inteligencia = inteligencia;
    }

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getClase_id() {
        return clase_id;
    }

    public void setClase_id(int clase_id) {
        this.clase_id = clase_id;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int getFuerza() {
        return fuerza;
    }

    public void setFuerza(int fuerza) {
        this.fuerza = fuerza;
    }

    public int getAgilidad() {
        return agilidad;
    }

    public void setAgilidad(int agilidad) {
        this.agilidad = agilidad;
    }

    public int getInteligencia() {
        return inteligencia;
    }

    public void setInteligencia(int inteligencia) {
        this.inteligencia = inteligencia;
    }
    
    
}
