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
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

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

    public boolean updateFlightData(Aeropuerto origen, Aeropuerto destino, String numVueloAntiguo, Vuelo nuevoVuelo) {
        Vertex<V, E> origenVertex = getVertexForAirport(origen);
        if (origenVertex == null) {
            return false;
        }

        for (Edge<E, V> edge : origenVertex.getEdges()) {
            Aeropuerto target = (Aeropuerto) edge.getTarget().getContent();
            if (target.equals(destino)) {
                Vuelo vuelo = (Vuelo) edge.getData();
                if (vuelo.getNum_vuelo().equals(numVueloAntiguo)) {
                    // Actualizar los datos del vuelo
                    edge.setData((E) nuevoVuelo);
                    edge.setWeight(nuevoVuelo.getDistance().intValue());
                    return true;
                }
            }
        }
        return false;
    }

    public boolean connectComplete(V source, V target, int weight, E data) {
        if (source == null || target == null) {
            return false;
        }

        Vertex<V, E> sourceVertex = findVertex(source);
        Vertex<V, E> targetVertex = findVertex(target);

        if (sourceVertex == null || targetVertex == null) {
            System.out.println("Error: Uno o ambos aeropuertos no existen en el grafo");
            return false;
        }

        // Verificación de conexión entre Aeropuertos - Usando lista de Edges
        for (Edge<E, V> edge : sourceVertex.getEdges()) {
            V targetContent = edge.getTarget().getContent();
            if (cmp.compare(targetContent, target) == 0) {
                System.out.println("Ya existe un vuelo entre "
                        + ((Aeropuerto) source).getCode() + " y "
                        + ((Aeropuerto) target).getCode());
                return false;
            }
        }

        // Creo y agrego la nueva arista
        Edge<E, V> newEdge = new Edge<>(sourceVertex, targetVertex, weight, data);
        sourceVertex.getEdges().add(newEdge);

        System.out.println("Vuelo creado: "
                + ((Aeropuerto) source).getCode() + " → "
                + ((Aeropuerto) target).getCode()
                + " | Distancia: " + weight + "km");

        return true;
    }

    // Método para eliminar un vértice del grafo, a su vez las aristas conectadas a él 
    public boolean removeVertex(V content) {
        if (content == null) {
            return false;
        }

        // Busco el vértice a eliminar
        Vertex<V, E> vertexRemove = findVertex(content);
        if (vertexRemove == null) {
            return false;
        }

        // Recorrer todas los Edges que apuntan a vertexRemove para removerlos
        for (Vertex<V, E> vertex : vertices) {
            // Obtengo las listas de Edges de cada Vértice
            LinkedList<Edge<E, V>> edges = vertex.getEdges();
            Iterator<Edge<E, V>> iterator = edges.iterator();
            while (iterator.hasNext()) {
                Edge<E, V> e = iterator.next();
                if (cmp.compare(e.getTarget().getContent(), content) == 0) {
                    iterator.remove();
                }
            }
        }

        // Finalmente eliminar el vértice de la lista
        vertices.remove(vertexRemove);
        return true;
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

                    // Creación de aeropuerto solo con Code para búsqueda
                    Aeropuerto origen = new Aeropuerto("", "", "", codigoOrigen);
                    Aeropuerto destino = new Aeropuerto("", "", "", codigoDestino);

                    // Creación de Vuelo
                    Vuelo vuelo = new Vuelo(aerolinea, numeroVuelo, (float) distancia);

                    // Conectar vuelos con aeropuertos 
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

    // Método para cargar los Aeropuertos de un .txt
    public void loadAeropuertosFromTXT(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean cabecera = true;

            while ((line = br.readLine()) != null) {
                if (cabecera) {
                    cabecera = false;
                    continue;
                }
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

    // Método para mostrar los aeropuertos
    public void displayAeropuertos() {
        for (Vertex<V, E> vertex : vertices) {
            Aeropuerto aeropuerto = (Aeropuerto) vertex.getContent();
            System.out.println("Código: " + aeropuerto.getCode() + ", Nombre: " + aeropuerto.getName()
                    + ", Ciudad: " + aeropuerto.getCity() + ", País: " + aeropuerto.getCountry());
        }
    }

    // Método para obtener todos los aeropuertos
    public LinkedList<Aeropuerto> getAllAirports() {
        LinkedList<Aeropuerto> aeropuertos = new LinkedList<>();
        for (Vertex<V, E> vertex : vertices) {
            aeropuertos.add((Aeropuerto) vertex.getContent());
        }
        return aeropuertos;
    }

    // Método para encontrar el vértice correspondiente a un aeropuerto
    private Vertex<V, E> getVertexForAirport(Aeropuerto aeropuerto) {
        for (Vertex<V, E> vertex : vertices) {
            if (cmp.compare(vertex.getContent(), (V) aeropuerto) == 0) {
                return vertex;
            }
        }
        return null;
    }

    // Método público para obtener distancia entre aeropuertos
    public int getDistanceBetween(Aeropuerto origen, Aeropuerto destino) {
        Vertex<V, E> origenVertex = getVertexForAirport(origen);

        if (origenVertex == null) {
            return 0;
        }

        for (Edge<E, V> edge : origenVertex.getEdges()) {
            Aeropuerto target = (Aeropuerto) edge.getTarget().getContent();
            if (target.equals(destino)) {
                return edge.getWeight();
            }
        }

        return 0;
    }

    // Método para encontrar la ruta más corta entre dos aeropuertos usando Dijkstra
    public LinkedList<Aeropuerto> findShortestPath(Aeropuerto origen, Aeropuerto destino) {
        Vertex<V, E> start = getVertexForAirport(origen);
        Vertex<V, E> end = getVertexForAirport(destino);

        if (start == null || end == null) {
            return new LinkedList<>();
        }

        // Estructuras para el algoritmo de Dijkstra
        Map<Vertex<V, E>, Integer> distances = new HashMap<>();
        Map<Vertex<V, E>, Vertex<V, E>> previous = new HashMap<>();
        PriorityQueue<VertexDistance<V, E>> queue = new PriorityQueue<>(
                (v1, v2) -> Integer.compare(v1.distance, v2.distance)
        );

        // Inicialización
        for (Vertex<V, E> vertex : vertices) {
            distances.put(vertex, Integer.MAX_VALUE);
            previous.put(vertex, null);
        }
        distances.put(start, 0);
        queue.add(new VertexDistance<>(start, 0));

        // Algoritmo principal
        while (!queue.isEmpty()) {
            VertexDistance<V, E> current = queue.poll();
            Vertex<V, E> currentVertex = current.vertex;

            if (currentVertex.equals(end)) {
                break;
            }

            if (current.distance > distances.get(currentVertex)) {
                continue;
            }

            for (Edge<E, V> edge : currentVertex.getEdges()) {
                Vertex<V, E> neighbor = edge.getTarget();
                int newDist = distances.get(currentVertex) + edge.getWeight();

                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    previous.put(neighbor, currentVertex);
                    queue.add(new VertexDistance<>(neighbor, newDist));
                }
            }
        }

        // Reconstruir el camino si existe
        return reconstructPath(previous, end, origen);
    }

    // Clase auxiliar para almacenar vértices con su distancia
    private static class VertexDistance<V, E> {

        Vertex<V, E> vertex;
        int distance;

        VertexDistance(Vertex<V, E> vertex, int distance) {
            this.vertex = vertex;
            this.distance = distance;
        }
    }

    // Método para reconstruir el camino desde el destino hasta el origen
    private LinkedList<Aeropuerto> reconstructPath(
            Map<Vertex<V, E>, Vertex<V, E>> previous,
            Vertex<V, E> end,
            Aeropuerto origen) {

        LinkedList<Aeropuerto> path = new LinkedList<>();

        // Si no hay camino, retornar lista vacía
        if (previous.get(end) == null && !((Aeropuerto) end.getContent()).equals(origen)) {
            return path;
        }

        // Reconstruir el camino desde el final hasta el inicio
        for (Vertex<V, E> at = end; at != null; at = previous.get(at)) {
            path.addFirst((Aeropuerto) at.getContent());
        }

        return path;
    }

    // Método para verificar si existe un vuelo entre dos aeropuertos
    public boolean flightExists(Aeropuerto origen, Aeropuerto destino) {
        Vertex<V, E> origenVertex = getVertexForAirport(origen);
        if (origenVertex == null) {
            return false;
        }

        for (Edge<E, V> edge : origenVertex.getEdges()) {
            Aeropuerto target = (Aeropuerto) edge.getTarget().getContent();
            if (target.equals(destino)) {
                return true;
            }
        }
        return false;
    }

    public boolean removeFlight(Aeropuerto origen, Aeropuerto destino, String numVuelo) {
        Vertex<V, E> origenVertex = getVertexForAirport(origen);
        if (origenVertex == null) {
            return false;
        }

        Iterator<Edge<E, V>> iterator = origenVertex.getEdges().iterator();
        while (iterator.hasNext()) {
            Edge<E, V> edge = iterator.next();
            Aeropuerto target = (Aeropuerto) edge.getTarget().getContent();
            if (target.equals(destino)) {
                Vuelo vuelo = (Vuelo) edge.getData();
                if (vuelo.getNum_vuelo().equals(numVuelo)) {
                    iterator.remove();
                    return true;
                }
            }
        }
        return false;
    }

    public List<Vuelo> getFlightsBetween(Aeropuerto origen, Aeropuerto destino) {
        List<Vuelo> vuelos = new LinkedList<>();
        Vertex<V, E> origenVertex = getVertexForAirport(origen);

        if (origenVertex == null) {
            return vuelos;
        }

        for (Edge<E, V> edge : origenVertex.getEdges()) {
            Aeropuerto target = (Aeropuerto) edge.getTarget().getContent();
            if (target.equals(destino)) {
                vuelos.add((Vuelo) edge.getData());
            }
        }
        return vuelos;
    }

    public LinkedList<String> getAllAirlines() {
        LinkedList<String> airlines = new LinkedList<>();
        for (Vertex<V, E> vertex : vertices) {
            for (Edge<E, V> edge : vertex.getEdges()) {
                Vuelo vuelo = (Vuelo) edge.getData();
                if (vuelo != null && !airlines.contains(vuelo.getAirline())) {
                    airlines.add(vuelo.getAirline());
                }
            }
        }
        return airlines;
    }

    public LinkedList<Vuelo> getFlightsByAirline(String airline) {
        LinkedList<Vuelo> airlineFlights = new LinkedList<>();
        for (Vertex<V, E> vertex : vertices) {
            for (Edge<E, V> edge : vertex.getEdges()) {
                Vuelo vuelo = (Vuelo) edge.getData();
                if (vuelo != null && vuelo.getAirline().equalsIgnoreCase(airline)) {
                    airlineFlights.add(vuelo);
                }
            }
        }
        return airlineFlights;
    }

    public Map<Aeropuerto, Integer> getAirlineAirportStats(String airline) {
        Map<Aeropuerto, Integer> stats = new HashMap<>();
        for (Vertex<V, E> vertex : vertices) {
            Aeropuerto airport = (Aeropuerto) vertex.getContent();
            int count = 0;

            // Contar vuelos de salida de esta aerolínea
            for (Edge<E, V> edge : vertex.getEdges()) {
                Vuelo vuelo = (Vuelo) edge.getData();
                if (vuelo != null && vuelo.getAirline().equalsIgnoreCase(airline)) {
                    count++;
                }
            }

            // Contar vuelos de llegada de esta aerolínea
            for (Vertex<V, E> otherVertex : vertices) {
                for (Edge<E, V> edge : otherVertex.getEdges()) {
                    Vuelo vuelo = (Vuelo) edge.getData();
                    Aeropuerto target = (Aeropuerto) edge.getTarget().getContent();
                    if (vuelo != null && vuelo.getAirline().equalsIgnoreCase(airline)
                            && target.equals(airport)) {
                        count++;
                    }
                }
            }

            if (count > 0) {
                stats.put(airport, count);
            }
        }
        return stats;
    }

    public String getFlightRouteInfo(Vuelo vuelo) {
        for (Vertex<V, E> vertex : vertices) {
            for (Edge<E, V> edge : vertex.getEdges()) {
                Vuelo currentVuelo = (Vuelo) edge.getData();
                if (currentVuelo != null && currentVuelo.equals(vuelo)) {
                    Aeropuerto origen = (Aeropuerto) vertex.getContent();
                    Aeropuerto destino = (Aeropuerto) edge.getTarget().getContent();
                    return String.format("%-5s → %-5s %-12.0fkm %-30s",
                            origen.getCode(), destino.getCode(),
                            vuelo.getDistance(), destino.getName());
                }
            }
        }
        return "Información no disponible";
    }

    public int getInDegree(Aeropuerto aeropuerto) {

        int cont = 0;
        for (Vertex<V, E> vertex : vertices) {
            for (Edge<E, V> edge : vertex.getEdges()) {
                if (cmp.compare(edge.getTarget().getContent(), (V) aeropuerto) == 0) {
                    cont = cont + 1;
                }

            }

        }

        return cont;
    }
}
