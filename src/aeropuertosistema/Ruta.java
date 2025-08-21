/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aeropuertosistema;

import java.util.LinkedList;

/**
 *
 * @author LENOVO
 */
public class Ruta<V,E> {

    private LinkedList<Vertex<V, E>> vertices;
    private Float costo_total;

    public LinkedList<Vertex<V, E>> getVertices() {
        return vertices;
    }

    public void setVertices(LinkedList<Vertex<V, E>> vertices) {
        this.vertices = vertices;
    }

    public Float getCosto_total() {
        return costo_total;
    }

    public void setCosto_total(Float costo_total) {
        this.costo_total = costo_total;
    }

    public Ruta(LinkedList<Vertex<V, E>> vertices, Float costo_total) {
        this.vertices = vertices;
        this.costo_total = costo_total;
    }

}
