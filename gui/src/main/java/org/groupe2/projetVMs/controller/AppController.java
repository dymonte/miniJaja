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
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("miniJaja codes", "*.mjj"));
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void createFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("New File");
        fileChooser.setInitialFileName("untitled.mjj");
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
