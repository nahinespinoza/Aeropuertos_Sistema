/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aeropuertosistema;

/**
 *
 * @author LENOVO
 */
public class Vuelo {

    private String airline;
    private String num_vuelo;
    private Float distance;

    public Vuelo(String airline, String num_vuelo, Float distance) {
        this.airline = airline;
        this.num_vuelo = num_vuelo;
        this.distance = distance;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public String getNum_vuelo() {
        return num_vuelo;
    }

    public void setNum_vuelo(String num_vuelo) {
        this.num_vuelo = num_vuelo;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return num_vuelo;
    }
    
    

}

