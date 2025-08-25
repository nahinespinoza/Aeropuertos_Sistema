package aeropuertosistema;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;

public class AeropuertoLoaderController {

    @FXML
    private TextArea outputArea;

    @FXML
    private Button loadButton;

    @FXML
    private Button displayButton;

    @FXML
    private Button clearButton;

    @FXML
    private Label statusLabel;

    @FXML
    private Button loadVuelosButton;

    @FXML
    private Button displayVuelosButton;

    @FXML
    private Button estadisticasButton;

    @FXML
    private BorderPane graphContainer;

    @FXML
    private Button viewGraphButton;

    @FXML
    private ComboBox<Aeropuerto> origenComboBox;

    @FXML
    private ComboBox<Aeropuerto> destinoComboBox;

    @FXML
    private Button buscarRutaButton;

    @FXML
    private Button addAirportButton;

    @FXML
    private Button deleteAirportButton;

    @FXML
    private Button addFlightButton;

    @FXML
    private Button editFlightButton;

    @FXML
    private Button deleteFlightButton;
    @FXML
    private Button exportTXTButton;

    @FXML
    private Button exportCSVButton;

    @FXML
    private Button exportHTMLButton;
    @FXML
    private ComboBox<Aeropuerto> deleteAirportComboBox;
    @FXML
    private Button airlineAnalysisButton;
    @FXML
    private ComboBox<String> airlineComboBox;

    private GraphAL<Aeropuerto, Vuelo> grafo;
    private Stage primaryStage;

    @FXML
    public void initialize() {
        // Crear comparador para aeropuertos (por código)
        Comparator<Aeropuerto> cmp = (a1, a2) -> a1.getCode().compareTo(a2.getCode());
        grafo = new GraphAL<>(true, cmp); // Grafo dirigido

        cargarDatos();

        // Configurar acciones de los botones
        exportTXTButton.setOnAction(e -> handleExportTXT());

        exportCSVButton.setOnAction(e -> handleExportCSV());
        exportHTMLButton.setOnAction(e -> handleExportHTML());

        loadButton.setOnAction(e -> handleLoadAeropuertos());
        displayButton.setOnAction(e -> handleDisplayAeropuertos());
        clearButton.setOnAction(e -> handleClearOutput());
        loadVuelosButton.setOnAction(e -> handleLoadVuelos());
        displayVuelosButton.setOnAction(e -> handleDisplayVuelos());
        estadisticasButton.setOnAction(e -> handleEstadisticas());
        viewGraphButton.setOnAction(e -> handleViewGraph());
        buscarRutaButton.setOnAction(e -> handleBuscarRuta());
        addAirportButton.setOnAction(e -> handleAddAirport());
        deleteAirportButton.setOnAction(e -> handleDeleteAirport());
        addFlightButton.setOnAction(e -> handleAddFlight());
        editFlightButton.setOnAction(e -> handleEditFlight());
        deleteFlightButton.setOnAction(e -> handleDeleteFlight());
        deleteAirportComboBox.setPromptText("Seleccione aeropuerto a eliminar");
        airlineAnalysisButton.setOnAction(e -> handleAirlineAnalysis());
        airlineComboBox.setPromptText("Seleccione aerolínea");

        statusLabel.setText("Listo para cargar datos");
        statusLabel.setStyle("-fx-text-fill: #27ae60;");

        // Inicialmente deshabilitar botones de vuelos hasta que haya aeropuertos
        loadVuelosButton.setDisable(false);
        displayVuelosButton.setDisable(false);
        estadisticasButton.setDisable(false);

    }

