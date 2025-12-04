package fr.umlp.m1info.projectvms.group2.interpreter.jajacode;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.JajaCodeNode;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.instr.Instr;
import fr.umlp.m1info.projectvms.group2.exception.InterpretRuntimeException;
import fr.umlp.m1info.projectvms.group2.logger.InterpreterLogger;
import fr.umlp.m1info.projectvms.group2.memory.Memory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class JajaCodeInterpreterInteractive extends JajaCodeInterpreter {
	private final Object lock;
	private volatile boolean waiting;
	private volatile boolean stepModeEnabled;
	private final Set<Integer> breakpoints;
	private int currentLine;

	public interface Listener {
		void onPaused(Instr instr, int line);

		void onBreakpointRemoved(int line);
	}

	private final List<Listener> listeners = new CopyOnWriteArrayList<>();

	public JajaCodeInterpreterInteractive(Memory memory, List<Instr> instrs) {
		super(memory, instrs);
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

	private void notifyPaused(Instr instr) {
		for (Listener listener : listeners) {
			listener.onPaused(instr, currentLine);
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

	private void waitForNext(JajaCodeNode node) {
		if (node == null) {
			return;
		}

		boolean hitBreakpoint;
		synchronized (breakpoints) {
			hitBreakpoint = breakpoints.contains(node.getLineId());
		}

		if (!stepModeEnabled && !hitBreakpoint)
			return;

		synchronized (lock) {
			waiting = true;

			InterpreterLogger.getInstance().log(
					"[PAUSED] Line " + node.getLineId() + ": " + node.getClass().getSimpleName(),
					InterpreterLogger.MessageLevel.DEBUG);

			while (waiting) {
				try {
					lock.wait();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					throw new InterpretRuntimeException("Interpreter interrupted");
				}
			}
		}
	}

	@Override
	public void interpret(JajaCodeNode node) {
		waitForNext(node);
		super.interpret(node);
	}
}
