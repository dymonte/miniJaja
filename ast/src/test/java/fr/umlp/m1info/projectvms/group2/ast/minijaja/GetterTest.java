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

public class GetterTest {

	@Test
	public void getterNode() {
		Assert.assertEquals(0, new ASTIdent("a", 0, 1).getLine());
		Assert.assertEquals(1, new ASTIdent("a", 0, 1).getColumn());
	}

	/**
	 * Expr
	 */

	@Test
	public void getterIdent() {
		Assert.assertEquals("a", new ASTIdent("a", 0, 0).getName());
	}

	@Test
	public void getterIntLiteral() {
		Assert.assertEquals(1, (new ASTIntLiteral(1, 0, 0)).getValue());
	}

	@Test
	public void getterBoolLiteral() {
		Assert.assertEquals(true, (new ASTBoolLiteral(true, 0, 0)).getValue());
		Assert.assertEquals(false, (new ASTBoolLiteral(false, 0, 0)).getValue());
	}

	@Test
	public void getterStringLiteral() {
		Assert.assertEquals("AH", (new ASTStringLiteral("AH", 0, 0)).getValue());
	}

	@Test
	public void getterArrayAccess() {
		ASTIdent ident = new ASTIdent("A", 0, 0);
		ASTIntLiteral intLiteral = new ASTIntLiteral(1, 0, 0);
		Assert.assertEquals(ident, (new ASTArrayAccess(ident, intLiteral, 0, 0)).getIdent());
		Assert.assertEquals(intLiteral, (new ASTArrayAccess(ident, intLiteral, 0, 0)).getExpr());
	}

	@Test
	public void getterTernaryOp() {
		ASTBoolLiteral condition = new ASTBoolLiteral(true, 0, 0);
		ASTIntLiteral firstCase = new ASTIntLiteral(4, 0, 0);
		ASTIntLiteral secondCase = new ASTIntLiteral(3, 0, 0);
		ASTTernaryOp ternaryOp = new ASTTernaryOp(condition, firstCase, secondCase, 0, 0);
		Assert.assertEquals(condition, ternaryOp.getCondition());
		Assert.assertEquals(firstCase, ternaryOp.getFirstCase());
		Assert.assertEquals(secondCase, ternaryOp.getSecondCase());
	}

	@Test
	public void getterBinaryOp() {
		ASTIntLiteral intLiteral = new ASTIntLiteral(1, 0, 0);
		BinaryOp op = BinaryOp.fromSymbol("+");
		Assert.assertEquals(intLiteral, (new ASTBinaryOp(intLiteral, intLiteral, op, 0, 0)).getLeft());
		Assert.assertEquals(intLiteral, (new ASTBinaryOp(intLiteral, intLiteral, op, 0, 0)).getRight());
		Assert.assertEquals(op, (new ASTBinaryOp(intLiteral, intLiteral, op, 0, 0)).getOp());
	}

	@Test
	public void getterUnaryOp() {
		ASTIntLiteral intLiteral = new ASTIntLiteral(1, 0, 0);
		UnaryOp op = UnaryOp.fromSymbol("-");
		Assert.assertEquals(intLiteral, (new ASTUnaryOp(intLiteral, op, 0, 0)).getExpr());
		Assert.assertEquals(op, (new ASTUnaryOp(intLiteral, op, 0, 0)).getOp());
	}

	@Test
	public void getterLength() {
		ASTIdent ident = new ASTIdent("tab", 0, 0);
		Assert.assertEquals(ident, (new ASTLength(ident, 0, 0)).getIdent());
	}

	@Test
	public void getterMethodCallExpr() {
		ASTIdent ident = new ASTIdent("meth", 0, 0);
		ASTExprsNil exprs = new ASTExprsNil(0, 0);
		Assert.assertEquals(ident, (new ASTMethodCallExpr(ident, exprs, 0, 0)).getIdent());
		Assert.assertEquals(exprs, (new ASTMethodCallExpr(ident, exprs, 0, 0)).getExprs());
	}

	/**
	 * Instr
	 */
	@Test
	public void getterMethodCallInstr() {
		ASTIdent ident = new ASTIdent("meth", 0, 0);
		ASTExprsNil exprs = new ASTExprsNil(0, 0);
		Assert.assertEquals(ident, (new ASTMethodCallInstr(ident, exprs, 0, 0)).getIdent());
		Assert.assertEquals(exprs, (new ASTMethodCallInstr(ident, exprs, 0, 0)).getExprs());
	}

