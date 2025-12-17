package fr.umlp.m1info.projectvms.group2.checker;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import fr.umlp.m1info.projectvms.group2.exception.TypeCheckException;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decl.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decls.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.exprs.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instrs.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.params.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.type.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.var.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.vars.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.cases.*;
import fr.umlp.m1info.projectvms.group2.checker.Diagnostic.ErrorCode;
import fr.umlp.m1info.projectvms.group2.utils.Type;

public class TypeCheckerTest {
	TypeChecker checker;
	Diagnostic diag;

	@Before
	public void setUp() {
		diag = new Diagnostic();
		checker = new TypeChecker(diag);
	}

	private ErrorCode getError(int index) {
		return diag.issues().get(index).errorCode();
	}

	@Test
	public void constructorTest() {
		Assert.assertThrows(NullPointerException.class, () -> new TypeChecker(null));
	}

	@Test
	public void checkTest() {
		Assert.assertThrows(NullPointerException.class, () -> checker.check(null));
		ASTIdent ident = new ASTIdent("a", 0, 0);
		Assert.assertThrows(TypeCheckException.class, () -> checker.check(ident));
	}

	@Test
	public void evalTypeTest() {
		Assert.assertThrows(NullPointerException.class, () -> checker.evalType(null));
		ASTWrite write = new ASTWrite(new ASTStringLiteral("ah", 0, 0), false, 0, 0);
		Assert.assertThrows(TypeCheckException.class, () -> checker.evalType(write));
	}

	@Test
	public void classTest() {
		TypeChecker spyChecker = Mockito.spy(checker);

		ASTDecls decls = new ASTDeclsNil(0, 0);
		ASTMainMethod mainMethod = new ASTMainMethod(new ASTVarsNil(0, 0), new ASTInstrsNil(0, 0), 0, 0);
		ASTClass astClass = new ASTClass(new ASTIdent("a", 0, 0), decls, mainMethod, 0, 0);
		Mockito.doNothing().when(spyChecker).check(decls);
		Mockito.doNothing().when(spyChecker).check(mainMethod);

		spyChecker.check(astClass);

		Assert.assertEquals(0, diag.issues().size());
	}

	@Test
	public void mainMethodTest() {
		TypeChecker spyChecker = Mockito.spy(checker);

		ASTVars vars = new ASTVarsNil(0, 0);
		ASTInstrs instrs = new ASTInstrsNil(0, 0);
		ASTMainMethod mainMethod = new ASTMainMethod(vars, instrs, 0, 0);
		Mockito.doNothing().when(spyChecker).check(vars);
		Mockito.doNothing().when(spyChecker).check(instrs);

		spyChecker.check(mainMethod);

		Assert.assertEquals(0, diag.issues().size());
	}

	@Test
	public void methodNoParamsTest() {
		TypeChecker spyChecker = Mockito.spy(checker);

		ASTIdent ident = new ASTIdent("method", 0, 0);
		ASTParams params = new ASTParamsNil(0, 0);
		ASTVoidType type = new ASTVoidType(0, 0);
		ASTInstrs instrs = new ASTInstrsNil(0, 0);
		ASTVars vars = new ASTVarsNil(0, 0);

		ASTMethod method = new ASTMethod(type, ident, params, vars, instrs, 0, 0);

		Mockito.doNothing().when(spyChecker).check(type);
		Mockito.doNothing().when(spyChecker).check(ident);
		Mockito.doNothing().when(spyChecker).check(params);
		Mockito.doNothing().when(spyChecker).check(vars);
		Mockito.doNothing().when(spyChecker).check(instrs);

		spyChecker.check(method);

		Assert.assertEquals(0, diag.issues().size());
	}

	@Test
	public void methodParamsTest() {
		TypeChecker spyChecker = Mockito.spy(checker);

		Assert.assertTrue(true);
		ASTIdent ident = new ASTIdent("method", 0, 0);
		ASTParams params = new ASTParamsNil(0, 0);
		ASTVoidType type = new ASTVoidType(0, 0);
		ASTInstrs instrs = new ASTInstrsNil(0, 0);
		ASTVars vars = new ASTVarsNil(0, 0);

		ASTMethod method = new ASTMethod(type, ident, params, vars, instrs, 0, 0);

		Mockito.doNothing().when(spyChecker).check(type);
		Mockito.doNothing().when(spyChecker).check(ident);
		Mockito.doNothing().when(spyChecker).check(params);
		Mockito.doNothing().when(spyChecker).check(vars);
		Mockito.doNothing().when(spyChecker).check(instrs);

		spyChecker.check(method);

		Assert.assertEquals(0, diag.issues().size());
	}

	@Test
	public void methodAlreadyExistsTest() {
		TypeChecker spyChecker = Mockito.spy(checker);

		Assert.assertTrue(true);
		ASTIdent ident = new ASTIdent("method", 0, 0);
		ASTParams params = new ASTParamsNil(0, 0);
		ASTVoidType type1 = new ASTVoidType(0, 0);
		ASTIntType type2 = new ASTIntType(0, 0);
		ASTInstrs instrs = new ASTInstrsNil(0, 0);
		ASTVars vars = new ASTVarsNil(0, 0);

		ASTMethod method1 = new ASTMethod(type1, ident, params, vars, instrs, 0, 0);
		ASTMethod method2 = new ASTMethod(type2, ident, params, vars, instrs, 0, 0);

		Mockito.doNothing().when(spyChecker).check(type1);
		Mockito.doNothing().when(spyChecker).check(type2);
		Mockito.doNothing().when(spyChecker).check(ident);
		Mockito.doNothing().when(spyChecker).check(params);
		Mockito.doNothing().when(spyChecker).check(vars);
		Mockito.doNothing().when(spyChecker).check(instrs);

		spyChecker.check(method1);
		spyChecker.check(method2);

		Assert.assertEquals(1, diag.issues().size());
		Assert.assertEquals(ErrorCode.METHOD_ALREADY_EXISTS, getError(0));
	}

	@Test
	public void methodNotSameParamsTest() {
		TypeChecker spyChecker = Mockito.spy(checker);

		ASTIdent ident = new ASTIdent("method", 0, 0);
		ASTParams params1 = new ASTParamsNil(0, 0);
		ASTParams params2 = new ASTParamsChain(new ASTParam(new ASTIntType(0, 0), new ASTIdent("a", 0, 0), 0, 0),
				new ASTParamsNil(0, 0), 0, 0);
		ASTVoidType type = new ASTVoidType(0, 0);
		ASTInstrs instrs = new ASTInstrsNil(0, 0);
		ASTVars vars = new ASTVarsNil(0, 0);

		ASTMethod method1 = new ASTMethod(type, ident, params1, vars, instrs, 0, 0);
		ASTMethod method2 = new ASTMethod(type, ident, params2, vars, instrs, 0, 0);

		Mockito.doNothing().when(spyChecker).check(type);
		Mockito.doNothing().when(spyChecker).check(ident);
		Mockito.doNothing().when(spyChecker).check(params1);
		Mockito.doNothing().when(spyChecker).check(params2);
		Mockito.doNothing().when(spyChecker).check(vars);
		Mockito.doNothing().when(spyChecker).check(instrs);

		spyChecker.check(method1);
		spyChecker.check(method2);

		Assert.assertEquals(0, diag.issues().size());
	}

