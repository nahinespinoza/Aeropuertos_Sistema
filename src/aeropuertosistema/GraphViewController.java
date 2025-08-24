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

public class GraphViewController implements Initializable {

    @FXML
    private BorderPane graphContainer;
    
    @FXML
    private Button refreshButton;
    
    @FXML
    private Button closeButton;
    
    private GraphVisualizer graphVisualizer;
    private GraphAL<Aeropuerto, Vuelo> grafo;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configurar botones
        refreshButton.setOnAction(e -> refreshGraph());
        closeButton.setOnAction(e -> closeWindow());
    }
    
    public void setGrafo(GraphAL<Aeropuerto, Vuelo> grafo) {
        this.grafo = grafo;
        initializeGraphVisualizer();
    }
    
    private void initializeGraphVisualizer() {
        if (grafo != null) {
            graphVisualizer = new GraphVisualizer(grafo);
            graphVisualizer.setPrefSize(1000, 700);
            graphContainer.setCenter(graphVisualizer);
            refreshGraph();
        }
    }
    
    private void refreshGraph() {
        if (graphVisualizer != null) {
            graphVisualizer.drawGraph();
        }
    }
    
    private void closeWindow() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
    
    public GraphVisualizer getGraphVisualizer() {
        return graphVisualizer;
    }
}