	@Test
	public void getterAssignmentOp() {
		ASTIdent ident = new ASTIdent("a", 0, 0);
		ASTIntLiteral intLiteral = new ASTIntLiteral(1, 0, 0);
		AssignmentOp op = AssignmentOp.fromSymbol("=");
		Assert.assertEquals(ident, (new ASTAssignmentOp(ident, intLiteral, op, 0, 0)).getLValue());
		Assert.assertEquals(intLiteral, (new ASTAssignmentOp(ident, intLiteral, op, 0, 0)).getExpr());
		Assert.assertEquals(op, (new ASTAssignmentOp(ident, intLiteral, op, 0, 0)).getOp());
	}

	@Test
	public void getterIncrementOp() {
		ASTIdent ident = new ASTIdent("a", 0, 0);
		IncrementOp op = IncrementOp.fromSymbol("++");
		Assert.assertEquals(ident, (new ASTIncrementOp(ident, op, 0, 0)).getLValue());
		Assert.assertEquals(op, (new ASTIncrementOp(ident, op, 0, 0)).getOp());
	}

	@Test
	public void getterReturn() {
		ASTIdent ident = new ASTIdent("a", 0, 0);
		Assert.assertEquals(ident, (new ASTReturn(ident, 0, 0)).getExpr());
	}

	@Test
	public void getterWhile() {
		ASTBoolLiteral bool = new ASTBoolLiteral(true, 0, 0);
		ASTInstrsNil instrs = new ASTInstrsNil(0, 0);
		Assert.assertEquals(bool, (new ASTWhile(bool, instrs, 0, 0)).getExpr());
		Assert.assertEquals(instrs, (new ASTWhile(bool, instrs, 0, 0)).getInstrs());
	}

	@Test
	public void gettFor() {
		ASTVar var = new ASTVarSimple(new ASTIntType(0, 0), new ASTIdent("i", 0, 0),
				new ASTVExpr(new ASTIntLiteral(0, 0, 0), 0, 0), 0, 0);
		ASTExpr expr = new ASTBinaryOp(new ASTIntLiteral(1, 0, 0), new ASTIntLiteral(1, 0, 0), BinaryOp.fromSymbol(">"), 0,
				0);
		ASTInstr instr = new ASTIncrementOp(new ASTArrayAccess(new ASTIdent("a", 0, 0), new ASTIntLiteral(1, 0, 0), 0, 0),
				IncrementOp.fromSymbol("++"), 0, 0);
		ASTInstrs instrs = new ASTInstrsNil(0, 0);
		Assert.assertEquals(var, (new ASTFor(var, expr, instr, instrs, 0, 0)).getVar());
		Assert.assertEquals(null, (new ASTFor(null, expr, instr, instrs, 0, 0)).getVar());
		Assert.assertEquals(expr, (new ASTFor(var, expr, instr, instrs, 0, 0)).getExpr());
		Assert.assertEquals(null, (new ASTFor(var, null, instr, instrs, 0, 0)).getExpr());
		Assert.assertEquals(instr, (new ASTFor(var, expr, instr, instrs, 0, 0)).getInc());
		Assert.assertEquals(null, (new ASTFor(var, expr, null, instrs, 0, 0)).getInc());
		Assert.assertEquals(instrs, (new ASTFor(var, expr, instr, instrs, 0, 0)).getInstrs());
	}

	@Test
	public void getterWrite() {
		ASTStringLiteral stringLiteral = new ASTStringLiteral("a", 0, 0);
		ASTIdent ident = new ASTIdent("a", 0, 0);
		Assert.assertEquals(stringLiteral, (new ASTWrite(stringLiteral, false, 0, 0)).getValue());
		Assert.assertEquals(ident, (new ASTWrite(ident, true, 0, 0)).getValue());
		Assert.assertEquals(true, (new ASTWrite(ident, true, 0, 0)).getIsLn());
		Assert.assertEquals(false, (new ASTWrite(ident, false, 0, 0)).getIsLn());
	}

