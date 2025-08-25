package aeropuertosistema;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author LENOVO
 */

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import java.util.List;

public class EditFlightDialogController {
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
    
    public void setAirports(List<Aeropuerto> airports) {
        origenCombo.getItems().setAll(airports);
        destinoCombo.getItems().setAll(airports);
    }
    
    // Método para cargar datos existentes del vuelo
    public void loadFlightData(Aeropuerto origen, Aeropuerto destino, Vuelo vuelo) {
        if (origen != null) {
            origenCombo.setValue(origen);
        }
        if (destino != null) {
            destinoCombo.setValue(destino);
        }
        if (vuelo != null) {
            airlineField.setText(vuelo.getAirline());
            flightNumberField.setText(vuelo.getNum_vuelo());
            distanceField.setText(String.valueOf(vuelo.getDistance().intValue()));
        }
    }
    
    // Método para validar los datos
    public boolean isValid() {
        return getOrigen() != null && 
               getDestino() != null && 
               !getAirline().isEmpty() && 
               !getFlightNumber().isEmpty() && 
               getDistance() > 0;
    }
    
    public String getValidationMessage() {
        if (getOrigen() == null) return "Seleccione aeropuerto origen";
        if (getDestino() == null) return "Seleccione aeropuerto destino";
        if (getAirline().isEmpty()) return "La aerolínea es obligatoria";
        if (getFlightNumber().isEmpty()) return "El número de vuelo es obligatorio";
        if (getDistance() <= 0) return "La distancia debe ser mayor a 0";
        if (getOrigen().equals(getDestino())) return "Origen y destino no pueden ser iguales";
        return null;
    }
}