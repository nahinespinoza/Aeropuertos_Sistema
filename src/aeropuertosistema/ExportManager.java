package aeropuertosistema;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;

public class ExportManager {

    public static boolean exportRutaToTXT(LinkedList<Aeropuerto> ruta, int distanciaTotal,
            Aeropuerto origen, Aeropuerto destino) {
        try (FileWriter writer = new FileWriter("ruta_optima_" + origen.getCode() + "_a_"
                + destino.getCode() + ".txt")) {

            writer.write("=== RUTA ÓPTIMA EXPORTADA ===\n");
            writer.write("Fecha: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n");
            writer.write("Origen: " + origen.getCode() + " - " + origen.getName() + "\n");
            writer.write("Destino: " + destino.getCode() + " - " + destino.getName() + "\n");
            writer.write("Distancia total: " + distanciaTotal + " km\n\n");

            writer.write("DETALLE DE LA RUTA:\n");
            writer.write("===================\n");

            for (int i = 0; i < ruta.size(); i++) {
                Aeropuerto aeropuerto = ruta.get(i);
                writer.write((i + 1) + ". " + aeropuerto.getCode() + " - "
                        + aeropuerto.getName() + " (" + aeropuerto.getCity() + ", "
                        + aeropuerto.getCountry() + ")\n");

                if (i < ruta.size() - 1) {
                    writer.write("   ↓ Vuelo hacia próximo aeropuerto\n");
                }
            }

            writer.write("\n=== FIN DEL REPORTE ===\n");
            return true;

        } catch (IOException e) {
            System.err.println("Error al exportar ruta: " + e.getMessage());
            return false;
        }
    }

    public static boolean exportRutaToCSV(LinkedList<Aeropuerto> ruta, int distanciaTotal,
            Aeropuerto origen, Aeropuerto destino) {
        try (FileWriter writer = new FileWriter("ruta_optima_" + origen.getCode() + "_a_"
                + destino.getCode() + ".csv")) {

            // Encabezados
            writer.write("Numero,Aeropuerto,Codigo,Ciudad,Pais\n");

            // Datos
            for (int i = 0; i < ruta.size(); i++) {
                Aeropuerto aeropuerto = ruta.get(i);
                writer.write((i + 1) + "," + aeropuerto.getName() + ","
                        + aeropuerto.getCode() + "," + aeropuerto.getCity() + ","
                        + aeropuerto.getCountry() + "\n");
            }

            // Información adicional
            writer.write("\nResumen\n");
            writer.write("Origen," + origen.getCode() + "\n");
            writer.write("Destino," + destino.getCode() + "\n");
            writer.write("Distancia Total (km)," + distanciaTotal + "\n");
            writer.write("Total Aeropuertos," + ruta.size() + "\n");
            writer.write("Fecha Exportacion,"
                    + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n");

            return true;

        } catch (IOException e) {
            System.err.println("Error al exportar CSV: " + e.getMessage());
            return false;
        }
    }

    public static boolean exportRutaToHTML(LinkedList<Aeropuerto> ruta, int distanciaTotal,
            Aeropuerto origen, Aeropuerto destino) {
        try (FileWriter writer = new FileWriter("ruta_optima_" + origen.getCode() + "_a_"
                + destino.getCode() + ".html")) {

            writer.write("<!DOCTYPE html>\n");
            writer.write("<html>\n");
            writer.write("<head>\n");
            writer.write("    <title>Ruta Óptima - " + origen.getCode() + " a " + destino.getCode() + "</title>\n");
            writer.write("    <style>\n");
            writer.write("        body { font-family: Arial, sans-serif; margin: 40px; }\n");
            writer.write("        .header { background-color: #f0f8ff; padding: 20px; border-radius: 10px; }\n");
            writer.write("        .ruta-item { margin: 10px 0; padding: 10px; border-left: 4px solid #3498db; }\n");
            writer.write("        .resumen { background-color: #e8f5e8; padding: 15px; border-radius: 5px; margin-top: 20px; }\n");
            writer.write("    </style>\n");
            writer.write("</head>\n");
            writer.write("<body>\n");

            writer.write("    <div class='header'>\n");
            writer.write("        <h1>✈️ Ruta Óptima de Vuelo</h1>\n");
            writer.write("        <p><strong>Fecha:</strong> "
                    + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "</p>\n");
            writer.write("    </div>\n");

            writer.write("    <div class='resumen'>\n");
            writer.write("        <h3>Resumen del Viaje</h3>\n");
            writer.write("        <p><strong>Origen:</strong> " + origen.getCode() + " - " + origen.getName() + "</p>\n");
            writer.write("        <p><strong>Destino:</strong> " + destino.getCode() + " - " + destino.getName() + "</p>\n");
            writer.write("        <p><strong>Distancia Total:</strong> " + distanciaTotal + " km</p>\n");
            writer.write("        <p><strong>Total de Escalas:</strong> " + (ruta.size() - 2) + "</p>\n");
            writer.write("    </div>\n");

            writer.write("    <h3>Detalle de la Ruta:</h3>\n");
            for (int i = 0; i < ruta.size(); i++) {
                Aeropuerto aeropuerto = ruta.get(i);
                writer.write("    <div class='ruta-item'>\n");
                writer.write("        <strong>" + (i + 1) + ". " + aeropuerto.getCode() + "</strong> - "
                        + aeropuerto.getName() + "<br>\n");
                writer.write("        <em>" + aeropuerto.getCity() + ", " + aeropuerto.getCountry() + "</em>\n");
                writer.write("    </div>\n");

                if (i < ruta.size() - 1) {
                    writer.write("    <div style='text-align: center; margin: 5px 0;'>↓</div>\n");
                }
            }

            writer.write("</body>\n");
            writer.write("</html>\n");

            return true;

        } catch (IOException e) {
            System.err.println("Error al exportar HTML: " + e.getMessage());
            return false;
        }
    }

    public static boolean exportAirlineReport(String airline, LinkedList<Vuelo> flights,
            Map<Aeropuerto, Integer> airportStats) {
        try {
            String fileName = "reporte_" + airline.toLowerCase() + "_"
                    + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".txt";

            try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
                writer.println("REPORTE DE AEROLÍNEA: " + airline.toUpperCase());
                writer.println("Fecha: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
                writer.println("==================================================");
                writer.println();

                writer.println("ESTADÍSTICAS GENERALES:");
                writer.println("Total de vuelos: " + flights.size());
                writer.println("Aeropuertos que opera: " + airportStats.size());
                writer.println();

                writer.println("DISTRIBUCIÓN POR AEROPUERTO:");
                writer.printf("%-5s %-30s %-10s%n", "CÓD", "AEROPUERTO", "VUELOS");
                writer.println("--------------------------------------------");

                for (Map.Entry<Aeropuerto, Integer> entry : airportStats.entrySet()) {
                    Aeropuerto airport = entry.getKey();
                    writer.printf("%-5s %-30s %-10d%n",
                            airport.getCode(), airport.getName(), entry.getValue());
                }

                writer.println();
                writer.println("DETALLE DE VUELOS:");
                writer.printf("%-8s %-5s → %-5s %-12s %-30s%n",
                        "VUELO", "ORIG", "DEST", "DISTANCIA", "AEROPUERTO DESTINO");
                writer.println("------------------------------------------------------------");

                // Necesitarías acceso al grafo para obtener información completa
                for (Vuelo vuelo : flights) {
                    writer.printf("%-8s %s%n", vuelo.getNum_vuelo(), "Información de ruta");
                }
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
