package fr.umlp.m1info.projectvms.group2.miniJaja;

import org.junit.Test;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTIdent;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTIntLiteral;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTLength;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTMethodCallExpr;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTUnaryOp;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTVExpr;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.exprs.ASTExprsChain;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.ASTIf;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.ASTReturn;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instrs.ASTInstrsChain;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.params.ASTParamsChain;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.var.ASTVarArray;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.var.ASTVarConst;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.var.ASTVarSimple;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.vars.ASTVarsChain;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decl.ASTParam;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTArrayAccess;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTBinaryOp;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.type.ASTBoolType;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decls.ASTDeclsChain;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.ASTIncrementOp;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.ASTMethodCallInstr;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.ASTWhile;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.ASTWrite;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decl.ASTMainMethod;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.vars.ASTVarsNil;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decl.ASTClass;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decls.ASTDeclsNil;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decl.ASTMethod;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.params.ASTParamsNil;

import org.junit.Assert;

public class TestASTConstructor {
	@Test
	public void constructorVarSimpleTest() {
		Assert.assertThrows(NullPointerException.class, () -> new ASTVarSimple(null, new ASTIdent("a", 0, 0), null, 0, 0));
	}

	@Test
	public void constructorVarConstTest() {
		Assert.assertThrows(NullPointerException.class, () -> new ASTVarConst(null, new ASTIdent("a", 0, 0), null, 0, 0));
	}

	@Test
	public void constructorIfTest() {
		Assert.assertThrows(NullPointerException.class, () -> new ASTIf(null, null, null, 0, 0));
		Assert.assertThrows(NullPointerException.class, () -> new ASTIf(new ASTIntLiteral(1, 0, 0), null, null, 0, 0));
	}

	@Test
	public void constructorBinaryOpTest() {
		Assert.assertThrows(NullPointerException.class, () -> new ASTBinaryOp(null, null, null, 0, 0));
		Assert.assertThrows(NullPointerException.class, () -> new ASTBinaryOp(new ASTIdent("a", 0, 0), null, null, 0, 0));
		Assert.assertThrows(NullPointerException.class,
				() -> new ASTBinaryOp(new ASTIdent("a", 0, 0), new ASTIdent("a", 0, 0), null, 0, 0));
	}

	@Test
	public void constructorArrayAccessTest() {
		Assert.assertThrows(NullPointerException.class, () -> new ASTArrayAccess(null, null, 0, 0));
		Assert.assertThrows(NullPointerException.class, () -> new ASTArrayAccess(new ASTIdent("a", 0, 0), null, 0, 0));
	}

	@Test
	public void constructorUnaryOpTest() {
		Assert.assertThrows(NullPointerException.class, () -> new ASTUnaryOp(null, null, 0, 0));
		Assert.assertThrows(NullPointerException.class, () -> new ASTUnaryOp(new ASTIdent("a", 0, 0), null, 0, 0));
	}

	@Test
	public void constructorMethodCallExpr() {
		Assert.assertThrows(NullPointerException.class, () -> new ASTMethodCallExpr(null, null, 0, 0));
		Assert.assertThrows(NullPointerException.class, () -> new ASTMethodCallExpr(new ASTIdent("a", 0, 0), null, 0, 0));
	}

	@Test
	public void constructorVExpr() {
		Assert.assertThrows(NullPointerException.class, () -> new ASTVExpr(null, 0, 0));
	}

	@Test
	public void constructorLength() {
		Assert.assertThrows(NullPointerException.class, () -> new ASTLength(null, 0, 0));
	}

	@Test
	public void constructorExprsChain() {
		Assert.assertThrows(NullPointerException.class, () -> new ASTExprsChain(null, null, 0, 0));
		Assert.assertThrows(NullPointerException.class, () -> new ASTExprsChain(new ASTIdent("a", 0, 0), null, 0, 0));
	}

	@Test
	public void constructorVarArray() {
		Assert.assertThrows(IllegalArgumentException.class, () -> new ASTVarArray(new ASTBoolType(0, 0), null, null, 0, 0));
		Assert.assertThrows(NullPointerException.class, () -> new ASTVarArray(null, new ASTIdent("a", 0, 0), null, 0, 0));
		Assert.assertThrows(NullPointerException.class,
				() -> new ASTVarArray(new ASTBoolType(0, 0), new ASTIdent("a", 0, 0), null, 0, 0));
	}

	@Test
	public void constructorVarsChain() {
		Assert.assertThrows(NullPointerException.class, () -> new ASTVarsChain(null, null, 0, 0));
		Assert.assertThrows(NullPointerException.class,
				() -> new ASTVarsChain(new ASTVarSimple(new ASTBoolType(0, 0), new ASTIdent("a", 0, 0), null, 0, 0), null, 0,
						0));
	}