	@Test
	public void declsWithoutMethodTest() {
		TypeChecker spyChecker = Mockito.spy(checker);

		ASTIdent ident = new ASTIdent("var", 0, 0);
		ASTDecl decl1 = new ASTVarSimple(new ASTIntType(0, 0), ident, null, 0, 0);
		ASTDecl decl2 = new ASTVarSimple(new ASTIntType(0, 0), ident, null, 0, 0);
		ASTDecl decl3 = new ASTVarSimple(new ASTIntType(0, 0), ident, null, 0, 0);

		ASTDecls decls1 = new ASTDeclsChain(decl1, new ASTDeclsNil(0, 0), 0, 0);
		ASTDecls decls2 = new ASTDeclsChain(decl2, decls1, 0, 0);
		ASTDecls decls3 = new ASTDeclsChain(decl3, decls2, 0, 0);

		Mockito.doNothing().when(spyChecker).check(decl1);
		Mockito.doNothing().when(spyChecker).check(decl2);
		Mockito.doNothing().when(spyChecker).check(decl3);

		spyChecker.check(decls3);

		Assert.assertEquals(0, diag.issues().size());
	}

	@Test
	public void declsMethodTest() {
		TypeChecker spyChecker = Mockito.spy(checker);

		ASTIdent ident = new ASTIdent("var", 0, 0);
		ASTDecl decl1 = new ASTVarSimple(new ASTIntType(0, 0), ident, null, 0, 0);
		ASTReturn ret = new ASTReturn(ident, 0, 0);
		ASTInstrsNil instrNil = new ASTInstrsNil(0, 0);
		ASTInstrs chain = new ASTInstrsChain(ret, instrNil, 0, 0);
		ASTDecl decl2 = new ASTMethod(new ASTIntType(0, 0), ident, new ASTParamsNil(0, 0), new ASTVarsNil(0, 0),
				chain, 0, 0);
		ASTDecl decl3 = new ASTVarSimple(new ASTIntType(0, 0), ident, null, 0, 0);

		ASTDecls decls1 = new ASTDeclsChain(decl1, new ASTDeclsNil(0, 0), 0, 0);
		ASTDecls decls2 = new ASTDeclsChain(decl2, decls1, 0, 0);
		ASTDecls decls3 = new ASTDeclsChain(decl3, decls2, 0, 0);

		Mockito.doNothing().when(spyChecker).check(decl1);
		Mockito.doNothing().when(spyChecker).check(decl2);
		Mockito.doNothing().when(spyChecker).check(decl3);
		Mockito.doReturn(Type.INT).when(spyChecker).evalType(ret);

		spyChecker.check(decls3);

		Assert.assertEquals(0, diag.issues().size());
	}

	@Test
	public void declsMethodVoid() {
		TypeChecker spyChecker = Mockito.spy(checker);

		ASTIdent ident = new ASTIdent("var", 0, 0);
		ASTDecl decl1 = new ASTVarSimple(new ASTIntType(0, 0), ident, null, 0, 0);
		ASTDecl decl2 = new ASTMethod(new ASTVoidType(0, 0), ident, new ASTParamsNil(0, 0), new ASTVarsNil(0, 0),
				new ASTInstrsNil(0, 0), 0, 0);
		ASTDecl decl3 = new ASTVarSimple(new ASTIntType(0, 0), ident, null, 0, 0);

		ASTDecls decls1 = new ASTDeclsChain(decl1, new ASTDeclsNil(0, 0), 0, 0);
		ASTDecls decls2 = new ASTDeclsChain(decl2, decls1, 0, 0);
		ASTDecls decls3 = new ASTDeclsChain(decl3, decls2, 0, 0);

		Mockito.doNothing().when(spyChecker).check(decl1);
		Mockito.doNothing().when(spyChecker).check(decl2);
		Mockito.doNothing().when(spyChecker).check(decl3);

		spyChecker.check(decls3);

		Assert.assertEquals(0, diag.issues().size());

	}

	@Test
	public void declsMethodVoidRetInt() {
		TypeChecker spyChecker = Mockito.spy(checker);

		ASTIdent ident = new ASTIdent("var", 0, 0);
		ASTDecl decl1 = new ASTVarSimple(new ASTIntType(0, 0), ident, null, 0, 0);
		ASTInstrsNil instrNil = new ASTInstrsNil(0, 0);
		ASTReturn ret = new ASTReturn(ident, 0, 0);
		ASTWrite write = new ASTWrite(new ASTStringLiteral("a", 0, 0), false, 0, 0);
		ASTInstrs chain1 = new ASTInstrsChain(ret, instrNil, 0, 0);
		ASTInstrs chain = new ASTInstrsChain(write, chain1, 0, 0);
		ASTDecl decl2 = new ASTMethod(new ASTVoidType(0, 0), ident, new ASTParamsNil(0, 0), new ASTVarsNil(0, 0),
				chain, 0, 0);
		ASTDecl decl3 = new ASTVarSimple(new ASTIntType(0, 0), ident, null, 0, 0);

		ASTDecls decls1 = new ASTDeclsChain(decl1, new ASTDeclsNil(0, 0), 0, 0);
		ASTDecls decls2 = new ASTDeclsChain(decl2, decls1, 0, 0);
		ASTDecls decls3 = new ASTDeclsChain(decl3, decls2, 0, 0);

		Mockito.doNothing().when(spyChecker).check(decl1);
		Mockito.doNothing().when(spyChecker).check(decl2);
		Mockito.doNothing().when(spyChecker).check(decl3);
		Mockito.doReturn(Type.INT).when(spyChecker).evalType(ret);

		spyChecker.check(decls3);

		Assert.assertEquals(1, diag.issues().size());
		Assert.assertEquals(ErrorCode.RETURN_TYPE_MISMATCH, getError(0));
	}

