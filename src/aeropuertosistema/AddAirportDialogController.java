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
import javafx.scene.control.TextField;

public class AddAirportDialogController {
    @FXML private TextField codeField;
    @FXML private TextField nameField;
    @FXML private TextField cityField;
    @FXML private TextField countryField;

    public String getCode() { return codeField.getText().trim(); }
    public String getName() { return nameField.getText().trim(); }
    public String getCity() { return cityField.getText().trim(); }
    public String getCountry() { return countryField.getText().trim(); }
}