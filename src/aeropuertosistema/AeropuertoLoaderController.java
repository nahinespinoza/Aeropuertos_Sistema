package aeropuertosistema;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;

public class AeropuertoLoaderController {

    @FXML
    private TextArea outputArea;

    @FXML
    private Button loadButton;

    @FXML
    private Button displayButton;

    @FXML
    private Button clearButton;

    @FXML
    private Label statusLabel;
    @FXML
    private Button loadVuelosButton;
    @FXML
    private Button displayVuelosButton;
    @FXML
    private Button estadisticasButton;

    @FXML
    private BorderPane graphContainer;
    @FXML
    private Button viewGraphButton;

    private GraphVisualizer graphVisualizer;
    private GraphAL<Aeropuerto, Vuelo> grafo;
    private Stage primaryStage;

    @FXML
    public void initialize() {
        // Crear comparador para aeropuertos (por código)
        Comparator<Aeropuerto> cmp = (a1, a2) -> a1.getCode().compareTo(a2.getCode());
        grafo = new GraphAL<>(true, cmp); // Grafo dirigido

        // Configurar acciones de los botones
        loadButton.setOnAction(e -> handleLoadAeropuertos());
        displayButton.setOnAction(e -> handleDisplayAeropuertos());
        clearButton.setOnAction(e -> handleClearOutput());
        loadVuelosButton.setOnAction(e -> handleLoadVuelos()); // NUEVA ACCIÓN
        displayVuelosButton.setOnAction(e -> handleDisplayVuelos()); // NUEVA ACCIÓN
        estadisticasButton.setOnAction(e -> handleEstadisticas());
        graphVisualizer = new GraphVisualizer(grafo);
        graphContainer.setCenter(graphVisualizer);

        statusLabel.setText("Listo para cargar datos");
        statusLabel.setStyle("-fx-text-fill: #27ae60;");
        // Inicialmente deshabilitar botones de vuelos hasta que haya aeropuertos
        loadVuelosButton.setDisable(true);
        displayVuelosButton.setDisable(true);
    }