	@Test
	public void declsMethodIntRetNot() {
		TypeChecker spyChecker = Mockito.spy(checker);

		ASTIdent ident = new ASTIdent("var", 0, 0);
		ASTDecl decl1 = new ASTVarSimple(new ASTIntType(0, 0), ident, null, 0, 0);
		ASTDecl decl2 = new ASTMethod(new ASTIntType(0, 0), ident, new ASTParamsNil(0, 0), new ASTVarsNil(0, 0),
				new ASTInstrsNil(0, 0), 0, 0);
		ASTDecl decl3 = new ASTVarSimple(new ASTIntType(0, 0), ident, null, 0, 0);

		ASTDecls decls1 = new ASTDeclsChain(decl1, new ASTDeclsNil(0, 0), 0, 0);
		ASTDecls decls2 = new ASTDeclsChain(decl2, decls1, 0, 0);
		ASTDecls decls3 = new ASTDeclsChain(decl3, decls2, 0, 0);

		Mockito.doNothing().when(spyChecker).check(decl1);
		Mockito.doNothing().when(spyChecker).check(decl2);
		Mockito.doNothing().when(spyChecker).check(decl3);

		spyChecker.check(decls3);

		Assert.assertEquals(1, diag.issues().size());
		Assert.assertEquals(ErrorCode.MISSING_RETURN_STATEMENT, getError(0));
	}

	@Test
	public void assignmentEQSameTest() {
		TypeChecker spyChecker = Mockito.spy(checker);

		ASTIdent ident = new ASTIdent("a", 0, 0);

		ASTAssignmentOp assOp = new ASTAssignmentOp(ident, ident, AssignmentOp.EQ, 0, 0);

		Mockito.doReturn(Type.INT).when(spyChecker).evalType(ident);

		spyChecker.check(assOp);

		Assert.assertEquals(0, diag.issues().size());
	}

	@Test
	public void assignmentEQDiffTest() {
		TypeChecker spyChecker = Mockito.spy(checker);

		ASTIdent ident = new ASTIdent("a", 0, 0);
		ASTIdent ident1 = new ASTIdent("a", 0, 0);

		ASTAssignmentOp assOp = new ASTAssignmentOp(ident, ident1, AssignmentOp.EQ, 0, 0);

		Mockito.doReturn(Type.INT).when(spyChecker).evalType(ident);
		Mockito.doReturn(Type.BOOL).when(spyChecker).evalType(ident1);

		spyChecker.check(assOp);

		Assert.assertEquals(1, diag.issues().size());
		Assert.assertEquals(ErrorCode.TYPE_MISMATCH, getError(0));
	}

	@Test
	public void assignmentADDNotIntTest() {
		TypeChecker spyChecker = Mockito.spy(checker);

		ASTIdent ident = new ASTIdent("a", 0, 0);
		ASTIdent ident1 = new ASTIdent("a", 0, 0);

		ASTAssignmentOp assOp = new ASTAssignmentOp(ident, ident1, AssignmentOp.ADD, 0, 0);

		Mockito.doReturn(Type.INT).when(spyChecker).evalType(ident);
		Mockito.doReturn(Type.BOOL).when(spyChecker).evalType(ident1);

		spyChecker.check(assOp);

		Assert.assertEquals(2, diag.issues().size());
		Assert.assertEquals(ErrorCode.TYPE_MISMATCH, getError(0));
		Assert.assertEquals(ErrorCode.TYPE_MISMATCH, getError(1));
	}

	@Test
	public void assignmentADDBeIntTest() {
		TypeChecker spyChecker = Mockito.spy(checker);

		ASTIdent ident = new ASTIdent("a", 0, 0);
		ASTIdent ident1 = new ASTIdent("a", 0, 0);

		ASTAssignmentOp assOp = new ASTAssignmentOp(ident, ident1, AssignmentOp.ADD, 0, 0);

		Mockito.doReturn(Type.INT).when(spyChecker).evalType(ident);
		Mockito.doReturn(Type.INT).when(spyChecker).evalType(ident1);

		spyChecker.check(assOp);

		Assert.assertEquals(0, diag.issues().size());
	}

	@Test
	public void ifNotBoolTest() {
		TypeChecker spyChecker = Mockito.spy(checker);

		ASTIdent ident = new ASTIdent("a", 0, 0);

		ASTInstrs instrs = new ASTInstrsNil(0, 0);
		ASTIf astIf = new ASTIf(ident, instrs, instrs, 0, 0);

		Mockito.doReturn(Type.INT).when(spyChecker).evalType(ident);
		Mockito.doNothing().when(spyChecker).check(instrs);

		spyChecker.check(astIf);

		Assert.assertEquals(1, diag.issues().size());
		Assert.assertEquals(ErrorCode.TYPE_MISMATCH, getError(0));
	}

	@Test
	public void ifBoolTest() {
		TypeChecker spyChecker = Mockito.spy(checker);

		ASTIdent ident = new ASTIdent("a", 0, 0);

		ASTInstrs instrs = new ASTInstrsNil(0, 0);
		ASTIf astIf = new ASTIf(ident, instrs, instrs, 0, 0);

		Mockito.doReturn(Type.BOOL).when(spyChecker).evalType(ident);
		Mockito.doNothing().when(spyChecker).check(instrs);

		spyChecker.check(astIf);

		Assert.assertEquals(0, diag.issues().size());
	}

	@Test
	public void incrementTest() {
		TypeChecker spyChecker = Mockito.spy(checker);

		ASTIdent ident = new ASTIdent("a", 0, 0);

		ASTIncrementOp inc = new ASTIncrementOp(ident, IncrementOp.INC, 0, 0);

		Mockito.doReturn(Type.INT).when(spyChecker).evalType(ident);

		spyChecker.check(inc);

		Assert.assertEquals(0, diag.issues().size());
	}

	@Test
	public void incrementDiffTest() {
		TypeChecker spyChecker = Mockito.spy(checker);

		ASTIdent ident = new ASTIdent("a", 0, 0);

		ASTIncrementOp inc = new ASTIncrementOp(ident, IncrementOp.INC, 0, 0);

		Mockito.doReturn(Type.BOOL).when(spyChecker).evalType(ident);

		spyChecker.check(inc);

		Assert.assertEquals(1, diag.issues().size());
		Assert.assertEquals(ErrorCode.TYPE_MISMATCH, getError(0));
	}

	@Test
	public void whileBoolTest() {
		TypeChecker spyChecker = Mockito.spy(checker);

		ASTIdent ident = new ASTIdent("a", 0, 0);

		ASTInstrs instrs = new ASTInstrsNil(0, 0);
		ASTWhile astWhile = new ASTWhile(ident, instrs, 0, 0);

		Mockito.doReturn(Type.BOOL).when(spyChecker).evalType(ident);
		Mockito.doNothing().when(spyChecker).check(instrs);

		spyChecker.check(astWhile);

		Assert.assertEquals(0, diag.issues().size());
	}

	@Test
	public void whileNotBoolTest() {
		TypeChecker spyChecker = Mockito.spy(checker);

		ASTIdent ident = new ASTIdent("a", 0, 0);

		ASTInstrs instrs = new ASTInstrsNil(0, 0);
		ASTWhile astWhile = new ASTWhile(ident, instrs, 0, 0);

		Mockito.doReturn(Type.INT).when(spyChecker).evalType(ident);
		Mockito.doNothing().when(spyChecker).check(instrs);

		spyChecker.check(astWhile);

		Assert.assertEquals(1, diag.issues().size());
		Assert.assertEquals(ErrorCode.TYPE_MISMATCH, getError(0));
	}

