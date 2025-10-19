package fr.umlp.m1info.projectvms.group2.ast.minijaja;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.exprs.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instrs.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.type.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.var.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.vars.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decl.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decls.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.params.*;

import org.junit.Test;
import org.junit.Assert;

public class TestASTToString {

	/**
	 * Expr
	 */
	@Test
	public void testToStringIdent() {
		Assert.assertEquals("a", (new ASTIdent("a", 0, 0)).toString());
	}

	@Test
	public void testToStringIntLiteral() {
		Assert.assertEquals("1", (new ASTIntLiteral(1, 0, 0)).toString());
	}

	@Test
	public void testToStringBoolLiteral() {
		Assert.assertEquals("true", (new ASTBoolLiteral(true, 0, 0)).toString());
		Assert.assertEquals("false", (new ASTBoolLiteral(false, 0, 0)).toString());
	}

	@Test
	public void testToStringStringLiteral() {
		Assert.assertEquals("AH", (new ASTStringLiteral("AH", 0, 0)).toString());
	}

	@Test
	public void testToStringArrayAccess() {
		Assert.assertEquals("A[1]",
				(new ASTArrayAccess(new ASTIdent("A", 0, 0), new ASTIntLiteral(1, 0, 0), 0, 0)).toString());
	}

	@Test
	public void testToStringBinaryOp() {
		Assert.assertEquals("1 + 1",
				(new ASTBinaryOp(new ASTIntLiteral(1, 0, 0), new ASTIntLiteral(1, 0, 0), BinaryOp.fromSymbol("+"), 0, 0))
						.toString());
		Assert.assertEquals("+", (BinaryOp.fromSymbol("+")).toString());
	}

	@Test
	public void testToStringUnaryOp() {
		Assert.assertEquals("-1", (new ASTUnaryOp(new ASTIntLiteral(1, 0, 0), UnaryOp.fromSymbol("-"), 0, 0)).toString());
		Assert.assertEquals("-", (UnaryOp.fromSymbol("-")).toString());
	}

	@Test
	public void testToStringLength() {
		Assert.assertEquals("length(tab)", (new ASTLength(new ASTIdent("tab", 0, 0), 0, 0)).toString());
	}

	@Test
	public void testToStringMethodCallExpr() {
		Assert.assertEquals("meth()",
				(new ASTMethodCallExpr(new ASTIdent("meth", 0, 0), new ASTExprsNil(0, 0), 0, 0)).toString());
		Assert.assertEquals("meth(x,)", (new ASTMethodCallExpr(new ASTIdent("meth", 0, 0),
				new ASTExprsChain(new ASTIdent("x", 0, 0), new ASTExprsNil(0, 0), 0, 0), 0, 0)).toString());
	}

	/**
	 * Instr
	 */
	@Test
	public void testToStringMethodCallInstr() {
		Assert.assertEquals("meth()",
				(new ASTMethodCallInstr(new ASTIdent("meth", 0, 0), new ASTExprsNil(0, 0), 0, 0)).toString());
		Assert.assertEquals("meth(x,)", (new ASTMethodCallInstr(new ASTIdent("meth", 0, 0),
				new ASTExprsChain(new ASTIdent("x", 0, 0), new ASTExprsNil(0, 0), 0, 0), 0, 0)).toString());
	}

	@Test
	public void testToStringAssignmentOp() {
		Assert.assertEquals("a = 1",
				(new ASTAssignmentOp(new ASTIdent("a", 0, 0), new ASTIntLiteral(1, 0, 0), AssignmentOp.fromSymbol("="), 0, 0))
						.toString());
		Assert.assertEquals("a[1] = 1",
				(new ASTAssignmentOp(new ASTArrayAccess(new ASTIdent("a", 0, 0), new ASTIntLiteral(1, 0, 0), 0, 0),
						new ASTIntLiteral(1, 0, 0), AssignmentOp.fromSymbol("="), 0, 0)).toString());
		Assert.assertEquals("=", AssignmentOp.fromSymbol("=").toString());
	}

	@Test
	public void testToStringIncrementOp() {
		Assert.assertEquals("a++",
				(new ASTIncrementOp(new ASTIdent("a", 0, 0), IncrementOp.fromSymbol("++"), 0, 0)).toString());
		Assert.assertEquals("a[1]++",
				(new ASTIncrementOp(new ASTArrayAccess(new ASTIdent("a", 0, 0), new ASTIntLiteral(1, 0, 0), 0, 0),
						IncrementOp.fromSymbol("++"), 0, 0)).toString());
		Assert.assertEquals("++", IncrementOp.fromSymbol("++").toString());
	}

