/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aeropuertosistema;

/**
 *
 * @author LENOVO
 */

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class AddFlightDialogController {
    @FXML private ComboBox<Aeropuerto> origenCombo;
    @FXML private ComboBox<Aeropuerto> destinoCombo;
    @FXML private TextField airlineField;
    @FXML private TextField flightNumberField;
    @FXML private TextField distanceField;

    public Aeropuerto getOrigen() { return origenCombo.getValue(); }
    public Aeropuerto getDestino() { return destinoCombo.getValue(); }
    public String getAirline() { return airlineField.getText().trim(); }
    public String getFlightNumber() { return flightNumberField.getText().trim(); }
    public int getDistance() { 
        try {
            return Integer.parseInt(distanceField.getText().trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    public void setAirports(java.util.List<Aeropuerto> airports) {
        origenCombo.getItems().setAll(airports);
        destinoCombo.getItems().setAll(airports);
    }
}
