/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aeropuertosistema;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

/**
 *
 * @author LENOVO
 */
public class SelectFlightDialogController {

    @FXML
    private ComboBox<Aeropuerto> origenCombo;
    @FXML
    private ComboBox<Aeropuerto> destinoCombo;
    @FXML
    private ComboBox<Vuelo> vueloCombo;

    private GraphAL<Aeropuerto, Vuelo> graph;

    public Aeropuerto getOrigen() {
        return origenCombo.getValue();
    }

    public Aeropuerto getDestino() {
        return destinoCombo.getValue();
    }

    public Vuelo getVuelo() {
        return vueloCombo.getValue();
    }

    public void setAirports(List<Aeropuerto> airports) {
        origenCombo.getItems().setAll(airports);
        destinoCombo.getItems().setAll(airports);
    }

    public void setGraph(GraphAL<Aeropuerto, Vuelo> graph) {
        this.graph = graph;
    }

    @FXML
    private void onOrigenSelected(ActionEvent event) {
        updateVuelosCombo();
    }

    @FXML
    private void onDestinoSelected(ActionEvent event) {
        updateVuelosCombo();
    }

    private void updateVuelosCombo() {
        if (origenCombo.getValue() != null && destinoCombo.getValue() != null && graph != null) {
            List<Vuelo> vuelos = graph.getFlightsBetween(origenCombo.getValue(), destinoCombo.getValue());
            vueloCombo.setItems(FXCollections.observableArrayList(vuelos));
            vueloCombo.setDisable(vuelos.isEmpty());
        } else {
            vueloCombo.getItems().clear();
            vueloCombo.setDisable(true);
        }
    }
}
