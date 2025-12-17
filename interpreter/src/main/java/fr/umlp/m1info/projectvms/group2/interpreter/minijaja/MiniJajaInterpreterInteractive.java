package fr.umlp.m1info.projectvms.group2.interpreter.minijaja;

import java.util.List;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.ASTNode;
import fr.umlp.m1info.projectvms.group2.interpreter.InteractiveInterpreter;
import fr.umlp.m1info.projectvms.group2.interpreter.InteractiveInterpreterController;
import fr.umlp.m1info.projectvms.group2.memory.Memory;

/**
 * Interactive version of the MiniJaja interpreter
 */
public class MiniJajaInterpreterInteractive extends MiniJajaInterpreter implements InteractiveInterpreter {

	private final InteractiveInterpreterController<ASTNode> controller;

	/**
	 * Creates a new interactive MiniJaja interpreter.
	 *
	 * @param memory the memory model used during interpretation
	 */
	public MiniJajaInterpreterInteractive(Memory memory) {
		super(memory);
		this.controller = new InteractiveInterpreterController<>();
	}

	// -------------------------------------------------------------------------
	// Listener delegation
	// -------------------------------------------------------------------------

	@Override
	public void addListener(InteractiveInterpreterController.Listener listener) {
		controller.addListener(listener);
	}

	@Override
	public void removeListener(InteractiveInterpreterController.Listener listener) {
		controller.removeListener(listener);
	}

	// -------------------------------------------------------------------------
	// Debugger control delegation
	// -------------------------------------------------------------------------

	@Override
	public void setStepModeEnabled(boolean enabled) {
		controller.setStepModeEnabled(enabled);
	}

	@Override
	public void skipBreakpoint() {
		controller.skipBreakpoint();
	}

	@Override
	public void nextStep() {
		controller.nextStep();
	}

	@Override
	public void setBreakpoints(List<Integer> lines) {
		controller.setBreakpoints(lines);
	}

	@Override
	public void addBreakPoint(int line) {
		controller.addBreakPoint(line);
	}

	@Override
	public void removeBreakpoint(int line) {
		controller.removeBreakpoint(line);
	}

	@Override
	public int getCurrentLine() {
		return controller.getCurrentLine();
	}

	// -------------------------------------------------------------------------
	// Interpretation with pause logic
	// -------------------------------------------------------------------------

	/**
	 * Interprets the given AST node, pausing execution if:
	 *
	 * @param node the AST node to interpret
	 */
	@Override
	public void interpret(ASTNode node) {
		if (node != null) {
			controller.checkAndWait(node, node.getLine());
		}
		super.interpret(node);
	}
}
