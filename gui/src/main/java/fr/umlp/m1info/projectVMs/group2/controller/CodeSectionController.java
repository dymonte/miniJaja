package fr.umlp.m1info.projectVMs.group2.controller;

import fr.umlp.m1info.projectVMs.group2.AppState;

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
