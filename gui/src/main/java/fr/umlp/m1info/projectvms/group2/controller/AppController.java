package fr.umlp.m1info.projectvms.group2.controller;

import fr.umlp.m1info.projectvms.group2.AppState;
import fr.umlp.m1info.projectvms.group2.HelpPopUp;
import fr.umlp.m1info.projectvms.group2.PopUp;
import fr.umlp.m1info.projectvms.group2.controller.service.InterpreterService;
import fr.umlp.m1info.projectvms.group2.controller.service.CompilerService;
import fr.umlp.m1info.projectvms.group2.controller.service.ThemeService;
import fr.umlp.m1info.projectvms.group2.controller.popup.StepPopupManager;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.instr.Instr;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.ASTNode;
import fr.umlp.m1info.projectvms.group2.compilation.MiniJajaCompiler;
import fr.umlp.m1info.projectvms.group2.exception.InterpreterInterruptedException;
import fr.umlp.m1info.projectvms.group2.interpreter.InteractiveInterpreterController;
import fr.umlp.m1info.projectvms.group2.interpreter.jajacode.JajaCodeInterpreterInteractive;
import fr.umlp.m1info.projectvms.group2.interpreter.minijaja.MiniJajaInterpreterInteractive;
import fr.umlp.m1info.projectvms.group2.memory.Memory;
import fr.umlp.m1info.projectvms.group2.memory.heap.Heap;
import fr.umlp.m1info.projectvms.group2.memory.stack.Stack;
import fr.umlp.m1info.projectvms.group2.memory.symtable.SymTable;
import fr.umlp.m1info.projectvms.group2.utils.ParserUtils;
import fr.umlp.m1info.projectvms.group2.checker.Diagnostic;
import fr.umlp.m1info.projectvms.group2.checker.TypeChecker;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.fxmisc.richtext.CodeArea;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

	@FXML
	private Menu menuThemes;

	@FXML
	private CodeArea codeArea;

	private Stage primaryStage;

	private AppState state;

	private InterpreterService interpreterService;

	private CompilerService compilerService;

	private ThemeService themeService;

	private StepPopupManager stepPopup;

	private Thread miniJajaInterpreterThread;

	private Thread jajacodeInterpreterThread;

	private Thread minijajaCompilerThread;

	private Scene mainScene;

	private final String STYLE_FOLDER = "/fr/umlp/m1info/projectvms/group2/styles/themes/";

	@FXML
	public void initialize() {
		addControlButtons();
	}

	@Override
	public void bindState(AppState appState) {
		// set App state to all controllers
		this.state = appState;
		codeSectionController.bindState(appState);
		codeSectionController.setOnSaveRequested(this::saveFile);
		memorySectionController.bindState(appState);

		fileTreeSectionController.setOnFileOpen(this::displayFile);

		stepPopup = new StepPopupManager();

		interpreterService = new InterpreterService(state, new InterpreterService.InterpreterCallbacks() {
			@Override
			public void highlihtLine(int line) {
				codeSectionController.highlightLine(line);
			};

			@Override
			public void refreshMemory() {
				memorySectionController.bindState(state);
			}

			@Override
			public void removeBreakpoint(int line) {
				codeSectionController.removeBreakpoint(line);

			}

			@Override
			public void openStepPopup(Runnable next, Runnable stop, Runnable skip) {
				stepPopup.open(primaryStage, next, stop, skip);
			}

			@Override
			public void closeStepPopup() {
				stepPopup.close();
			}
		});

		compilerService = new CompilerService(state);
	}

	/**
	 * Adds control buttons to the code section toolbar
	 */
	private void addControlButtons() {
		// Run Button
		createToolbarButton("btn-run", "run.png", 32, this::runCurrentFile);

		// Step Button (for step by step)
		createToolbarButton("btn-step", "step.png", 36, this::runCurrentFileStep);

		// Stop Button
		createToolbarButton("btn-stop", "stop.png", 36, this::stopInterpretation);

		// Compile Button
		createToolbarButton("btn-compile", "compile.png", 30, this::compileCurrentFile);
	}

	/**
	 * Creates a toolbar button with an icon.
	 */
	private void createToolbarButton(String cssClass, String iconName, int size, Runnable action) {
		Button button = new Button();
		button.setId(cssClass);
		button.getStyleClass().add(cssClass);
		button.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(iconName), size, size, true, true)));
		button.setOnAction(e -> action.run());
		codeSectionController.getButtonBar().getChildren().add(button);
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
		} else if (isInterpreterRunningAndBlock("open a file")) {
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
		if (isInterpreterRunningAndBlock("create a file")) {
			return null;
		}
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
		if (isInterpreterRunningAndBlock("open a file")) {
			return;
		}
		File file = chooseFile();
		openFile(file);
	}

	/**
	 * Opens a folder and updates the file tree
	 * Called by the button
	 */
	@FXML
	private void openFolder() {
		if (isInterpreterRunningAndBlock("open a folder")) {
			return;
		}
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
		boolean createdFile;
		if (openedFile.isEmpty()) {
			file = createFile();
			createdFile = true;
		} else {
			file = openedFile.get();
			createdFile = false;
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
			if (createdFile) {
				openFile(file);
			}
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
		if (isInterpreterRunningAndBlock("close the file")) {
			return;
		}
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
	 * Opens help jajacode language window
	 */
	@FXML
	private void openHelpJajaCode() {
		openHelpFile("help_jajacode_content.html");
	}

	/**
	 * Opens help memory window
	 */
	@FXML
	private void openHelpMemory() {
		openHelpFile("help_memory_content.html");
	}

	/**
	 * Opens help errors window
	 */
	@FXML
	private void openHelpErrors() {
		openHelpFile("help_errors_content.html");
	}

	/**
	 * Opens help inerface window
	 */
	@FXML
	private void openHelpInterface() {
		openHelpFile("help_interface_content.html");
	}

	/**
	 * Opens credit window
	 */
	@FXML
	private void openCredit() {
		openHelpFile("credit.html");
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
	 * Compiles the current file.
	 */
	@FXML
	private void compileCurrentFile() {
		compilerService.compile(fileTreeSectionController::refreshTree);
	};

	/**
	 * Runs the current file in a seperate thread
	 */
	@FXML
	private void runCurrentFile() {
		runCurrentFile(false);
	}

	/**
	 * Runs the current file in a seperate thread in step by step mode.
	 */
	@FXML
	private void runCurrentFileStep() {
		runCurrentFile(true);
	}

	private void runCurrentFile(Boolean step) {
		consoleSectionController.clearAllTabs();
		Optional<File> toRunOpt = state.getOpenedFile();

		List<Integer> breakpoints = new ArrayList<>(codeSectionController.getBreakpoints());

		initMemory();
		if (!toRunOpt.isPresent()) {
			interpreterService.runMiniJaja(step, breakpoints);
			return;
		}

		String fileName = toRunOpt.get().getName();
		String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
		switch (extension) {
			case "jjc" -> interpreterService.runJajaCode(step, breakpoints);
			case "mjj" -> interpreterService.runMiniJaja(step, breakpoints);
			default ->
				PopUp.error("Interpreter", "Unrecognized file extension",
						"Unrecognized file extension : " + extension + " . (Should be .jjc or .mjj)");
		}

	}

	/**
	 * Stops the interpretation
	 */
	@FXML
	private void stopInterpretation() {
		if (miniJajaInterpreterThread != null && miniJajaInterpreterThread.isAlive()) {
			miniJajaInterpreterThread.interrupt();
			miniJajaInterpreterThread = null;
		} else {
			PopUp.info("MiniJaja Interpreter", "Interpretation is not running", "Start the interpretation");
		}
	}

	/**
	 * Clear console
	 */
	@FXML
	private void clearConsole() {
		consoleSectionController.clearAllTabs();
	}

	/**
	 * Initializes the memory
	 */
	private void initMemory() {
		SymTable symTable = new SymTable(256);
		Stack stack = new Stack(1024);
		Heap heap = new Heap(4096);
		state.setMemory(new Memory(symTable, stack, heap));
	}

	/**
	 * Sets the primary stage for this controller
	 *
	 * @param stage the primary stage
	 */
	public void setPrimaryStage(Stage stage) {
		this.primaryStage = stage;

		Platform.runLater(() -> {
			mainScene = primaryStage.getScene();
			if (mainScene != null) {
				themeService = new ThemeService(mainScene, STYLE_FOLDER);
				themeService.loadThemesMenu(menuThemes);
			}
		});
	}

	/**
	 * Checks whether the interpreter is currently running.
	 * If it is, a pop-up is shown informing the user that the action cannot
	 * proceed.
	 *
	 * @param action The action the user is attempting to perform.
	 * @return true if the interpreter is running (the caller should block the
	 *         action), false otherwise.
	 */
	private boolean isInterpreterRunningAndBlock(String action) {
		if (miniJajaInterpreterThread != null && miniJajaInterpreterThread.isAlive()) {
			PopUp.info(
					"Minijaja Interpreter",
					"Interpreter is running",
					"Stop the interpretation before you can " + action + ".");
			return true;
		}
		return false;
	}

	@FXML
	private void undoCode() {
		codeSectionController.undo();
	}

	@FXML
	private void redoCode() {
		codeSectionController.redo();
	}

	@FXML
	private void cutCode() {
		codeSectionController.cut();
	}

	@FXML
	private void copyCode() {
		codeSectionController.copy();
	}

	@FXML
	private void pasteCode() {
		codeSectionController.paste();
	}

	@FXML
	private void deleteCode() {
		codeSectionController.delete();
	}

	@FXML
	private void selectAllCode() {
		codeSectionController.selectAll();
	}

	@FXML
	private void unSelectAllCode() {
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
