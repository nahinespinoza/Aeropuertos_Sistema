/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package aeropuertosistema;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.Button;
import javafx.scene.shape.Line;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;

public class GraphViewController implements Initializable {

    @FXML
    private BorderPane graphContainer;

    @FXML
    private Button refreshButton;

    @FXML
    private Button closeButton;

    private Pane graphPane;
    private GraphAL<Aeropuerto, Vuelo> grafo;
    private Map<Aeropuerto, Circle> nodeCircles;
    private Map<Aeropuerto, Text> nodeLabels;
    private Map<String, Line> edges;
    private LinkedList<Aeropuerto> currentPath; // Para almacenar la ruta actual

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        graphPane = new Pane();
        graphPane.setPrefSize(1000, 700);
        graphPane.setStyle("-fx-background-color: #f0f8ff;");

        nodeCircles = new HashMap<>();
        nodeLabels = new HashMap<>();
        edges = new HashMap<>();
        currentPath = new LinkedList<>();

        graphContainer.setCenter(graphPane);

        refreshButton.setOnAction(e -> {
            drawGraph();
            if (!currentPath.isEmpty()) {
                highlightPath(currentPath);
            }
        });

        closeButton.setOnAction(e -> closeWindow());
    }

    public void setGrafo(GraphAL<Aeropuerto, Vuelo> grafo) {
        this.grafo = grafo;
        drawGraph();
    }

    // Método para establecer y resaltar una ruta
    public void setAndHighlightPath(LinkedList<Aeropuerto> path) {
        this.currentPath = path;
        drawGraph(); // Redibujar todo
        highlightPath(path); // Resaltar la ruta
    }

    private void drawGraph() {
        graphPane.getChildren().clear();
        nodeCircles.clear();
        nodeLabels.clear();
        edges.clear();

        if (grafo == null || grafo.getVertices() == null || grafo.getVertices().isEmpty()) {
            return;
        }

        // Calcular posiciones (layout circular)
        int centerX = 500;
        int centerY = 350;
        int radius = 250;
        int nodeCount = grafo.getVertices().size();

        // Dibujar aristas primero
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

            drawNode(aeropuerto, x, y);
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


    private void drawNode(Aeropuerto aeropuerto, int x, int y) {
        Circle circle = new Circle(x, y, 20);

        // Obtener conexiones entrantes y salientes
        Vertex<Aeropuerto, Vuelo> vertex = null;
        for (Vertex<Aeropuerto, Vuelo> v : grafo.getVertices()) {
            if (v.getContent().equals(aeropuerto)) {
                vertex = v;
                break;
            }
        }

        if (vertex == null) {
            return;
        }

        int outConnections = vertex.getEdges().size(); // Conexiones salientes
        int inConnections = grafo.getInDegree(aeropuerto); // Conexiones entrantes
        int totalConnections = outConnections + inConnections;

        // Color basado en el número total de conexiones
        if (totalConnections > 8) {
            circle.setFill(Color.RED);
        } else if (totalConnections > 5) {
            circle.setFill(Color.ORANGE);
        } else if (totalConnections > 3) {
            circle.setFill(Color.YELLOW);
        } else {
            circle.setFill(Color.LIGHTGREEN);
        }

        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(2);

        Text label = new Text(x - 10, y + 5, aeropuerto.getCode());
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");

        // Texto de los Nodos 
        String tooltipText = String.format("%s - %s\nConexiones: %d entrantes, %d salientes\nTotal: %d conexiones",
                aeropuerto.getCode(),
                aeropuerto.getName(),
                inConnections,
                outConnections,
                totalConnections);

        Tooltip.install(circle, new Tooltip(tooltipText));
        Tooltip.install(label, new Tooltip(tooltipText));

        graphPane.getChildren().addAll(circle, label);
        nodeCircles.put(aeropuerto, circle);
        nodeLabels.put(aeropuerto, label);
    }

    private void drawEdge(Aeropuerto source, Aeropuerto target,
            int x1, int y1, int x2, int y2, Vuelo vuelo) {

        Line line = new Line(x1, y1, x2, y2);
        line.setStroke(Color.GRAY);
        line.setStrokeWidth(2);

        drawArrowHead(line, x1, y1, x2, y2);

        int midX = (x1 + x2) / 2;
        int midY = (y1 + y2) / 2;

        if (vuelo != null) {
            Text distanceText = new Text(midX, midY - 5,
                    vuelo.getDistance() + "km");
            distanceText.setStyle("-fx-font-size: 10px; -fx-fill: #666;");
            graphPane.getChildren().add(distanceText);
        }

        String tooltipText = String.format("%s → %s\n%s %s\n%s km",
                source.getCode(), target.getCode(),
                vuelo != null ? vuelo.getAirline() : "Unknown",
                vuelo != null ? vuelo.getNum_vuelo() : "",
                vuelo != null ? String.valueOf(vuelo.getDistance()) : "0");

        javafx.scene.control.Tooltip.install(line,
                new javafx.scene.control.Tooltip(tooltipText));

        graphPane.getChildren().add(line);
        edges.put(source.getCode() + "-" + target.getCode(), line);
    }

    private void drawArrowHead(Line line, double x1, double y1, double x2, double y2) {
        double angle = Math.atan2(y2 - y1, x2 - x1);
        double arrowLength = 10;

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

        graphPane.getChildren().addAll(arrow1, arrow2);
    }

    // Método para resaltar una ruta específica
    public void highlightPath(LinkedList<Aeropuerto> path) {
        if (path == null || path.size() < 2) {
            return;
        }

        // Resaltar nodos del camino
        for (Aeropuerto aeropuerto : path) {
            if (nodeCircles.containsKey(aeropuerto)) {
                nodeCircles.get(aeropuerto).setFill(Color.BLUE);
                nodeCircles.get(aeropuerto).setStroke(Color.DARKBLUE);
                nodeCircles.get(aeropuerto).setStrokeWidth(3);

                // También resaltar el texto
                if (nodeLabels.containsKey(aeropuerto)) {
                    nodeLabels.get(aeropuerto).setFill(Color.WHITE);
                    nodeLabels.get(aeropuerto).setStyle("-fx-font-weight: bold; -fx-font-size: 12px; -fx-fill: white;");
                }
            }
        }

        // Resaltar aristas del camino
        for (int i = 0; i < path.size() - 1; i++) {
            Aeropuerto source = path.get(i);
            Aeropuerto target = path.get(i + 1);

            String edgeKey = source.getCode() + "-" + target.getCode();
            String reverseEdgeKey = target.getCode() + "-" + source.getCode();

            if (edges.containsKey(edgeKey)) {
                edges.get(edgeKey).setStroke(Color.BLUE);
                edges.get(edgeKey).setStrokeWidth(4);
            } else if (edges.containsKey(reverseEdgeKey)) {
                edges.get(reverseEdgeKey).setStroke(Color.BLUE);
                edges.get(reverseEdgeKey).setStrokeWidth(4);
            }
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
