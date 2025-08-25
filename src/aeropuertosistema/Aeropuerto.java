/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aeropuertosistema;

import java.util.Objects;

/**
 *
 * @author LENOVO
 */
public class Aeropuerto {

    private String name;
    private String city;
    private String country;
    private String code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return name;
    }
    
    public Aeropuerto(String name, String city, String country, String code) {
        this.name = name;
        this.city = city;
        this.country = country;
        this.code = code;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Aeropuerto that = (Aeropuerto) obj;
        return code.equals(that.code); // Compara solo por código
    }

    @Override
    public int hashCode() {
        return code.hashCode(); // Debe ser consistente con equals
    }

}
