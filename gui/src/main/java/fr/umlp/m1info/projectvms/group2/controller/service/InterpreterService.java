package fr.umlp.m1info.projectvms.group2.controller.service;

import java.io.IOException;
import java.util.List;

import fr.umlp.m1info.projectvms.group2.AppState;
import fr.umlp.m1info.projectvms.group2.PopUp;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.instr.Instr;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.ASTNode;
import fr.umlp.m1info.projectvms.group2.checker.Diagnostic;
import fr.umlp.m1info.projectvms.group2.checker.TypeChecker;
import fr.umlp.m1info.projectvms.group2.interpreter.InteractiveInterpreterController;
import fr.umlp.m1info.projectvms.group2.interpreter.minijaja.MiniJajaInterpreterInteractive;
import fr.umlp.m1info.projectvms.group2.logger.InterpreterLogger;
import fr.umlp.m1info.projectvms.group2.interpreter.jajacode.JajaCodeInterpreterInteractive;
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
		 * @param next runnable executed when the user clicks "Next Step"
		 * @param stop runnable executed when the user clicks "Stop"
		 * @param skip runnable executed when the user clicks "Skip Breakpoint"
		 */
		void openStepPopup(Runnable next, Runnable stop, Runnable skip);

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

		Task<Void> task = new Task<>() {
			@Override
			protected Void call() {
				boolean showPopup = stepMode || !breakpoints.isEmpty();
				try {
					ASTNode programRoot = loadMiniJajaProgram();

					checker(programRoot);

					if (showPopup) {
						Platform.runLater(() -> callbacks.openStepPopup(
								interpreter::nextStep,
								() -> {
									stop();
									callbacks.closeStepPopup();
								},
								breakpoints.isEmpty() ? null : interpreter::skipBreakpoint));
					}
					interpreter.interpret(programRoot);
				} catch (InterpreterInterruptedException ignored) {
				} catch (Exception e) {
					InterpreterLogger.getInstance().log(e.getMessage(), InterpreterLogger.MessageLevel.ERROR);
				} finally {
					Platform.runLater(() -> {
						if (showPopup) {
							callbacks.closeStepPopup();
							callbacks.highlihtLine(-1);
						}
						callbacks.refreshMemory();
					});
				}
				return null;
			}
		};
		interpreterThread = new Thread(task);
		interpreterThread.start();
	}

	private void checker(ASTNode node) throws Exception {
		Diagnostic diag = new Diagnostic();
		TypeChecker typeChecker = new TypeChecker(diag);
		typeChecker.check(node);

		if (!diag.issues().isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (Diagnostic.Issue issue : diag.issues()) {
				sb.append(issue.toString()).append("\n");
			}

			throw new Exception(sb.toString());
		}
	}

	public void runJajaCode(boolean stepMode, List<Integer> breakpoints) {
		if (isRunning()) {
			PopUp.info("JajaCode Interpreter", "Already running", "Stop the current interpretation first");
			return;
		}

		List<Instr> instrs;
		try {
			instrs = loadJajaCodeProgram();
			if (instrs == null || instrs.isEmpty()) {
				PopUp.error("JajaCode Interpreter Error", "No JajaCode instructions found", "");
				return;
			}
		} catch (Exception e) {
			PopUp.error("JajaCode Interpreter Error", e.getMessage(), "");
			return;
		}

		JajaCodeInterpreterInteractive interpreter = new JajaCodeInterpreterInteractive(state.getMemory(), instrs);
		interpreter.setStepModeEnabled(stepMode);
		interpreter.setBreakpoints(breakpoints);

		interpreter.addListener(new InteractiveInterpreterController.Listener() {
			@Override
			public void onPaused(int line) {
				Platform.runLater(() -> {
					callbacks.highlihtLine(line);
					callbacks.refreshMemory();
				});
			}

			@Override
			public void onBreakpointRemoved(int line) {
				callbacks.removeBreakpoint(line);
			}
		});

		Task<Void> task = new Task<>() {
			@Override
			protected Void call() {
				final boolean showPopup = stepMode || !breakpoints.isEmpty();
				try {
					if (showPopup) {
						Platform.runLater(() -> callbacks.openStepPopup(
								interpreter::nextStep,
								() -> {
									stop();
									callbacks.closeStepPopup();
								},
								breakpoints.isEmpty() ? null : interpreter::skipBreakpoint));
					}
					interpreter.interpret(instrs.get(0));
				} catch (InterpreterInterruptedException ignored) {
				} catch (Exception e) {
					Platform.runLater(() -> PopUp.error("JajaCode Interpreter Error", e.getMessage(), ""));
				} finally {
					Platform.runLater(() -> {
						if (showPopup) {
							callbacks.closeStepPopup();
							callbacks.highlihtLine(-1);
						}
						callbacks.refreshMemory();
					});
				}
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

	private List<Instr> loadJajaCodeProgram() throws IOException {
		if (state.getOpenedFile().isEmpty()) {
			return null;
		}
		return ParserUtils.jajaCodeParseFile(state.getOpenedFile().get().getAbsolutePath());

	}
}