    private void cargarDatos() {
        try {
            // Rutas relativas desde la carpeta del proyecto
            String rutaAeropuertos = "src/aeropuertosistema/aeropuertos.txt";
            String rutaVuelos = "src/aeropuertosistema/vuelos.txt";

            // Cargar aeropuertos
            grafo.loadAeropuertosFromTXT(rutaAeropuertos);
            outputArea.appendText("✓ Aeropuertos cargados automáticamente\n");

            // Cargar vuelos
            grafo.loadVuelosFromTXT(rutaVuelos);
            outputArea.appendText("✓ Vuelos cargados automáticamente\n");
            updateAirlineComboBox();

            // Habilitar funcionalidades
            displayButton.setDisable(false);
            loadVuelosButton.setDisable(true);
            displayVuelosButton.setDisable(true);
            estadisticasButton.setDisable(true);

            // Actualizar comboboxes
            updateAirportComboBoxes();

            statusLabel.setText("Datos cargados automáticamente");
            statusLabel.setStyle("-fx-text-fill: #27ae60;");

        } catch (Exception e) {
            outputArea.appendText("⚠️ No se pudieron cargar los datos automáticos: " + e.getMessage() + "\n");
            statusLabel.setText("Error al cargar datos automáticos");
            statusLabel.setStyle("-fx-text-fill: #c0392b;");
        }
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private void handleViewGraph() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GraphView.fxml"));
            Parent root = loader.load();

            // Pasar el grafo al controlador de la nueva ventana
            GraphViewController controller = loader.getController();
            controller.setGrafo(grafo);

            Stage graphStage = new Stage();
            graphStage.setTitle("Visualización Completa del Grafo - Aeropuerto Daxing");
            graphStage.setScene(new Scene(root, 1100, 800));
            graphStage.initModality(Modality.WINDOW_MODAL);
            graphStage.initOwner(primaryStage);
            graphStage.show();

        } catch (IOException e) {
            showAlert("Error", "No se pudo cargar la visualización del grafo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEstadisticas() {
        outputArea.clear();
        outputArea.appendText("=== ESTADÍSTICAS DEL SISTEMA ===\n\n");

        try {
            LinkedList<Vertex<Aeropuerto, Vuelo>> vertices = grafo.getVertices();

            if (vertices == null || vertices.isEmpty()) {
                outputArea.appendText("No hay aeropuertos cargados en el sistema.\n");
                return;
            }

            // Estadísticas generales
            int totalAeropuertos = vertices.size();
            int totalVuelos = 0;
            Aeropuerto masConectado = null;
            int maxConexiones = 0;
            Aeropuerto menosConectado = null;
            int minConexiones = Integer.MAX_VALUE;
            float distanciaTotal = 0;

            for (Vertex<Aeropuerto, Vuelo> vertex : vertices) {
                int conexiones = vertex.getEdges().size();
                totalVuelos += conexiones;

                // Calcular distancia total
                for (Edge<Vuelo, Aeropuerto> edge : vertex.getEdges()) {
                    Vuelo vuelo = edge.getData();
                    if (vuelo != null && vuelo.getDistance() != null) {
                        distanciaTotal += vuelo.getDistance();
                    }
                }

                // Encontrar aeropuerto más conectado
                if (conexiones > maxConexiones) {
                    maxConexiones = conexiones;
                    masConectado = vertex.getContent();
                }

                // Encontrar aeropuerto menos conectado
                if (conexiones < minConexiones) {
                    minConexiones = conexiones;
                    menosConectado = vertex.getContent();
                }
            }

            // Mostrar estadísticas generales
            outputArea.appendText("ESTADÍSTICAS GENERALES:\n");
            outputArea.appendText("• Total de aeropuertos: " + totalAeropuertos + "\n");
            outputArea.appendText("• Total de vuelos: " + totalVuelos + "\n");
            outputArea.appendText("• Distancia total de vuelos: " + String.format("%,.0f", distanciaTotal) + " km\n");
            outputArea.appendText("• Promedio de vuelos por aeropuerto: "
                    + String.format("%.2f", (double) totalVuelos / totalAeropuertos) + "\n\n");

            // Mostrar aeropuertos más y menos conectados
            outputArea.appendText("CONECTIVITY:\n");
            if (masConectado != null) {
                outputArea.appendText("• Más conectado: " + masConectado.getCode()
                        + " - " + masConectado.getName() + " (" + maxConexiones + " conexiones)\n");
            }
            if (menosConectado != null && minConexiones != Integer.MAX_VALUE) {
                outputArea.appendText("• Menos conectado: " + menosConectado.getCode()
                        + " - " + menosConectado.getName() + " (" + minConexiones + " conexiones)\n");
            }

            // Estadísticas por aeropuerto
            outputArea.appendText("\nESTADÍSTICAS POR AEROPUERTO:\n");
            outputArea.appendText(String.format("%-5s %-30s %-10s %-12s\n",
                    "CÓD", "NOMBRE", "VUELOS", "DISTANCIA (km)"));
            outputArea.appendText("------------------------------------------------------------------------\n");

            for (Vertex<Aeropuerto, Vuelo> vertex : vertices) {
                Aeropuerto aeropuerto = vertex.getContent();
                int numVuelos = vertex.getEdges().size();
                float distanciaAeropuerto = 0;

                for (Edge<Vuelo, Aeropuerto> edge : vertex.getEdges()) {
                    Vuelo vuelo = edge.getData();
                    if (vuelo != null && vuelo.getDistance() != null) {
                        distanciaAeropuerto += vuelo.getDistance();
                    }
                }

                outputArea.appendText(String.format("%-5s %-30s %-10d %-,12.0f\n",
                        aeropuerto.getCode(),
                        aeropuerto.getName(),
                        numVuelos,
                        distanciaAeropuerto));
            }

        } catch (Exception e) {
            outputArea.appendText("Error al calcular estadísticas: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    //METODOS PARA AEROPUERTOS
    @FXML
    private void handleLoadAeropuertos() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo de aeropuertos");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos de texto", "*.txt"),
                new FileChooser.ExtensionFilter("Todos los archivos", "*.*")
        );

        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            try {
                grafo.loadAeropuertosFromTXT(selectedFile.getAbsolutePath());
                statusLabel.setText("Archivo cargado exitosamente: " + selectedFile.getName());
                statusLabel.setStyle("-fx-text-fill: #27ae60;");
                displayButton.setDisable(false);
                // Habilitar botones de vuelos ahora que hay aeropuertos
                loadVuelosButton.setDisable(false);
                displayVuelosButton.setDisable(false);
                estadisticasButton.setDisable(false);

                outputArea.appendText("✓ Archivo cargado: " + selectedFile.getName() + "\n");
                outputArea.appendText("✓ Ubicación: " + selectedFile.getAbsolutePath() + "\n");
                outputArea.appendText("✓ Haga clic en 'Mostrar Aeropuertos Cargados' para ver los detalles.\n\n");

            } catch (Exception e) {
                statusLabel.setText("Error al cargar el archivo: " + e.getMessage());
                statusLabel.setStyle("-fx-text-fill: #c0392b;");
                showAlert("Error", "No se pudo cargar el archivo: " + e.getMessage());
                e.printStackTrace();
            }
        }
        updateAirportComboBoxes();
    }