    private void updateGraphVisualization() {
        Platform.runLater(() -> {
            graphVisualizer.drawGraph();
        });
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private void handleViewGraph() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GraphView.fxml"));
            Parent root = loader.load();

            // Pasar el grafo al controlador de la nueva ventana
            GraphViewController controller = loader.getController();
            controller.setGrafo(grafo);

            Stage graphStage = new Stage();
            graphStage.setTitle("Visualización Completa del Grafo - Aeropuerto Daxing");
            graphStage.setScene(new Scene(root, 1100, 800));
            graphStage.initModality(Modality.WINDOW_MODAL);
            graphStage.initOwner(primaryStage);
            graphStage.show();

        } catch (IOException e) {
            showAlert("Error", "No se pudo cargar la visualización del grafo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEstadisticas() {
        outputArea.clear();
        outputArea.appendText("=== ESTADÍSTICAS DEL SISTEMA ===\n\n");

        try {
            LinkedList<Vertex<Aeropuerto, Vuelo>> vertices = grafo.getVertices();

            if (vertices == null || vertices.isEmpty()) {
                outputArea.appendText("No hay aeropuertos cargados en el sistema.\n");
                return;
            }

            // Estadísticas generales
            int totalAeropuertos = vertices.size();
            int totalVuelos = 0;
            Aeropuerto masConectado = null;
            int maxConexiones = 0;
            Aeropuerto menosConectado = null;
            int minConexiones = Integer.MAX_VALUE;
            float distanciaTotal = 0;

            for (Vertex<Aeropuerto, Vuelo> vertex : vertices) {
                int conexiones = vertex.getEdges().size();
                totalVuelos += conexiones;

                // Calcular distancia total
                for (Edge<Vuelo, Aeropuerto> edge : vertex.getEdges()) {
                    Vuelo vuelo = edge.getData();
                    if (vuelo != null && vuelo.getDistance() != null) {
                        distanciaTotal += vuelo.getDistance();
                    }
                }

                // Encontrar aeropuerto más conectado
                if (conexiones > maxConexiones) {
                    maxConexiones = conexiones;
                    masConectado = vertex.getContent();
                }

                // Encontrar aeropuerto menos conectado
                if (conexiones < minConexiones) {
                    minConexiones = conexiones;
                    menosConectado = vertex.getContent();
                }
            }

            // Mostrar estadísticas generales
            outputArea.appendText("ESTADÍSTICAS GENERALES:\n");
            outputArea.appendText("• Total de aeropuertos: " + totalAeropuertos + "\n");
            outputArea.appendText("• Total de vuelos: " + totalVuelos + "\n");
            outputArea.appendText("• Distancia total de vuelos: " + String.format("%,.0f", distanciaTotal) + " km\n");
            outputArea.appendText("• Promedio de vuelos por aeropuerto: "
                    + String.format("%.2f", (double) totalVuelos / totalAeropuertos) + "\n\n");

            // Mostrar aeropuertos más y menos conectados
            outputArea.appendText("CONECTIVIDAD:\n");
            if (masConectado != null) {
                outputArea.appendText("• Más conectado: " + masConectado.getCode()
                        + " - " + masConectado.getName() + " (" + maxConexiones + " conexiones)\n");
            }
            if (menosConectado != null && minConexiones != Integer.MAX_VALUE) {
                outputArea.appendText("• Menos conectado: " + menosConectado.getCode()
                        + " - " + menosConectado.getName() + " (" + minConexiones + " conexiones)\n");
            }

            // Estadísticas por aeropuerto
            outputArea.appendText("\nESTADÍSTICAS POR AEROPUERTO:\n");
            outputArea.appendText(String.format("%-5s %-30s %-10s %-12s\n",
                    "CÓD", "NOMBRE", "VUELOS", "DISTANCIA (km)"));
            outputArea.appendText("------------------------------------------------------------------------\n");

            for (Vertex<Aeropuerto, Vuelo> vertex : vertices) {
                Aeropuerto aeropuerto = vertex.getContent();
                int numVuelos = vertex.getEdges().size();
                float distanciaAeropuerto = 0;

                for (Edge<Vuelo, Aeropuerto> edge : vertex.getEdges()) {
                    Vuelo vuelo = edge.getData();
                    if (vuelo != null && vuelo.getDistance() != null) {
                        distanciaAeropuerto += vuelo.getDistance();
                    }
                }

                outputArea.appendText(String.format("%-5s %-30s %-10d %-,12.0f\n",
                        aeropuerto.getCode(),
                        aeropuerto.getName(),
                        numVuelos,
                        distanciaAeropuerto));
            }

        } catch (Exception e) {
            outputArea.appendText("Error al calcular estadísticas: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    //METODOS PARA AEROPUERTOS
    @FXML
    private void handleLoadAeropuertos() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo de aeropuertos");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos de texto", "*.txt"),
                new FileChooser.ExtensionFilter("Todos los archivos", "*.*")
        );

        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            try {
                grafo.loadAeropuertosFromTXT(selectedFile.getAbsolutePath());
                statusLabel.setText("Archivo cargado exitosamente: " + selectedFile.getName());
                statusLabel.setStyle("-fx-text-fill: #27ae60;");
                displayButton.setDisable(false);
                // Habilitar botones de vuelos ahora que hay aeropuertos
                loadVuelosButton.setDisable(false);
                displayVuelosButton.setDisable(false);
                estadisticasButton.setDisable(false);

                outputArea.appendText("✓ Archivo cargado: " + selectedFile.getName() + "\n");
                outputArea.appendText("✓ Ubicación: " + selectedFile.getAbsolutePath() + "\n");
                outputArea.appendText("✓ Haga clic en 'Mostrar Aeropuertos Cargados' para ver los detalles.\n\n");

            } catch (Exception e) {
                statusLabel.setText("Error al cargar el archivo: " + e.getMessage());
                statusLabel.setStyle("-fx-text-fill: #c0392b;");
                showAlert("Error", "No se pudo cargar el archivo: " + e.getMessage());
                e.printStackTrace();
            }
        }
        updateGraphVisualization(); // ← Agregar esta línea

    }

