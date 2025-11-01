package fr.umlp.m1info.projectvms.group2;

import java.io.File;
import java.io.IOException;

import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HelpPopUp {

	public static void openHelpPopup(Stage parent, File file) {
		final Stage popup = new Stage();
		popup.initModality(Modality.APPLICATION_MODAL);
		popup.initOwner(parent);
		popup.setMinHeight(500);
		popup.setMinWidth(700);
		popup.setTitle("Help");
		WebView webView = new WebView();
		try {
			webView.getEngine().load(file.toURI().toURL().toString());
			popup.setScene(new Scene(webView));
			popup.show();
		} catch (IOException e) {
			PopUp.error("Help page", "Cannot open help page", "Error while opening help page");
		}
	}
}
