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
import java.util.Iterator;
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

    private boolean connectComplete(V source, V target, int weight, E data) {
        if (source == null || target == null) {
            return false;
        }

        Vertex<V, E> sourceVertex = findVertex(source);
        Vertex<V, E> targetVertex = findVertex(target);

        if (sourceVertex == null || targetVertex == null) {
            System.out.println("Error: Uno o ambos aeropuertos no existen en el grafo");
            return false;
        }

        //Verificacion de conexion entre Aropuertos - Usando lista de Edges
        for (Edge<E, V> edge : sourceVertex.getEdges()) {
            V targetContent = edge.getTarget().getContent();
            if (cmp.compare(targetContent, target) == 0) {
                System.out.println("Ya existe un vuelo entre " 
                        + ((Aeropuerto) source).getCode() + " y " 
                        + ((Aeropuerto) target).getCode());
                return false;
            }
        }
        //Creo y agrego la nueva arista
        Edge<E, V> newEdge = new Edge<>(sourceVertex, targetVertex, weight, data);
        sourceVertex.getEdges().add(newEdge);

        System.out.println("Vuelo creado: "
                + ((Aeropuerto) source).getCode() + " → "
                + ((Aeropuerto) target).getCode()
                + " | Distancia: " + weight + "km");

        return true;
    }

    //Metodo para eliminar un vertice del grafo, a su vez las aristas conectadas a el 
    public boolean removeVertex(V content) {

        if (content == null) {
            return false;
        }

        //Busco el vertice a eliminar
        Vertex<V, E> vertexRemove = findVertex(content);
        if (vertexRemove == null) {
            return false;
        }

        //Recorrer todas los Edges que apuntan a vertexRemove para removerlos
        for (Vertex<V, E> vertex : vertices) {
            //Obtengo las listas de Edges de cada Vertice
            LinkedList<Edge<E, V>> edges = vertex.getEdges();
            Iterator<Edge<E, V>> iterator = edges.iterator();
            while (iterator.hasNext()) {
                Edge<E, V> e = iterator.next();
                if (cmp.compare(e.getSource().getContent(), content) == 0 || cmp.compare(e.getTarget().getContent(), content) == 0) {
                    iterator.remove();
                }
            }
        }

        return false;

    }

    public void loadVuelosFromTXT(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean cabecera = true;
            while ((line = br.readLine()) != null) {
                if (cabecera) {
                    cabecera = false;
                    continue;
                }
                String[] values = line.split(",");

                if (values.length == 5) {
                    String codigoOrigen = values[0].trim();
                    String codigoDestino = values[1].trim();
                    String aerolinea = values[2].trim();
                    String numeroVuelo = values[3].trim();
                    int distancia = Integer.parseInt(values[4].trim());

                    //Creacion de aeropuerto solo con Code para busqueda
                    Aeropuerto origen = new Aeropuerto("", "", "", codigoOrigen);
                    Aeropuerto destino = new Aeropuerto("", "", "", codigoDestino);

                    //Creacion de Vuelo
                    Vuelo vuelo = new Vuelo(aerolinea, numeroVuelo, (float) distancia);

                    //Conectar vuelos con aeropuertos 
                    boolean exito = this.connectComplete((V) origen, (V) destino, distancia, (E) vuelo);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de vuelos: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Error en formato de número en el archivo: " + e.getMessage());
        }
    }

    //Metodo para cargar los Aeropuertos de un .txt
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

    //Metodo para mostrar los aeropuertos
    public void displayAeropuertos() {
        for (Vertex<V, E> vertex : vertices) {
            Aeropuerto aeropuerto = (Aeropuerto) vertex.getContent();
            System.out.println("Código: " + aeropuerto.getCode() + ", Nombre: " + aeropuerto.getName()
                    + ", Ciudad: " + aeropuerto.getCity() + ", País: " + aeropuerto.getCountry());
        }
    }

}
