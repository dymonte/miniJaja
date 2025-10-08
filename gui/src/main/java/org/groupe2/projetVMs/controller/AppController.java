package org.groupe2.projetVMs.controller;

import javafx.scene.control.Button;
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

    private void openFile(File file) {
        if (file != null) {
            if (file.isDirectory()) {
                openFolder(file);
            }
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
    private void openFile() {
        File file = chooseFile();
        openFile(file);
    }

    @FXML
    private void openFolder() {
        File file = chooseDirectory();
        openFolder(file);
    }

    private void openFolder(File file) {
        state.setWorkspaceDir(file);
        try {
            File[] files = Files.list(file.toPath())
                    .map(Path::toFile)
                    .toArray(File[]::new);
            state.setWorkspaceFiles(files);
            fileTree.getChildren().clear();
            if (!file.equals(File.listRoots()[0])) {
                Button parent_button = new Button("..");
                parent_button.setOnAction(event -> openFolder(file.getParentFile()));
                fileTree.getChildren().add(parent_button);
            }
            for (File f : files) {
                Button file_button = new Button(f.getName());
                file_button.setOnAction(event -> openFile(f));
                fileTree.getChildren().add(file_button);
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
