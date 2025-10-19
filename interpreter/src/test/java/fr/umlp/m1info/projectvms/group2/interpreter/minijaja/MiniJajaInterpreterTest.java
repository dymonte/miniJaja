package fr.umlp.m1info.projectvms.group2.interpreter.minijaja;

import org.junit.Test;
import org.antlr.v4.runtime.InterpreterRuleContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;

import org.mockito.Mockito;

import fr.umlp.m1info.projectvms.group2.logger.InterpreterLogger;
import fr.umlp.m1info.projectvms.group2.exception.InterpretRuntimeException;
import fr.umlp.m1info.projectvms.group2.exception.EvalRuntimeException;
import fr.umlp.m1info.projectvms.group2.memory.symtable.Type;
import fr.umlp.m1info.projectvms.group2.memory.symtable.SymTable;
import fr.umlp.m1info.projectvms.group2.memory.stack.Stack;
import fr.umlp.m1info.projectvms.group2.memory.heap.Heap;

import fr.umlp.m1info.projectvms.group2.memory.Memory;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.decl.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decls.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instrs.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.vars.*;

public class MiniJajaInterpreterTest {
	InterpreterLogger logger = InterpreterLogger.getInstance();
	MiniJajaInterpreter interpreter;
	Memory memory;

	@Before
	public void setUp() {
		// memory = new Memory();
		memory = new Memory(new SymTable(100), new Stack(100), new Heap(100));
		interpreter = new MiniJajaInterpreter(memory);
	}

	@Test
	public void constructorNull() {
		Assert.assertThrows(NullPointerException.class, () -> new MiniJajaInterpreter(null));
	}

	@Test
	public void interpretNull() {
		Assert.assertThrows(InterpretRuntimeException.class, () -> interpreter.interpret(null));
	}

	@Test
	public void interpretNotExist() {
		Assert.assertThrows(InterpretRuntimeException.class, () -> interpreter.interpret(new ASTBoolLiteral(true, 0, 0)));
	}

	@Test
	public void evalNull() {
		Assert.assertThrows(EvalRuntimeException.class, () -> interpreter.eval(null));
	}

	@Test
	public void evalNotExist() {
		ASTWrite write = new ASTWrite(new ASTIdent("a", 0, 0), false, 0, 0);
		Assert.assertThrows(EvalRuntimeException.class,
				() -> interpreter.eval(write));
	}

	@Test
	public void interpretAssignmentEq() {
		ASTIdent ident = new ASTIdent("i", 0, 0);
		ASTIdent ident2 = new ASTIdent("a", 0, 0);
		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		Mockito.doReturn("i").when(spyInterpreter).eval(ident);
		Mockito.doReturn("a").when(spyInterpreter).eval(ident2);

		memory.declVar("i", 1, Type.INT);
		memory.declVar("a", 2, Type.INT);

		ASTAssignmentOp assignmentOp = new ASTAssignmentOp(ident, ident2, AssignmentOp.EQ, 0, 0);
		spyInterpreter.interpret(assignmentOp);

		Assert.assertEquals(2, memory.getVal("i"));
	}

	@Test
	public void interpretAssignmentAddVar() {
		ASTIdent ident = new ASTIdent("i", 0, 0);
		ASTIdent ident2 = new ASTIdent("a", 0, 0);
		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		Mockito.doReturn("i").when(spyInterpreter).eval(ident);
		Mockito.doReturn("a").when(spyInterpreter).eval(ident2);

		memory.declVar("i", 1, Type.INT);
		memory.declVar("a", 2, Type.INT);

		ASTAssignmentOp assignmentOp = new ASTAssignmentOp(ident, ident2, AssignmentOp.ADD, 0, 0);
		spyInterpreter.interpret(assignmentOp);

		Assert.assertEquals(3, memory.getVal("i"));
	}

	@Test
	public void interpretAssignmentAddNb() {
		ASTIdent ident = new ASTIdent("i", 0, 0);
		ASTIntLiteral intLiteral = new ASTIntLiteral(3, 0, 0);
		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		Mockito.doReturn("i").when(spyInterpreter).eval(ident);
		Mockito.doReturn(3).when(spyInterpreter).eval(intLiteral);

		memory.declVar("i", 1, Type.INT);

		ASTAssignmentOp assignmentOp = new ASTAssignmentOp(ident, intLiteral, AssignmentOp.ADD, 0, 0);
		spyInterpreter.interpret(assignmentOp);

		Assert.assertEquals(4, memory.getVal("i"));
	}

	@Test
	public void interpretAssignmentAddArray() {
		ASTIdent identValue = new ASTIdent("i", 0, 0);
		ASTIdent ident = new ASTIdent("tab", 0, 0);
		ASTIntLiteral intLiteral = new ASTIntLiteral(0, 0, 0);
		ASTArrayAccess array = new ASTArrayAccess(ident, intLiteral, 0, 0);
		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		Mockito.doReturn("i").when(spyInterpreter).eval(identValue);
		Mockito.doReturn("tab").when(spyInterpreter).eval(ident);
		Mockito.doReturn(0).when(spyInterpreter).eval(intLiteral);

		memory.declVar("i", 2, Type.INT);
		memory.declArray("tab", 1, Type.INT);
		memory.updateValArray("tab", 0, 1);
		// i = tab[0] = 1
		ASTAssignmentOp assignmentOp = new ASTAssignmentOp(identValue, array, AssignmentOp.EQ, 0, 0);
		spyInterpreter.interpret(assignmentOp);

		Assert.assertEquals(1, memory.getValArray("tab", 0));
	}