	@Test
	public void testToStringReturn() {
		Assert.assertEquals("return a", (new ASTReturn(new ASTIdent("a", 0, 0), 0, 0)).toString());
	}

	@Test
	public void testToStringWhile() {
		Assert.assertEquals("while (true) {\n}",
				(new ASTWhile(new ASTBoolLiteral(true, 0, 0), new ASTInstrsNil(0, 0), 0, 0)).toString());
	}

	@Test
	public void testToStringWrite() {
		Assert.assertEquals("write(\"ah\")", (new ASTWrite(new ASTStringLiteral("ah", 0, 0), false, 0, 0)).toString());
		Assert.assertEquals("writeln(\"ah\")", (new ASTWrite(new ASTStringLiteral("ah", 0, 0), true, 0, 0)).toString());
	}

	@Test
	public void testToStringIf() {
		ASTInstrsChain chain = new ASTInstrsChain(new ASTWrite(new ASTStringLiteral("AH", 0, 0), false, 0, 0),
				new ASTInstrsNil(0, 0), 0, 0);
		Assert.assertEquals("if (true) {\n}",
				(new ASTIf(new ASTBoolLiteral(true, 0, 0), new ASTInstrsNil(0, 0), new ASTInstrsNil(0, 0), 0, 0)).toString());
		Assert.assertEquals("if (true) {\n} else {\nwrite(\"AH\");\n}",
				(new ASTIf(new ASTBoolLiteral(true, 0, 0), new ASTInstrsNil(0, 0), chain, 0, 0)).toString());
	}

	/**
	 * Type
	 */
	@Test
	public void testToStringBool() {
		Assert.assertEquals("boolean", (new ASTBoolType(0, 0)).toString());
	}

	@Test
	public void testToStringInt() {
		Assert.assertEquals("int", (new ASTIntType(0, 0)).toString());
	}

	@Test
	public void testToStringVoid() {
		Assert.assertEquals("void", (new ASTVoidType(0, 0)).toString());
	}

	/**
	 * Var
	 */
	@Test
	public void testToStringVarSimple() {
		Assert.assertEquals("int x",
				(new ASTVarSimple(new ASTIntType(0, 0), new ASTIdent("x", 0, 0), null, 0, 0)).toString());
		Assert.assertEquals("int x = 1", (new ASTVarSimple(new ASTIntType(0, 0), new ASTIdent("x", 0, 0),
				new ASTVExpr(new ASTIntLiteral(1, 0, 0), 0, 0), 0, 0)).toString());
	}

	@Test
	public void testToStringVarCont() {
		Assert.assertEquals("final int x",
				(new ASTVarConst(new ASTIntType(0, 0), new ASTIdent("x", 0, 0), null, 0, 0)).toString());
		Assert.assertEquals("final int x = 1", (new ASTVarConst(new ASTIntType(0, 0), new ASTIdent("x", 0, 0),
				new ASTVExpr(new ASTIntLiteral(1, 0, 0), 0, 0), 0, 0)).toString());
	}

	@Test
	public void testToStringVarArray() {
		Assert.assertEquals("int x[1]",
				(new ASTVarArray(new ASTIntType(0, 0), new ASTIdent("x", 0, 0), new ASTIntLiteral(1, 0, 0), 0, 0)).toString());
	}

	/**
	 * Decl
	 */
	@Test
	public void testToStringMethod() {
		Assert.assertEquals("int function() {\n}", (new ASTMethod(new ASTIntType(0, 0), new ASTIdent("function", 0, 0),
				new ASTParamsNil(0, 0), new ASTVarsNil(0, 0), new ASTInstrsNil(0, 0), 0, 0)).toString());
	}

	@Test
	public void testToStringMainMethod() {
		Assert.assertEquals("main {\n}",
				(new ASTMainMethod(new ASTVarsNil(0, 0), new ASTInstrsNil(0, 0), 0, 0)).toString());
	}

	@Test
	public void testToStringClass() {
		Assert.assertEquals("class A {\nmain {\n}\n}", (new ASTClass(new ASTIdent("A", 0, 0), new ASTDeclsNil(0, 0),
				new ASTMainMethod(new ASTVarsNil(0, 0), new ASTInstrsNil(0, 0), 0, 0), 0, 0)).toString());
	}
}
