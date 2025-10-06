package fr.umlp.m1info.projectvms.group2;

import javafx.stage.FileChooser;
import fr.umlp.m1info.projectvms.group2.controller.AppController;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.File;

/**
 * Entry point of the project GUI
 *
 */
public class App extends Application {

    public static int DEFAULT_SCREEN_WIDTH = 900;
    public static int DEFAULT_SCREEN_HEIGHT = 600;

    @Override
    public void start(Stage stage) throws Exception {
        AppState state = new AppState();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("App.fxml"));
        Scene scene = new Scene(loader.load(), DEFAULT_SCREEN_WIDTH, DEFAULT_SCREEN_HEIGHT);

        AppController controller = loader.getController();
        controller.bindState(state);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}