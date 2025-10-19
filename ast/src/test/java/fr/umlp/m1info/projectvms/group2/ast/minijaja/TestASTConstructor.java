package fr.umlp.m1info.projectvms.group2.ast.minijaja;

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
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instrs.ASTInstrsNil;

import org.junit.Assert;

public class TestASTConstructor {
	@Test
	public void constructorVarSimpleTest() {
		ASTIdent ident = new ASTIdent("a", 0, 0);
		Assert.assertThrows(NullPointerException.class, () -> new ASTVarSimple(null, ident, null, 0, 0));
	}

	@Test
	public void constructorVarConstTest() {
		ASTIdent ident = new ASTIdent("a", 0, 0);
		Assert.assertThrows(NullPointerException.class, () -> new ASTVarConst(null, ident, null, 0, 0));
	}

	@Test
	public void constructorIfTest() {
		ASTIntLiteral literal = new ASTIntLiteral(1, 0, 0);
		ASTInstrsNil instrs = new ASTInstrsNil(0, 0);
		Assert.assertThrows(NullPointerException.class, () -> new ASTIf(null, null, null, 0, 0));
		Assert.assertThrows(NullPointerException.class, () -> new ASTIf(literal, null, null, 0, 0));
		Assert.assertThrows(NullPointerException.class, () -> new ASTIf(literal, instrs, null, 0, 0));
	}

	@Test
	public void constructorBinaryOpTest() {
		ASTIdent ident = new ASTIdent("a", 0, 0);
		Assert.assertThrows(NullPointerException.class, () -> new ASTBinaryOp(null, null, null, 0, 0));
		Assert.assertThrows(NullPointerException.class, () -> new ASTBinaryOp(ident, null, null, 0, 0));
		Assert.assertThrows(NullPointerException.class, () -> new ASTBinaryOp(ident, ident, null, 0, 0));
	}

	@Test
	public void constructorArrayAccessTest() {
		ASTIdent ident = new ASTIdent("a", 0, 0);
		Assert.assertThrows(NullPointerException.class, () -> new ASTArrayAccess(null, null, 0, 0));
		Assert.assertThrows(NullPointerException.class, () -> new ASTArrayAccess(ident, null, 0, 0));
	}

	@Test
	public void constructorUnaryOpTest() {
		ASTIdent ident = new ASTIdent("a", 0, 0);
		Assert.assertThrows(NullPointerException.class, () -> new ASTUnaryOp(null, null, 0, 0));
		Assert.assertThrows(NullPointerException.class, () -> new ASTUnaryOp(ident, null, 0, 0));
	}

	@Test
	public void constructorMethodCallExpr() {
		ASTIdent ident = new ASTIdent("a", 0, 0);
		Assert.assertThrows(NullPointerException.class, () -> new ASTMethodCallExpr(null, null, 0, 0));
		Assert.assertThrows(NullPointerException.class, () -> new ASTMethodCallExpr(ident, null, 0, 0));
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
		ASTIdent ident = new ASTIdent("a", 0, 0);
		Assert.assertThrows(NullPointerException.class, () -> new ASTExprsChain(null, null, 0, 0));
		Assert.assertThrows(NullPointerException.class, () -> new ASTExprsChain(ident, null, 0, 0));
	}

	@Test
	public void constructorVarArray() {
		ASTIdent ident = new ASTIdent("a", 0, 0);
		ASTBoolType boolType = new ASTBoolType(0, 0);
		Assert.assertThrows(IllegalArgumentException.class, () -> new ASTVarArray(boolType, null, null, 0, 0));
		Assert.assertThrows(NullPointerException.class, () -> new ASTVarArray(null, ident, null, 0, 0));
		Assert.assertThrows(NullPointerException.class, () -> new ASTVarArray(boolType, ident, null, 0, 0));
	}

	@Test
	public void constructorVarsChain() {
		ASTVarSimple varSimple = new ASTVarSimple(new ASTBoolType(0, 0), new ASTIdent("a", 0, 0), null, 0, 0);
		Assert.assertThrows(NullPointerException.class, () -> new ASTVarsChain(null, null, 0, 0));
		Assert.assertThrows(NullPointerException.class, () -> new ASTVarsChain(varSimple, null, 0, 0));
	}

	@Test
	public void constructorInstrsChain() {
		ASTReturn astReturn = new ASTReturn(new ASTIdent("a", 0, 0), 0, 0);
		Assert.assertThrows(NullPointerException.class, () -> new ASTInstrsChain(null, null, 0, 0));
		Assert.assertThrows(NullPointerException.class, () -> new ASTInstrsChain(astReturn, null, 0, 0));
	}

