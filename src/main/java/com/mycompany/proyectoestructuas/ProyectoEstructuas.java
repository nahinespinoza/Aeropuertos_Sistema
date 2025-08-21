/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.proyectoestructuas;

import java.util.Comparator;
import model.Aeropuerto;
import model.GraphAL;

/**
 *
 * @author LENOVO
 */
public class ProyectoEstructuas {

     public static void main(String[] args) {
        GraphAL<Aeropuerto, String> graph = new GraphAL<>(false, Comparator.comparing(Aeropuerto::getCode));
System.out.println("Ruta actual de trabajo: " + System.getProperty("user.dir"));

        // Cargar los aeropuertos desde el archivo .txt
graph.loadAeropuertosFromTXT("/data/aeropuertos.txt");

        // Mostrar los aeropuertos cargados
        graph.displayAeropuertos();
    }
}
