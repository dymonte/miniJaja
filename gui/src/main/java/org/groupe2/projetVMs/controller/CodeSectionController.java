package org.groupe2.projetVMs.controller;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.groupe2.projetVMs.AppState;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class CodeSectionController implements StatefulController {

    @FXML
    private CodeArea codeArea;

    private Runnable onSaveRequested;

    @Override
    public void bindState(AppState appState) {
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        codeArea.getStyleClass().add("code-area");

        // Lier la propriété text du CodeArea à la propriété code de l'AppState
        appState.codeProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals(codeArea.getText())) {
                codeArea.replaceText(newValue);
            }
        });
        
        // Mettre à jour la propriété code de l'AppState lorsque le texte change
        codeArea.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals(appState.codeProperty().get())) {
                appState.codeProperty().set(newValue);
            }
        });
        
        // Initialiser la valeur
        codeArea.replaceText(appState.codeProperty().get());
        
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