	@Test
	public void constructorParamsChain() {
		ASTParam param = new ASTParam(new ASTBoolType(0, 0), new ASTIdent("a", 0, 0), 0, 0);
		Assert.assertThrows(NullPointerException.class, () -> new ASTParamsChain(null, null, 0, 0));
		Assert.assertThrows(NullPointerException.class, () -> new ASTParamsChain(param, null, 0, 0));
	}

	@Test
	public void constructorDeclsChain() {
		ASTVarSimple varSimple = new ASTVarSimple(new ASTBoolType(0, 0), new ASTIdent("a", 0, 0), null, 0, 0);
		Assert.assertThrows(NullPointerException.class, () -> new ASTDeclsChain(null, null, 0, 0));
		Assert.assertThrows(NullPointerException.class, () -> new ASTDeclsChain(varSimple, null, 0, 0));
	}

	@Test
	public void constructorReturn() {
		Assert.assertThrows(NullPointerException.class, () -> new ASTReturn(null, 0, 0));
	}

	@Test
	public void constructorIncrementOp() {
		ASTIdent ident = new ASTIdent("a", 0, 0);
		Assert.assertThrows(NullPointerException.class, () -> new ASTIncrementOp(null, null, 0, 0));
		Assert.assertThrows(NullPointerException.class, () -> new ASTIncrementOp(ident, null, 0, 0));
	}

	@Test
	public void constructorMethodCallInstr() {
		ASTIdent ident = new ASTIdent("a", 0, 0);
		Assert.assertThrows(NullPointerException.class, () -> new ASTMethodCallInstr(null, null, 0, 0));
		Assert.assertThrows(NullPointerException.class, () -> new ASTMethodCallInstr(ident, null, 0, 0));
	}

	@Test
	public void constructorWhile() {
		ASTIdent ident = new ASTIdent("a", 0, 0);
		Assert.assertThrows(NullPointerException.class, () -> new ASTWhile(null, null, 0, 0));
		Assert.assertThrows(NullPointerException.class, () -> new ASTWhile(ident, null, 0, 0));
	}

	@Test
	public void constructorWrite() {

		Assert.assertThrows(NullPointerException.class, () -> new ASTWrite((ASTIdent) null, false, 0, 0));
	}

	@Test
	public void constructorMainMethod() {
		ASTVarsNil varsNil = new ASTVarsNil(0, 0);
		Assert.assertThrows(NullPointerException.class, () -> new ASTMainMethod(null, null, 0, 0));
		Assert.assertThrows(NullPointerException.class, () -> new ASTMainMethod(varsNil, null, 0, 0));
	}

	@Test
	public void constructorParam() {
		ASTBoolType bool = new ASTBoolType(0, 0);
		Assert.assertThrows(NullPointerException.class, () -> new ASTParam(null, null, 0, 0));
		Assert.assertThrows(NullPointerException.class, () -> new ASTParam(bool, null, 0, 0));
	}

	@Test
	public void constructorClass() {
		ASTIdent ident = new ASTIdent("a", 0, 0);
		ASTDeclsNil declsNil = new ASTDeclsNil(0, 0);
		Assert.assertThrows(NullPointerException.class, () -> new ASTClass(null, null, null, 0, 0));
		Assert.assertThrows(NullPointerException.class, () -> new ASTClass(ident, null, null, 0, 0));
		Assert.assertThrows(NullPointerException.class, () -> new ASTClass(ident, declsNil, null, 0, 0));
	}

	@Test
	public void constructorIdent() {
		Assert.assertThrows(IllegalArgumentException.class, () -> new ASTIdent("a", -1, 0));
		Assert.assertThrows(IllegalArgumentException.class, () -> new ASTIdent("a", 0, -1));
	}

	@Test
	public void constructorMethod() {
		ASTBoolType bool = new ASTBoolType(0, 0);
		ASTIdent ident = new ASTIdent("a", 0, 0);
		ASTParamsNil paramsNil = new ASTParamsNil(0, 0);
		ASTVarsNil varsNil = new ASTVarsNil(0, 0);
		Assert.assertThrows(NullPointerException.class, () -> new ASTMethod(null, null, null, null, null, 0, 0));
		Assert.assertThrows(NullPointerException.class, () -> new ASTMethod(bool, null, null, null, null, 0, 0));
		Assert.assertThrows(NullPointerException.class, () -> new ASTMethod(bool, ident, null, null, null, 0, 0));
		Assert.assertThrows(NullPointerException.class, () -> new ASTMethod(bool, ident, paramsNil, null, null, 0, 0));
		Assert.assertThrows(NullPointerException.class, () -> new ASTMethod(bool, ident, paramsNil, varsNil, null, 0, 0));
	}
}