	@Test
	public void getterIf() {
		ASTBoolLiteral bool = new ASTBoolLiteral(true, 0, 0);
		ASTInstrsNil instrs = new ASTInstrsNil(0, 0);
		Assert.assertEquals(bool, (new ASTIf(bool, instrs, instrs, 0, 0)).getExpr());
		Assert.assertEquals(instrs, (new ASTIf(bool, instrs, instrs, 0, 0)).getIfInstrs());
		Assert.assertEquals(instrs, (new ASTIf(bool, instrs, instrs, 0, 0)).getElseInstrs());
	}

	/**
	 * Instrs
	 */
	@Test
	public void getterInstrsChain() {
		ASTReturn returnInstr = new ASTReturn(new ASTIdent("a", 0, 0), 0, 0);
		ASTInstrsNil instrs = new ASTInstrsNil(1, 0);
		Assert.assertEquals(returnInstr, new ASTInstrsChain(returnInstr, instrs, 0, 0).getInstr());
		Assert.assertEquals(instrs, new ASTInstrsChain(returnInstr, instrs, 0, 0).getInstrs());
	}

	/**
	 * Var
	 */
	@Test
	public void getterVarSimple() {
		ASTIntType type = new ASTIntType(0, 0);
		ASTIdent ident = new ASTIdent("a", 0, 0);
		ASTVExpr vexpr = new ASTVExpr(new ASTIntLiteral(1, 0, 0), 0, 0);
		Assert.assertEquals(type, (new ASTVarSimple(type, ident, vexpr, 0, 0)).getType());
		Assert.assertEquals(ident, (new ASTVarSimple(type, ident, vexpr, 0, 0)).getIdent());
		Assert.assertEquals(vexpr, (new ASTVarSimple(type, ident, vexpr, 0, 0)).getVExpr());
	}

	@Test
	public void getterVarCont() {
		ASTIntType type = new ASTIntType(0, 0);
		ASTIdent ident = new ASTIdent("a", 0, 0);
		ASTVExpr vexpr = new ASTVExpr(new ASTIntLiteral(1, 0, 0), 0, 0);
		Assert.assertEquals(type, (new ASTVarConst(type, ident, vexpr, 0, 0)).getType());
		Assert.assertEquals(ident, (new ASTVarConst(type, ident, vexpr, 0, 0)).getIdent());
		Assert.assertEquals(vexpr, (new ASTVarConst(type, ident, vexpr, 0, 0)).getVExpr());
	}

	@Test
	public void getterVarArray() {
		ASTIntType type = new ASTIntType(0, 0);
		ASTIdent ident = new ASTIdent("a", 0, 0);
		ASTIntLiteral intLiteral = new ASTIntLiteral(1, 0, 0);
		Assert.assertEquals(type, (new ASTVarArray(type, ident, intLiteral, 0, 0)).getType());
		Assert.assertEquals(ident, (new ASTVarArray(type, ident, intLiteral, 0, 0)).getIdent());
		Assert.assertEquals(intLiteral, (new ASTVarArray(type, ident, intLiteral, 0, 0)).getExpr());
	}

	/**
	 * Vars
	 */
	@Test
	public void getterVarsChain() {
		ASTVarSimple varSimple = new ASTVarSimple(new ASTIntType(0, 0), new ASTIdent("x", 0, 0), null, 0, 0);
		ASTVarsNil varsNil = new ASTVarsNil(0, 0);
		Assert.assertEquals(varSimple, (new ASTVarsChain(varSimple, varsNil, 0, 0)).getVar());
		Assert.assertEquals(varsNil, (new ASTVarsChain(varSimple, varsNil, 0, 0)).getVars());
	}

	/**
	 * Decl
	 */
	@Test
	public void getterMethod() {
		ASTIntType type = new ASTIntType(0, 0);
		ASTIdent ident = new ASTIdent("function", 0, 0);
		ASTParamsNil paramsNil = new ASTParamsNil(0, 0);
		ASTVarsNil varsNil = new ASTVarsNil(0, 0);
		ASTInstrsNil instrsNil = new ASTInstrsNil(0, 0);
		Assert.assertEquals(type, (new ASTMethod(type, ident, paramsNil, varsNil, instrsNil, 0, 0)).getType());
		Assert.assertEquals(ident, (new ASTMethod(type, ident, paramsNil, varsNil, instrsNil, 0, 0)).getIdent());
		Assert.assertEquals(paramsNil, (new ASTMethod(type, ident, paramsNil, varsNil, instrsNil, 0, 0)).getParams());
		Assert.assertEquals(varsNil, (new ASTMethod(type, ident, paramsNil, varsNil, instrsNil, 0, 0)).getVars());
		Assert.assertEquals(instrsNil, (new ASTMethod(type, ident, paramsNil, varsNil, instrsNil, 0, 0)).getInstrs());
	}

