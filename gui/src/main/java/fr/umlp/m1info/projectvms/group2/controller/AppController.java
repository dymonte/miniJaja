package fr.umlp.m1info.projectvms.group2.controller;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Optional;

import fr.umlp.m1info.projectvms.group2.AppState;

import fr.umlp.m1info.projectvms.group2.PopUp;
import fr.umlp.m1info.projectvms.group2.HelpPopUp;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.ASTNode;
import fr.umlp.m1info.projectvms.group2.exception.InterpretRuntimeException;
import fr.umlp.m1info.projectvms.group2.interpreter.minijaja.MiniJajaInterpreterInteractive;
import fr.umlp.m1info.projectvms.group2.memory.Memory;
import fr.umlp.m1info.projectvms.group2.memory.heap.Heap;
import fr.umlp.m1info.projectvms.group2.memory.stack.Stack;
import fr.umlp.m1info.projectvms.group2.memory.symtable.SymTable;
import fr.umlp.m1info.projectvms.group2.utils.ParserUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * AppController
 */
public class AppController implements StatefulController {

	@FXML
	private FileTreeSectionController fileTreeSectionController;

	@FXML
	private CodeSectionController codeSectionController;

	@FXML
	private ConsoleSectionController consoleSectionController;

	@FXML
	private MemorySectionController memorySectionController;

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

		fileTreeSectionController.setOnFileOpen(this::displayFile);
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
		fileTreeSectionController.openFile(file);
	}

	private void displayFile(File file) {
		if (file == null || file.isDirectory()) {
			return;
		}

		try {
			String content = Files.readString(file.toPath(), StandardCharsets.UTF_8);
			if (state != null) {
				state.setOpenedFile(file);
				state.codeProperty().set(content);
			}
			updateWindowTitle(file.getName());
		} catch (IOException e) {
			PopUp.error("File Error", "Opening File", "Error while opening file " + file.toPath());
		}

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
				PopUp.info("File Info", "Creating file", "File : " + file.getName() + " already exists");
			}
		} catch (IOException e) {
			PopUp.error("File Error", "Creating file", "Error while creating new file " + file.getAbsolutePath());
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
		fileTreeSectionController.openDir(file);
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
			file = createFile();
			openFile(file);
		} else {
			file = openedFile.get();
		}

		if (file == null) {
			PopUp.error("File Error", "Cannot save file", "Not file found");
			return;
		}

		try { // try to save the file
			Files.writeString(
					file.toPath(),
					state.codeProperty().get(),
					StandardCharsets.UTF_8);
		} catch (IOException e) {
			PopUp.error("File Error", "Cannot save file", e.getMessage());
		}
	}

	@FXML
	private void saveFileAs() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save As");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MiniJaja Files", "*.mjj"));

		state.getOpenedFile().ifPresent(file -> fileChooser.setInitialFileName(file.getName()));

		File newFile = fileChooser.showSaveDialog(null);
		if (newFile == null) {
			return;
		}

		String fileName = newFile.getName();
		if (!fileName.toLowerCase().endsWith(".mjj")) {
			newFile = new File(newFile.getParentFile(), fileName + ".mjj");
		}

		try {
			Files.writeString(newFile.toPath(), state.codeProperty().get(), StandardCharsets.UTF_8);
			state.setOpenedFile(newFile);
			openFile(newFile);
		} catch (IOException e) {
			PopUp.error("Save Error", "Cannot save file", e.getMessage());
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
	 * Opens help minijaja language window
	 */
	@FXML
	private void openHelpMiniJaja() {
		openHelpFile("help_minijaja_content.html");
	}

	/**
	 * Opens help memory window
	 */
	@FXML
	private void openHelpMemory() {
		openHelpFile("help_memory_content.html");
	}

	private void openHelpFile(String resourceName) {
		try {
			URL resource = getClass().getResource(resourceName);
			if (resource == null) {
				PopUp.error("Help page", "Missing resource", "Cannot find " + resourceName);
				return;
			}

			File file = new File(resource.toURI());
			HelpPopUp.openHelpPopup(primaryStage, file);

		} catch (Exception e) {
			PopUp.error("Help page", "Cannot open help page", e.getMessage());
		}
	}

	/**
	 * Runs the current file in a seperate thread
	 */
	public void runCurrentFile() {
		initMemory();
		memorySectionController.bindState(state);

		MiniJajaInterpreterInteractive interpreter = new MiniJajaInterpreterInteractive(state.getMemory());
		interpreter.setStepModeEnabled(false);
		miniJajaInterpreterThread = new Thread(() -> {
			try {
				ASTNode programRoot = ParserUtils.parseString(state.codeProperty().get());
				interpreter.interpret(programRoot);
			} catch (InterpretRuntimeException e) {
				Platform.runLater(() -> PopUp.error("MiniJaja Interpreter Error", e.getMessage(), ""));
			} catch (Exception e) {
				Platform.runLater(() -> PopUp.error("MiniJaja Interpreter Error", "Error while parsing code", e.getMessage()));
			}
		});

		miniJajaInterpreterThread.start();
	}

	/**
	 * Stops the interpretation
	 */
	public void stopInterpretation() {
		if (miniJajaInterpreterThread != null && miniJajaInterpreterThread.isAlive()) {
			miniJajaInterpreterThread.interrupt();
		} else {
			PopUp.info("MiniJaja Interpreter", "Interpretation is not running", "Start the interpretation");
		}
	}

	/**
	 * Clear console
	 */
	public void clearConsole() {
		consoleSectionController.clearAllTabs();
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

	public void undoCode() {
		codeSectionController.undo();
	}

	public void redoCode() {
		codeSectionController.redo();
	}

	public void cutCode() {
		codeSectionController.cut();
	}

	public void copyCode() {
		codeSectionController.copy();
	}

	public void pasteCode() {
		codeSectionController.paste();
	}

	public void deleteCode() {
		codeSectionController.delete();
	}

	public void selectAllCode() {
		codeSectionController.selectAll();
	}

	public void unSelectAllCode() {
		codeSectionController.unSelectAll();
	}

	@FXML
	private void quitApp() {
		boolean confirmed = PopUp.confirm("Quit Application", "Are you sure you want to quit?");
		if (confirmed) {
			Platform.exit();
			System.exit(0);
		}
	}
}
