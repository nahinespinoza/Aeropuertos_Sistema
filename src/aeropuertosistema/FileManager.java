/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aeropuertosistema;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author LENOVO
 */
public class FileManager {

    private static final String AEROPUERTOS_FILE = "src/aeropuertosistema/aeropuertos.txt";
    private static final String VUELOS_FILE = "src/aeropuertosistema/vuelos.txt";

    // Método para añadir aeropuerto al archivo
    public static boolean addAirportToFile(Aeropuerto aeropuerto) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(AEROPUERTOS_FILE, true))) {
            String line = String.format("\n%s,%s,%s,%s\n",
                    aeropuerto.getCode(),
                    aeropuerto.getName(),
                    aeropuerto.getCity(),
                    aeropuerto.getCountry());
            writer.write(line);
            return true;
        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo: " + e.getMessage());
            return false;
        }
    }

    // Método para eliminar aeropuerto del archivo
    public static boolean removeAirportFromFile(String code) {
        try {
            List<String> lines = new LinkedList<>();
            boolean found = false;

            // Leer todas las líneas excepto la que coincide con el código
            try (BufferedReader reader = new BufferedReader(new FileReader(AEROPUERTOS_FILE))) {
                String line;
                boolean firstLine = true;
                while ((line = reader.readLine()) != null) {
                    if (firstLine) {
                        firstLine = false;
                        lines.add(line); // Mantener la cabecera
                        continue;
                    }
                    if (!line.startsWith(code + ",")) {
                        lines.add(line);
                    } else {
                        found = true;
                    }
                }
            }

            // Reescribir el archivo sin el aeropuerto eliminado
            if (found) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(AEROPUERTOS_FILE))) {
                    for (String line : lines) {
                        writer.write(line + "\n");
                    }
                }
                return true;
            }
            return false;

        } catch (IOException e) {
            System.out.println("Error al modificar el archivo: " + e.getMessage());
            return false;
        }
    }

    // Método para añadir vuelo al archivo
    public static boolean addFlightToFile(Aeropuerto origen, Aeropuerto destino, Vuelo vuelo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(VUELOS_FILE, true))) {
            String line = String.format("\n%s,%s,%s,%s,%d\n",
                    origen.getCode(),
                    destino.getCode(),
                    vuelo.getAirline(),
                    vuelo.getNum_vuelo(),
                    vuelo.getDistance().intValue());
            writer.write(line);
            return true;
        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo: " + e.getMessage());
            return false;
        }
    }

    // Método para eliminar vuelo del archivo
    public static boolean removeFlightFromFile(Aeropuerto origen, Aeropuerto destino, String numVuelo) {
        try {
            List<String> lines = new LinkedList<>();
            boolean found = false;

            try (BufferedReader reader = new BufferedReader(new FileReader(VUELOS_FILE))) {
                String line;
                boolean firstLine = true;
                while ((line = reader.readLine()) != null) {
                    if (firstLine) {
                        firstLine = false;
                        lines.add(line);
                        continue;
                    }

                    String[] parts = line.split(",");
                    if (parts.length >= 5) {
                        String fileOrigen = parts[0].trim();
                        String fileDestino = parts[1].trim();
                        String fileNumVuelo = parts[3].trim();

                        // Buscar coincidencia exacta
                        if (fileOrigen.equals(origen.getCode()) && 
                            fileDestino.equals(destino.getCode()) && 
                            fileNumVuelo.equals(numVuelo)) {
                            found = true;
                            continue; // Saltar esta línea (eliminarla)
                        }
                    }
                    lines.add(line);
                }
            }

            if (found) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(VUELOS_FILE))) {
                    for (String line : lines) {
                        writer.write(line + "\n");
                    }
                }
                return true;
            }
            return false;

        } catch (IOException e) {
            System.out.println("Error al modificar el archivo: " + e.getMessage());
            return false;
        }
    }

    // Método para actualizar vuelo en el archivo (eliminar y añadir)
    public static boolean updateFlightInFile(Aeropuerto origen, Aeropuerto destino, String numVueloOriginal, Vuelo nuevoVuelo) {
        // Primero eliminar el vuelo existente usando el número de vuelo original
        boolean removed = removeFlightFromFile(origen, destino, numVueloOriginal);
        if (removed) {
            // Luego añadir el nuevo vuelo
            return addFlightToFile(origen, destino, nuevoVuelo);
        }
        return false;
    }
    
    

}
