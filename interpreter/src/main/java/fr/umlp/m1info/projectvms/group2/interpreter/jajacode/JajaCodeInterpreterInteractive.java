package fr.umlp.m1info.projectvms.group2.interpreter.jajacode;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.JajaCodeNode;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.instr.Instr;
import fr.umlp.m1info.projectvms.group2.interpreter.InteractiveInterpreter;
import fr.umlp.m1info.projectvms.group2.interpreter.InteractiveInterpreterController;
import fr.umlp.m1info.projectvms.group2.memory.Memory;

import java.util.List;

public class JajaCodeInterpreterInteractive extends JajaCodeInterpreter implements InteractiveInterpreter {
	private final InteractiveInterpreterController<JajaCodeNode> controller;

	public JajaCodeInterpreterInteractive(Memory memory, List<Instr> instrs) {
		super(memory, instrs);
		this.controller = new InteractiveInterpreterController<>();
	}

	public void addListener(InteractiveInterpreterController.Listener listener) {
		controller.addListener(listener);
	}

	public void removeListener(InteractiveInterpreterController.Listener listener) {
		controller.removeListener(listener);
	}

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

	@Override
	public void interpret(JajaCodeNode node) {
		if (node != null) {
			controller.checkAndWait(node, node.getLineId());
		}
		super.interpret(node);
	}
}