	@Test
	public void forBoolTest() {
		TypeChecker spyChecker = Mockito.spy(checker);

		ASTIdent ident = new ASTIdent("a", 0, 0);

		ASTInstrs instrs = new ASTInstrsNil(0, 0);
		ASTFor astFor = new ASTFor(null, ident, null, instrs, 0, 0);

		Mockito.doReturn(Type.BOOL).when(spyChecker).evalType(ident);
		Mockito.doNothing().when(spyChecker).check(instrs);

		spyChecker.check(astFor);

		Assert.assertEquals(0, diag.issues().size());
	}

	@Test
	public void forNotBoolTest() {
		TypeChecker spyChecker = Mockito.spy(checker);

		ASTIdent ident = new ASTIdent("a", 0, 0);

		ASTInstrs instrs = new ASTInstrsNil(0, 0);
		ASTFor astFor = new ASTFor(null, ident, null, instrs, 0, 0);

		Mockito.doReturn(Type.INT).when(spyChecker).evalType(ident);
		Mockito.doNothing().when(spyChecker).check(instrs);

		spyChecker.check(astFor);

		Assert.assertEquals(1, diag.issues().size());
		Assert.assertEquals(ErrorCode.TYPE_MISMATCH, getError(0));
	}

	@Test
	public void writeTest() {
		ASTWrite write = new ASTWrite(new ASTStringLiteral("ah", 0, 0), false, 0, 0);
		checker.check(write);

		Assert.assertEquals(0, diag.issues().size());
	}

	@Test
	public void instrsChainTest() {
		TypeChecker spyChecker = Mockito.spy(checker);

		ASTWrite write = new ASTWrite(new ASTStringLiteral("a", 0, 0), false, 0, 0);
		ASTInstrs instrsNil = new ASTInstrsNil(0, 0);
		ASTInstrs instrs = new ASTInstrsChain(write, instrsNil, 0, 0);

		Mockito.doNothing().when(spyChecker).check(write);
		Mockito.doNothing().when(spyChecker).check(instrsNil);

		spyChecker.check(instrs);

		Assert.assertEquals(0, diag.issues().size());
	}

	@Test
	public void varSimpleTest() {
		TypeChecker spyChecker = Mockito.spy(checker);
		ASTIdent ident = new ASTIdent("a", 0, 0);
		ASTVarSimple varSimple = new ASTVarSimple(new ASTIntType(0, 0), ident, null, 0, 0);

		spyChecker.check(varSimple);

		Assert.assertEquals(0, diag.issues().size());
	}

	@Test
	public void varSimpleAssignTest() {
		TypeChecker spyChecker = Mockito.spy(checker);
		ASTIdent ident = new ASTIdent("a", 0, 0);
		ASTIdent ident1 = new ASTIdent("b", 0, 0);
		ASTVarSimple varSimple = new ASTVarSimple(new ASTIntType(0, 0), ident, new ASTVExpr(ident1, 0, 0), 0, 0);

		Mockito.doReturn(Type.INT).when(spyChecker).evalType(ident1);
		spyChecker.check(varSimple);

		Assert.assertEquals(0, diag.issues().size());
	}

	@Test
	public void varSimpleWrongTypeTest() {
		TypeChecker spyChecker = Mockito.spy(checker);
		ASTIdent ident = new ASTIdent("a", 0, 0);
		ASTIdent ident1 = new ASTIdent("b", 0, 0);
		ASTVarSimple varSimple = new ASTVarSimple(new ASTIntType(0, 0), ident, new ASTVExpr(ident1, 0, 0), 0, 0);

		Mockito.doReturn(Type.BOOL).when(spyChecker).evalType(ident1);
		spyChecker.check(varSimple);

		Assert.assertEquals(1, diag.issues().size());
		Assert.assertEquals(ErrorCode.TYPE_MISMATCH, getError(0));
	}

	@Test
	public void varArrayTest() {
		TypeChecker spyChecker = Mockito.spy(checker);
		ASTIdent ident = new ASTIdent("a", 0, 0);
		ASTIdent ident1 = new ASTIdent("a", 0, 0);
		ASTVarArray varArray = new ASTVarArray(new ASTIntType(0, 0), ident, ident1, 0, 0);

		Mockito.doReturn(Type.INT).when(spyChecker).evalType(ident1);

		spyChecker.check(varArray);

		Assert.assertEquals(0, diag.issues().size());
	}

	@Test
	public void varArrayWrongTypeSizeTest() {
		TypeChecker spyChecker = Mockito.spy(checker);
		ASTIdent ident = new ASTIdent("a", 0, 0);
		ASTIdent ident1 = new ASTIdent("b", 0, 0);
		ASTVarArray varArray = new ASTVarArray(new ASTIntType(0, 0), ident, ident1, 0, 0);

		Mockito.doReturn(Type.BOOL).when(spyChecker).evalType(ident1);
		spyChecker.check(varArray);

		Assert.assertEquals(1, diag.issues().size());
		Assert.assertEquals(ErrorCode.TYPE_MISMATCH, getError(0));
	}

	@Test
	public void varConstTest() {
		TypeChecker spyChecker = Mockito.spy(checker);
		ASTIdent ident = new ASTIdent("a", 0, 0);
		ASTVarConst varConst = new ASTVarConst(new ASTIntType(0, 0), ident, null, 0, 0);

		spyChecker.check(varConst);

		Assert.assertEquals(0, diag.issues().size());
	}

	@Test
	public void varConstAssignTest() {
		TypeChecker spyChecker = Mockito.spy(checker);
		ASTIdent ident = new ASTIdent("a", 0, 0);
		ASTIdent ident1 = new ASTIdent("b", 0, 0);
		ASTVarConst varConst = new ASTVarConst(new ASTIntType(0, 0), ident, new ASTVExpr(ident1, 0, 0), 0, 0);

		Mockito.doReturn(Type.INT).when(spyChecker).evalType(ident1);
		spyChecker.check(varConst);

		Assert.assertEquals(0, diag.issues().size());
	}

	@Test
	public void varConstWrongTypeTest() {
		TypeChecker spyChecker = Mockito.spy(checker);
		ASTIdent ident = new ASTIdent("a", 0, 0);
		ASTIdent ident1 = new ASTIdent("b", 0, 0);
		ASTVarConst varConst = new ASTVarConst(new ASTIntType(0, 0), ident, new ASTVExpr(ident1, 0, 0), 0, 0);

		Mockito.doReturn(Type.BOOL).when(spyChecker).evalType(ident1);
		spyChecker.check(varConst);

		Assert.assertEquals(1, diag.issues().size());
		Assert.assertEquals(ErrorCode.TYPE_MISMATCH, getError(0));
	}

