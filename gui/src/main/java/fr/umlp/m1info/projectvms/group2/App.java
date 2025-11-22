package fr.umlp.m1info.projectvms.group2;

import fr.umlp.m1info.projectvms.group2.styler.MiniJajaStyler;

import java.time.Duration;
import java.util.stream.Collectors;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.model.TwoDimensional.Bias;
import org.reactfx.Subscription;

import fr.umlp.m1info.projectvms.group2.controller.AppController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.IndexRange;

/**
 * Entry point of the project GUI
 *
 */
public class App extends Application {

	private static final int DEFAULT_SCREEN_WIDTH = 900;
	private static final int DEFAULT_SCREEN_HEIGHT = 600;

	@Override
	public void start(Stage stage) throws Exception {
		AppState state = new AppState();


		FXMLLoader loader = new FXMLLoader(getClass().getResource("App.fxml"));
		Scene scene = new Scene(loader.load(), DEFAULT_SCREEN_WIDTH, DEFAULT_SCREEN_HEIGHT);

		scene.getStylesheets().add(
			getClass().getResource("styles/base.css").toExternalForm()
		);
		scene.getStylesheets().add(
			getClass().getResource("styles/themes/theme-blue-light.css").toExternalForm()
		);

		AppController controller = loader.getController();
		controller.bindState(state);
		controller.setPrimaryStage(stage);

		// Set initial title
		controller.updateWindowTitle("MiniJaja IDE");

		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
