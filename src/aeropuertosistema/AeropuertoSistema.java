package aeropuertosistema;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AeropuertoSistema extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Cargar el archivo FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AeropuertoLoaderUI.fxml"));
        Parent root = loader.load();
        
        AeropuertoLoaderController controller = loader.getController();
        controller.setPrimaryStage(primaryStage);
        
        Scene scene = new Scene(root, 900, 700);
        
        // Configurar el stage
        primaryStage.setTitle("Sistema de Gestión de Aeropuertos");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}