	@Test
	public void constructorInstrsChain() {
		Assert.assertThrows(NullPointerException.class, () -> new ASTInstrsChain(null, null, 0, 0));
		Assert.assertThrows(NullPointerException.class,
				() -> new ASTInstrsChain(new ASTReturn(new ASTIdent("a", 0, 0), 0, 0), null, 0, 0));
	}

	@Test
	public void constructorParamsChain() {
		Assert.assertThrows(NullPointerException.class, () -> new ASTParamsChain(null, null, 0, 0));
		Assert.assertThrows(NullPointerException.class,
				() -> new ASTParamsChain(new ASTParam(new ASTBoolType(0, 0), new ASTIdent("a", 0, 0), 0, 0), null, 0, 0));
	}

	@Test
	public void constructorDeclsChain() {
		Assert.assertThrows(NullPointerException.class, () -> new ASTDeclsChain(null, null, 0, 0));
		Assert.assertThrows(NullPointerException.class,
				() -> new ASTDeclsChain(new ASTVarSimple(new ASTBoolType(0, 0), new ASTIdent("a", 0, 0), null, 0, 0), null, 0,
						0));
	}

	@Test
	public void constructorReturn() {
		Assert.assertThrows(NullPointerException.class, () -> new ASTReturn(null, 0, 0));
	}

	@Test
	public void constructorIncrementOp() {
		Assert.assertThrows(NullPointerException.class, () -> new ASTIncrementOp(null, null, 0, 0));
		Assert.assertThrows(NullPointerException.class, () -> new ASTIncrementOp(new ASTIdent("a", 0, 0), null, 0, 0));
	}

	@Test
	public void constructorMethodCallInstr() {
		Assert.assertThrows(NullPointerException.class, () -> new ASTMethodCallInstr(null, null, 0, 0));
		Assert.assertThrows(NullPointerException.class, () -> new ASTMethodCallInstr(new ASTIdent("a", 0, 0), null, 0, 0));
	}

	@Test
	public void constructorWhile() {
		Assert.assertThrows(NullPointerException.class, () -> new ASTWhile(null, null, 0, 0));
		Assert.assertThrows(NullPointerException.class, () -> new ASTWhile(new ASTIdent("a", 0, 0), null, 0, 0));
	}

	@Test
	public void constructorWrite() {
		Assert.assertThrows(NullPointerException.class, () -> new ASTWrite((ASTIdent) null, false, 0, 0));
	}

	@Test
	public void constructorMainMethod() {
		Assert.assertThrows(NullPointerException.class, () -> new ASTMainMethod(null, null, 0, 0));
		Assert.assertThrows(NullPointerException.class, () -> new ASTMainMethod(new ASTVarsNil(0, 0), null, 0, 0));
	}

	@Test
	public void constructorParam() {
		Assert.assertThrows(NullPointerException.class, () -> new ASTParam(null, null, 0, 0));
		Assert.assertThrows(NullPointerException.class, () -> new ASTParam(new ASTBoolType(0, 0), null, 0, 0));
	}

	@Test
	public void constructorClass() {
		Assert.assertThrows(NullPointerException.class, () -> new ASTClass(null, null, null, 0, 0));
		Assert.assertThrows(NullPointerException.class, () -> new ASTClass(new ASTIdent("a", 0, 0), null, null, 0, 0));
		Assert.assertThrows(NullPointerException.class,
				() -> new ASTClass(new ASTIdent("a", 0, 0), new ASTDeclsNil(0, 0), null, 0, 0));
	}

	@Test
	public void constructorIdent() {
		Assert.assertThrows(IllegalArgumentException.class, () -> new ASTIdent("a", -1, 0));
		Assert.assertThrows(IllegalArgumentException.class, () -> new ASTIdent("a", 0, -1));
	}

	@Test
	public void constructorMethod() {
		Assert.assertThrows(NullPointerException.class, () -> new ASTMethod(null, null, null, null, null, 0, 0));
		Assert.assertThrows(NullPointerException.class,
				() -> new ASTMethod(new ASTBoolType(0, 0), null, null, null, null, 0, 0));
		Assert.assertThrows(NullPointerException.class,
				() -> new ASTMethod(new ASTBoolType(0, 0), new ASTIdent("a", 0, 0), null, null, null, 0, 0));
		Assert.assertThrows(NullPointerException.class,
				() -> new ASTMethod(new ASTBoolType(0, 0), new ASTIdent("a", 0, 0), new ASTParamsNil(0, 0), null, null, 0, 0));
		Assert.assertThrows(NullPointerException.class,
				() -> new ASTMethod(new ASTBoolType(0, 0), new ASTIdent("a", 0, 0), new ASTParamsNil(0, 0),
						new ASTVarsNil(0, 0), null, 0, 0));
	}
}