	@Test
	public void getterMainMethod() {
		ASTVarsNil varsNil = new ASTVarsNil(0, 0);
		ASTInstrsNil instrsNil = new ASTInstrsNil(0, 0);
		Assert.assertEquals(varsNil, (new ASTMainMethod(varsNil, instrsNil, 0, 0)).getVars());
		Assert.assertEquals(instrsNil, (new ASTMainMethod(varsNil, instrsNil, 0, 0)).getInstrs());
	}

	@Test
	public void getterClass() {
		ASTIdent ident = new ASTIdent("a", 0, 0);
		ASTDeclsNil declsNil = new ASTDeclsNil(0, 0);
		ASTMainMethod mainMethod = new ASTMainMethod(new ASTVarsNil(0, 0), new ASTInstrsNil(0, 0), 0, 0);
		Assert.assertEquals(ident, (new ASTClass(ident, declsNil, mainMethod, 0, 0)).getIdent());
		Assert.assertEquals(declsNil, (new ASTClass(ident, declsNil, mainMethod, 0, 0)).getDecls());
		Assert.assertEquals(mainMethod, (new ASTClass(ident, declsNil, mainMethod, 0, 0)).getMainMethod());
		Assert.assertEquals(ident, ASTClass.getClassVar());
	}

	/**
	 * Decls
	 */
	@Test
	public void getterDeclsChain() {
		ASTVarSimple varSimple = new ASTVarSimple(new ASTIntType(0, 0), new ASTIdent("a", 0, 0), null, 0, 0);
		ASTDeclsNil declsNil = new ASTDeclsNil(0, 0);
		Assert.assertEquals(varSimple, (new ASTDeclsChain(varSimple, declsNil, 0, 0)).getDecl());
		Assert.assertEquals(declsNil, (new ASTDeclsChain(varSimple, declsNil, 0, 0)).getDecls());
	}

	/**
	 * Params
	 */
	@Test
	public void getterParamsChain() {
		ASTParam param = new ASTParam(new ASTIntType(0, 0), new ASTIdent("a", 0, 0), 0, 0);
		ASTParamsNil paramsNil = new ASTParamsNil(0, 0);
		Assert.assertEquals(param, (new ASTParamsChain(param, paramsNil, 0, 0)).getParam());
		Assert.assertEquals(paramsNil, (new ASTParamsChain(param, paramsNil, 0, 0)).getParams());
	}

	/**
	 * Other
	 */
	@Test
	public void getterVExpr() {
		ASTIdent ident = new ASTIdent("a", 0, 0);
		Assert.assertEquals(ident, (new ASTVExpr(ident, 0, 0)).getExpr());
	}

	@Test
	public void getterParam() {
		ASTIntType intType = new ASTIntType(0, 0);
		ASTIdent ident = new ASTIdent("a", 0, 0);
		Assert.assertEquals(intType, (new ASTParam(intType, ident, 0, 0)).getType());
		Assert.assertEquals(ident, (new ASTParam(intType, ident, 0, 0)).getIdent());
	}

	@Test
	public void getterExprsChain() {
		ASTIdent ident = new ASTIdent("a", 0, 0);
		ASTExprsNil exprsNil = new ASTExprsNil(0, 0);
		Assert.assertEquals(ident, (new ASTExprsChain(ident, exprsNil, 0, 0)).getExpr());
		Assert.assertEquals(exprsNil, (new ASTExprsChain(ident, exprsNil, 0, 0)).getExprs());
	}

	@Test
	public void getterVar() {
		ASTIdent ident = new ASTIdent("a", 0, 0);
		class ASTTestVar extends ASTVar {
			public ASTTestVar(ASTIdent ident, int line, int column) {
				super(ident, line, column);
			}

			@Override
			public String toString() {
				return "";
			}
		}
		Assert.assertEquals(ident, (new ASTTestVar(ident, 0, 0)).getIdent());

	}
}
