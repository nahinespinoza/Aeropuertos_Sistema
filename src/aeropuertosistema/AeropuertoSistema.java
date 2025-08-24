//package aeropuertosistema;
//
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.stage.FileChooser;
//import javafx.stage.Stage;
//import java.io.*;
//import java.util.LinkedList;
//
//public class AeropuertoSistema extends Application {
//    private TextArea outputArea;
//    private Button loadButton;
//    private Button displayButton;
//    private GraphAL<Aeropuerto, Vuelo> grafo;
//    private Label statusLabel;
//
//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        // Cargar el archivo FXML
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("AeropuertoLoaderUI.fxml"));
//        Parent root = loader.load();
//        
//        // Obtener el controlador y pasarle la referencia del stage
//        AeropuertoLoaderController controller = loader.getController();
//        controller.setPrimaryStage(primaryStage);
//        
//        // Configurar la escena
//        Scene scene = new Scene(root, 800, 600);
//        
//        // Configurar el stage
//        primaryStage.setTitle("Sistema de Gestión de Aeropuertos - Aeropuerto Daxing");
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//    
//    private void loadAeropuertosFromFile(Stage stage) {
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Seleccionar archivo de aeropuertos");
//        fileChooser.getExtensionFilters().addAll(
//            new FileChooser.ExtensionFilter("Archivos de texto", "*.txt"),
//            new FileChooser.ExtensionFilter("Todos los archivos", "*.*")
//        );
//        
//        File selectedFile = fileChooser.showOpenDialog(stage);
//        if (selectedFile != null) {
//            try {
//                grafo.loadAeropuertosFromTXT(selectedFile.getAbsolutePath());
//                statusLabel.setText("Archivo cargado exitosamente: " + selectedFile.getName());
//                statusLabel.setStyle("-fx-text-fill: #27ae60;");
//                displayButton.setDisable(false);
//                
//                outputArea.appendText("✓ Archivo cargado: " + selectedFile.getName() + "\n");
//                outputArea.appendText("✓ Ubicación: " + selectedFile.getAbsolutePath() + "\n");
//                outputArea.appendText("✓ Haga clic en 'Mostrar Aeropuertos Cargados' para ver los detalles.\n\n");
//                
//            } catch (Exception e) {
//                statusLabel.setText("Error al cargar el archivo: " + e.getMessage());
//                statusLabel.setStyle("-fx-text-fill: #c0392b;");
//                showAlert("Error", "No se pudo cargar el archivo: " + e.getMessage());
//                e.printStackTrace();
//            }
//        }
//    }
//    
//    private void displayAeropuertos() {
//        outputArea.clear();
//        outputArea.appendText("=== AEROPUERTOS CARGADOS ===\n\n");
//        
//        try {
//            LinkedList<Vertex<Aeropuerto, Vuelo>> vertices = grafo.getVertices();
//            
//            if (vertices.isEmpty()) {
//                outputArea.appendText("No hay aeropuertos cargados en el sistema.\n");
//                return;
//            }
//            
//            outputArea.appendText(String.format("%-5s %-30s %-20s %-15s\n", 
//                "CÓD", "NOMBRE", "CIUDAD", "PAÍS"));
//            outputArea.appendText("--------------------------------------------------------------------------------\n");
//            
//            for (Vertex<Aeropuerto, Vuelo> vertex : vertices) {
//                Aeropuerto aeropuerto = vertex.getContent();
//                outputArea.appendText(String.format("%-5s %-30s %-20s %-15s\n", 
//                    aeropuerto.getCode(),
//                    aeropuerto.getName(),
//                    aeropuerto.getCity(),
//                    aeropuerto.getCountry()));
//            }
//            
//            outputArea.appendText("\n✓ Total: " + vertices.size() + " aeropuertos cargados correctamente.\n");
//            
//        } catch (Exception e) {
//            outputArea.appendText("Error al mostrar aeropuertos: " + e.getMessage() + "\n");
//            e.printStackTrace();
//        }
//    }
//    
//    private void showAlert(String title, String message) {
//        Alert alert = new Alert(Alert.AlertType.ERROR);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//    
//    public static void main(String[] args) {
//        launch(args);
//    }
//}

package aeropuertosistema;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AeropuertoSistema extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Cargar el archivo FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AeropuertoLoaderUI.fxml"));
        Parent root = loader.load();
        
        // Obtener el controlador y pasarle la referencia del stage
        AeropuertoLoaderController controller = loader.getController();
        controller.setPrimaryStage(primaryStage);
        
        // Configurar la escena
        Scene scene = new Scene(root, 900, 700); // Aumenté el tamaño para mejor visualización
        
        // Configurar el stage
        primaryStage.setTitle("Sistema de Gestión de Aeropuertos - Aeropuerto Daxing");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}