	@Test
	public void varsChainTest() {
		TypeChecker spyChecker = Mockito.spy(checker);
		ASTIdent ident = new ASTIdent("a", 0, 0);
		ASTIdent ident1 = new ASTIdent("b", 0, 0);
		ASTVarConst varConst = new ASTVarConst(new ASTIntType(0, 0), ident, new ASTVExpr(ident1, 0, 0), 0, 0);
		ASTVarsNil varsNil = new ASTVarsNil(0, 0);

		ASTVars chain = new ASTVarsChain(varConst, varsNil, 0, 0);

		Mockito.doNothing().when(spyChecker).check(varConst);
		Mockito.doNothing().when(spyChecker).check(varsNil);

		spyChecker.check(chain);

		Assert.assertEquals(0, diag.issues().size());
	}

	@Test
	public void varsNilTest() {
		TypeChecker spyChecker = Mockito.spy(checker);
		ASTVarsNil varsNil = new ASTVarsNil(0, 0);

		spyChecker.check(varsNil);

		Assert.assertEquals(0, diag.issues().size());
	}

	@Test
	public void binaryTest() {
		TypeChecker spyChecker = Mockito.spy(checker);
		ASTIdent identL = new ASTIdent("a", 0, 0);
		ASTIdent identR = new ASTIdent("b", 0, 0);
		ASTBinaryOp binary = new ASTBinaryOp(identL, identR, BinaryOp.ADD, 0, 0);

		Mockito.doReturn(Type.INT).when(spyChecker).evalType(identL);
		Mockito.doReturn(Type.INT).when(spyChecker).evalType(identR);

		Type type = spyChecker.evalType(binary);

		Assert.assertEquals(Type.INT, type);
		Assert.assertEquals(0, diag.issues().size());
	}

	@Test
	public void binaryLeftNotGoodTest() {
		TypeChecker spyChecker = Mockito.spy(checker);
		ASTIdent identL = new ASTIdent("a", 0, 0);
		ASTIdent identR = new ASTIdent("b", 0, 0);
		ASTBinaryOp binary = new ASTBinaryOp(identL, identR, BinaryOp.ADD, 0, 0);

		Mockito.doReturn(Type.BOOL).when(spyChecker).evalType(identL);
		Mockito.doReturn(Type.INT).when(spyChecker).evalType(identR);

		Type type = spyChecker.evalType(binary);

		Assert.assertEquals(Type.INT, type);
		Assert.assertEquals(1, diag.issues().size());
		Assert.assertEquals(ErrorCode.TYPE_MISMATCH, getError(0));
	}

	@Test
	public void binaryRightNotGoodTest() {
		TypeChecker spyChecker = Mockito.spy(checker);
		ASTIdent identL = new ASTIdent("a", 0, 0);
		ASTIdent identR = new ASTIdent("b", 0, 0);
		ASTBinaryOp binary = new ASTBinaryOp(identL, identR, BinaryOp.ADD, 0, 0);

		Mockito.doReturn(Type.INT).when(spyChecker).evalType(identL);
		Mockito.doReturn(Type.BOOL).when(spyChecker).evalType(identR);

		Type type = spyChecker.evalType(binary);

		Assert.assertEquals(Type.INT, type);
		Assert.assertEquals(1, diag.issues().size());
		Assert.assertEquals(ErrorCode.TYPE_MISMATCH, getError(0));
	}

	@Test
	public void binaryBoolTest() {
		TypeChecker spyChecker = Mockito.spy(checker);
		ASTIdent identL = new ASTIdent("a", 0, 0);
		ASTIdent identR = new ASTIdent("b", 0, 0);
		ASTBinaryOp binary = new ASTBinaryOp(identL, identR, BinaryOp.OR, 0, 0);

		Mockito.doReturn(Type.BOOL).when(spyChecker).evalType(identL);
		Mockito.doReturn(Type.BOOL).when(spyChecker).evalType(identR);

		Type type = spyChecker.evalType(binary);

		Assert.assertEquals(Type.BOOL, type);
		Assert.assertEquals(0, diag.issues().size());
	}

	@Test
	public void unaryTest() {
		TypeChecker spyChecker = Mockito.spy(checker);
		ASTIdent ident = new ASTIdent("a", 0, 0);
		ASTUnaryOp unary = new ASTUnaryOp(ident, UnaryOp.NEG, 0, 0);

		Mockito.doReturn(Type.INT).when(spyChecker).evalType(ident);

		Type type = spyChecker.evalType(unary);

		Assert.assertEquals(Type.INT, type);
		Assert.assertEquals(0, diag.issues().size());
	}

	@Test
	public void unaryTestValueNotGood() {
		TypeChecker spyChecker = Mockito.spy(checker);
		ASTIdent ident = new ASTIdent("a", 0, 0);
		ASTUnaryOp unary = new ASTUnaryOp(ident, UnaryOp.NEG, 0, 0);

		Mockito.doReturn(Type.BOOL).when(spyChecker).evalType(ident);

		Type type = spyChecker.evalType(unary);

		Assert.assertEquals(Type.INT, type);
		Assert.assertEquals(1, diag.issues().size());
		Assert.assertEquals(ErrorCode.TYPE_MISMATCH, getError(0));
	}

	@Test
	public void unaryBoolTest() {
		TypeChecker spyChecker = Mockito.spy(checker);
		ASTIdent ident = new ASTIdent("a", 0, 0);
		ASTUnaryOp unary = new ASTUnaryOp(ident, UnaryOp.NOT, 0, 0);

		Mockito.doReturn(Type.BOOL).when(spyChecker).evalType(ident);

		Type type = spyChecker.evalType(unary);

		Assert.assertEquals(Type.BOOL, type);
		Assert.assertEquals(0, diag.issues().size());
	}

	@Test
	public void arrayAccessTest() {
		TypeChecker spyChecker = Mockito.spy(checker);
		ASTIdent ident = new ASTIdent("a", 0, 0);
		ASTIdent ident1 = new ASTIdent("b", 0, 0);

		ASTArrayAccess arrayAcc = new ASTArrayAccess(ident, ident1, 0, 0);

		Mockito.doReturn(Type.INT).when(spyChecker).evalType(ident);
		Mockito.doReturn(Type.INT).when(spyChecker).evalType(ident1);

		Type type = spyChecker.evalType(arrayAcc);

		Assert.assertEquals(Type.INT, type);
		Assert.assertEquals(0, diag.issues().size());
	}

