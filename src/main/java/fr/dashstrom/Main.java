package fr.dashstrom;

import fr.dashstrom.controller.Images;
import fr.dashstrom.utils.ResourceContainer;
import fr.dashstrom.utils.ResourceUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            ResourceContainer.loadAll();
            BorderPane root = FXMLLoader.load(ResourceUtils.getResourceURL("/view/view.fxml"));
            System.out.println("\n\nZ -> Up\n" +
                "Q -> Left\n" +
                "S -> Down\n" +
                "D -> Right\n" +
                "H -> Interact\n" +
                "J -> Attack with weapon 1\n" +
                "K -> Attack with weapon 2\n" +
                "L -> Attack with weapon 3\n" +
                "Escape or I -> Open/close Inventory\n"
            );
            Scene scene = new Scene(root);

            primaryStage.setScene(scene);
            primaryStage.setTitle("The Legend of Link");
            scene.getStylesheets().add(ResourceUtils.getResourceURL("/view/style/stylesheet.css").toExternalForm());
            Font.loadFont(ResourceUtils.getResourceURL("/view/font/Wantedo.ttf").toExternalForm(), 24);
            primaryStage.getIcons().add(Images.ICON.get());
            primaryStage.centerOnScreen();
            primaryStage.toFront();
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}