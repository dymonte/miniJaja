package fr.umlp.m1info.projectvms.group2.controller;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.HashSet;
import java.util.Set;
import java.util.function.IntFunction;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import fr.umlp.m1info.projectvms.group2.AppState;

import javafx.fxml.FXML;

public class CodeSectionController implements StatefulController {

	@FXML
	private CodeArea codeArea;

	private Runnable onSaveRequested;

	private final Set<Integer> breakpoints = new HashSet<>();

	@Override
	public void bindState(AppState appState) {
		IntFunction<javafx.scene.Node> numberFactory = lineNumberFactory();
		codeArea.setParagraphGraphicFactory(numberFactory);
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

	private IntFunction<javafx.scene.Node> lineNumberFactory() {
		IntFunction<javafx.scene.Node> baseFactory = LineNumberFactory.get(codeArea);

		return line -> {
			javafx.scene.Node lineNumber = baseFactory.apply(line);

			Circle marker = new Circle(4);
			marker.setFill(breakpoints.contains(line + 1) ? Color.RED : Color.TRANSPARENT);
			marker.setOnMouseClicked(e -> toggleBreakpoint(line + 1));

			HBox box = new HBox(5, marker, lineNumber);
			box.setOnMouseClicked(e -> toggleBreakpoint(line + 1));
			return box;
		};
	}

	private void toggleBreakpoint(int line) {
		if (breakpoints.contains(line)) {
			breakpoints.remove(line);
		} else {
			breakpoints.add(line);
		}
		codeArea.setParagraphGraphicFactory(lineNumberFactory());
	}

	public Set<Integer> getBreakpoints() {
		return breakpoints;
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
