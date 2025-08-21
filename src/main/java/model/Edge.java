/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author LENOVO
 */
class Edge<E, V> {
    
    private Vertex<V, E> source;
    private Vertex<V, E> target;
    private int weight; //Factor de Peso
    private E data; //Metada de Arcos

    //Constructores
    
    //Minimo para hacer un arco

    public Edge(Vertex<V, E> source, Vertex<V, E> target) {
        this.source = source;
        this.target = target;
    }
    //Completo 
    public Edge(Vertex<V, E> source, Vertex<V, E> target, int weight, E data) {
        this.source = source;
        this.target = target;
        this.weight = weight;
        this.data = data;
    }
    
    
    
    //Variaciones

    public Edge(Vertex<V, E> source, Vertex<V, E> target, int weight) {
        this.source = source;
        this.target = target;
        this.weight = weight;
    }

    public Edge(Vertex<V, E> source, Vertex<V, E> target, E data) {
        this.source = source;
        this.target = target;
        this.data = data;
    }
    

    public Vertex<V, E> getSource() {
        return source;
    }

    public void setSource(Vertex<V, E> source) {
        this.source = source;
    }

    public Vertex<V, E> getTarget() {
        return target;
    }

    public void setTarget(Vertex<V, E> target) {
        this.target = target;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }
}
