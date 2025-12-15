package fr.umlp.m1info.projectvms.group2.interpreter;

import java.util.List;

/**
 * Defines the capabilities of an interpreter that supports interactive,
 * debugger-like execution.
 */
public interface InteractiveInterpreter {

	// -------------------------------------------------------------------------
	// Debugger Mode
	// -------------------------------------------------------------------------

	/**
	 * Enables or disables step-by-step execution.
	 *
	 * @param enabled true to enable step mode, false to disable it
	 */
	void setStepModeEnabled(boolean enabled);

	// -------------------------------------------------------------------------
	// Execution Control
	// -------------------------------------------------------------------------

	/**
	 * Resumes execution until the next pause (step or breakpoint).
	 */
	void nextStep();

	/**
	 * Resumes execution and removes the breakpoint on the current line.
	 */
	void skipBreakpoint();

	// -------------------------------------------------------------------------
	// Breakpoint Management
	// -------------------------------------------------------------------------

	/**
	 * Replaces all currently active breakpoints with the given list of lines.
	 *
	 * @param lines the list of line numbers where execution should pause
	 */
	void setBreakpoints(List<Integer> lines);

	/**
	 * Adds a new breakpoint on the given line.
	 *
	 * @param line the line to break on
	 */
	void addBreakPoint(int line);

	/**
	 * Removes a breakpoint on the given line.
	 *
	 * @param line the line number whose breakpoint should be removed
	 */
	void removeBreakpoint(int line);

	// -------------------------------------------------------------------------
	// Query
	// -------------------------------------------------------------------------

	/**
	 * Returns the current line at which the interpreter is paused.
	 *
	 * @return the current paused line, or -1 if the interpreter is not paused
	 */
	int getCurrentLine();

	// -------------------------------------------------------------------------
	// Listeners
	// -------------------------------------------------------------------------

	/**
	 * Registers a listener that receives interpreter pause/breakpoint events.
	 *
	 * @param listener the listener to add
	 */
	void addListener(InteractiveInterpreterController.Listener listener);

	/**
	 * Unregisters a previously added listener.
	 *
	 * @param listener the listener to remove
	 */
	void removeListener(InteractiveInterpreterController.Listener listener);

}
