package aeropuertosistema;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.Comparator;
import java.util.LinkedList;

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
        
        statusLabel.setText("Listo para cargar datos");
        statusLabel.setStyle("-fx-text-fill: #27ae60;");
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

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