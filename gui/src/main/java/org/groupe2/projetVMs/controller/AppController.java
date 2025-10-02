package org.groupe2.projetVMs.controller;

import org.groupe2.projetVMs.AppState;

import javafx.fxml.FXML;

/**
 * AppController
 */
public class AppController implements StatefulController {

    @FXML
    private CodeSectionController codeSectionController;

    @Override
    public void bindState(AppState appState) {
        // set App state to all controllers
        codeSectionController.bindState(appState);
    }
}
