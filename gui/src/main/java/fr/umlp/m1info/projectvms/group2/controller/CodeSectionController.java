package fr.umlp.m1info.projectvms.group2.controller;
import fr.umlp.m1info.projectvms.group2.logger.InterpreterLogger;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import fr.umlp.m1info.projectvms.group2.AppState;

import javafx.fxml.FXML;

import javafx.scene.control.TextArea;

public class CodeSectionController implements StatefulController {

    @FXML
    private TextArea executionTextArea;

    @FXML
    private CodeArea codeArea;

    private Runnable onSaveRequested;

    @FXML
    public void initialize() {
        InterpreterLogger.getInstance().addListener((message, level) ->
                Platform.runLater(() ->
                        executionTextArea.appendText("[" + level + "] " + message + "\n")
                )
        );
    }


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

    public void setOnSaveRequested(Runnable callback) {
        this.onSaveRequested = callback;
    }
}

