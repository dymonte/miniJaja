package fr.umlp.m1info.projectvms.group2.interpreter.minijaja;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.HashSet;
import java.util.List;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.ASTNode;
import fr.umlp.m1info.projectvms.group2.exception.InterpreterInterruptedException;
import fr.umlp.m1info.projectvms.group2.logger.InterpreterLogger;
import fr.umlp.m1info.projectvms.group2.memory.Memory;

public class MiniJajaInterpreterInteractive extends MiniJajaInterpreter {

	private final Object lock;
	private volatile boolean waiting;
	private volatile boolean stepModeEnabled;
	private final Set<Integer> breakpoints;
	private int currentLine;

	public interface Listener {
		void onPaused(ASTNode node, int line);

		void onBreakpointRemoved(int line);
	}

	private final List<Listener> listeners = new CopyOnWriteArrayList<>();

	public MiniJajaInterpreterInteractive(Memory memory) {
		super(memory);
		this.lock = new Object();
		this.waiting = false;
		this.stepModeEnabled = true;
		this.breakpoints = new HashSet<>();
	}

	public void addListener(Listener listener) {
		listeners.add(listener);
	}

	public void removeListener(Listener listener) {
		listeners.remove(listener);
	}

	private void notifyPaused(ASTNode node) {
		for (Listener listener : listeners) {
			listener.onPaused(node, currentLine);
		}
	}

	private void notifyBreakpointRemoved(int line) {
		for (Listener listener : listeners) {
			listener.onBreakpointRemoved(line);
		}
	}

	public void setStepModeEnabled(boolean enabled) {
		this.stepModeEnabled = enabled;
	}

	public void skipBreakpoint() {
		synchronized (lock) {
			waiting = false;
			lock.notifyAll();
			breakpoints.remove(currentLine);
			notifyBreakpointRemoved(currentLine);
		}
	}

	public void nextStep() {
		synchronized (lock) {
			waiting = false;
			lock.notifyAll();
		}
	}

	public void setBreakpoints(List<Integer> lines) {
		synchronized (breakpoints) {
			breakpoints.clear();
			breakpoints.addAll(lines);
		}
	}

	public void addBreakPoint(int line) {
		synchronized (breakpoints) {
			breakpoints.add(line);
		}
	}

	public void removeBreakpoint(int line) {
		synchronized (breakpoints) {
			breakpoints.remove(line);
		}
	}

	public int getCurrentLine() {
		return currentLine;
	}

	private void waitForNext(ASTNode node) {
		if (node == null) {
			return;
		}

		boolean hitBreakpoint;
		synchronized (breakpoints) {
			hitBreakpoint = breakpoints.contains(node.getLine());
		}

		if (!stepModeEnabled && !hitBreakpoint)
			return;

		synchronized (lock) {
			waiting = true;

			InterpreterLogger.getInstance().log("[PAUSED] Line " + node.getLine() + ": " + node.getClass().getSimpleName(),
					InterpreterLogger.MessageLevel.DEBUG);
			currentLine = node.getLine();

			notifyPaused(node);

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

	@Override
	public void interpret(ASTNode node) {
		waitForNext(node);
		super.interpret(node);
	}
}
