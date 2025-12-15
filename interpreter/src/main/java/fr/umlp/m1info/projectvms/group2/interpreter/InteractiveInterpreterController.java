package fr.umlp.m1info.projectvms.group2.interpreter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import fr.umlp.m1info.projectvms.group2.exception.InterpreterInterruptedException;
import fr.umlp.m1info.projectvms.group2.logger.InterpreterLogger;

/**
 * Controls the execution of an interpreter in an interactive or step-by-step
 * mode.
 * 
 * @param <T> The AST node type the interpreter is working with.
 */
public class InteractiveInterpreterController<T> {
	private final Object lock;
	private volatile boolean waiting;
	private volatile boolean stepModeEnabled;
	private final Set<Integer> breakpoints;
	private int currentLine;

	/**
	 * Listener interface use to notify external components about interpreter
	 * events.
	 */
	public interface Listener {
		/**
		 * Called when the interpreter pauses execution at a given line.
		 *
		 * @param line the line where execution has paused
		 */
		void onPaused(int line);

		/**
		 * Called when a breakpoint is removed, either manually or because
		 * the user chose to skip it.
		 *
		 * @param line the line whose breakpoint was removed
		 */
		void onBreakpointRemoved(int line);
	}

	private final List<Listener> listeners = new CopyOnWriteArrayList<>();

	/**
	 * Creates a new interactive interpreter controller with:
	 */
	public InteractiveInterpreterController() {
		this.lock = new Object();
		this.waiting = false;
		this.stepModeEnabled = true;
		this.breakpoints = new HashSet<>();
	}

	// -------------------------------------------------------------------------
	// Listener registration
	// -------------------------------------------------------------------------

	/**
	 * Adds a listener that will be notified when the interpreter pauses or when
	 * breakpoints are removed.
	 *
	 * @param listener the listener to register
	 */
	public void addListener(Listener listener) {
		listeners.add(listener);
	}

	/**
	 * Removes a previously registered listener.
	 *
	 * @param listener the listener to remove
	 */
	public void removeListener(Listener listener) {
		listeners.remove(listener);
	}

	/**
	 * Notifies all listeners that the interpreter has paused.
	 */
	private void notifyPaused() {
		for (Listener listener : listeners) {
			listener.onPaused(currentLine);
		}
	}

	/**
	 * Notifies listeners that a breakpoint was removed.
	 *
	 * @param line the line number whose breakpoint was deleted
	 */
	private void notifyBreakpointRemoved(int line) {
		for (Listener listener : listeners) {
			listener.onBreakpointRemoved(line);
		}
	}

	// -------------------------------------------------------------------------
	// Control and stepping logic
	// -------------------------------------------------------------------------

	/**
	 * Enables or disables step-by-step execution.
	 *
	 * @param enabled true to enable step mode, false otherwise
	 */
	public void setStepModeEnabled(boolean enabled) {
		this.stepModeEnabled = enabled;
	}

	/**
	 * Resumes execution and removes the breakpoint on the current line.
	 */
	public void skipBreakpoint() {
		synchronized (lock) {
			waiting = false;
			lock.notifyAll();
			breakpoints.remove(currentLine);
			notifyBreakpointRemoved(currentLine);
		}
	}

	/**
	 * Resumes execution until the next step/pause condition.
	 */
	public void nextStep() {
		synchronized (lock) {
			waiting = false;
			lock.notifyAll();
		}
	}

	// -------------------------------------------------------------------------
	// Breakpoint management
	// -------------------------------------------------------------------------

	/**
	 * Replaces the current list of breakpoints with the given list.
	 *
	 * @param lines the lines containing breakpoints
	 */
	public void setBreakpoints(List<Integer> lines) {
		synchronized (breakpoints) {
			breakpoints.clear();
			breakpoints.addAll(lines);
		}
	}

	/**
	 * Adds a single breakpoint.
	 *
	 * @param line the line number where execution should pause
	 */
	public void addBreakPoint(int line) {
		synchronized (breakpoints) {
			breakpoints.add(line);
		}
	}

	/**
	 * Removes a specific breakpoint.
	 *
	 * @param line the line number whose breakpoint should be removed
	 */
	public void removeBreakpoint(int line) {
		synchronized (breakpoints) {
			breakpoints.remove(line);
		}
	}

	/**
	 * Returns the current line at which the interpreter is paused.
	 *
	 * @return the current line
	 */
	public int getCurrentLine() {
		return currentLine;
	}

	// -------------------------------------------------------------------------
	// Interpreter pause/wait logic
	// -------------------------------------------------------------------------

	/**
	 * Checks whether the interpreter should pause at the given line, and waits
	 * until the user resumes execution.
	 *
	 * @param node the AST node currently being executed (may be used for logging)
	 * @param line the line number of the node
	 * @throws InterpreterInterruptedException if the thread is interrupted while
	 *                                         waiting
	 */
	public void checkAndWait(T node, int line) {
		if (node == null) {
			return;
		}

		boolean hitBreakpoint;
		synchronized (breakpoints) {
			hitBreakpoint = breakpoints.contains(line);
		}

		if (!stepModeEnabled && !hitBreakpoint)
			return;

		synchronized (lock) {
			waiting = true;
			currentLine = line;

			InterpreterLogger.getInstance().log(
					"[PAUSED] Line " + line + ": " + node.getClass().getSimpleName(),
					InterpreterLogger.MessageLevel.DEBUG);

			notifyPaused();

			while (waiting) {
				try {
					lock.wait();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					throw new InterpreterInterruptedException("Interpreter interrupted");
				}
			}
		}
	}
}
