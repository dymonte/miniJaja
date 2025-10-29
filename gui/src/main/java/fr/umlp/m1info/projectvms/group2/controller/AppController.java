package fr.umlp.m1info.projectvms.group2.controller;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

import fr.umlp.m1info.projectvms.group2.AppState;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.ASTNode;
import fr.umlp.m1info.projectvms.group2.exception.InterpretRuntimeException;
import fr.umlp.m1info.projectvms.group2.interpreter.minijaja.MiniJajaInterpreter;
import fr.umlp.m1info.projectvms.group2.memory.Memory;
import fr.umlp.m1info.projectvms.group2.memory.heap.Heap;
import fr.umlp.m1info.projectvms.group2.memory.stack.Stack;
import fr.umlp.m1info.projectvms.group2.memory.symtable.SymTable;
import fr.umlp.m1info.projectvms.group2.utils.ParserUtils;
import javafx.scene.Scene;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * AppController
 */
public class AppController implements StatefulController {

    @FXML
    private CodeSectionController codeSectionController;

    @FXML
    private MemorySectionController memorySectionController;

    @FXML
    private VBox fileTree;

    private Stage primaryStage;

    private AppState state;

    private Thread miniJajaInterpreterThread;

    @Override
    public void bindState(AppState appState) {
        // set App state to all controllers
        this.state = appState;
        codeSectionController.bindState(appState);
        codeSectionController.setOnSaveRequested(this::saveFile);
        memorySectionController.bindState(appState);
    }

    /**
     * Opens a file
     * 
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
     * 
     * @param file the file to open
     */
    private void openFile(File file) {
        if (file == null) {
            return;
        }

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
            // TODO: use a logger instead
            System.err.println("Error while opening file " + file.toPath());
        }
        openFolder(file.getParentFile());
    }

    /**
     * Create a file with "untilted.mjj as default name
     * Called by the button
     * 
     * @return the file created
     */
    @FXML
    private File createFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("New File");
        fileChooser.setInitialFileName("untitled.mjj");
        File file = fileChooser.showSaveDialog(null);
        if (file == null) {
            return null;
        }

        try {
            boolean created = file.createNewFile();
            if (created && state.getOpenedFile().isPresent()) {
                openFile(file);
            } else {
                // TODO: use a logger instead
                System.out.println(file.getAbsolutePath() + " already exists.");
            }
        } catch (IOException e) {
            System.out.println("Error while creating new file " + file.getAbsolutePath());
        }

        if (state.getOpenedFile().isPresent())
            openFile(file);
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
     * 
     * @param file the folder to open
     */
    private void openFolder(File file) {
        state.setWorkspaceDir(file);
        try (Stream<Path> stream = Files.list(file.toPath())) {
            File[] files = stream.map(Path::toFile).toArray(File[]::new);
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
        } catch (IOException e) {
            System.err.println("Error while opening folder " + file.getAbsolutePath());
        }
    }

    /**
     * Lets the user choose a directory
     * 
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
        Optional<File> openedFile = state.getOpenedFile();
        if (openedFile.isEmpty()) {
            System.out.println("No file opened");
            file = createFile();
            openFile(file);
        } else {
            file = openedFile.get();
        }

        if (file == null) {
            System.err.println("Error while trying to save file : file is null.");
            return;
        }

        try { // try to save the file
            Files.writeString(
                    file.toPath(),
                    state.codeProperty().get(),
                    StandardCharsets.UTF_8);
            System.out.println("File saved successfully");
        } catch (IOException e) {
            System.err.println("Error saving file: " + e.getMessage());
        }
    }

    /**
     * Closes the current file
     * Called by the button
     */
    @FXML
    private void closeCurrentFile() {
        state.closeOpenedFile();
        state.codeProperty().set("");
        updateWindowTitle("MiniJaja IDE");
    }

    /**
     * Change the window name
     * 
     * @param newTitle new window name
     */
    public void updateWindowTitle(String newTitle) {
        if (primaryStage != null) {
            primaryStage.setTitle(newTitle);
        }
    }

    /**
     * Opens help window
     */
    @FXML
    private void openHelpPopup() {
        final Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.initOwner(primaryStage);
        popup.setMinHeight(500);
        popup.setMinWidth(700);
        popup.setTitle("Help");
        WebView webView = new WebView();
        File file = new File("src/main/resources/fr/umlp/m1info/projectvms/group2/help_content.html");
        try {
            webView.getEngine().load(file.toURI().toURL().toString());
            popup.setScene(new Scene(webView));
            popup.show();
        } catch (IOException e) {
            //throw new RuntimeException(e);
            System.out.println(file.getAbsolutePath());

        }
    }

    /**
     * Runs the current file in a seperate thread
     */
    public void runCurrentFile() {
        saveFile();

        initMemory();
        memorySectionController.bindState(state);

        MiniJajaInterpreter interpreter = new MiniJajaInterpreter(state.getMemory());
        miniJajaInterpreterThread = new Thread(() -> {
            try {
                ASTNode programRoot = ParserUtils.parseString(state.codeProperty().get());
                interpreter.interpret(programRoot);
            } catch (InterpretRuntimeException e) {
                System.err.println("InterpretRuntimeException: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Error while parsing code");
            }
        });

        miniJajaInterpreterThread.start();
    }

    /**
     * Stops the interpretation
     */
    public void stopInterpretation() {
        if(miniJajaInterpreterThread.isAlive()) {
            miniJajaInterpreterThread.interrupt();
        } else {
            System.err.println("Error the interpretation is not running");
        }
    }

    /**
     * Initializes the memory
     */
    private void initMemory() {
        SymTable symTable = new SymTable(256);
        Stack stack = new Stack(1024);
        Heap heap = new Heap(1024);
        state.setMemory(new Memory(symTable, stack, heap));
    }

    /**
     * Sets the primary stage for this controller
     * 
     * @param stage the primary stage
     */
    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }
}