	@Test
	public void interpretIncrementInc() {
		ASTIdent ident = new ASTIdent("i", 0, 0);
		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		Mockito.doReturn("i").when(spyInterpreter).eval(ident);

		memory.declVar("i", 1, Type.INT);

		ASTIncrementOp incrementOp = new ASTIncrementOp(ident, IncrementOp.INC, 0, 0);
		spyInterpreter.interpret(incrementOp);

		Assert.assertEquals(2, memory.getVal("i"));
	}

	@Test
	public void interpretIncrementIncArray() {
		ASTIdent ident = new ASTIdent("tab", 0, 0);
		ASTIntLiteral intLiteral = new ASTIntLiteral(0, 0, 0);
		ASTArrayAccess array = new ASTArrayAccess(ident, intLiteral, 0, 0);
		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		Mockito.doReturn("tab").when(spyInterpreter).eval(ident);
		Mockito.doReturn(0).when(spyInterpreter).eval(intLiteral);

		memory.declArray("tab", 1, Type.INT);
		memory.updateValArray("tab", 1, 0);

		ASTIncrementOp incrementOp = new ASTIncrementOp(array, IncrementOp.INC, 0, 0);
		spyInterpreter.interpret(incrementOp);

		Assert.assertEquals(1, memory.getValArray("tab", 0));
	}

	@Test
	public void interpretWriteIntTest() {
		ASTIdent ident = new ASTIdent("i", 0, 0);
		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		Mockito.doReturn(1).when(spyInterpreter).eval(ident);

		logger.addListener((message, level) -> {
			Assert.assertEquals("1", message);
			Assert.assertEquals(InterpreterLogger.MessageLevel.WRITE, level);
		});
		ASTWrite write = new ASTWrite(ident, false, 0, 0);
		spyInterpreter.interpret(write);
		logger.clearListeners();
	}

	@Test
	public void interpretWriteArrayTest() {
		ASTArrayAccess arrayAccess = new ASTArrayAccess(new ASTIdent("tab", 0, 0), new ASTIntLiteral(1, 0, 0), 0, 0);
		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		Mockito.doReturn(1).when(spyInterpreter).evalArrayAccess(arrayAccess);

		logger.addListener((message, level) -> {
			Assert.assertEquals("1\n", message);
			Assert.assertEquals(InterpreterLogger.MessageLevel.WRITE, level);
		});
		ASTWrite write = new ASTWrite(arrayAccess, true, 0, 0);
		spyInterpreter.interpret(write);
		logger.clearListeners();
	}

	@Test
	public void interpretWriteStringTest() {
		ASTStringLiteral stringLiteral = new ASTStringLiteral("WRITE", 0, 0);
		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		Mockito.doReturn("WRITE").when(spyInterpreter).eval(stringLiteral);

		logger.addListener((message, level) -> {
			Assert.assertEquals("WRITE", message);
			Assert.assertEquals(InterpreterLogger.MessageLevel.WRITE, level);
		});
		ASTWrite write = new ASTWrite(stringLiteral, false, 0, 0);
		spyInterpreter.interpret(write);
		logger.clearListeners();
	}

	@Test
	public void interpretWriteLnStringTest() {
		ASTStringLiteral stringLiteral = new ASTStringLiteral("WRITE", 0, 0);
		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		Mockito.doReturn("WRITE").when(spyInterpreter).eval(stringLiteral);

		logger.addListener((message, level) -> {
			Assert.assertEquals("WRITE\n", message);
			Assert.assertEquals(InterpreterLogger.MessageLevel.WRITE, level);
		});
		ASTWrite write = new ASTWrite(stringLiteral, true, 0, 0);
		spyInterpreter.interpret(write);
		logger.clearListeners();
	}

	@Test
	public void interpretWriteInvalidTest() {
		ASTReturn mockNode = Mockito.mock(ASTReturn.class);
		ASTWrite mockWrite = Mockito.mock(ASTWrite.class);

		Mockito.when(mockWrite.getValue()).thenReturn(mockNode);
		Assert.assertThrows(InterpretRuntimeException.class, () -> interpreter.interpret(mockWrite));
	}