	@Test
	public void arrayAccessNotGoodType() {
		TypeChecker spyChecker = Mockito.spy(checker);
		ASTIdent ident = new ASTIdent("a", 0, 0);
		ASTIdent ident1 = new ASTIdent("b", 0, 0);

		ASTArrayAccess arrayAcc = new ASTArrayAccess(ident, ident1, 0, 0);

		Mockito.doReturn(Type.INT).when(spyChecker).evalType(ident);
		Mockito.doReturn(Type.BOOL).when(spyChecker).evalType(ident1);

		Type type = spyChecker.evalType(arrayAcc);

		Assert.assertEquals(Type.INT, type);
		Assert.assertEquals(1, diag.issues().size());
		Assert.assertEquals(ErrorCode.TYPE_MISMATCH, getError(0));
	}

	@Test
	public void methodInstrNotFoundTest() {
		TypeChecker spyChecker = Mockito.spy(checker);

		ASTIdent ident = new ASTIdent("meth", 0, 0);

		ASTVarSimple varSimple = new ASTVarSimple(new ASTIntType(0, 0), ident, null, 0, 0);

		ASTIdent ident1 = new ASTIdent("a", 0, 0);
		ASTExprsNil exprsNil = new ASTExprsNil(0, 0);
		ASTExprs exprs = new ASTExprsChain(ident1, exprsNil, 0, 0);
		ASTMethodCallInstr callInstr = new ASTMethodCallInstr(ident, exprs, 0, 0);

		Mockito.doReturn(Type.INT).when(spyChecker).evalType(ident1);

		spyChecker.check(varSimple);
		Type type = spyChecker.evalType(callInstr);

		Assert.assertEquals(null, type);
		Assert.assertEquals(1, diag.issues().size());
		Assert.assertEquals(ErrorCode.UNKNOWN_FUNCTION, getError(0));
	}

	@Test
	public void methodInstrArgumentMismatch() {
		TypeChecker spyChecker = Mockito.spy(checker);

		ASTTypeMeth typeMeth = new ASTVoidType(0, 0);
		ASTIdent ident = new ASTIdent("meth", 0, 0);

		ASTParams params = new ASTParamsNil(0, 0);
		ASTVars vars = new ASTVarsNil(0, 0);
		ASTInstrs instrs = new ASTInstrsNil(0, 0);
		ASTMethod method = new ASTMethod(typeMeth, ident, params, vars, instrs, 0, 0);

		Mockito.doNothing().when(spyChecker).check(vars);
		Mockito.doNothing().when(spyChecker).check(instrs);

		spyChecker.check(method);

		ASTIdent ident1 = new ASTIdent("a", 0, 0);
		ASTExprsNil exprsNil = new ASTExprsNil(0, 0);
		ASTExprs exprs = new ASTExprsChain(ident1, exprsNil, 0, 0);
		ASTMethodCallInstr callInstr = new ASTMethodCallInstr(ident, exprs, 0, 0);

		Mockito.doReturn(Type.INT).when(spyChecker).evalType(ident1);

		Type type = spyChecker.evalType(callInstr);

		Assert.assertEquals(null, type);
		Assert.assertEquals(1, diag.issues().size());
		Assert.assertEquals(ErrorCode.ARGUMENT_MISMATCH, getError(0));
	}

	@Test
	public void methodInstrTest() {
		TypeChecker spyChecker = Mockito.spy(checker);

		ASTTypeMeth typeMeth = new ASTIntType(0, 0);
		ASTIdent ident = new ASTIdent("meth", 0, 0);
		ASTIdent ident1 = new ASTIdent("a", 0, 0);

		ASTParams params = new ASTParamsNil(0, 0);
		ASTVars vars = new ASTVarsNil(0, 0);
		ASTReturn ret = new ASTReturn(ident1, 0, 0);
		ASTInstrsNil instrsNil = new ASTInstrsNil(0, 0);
		ASTInstrs instrs = new ASTInstrsChain(ret, instrsNil, 0, 0);

		ASTVarSimple varSimple = new ASTVarSimple(new ASTIntType(0, 0), ident, null, 0, 0);
		ASTMethod method = new ASTMethod(typeMeth, ident, params, vars, instrs, 0, 0);

		ASTExprsNil exprsNil = new ASTExprsNil(0, 0);
		ASTMethodCallInstr callInstr = new ASTMethodCallInstr(ident, exprsNil, 0, 0);

		Mockito.doNothing().when(spyChecker).check(vars);
		Mockito.doNothing().when(spyChecker).check(instrs);
		Mockito.doReturn(Type.INT).when(spyChecker).evalType(ident1);

		spyChecker.check(varSimple);
		Assert.assertEquals(0, diag.issues().size());
		spyChecker.check(method);
		Assert.assertEquals(0, diag.issues().size());

		Type type = spyChecker.evalType(callInstr);

		Assert.assertEquals(Type.INT, type);
		Assert.assertEquals(0, diag.issues().size());
	}

	@Test
	public void methodExprTest() {
		TypeChecker spyChecker = Mockito.spy(checker);

		ASTTypeMeth typeMeth = new ASTIntType(0, 0);
		ASTIdent ident = new ASTIdent("meth", 0, 0);
		ASTIdent ident1 = new ASTIdent("a", 0, 0);

		ASTParams params = new ASTParamsNil(0, 0);
		ASTVars vars = new ASTVarsNil(0, 0);
		ASTReturn ret = new ASTReturn(ident1, 0, 0);
		ASTInstrsNil instrsNil = new ASTInstrsNil(0, 0);
		ASTInstrs instrs = new ASTInstrsChain(ret, instrsNil, 0, 0);
		ASTMethod method = new ASTMethod(typeMeth, ident, params, vars, instrs, 0, 0);

		Mockito.doNothing().when(spyChecker).check(vars);
		Mockito.doNothing().when(spyChecker).check(instrs);
		Mockito.doReturn(Type.INT).when(spyChecker).evalType(ident1);

		spyChecker.check(method);

		ASTExprsNil exprsNil = new ASTExprsNil(0, 0);
		ASTMethodCallExpr callExpr = new ASTMethodCallExpr(ident, exprsNil, 0, 0);

		Type type = spyChecker.evalType(callExpr);

		Assert.assertEquals(Type.INT, type);
		Assert.assertEquals(0, diag.issues().size());
	}

	@Test
	public void methodExprVoidType() {
		TypeChecker spyChecker = Mockito.spy(checker);

		ASTTypeMeth typeMeth = new ASTVoidType(0, 0);
		ASTIdent ident = new ASTIdent("meth", 0, 0);
		ASTIdent ident1 = new ASTIdent("a", 0, 0);

		ASTParams params = new ASTParamsNil(0, 0);
		ASTVars vars = new ASTVarsNil(0, 0);
		ASTReturn ret = new ASTReturn(ident1, 0, 0);
		ASTInstrsNil instrsNil = new ASTInstrsNil(0, 0);
		ASTInstrs instrs = new ASTInstrsChain(ret, instrsNil, 0, 0);
		ASTMethod method = new ASTMethod(typeMeth, ident, params, vars, instrs, 0, 0);

		Mockito.doNothing().when(spyChecker).check(vars);
		Mockito.doNothing().when(spyChecker).check(instrs);
		Mockito.doReturn(Type.INT).when(spyChecker).evalType(ident1);

		spyChecker.check(method);

		ASTExprsNil exprsNil = new ASTExprsNil(0, 0);
		ASTMethodCallExpr callExpr = new ASTMethodCallExpr(ident, exprsNil, 0, 0);

		Type type = spyChecker.evalType(callExpr);

		Assert.assertEquals(Type.VOID, type);
		Assert.assertEquals(1, diag.issues().size());
		Assert.assertEquals(ErrorCode.TYPE_MISMATCH, getError(0));
	}

