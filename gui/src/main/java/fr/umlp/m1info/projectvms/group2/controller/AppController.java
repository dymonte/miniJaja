package fr.umlp.m1info.projectvms.group2.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.fxmisc.richtext.CodeArea;

import fr.umlp.m1info.projectvms.group2.AppState;
import fr.umlp.m1info.projectvms.group2.HelpPopUp;
import fr.umlp.m1info.projectvms.group2.PopUp;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.ASTNode;
import fr.umlp.m1info.projectvms.group2.exception.InterpreterInterruptedException;
import fr.umlp.m1info.projectvms.group2.interpreter.minijaja.MiniJajaInterpreterInteractive;
import fr.umlp.m1info.projectvms.group2.memory.Memory;
import fr.umlp.m1info.projectvms.group2.memory.heap.Heap;
import fr.umlp.m1info.projectvms.group2.memory.stack.Stack;
import fr.umlp.m1info.projectvms.group2.memory.symtable.SymTable;
import fr.umlp.m1info.projectvms.group2.utils.ParserUtils;
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

	private Thread miniJajaInterpreterThread;

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
		createToolbarButton("btn-compile", "compile.png", 30, () -> {
			PopUp.info("Compile", "Compilation not implemented", "This feature is not yet implemented.");
		});
	}

	/**
	 * Creates a toolbar button with an icon.
	 */
	private void createToolbarButton(String cssClass, String iconName, int size, Runnable action) {
		Button button = new Button();
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
	@FXML
	private void runCurrentFile() {
		runMiniJajaInterpretation(false);
	}

	/**
	 * Runs the current file in a seperate thread in step by step mode.
	 */
	@FXML
	private void runCurrentFileStep() {
		runMiniJajaInterpretation(true);
	}

	private void runMiniJajaInterpretation(Boolean stepMode) {
		if (miniJajaInterpreterThread != null && miniJajaInterpreterThread.isAlive()) {
			PopUp.info("Minijaja Interpreter", "Interpreter is already running",
					"Stop interpretation and after you can run it");
			return;
		}

		initMemory();
		memorySectionController.bindState(state);

		MiniJajaInterpreterInteractive interpreter = new MiniJajaInterpreterInteractive(state.getMemory());
		interpreter.setStepModeEnabled(stepMode);
		interpreter.addListener(new MiniJajaInterpreterInteractive.Listener() {
			@Override
			public void onPaused(ASTNode node, int line) {
				Platform.runLater(() -> codeSectionController.highlightLine(line));
				memorySectionController.bindState(state);
			}

			@Override
			public void onBreakpointRemoved(int line) {
				codeSectionController.removeBreakpoint(line);
			}
		});

		List<Integer> breakpoints = new ArrayList<>(codeSectionController.getBreakpoints());

		Stage popupStage = new Stage();

		Task<Void> task = new Task<>() {
			@Override
			protected Void call() {
				try {
					ASTNode programRoot;
					if (state.getOpenedFile().isEmpty()) {
						programRoot = ParserUtils.parseString(state.codeProperty().get());
					} else {
						programRoot = ParserUtils.parseFile(state.getOpenedFile().get().getAbsolutePath());
					}

					if (stepMode || !breakpoints.isEmpty()) {
						showStepPopup(popupStage, interpreter, !breakpoints.isEmpty());
					}

					interpreter.setBreakpoints(breakpoints);
					interpreter.interpret(programRoot);
				} catch (InterpreterInterruptedException e) {
					// Do nothing
				} catch (Exception e) {
					handleInterpretationError(popupStage, e.getMessage());
				} finally {
					if (stepMode || !breakpoints.isEmpty()) {
						Platform.runLater(popupStage::close);
						Platform.runLater(() -> codeSectionController.highlightLine(-1));
					}
					memorySectionController.bindState(state);
				}
				return null;
			}
		};

		miniJajaInterpreterThread = new Thread(task);
		miniJajaInterpreterThread.start();
	}

	private void handleInterpretationError(Stage popupStage, String message) {
		Platform.runLater(() -> {
			popupStage.close();
			PopUp.error("MiniJaja Interpreter Error", message, "");
		});
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
	 * Shows step by step popup
	 *
	 * @param popupStage     the Stage for the popUp
	 * @param interpreter    the interpreter to be attached
	 * @param hasBreakpoints whether the interpreter has breakpoints
	 */
	private void showStepPopup(Stage popupStage, MiniJajaInterpreterInteractive interpreter, boolean hasBreakpoints) {
		Platform.runLater(() -> {
			popupStage.initOwner(primaryStage);
			popupStage.setAlwaysOnTop(true);
			popupStage.initStyle(StageStyle.UNDECORATED);

			// Create Buttons
			setStepPopupBtns(popupStage, interpreter, hasBreakpoints);

			// Position Popup
			setStepPopupPos(popupStage, hasBreakpoints);

			popupStage.show();
		});
	}

	/**
	 * Set up step by step popup position
	 *
	 * @param popupStage     the step by step popup to add the buttons to
	 * @param interpreter    the interpreter for the buttons to interact with
	 * @param hasBreakpoints whether the interpreter has breakpoints
	 */
	private void setStepPopupBtns(Stage popupStage, MiniJajaInterpreterInteractive interpreter, boolean hasBreakpoints) {
		Button btnNext = new Button("Next");

		Button btnStop = new Button("Stop");
		HBox root;

		if (hasBreakpoints) {
			Button btnSkipBreakpoint = new Button("Skip breakpoint");
			root = new HBox(10, btnStop, btnNext, btnSkipBreakpoint);

			btnSkipBreakpoint.setOnAction(event -> {
				interpreter.skipBreakpoint();
			});
		} else {
			root = new HBox(10, btnStop, btnNext);
		}

		btnNext.setOnAction(event -> {
			interpreter.nextStep();
		});
		btnStop.setOnAction(event -> stopInterpretation());

		root.setStyle("-fx-padding: 20;");
		popupStage.setScene(new Scene(root, 150 + (hasBreakpoints ? 150 : 0), 50));
	}

	/**
	 * Set up step by step popup position
	 *
	 * @param popupStage     the step by step popup to position
	 * @param hasBreakpoints whether the interpreter has breakpoints
	 */
	private void setStepPopupPos(Stage popupStage, boolean hasBreakpoints) {
		double x = primaryStage.getX() + primaryStage.getWidth() / 2 - 75 - (hasBreakpoints ? 27.5 : 0);
		double y = primaryStage.getY() + 25;
		popupStage.setX(x);
		popupStage.setY(y);

		ChangeListener<Number> xListener = (obs, oldVal, newVal) -> {
			popupStage.setX(newVal.doubleValue() + primaryStage.getWidth() / 2 - 75);
		};
		ChangeListener<Number> yListener = (obs, oldVal, newVal) -> {
			popupStage.setY(newVal.doubleValue() + 25);
		};
		primaryStage.xProperty().addListener(xListener);
		primaryStage.yProperty().addListener(yListener);

		popupStage.setOnHidden(e -> {
			primaryStage.xProperty().removeListener(xListener);
			primaryStage.yProperty().removeListener(yListener);
		});
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

		Platform.runLater(() -> {
			Scene mainScene = primaryStage.getScene();
			if (mainScene != null) {
				loadThemesMenu();
			}
		});
	}

	/**
	 * Loads available themes into the themes menu
	 */
	private void loadThemesMenu() {
		menuThemes.getItems().clear();

		URL themesFolderUrl = getClass().getResource(STYLE_FOLDER);
		if (themesFolderUrl == null) {
			System.err.println("Themes folder not found");
			return;
		}

		File themesFolder = new File(themesFolderUrl.getPath());
		File[] cssFiles = themesFolder.listFiles((dir, name) -> name.endsWith(".css"));

		for (File file : cssFiles) {
			String cssName = extractThemeName(file);
			MenuItem item = new MenuItem(cssName);

			item.setOnAction(e -> applyTheme(file.getName()));
			menuThemes.getItems().add(item);
		}
	}

	/**
	 * Extracts the theme name from the CSS file
	 *
	 * @param file the CSS file
	 * @return the theme name
	 */
	private String extractThemeName(File file) {
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String firstLine = br.readLine();

			if (firstLine != null && firstLine.contains("ThemeName:")) {
				return firstLine.replace("/*", "")
								.replace("*/", "")
								.replace("ThemeName:", "")
								.trim();
			}
		} catch (Exception ignored) {}

		// fallback to file name
		String name = file.getName().replace(".css", "");
		return Character.toUpperCase(name.charAt(0)) + name.substring(1);
	}

	/**
	 * Applies the selected theme by changing the stylesheet
	 *
	 * @param cssFileName the CSS file name of the theme
	 */
	private void applyTheme(String cssFileName) {
		Scene scene = primaryStage.getScene();
		if (scene == null) return;

		scene.getStylesheets().removeIf(url -> url.contains("themes/"));

		String fullPath = STYLE_FOLDER + cssFileName;
		URL cssUrl = getClass().getResource(fullPath);

		if (cssUrl == null) {
			System.err.println("Unable to find " + fullPath);
			return;
		}

		scene.getStylesheets().add(cssUrl.toExternalForm());
	}

	private void addCtrlCHandler(Scene scene) {
		scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			if (event.isControlDown() && event.getCode() == KeyCode.C) {
				if (miniJajaInterpreterThread != null) {
					stopInterpretation();
					event.consume();
				}
			}
		});
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
