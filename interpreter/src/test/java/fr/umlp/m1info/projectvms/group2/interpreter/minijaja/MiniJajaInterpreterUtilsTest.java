package fr.umlp.m1info.projectvms.group2.interpreter.minijaja;

import org.junit.Test;

import fr.umlp.m1info.projectvms.group2.exception.InterpretRuntimeException;
import fr.umlp.m1info.projectvms.group2.memory.Memory;
import fr.umlp.m1info.projectvms.group2.memory.heap.Heap;
import fr.umlp.m1info.projectvms.group2.memory.stack.Stack;
import fr.umlp.m1info.projectvms.group2.memory.symtable.SymTable;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTLValue;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTIdent;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.BinaryOp;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.UnaryOp;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.type.ASTIntType;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.params.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decl.ASTParam;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.exprs.*;

import org.junit.Assert;

public class MiniJajaInterpreterUtilsTest {

	@Test
	public void bindParametersNotSameSize1Test() {
		ASTParam param = new ASTParam(new ASTIntType(0, 0), new ASTIdent("a", 0, 0), 0, 0);
		ASTParams params = new ASTParamsChain(param, new ASTParamsNil(0, 0), 0, 0);
		ASTExprs exprs = new ASTExprsNil(0, 0);

		Memory memory = new Memory(new SymTable(10), new Stack(10), new Heap(10));
		MiniJajaInterpreter interpreter = new MiniJajaInterpreter(memory);

		Assert.assertThrows(InterpretRuntimeException.class,
				() -> MiniJajaInterpreterUtils.bindParameters(memory, params, exprs, interpreter));
	}

	@Test
	public void bindParametersNotSameSize2Test() {
		ASTParams params = new ASTParamsNil(0, 0);
		ASTExprs exprs = new ASTExprsChain(new ASTIdent("a", 0, 0), new ASTExprsNil(0, 0), 0, 0);

		Memory memory = new Memory(new SymTable(10), new Stack(10), new Heap(10));
		MiniJajaInterpreter interpreter = new MiniJajaInterpreter(memory);

		Assert.assertThrows(InterpretRuntimeException.class,
				() -> MiniJajaInterpreterUtils.bindParameters(memory, params, exprs, interpreter));
	}

	@Test
	public void binaryOperationTest() {
		Assert.assertThrows(NullPointerException.class,
				() -> MiniJajaInterpreterUtils.binaryOperation(BinaryOp.ADD, null, 1));
		Assert.assertThrows(NullPointerException.class,
				() -> MiniJajaInterpreterUtils.binaryOperation(BinaryOp.ADD, 1, null));
		Assert.assertFalse((Boolean) MiniJajaInterpreterUtils.binaryOperation(BinaryOp.AND, false, false));
		Assert.assertFalse((Boolean) MiniJajaInterpreterUtils.binaryOperation(BinaryOp.AND, false, true));
		Assert.assertFalse((Boolean) MiniJajaInterpreterUtils.binaryOperation(BinaryOp.AND, true, false));
		Assert.assertTrue((Boolean) MiniJajaInterpreterUtils.binaryOperation(BinaryOp.AND, true, true));
		Assert.assertFalse((Boolean) MiniJajaInterpreterUtils.binaryOperation(BinaryOp.OR, false, false));
		Assert.assertTrue((Boolean) MiniJajaInterpreterUtils.binaryOperation(BinaryOp.OR, false, true));
		Assert.assertTrue((Boolean) MiniJajaInterpreterUtils.binaryOperation(BinaryOp.OR, true, false));
		Assert.assertTrue((Boolean) MiniJajaInterpreterUtils.binaryOperation(BinaryOp.OR, true, true));
		Assert.assertFalse((Boolean) MiniJajaInterpreterUtils.binaryOperation(BinaryOp.GT, 1, 3));
		Assert.assertTrue((Boolean) MiniJajaInterpreterUtils.binaryOperation(BinaryOp.GT, 3, 1));
		Assert.assertTrue((Boolean) MiniJajaInterpreterUtils.binaryOperation(BinaryOp.LT, 1, 3));
		Assert.assertFalse((Boolean) MiniJajaInterpreterUtils.binaryOperation(BinaryOp.LT, 3, 1));

		Assert.assertThrows(InterpretRuntimeException.class,
				() -> MiniJajaInterpreterUtils.binaryOperation(BinaryOp.ADD, true, 1));
		Assert.assertThrows(InterpretRuntimeException.class,
				() -> MiniJajaInterpreterUtils.binaryOperation(BinaryOp.ADD, 1, true));
		Assert.assertThrows(InterpretRuntimeException.class,
				() -> MiniJajaInterpreterUtils.binaryOperation(BinaryOp.OR, true, 1));
		Assert.assertThrows(InterpretRuntimeException.class,
				() -> MiniJajaInterpreterUtils.binaryOperation(BinaryOp.OR, 1, true));
	}

	@Test
	public void evalUnaryOpTest() {
		Assert.assertThrows(NullPointerException.class, () -> MiniJajaInterpreterUtils.unaryOperation(UnaryOp.NEG, null));
		Assert.assertEquals(-1, MiniJajaInterpreterUtils.unaryOperation(UnaryOp.NEG, 1));
		Assert.assertFalse((Boolean) MiniJajaInterpreterUtils.unaryOperation(UnaryOp.NOT, true));
	}
}