	@Test
	public void bonusInstrTest() {
		TypeChecker spyChecker = Mockito.spy(checker);
		ASTIdent ident = new ASTIdent("a", 0, 0);

		// CHEVROT (Assert) - Correct (BOOL)
		ASTBonusInstr chevrot = new ASTBonusInstr(ASTBonusInstr.Op.CHEVROT, ident, 0, 0);
		Mockito.doReturn(Type.BOOL).when(spyChecker).evalType(ident);
		spyChecker.check(chevrot);
		Assert.assertEquals(0, diag.issues().size());

		// CHEVROT (Assert) - Incorrect (INT)
		Mockito.doReturn(Type.INT).when(spyChecker).evalType(ident);
		spyChecker.check(chevrot);
		Assert.assertEquals(1, diag.issues().size());
		Assert.assertEquals(ErrorCode.TYPE_MISMATCH, getError(0));

		// Reset
		diag = new Diagnostic();
		checker = new TypeChecker(diag);
		spyChecker = Mockito.spy(checker);

		// DUGOIS (Sleep) - Correct (INT)
		ASTBonusInstr dugois = new ASTBonusInstr(ASTBonusInstr.Op.DUGOIS, ident, 0, 0);
		Mockito.doReturn(Type.INT).when(spyChecker).evalType(ident);
		spyChecker.check(dugois);
		Assert.assertEquals(0, diag.issues().size());

		// DUGOIS (Sleep) - Incorrect (BOOL)
		Mockito.doReturn(Type.BOOL).when(spyChecker).evalType(ident);
		spyChecker.check(dugois);
		Assert.assertEquals(1, diag.issues().size());
		Assert.assertEquals(ErrorCode.TYPE_MISMATCH, getError(0));
	}

	@Test
	public void bonusExprTest() {
		TypeChecker spyChecker = Mockito.spy(checker);
		ASTIdent ident1 = new ASTIdent("a", 0, 0);
		ASTIdent ident2 = new ASTIdent("b", 0, 0);

		// JACQUIN (Pow) - Correct (INT, INT)
		ASTBonusExpr jacquin = new ASTBonusExpr(ASTBonusExpr.Op.JACQUIN, ident1, ident2, 0, 0);
		Mockito.doReturn(Type.INT).when(spyChecker).evalType(ident1);
		Mockito.doReturn(Type.INT).when(spyChecker).evalType(ident2);
		Type type = spyChecker.evalType(jacquin);
		Assert.assertEquals(Type.INT, type);
		Assert.assertEquals(0, diag.issues().size());

		// JACQUIN (Pow) - Incorrect (BOOL, INT)
		Mockito.doReturn(Type.BOOL).when(spyChecker).evalType(ident1);
		spyChecker.evalType(jacquin);
		Assert.assertEquals(1, diag.issues().size());
		Assert.assertEquals(ErrorCode.TYPE_MISMATCH, getError(0));

		// Reset
		diag = new Diagnostic();
		checker = new TypeChecker(diag);
		spyChecker = Mockito.spy(checker);

		// BOUQUET (Random) - Correct (INT, INT)
		ASTBonusExpr bouquet = new ASTBonusExpr(ASTBonusExpr.Op.BOUQUET, ident1, ident2, 0, 0);
		Mockito.doReturn(Type.INT).when(spyChecker).evalType(ident1);
		Mockito.doReturn(Type.INT).when(spyChecker).evalType(ident2);
		type = spyChecker.evalType(bouquet);
		Assert.assertEquals(Type.INT, type);
		Assert.assertEquals(0, diag.issues().size());
	}

	@Test
	public void ternaryOpTest() {
		TypeChecker spyChecker = Mockito.spy(checker);
		ASTIdent cond = new ASTIdent("c", 0, 0);
		ASTIdent case1 = new ASTIdent("t", 0, 0);
		ASTIdent case2 = new ASTIdent("f", 0, 0);
		ASTTernaryOp ternary = new ASTTernaryOp(cond, case1, case2, 0, 0);

		// Correct: BOOL ? INT : INT
		Mockito.doReturn(Type.BOOL).when(spyChecker).evalType(cond);
		Mockito.doReturn(Type.INT).when(spyChecker).evalType(case1);
		Mockito.doReturn(Type.INT).when(spyChecker).evalType(case2);
		Type type = spyChecker.evalType(ternary);
		Assert.assertEquals(Type.INT, type);
		Assert.assertEquals(0, diag.issues().size());

		// Incorrect condition: INT ? INT : INT
		Mockito.doReturn(Type.INT).when(spyChecker).evalType(cond);
		spyChecker.evalType(ternary);
		Assert.assertEquals(1, diag.issues().size());
		Assert.assertEquals(ErrorCode.TYPE_MISMATCH, getError(0));

		// Reset
		diag = new Diagnostic();
		checker = new TypeChecker(diag);
		spyChecker = Mockito.spy(checker);

		// Incorrect types: BOOL ? INT : BOOL
		Mockito.doReturn(Type.BOOL).when(spyChecker).evalType(cond);
		Mockito.doReturn(Type.INT).when(spyChecker).evalType(case1);
		Mockito.doReturn(Type.BOOL).when(spyChecker).evalType(case2);
		spyChecker.evalType(ternary);
		Assert.assertEquals(1, diag.issues().size());
		Assert.assertEquals(ErrorCode.TYPE_MISMATCH, getError(0));
	}

	@Test
	public void returnIntTest() {
		TypeChecker spyChecker = Mockito.spy(checker);
		ASTIdent ident = new ASTIdent("a", 0, 0);

		ASTReturn ret = new ASTReturn(ident, 0, 0);

		Mockito.doReturn(Type.INT).when(spyChecker).evalType(ident);

		Type type = spyChecker.evalType(ret);

		Assert.assertEquals(Type.INT, type);
		Assert.assertEquals(0, diag.issues().size());
	}

	@Test
	public void returnBoolTest() {
		TypeChecker spyChecker = Mockito.spy(checker);
		ASTIdent ident = new ASTIdent("a", 0, 0);

		ASTReturn ret = new ASTReturn(ident, 0, 0);

		Mockito.doReturn(Type.BOOL).when(spyChecker).evalType(ident);

		Type type = spyChecker.evalType(ret);

		Assert.assertEquals(Type.BOOL, type);
		Assert.assertEquals(0, diag.issues().size());
	}

