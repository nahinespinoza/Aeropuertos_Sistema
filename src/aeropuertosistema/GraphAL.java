/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aeropuertosistema;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.LinkedList;

/**
 *
 * @author LENOVO
 */
public class GraphAL<V, E> {

    private LinkedList<Vertex<V, E>> vertices;
    private boolean isDirectec;
    private Comparator<V> cmp;

    public LinkedList<Vertex<V, E>> getVertices() {
        return vertices;
    }

    public void setVertices(LinkedList<Vertex<V, E>> vertices) {
        this.vertices = vertices;
    }

    public boolean isIsDirectec() {
        return isDirectec;
    }

    public void setIsDirectec(boolean isDirectec) {
        this.isDirectec = isDirectec;
    }

    public Comparator<V> getCmp() {
        return cmp;
    }

    public void setCmp(Comparator<V> cmp) {
        this.cmp = cmp;
    }

    public GraphAL(boolean isDirectec, Comparator<V> cmp) {
        this.isDirectec = isDirectec;
        this.cmp = cmp;
        this.vertices = new LinkedList<>();

    }

    private Vertex<V, E> findVertex(V content) {
        for (Vertex<V, E> v : vertices) {
            V c = v.getContent();
            if (this.cmp.compare(content, c) == 0) {
                return v;
            }
        }
        return null;
    }

    public boolean addVertex(V content) {
        if (content == null || findVertex(content) != null) {
            return false;
        }
        Vertex<V, E> newVertex = new Vertex<>(content);
        this.vertices.add(newVertex);
        return true;
    }

    private boolean connect(V v1, V v2) {
        if (v1 == null || v2 == null) {
            return false;
        }
        Vertex<V, E> ver1 = findVertex(v1);
        Vertex<V, E> ver2 = findVertex(v2);

        if (ver1 == null || ver2 == null) {
            return false;
        }

        Edge<E, V> newArco = new Edge<>(ver1, ver2);
        ver1.getEdges().add(newArco);
        if (!this.isDirectec) {
            Edge<E, V> rever = new Edge<>(ver2, ver1);
            ver2.getEdges().add(rever);
        }
        return true;
    }

public void loadAeropuertosFromTXT(String filePath) {
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] values = line.split(",");
            if (values.length == 4) {
                String code = values[0].trim();
                String name = values[1].trim();
                String city = values[2].trim();
                String country = values[3].trim();

                // Crear el aeropuerto y agregarlo al grafo
                Aeropuerto aeropuerto = new Aeropuerto(name, city, country, code);
                this.addVertex((V) aeropuerto);
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}


    public void displayAeropuertos() {
        for (Vertex<V, E> vertex : vertices) {
            Aeropuerto aeropuerto = (Aeropuerto) vertex.getContent();
            System.out.println("Código: " + aeropuerto.getCode() + ", Nombre: " + aeropuerto.getName()
                    + ", Ciudad: " + aeropuerto.getCity() + ", País: " + aeropuerto.getCountry());
        }
    }

}