	@Test
	public void interpretIfIfTest() {
		ASTStringLiteral stringLiteral = new ASTStringLiteral("WRITE", 0, 0);
		ASTStringLiteral stringLiteral2 = new ASTStringLiteral("AH", 0, 0);
		ASTInstrsChain chain = new ASTInstrsChain(new ASTWrite(stringLiteral, false, 0, 0), new ASTInstrsNil(0, 0), 0, 0);
		ASTInstrsChain chain2 = new ASTInstrsChain(new ASTWrite(stringLiteral, false, 0, 0), new ASTInstrsNil(0, 0), 0, 0);
		ASTBoolLiteral bool = new ASTBoolLiteral(true, 0, 0);
		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		Mockito.doReturn("WRITE").when(spyInterpreter).eval(stringLiteral);
		Mockito.doReturn("AH").when(spyInterpreter).eval(stringLiteral2);
		Mockito.doReturn(true).when(spyInterpreter).eval(bool);

		logger.addListener((message, level) -> {
			Assert.assertEquals("WRITE", message);
			Assert.assertEquals(InterpreterLogger.MessageLevel.WRITE, level);
		});

		ASTIf astIf = new ASTIf(bool, chain, chain2, 0, 0);
		spyInterpreter.interpret(astIf);
		logger.clearListeners();
	}

	@Test
	public void interpretIfElse() {
		ASTStringLiteral stringLiteral = new ASTStringLiteral("WRITE", 0, 0);
		ASTStringLiteral stringLiteral2 = new ASTStringLiteral("AH", 0, 0);
		ASTInstrsChain chain = new ASTInstrsChain(new ASTWrite(stringLiteral, false, 0, 0), new ASTInstrsNil(0, 0), 0, 0);
		ASTInstrsChain chain2 = new ASTInstrsChain(new ASTWrite(stringLiteral2, false, 0, 0), new ASTInstrsNil(0, 0), 0, 0);
		ASTBoolLiteral bool = new ASTBoolLiteral(false, 0, 0);

		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		Mockito.doReturn("WRITE").when(spyInterpreter).eval(stringLiteral);
		Mockito.doReturn("AH").when(spyInterpreter).eval(stringLiteral2);
		Mockito.doReturn(false).when(spyInterpreter).eval(bool);

		logger.addListener((message, level) -> {
			Assert.assertEquals("AH", message);
			Assert.assertEquals(InterpreterLogger.MessageLevel.WRITE, level);
		});

		ASTIf astIf = new ASTIf(bool, chain, chain2, 0, 0);
		spyInterpreter.interpret(astIf);
		logger.clearListeners();
	}

	@Test
	public void interpretIfExceptionTest() {
		ASTStringLiteral stringLiteral = new ASTStringLiteral("WRITE", 0, 0);
		ASTStringLiteral stringLiteral2 = new ASTStringLiteral("AH", 0, 0);
		ASTInstrsChain chain = new ASTInstrsChain(new ASTWrite(stringLiteral, false, 0, 0), new ASTInstrsNil(0, 0), 0, 0);
		ASTInstrsChain chain2 = new ASTInstrsChain(new ASTWrite(stringLiteral, false, 0, 0), new ASTInstrsNil(0, 0), 0, 0);
		ASTBoolLiteral bool = new ASTBoolLiteral(true, 0, 0);
		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		Mockito.doReturn("WRITE").when(spyInterpreter).eval(stringLiteral);
		Mockito.doReturn("AH").when(spyInterpreter).eval(stringLiteral2);
		Mockito.doReturn(null).when(spyInterpreter).eval(bool);

		ASTIf astIf = new ASTIf(bool, chain, chain2, 0, 0);
		Assert.assertThrows(InterpretRuntimeException.class, () -> spyInterpreter.interpret(astIf));
	}

	@Test
	public void interpretWhile() {
		int[] i = { 0 };
		ASTBoolLiteral bool = new ASTBoolLiteral(false, 0, 0);
		ASTStringLiteral stringLiteral = new ASTStringLiteral("WRITE", 0, 0);
		ASTInstrsChain chain = new ASTInstrsChain(new ASTWrite(stringLiteral, false, 0, 0), new ASTInstrsNil(0, 0), 0, 0);

		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		Mockito.doReturn("WRITE").when(spyInterpreter).eval(stringLiteral);
		Mockito.doAnswer(invocation -> i[0] == 0).when(spyInterpreter).eval(bool);

		logger.addListener((message, level) -> {
			if (i[0] > 0) {
				throw new IllegalArgumentException();
			}
			Assert.assertEquals("WRITE", message);
			Assert.assertEquals(InterpreterLogger.MessageLevel.WRITE, level);
			i[0]++;
		});

		ASTWhile astWhile = new ASTWhile(bool, chain, 0, 0);
		spyInterpreter.interpret(astWhile);
		logger.clearListeners();
	}

	@Test
	public void interpretReturn() {
		ASTClass astClass = new ASTClass(new ASTIdent("A", 0, 0), new ASTDeclsNil(0, 0),
				new ASTMainMethod(new ASTVarsNil(0, 0), new ASTInstrsNil(0, 0), 0, 0), 0, 0);
		ASTIntLiteral intLiteral = new ASTIntLiteral(1, 0, 0);

		memory.declVar("A", 0, Type.INT);

		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		Mockito.doReturn(1).when(spyInterpreter).eval(intLiteral);

		ASTReturn astReturn = new ASTReturn(intLiteral, 0, 0);
		spyInterpreter.interpret(astReturn);

		Assert.assertEquals(1, memory.getVal("A"));
	}
}
