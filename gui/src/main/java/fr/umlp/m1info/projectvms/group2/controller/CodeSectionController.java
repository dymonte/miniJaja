package fr.umlp.m1info.projectvms.group2.controller;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import fr.umlp.m1info.projectvms.group2.AppState;

import javafx.fxml.FXML;

public class CodeSectionController implements StatefulController {

	@FXML
	private CodeArea codeArea;

	private Runnable onSaveRequested;

	@Override
	public void bindState(AppState appState) {
		codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
		codeArea.getStyleClass().add("code-area");

		// link appState.codeProperty and codeArea.textProperty
		appState.codeProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue != null && !newValue.equals(codeArea.getText())) {
				codeArea.replaceText(newValue);
			}
		});

		codeArea.textProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue != null && !newValue.equals(appState.codeProperty().get())) {
				appState.codeProperty().set(newValue);
			}
		});

		codeArea.replaceText(appState.codeProperty().get());

		// handle save shortcut (ctrl+s)
		codeArea.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
			if (new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN).match(e)) {
				if (onSaveRequested != null) {
					onSaveRequested.run();
				}
				e.consume();
			}
		});
	}

	@FXML
	public void undo() {
		codeArea.undo();
	}

	@FXML
	public void redo() {
		codeArea.redo();
	}

	@FXML
	public void cut() {
		codeArea.cut();
	}

	@FXML
	public void copy() {
		codeArea.copy();
	}

	@FXML
	public void paste() {
		codeArea.paste();
	}

	@FXML
	public void delete() {
		codeArea.replaceSelection("");
	}

	@FXML
	public void selectAll() {
		codeArea.selectAll();
	}

	@FXML
	public void unSelectAll() {
		codeArea.deselect();
	}

	public void setOnSaveRequested(Runnable callback) {
		this.onSaveRequested = callback;
	}
}
