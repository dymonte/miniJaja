package fr.umlp.m1info.projectvms.group2.controller;

import fr.umlp.m1info.projectvms.group2.logger.InterpreterLogger;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class ConsoleSectionController {

	@FXML
	private TabPane tabPane;

	@FXML
	private Tab outputTab;

	@FXML
	private Tab debugTab;

	@FXML
	private Tab errorTab;

	@FXML
	private TextArea outputArea;

	@FXML
	private TextArea debugArea;

	@FXML
	private TextArea errorArea;

	@FXML
	public void initialize() {
		InterpreterLogger.getInstance().addListener((message, messageLevel) -> {
			switch (messageLevel) {
				case WRITE:
					appendToOutput(message);
					break;
				case ERROR:
					appendToError(message);
					break;
				case DEBUG:
					appendToDebug(message);
					break;
				default:
					break;
			}
		});
	}

	public void appendToOutput(String msg) {
		outputArea.appendText(msg);
	}

	public void appendToDebug(String msg) {
		debugArea.appendText(msg + "\n");
	}

	public void appendToError(String msg) {
		errorArea.appendText(msg + "\n");
		tabPane.getSelectionModel().select(errorTab);
	}
}