    @FXML
    private void handleDisplayAeropuertos() {
        outputArea.clear();
        outputArea.appendText("=== AEROPUERTOS CARGADOS ===\n\n");

        try {
            LinkedList<Vertex<Aeropuerto, Vuelo>> vertices = grafo.getVertices();

            if (vertices == null || vertices.isEmpty()) {
                outputArea.appendText("No hay aeropuertos cargados en el sistema.\n");
                return;
            }

            outputArea.appendText(String.format("%-5s %-30s %-20s %-15s\n",
                    "CÓD", "NOMBRE", "CIUDAD", "PAÍS"));
            outputArea.appendText("--------------------------------------------------------------------------------\n");

            for (Vertex<Aeropuerto, Vuelo> vertex : vertices) {
                Aeropuerto aeropuerto = vertex.getContent();
                outputArea.appendText(String.format("%-5s %-30s %-20s %-15s\n",
                        aeropuerto.getCode(),
                        aeropuerto.getName(),
                        aeropuerto.getCity(),
                        aeropuerto.getCountry()));
            }

            outputArea.appendText("\n✓ Total: " + vertices.size() + " aeropuertos cargados correctamente.\n");

        } catch (Exception e) {
            outputArea.appendText("Error al mostrar aeropuertos: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    //METODOS PARA VUELOS
    // NUEVO MÉTODO: Cargar vuelos desde archivo
    @FXML
    private void handleLoadVuelos() {
        estadisticasButton.setDisable(false);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo de vuelos");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos de texto", "*.txt"),
                new FileChooser.ExtensionFilter("Todos los archivos", "*.*")
        );

        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            try {
                grafo.loadVuelosFromTXT(selectedFile.getAbsolutePath());
                statusLabel.setText("Vuelos cargados exitosamente: " + selectedFile.getName());
                statusLabel.setStyle("-fx-text-fill: #27ae60;");

                outputArea.appendText("✓ Vuelos cargados: " + selectedFile.getName() + "\n");
                outputArea.appendText("✓ Archivo procesado correctamente\n");

                // Mostrar estadísticas de conexiones
                mostrarEstadisticasConexiones();
            } catch (Exception e) {
                statusLabel.setText("Error al cargar vuelos: " + e.getMessage());
                statusLabel.setStyle("-fx-text-fill: #c0392b;");
                showAlert("Error", "No se pudo cargar el archivo de vuelos: " + e.getMessage());
                e.printStackTrace();
            }
        }
        updateGraphVisualization(); // ← Agregar esta línea

    }

    // NUEVO MÉTODO: Mostrar vuelos y conexiones
    @FXML
    private void handleDisplayVuelos() {
        outputArea.clear();
        outputArea.appendText("=== VUELOS Y CONEXIONES ===\n\n");

        try {
            LinkedList<Vertex<Aeropuerto, Vuelo>> vertices = grafo.getVertices();

            if (vertices == null || vertices.isEmpty()) {
                outputArea.appendText("No hay aeropuertos cargados en el sistema.\n");
                return;
            }

            for (Vertex<Aeropuerto, Vuelo> vertex : vertices) {
                Aeropuerto aeropuerto = vertex.getContent();
                int numVuelos = vertex.getEdges().size();

                outputArea.appendText("Aeropuerto: " + aeropuerto.getCode() + " - " + aeropuerto.getName() + "\n");
                outputArea.appendText("Vuelos salientes: " + numVuelos + "\n");

                if (numVuelos > 0) {
                    for (Edge<Vuelo, Aeropuerto> edge : vertex.getEdges()) {
                        Vuelo vuelo = edge.getData();
                        Aeropuerto destino = edge.getTarget().getContent();
                        outputArea.appendText("  → " + destino.getCode() + " | "
                                + vuelo.getAirline() + " " + vuelo.getNum_vuelo() + " | "
                                + vuelo.getDistance() + "km\n");
                    }
                }
                outputArea.appendText("----------------------------------------\n");
            }

        } catch (Exception e) {
            outputArea.appendText("Error al mostrar vuelos: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    // NUEVO MÉTODO: Mostrar estadísticas de conexiones
    private void mostrarEstadisticasConexiones() {
        outputArea.appendText("\n=== ESTADÍSTICAS DE CONEXIONES ===\n");

        int totalVuelos = 0;
        Aeropuerto masConectado = null;
        int maxConexiones = 0;

        for (Vertex<Aeropuerto, Vuelo> vertex : grafo.getVertices()) {
            int conexiones = vertex.getEdges().size();
            totalVuelos += conexiones;

            if (conexiones > maxConexiones) {
                maxConexiones = conexiones;
                masConectado = vertex.getContent();
            }
        }

        outputArea.appendText("Total de vuelos en el sistema: " + totalVuelos + "\n");
        if (masConectado != null) {
            outputArea.appendText("Aeropuerto más conectado: " + masConectado.getCode()
                    + " - " + masConectado.getName() + " (" + maxConexiones + " conexiones)\n");
        }
        outputArea.appendText("\n");
    }

    @FXML
    private void handleClearOutput() {
        outputArea.clear();
        statusLabel.setText("Salida limpiada - Listo para nuevas operaciones");
        statusLabel.setStyle("-fx-text-fill: #f39c12;");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public GraphAL<Aeropuerto, Vuelo> getGrafo() {
        return grafo;
    }
}