    @FXML
    private void handleDisplayAeropuertos() {
        outputArea.clear();
        outputArea.appendText("=== AEROPUERTOS CARGADOS ===\n\n");

        try {
            LinkedList<Vertex<Aeropuerto, Vuelo>> vertices = grafo.getVertices();

            if (vertices == null || vertices.isEmpty()) {
                outputArea.appendText("No hay aeropuertos cargados en el sistema.\n");
                return;
            }

            outputArea.appendText(String.format("%-5s %-30s %-20s %-15s\n",
                    "CÓD", "NOMBRE", "CIUDAD", "PAÍS"));
            outputArea.appendText("--------------------------------------------------------------------------------\n");

            for (Vertex<Aeropuerto, Vuelo> vertex : vertices) {
                Aeropuerto aeropuerto = vertex.getContent();
                outputArea.appendText(String.format("%-5s %-30s %-20s %-15s\n",
                        aeropuerto.getCode(),
                        aeropuerto.getName(),
                        aeropuerto.getCity(),
                        aeropuerto.getCountry()));
            }

            outputArea.appendText("\n✓ Total: " + vertices.size() + " aeropuertos cargados correctamente.\n");

        } catch (Exception e) {
            outputArea.appendText("Error al mostrar aeropuertos: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    //METODOS PARA VUELOS
    @FXML
    private void handleLoadVuelos() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo de vuelos");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos de texto", "*.txt"),
                new FileChooser.ExtensionFilter("Todos los archivos", "*.*")
        );

        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            try {
                grafo.loadVuelosFromTXT(selectedFile.getAbsolutePath());
                statusLabel.setText("Vuelos cargados exitosamente: " + selectedFile.getName());
                statusLabel.setStyle("-fx-text-fill: #27ae60;");

                outputArea.appendText("✓ Vuelos cargados: " + selectedFile.getName() + "\n");
                outputArea.appendText("✓ Archivo procesado correctamente\n");

                // Mostrar estadísticas de conexiones
                mostrarEstadisticasConexiones();
            } catch (Exception e) {
                statusLabel.setText("Error al cargar vuelos: " + e.getMessage());
                statusLabel.setStyle("-fx-text-fill: #c0392b;");
                showAlert("Error", "No se pudo cargar el archivo de vuelos: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleDisplayVuelos() {
        outputArea.clear();
        outputArea.appendText("=== VUELOS Y CONEXIONES ===\n\n");

        try {
            LinkedList<Vertex<Aeropuerto, Vuelo>> vertices = grafo.getVertices();

            if (vertices == null || vertices.isEmpty()) {
                outputArea.appendText("No hay aeropuertos cargados en el sistema.\n");
                return;
            }

            for (Vertex<Aeropuerto, Vuelo> vertex : vertices) {
                Aeropuerto aeropuerto = vertex.getContent();
                int numVuelos = vertex.getEdges().size();

                outputArea.appendText("Aeropuerto: " + aeropuerto.getCode() + " - " + aeropuerto.getName() + "\n");
                outputArea.appendText("Vuelos salientes: " + numVuelos + "\n");

                if (numVuelos > 0) {
                    for (Edge<Vuelo, Aeropuerto> edge : vertex.getEdges()) {
                        Vuelo vuelo = edge.getData();
                        Aeropuerto destino = edge.getTarget().getContent();
                        outputArea.appendText("  → " + destino.getCode() + " | "
                                + vuelo.getAirline() + " " + vuelo.getNum_vuelo() + " | "
                                + vuelo.getDistance() + "km\n");
                    }
                }
                outputArea.appendText("----------------------------------------\n");
            }

        } catch (Exception e) {
            outputArea.appendText("Error al mostrar vuelos: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    // Método: Mostrar estadísticas de conexiones
    private void mostrarEstadisticasConexiones() {
        outputArea.appendText("\n=== ESTADÍSTICAS DE CONEXIONES ===\n");

        int totalVuelos = 0;
        Aeropuerto masConectado = null;
        int maxConexiones = 0;

        for (Vertex<Aeropuerto, Vuelo> vertex : grafo.getVertices()) {
            int conexiones = vertex.getEdges().size();
            totalVuelos += conexiones;

            if (conexiones > maxConexiones) {
                maxConexiones = conexiones;
                masConectado = vertex.getContent();
            }
        }

        outputArea.appendText("Total de vuelos en el sistema: " + totalVuelos + "\n");
        if (masConectado != null) {
            outputArea.appendText("Aeropuerto más conectado: " + masConectado.getCode()
                    + " - " + masConectado.getName() + " (" + maxConexiones + " conexiones)\n");
        }
        outputArea.appendText("\n");
    }

    // Método para actualizar los ComboBox cuando se cargan aeropuertos
    private void updateAirportComboBoxes() {
        LinkedList<Aeropuerto> aeropuertos = grafo.getAllAirports();
        origenComboBox.getItems().setAll(aeropuertos);
        destinoComboBox.getItems().setAll(aeropuertos);
        deleteAirportComboBox.getItems().setAll(aeropuertos);
    }

    @FXML
    private void handleBuscarRuta() {
        Aeropuerto origen = origenComboBox.getValue();
        Aeropuerto destino = destinoComboBox.getValue();

        if (origen == null || destino == null) {
            showAlert("Error", "Seleccione aeropuerto origen y destino");
            return;
        }

        if (origen.equals(destino)) {
            showAlert("Error", "El aeropuerto origen y destino no pueden ser iguales");
            return;
        }

        outputArea.appendText("\n=== BÚSQUEDA DE RUTA ÓPTIMA ===\n");
        outputArea.appendText("Buscando ruta más corta: " + origen.getCode() + " → " + destino.getCode() + "\n");

        try {
            LinkedList<Aeropuerto> rutaOptima = grafo.findShortestPath(origen, destino);
            int totalDistance = 0;

            if (rutaOptima.isEmpty()) {
                outputArea.appendText("❌ No se encontró una ruta entre estos aeropuertos\n");
                return;
            }

            // Calcular distancia total
            for (int i = 0; i < rutaOptima.size() - 1; i++) {
                Aeropuerto actual = rutaOptima.get(i);
                Aeropuerto siguiente = rutaOptima.get(i + 1);
                totalDistance += grafo.getDistanceBetween(actual, siguiente);
            }

            // Mostrar la ruta encontrada
            outputArea.appendText("✅ Ruta óptima encontrada:\n");
            for (int i = 0; i < rutaOptima.size(); i++) {
                Aeropuerto aeropuerto = rutaOptima.get(i);
                outputArea.appendText("   " + aeropuerto.getCode() + " - " + aeropuerto.getName());

                if (i < rutaOptima.size() - 1) {
                    Aeropuerto next = rutaOptima.get(i + 1);
                    int distancia = grafo.getDistanceBetween(aeropuerto, next);
                    outputArea.appendText(" → " + distancia + "km\n");
                } else {
                    outputArea.appendText("\n");
                }
            }

            outputArea.appendText("📊 Distancia total: " + totalDistance + "km\n");

            // Exportar la ruta
            boolean exportadoTXT = ExportManager.exportRutaToTXT(rutaOptima, totalDistance, origen, destino);
            boolean exportadoCSV = ExportManager.exportRutaToCSV(rutaOptima, totalDistance, origen, destino);
            boolean exportadoHTML = ExportManager.exportRutaToHTML(rutaOptima, totalDistance, origen, destino);

            if (exportadoTXT && exportadoCSV && exportadoHTML) {
                outputArea.appendText("📁 Ruta exportada en formatos: TXT, CSV, HTML\n");
                outputArea.appendText("💾 Archivos guardados en la carpeta del proyecto\n");
            }

            // Resaltar la ruta en la visualización
            openGraphViewWithHighlightedPath(rutaOptima);

        } catch (Exception e) {
            outputArea.appendText("❌ Error al buscar ruta: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    private void openGraphViewWithHighlightedPath(LinkedList<Aeropuerto> path) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GraphView.fxml"));
            Parent root = loader.load();

            GraphViewController controller = loader.getController();
            controller.setGrafo(grafo);
            controller.setAndHighlightPath(path); // Pasar y resaltar la ruta

            Stage graphStage = new Stage();
            graphStage.setTitle("Visualización de Ruta Óptima - "
                    + path.getFirst().getCode() + " → "
                    + path.getLast().getCode());
            graphStage.setScene(new Scene(root, 1100, 800));
            graphStage.initModality(Modality.WINDOW_MODAL);
            graphStage.initOwner(primaryStage);
            graphStage.show();

        } catch (IOException e) {
            showAlert("Error", "No se pudo abrir la visualización: " + e.getMessage());
        }
    }

    //-----------------
    // Métodos para gestión de aeropuertos
    @FXML
    private void handleAddAirport() {
        try {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Añadir Aeropuerto");
            dialog.setHeaderText("Ingrese los datos del nuevo aeropuerto");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddAirportDialog.fxml"));
            dialog.getDialogPane().setContent(loader.load());

            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                AddAirportDialogController controller = loader.getController();

                String code = controller.getCode();
                String name = controller.getName();
                String city = controller.getCity();
                String country = controller.getCountry();

                if (code.isEmpty() || name.isEmpty() || city.isEmpty() || country.isEmpty()) {
                    showAlert("Error", "Todos los campos son obligatorios");
                    return;
                }

                // Crear aeropuerto
                Aeropuerto nuevoAeropuerto = createAirport(code, name, city, country);
                if (addVertexGeneric(nuevoAeropuerto)) {
                    // Añadir al archivo
                    if (FileManager.addAirportToFile(nuevoAeropuerto)) {
                        outputArea.appendText("✓ Aeropuerto añadido: " + code + " - " + name + "\n");
                        updateAirportComboBoxes();
                    } else {
                        showAlert("Error", "Aeropuerto añadido al grafo pero no se pudo guardar en el archivo");
                    }
                } else {
                    showAlert("Error", "El aeropuerto ya existe o los datos son inválidos");
                }
            }
        } catch (IOException e) {
            showAlert("Error", "No se pudo cargar el diálogo: " + e.getMessage());
        }
    }

    // Método factory para crear aeropuertos (genérico)
    private Aeropuerto createAirport(String code, String name, String city, String country) {
        return new Aeropuerto(name, city, country, code);
    }

    // Método genérico para añadir vértices
    private <T> boolean addVertexGeneric(T content) {
        if (content == null) {
            return false;
        }
        return grafo.addVertex((Aeropuerto) content);
    }

    @FXML
    private void handleDeleteAirport() {
        Aeropuerto selected = deleteAirportComboBox.getValue();
        if (selected == null) {
            showAlert("Error", "Seleccione un aeropuerto para eliminar");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmar Eliminación");
        confirmation.setHeaderText("¿Está seguro de eliminar el aeropuerto?");
        confirmation.setContentText("Aeropuerto: " + selected.getCode() + " - " + selected.getName());

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (grafo.removeVertex(selected)) {
                // Eliminar del archivo
                if (FileManager.removeAirportFromFile(selected.getCode())) {
                    outputArea.appendText("✓ Aeropuerto eliminado: " + selected.getCode() + "\n");
                    updateAirportComboBoxes();
                } else {
                    showAlert("Error", "Aeropuerto eliminado del grafo pero no se pudo eliminar del archivo");
                }
            } else {
                showAlert("Error", "No se pudo eliminar el aeropuerto");
            }
        }
    }

    // Métodos para gestión de vuelos
    @FXML
    private void handleAddFlight() {
        try {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Añadir Vuelo");
            dialog.setHeaderText("Ingrese los datos del nuevo vuelo");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddFlightDialog.fxml"));
            dialog.getDialogPane().setContent(loader.load());

            AddFlightDialogController controller = loader.getController();
            controller.setAirports(grafo.getAllAirports());

            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Aeropuerto origen = controller.getOrigen();
                Aeropuerto destino = controller.getDestino();
                String airline = controller.getAirline();
                String flightNumber = controller.getFlightNumber();
                int distance = controller.getDistance();

                if (origen == null || destino == null || airline.isEmpty() || flightNumber.isEmpty() || distance <= 0) {
                    showAlert("Error", "Todos los campos son obligatorios y la distancia debe ser mayor a 0");
                    return;
                }

                if (origen.equals(destino)) {
                    showAlert("Error", "El origen y destino no pueden ser iguales");
                    return;
                }

                // Crear vuelo
                Vuelo nuevoVuelo = createFlight(airline, flightNumber, distance);

                // Conectar en el grafo
                boolean exito = connectCompleteGeneric(origen, destino, distance, nuevoVuelo);

                if (exito) {
                    // Añadir al archivo
                    if (FileManager.addFlightToFile(origen, destino, nuevoVuelo)) {
                        outputArea.appendText("✓ Vuelo añadido: " + origen.getCode() + " → "
                                + destino.getCode() + " | " + airline + " " + flightNumber + "\n");
                    } else {
                        showAlert("Error", "Vuelo añadido al grafo pero no se pudo guardar en el archivo");
                    }
                } else {
                    showAlert("Error", "No se pudo añadir el vuelo (¿ya existe?)");
                }
            }
        } catch (IOException e) {
            showAlert("Error", "No se pudo cargar el diálogo: " + e.getMessage());
        }
    }

    // Método factory para crear vuelos (genérico)
    private Vuelo createFlight(String airline, String flightNumber, int distance) {
        return new Vuelo(airline, flightNumber, (float) distance);
    }

    // Método genérico para conectar vértices
    private <T, U> boolean connectCompleteGeneric(T source, T target, int weight, U data) {
        if (source == null || target == null) {
            return false;
        }
        return grafo.connectComplete((Aeropuerto) source, (Aeropuerto) target, weight, (Vuelo) data);
    }

    @FXML
    private void handleEditFlight() {
        try {
            // 1. Cargar diálogo de selección
            FXMLLoader selectLoader = new FXMLLoader(getClass().getResource("SelectFlightDialog.fxml"));
            Parent selectContent = selectLoader.load();

            SelectFlightDialogController selectController = selectLoader.getController();
            selectController.setAirports(grafo.getAllAirports());
            selectController.setGraph(grafo);

            // 2. Crear diálogo
            Dialog<ButtonType> selectDialog = new Dialog<>();
            selectDialog.setTitle("Seleccionar Vuelo a Editar");
            selectDialog.setHeaderText("Seleccione el vuelo que desea editar");
            selectDialog.getDialogPane().setContent(selectContent);
            selectDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            Optional<ButtonType> selectResult = selectDialog.showAndWait();
            if (selectResult.isPresent() && selectResult.get() == ButtonType.OK) {
                Aeropuerto origenSeleccionado = selectController.getOrigen();
                Aeropuerto destinoSeleccionado = selectController.getDestino();
                Vuelo vueloSeleccionado = selectController.getVuelo();

                if (origenSeleccionado == null || destinoSeleccionado == null || vueloSeleccionado == null) {
                    showAlert("Error", "Debe seleccionar origen, destino y vuelo específico");
                    return;
                }

                // Cargar diálogo de edición
                FXMLLoader editLoader = new FXMLLoader(getClass().getResource("EditFlightDialog.fxml"));
                Dialog<ButtonType> editDialog = new Dialog<>();
                editDialog.setTitle("Editar Vuelo");
                editDialog.setHeaderText("Edite los datos del vuelo");
                editDialog.getDialogPane().setContent(editLoader.load());

                EditFlightDialogController editController = editLoader.getController();
                editController.setAirports(grafo.getAllAirports());
                editController.loadFlightData(origenSeleccionado, destinoSeleccionado, vueloSeleccionado);

                editDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                Optional<ButtonType> editResult = editDialog.showAndWait();
                if (editResult.isPresent() && editResult.get() == ButtonType.OK) {
                    // Validar datos editados
                    if (!editController.isValid()) {
                        showAlert("Error", editController.getValidationMessage());
                        return;
                    }

                    // Obtener nuevos valores
                    Aeropuerto nuevoOrigen = editController.getOrigen();
                    Aeropuerto nuevoDestino = editController.getDestino();
                    String nuevaAerolinea = editController.getAirline();
                    String nuevoNumVuelo = editController.getFlightNumber();
                    int nuevaDistancia = editController.getDistance();

                    // Verificar si cambió el origen/destino (necesita eliminar y recrear)
                    if (!origenSeleccionado.equals(nuevoOrigen) || !destinoSeleccionado.equals(nuevoDestino)) {
                        // Eliminar vuelo antiguo
                        grafo.removeFlight(origenSeleccionado, destinoSeleccionado, vueloSeleccionado.getNum_vuelo());

                        // Crear nuevo vuelo
                        Vuelo vueloEditado = new Vuelo(nuevaAerolinea, nuevoNumVuelo, (float) nuevaDistancia);

                        // Añadir nueva conexión
                        grafo.connectComplete(nuevoOrigen, nuevoDestino, nuevaDistancia, vueloEditado);

                        // Actualizar archivos
                        FileManager.removeFlightFromFile(origenSeleccionado, destinoSeleccionado, vueloSeleccionado.getNum_vuelo());
                        FileManager.addFlightToFile(nuevoOrigen, nuevoDestino, vueloEditado);

                    } else {
                        // Solo actualizar datos del vuelo (mismo origen y destino)
                        Vuelo vueloEditado = new Vuelo(nuevaAerolinea, nuevoNumVuelo, (float) nuevaDistancia);

                        // Necesitas implementar este método en GraphAL
                        grafo.updateFlightData(origenSeleccionado, destinoSeleccionado,
                                vueloSeleccionado.getNum_vuelo(), vueloEditado);

                        // Actualizar archivo
                        FileManager.updateFlightInFile(origenSeleccionado, destinoSeleccionado,
                                vueloSeleccionado.getNum_vuelo(), vueloEditado);
                    }

                    outputArea.appendText("✓ Vuelo editado exitosamente: " + nuevaAerolinea + " " + nuevoNumVuelo + "\n");
                }
            }

        } catch (IOException e) {
            showAlert("Error", "No se pudo cargar el diálogo: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            showAlert("Error", "Error al editar el vuelo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteFlight() {
        try {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Eliminar Vuelo");
            dialog.setHeaderText("Seleccione el vuelo a eliminar");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("DeleteFlightDialog.fxml"));
            dialog.getDialogPane().setContent(loader.load());

            DeleteFlightDialogController controller = loader.getController();
            controller.setAirports(grafo.getAllAirports());
            controller.setGraph(grafo); // Pasar la referencia del grafo

            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Aeropuerto origen = controller.getOrigen();
                Aeropuerto destino = controller.getDestino();
                Vuelo vuelo = controller.getVuelo(); // Obtener el vuelo específico

                if (origen == null || destino == null || vuelo == null) {
                    showAlert("Error", "Seleccione origen, destino y vuelo específico");
                    return;
                }

                // Eliminar usando el número de vuelo específico
                if (grafo.removeFlight(origen, destino, vuelo.getNum_vuelo())) {
                    // Eliminar del archivo usando el número de vuelo
                    if (FileManager.removeFlightFromFile(origen, destino, vuelo.getNum_vuelo())) {
                        outputArea.appendText("✓ Vuelo eliminado: " + origen.getCode() + " → "
                                + destino.getCode() + " | " + vuelo.getNum_vuelo() + "\n");
                    } else {
                        showAlert("Error", "Vuelo eliminado del grafo pero no se pudo eliminar del archivo");
                    }
                } else {
                    showAlert("Error", "No se encontró el vuelo especificado");
                }
            }
        } catch (IOException e) {
            showAlert("Error", "No se pudo cargar el diálogo: " + e.getMessage());
        }
    }

    //------------------
    @FXML
    private void handleClearOutput() {
        outputArea.clear();
        statusLabel.setText("Salida limpiada - Listo para nuevas operaciones");
        statusLabel.setStyle("-fx-text-fill: #f39c12;");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public GraphAL<Aeropuerto, Vuelo> getGrafo() {
        return grafo;
    }

    // Método para verificar tipos en tiempo de ejecución
    private boolean isValidType(Object obj, Class<?> expectedClass) {
        return obj != null && expectedClass.isInstance(obj);
    }

    @FXML
    private void handleExportTXT() {
        Aeropuerto origen = origenComboBox.getValue();
        Aeropuerto destino = destinoComboBox.getValue();

        if (origen == null || destino == null) {
            showAlert("Error", "Seleccione origen y destino primero");
            return;
        }

        try {
            LinkedList<Aeropuerto> rutaOptima = grafo.findShortestPath(origen, destino);
            int totalDistance = calcularDistanciaTotal(rutaOptima);

            if (rutaOptima.isEmpty()) {
                showAlert("Error", "No existe ruta entre estos aeropuertos");
                return;
            }

            if (ExportManager.exportRutaToTXT(rutaOptima, totalDistance, origen, destino)) {
                outputArea.appendText("✓ Ruta exportada a TXT correctamente\n");
                showAlert("Éxito", "Ruta exportada a archivo TXT");
            }
        } catch (Exception e) {
            showAlert("Error", "No se pudo exportar: " + e.getMessage());
        }
    }

    @FXML
    private void handleExportCSV() {
        Aeropuerto origen = origenComboBox.getValue();
        Aeropuerto destino = destinoComboBox.getValue();

        if (origen == null || destino == null) {
            showAlert("Error", "Seleccione origen y destino primero");
            return;
        }

        try {
            LinkedList<Aeropuerto> rutaOptima = grafo.findShortestPath(origen, destino);
            int totalDistance = calcularDistanciaTotal(rutaOptima);

            if (rutaOptima.isEmpty()) {
                showAlert("Error", "No existe ruta entre estos aeropuertos");
                return;
            }

            if (ExportManager.exportRutaToCSV(rutaOptima, totalDistance, origen, destino)) {
                outputArea.appendText("✓ Ruta exportada a CSV correctamente\n");
                showAlert("Éxito", "Ruta exportada a archivo CSV");
            } else {
                showAlert("Error", "No se pudo exportar a CSV");
            }
        } catch (Exception e) {
            showAlert("Error", "No se pudo exportar: " + e.getMessage());
        }
    }

    @FXML
    private void handleExportHTML() {
        Aeropuerto origen = origenComboBox.getValue();
        Aeropuerto destino = destinoComboBox.getValue();

        if (origen == null || destino == null) {
            showAlert("Error", "Seleccione origen y destino primero");
            return;
        }

        try {
            LinkedList<Aeropuerto> rutaOptima = grafo.findShortestPath(origen, destino);
            int totalDistance = calcularDistanciaTotal(rutaOptima);

            if (rutaOptima.isEmpty()) {
                showAlert("Error", "No existe ruta entre estos aeropuertos");
                return;
            }

            if (ExportManager.exportRutaToHTML(rutaOptima, totalDistance, origen, destino)) {
                outputArea.appendText("✓ Ruta exportada a HTML correctamente\n");
                showAlert("Éxito", "Ruta exportada a archivo HTML");
            } else {
                showAlert("Error", "No se pudo exportar a HTML");
            }
        } catch (Exception e) {
            showAlert("Error", "No se pudo exportar: " + e.getMessage());
        }
    }

    // Métodos similares para handleExportCSV() y handleExportHTML()
    private int calcularDistanciaTotal(LinkedList<Aeropuerto> ruta) {
        int total = 0;
        for (int i = 0; i < ruta.size() - 1; i++) {
            total += grafo.getDistanceBetween(ruta.get(i), ruta.get(i + 1));
        }
        return total;
    }

    @FXML
    private void handleAirlineAnalysis() {
        String selectedAirline = airlineComboBox.getValue();

        if (selectedAirline == null || selectedAirline.isEmpty()) {
            showAlert("Error", "Seleccione una aerolínea para analizar");
            return;
        }

        try {
            // Crear ventana de análisis
            Stage analysisStage = new Stage();
            analysisStage.setTitle("Análisis de Aerolínea: " + selectedAirline);

            VBox root = new VBox(10);
            root.setPadding(new javafx.geometry.Insets(10));

            Label titleLabel = new Label("Vuelos de " + selectedAirline);
            titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

            TextArea analysisArea = new TextArea();
            analysisArea.setEditable(false);
            analysisArea.setWrapText(true);
            analysisArea.setPrefSize(600, 400);

            // Obtener y mostrar estadísticas
            LinkedList<Vuelo> airlineFlights = grafo.getFlightsByAirline(selectedAirline);
            Map<Aeropuerto, Integer> airportStats = grafo.getAirlineAirportStats(selectedAirline);

            analysisArea.appendText("=== ESTADÍSTICAS DE " + selectedAirline.toUpperCase() + " ===\n\n");
            analysisArea.appendText("Total de vuelos: " + airlineFlights.size() + "\n");
            analysisArea.appendText("Aeropuertos que opera: " + airportStats.size() + "\n\n");

            analysisArea.appendText("DISTRIBUCIÓN POR AEROPUERTO:\n");
            analysisArea.appendText(String.format("%-5s %-30s %-10s\n", "CÓD", "AEROPUERTO", "VUELOS"));
            analysisArea.appendText("--------------------------------------------\n");

            for (Map.Entry<Aeropuerto, Integer> entry : airportStats.entrySet()) {
                Aeropuerto airport = entry.getKey();
                analysisArea.appendText(String.format("%-5s %-30s %-10d\n",
                        airport.getCode(), airport.getName(), entry.getValue()));
            }

            analysisArea.appendText("\nDETALLE DE VUELOS:\n");
            analysisArea.appendText(String.format("%-8s %-5s → %-5s %-12s %-10s\n",
                    "VUELO", "ORIG", "DEST", "DISTANCIA", "AEROPUERTO DESTINO"));
            analysisArea.appendText("------------------------------------------------------------\n");

            for (Vuelo vuelo : airlineFlights) {
                // Necesitarías un método en GraphAL para obtener la ruta del vuelo
                String routeInfo = grafo.getFlightRouteInfo(vuelo);
                analysisArea.appendText(String.format("%-8s %s\n",
                        vuelo.getNum_vuelo(), routeInfo));
            }

            Button exportButton = new Button("Exportar Reporte");
            exportButton.setOnAction(e -> {
                boolean success = ExportManager.exportAirlineReport(selectedAirline, airlineFlights, airportStats);
                if (success) {
                    showAlert("Éxito", "Reporte exportado correctamente");
                }
            });

            Button closeButton = new Button("Cerrar");
            closeButton.setOnAction(e -> analysisStage.close());

            HBox buttonBox = new HBox(10, exportButton, closeButton);

            root.getChildren().addAll(titleLabel, analysisArea, buttonBox);

            Scene scene = new Scene(root, 650, 500);
            analysisStage.setScene(scene);
            analysisStage.initModality(Modality.WINDOW_MODAL);
            analysisStage.initOwner(primaryStage);
            analysisStage.show();

        } catch (Exception e) {
            showAlert("Error", "No se pudo realizar el análisis: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateAirlineComboBox() {
        try {
            LinkedList<String> airlines = grafo.getAllAirlines();
            airlineComboBox.getItems().setAll(airlines);
        } catch (Exception e) {
            outputArea.appendText("Error al cargar aerolíneas: " + e.getMessage() + "\n");
        }
    }

}
