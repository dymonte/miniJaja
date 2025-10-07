package org.groupe2.projetVMs.controller;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import org.groupe2.projetVMs.AppState;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class CodeSectionController implements StatefulController {

    @FXML
    private TextArea codeArea;

    private Runnable onSaveRequested;

    @Override
    public void bindState(AppState appState) {
        codeArea.textProperty().bindBidirectional(appState.codeProperty());
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
