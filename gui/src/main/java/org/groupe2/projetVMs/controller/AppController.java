package org.groupe2.projetVMs.controller;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.groupe2.projetVMs.AppState;

import javafx.fxml.FXML;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * AppController
 */
public class AppController implements StatefulController {

    @FXML
    private CodeSectionController codeSectionController;

    @FXML
    private VBox fileTree;

    private AppState state;

    @Override
    public void bindState(AppState appState) {
        // set App state to all controllers
        this.state = appState;
        codeSectionController.bindState(appState);
        codeSectionController.setOnSaveRequested(this::saveFile);
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
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void openFolder() {
        File file = chooseDirectory();
        state.setWorkspaceDir(file);
        try {
            File[] files = Files.list(file.toPath())
                .map(Path::toFile)
                .toArray(File[]::new);
            state.setWorkspaceFiles(files);
            fileTree.getChildren().clear();
            for (File f : files) {
                Label label = new Label(f.getName());
                fileTree.getChildren().add(label);
            }
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private File chooseDirectory() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Open Folder");
        return chooser.showDialog(null);
    }

    @FXML
    private void saveFile() {
        if(state.getOpenedFile().isEmpty()){
            System.err.println("Cannot save: no file opened");
            return;
        }
        try{
            Files.writeString(
                state.getOpenedFile().get().toPath(), 
                state.codeProperty().get(),
                StandardCharsets.UTF_8
            );
            System.out.println("File saved successfully");
        } catch (IOException e) {
            System.err.println("Error saving file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
