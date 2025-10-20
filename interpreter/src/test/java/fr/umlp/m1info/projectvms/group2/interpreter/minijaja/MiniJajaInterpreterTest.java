package fr.umlp.m1info.projectvms.group2.interpreter.minijaja;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.decl.ASTClass;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decl.ASTMainMethod;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decls.ASTDeclsNil;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTArrayAccess;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTBinaryOp;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTBoolLiteral;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTIdent;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTIntLiteral;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTStringLiteral;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTUnaryOp;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.BinaryOp;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.UnaryOp;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.ASTAssignmentOp;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.ASTIf;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.ASTIncrementOp;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.ASTReturn;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.ASTWhile;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.ASTWrite;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.AssignmentOp;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.IncrementOp;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instrs.ASTInstrsChain;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instrs.ASTInstrsNil;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.vars.ASTVarsNil;
import fr.umlp.m1info.projectvms.group2.exception.EvalRuntimeException;
import fr.umlp.m1info.projectvms.group2.exception.InterpretRuntimeException;
import fr.umlp.m1info.projectvms.group2.logger.InterpreterLogger;
import fr.umlp.m1info.projectvms.group2.memory.Memory;
import fr.umlp.m1info.projectvms.group2.memory.heap.Heap;
import fr.umlp.m1info.projectvms.group2.memory.stack.Stack;
import fr.umlp.m1info.projectvms.group2.memory.symtable.SymTable;
import fr.umlp.m1info.projectvms.group2.memory.symtable.Type;

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

	@Test
	public void evalArrayAccess() {
		ASTIdent ident = new ASTIdent("tab", 0, 0);
		ASTIntLiteral intLiteral = new ASTIntLiteral(1, 0, 0);
		ASTArrayAccess arrayAccess = new ASTArrayAccess(ident, intLiteral, 0, 0);

		memory.declArray("tab", 3, Type.INT);
		memory.updateValArray("tab", 0, 10);
		memory.updateValArray("tab", 1, 20);
		memory.updateValArray("tab", 2, 30);

		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		Mockito.doReturn("tab").when(spyInterpreter).eval(ident);
		Mockito.doReturn(1).when(spyInterpreter).eval(intLiteral);

		Assert.assertEquals(20, spyInterpreter.evalArrayAccess(arrayAccess));
	}

	@Test
	public void testBinaryOp_ADD() {
		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		ASTIntLiteral a = new ASTIntLiteral(5, 0, 0);
		ASTIntLiteral b = new ASTIntLiteral(10, 0, 0);

		Mockito.doReturn(5).when(spyInterpreter).eval(a);
		Mockito.doReturn(10).when(spyInterpreter).eval(b);

		ASTBinaryOp node = new ASTBinaryOp(a, b, BinaryOp.ADD, 0, 0);
		Assert.assertEquals(15, spyInterpreter.evalBinaryOp(node));
	}

	@Test
	public void testBinaryOp_SUB() {
		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		ASTIntLiteral a = new ASTIntLiteral(10, 0, 0);
		ASTIntLiteral b = new ASTIntLiteral(5, 0, 0);
		ASTBoolLiteral boolVal = new ASTBoolLiteral(true, 0, 0);

		Mockito.doReturn(10).when(spyInterpreter).eval(a);
		Mockito.doReturn(5).when(spyInterpreter).eval(b);
		Mockito.doReturn(true).when(spyInterpreter).eval(boolVal);

		ASTBinaryOp node = new ASTBinaryOp(a, b, BinaryOp.SUB, 0, 0);
		Assert.assertEquals(5, spyInterpreter.evalBinaryOp(node));

		ASTBinaryOp invalidNode = new ASTBinaryOp(a, boolVal, BinaryOp.SUB, 0, 0);
		Assert.assertThrows(InterpretRuntimeException.class, () -> spyInterpreter.evalBinaryOp(invalidNode));
	}

	@Test
	public void testBinaryOp_MUL() {
		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		ASTIntLiteral a = new ASTIntLiteral(3, 0, 0);
		ASTIntLiteral b = new ASTIntLiteral(4, 0, 0);
		ASTBoolLiteral boolVal = new ASTBoolLiteral(false, 0, 0);

		Mockito.doReturn(3).when(spyInterpreter).eval(a);
		Mockito.doReturn(4).when(spyInterpreter).eval(b);
		Mockito.doReturn(false).when(spyInterpreter).eval(boolVal);

		ASTBinaryOp node = new ASTBinaryOp(a, b, BinaryOp.MUL, 0, 0);
		Assert.assertEquals(12, spyInterpreter.evalBinaryOp(node));

		ASTBinaryOp invalidNode = new ASTBinaryOp(a, boolVal, BinaryOp.MUL, 0, 0);
		Assert.assertThrows(InterpretRuntimeException.class, () -> spyInterpreter.evalBinaryOp(invalidNode));
	}

	@Test
	public void testBinaryOp_DIV() {
		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		ASTIntLiteral a = new ASTIntLiteral(10, 0, 0);
		ASTIntLiteral b = new ASTIntLiteral(2, 0, 0);
		ASTIntLiteral zero = new ASTIntLiteral(0, 0, 0);

		Mockito.doReturn(10).when(spyInterpreter).eval(a);
		Mockito.doReturn(2).when(spyInterpreter).eval(b);
		Mockito.doReturn(0).when(spyInterpreter).eval(zero);

		ASTBinaryOp node = new ASTBinaryOp(a, b, BinaryOp.DIV, 0, 0);
		Assert.assertEquals(5, spyInterpreter.evalBinaryOp(node));

		ASTBinaryOp zeroNode = new ASTBinaryOp(a, zero, BinaryOp.DIV, 0, 0);
		Assert.assertThrows(InterpretRuntimeException.class, () -> spyInterpreter.evalBinaryOp(zeroNode));
	}

	@Test
	public void testBinaryOp_AND() {
		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		ASTBoolLiteral t = new ASTBoolLiteral(true, 0, 0);
		ASTBoolLiteral f = new ASTBoolLiteral(false, 0, 0);
		ASTIntLiteral zero = new ASTIntLiteral(0, 0, 0);
		ASTIntLiteral five = new ASTIntLiteral(5, 0, 0);

		Mockito.doReturn(true).when(spyInterpreter).eval(t);
		Mockito.doReturn(false).when(spyInterpreter).eval(f);
		Mockito.doReturn(0).when(spyInterpreter).eval(zero);
		Mockito.doReturn(5).when(spyInterpreter).eval(five);

		// bool && bool
		Assert.assertEquals(false, spyInterpreter.evalBinaryOp(new ASTBinaryOp(t, f, BinaryOp.AND, 0, 0)));
		// int && bool
		Assert.assertEquals(false, spyInterpreter.evalBinaryOp(new ASTBinaryOp(five, f, BinaryOp.AND, 0, 0)));
		// bool && int
		Assert.assertEquals(false, spyInterpreter.evalBinaryOp(new ASTBinaryOp(t, zero, BinaryOp.AND, 0, 0)));
		// int && int
		Assert.assertEquals(false, spyInterpreter.evalBinaryOp(new ASTBinaryOp(five, zero, BinaryOp.AND, 0, 0)));
	}

	@Test
	public void testBinaryOp_OR() {
		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		ASTBoolLiteral t = new ASTBoolLiteral(true, 0, 0);
		ASTBoolLiteral f = new ASTBoolLiteral(false, 0, 0);
		ASTIntLiteral zero = new ASTIntLiteral(0, 0, 0);
		ASTIntLiteral five = new ASTIntLiteral(5, 0, 0);

		Mockito.doReturn(true).when(spyInterpreter).eval(t);
		Mockito.doReturn(false).when(spyInterpreter).eval(f);
		Mockito.doReturn(0).when(spyInterpreter).eval(zero);
		Mockito.doReturn(5).when(spyInterpreter).eval(five);

		// bool || bool
		Assert.assertEquals(true, spyInterpreter.evalBinaryOp(new ASTBinaryOp(t, f, BinaryOp.OR, 0, 0)));
		// int || bool
		Assert.assertEquals(true, spyInterpreter.evalBinaryOp(new ASTBinaryOp(five, f, BinaryOp.OR, 0, 0)));
		// bool || int
		Assert.assertEquals(true, spyInterpreter.evalBinaryOp(new ASTBinaryOp(f, five, BinaryOp.OR, 0, 0)));
		// int || int
		Assert.assertEquals(true, spyInterpreter.evalBinaryOp(new ASTBinaryOp(zero, five, BinaryOp.OR, 0, 0)));
	}

	@Test
	public void testBinaryOp_EQ() {
		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		ASTIntLiteral a = new ASTIntLiteral(5, 0, 0);
		ASTIntLiteral b = new ASTIntLiteral(5, 0, 0);
		ASTIntLiteral c = new ASTIntLiteral(3, 0, 0);

		Mockito.doReturn(5).when(spyInterpreter).eval(a);
		Mockito.doReturn(5).when(spyInterpreter).eval(b);
		Mockito.doReturn(3).when(spyInterpreter).eval(c);

		Assert.assertEquals(true, spyInterpreter.evalBinaryOp(new ASTBinaryOp(a, b, BinaryOp.EQ, 0, 0)));
		Assert.assertEquals(false, spyInterpreter.evalBinaryOp(new ASTBinaryOp(a, c, BinaryOp.EQ, 0, 0)));
	}

	@Test
	public void testBinaryOp_GT() {
		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		ASTIntLiteral a = new ASTIntLiteral(5, 0, 0);
		ASTIntLiteral b = new ASTIntLiteral(3, 0, 0);
		ASTBoolLiteral boolVal = new ASTBoolLiteral(true, 0, 0);

		Mockito.doReturn(5).when(spyInterpreter).eval(a);
		Mockito.doReturn(3).when(spyInterpreter).eval(b);
		Mockito.doReturn(true).when(spyInterpreter).eval(boolVal);

		Assert.assertEquals(true, spyInterpreter.evalBinaryOp(new ASTBinaryOp(a, b, BinaryOp.GT, 0, 0)));
		Assert.assertThrows(InterpretRuntimeException.class,
				() -> spyInterpreter.evalBinaryOp(new ASTBinaryOp(a, boolVal, BinaryOp.GT, 0, 0)));
	}

	@Test
	public void testBinaryOp_LT() {
		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		ASTIntLiteral a = new ASTIntLiteral(3, 0, 0);
		ASTIntLiteral b = new ASTIntLiteral(5, 0, 0);
		ASTBoolLiteral boolVal = new ASTBoolLiteral(true, 0, 0);

		Mockito.doReturn(3).when(spyInterpreter).eval(a);
		Mockito.doReturn(5).when(spyInterpreter).eval(b);
		Mockito.doReturn(true).when(spyInterpreter).eval(boolVal);

		Assert.assertEquals(true, spyInterpreter.evalBinaryOp(new ASTBinaryOp(a, b, BinaryOp.LT, 0, 0)));
		Assert.assertThrows(InterpretRuntimeException.class,
				() -> spyInterpreter.evalBinaryOp(new ASTBinaryOp(a, boolVal, BinaryOp.LT, 0, 0)));
	}

	@Test
	public void evalBoolLiteral() {
		ASTBoolLiteral boolLiteralTrue = new ASTBoolLiteral(true, 0, 0);
		ASTBoolLiteral boolLiteralFalse = new ASTBoolLiteral(false, 0, 0);

		Assert.assertEquals(true, interpreter.eval(boolLiteralTrue));
		Assert.assertEquals(false, interpreter.eval(boolLiteralFalse));
	}

	@Test
	public void evalIdent() {
		ASTIdent ident = new ASTIdent("a", 0, 0);
		Assert.assertEquals("a", interpreter.eval(ident));
	}

	@Test
	public void evalIntLiteral() {
		ASTIntLiteral intLiteral = new ASTIntLiteral(42, 0, 0);

		Assert.assertEquals(42, interpreter.eval(intLiteral));
	}

	@Test
	public void evalStringLiteral() {
		ASTStringLiteral stringLiteral = new ASTStringLiteral("Hello, World!", 0, 0);

		Assert.assertEquals("Hello, World!", interpreter.eval(stringLiteral));
	}

	@Test
	public void evalUnaryOp() {
		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);

		ASTIntLiteral intLiteral = new ASTIntLiteral(5, 0, 0);
		ASTBoolLiteral boolLiteralTrue = new ASTBoolLiteral(true, 0, 0);
		ASTBoolLiteral boolLiteralFalse = new ASTBoolLiteral(false, 0, 0);
		ASTIntLiteral zeroLiteral = new ASTIntLiteral(0, 0, 0);

		Mockito.doReturn(5).when(spyInterpreter).eval(intLiteral);
		Mockito.doReturn(true).when(spyInterpreter).eval(boolLiteralTrue);
		Mockito.doReturn(false).when(spyInterpreter).eval(boolLiteralFalse);
		Mockito.doReturn(0).when(spyInterpreter).eval(zeroLiteral);

		ASTUnaryOp negIntNode = new ASTUnaryOp(intLiteral, UnaryOp.NEG, 0, 0);
		Assert.assertEquals(-5, spyInterpreter.evalUnaryOp(negIntNode));

		ASTUnaryOp notBoolTrueNode = new ASTUnaryOp(boolLiteralTrue, UnaryOp.NOT, 0, 0);
		ASTUnaryOp notBoolFalseNode = new ASTUnaryOp(boolLiteralFalse, UnaryOp.NOT, 0, 0);
		Assert.assertEquals(false, spyInterpreter.evalUnaryOp(notBoolTrueNode));
		Assert.assertEquals(true, spyInterpreter.evalUnaryOp(notBoolFalseNode));

		ASTUnaryOp notIntNode = new ASTUnaryOp(intLiteral, UnaryOp.NOT, 0, 0);
		ASTUnaryOp notZeroNode = new ASTUnaryOp(zeroLiteral, UnaryOp.NOT, 0, 0);
		Assert.assertEquals(true, spyInterpreter.evalUnaryOp(notIntNode));
		Assert.assertEquals(false, spyInterpreter.evalUnaryOp(notZeroNode));
	}
}
