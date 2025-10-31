package fr.umlp.m1info.projectvms.group2.interpreter;

import org.junit.Test;
import org.junit.Before;

import org.junit.Assert;

import java.util.List;
import java.util.Arrays;

import fr.umlp.m1info.projectvms.group2.memory.Memory;
import fr.umlp.m1info.projectvms.group2.memory.heap.Heap;
import fr.umlp.m1info.projectvms.group2.memory.stack.Stack;
import fr.umlp.m1info.projectvms.group2.memory.symtable.SymTable;

import fr.umlp.m1info.projectvms.group2.interpreter.minijaja.MiniJajaInterpreterInteractive;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTIdent;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTStringLiteral;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.ASTWrite;
import fr.umlp.m1info.projectvms.group2.exception.InterpretRuntimeException;

public class MiniJajaInterpreterInteractiveTest {
	MiniJajaInterpreterInteractive interactive;
	Memory memory;

	@Before
	public void setUp() {
		memory = new Memory(new SymTable(100), new Stack(100), new Heap(100));
		interactive = new MiniJajaInterpreterInteractive(memory);
	}

	@Test
	public void testAll() {
		interactive.setStepModeEnabled(true);
		interactive.nextStep();
		List<Integer> list = Arrays.asList(1, 2);
		interactive.setBreakpoints(list);
		interactive.addBreakPoint(1);
		interactive.removeBreakpoint(1);
		Assert.assertThrows(InterpretRuntimeException.class, () -> interactive.interpret(null));
	}

	@Test
	public void testHitBreakPoint() {
		interactive.addBreakPoint(1);
		interactive.setStepModeEnabled(false);
		ASTIdent ident = new ASTIdent("a", 0, 0);
		ASTIdent ident1 = new ASTIdent("a", 1, 0);
		Assert.assertThrows(InterpretRuntimeException.class, () -> interactive.interpret(ident));

		Thread t = new Thread(
				() -> Assert.assertThrows(InterpretRuntimeException.class, () -> interactive.interpret(ident1)));

		t.start();
		interactive.nextStep();
	}

	@Test
	public void testStepModeEnabled() {
		ASTIdent ident = new ASTIdent("a", 0, 0);

		Thread t = new Thread(
				() -> Assert.assertThrows(InterpretRuntimeException.class, () -> interactive.interpret(ident)));

		t.start();
		interactive.nextStep();
	}

	@Test
	public void testLock() {
		ASTIdent ident = new ASTIdent("a", 0, 0);

		Thread t = new Thread(
				() -> Assert.assertThrows(InterpretRuntimeException.class, () -> interactive.interpret(ident)));

		t.start();
		t.interrupt();

		interactive.nextStep();
	}

	@Test
	public void testInterpret() {
		ASTWrite astWrite = new ASTWrite(new ASTStringLiteral("a", 0, 0), false, 0, 0);

		interactive.setStepModeEnabled(false);
		interactive.interpret(astWrite);
	}
}
