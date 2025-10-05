package org.groupe2.projetVMs.controller;

import javafx.stage.FileChooser;
import org.groupe2.projetVMs.AppState;

import javafx.fxml.FXML;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * AppController
 */
public class AppController implements StatefulController {

    @FXML
    private CodeSectionController codeSectionController;

    private AppState state;

    @Override
    public void bindState(AppState appState) {
        // set App state to all controllers
        this.state = appState;
        codeSectionController.bindState(appState);
    }

    private File chooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Code File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("miniJaja codes", "*.mjj")); //TODO: mettre la bonne extension de fichier
        return fileChooser.showOpenDialog(null);
    }

    @FXML
    private void openFile() {
        File file = chooseFile();
        if (file != null) {
            try {
                String content = Files.readString(file.toPath(), StandardCharsets.UTF_8);
                if (state != null) {
                    state.setOpenedFile(file);
                    // Optionally update code content in state
                    state.codeProperty().set(content);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
