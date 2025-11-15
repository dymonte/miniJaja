package fr.umlp.m1info.projectvms.group2.controller;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.HashSet;
import java.util.Set;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import fr.umlp.m1info.projectvms.group2.AppState;

import javafx.fxml.FXML;

public class CodeSectionController implements StatefulController {

	@FXML
	private CodeArea codeArea;

	private Runnable onSaveRequested;

	private int highlightedLine;

	private final Set<Integer> breakpoints = new HashSet<>();

	@Override
	public void bindState(AppState appState) {
		applyLineNumberFactory();
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

	private void toggleBreakpoint(int line) {
		if (breakpoints.contains(line)) {
			breakpoints.remove(line);
		} else {
			breakpoints.add(line);
		}
		applyLineNumberFactory();
	}

	public Set<Integer> getBreakpoints() {
		return breakpoints;
	}

	public void removeBreakpoint(int line) {
		breakpoints.remove(line);
		applyLineNumberFactory();
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

	public void highlightLine(int lineNumber) {
		highlightedLine = lineNumber;
		applyLineNumberFactory();
	}

	@FXML
	private void applyLineNumberFactory() {
		codeArea.setParagraphGraphicFactory(line -> {
			Node lineNumber = LineNumberFactory.get(codeArea).apply(line);

			if (line + 1 == highlightedLine && lineNumber instanceof Label label) {
				label.setStyle(label.getStyle() + "; -fx-text-fill: red;");
			}

			Circle marker = new Circle(4);
			marker.setFill(breakpoints.contains(line + 1) ? Color.RED : Color.TRANSPARENT);
			marker.setOnMouseClicked(e -> toggleBreakpoint(line + 1));

			HBox box = new HBox(5, marker, lineNumber);
			box.setOnMouseClicked(e -> toggleBreakpoint(line + 1));

			return box;
		});
	}
}
