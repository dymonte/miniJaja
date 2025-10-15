package org.groupe2.projetVMs.controller;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
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
    
    private Stage primaryStage;

    private AppState state;

    @Override
    public void bindState(AppState appState) {
        // set App state to all controllers
        this.state = appState;
        codeSectionController.bindState(appState);
        codeSectionController.setOnSaveRequested(this::saveFile);
    }

    /**
     * Opens a file
     * @return the file chosen by the user
     */
    private File chooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Code File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("miniJaja codes", "*.mjj"));
        return fileChooser.showOpenDialog(null);
    }

    /**
     * Opens a file and updates the code section
     * @param file the file to open
     */
    private void openFile(File file) {
        if (file != null) {
            if (file.isDirectory()) {
                openFolder(file);
                return;
            }
            updateWindowTitle(file.getName());
            try {
                String content = Files.readString(file.toPath(), StandardCharsets.UTF_8);
                if (state != null) {
                    state.setOpenedFile(file);
                    state.codeProperty().set(content);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            openFolder(file.getParentFile());
        }
    }

    @FXML
    private File createFile() {
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
        if (state.getOpenedFile().isPresent()) openFile(file);
        return file;
    }

    /**
     * Opens a file and updates the code section
     * Called by the button
     */
    @FXML
    private void openFile() {
        File file = chooseFile();
        openFile(file);
    }

    /**
     * Opens a folder and updates the file tree
     * Called by the button
     */
    @FXML
    private void openFolder() {
        File file = chooseDirectory();
        openFolder(file);
    }

    /**
     * Opens a folder and updates the file tree
     * @param file the folder to open
     */
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
                parent_button.setMaxWidth(Double.MAX_VALUE);
                parent_button.prefWidthProperty().bind(fileTree.widthProperty());
                parent_button.setOnAction(event -> openFolder(file.getParentFile()));
                HBox.setHgrow(parent_button, Priority.ALWAYS);
                fileTree.getChildren().add(parent_button);
            }
            for (File f : files) {
                Button file_button = new Button(f.getName());
                file_button.setMaxWidth(Double.MAX_VALUE);
                file_button.prefWidthProperty().bind(fileTree.widthProperty());
                file_button.setOnAction(event -> openFile(f));
                HBox.setHgrow(file_button, Priority.ALWAYS);
                fileTree.getChildren().add(file_button);
            }
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Lets the user choose a directory
     * @return the directory chosen by the user
     */
    private File chooseDirectory() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Open Folder");
        return chooser.showDialog(null);
    }

    /**
     * Saves the current file
     * Called by the button or crtl+S
     */
    @FXML
    private void saveFile() {
        File file;
        if(state.getOpenedFile().isEmpty()){
            System.out.println("No file opened");
            file = createFile();
            openFile(file);
        }
        else {
            file = state.getOpenedFile().get();
        }
        try{ // try to save the file
            Files.writeString(
                file.toPath(),
                state.codeProperty().get(),
                StandardCharsets.UTF_8
            );
            System.out.println("File saved successfully");
        } catch (IOException e) {
            System.err.println("Error saving file: " + e.getMessage());
            e.printStackTrace(); // TODO popup error message
        }
    }


    /**
     * Change the window name
     * @param newTitle new window name
     */
    public void updateWindowTitle(String newTitle) {
        if (primaryStage != null) {
            primaryStage.setTitle(newTitle);
        }
    }
    
    /**
     * Sets the primary stage for this controller
     * @param stage the primary stage
     */
    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }
}
