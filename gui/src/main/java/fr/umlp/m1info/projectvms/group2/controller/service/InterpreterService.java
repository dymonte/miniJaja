package fr.umlp.m1info.projectvms.group2.controller.service;

import java.util.List;

import fr.umlp.m1info.projectvms.group2.AppState;
import fr.umlp.m1info.projectvms.group2.PopUp;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.ASTNode;
import fr.umlp.m1info.projectvms.group2.interpreter.InteractiveInterpreterController;
import fr.umlp.m1info.projectvms.group2.interpreter.minijaja.MiniJajaInterpreterInteractive;
import fr.umlp.m1info.projectvms.group2.exception.InterpreterInterruptedException;
import fr.umlp.m1info.projectvms.group2.utils.ParserUtils;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.stage.Stage;

/**
 * Service layer responsible for running MiniJaja programs inside a backgronud
 * thread and coordinating UI events with the interactive interpreter
 */
public class InterpreterService {
	private Thread interpreterThread;

	private final AppState state;

	/**
	 * UI callback interface used by the service to notify the controller layer
	 */
	public interface InterpreterCallbacks {
		/**
		 * Highlights a specific line in the editor.
		 *
		 * @param line the line to highlight, or -1 to clear highlighting
		 */
		void highlihtLine(int line);

		/**
		 * Refreshes the memory view.
		 */
		void refreshMemory();

		/**
		 * Requests the UI to remove a breakpoint marker.
		 *
		 * @param line the line number where the breakpoint was removed
		 */
		void removeBreakpoint(int line);

		/**
		 * Opens the step-by-step interpreter popup.
		 *
		 * @param stage the JavaFX stage used to host the popup
		 * @param next  runnable executed when the user clicks "Next Step"
		 * @param stop  runnable executed when the user clicks "Stop"
		 * @param skip  runnable executed when the user clicks "Skip Breakpoint"
		 */
		void openStepPopup(Stage stage, Runnable next, Runnable stop, Runnable skip);

		/**
		 * Closes the step-by-step interpreter popup if it is currently open.
		 */
		void closeStepPopup();
	}

	private final InterpreterCallbacks callbacks;

	/**
	 * Constructs an InterpreterService
	 *
	 * @param state     the global application state (must not be null)
	 * @param callbacks the UI callback handler (must not be null)
	 * @throws NullPointerException if any argument is null
	 */
	public InterpreterService(AppState state, InterpreterCallbacks callbacks) {
		if (state == null || callbacks == null) {
			throw new NullPointerException("Arguments cannot be null");
		}
		this.state = state;
		this.callbacks = callbacks;
	}

	/**
	 * Indicates whether an interpreter thread is currently running.
	 *
	 * @return true if running, false otherwise
	 */
	public boolean isRunning() {
		return interpreterThread != null && interpreterThread.isAlive();
	}

	/**
	 * Interrupts the interpreter thread if it is running.
	 * This causes the interpreter to stop execution.
	 */
	public void stop() {
		if (isRunning())
			interpreterThread.interrupt();
	}

	/**
	 * Starts the MiniJaja interpreter with or without step mode enabled
	 *
	 * @param stepMode    whether step-by-step execution is enabled
	 * @param breakpoints list of breakpoints line numbers
	 */
	public void runMiniJaja(boolean stepMode, List<Integer> breakpoints) {
		if (isRunning()) {
			PopUp.info("MiniJaja Interpreter", "Already running", "Stop the current interpretation first");
			return;
		}
		MiniJajaInterpreterInteractive interpreter = new MiniJajaInterpreterInteractive(state.getMemory());
		interpreter.setStepModeEnabled(stepMode);
		interpreter.setBreakpoints(breakpoints);

		interpreter.addListener(new InteractiveInterpreterController.Listener() {
			public void onPaused(int line) {
				Platform.runLater(() -> {
					callbacks.highlihtLine(line);
					callbacks.refreshMemory();
				});
			}

			public void onBreakpointRemoved(int line) {
				callbacks.removeBreakpoint(line);
			}
		});

		try {
			ASTNode programRoot = loadMiniJajaProgram();
			createInterpreterTask(interpreter, () -> interpreter.interpret(programRoot), stepMode, breakpoints);
		} catch (InterpreterInterruptedException ignored) {
		} catch (Exception e) {
			if (stepMode) {
				callbacks.closeStepPopup();
			}
			PopUp.error("Interpreter Error", e.getMessage(), "");
		} finally {
			if (stepMode) {
				callbacks.closeStepPopup();
			}
			callbacks.highlihtLine(-1);
			callbacks.refreshMemory();
		}
	}

	/**
	 * Opens the step popup on the JavaFX UI thread with the correct action
	 * handlers
	 *
	 * @param popupStage  the popup window
	 * @param interpreter the interpreter instance
	 */
	private void showStepPopup(Stage popupStage, Object interpreter) {
		Platform.runLater(() -> callbacks.openStepPopup(
				popupStage,
				() -> callMethod(interpreter, "nextStep"),
				() -> stop(),
				() -> callMethod(interpreter, "skipBreakpoint")));
	}

	/**
	 * Invokes a public method of the interpreter using reflection
	 *
	 * @param obj    target object
	 * @param method name of method to call
	 */
	private void callMethod(Object obj, String method) {
		try {
			obj.getClass().getMethod(method).invoke(obj);
		} catch (Exception ignored) {
		}
	}

	/**
	 * Creates and starts a task that runs the interpreter logic in a dedicated
	 * background thread
	 *
	 * @param interpreter interpreter instance
	 * @param runLogic    interpreter execution logic
	 * @param stepMode    whether step mode is active
	 * @param breakpoints list of breakpoints
	 */
	private void createInterpreterTask(Object interpreter, Runnable runLogic, boolean stepMode,
			List<Integer> breakpoints) {
		Stage popupStage = new Stage();

		Task<Void> task = new Task<>() {
			protected Void call() {
				if (stepMode || !breakpoints.isEmpty()) {
					showStepPopup(popupStage, interpreter);
				}
				runLogic.run();

				if (stepMode || !breakpoints.isEmpty()) {
					Platform.runLater(callbacks::closeStepPopup);
					Platform.runLater(() -> callbacks.highlihtLine(-1));
				}
				Platform.runLater(callbacks::refreshMemory);
				return null;
			}
		};

		interpreterThread = new Thread(task);
		interpreterThread.start();

	}

	/**
	 * Loads the MiniJaja program either from the opened file or directly from the
	 * editor content
	 *
	 * @return parsed AST root node
	 * @throws Exception if parsing fails
	 */
	private ASTNode loadMiniJajaProgram() throws Exception {
		ASTNode programRoot;
		if (state.getOpenedFile().isEmpty()) {
			programRoot = ParserUtils.parseString(state.codeProperty().get());
		} else {
			programRoot = ParserUtils.parseFile(state.getOpenedFile().get().getAbsolutePath());
		}
		return programRoot;
	}
}
