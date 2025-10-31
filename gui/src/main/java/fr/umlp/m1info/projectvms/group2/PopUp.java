package fr.umlp.m1info.projectvms.group2;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Utility class for showing pop-up dialogs
 * Static methods only
 */
public class PopUp {
	private PopUp() {
	}

	/**
	 * Show an information dialog
	 */
	public static void info(String title, String header, String content) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}

	/**
	 * Show a warning dialog
	 */
	public static void warning(String title, String header, String content) {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}

	/**
	 * Show a error dialog
	 */
	public static void error(String title, String header, String content) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}

	/**
	 * Show a warning dialog
	 */
	public static boolean confirm(String title, String message) {
		final boolean[] result = { false };
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.showAndWait().ifPresent(button -> result[0] = button == ButtonType.YES);
		return result[0];
	}

	/**
	 * Show a custom alert type dialog
	 */
	public static void custom(Alert.AlertType type, String title, String header, String content) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
}
