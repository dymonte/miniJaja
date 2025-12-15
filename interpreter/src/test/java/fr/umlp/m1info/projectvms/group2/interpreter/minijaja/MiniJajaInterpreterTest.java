package fr.umlp.m1info.projectvms.group2.interpreter.minijaja;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.decl.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decls.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.exprs.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instrs.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.vars.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.var.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.params.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.type.*;

import fr.umlp.m1info.projectvms.group2.exception.EvalRuntimeException;
import fr.umlp.m1info.projectvms.group2.exception.InterpretRuntimeException;
import fr.umlp.m1info.projectvms.group2.exception.SymbolNotFoundException;

import fr.umlp.m1info.projectvms.group2.logger.InterpreterLogger;

import fr.umlp.m1info.projectvms.group2.memory.Memory;
import fr.umlp.m1info.projectvms.group2.memory.heap.Heap;
import fr.umlp.m1info.projectvms.group2.memory.stack.Stack;
import fr.umlp.m1info.projectvms.group2.memory.symtable.SymTable;
import fr.umlp.m1info.projectvms.group2.utils.Type;

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
	public void testConstructorNull() {
		Assert.assertThrows(NullPointerException.class, () -> new MiniJajaInterpreter(null));
	}

	@Test
	public void testInterpretNull() {
		Assert.assertThrows(InterpretRuntimeException.class, () -> interpreter.interpret(null));
	}

	@Test
	public void testInterpretNotExist() {
		ASTBoolLiteral bool = new ASTBoolLiteral(true, 0, 0);
		Assert.assertThrows(InterpretRuntimeException.class,
				() -> interpreter.interpret(bool));
	}

	@Test
	public void testEvalNull() {
		Assert.assertThrows(EvalRuntimeException.class, () -> interpreter.eval(null));
	}

	@Test
	public void testEvalNotExist() {
		ASTWrite write = new ASTWrite(new ASTIdent("a", 0, 0), false, 0, 0);
		Assert.assertThrows(EvalRuntimeException.class,
				() -> interpreter.eval(write));
	}

	@Test
	public void testInterpretAssignmentEq() {
		ASTIdent ident = new ASTIdent("i", 0, 0);
		ASTIdent ident2 = new ASTIdent("a", 0, 0);
		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		Mockito.doReturn(1).when(spyInterpreter).eval(ident);
		Mockito.doReturn(2).when(spyInterpreter).eval(ident2);

		memory.declVar("i", 1, Type.INT);

		ASTAssignmentOp assignmentOp = new ASTAssignmentOp(ident, ident2, AssignmentOp.EQ, 0, 0);
		spyInterpreter.interpret(assignmentOp);

		Assert.assertEquals(2, memory.getVal("i"));
	}

	@Test
	public void testInterpretAssignmentAddVar() {
		ASTIdent ident = new ASTIdent("i", 0, 0);
		ASTIdent ident2 = new ASTIdent("a", 0, 0);
		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		Mockito.doReturn(1).when(spyInterpreter).eval(ident);
		Mockito.doReturn(2).when(spyInterpreter).eval(ident2);

		memory.declVar("i", 1, Type.INT);

		ASTAssignmentOp assignmentOp = new ASTAssignmentOp(ident, ident2, AssignmentOp.ADD, 0, 0);
		spyInterpreter.interpret(assignmentOp);

		Assert.assertEquals(3, memory.getVal("i"));
	}

	@Test
	public void testInterpretAssignmentAddNb() {
		ASTIdent ident = new ASTIdent("i", 0, 0);
		ASTIntLiteral intLiteral = new ASTIntLiteral(3, 0, 0);
		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		Mockito.doReturn(1).when(spyInterpreter).eval(ident);
		Mockito.doReturn(3).when(spyInterpreter).eval(intLiteral);

		memory.declVar("i", 1, Type.INT);

		ASTAssignmentOp assignmentOp = new ASTAssignmentOp(ident, intLiteral, AssignmentOp.ADD, 0, 0);
		spyInterpreter.interpret(assignmentOp);

		Assert.assertEquals(4, memory.getVal("i"));
	}

	@Test
	public void testInterpretAssignmentAddArray() {
		ASTIdent identValue = new ASTIdent("i", 0, 0);
		ASTIdent ident = new ASTIdent("tab", 0, 0);
		ASTIntLiteral intLiteral = new ASTIntLiteral(0, 0, 0);
		ASTArrayAccess array = new ASTArrayAccess(ident, intLiteral, 0, 0);
		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		Mockito.doReturn(1).when(spyInterpreter).eval(identValue);
		Mockito.doReturn(1).when(spyInterpreter).eval(ident);
		Mockito.doReturn(0).when(spyInterpreter).eval(intLiteral);

		memory.declVar("i", 2, Type.INT);
		memory.declArray("tab", 1, Type.INT);
		memory.updateValArray("tab", 0, 1);
		// i = tab[0] => 1
		ASTAssignmentOp assignmentOp = new ASTAssignmentOp(identValue, array, AssignmentOp.EQ, 0, 0);
		spyInterpreter.interpret(assignmentOp);
		Assert.assertEquals(1, memory.getValArray("tab", 0));
		Assert.assertEquals(1, memory.getVal("i"));

		ASTAssignmentOp assignmentOp2 = new ASTAssignmentOp(array, identValue, AssignmentOp.ADD, 0, 0);
		spyInterpreter.interpret(assignmentOp2);
		// tab[0] = i
		Assert.assertEquals(1, memory.getVal("i"));
		Assert.assertEquals(2, memory.getValArray("tab", 0));
	}

	@Test
	public void testAssignmentIncrementException() {
		class ErrorClass extends ASTLValue {
			ErrorClass() {
				super(0, 0);
			}

			@Override
			public String toString() {
				return "";
			}
		}

		ASTIdent identValue = new ASTIdent("i", 0, 0);
		ErrorClass errorClass = new ErrorClass();

		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		Mockito.doReturn(0).when(spyInterpreter).eval(errorClass);
		Mockito.doReturn(1).when(spyInterpreter).eval(identValue);

		ASTAssignmentOp assignmentOp = new ASTAssignmentOp(errorClass, identValue, AssignmentOp.ADD, 0, 0);
		Assert.assertThrows(InterpretRuntimeException.class, () -> spyInterpreter.interpret(assignmentOp));
		ASTIncrementOp incrementOp = new ASTIncrementOp(errorClass, IncrementOp.INC, 0, 0);
		Assert.assertThrows(InterpretRuntimeException.class, () -> spyInterpreter.interpret(incrementOp));
	}

	@Test
	public void testInterpretIncrementInc() {
		ASTIdent ident = new ASTIdent("i", 0, 0);
		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		Mockito.doReturn(1).when(spyInterpreter).eval(ident);

		memory.declVar("i", 1, Type.INT);

		ASTIncrementOp incrementOp = new ASTIncrementOp(ident, IncrementOp.INC, 0, 0);
		spyInterpreter.interpret(incrementOp);

		Assert.assertEquals(2, memory.getVal("i"));
	}

	@Test
	public void testInterpretIncrementIncArray() {
		ASTIdent ident = new ASTIdent("tab", 0, 0);
		ASTIntLiteral intLiteral = new ASTIntLiteral(0, 0, 0);
		ASTArrayAccess array = new ASTArrayAccess(ident, intLiteral, 0, 0);
		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		Mockito.doReturn(0).when(spyInterpreter).eval(intLiteral);

		memory.declArray("tab", 1, Type.INT);
		memory.updateValArray("tab", 1, 0);

		ASTIncrementOp incrementOp = new ASTIncrementOp(array, IncrementOp.INC, 0, 0);
		spyInterpreter.interpret(incrementOp);

		Assert.assertEquals(1, memory.getValArray("tab", 0));
	}

	@Test
	public void testInterpretWriteInt() {
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
	public void testInterpretWriteArray() {
		ASTArrayAccess arrayAccess = new ASTArrayAccess(new ASTIdent("tab", 0, 0), new ASTIntLiteral(1, 0, 0),
				0, 0);
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
	public void testInterpretWriteString() {
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
	public void testInterpretWriteLnString() {
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
	public void testInterpretWriteInvalid() {
		ASTReturn mockNode = Mockito.mock(ASTReturn.class);
		ASTWrite mockWrite = Mockito.mock(ASTWrite.class);

		Mockito.when(mockWrite.getValue()).thenReturn(mockNode);
		Assert.assertThrows(EvalRuntimeException.class, () -> interpreter.interpret(mockWrite));
	}

	@Test
	public void testInterpretIfIf() {
		ASTStringLiteral stringLiteral = new ASTStringLiteral("WRITE", 0, 0);
		ASTStringLiteral stringLiteral2 = new ASTStringLiteral("AH", 0, 0);
		ASTInstrsChain chain = new ASTInstrsChain(new ASTWrite(stringLiteral, false, 0, 0),
				new ASTInstrsNil(0, 0), 0, 0);
		ASTInstrsChain chain2 = new ASTInstrsChain(new ASTWrite(stringLiteral, false, 0, 0),
				new ASTInstrsNil(0, 0), 0,
				0);
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
	public void testInterpretIfElse() {
		ASTStringLiteral stringLiteral = new ASTStringLiteral("WRITE", 0, 0);
		ASTStringLiteral stringLiteral2 = new ASTStringLiteral("AH", 0, 0);
		ASTInstrsChain chain = new ASTInstrsChain(new ASTWrite(stringLiteral, false, 0, 0),
				new ASTInstrsNil(0, 0), 0, 0);
		ASTInstrsChain chain2 = new ASTInstrsChain(new ASTWrite(stringLiteral2, false, 0, 0),
				new ASTInstrsNil(0, 0), 0,
				0);
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
	public void testInterpretIfException() {
		ASTStringLiteral stringLiteral = new ASTStringLiteral("WRITE", 0, 0);
		ASTStringLiteral stringLiteral2 = new ASTStringLiteral("AH", 0, 0);
		ASTInstrsChain chain = new ASTInstrsChain(new ASTWrite(stringLiteral, false, 0, 0),
				new ASTInstrsNil(0, 0), 0, 0);
		ASTInstrsChain chain2 = new ASTInstrsChain(new ASTWrite(stringLiteral, false, 0, 0),
				new ASTInstrsNil(0, 0), 0,
				0);
		ASTBoolLiteral bool = new ASTBoolLiteral(true, 0, 0);
		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		Mockito.doReturn("WRITE").when(spyInterpreter).eval(stringLiteral);
		Mockito.doReturn("AH").when(spyInterpreter).eval(stringLiteral2);
		Mockito.doReturn(null).when(spyInterpreter).eval(bool);

		ASTIf astIf = new ASTIf(bool, chain, chain2, 0, 0);
		Assert.assertThrows(InterpretRuntimeException.class, () -> spyInterpreter.interpret(astIf));
	}

	@Test
	public void testInterpretWhile() {
		int[] i = { 0 };
		ASTBoolLiteral bool = new ASTBoolLiteral(false, 0, 0);
		ASTStringLiteral stringLiteral = new ASTStringLiteral("WRITE", 0, 0);
		ASTInstrsChain chain = new ASTInstrsChain(new ASTWrite(stringLiteral, false, 0, 0),
				new ASTInstrsNil(0, 0), 0, 0);

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
	public void testInterpretReturn() {
		new ASTClass(new ASTIdent("A", 0, 0), new ASTDeclsNil(0, 0),
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
	public void testInterpretMethodCallInstrInt() {
		new ASTClass(new ASTIdent("A", 0, 0), new ASTDeclsNil(0, 0),
				new ASTMainMethod(new ASTVarsNil(0, 0), new ASTInstrsNil(0, 0), 0, 0), 0, 0);
		int[] i = { 0, 0 };
		ASTVoidType type = new ASTVoidType(0, 0);
		ASTIdent ident = new ASTIdent("fct", 0, 0);
		ASTIdent identParam = new ASTIdent("i", 0, 0);
		ASTIdent identWrite = new ASTIdent("i", 0, 0);
		ASTParam param = new ASTParam(new ASTIntType(0, 0), identParam, 0, 0);
		ASTParams params = new ASTParamsChain(param, new ASTParamsNil(0, 0), 0, 0);
		ASTVars vars = new ASTVarsNil(0, 0);
		ASTInstrs instrs = new ASTInstrsChain(new ASTWrite(identWrite, false, 0, 0), new ASTInstrsNil(0, 0), 0,
				0);

		ASTIntLiteral intLiteral = new ASTIntLiteral(1, 0, 0);
		ASTExprs exprs = new ASTExprsChain(intLiteral, new ASTExprsNil(0, 0), 0, 0);

		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		Mockito.doReturn(1).when(spyInterpreter).eval(intLiteral);
		Mockito.doAnswer(invocation -> i[0]++).when(spyInterpreter).interpret(Mockito.isA(ASTVarsNil.class));
		Mockito.doAnswer(invocation -> i[1]++).when(spyInterpreter).interpret(Mockito.isA(ASTWrite.class));

		ASTMethod method = new ASTMethod(type, ident, params, vars, instrs, 0, 0);

		memory.declVar("A", 0, Type.INT);
		memory.declMeth(ident.getName(), method, List.of(Type.INT), Type.VOID);

		ASTMethodCallInstr methodCall = new ASTMethodCallInstr(ident, exprs, 0, 0);
		spyInterpreter.interpret(methodCall);

		Assert.assertEquals(1, i[0]);
		Assert.assertEquals(1, i[1]);
	}

	@Test
	public void testInterpretMethodCallInstrBool() {
		new ASTClass(new ASTIdent("A", 0, 0), new ASTDeclsNil(0, 0),
				new ASTMainMethod(new ASTVarsNil(0, 0), new ASTInstrsNil(0, 0), 0, 0), 0, 0);
		int[] i = { 0, 0 };
		ASTVoidType type = new ASTVoidType(0, 0);
		ASTIdent ident = new ASTIdent("fct", 0, 0);
		ASTIdent identParam = new ASTIdent("i", 0, 0);
		ASTIdent identWrite = new ASTIdent("i", 0, 0);
		ASTParam param = new ASTParam(new ASTBoolType(0, 0), identParam, 0, 0);
		ASTParams params = new ASTParamsChain(param, new ASTParamsNil(0, 0), 0, 0);
		ASTVars vars = new ASTVarsNil(0, 0);
		ASTInstrs instrs = new ASTInstrsChain(new ASTWrite(identWrite, false, 0, 0), new ASTInstrsNil(0, 0), 0,
				0);

		ASTBoolLiteral bool = new ASTBoolLiteral(false, 0, 0);
		ASTExprs exprs = new ASTExprsChain(bool, new ASTExprsNil(0, 0), 0, 0);

		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		Mockito.doReturn(false).when(spyInterpreter).eval(bool);
		Mockito.doAnswer(invocation -> i[0]++).when(spyInterpreter).interpret(Mockito.isA(ASTVarsNil.class));
		Mockito.doAnswer(invocation -> i[1]++).when(spyInterpreter).interpret(Mockito.isA(ASTWrite.class));

		ASTMethod method = new ASTMethod(type, ident, params, vars, instrs, 0, 0);

		memory.declVar("A", 0, Type.INT);
		memory.declMeth(ident.getName(), method, List.of(Type.BOOL), Type.VOID);

		ASTMethodCallInstr methodCall = new ASTMethodCallInstr(ident, exprs, 0, 0);
		spyInterpreter.interpret(methodCall);

		Assert.assertEquals(1, i[0]);
		Assert.assertEquals(1, i[1]);
	}

	@Test
	public void testInterpretMethodCallInstrException() {
		int[] i = { 0, 0 };
		ASTVoidType type = new ASTVoidType(0, 0);
		ASTIdent ident = new ASTIdent("fct", 0, 0);
		ASTIdent identParam = new ASTIdent("i", 0, 0);
		ASTIdent identWrite = new ASTIdent("i", 0, 0);
		ASTParam param = new ASTParam(new ASTBoolType(0, 0), identParam, 0, 0);
		ASTParams params = new ASTParamsChain(param, new ASTParamsNil(0, 0), 0, 0);
		ASTVars vars = new ASTVarsNil(0, 0);
		ASTInstrs instrs = new ASTInstrsChain(new ASTWrite(identWrite, false, 0, 0), new ASTInstrsNil(0, 0), 0,
				0);

		ASTBoolLiteral bool = new ASTBoolLiteral(false, 0, 0);
		ASTExprs exprs = new ASTExprsChain(bool, new ASTExprsNil(0, 0), 0, 0);

		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		Mockito.doReturn(null).when(spyInterpreter).eval(bool);
		Mockito.doAnswer(invocation -> i[0]++).when(spyInterpreter).interpret(Mockito.isA(ASTVarsNil.class));
		Mockito.doAnswer(invocation -> i[1]++).when(spyInterpreter).interpret(Mockito.isA(ASTWrite.class));

		ASTMethod method = new ASTMethod(type, ident, params, vars, instrs, 0, 0);

		memory.declMeth(ident.getName(), method, List.of(Type.BOOL), Type.VOID);

		ASTMethodCallInstr methodCall = new ASTMethodCallInstr(ident, exprs, 0, 0);
		Assert.assertThrows(InterpretRuntimeException.class, () -> spyInterpreter.interpret(methodCall));
	}

	@Test
	public void interpretMethodCallExprIntTest() {
		int[] i = { 0, 0 };
		ASTVoidType type = new ASTVoidType(0, 0);
		ASTIdent ident = new ASTIdent("fct", 0, 0);
		ASTIdent identParam = new ASTIdent("i", 0, 0);
		ASTParam param = new ASTParam(new ASTIntType(0, 0), identParam, 0, 0);
		ASTParams params = new ASTParamsChain(param, new ASTParamsNil(0, 0), 0, 0);
		ASTVars vars = new ASTVarsNil(0, 0);
		ASTIntLiteral intLiteral = new ASTIntLiteral(1, 0, 0);
		ASTReturn astReturn = new ASTReturn(intLiteral, 0, 0);
		ASTInstrs instrs1 = new ASTInstrsChain(astReturn, new ASTInstrsNil(0, 0), 0, 0);

		ASTExprs exprs = new ASTExprsChain(intLiteral, new ASTExprsNil(0, 0), 0, 0);

		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		Mockito.doReturn(1).when(spyInterpreter).eval(intLiteral);
		Mockito.doAnswer(invocation -> i[0]++).when(spyInterpreter).interpret(Mockito.isA(ASTVarsNil.class));
		Mockito.doAnswer(invocation -> i[1]++).when(spyInterpreter).interpret(Mockito.isA(ASTReturn.class));
		new ASTClass(new ASTIdent("expr", 0, 0), new ASTDeclsNil(0, 0),
				new ASTMainMethod(new ASTVarsNil(0, 0), new ASTInstrsNil(0, 0), 0, 0), 0, 0);

		memory.declVar("expr", 1, Type.INT);
		ASTMethod method = new ASTMethod(type, ident, params, vars, instrs1, 0, 0);

		memory.declMeth(ident.getName(), method, List.of(Type.INT), Type.VOID);

		ASTMethodCallExpr methodCall = new ASTMethodCallExpr(ident, exprs, 0, 0);
		Assert.assertEquals(1, spyInterpreter.eval(methodCall));

		Assert.assertEquals(1, i[0]);
		Assert.assertEquals(1, i[1]);
	}

	@Test
	public void testEvalArrayAccess() {
		ASTIdent ident = new ASTIdent("tab", 0, 0);
		ASTIntLiteral intLiteral1 = new ASTIntLiteral(1, 0, 0);
		ASTIntLiteral intLiteral2 = new ASTIntLiteral(2, 0, 0);
		ASTIntLiteral intLiteral3 = new ASTIntLiteral(3, 0, 0);
		ASTArrayAccess arrayAccess1 = new ASTArrayAccess(ident, intLiteral1, 0, 0);
		ASTArrayAccess arrayAccess2 = new ASTArrayAccess(ident, intLiteral2, 0, 0);
		ASTArrayAccess arrayAccess3 = new ASTArrayAccess(ident, intLiteral3, 0, 0);

		memory.declArray("tab", 3, Type.INT);
		memory.updateValArray("tab", 0, 1);
		memory.updateValArray("tab", 1, 2);
		memory.updateValArray("tab", 2, 3);

		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		Mockito.doReturn(0).when(spyInterpreter).eval(intLiteral1);
		Mockito.doReturn(1).when(spyInterpreter).eval(intLiteral2);
		Mockito.doReturn(2).when(spyInterpreter).eval(intLiteral3);

		Assert.assertEquals(1, spyInterpreter.evalArrayAccess(arrayAccess1));
		Assert.assertEquals(2, spyInterpreter.evalArrayAccess(arrayAccess2));
		Assert.assertEquals(3, spyInterpreter.evalArrayAccess(arrayAccess3));
	}

	@Test
	public void testBinaryOp_ADD() {
		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		ASTIntLiteral a = new ASTIntLiteral(5, 0, 0);
		ASTIntLiteral b = new ASTIntLiteral(10, 0, 0);

		Mockito.doReturn(5).when(spyInterpreter).eval(a);
		Mockito.doReturn(10).when(spyInterpreter).eval(b);

		ASTBinaryOp node = new ASTBinaryOp(a, b, BinaryOp.ADD, 0, 0);
		Assert.assertEquals(15, spyInterpreter.eval(node));
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
		Assert.assertEquals(5, spyInterpreter.eval(node));

		ASTBinaryOp invalidNode = new ASTBinaryOp(a, boolVal, BinaryOp.SUB, 0, 0);
		Assert.assertThrows(InterpretRuntimeException.class, () -> spyInterpreter.eval(invalidNode));
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
		Assert.assertEquals(12, spyInterpreter.eval(node));

		ASTBinaryOp invalidNode = new ASTBinaryOp(a, boolVal, BinaryOp.MUL, 0, 0);
		Assert.assertThrows(InterpretRuntimeException.class, () -> spyInterpreter.eval(invalidNode));
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
		Assert.assertEquals(5, spyInterpreter.eval(node));

		ASTBinaryOp zeroNode = new ASTBinaryOp(a, zero, BinaryOp.DIV, 0, 0);
		Assert.assertThrows(InterpretRuntimeException.class, () -> spyInterpreter.eval(zeroNode));
	}

	@Test
	public void testBinaryOp_AND() {
		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		ASTBoolLiteral t = new ASTBoolLiteral(true, 0, 0);
		ASTBoolLiteral f = new ASTBoolLiteral(false, 0, 0);

		Mockito.doReturn(true).when(spyInterpreter).eval(t);
		Mockito.doReturn(false).when(spyInterpreter).eval(f);

		Assert.assertEquals(false, spyInterpreter.eval(new ASTBinaryOp(t, f, BinaryOp.AND, 0, 0)));
	}

	@Test
	public void testBinaryOp_OR() {
		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		ASTBoolLiteral t = new ASTBoolLiteral(true, 0, 0);
		ASTBoolLiteral f = new ASTBoolLiteral(false, 0, 0);

		Mockito.doReturn(true).when(spyInterpreter).eval(t);
		Mockito.doReturn(false).when(spyInterpreter).eval(f);

		Assert.assertEquals(true, spyInterpreter.eval(new ASTBinaryOp(t, f, BinaryOp.OR, 0, 0)));
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

		Assert.assertEquals(true, spyInterpreter.eval(new ASTBinaryOp(a, b, BinaryOp.EQ, 0, 0)));
		Assert.assertEquals(false, spyInterpreter.eval(new ASTBinaryOp(a, c, BinaryOp.EQ, 0, 0)));
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
		ASTBinaryOp binary = new ASTBinaryOp(a, boolVal, BinaryOp.GT, 0, 0);
		Assert.assertEquals(true, spyInterpreter.eval(new ASTBinaryOp(a, b, BinaryOp.GT, 0, 0)));
		Assert.assertThrows(InterpretRuntimeException.class,
				() -> spyInterpreter.eval(binary));
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

		ASTBinaryOp binary = new ASTBinaryOp(a, boolVal, BinaryOp.LT, 0, 0);
		Assert.assertEquals(true, spyInterpreter.eval(new ASTBinaryOp(a, b, BinaryOp.LT, 0, 0)));
		Assert.assertThrows(InterpretRuntimeException.class,
				() -> spyInterpreter.eval(binary));
	}

	@Test
	public void testTernaryOp() {

		List<Boolean> booleans = List.of(new Boolean[]{true, false});

		for (Boolean b: booleans) {
			MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
			ASTIntLiteral firstCase = new ASTIntLiteral(3, 0, 0);
			ASTIntLiteral secondCase = new ASTIntLiteral(5, 0, 0);
			ASTBoolLiteral condition = new ASTBoolLiteral(b, 0, 0);

			Mockito.doReturn(3).when(spyInterpreter).eval(firstCase);
			Mockito.doReturn(5).when(spyInterpreter).eval(secondCase);
			Mockito.doReturn(b).when(spyInterpreter).eval(condition);

			ASTTernaryOp ternaryOp = new ASTTernaryOp(condition, firstCase, secondCase, 0, 0);
			Assert.assertEquals(b ? 3 : 5, spyInterpreter.eval(ternaryOp));
		}
		
	}

	@Test
	public void testEvalBoolLiteral() {
		ASTBoolLiteral boolLiteralTrue = new ASTBoolLiteral(true, 0, 0);
		ASTBoolLiteral boolLiteralFalse = new ASTBoolLiteral(false, 0, 0);

		Assert.assertEquals(true, interpreter.eval(boolLiteralTrue));
		Assert.assertEquals(false, interpreter.eval(boolLiteralFalse));
	}

	@Test
	public void testEvalIdent() {
		ASTIdent ident = new ASTIdent("a", 0, 0);
		memory.declVar("a", 1, Type.INT);
		Assert.assertEquals(1, interpreter.eval(ident));
	}

	@Test
	public void testEvalIntLiteral() {
		ASTIntLiteral intLiteral = new ASTIntLiteral(42, 0, 0);

		Assert.assertEquals(42, interpreter.eval(intLiteral));
	}

	@Test
	public void testEvalStringLiteral() {
		ASTStringLiteral stringLiteral = new ASTStringLiteral("Hello, World!", 0, 0);

		Assert.assertEquals("Hello, World!", interpreter.eval(stringLiteral));
	}

	@Test
	public void testEvalUnaryOp() {
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
		Assert.assertEquals(-5, spyInterpreter.eval(negIntNode));

		ASTUnaryOp notBoolTrueNode = new ASTUnaryOp(boolLiteralTrue, UnaryOp.NOT, 0, 0);
		ASTUnaryOp notBoolFalseNode = new ASTUnaryOp(boolLiteralFalse, UnaryOp.NOT, 0, 0);
		Assert.assertEquals(false, spyInterpreter.eval(notBoolTrueNode));
		Assert.assertEquals(true, spyInterpreter.eval(notBoolFalseNode));
	}

	@Test
	public void TestFreeVar() {
		ASTIdent ident = new ASTIdent("a", 0, 0);

		memory.declVar("a", 0, Type.INT);
		interpreter.freeVar(new ASTVarSimple(new ASTIntType(0, 0), ident, null, 0, 0));
		Assert.assertThrows(SymbolNotFoundException.class, () -> memory.getVal("a"));
	}

	@Test
	public void TestFreeVarsChain() {
		ASTIdent ident = new ASTIdent("a", 0, 0);

		memory.declVar("a", 0, Type.INT);
		interpreter.freeVarsChain(
				new ASTVarsChain(new ASTVarSimple(new ASTIntType(0, 0), ident, null, 0, 0),
						new ASTVarsNil(0, 0), 0, 0));
		Assert.assertThrows(SymbolNotFoundException.class, () -> memory.getVal("a"));
	}

	@Test
	public void TestFree() {
		ASTIdent ident = new ASTIdent("a", 0, 0);

		memory.declVar("a", 0, Type.INT);
		interpreter.free(
				new ASTVarsChain(new ASTVarSimple(new ASTIntType(0, 0), ident, null, 0, 0),
						new ASTVarsNil(0, 0), 0, 0));
		Assert.assertThrows(SymbolNotFoundException.class, () -> memory.getVal("a"));
	}

	@Test
	public void testFreeException() {
		ASTIdent ident = new ASTIdent("a", 0, 0);

		Assert.assertThrows(InterpretRuntimeException.class, () -> interpreter.free(ident));
		Assert.assertThrows(InterpretRuntimeException.class, () -> interpreter.free(null));
	}

	@Test
	public void TestInterpretDeclsChain() {
		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);

		ASTMethod method1 = Mockito.mock(ASTMethod.class);
		ASTMethod method2 = Mockito.mock(ASTMethod.class);

		ASTDeclsNil nil = new ASTDeclsNil(0, 0);
		ASTDeclsChain innerChain = new ASTDeclsChain(method2, nil, 0, 0);
		ASTDeclsChain outerChain = new ASTDeclsChain(method1, innerChain, 0, 0);

		Mockito.doNothing().when(spyInterpreter).interpret(Mockito.any(ASTMethod.class));

		spyInterpreter.interpret(outerChain);

		Mockito.verify(spyInterpreter).interpret(method1);
		Mockito.verify(spyInterpreter).interpret(method2);
		Mockito.verify(spyInterpreter, Mockito.times(2)).interpretDeclsChain(Mockito.any(ASTDeclsChain.class));
		Mockito.verify(spyInterpreter).interpretDeclsNil(nil);
	}

	@Test
	public void testInterpretClass() {
		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		ASTClass astClass = Mockito.mock(ASTClass.class);
		ASTIdent astIdent = new ASTIdent("class", 0, 0);
		ASTDeclsChain decls = Mockito.mock(ASTDeclsChain.class);
		ASTMainMethod main = Mockito.mock(ASTMainMethod.class);

		Mockito.when(astClass.getDecls()).thenReturn(decls);
		Mockito.when(astClass.getIdent()).thenReturn(astIdent);
		Mockito.when(astClass.getMainMethod()).thenReturn(main);
		Mockito.doNothing().when(spyInterpreter).interpretDeclsChain(Mockito.any());
		Mockito.doNothing().when(spyInterpreter).interpretMainMethod(Mockito.any());
		Mockito.doNothing().when(spyInterpreter).free(decls);

		spyInterpreter.interpret(astClass);

		Mockito.verify(spyInterpreter, Mockito.times(1)).interpretDeclsChain(decls);
		Mockito.verify(spyInterpreter, Mockito.times(1)).interpretMainMethod(main);
	}

	@Test
	public void testInterpretMainMeth() {
		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		ASTMainMethod mainMethod = Mockito.mock(ASTMainMethod.class);
		ASTVarsChain vars = Mockito.mock(ASTVarsChain.class);
		ASTInstrs instrs = Mockito.mock(ASTInstrs.class);

		Mockito.when(mainMethod.getVars()).thenReturn(vars);
		Mockito.when(mainMethod.getInstrs()).thenReturn(instrs);
		Mockito.doNothing().when(spyInterpreter).interpret(Mockito.any());
		Mockito.doNothing().when(spyInterpreter).free(vars);

		spyInterpreter.interpretMainMethod(mainMethod);

		Mockito.verify(spyInterpreter, Mockito.times(1)).interpret(vars);
		Mockito.verify(spyInterpreter, Mockito.times(1)).interpret(instrs);
	}

	@Test
	public void testInterpretMeth() {
		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		ASTMethod method = Mockito.mock(ASTMethod.class);
		ASTIdent ident = new ASTIdent("method", 0, 0);
		ASTParams params = new ASTParamsNil(0, 0);
		ASTVoidType type = new ASTVoidType(0, 0);
		List<Type> types = MiniJajaInterpreterUtils.collectParamTypes(params);

		Mockito.when(method.getIdent()).thenReturn(ident);
		Mockito.when(method.getParams()).thenReturn(params);
		Mockito.when(method.getType()).thenReturn(type);

		spyInterpreter.interpret(method);

		String fullIdent = memory.getMethKey("method", types);
		Assert.assertNotNull(memory.getVal(fullIdent));
	}

	@Test
	public void testInterpretVarSimple() {
		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		ASTTypeMeth intType = new ASTIntType(0, 0);
		ASTTypeMeth boolType = new ASTBoolType(0, 0);
		ASTIdent ident = new ASTIdent("x", 0, 0);
		ASTIdent ident2 = new ASTIdent("x2", 0, 0);
		ASTIdent ident3 = new ASTIdent("x3", 0, 0);
		ASTIdent ident4 = new ASTIdent("x4", 0, 0);
		ASTIntLiteral intLiteral = new ASTIntLiteral(3, 0, 0);
		ASTBoolLiteral boolLiteral = new ASTBoolLiteral(true, 0, 0);

		Mockito.doReturn(3).when(spyInterpreter).eval(intLiteral);
		Mockito.doReturn(true).when(spyInterpreter).eval(boolLiteral);

		ASTVarSimple varSimple = new ASTVarSimple(intType, ident, new ASTVExpr(intLiteral, 0, 0), 0, 0);
		ASTVarSimple varSimple2 = new ASTVarSimple(intType, ident2, null, 0, 0);
		ASTVarSimple varSimple3 = new ASTVarSimple(intType, ident3, new ASTVExpr(boolLiteral, 0, 0), 0, 0);
		ASTVarSimple varSimple4 = new ASTVarSimple(boolType, ident4, new ASTVExpr(boolLiteral, 0, 0), 0, 0);

		spyInterpreter.interpret(varSimple);
		spyInterpreter.interpret(varSimple2);
		spyInterpreter.interpret(varSimple4);
		Assert.assertEquals(3, memory.getVal(ident.getName()));
		Assert.assertNull(memory.getVal(ident2.getName()));
		Assert.assertThrows(InterpretRuntimeException.class, () -> spyInterpreter.interpret(varSimple3));
		Assert.assertEquals(true, memory.getVal(ident4.getName()));
	}

	@Test
	public void testInterpretVarConst() {
		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		ASTType intType = new ASTIntType(0, 0);
		ASTType boolType = new ASTBoolType(0, 0);
		ASTIdent ident = new ASTIdent("x", 0, 0);
		ASTIdent ident2 = new ASTIdent("x2", 0, 0);
		ASTIdent ident3 = new ASTIdent("x3", 0, 0);
		ASTIdent ident4 = new ASTIdent("x4", 0, 0);
		ASTIntLiteral intLiteral = new ASTIntLiteral(3, 0, 0);
		ASTBoolLiteral boolLiteral = new ASTBoolLiteral(true, 0, 0);

		Mockito.doReturn(3).when(spyInterpreter).eval(intLiteral);
		Mockito.doReturn(true).when(spyInterpreter).eval(boolLiteral);

		ASTVarConst varConst = new ASTVarConst(intType, ident, new ASTVExpr(intLiteral, 0, 0), 0, 0);
		ASTVarConst varConst2 = new ASTVarConst(intType, ident2, null, 0, 0);
		ASTVarConst varConst3 = new ASTVarConst(intType, ident3, new ASTVExpr(boolLiteral, 0, 0), 0, 0);
		ASTVarConst varConst4 = new ASTVarConst(boolType, ident4, new ASTVExpr(boolLiteral, 0, 0), 0, 0);

		spyInterpreter.interpret(varConst);
		spyInterpreter.interpret(varConst4);
		Assert.assertEquals(3, memory.getVal(ident.getName()));
		spyInterpreter.interpret(varConst2);
		Assert.assertThrows(InterpretRuntimeException.class, () -> spyInterpreter.interpret(varConst3));
		Assert.assertEquals(true, memory.getVal(ident4.getName()));
	}

	@Test
	public void testInterpretVarArray() {
		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		ASTTypeMeth intType = new ASTIntType(0, 0);
		ASTTypeMeth boolType = new ASTBoolType(0, 0);
		ASTIdent ident = new ASTIdent("t", 0, 0);
		ASTIdent ident2 = new ASTIdent("t2", 0, 0);
		ASTIntLiteral intLiteral = new ASTIntLiteral(3, 0, 0);
		ASTIntLiteral intLiteral2 = new ASTIntLiteral(0, 0, 0);
		ASTBoolLiteral boolLiteral = new ASTBoolLiteral(true, 0, 0);

		Mockito.doReturn(3).when(spyInterpreter).eval(intLiteral);

		ASTVarArray tab = new ASTVarArray(intType, ident, intLiteral, 0, 0);
		ASTVarArray tab2 = new ASTVarArray(intType, ident, intLiteral2, 0, 0);
		ASTVarArray tab3 = new ASTVarArray(intType, ident, boolLiteral, 0, 0);
		ASTVarArray tab4 = new ASTVarArray(boolType, ident2, intLiteral, 0, 0);

		spyInterpreter.interpret(tab);
		spyInterpreter.interpret(tab4);

		Assert.assertEquals(Type.INT, memory.getType(ident.getName()));
		Assert.assertThrows(InterpretRuntimeException.class, () -> spyInterpreter.interpret(tab2));
		Assert.assertThrows(InterpretRuntimeException.class, () -> spyInterpreter.interpret(tab3));
		Assert.assertEquals(Type.BOOL, memory.getType(ident2.getName()));
	}

	@Test
	public void TestInterpretVarsChain() {
		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);

		ASTVarSimple var1 = Mockito.mock(ASTVarSimple.class);
		ASTVarSimple var2 = Mockito.mock(ASTVarSimple.class);

		ASTVarsNil nil = new ASTVarsNil(0, 0);
		ASTVarsChain innerChain = new ASTVarsChain(var2, nil, 0, 0);
		ASTVarsChain outerChain = new ASTVarsChain(var1, innerChain, 0, 0);

		Mockito.doNothing().when(spyInterpreter).interpret(Mockito.any(ASTVarSimple.class));

		spyInterpreter.interpret(outerChain);

		Mockito.verify(spyInterpreter).interpret(var1);
		Mockito.verify(spyInterpreter).interpret(var2);
		Mockito.verify(spyInterpreter, Mockito.times(2)).interpret(Mockito.any(ASTVarsChain.class));
		Mockito.verify(spyInterpreter).interpret(nil);
	}

	@Test
	public void TestEValLength() {
		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);

		ASTIdent ident = new ASTIdent("tab", 0, 0);
		ASTLength length = new ASTLength(ident, 0, 0);

		memory.declArray("tab", 2, Type.INT);

		Assert.assertEquals(2, spyInterpreter.eval(length));
	}

	@Test
	public void testFreeDeclsChain() {
		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		ASTDeclsNil end = new ASTDeclsNil(0, 0);
		ASTVarSimple simple = new ASTVarSimple(new ASTIntType(0, 0), new ASTIdent("a", 0, 0), null, 0, 0);
		ASTDecls decls = new ASTDeclsChain(simple, end, 0, 0);

		Mockito.doNothing().when(spyInterpreter).free(end);
		Mockito.doNothing().when(spyInterpreter).free(simple);

		spyInterpreter.free(decls);

		Mockito.verify(spyInterpreter).free(end);
		Mockito.verify(spyInterpreter).free(simple);
	}

	@Test
	public void testFreeDeclsNil() {
		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		ASTDeclsNil end = new ASTDeclsNil(0, 0);

		spyInterpreter.free(end);
	}

	@Test
	public void testFreeMethod() {
		MiniJajaInterpreter spyInterpreter = Mockito.spy(interpreter);
		ASTMethod method = Mockito.mock(ASTMethod.class);
		ASTIdent ident = new ASTIdent("method", 0, 0);
		ASTParams params = new ASTParamsNil(0, 0);
		ASTVoidType type = new ASTVoidType(0, 0);
		List<Type> types = MiniJajaInterpreterUtils.collectParamTypes(params);

		Mockito.when(method.getIdent()).thenReturn(ident);
		Mockito.when(method.getParams()).thenReturn(params);
		Mockito.when(method.getType()).thenReturn(type);

		spyInterpreter.interpret(method);

		String fullIdent = memory.getMethKey("method", types);
		Assert.assertNotNull(memory.getVal(fullIdent));

		spyInterpreter.free(method);

		Assert.assertThrows(SymbolNotFoundException.class, () -> memory.getVal(fullIdent));
	}

	@Test
	public void TestExceptionFree() {
		ASTVarSimple simple = new ASTVarSimple(new ASTIntType(0, 0), new ASTIdent("a", 0, 0), null, 0, 0);
		Assert.assertThrows(InterpretRuntimeException.class,
				() -> interpreter.free(simple));
	}
}