	@Test
	public void lengthTest() {
		ASTIdent ident = new ASTIdent("a", 0, 0);
		Type type = checker.evalType(new ASTLength(ident, 0, 0));
		Assert.assertEquals(Type.INT, type);
		Assert.assertEquals(0, diag.issues().size());
	}

	@Test
	public void literalTest() {
		ASTIntLiteral intLiteral = new ASTIntLiteral(1, 0, 0);
		ASTBoolLiteral boolLiteral = new ASTBoolLiteral(true, 0, 0);
		ASTStringLiteral stringLiteral = new ASTStringLiteral("ah", 0, 0);

		Assert.assertEquals(Type.INT, checker.evalType(intLiteral));
		Assert.assertEquals(Type.BOOL, checker.evalType(boolLiteral));
		Assert.assertEquals(Type.STRING, checker.evalType(stringLiteral));
		Assert.assertEquals(0, diag.issues().size());
	}

	@Test
	public void identNotFoundTest() {
		ASTIdent ident = new ASTIdent("a", 0, 0);
		Type type = checker.evalType(ident);
		Assert.assertEquals(null, type);
		Assert.assertEquals(1, diag.issues().size());
		Assert.assertEquals(ErrorCode.UNKNOWN_VARIABLE, getError(0));
	}

	@Test
	public void identFoundTest() {
		ASTIdent ident = new ASTIdent("a", 0, 0);

		ASTVarSimple varSimple = new ASTVarSimple(new ASTIntType(0, 0), ident, null, 0, 0);

		checker.check(varSimple);
		Assert.assertEquals(0, diag.issues().size());

		Type type = checker.evalType(ident);
		Assert.assertEquals(Type.INT, type);
		Assert.assertEquals(0, diag.issues().size());
	}

	@Test
	public void testSwitchCase() {
		new ASTClass(new ASTIdent("A", 0, 0), new ASTDeclsNil(0, 0),
				new ASTMainMethod(new ASTVarsNil(0, 0), new ASTInstrsNil(0, 0), 0, 0), 0, 0);

		TypeChecker spyChecker = Mockito.spy(checker);
		ASTIdent ident = new ASTIdent("a", 0, 0);
		ASTAssignmentOp defInstr = new ASTAssignmentOp(ident, new ASTIntLiteral(30, 0, 0),
				AssignmentOp.ADD, 0, 0);
		ASTInstrs defInstrs = new ASTInstrsChain(defInstr, new ASTInstrsNil(0, 0), 0, 0);
		ASTDefault def = new ASTDefault(defInstrs, 0, 0);
		ASTAssignmentOp cs20Instr = new ASTAssignmentOp(ident, new ASTIntLiteral(20, 0, 0),
				AssignmentOp.ADD, 0, 0);
		ASTInstrs cs20Instrs = new ASTInstrsChain(cs20Instr, new ASTInstrsNil(0, 0), 0, 0);
		ASTCase cs20 = new ASTCase(new ASTIntLiteral(20, 0, 0), cs20Instrs, 0, 0);
		ASTAssignmentOp cs10Instr = new ASTAssignmentOp(ident, new ASTIntLiteral(10, 0, 0),
				AssignmentOp.ADD, 0, 0);
		ASTInstrs cs10Instrs = new ASTInstrsChain(cs10Instr, new ASTInstrsNil(0, 0), 0, 0);
		ASTCase cs10 = new ASTCase(new ASTIntLiteral(10, 0, 0), cs10Instrs, 0, 0);
		ASTCasesChain chain = new ASTCasesChain(cs10,
				new ASTCasesChain(cs20, new ASTCasesChain(def, new ASTCasesNil(0, 0), 0, 0), 0, 0), 0, 0);

		ASTSwitch swt = new ASTSwitch(ident, chain, 0, 0);

		Mockito.doReturn(Type.INT).when(spyChecker).evalType(ident);
		Mockito.doNothing().when(spyChecker).check(defInstr);
		Mockito.doNothing().when(spyChecker).check(cs20Instr);
		Mockito.doNothing().when(spyChecker).check(cs10Instr);
		spyChecker.check(swt);

		Assert.assertEquals(0, diag.issues().size());
	}

	@Test
	public void testSwitchDefault() {
		new ASTClass(new ASTIdent("A", 0, 0), new ASTDeclsNil(0, 0),
				new ASTMainMethod(new ASTVarsNil(0, 0), new ASTInstrsNil(0, 0), 0, 0), 0, 0);

		TypeChecker spyChecker = Mockito.spy(checker);
		ASTIdent ident = new ASTIdent("a", 0, 0);
		ASTAssignmentOp defInstr = new ASTAssignmentOp(ident, new ASTIntLiteral(30, 0, 0),
				AssignmentOp.ADD, 0, 0);
		ASTInstrs defInstrs = new ASTInstrsChain(defInstr, new ASTInstrsNil(0, 0), 0, 0);
		ASTDefault def = new ASTDefault(defInstrs, 0, 0);
		ASTAssignmentOp cs20Instr = new ASTAssignmentOp(ident, new ASTIntLiteral(20, 0, 0),
				AssignmentOp.ADD, 0, 0);
		ASTInstrs cs20Instrs = new ASTInstrsChain(cs20Instr, new ASTInstrsNil(0, 0), 0, 0);
		ASTCase cs20 = new ASTCase(new ASTIntLiteral(20, 0, 0), cs20Instrs, 0, 0);
		ASTAssignmentOp cs10Instr = new ASTAssignmentOp(ident, new ASTIntLiteral(10, 0, 0),
				AssignmentOp.ADD, 0, 0);
		ASTInstrs cs10Instrs = new ASTInstrsChain(cs10Instr, new ASTInstrsNil(0, 0), 0, 0);
		ASTCase cs10 = new ASTCase(new ASTIntLiteral(10, 0, 0), cs10Instrs, 0, 0);
		ASTCasesChain chain = new ASTCasesChain(cs10,
				new ASTCasesChain(cs20, new ASTCasesChain(def, new ASTCasesNil(0, 0), 0, 0), 0, 0), 0, 0);

		ASTSwitch swt = new ASTSwitch(ident, chain, 0, 0);

		Mockito.doReturn(Type.BOOL).when(spyChecker).evalType(ident);
		Mockito.doNothing().when(spyChecker).check(defInstr);
		Mockito.doNothing().when(spyChecker).check(cs20Instr);
		Mockito.doNothing().when(spyChecker).check(cs10Instr);
		spyChecker.check(swt);

		Assert.assertEquals(2, diag.issues().size());
		Assert.assertEquals(ErrorCode.TYPE_MISMATCH, getError(0));
		Assert.assertEquals(ErrorCode.TYPE_MISMATCH, getError(1));
	}
}
