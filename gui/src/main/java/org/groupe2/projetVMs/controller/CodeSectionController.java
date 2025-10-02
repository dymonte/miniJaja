package org.groupe2.projetVMs.controller;

import org.groupe2.projetVMs.AppState;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class CodeSectionController implements StatefulController {

    @FXML
    private TextArea codeArea;

    @Override
    public void bindState(AppState appState) {
        codeArea.textProperty().bindBidirectional(appState.codeProperty());
    }

}
