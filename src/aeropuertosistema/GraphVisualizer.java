/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aeropuertosistema;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class GraphVisualizer extends Pane {

    private GraphAL<Aeropuerto, Vuelo> grafo;
    private Map<Aeropuerto, Circle> nodeCircles;
    private Map<Aeropuerto, Text> nodeLabels;
    private Map<String, Line> edges;

    public GraphVisualizer(GraphAL<Aeropuerto, Vuelo> grafo) {
        this.grafo = grafo;
        this.nodeCircles = new HashMap<>();
        this.nodeLabels = new HashMap<>();
        this.edges = new HashMap<>();

        // Tamaño más grande para la nueva ventana
        this.setPrefSize(1000, 700);
        this.setStyle("-fx-background-color: #f0f8ff;");
    }

    public void drawGraph() {
        this.getChildren().clear();
        nodeCircles.clear();
        nodeLabels.clear();
        edges.clear();

        // Calcular posiciones para los nodos (layout circular)
        int centerX = 400;
        int centerY = 300;
        int radius = 200;
        int nodeCount = grafo.getVertices().size();

        // Dibujar aristas primero (para que queden detrás de los nodos)
        int i = 0;
        for (Vertex<Aeropuerto, Vuelo> vertex : grafo.getVertices()) {
            Aeropuerto source = vertex.getContent();
            double angle = 2 * Math.PI * i / nodeCount;
            int sourceX = (int) (centerX + radius * Math.cos(angle));
            int sourceY = (int) (centerY + radius * Math.sin(angle));

            for (Edge<Vuelo, Aeropuerto> edge : vertex.getEdges()) {
                Aeropuerto target = edge.getTarget().getContent();
                int targetIndex = getVertexIndex(target);
                double targetAngle = 2 * Math.PI * targetIndex / nodeCount;
                int targetX = (int) (centerX + radius * Math.cos(targetAngle));
                int targetY = (int) (centerY + radius * Math.sin(targetAngle));

                drawEdge(source, target, sourceX, sourceY, targetX, targetY, edge.getData());
            }
            i++;
        }

        // Dibujar nodos
        i = 0;
        for (Vertex<Aeropuerto, Vuelo> vertex : grafo.getVertices()) {
            Aeropuerto aeropuerto = vertex.getContent();
            double angle = 2 * Math.PI * i / nodeCount;
            int x = (int) (centerX + radius * Math.cos(angle));
            int y = (int) (centerY + radius * Math.sin(angle));

            drawNode(aeropuerto, x, y, vertex.getEdges().size());
            i++;
        }
    }

    private int getVertexIndex(Aeropuerto aeropuerto) {
        int index = 0;
        for (Vertex<Aeropuerto, Vuelo> vertex : grafo.getVertices()) {
            if (vertex.getContent().equals(aeropuerto)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    private void drawNode(Aeropuerto aeropuerto, int x, int y, int connectionCount) {
        // Crear círculo del nodo
        Circle circle = new Circle(x, y, 20);

        // Color basado en número de conexiones
        if (connectionCount > 8) {
            circle.setFill(Color.RED);
        } else if (connectionCount > 5) {
            circle.setFill(Color.ORANGE);
        } else if (connectionCount > 3) {
            circle.setFill(Color.YELLOW);
        } else {
            circle.setFill(Color.LIGHTGREEN);
        }

        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(2);

        // Crear texto con el código del aeropuerto
        Text label = new Text(x - 10, y + 5, aeropuerto.getCode());
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");

        // Tooltip con información del aeropuerto
        String tooltipText = String.format("%s\n%s\n%d conexiones",
                aeropuerto.getCode(), aeropuerto.getName(), connectionCount);
        javafx.scene.control.Tooltip.install(circle,
                new javafx.scene.control.Tooltip(tooltipText));
        javafx.scene.control.Tooltip.install(label,
                new javafx.scene.control.Tooltip(tooltipText));

        // Agregar al panel y a los mapas
        this.getChildren().addAll(circle, label);
        nodeCircles.put(aeropuerto, circle);
        nodeLabels.put(aeropuerto, label);
    }

    private void drawEdge(Aeropuerto source, Aeropuerto target,
            int x1, int y1, int x2, int y2, Vuelo vuelo) {

        Line line = new Line(x1, y1, x2, y2);
        line.setStroke(Color.GRAY);
        line.setStrokeWidth(2);

        // Flecha para indicar dirección (solo si el grafo es dirigido)
        drawArrowHead(line, x1, y1, x2, y2);

        // Texto con la distancia
        int midX = (x1 + x2) / 2;
        int midY = (y1 + y2) / 2;

        if (vuelo != null) {
            Text distanceText = new Text(midX, midY - 5,
                    vuelo.getDistance() + "km");
            distanceText.setStyle("-fx-font-size: 10px; -fx-fill: #666;");
            this.getChildren().add(distanceText);
        }

        // Tooltip con información del vuelo
        String tooltipText = String.format("%s → %s\n%s %s\n%s km",
                source.getCode(), target.getCode(),
                vuelo != null ? vuelo.getAirline() : "Unknown",
                vuelo != null ? vuelo.getNum_vuelo() : "",
                vuelo != null ? String.valueOf(vuelo.getDistance()) : "0");

        javafx.scene.control.Tooltip.install(line,
                new javafx.scene.control.Tooltip(tooltipText));

        this.getChildren().add(line);
        edges.put(source.getCode() + "-" + target.getCode(), line);
    }

    private void drawArrowHead(Line line, double x1, double y1, double x2, double y2) {
        double angle = Math.atan2(y2 - y1, x2 - x1);
        double arrowLength = 10;

        // Puntos de la flecha
        double x3 = x2 - arrowLength * Math.cos(angle - Math.PI / 6);
        double y3 = y2 - arrowLength * Math.sin(angle - Math.PI / 6);
        double x4 = x2 - arrowLength * Math.cos(angle + Math.PI / 6);
        double y4 = y2 - arrowLength * Math.sin(angle + Math.PI / 6);

        Line arrow1 = new Line(x2, y2, x3, y3);
        Line arrow2 = new Line(x2, y2, x4, y4);

        arrow1.setStroke(Color.GRAY);
        arrow2.setStroke(Color.GRAY);
        arrow1.setStrokeWidth(2);
        arrow2.setStrokeWidth(2);

        this.getChildren().addAll(arrow1, arrow2);
    }

    public void highlightPath(LinkedList<Aeropuerto> path) {
        // Resetear todos los colores primero
        resetColors();

        // Resaltar el camino
        for (int i = 0; i < path.size() - 1; i++) {
            Aeropuerto source = path.get(i);
            Aeropuerto target = path.get(i + 1);

            // Resaltar nodos
            if (nodeCircles.containsKey(source)) {
                nodeCircles.get(source).setFill(Color.BLUE);
            }
            if (nodeCircles.containsKey(target)) {
                nodeCircles.get(target).setFill(Color.BLUE);
            }

            // Resaltar arista
            String edgeKey = source.getCode() + "-" + target.getCode();
            if (edges.containsKey(edgeKey)) {
                edges.get(edgeKey).setStroke(Color.BLUE);
                edges.get(edgeKey).setStrokeWidth(4);
            }
        }
    }

    public void resetColors() {
        // Resetear colores de nodos
        for (Circle circle : nodeCircles.values()) {
            circle.setFill(Color.LIGHTGRAY);
        }

        // Resetear colores de aristas
        for (Line line : edges.values()) {
            line.setStroke(Color.GRAY);
            line.setStrokeWidth(2);
        }
    }
}
