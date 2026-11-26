/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.izquierdofp.heroforge.modelo;

/**
 *
 * @author 2DAM
 */
public final class Clase {
    private int id;
    private String nombre;
    
    public Clase(int id){
        this.id=id;
        String[] clases={"Guerrero", "Mago", "Pícaro", "Clérigo"};
        this.nombre=clases[id];
    }
    
    
    public String getClase(){
        return nombre;
    }
    
    
    public int getId(){
        return id;
    }
    
    public void setId(int id){
        this.id=id;
        String[] clases={"Guerrero", "Mago", "Pícaro", "Clérigo"};
        this.nombre=clases[id];
    }
}
