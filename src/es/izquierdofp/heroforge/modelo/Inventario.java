/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.izquierdofp.heroforge.modelo;

/**
 *
 * @author 2DAM
 */
public class Inventario {
    private int id;
    private int personaje_id;
    private int objeto_id;
    private int cantidad;
    private int equipado;

    public Inventario(int id, int personaje_id, int objeto_id, int cantidad, int equipado) {
        this.id = id;
        this.personaje_id = personaje_id;
        this.objeto_id = objeto_id;
        this.cantidad = cantidad;
        this.equipado = equipado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPersonaje_id() {
        return personaje_id;
    }

    public void setPersonaje_id(int personaje_id) {
        this.personaje_id = personaje_id;
    }

    public int getObjeto_id() {
        return objeto_id;
    }

    public void setObjeto_id(int objeto_id) {
        this.objeto_id = objeto_id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getEquipado() {
        return equipado;
    }

    public void setEquipado(int equipado) {
        this.equipado = equipado;
    }
    
    
}
