package fr.umlp.m1info.projectvms.group2.controller;

import fr.umlp.m1info.projectvms.group2.AppState;
import fr.umlp.m1info.projectvms.group2.styler.JajaCodeStyler;
import fr.umlp.m1info.projectvms.group2.styler.MiniJajaStyler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.reactfx.Subscription;

import java.io.File;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class CodeSectionController implements StatefulController {

	public enum LanguageType {
		MINIJAJA, JAJACODE
	}

	private static final String SKIP = "(?:\\s|//.*|/\\*(?:.|\\R)*?\\*/)*";
	private static final Pattern JAJACODE_START_PATTERN =
			Pattern.compile("^" + SKIP + "init", Pattern.MULTILINE | Pattern.DOTALL);
	private static final Pattern MINIJAJA_START_PATTERN =
			Pattern.compile("^" + SKIP + "class", Pattern.MULTILINE | Pattern.DOTALL);

	private AppState appState;

	@FXML
	private CodeArea codeArea;

	@FXML
	private HBox buttonBar;

	private Runnable onSaveRequested;

	private int highlightedLine;

	private final Set<Integer> breakpoints = new HashSet<>();

	@FXML
	public void initialize() {
		Subscription cleanupWhenNoLongerNeedIt = codeArea
				.multiPlainChanges()
				.successionEnds(Duration.ofMillis(500))
				.subscribe(ignore -> {
					// Recoloration sur *tout* le document
					highlightCode();
				});
	}

	private void highlightCode() {
		String text = codeArea.getText();
		LanguageType type = detectLanguage();
		if (type == LanguageType.MINIJAJA) {
			codeArea.setStyleSpans(0, new MiniJajaStyler(text).style());
		} else {
			codeArea.setStyleSpans(0, new JajaCodeStyler(text).style());
		}
	}


	@Override
	public void bindState(AppState appState) {
		this.appState = appState;
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

	public HBox getButtonBar() {
		return buttonBar;
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
			lineNumber.getStyleClass().add("line-number");

			if (line + 1 == highlightedLine && lineNumber instanceof Label label) {
				label.setStyle(label.getStyle() + "; -fx-text-fill: red;");
			}

			Circle marker = new Circle(4);
			marker.setFill(breakpoints.contains(line + 1) ? Color.RED : Color.TRANSPARENT);
			marker.setOnMouseClicked(e -> toggleBreakpoint(line + 1));

			HBox box = new HBox(5, marker, lineNumber);
			box.getStyleClass().add("line-number-box");
			box.setOnMouseClicked(e -> toggleBreakpoint(line + 1));

			return box;
		});
	}

	private LanguageType detectLanguage() {
		File openedFile = appState.getOpenedFile().orElse(null);

		// File-based detection
		if (openedFile != null) {
			String name = openedFile.getName();
			if (name.endsWith(".mjj")) return LanguageType.MINIJAJA;
			if (name.endsWith(".jjc")) return LanguageType.JAJACODE;
		}

		// Fallback: text-based detection
		String text = codeArea.getText();
		if (MINIJAJA_START_PATTERN.matcher(text).find()) return LanguageType.MINIJAJA;
		if (JAJACODE_START_PATTERN.matcher(text).find()) return LanguageType.JAJACODE;

		// Default
		return LanguageType.MINIJAJA;
	}
}
