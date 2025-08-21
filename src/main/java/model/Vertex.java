/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.LinkedList;

/**
 *
 * @author LENOVO
 */

public class Vertex<V, E> {

    private V content;
    private LinkedList<Edge<E, V>> edges;

    public V getContent() {
        return content;
    }

    public void setContent(V Content) {
        this.content = Content;
    }

    public LinkedList<Edge<E, V>> getEdges() {
        return edges;
    }

    public void setEdges(LinkedList<Edge<E, V>> edges) {
        this.edges = edges;
    }

    public Vertex(V Content, LinkedList<Edge<E, V>> edges) {
        this.content = Content;
        this.edges = edges;
    }
    public Vertex(V content) {
        this.content = content;
        this.edges = new LinkedList<>(); // Todo vértice nace con un contenido sin lista de arcos (esta vacia)
    }
}