package fr.umlp.m1info.projectvms.group2.compilation;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.decl.ASTClass;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decl.ASTMainMethod;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decl.ASTMethod;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decls.ASTDecls;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instrs.ASTInstrs;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.params.ASTParams;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.type.ASTTypeMeth;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.type.ASTVoidType;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.vars.ASTVars;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTArrayAccess;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTBinaryOp;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTBoolLiteral;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTExpr;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTIdent;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTIntLiteral;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTStringLiteral;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTUnaryOp;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTVExpr;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.BinaryOp;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.UnaryOp;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.exprs.ASTExprsChain;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.exprs.ASTExprsNil;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.type.ASTIntType;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.var.ASTVar;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.var.ASTVarArray;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.var.ASTVarConst;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.var.ASTVarSimple;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.vars.ASTVarsChain;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.vars.ASTVarsNil;

import fr.umlp.m1info.projectvms.group2.exception.CompilationRuntimeException;
import fr.umlp.m1info.projectvms.group2.exception.FreeCompilationRuntimeException;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;

public class MiniJajaCompilerTest {
	MiniJajaCompiler miniJajaCompiler;
	StringBuilder sb;

	@Before
	public void setUp() {
		sb = new StringBuilder();
		miniJajaCompiler = new MiniJajaCompiler(sb);
	}

	@Test
	public void testUtilsBinary() {
		Assert.assertThrows(CompilationRuntimeException.class, () -> MiniJajaCompilerUtils.binaryOpToString("UNKNOWN"));
	}

	@Test
	public void testUtilsUnary() {
		Assert.assertThrows(CompilationRuntimeException.class, () -> MiniJajaCompilerUtils.unaryOpToString("UNKNOWN"));
	}

	@Test
	public void testNullCompiler() {
		Assert.assertThrows(CompilationRuntimeException.class, () -> new MiniJajaCompiler(null));
	}

	@Test
	public void testCompileException() {
		Assert.assertThrows(CompilationRuntimeException.class, () -> miniJajaCompiler.compile(null));
		ASTIntType type = new ASTIntType(0, 0);
		Assert.assertThrows(CompilationRuntimeException.class, () -> miniJajaCompiler.compile(type));
		ASTIdent ident = new ASTIdent("a", 0, 0);

		MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);
		Mockito.doThrow(new CompilationRuntimeException("")).when(spyCompiler).compileIdent(ident);
		Assert.assertThrows(CompilationRuntimeException.class, () -> spyCompiler.compile(ident));
	}

	@Test
	public void testFreeException() {
		Assert.assertThrows(FreeCompilationRuntimeException.class, () -> miniJajaCompiler.free(null));
		ASTIntType type = new ASTIntType(0, 0);
		Assert.assertThrows(FreeCompilationRuntimeException.class, () -> miniJajaCompiler.free(type));
		ASTVarsNil nil = new ASTVarsNil(0, 0);

		MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);
		Mockito.doThrow(new FreeCompilationRuntimeException("")).when(spyCompiler).freeVarsNil(nil);
		Assert.assertThrows(FreeCompilationRuntimeException.class, () -> spyCompiler.free(nil));
	}

	@Test
	public void testCompileClasse() {
		ASTClass classNode = Mockito.mock(ASTClass.class);
		ASTDecls decls = Mockito.mock(ASTDecls.class);
		ASTMainMethod mainMethod = Mockito.mock(ASTMainMethod.class);
		Mockito.when(classNode.getDecls()).thenReturn(decls);
		Mockito.when(classNode.getMainMethod()).thenReturn(mainMethod);
		MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);
		Mockito.doReturn(0).when(spyCompiler).compile(decls);
		Mockito.doReturn(0).when(spyCompiler).compile(mainMethod);
		Mockito.doReturn(0).when(spyCompiler).free(decls);
		int nbInstr = spyCompiler.compile(classNode);
		assertEquals(3, nbInstr);
		assertEquals("init\npop\njcstop\n", sb.toString());
	}

	@Test
	public void testCompileMainMethod() {
		ASTMainMethod mainMethod = Mockito.mock(ASTMainMethod.class);
		ASTVars vars = Mockito.mock(ASTVars.class);
		ASTInstrs instrs = Mockito.mock(ASTInstrs.class);
		Mockito.when(mainMethod.getVars()).thenReturn(vars);
		Mockito.when(mainMethod.getInstrs()).thenReturn(instrs);
		MiniJajaCompiler compiler = Mockito.spy(new MiniJajaCompiler(sb));
		Mockito.doReturn(0).when(compiler).compile(vars);
		Mockito.doReturn(0).when(compiler).compile(instrs);
		Mockito.doReturn(0).when(compiler).free(vars);
		int result = compiler.compileMainMethod(mainMethod);
		assertEquals(1, result);
		assertEquals("push(0)\n", sb.toString());
	}

	@Test
	public void testCompileMethod() {
		ASTMethod method = Mockito.mock(ASTMethod.class);
		ASTIdent ident = Mockito.mock(ASTIdent.class);
		ASTParams params = Mockito.mock(ASTParams.class);
		ASTVars vars = Mockito.mock(ASTVars.class);
		ASTInstrs instrs = Mockito.mock(ASTInstrs.class);
		ASTTypeMeth type = new ASTIntType(0, 0);
		Mockito.when(method.getIdent()).thenReturn(ident);
		Mockito.when(method.getParams()).thenReturn(params);
		Mockito.when(method.getVars()).thenReturn(vars);
		Mockito.when(method.getInstrs()).thenReturn(instrs);
		Mockito.when(method.getType()).thenReturn(type);
		Mockito.when(ident.getName()).thenReturn("m");
		Mockito.when(instrs.toString()).thenReturn("x := 1;");
		MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);
		Mockito.doReturn(0).when(spyCompiler).compile(params);
		Mockito.doReturn(0).when(spyCompiler).compile(vars);
		Mockito.doReturn(0).when(spyCompiler).compile(instrs);
		Mockito.doReturn(0).when(spyCompiler).free(vars);
		int nbInstr = spyCompiler.compile(method);
		assertEquals(7, nbInstr);
		assertEquals("jcnil\npush(3)\nnew(m, int, meth, 0)\ngoto(5)\npush(0)\nswap\nreturn\n", sb.toString());
	}

	@Test
	public void testCompileMethodVoid() {
		ASTMethod method = Mockito.mock(ASTMethod.class);
		ASTIdent ident = Mockito.mock(ASTIdent.class);
		ASTParams params = Mockito.mock(ASTParams.class);
		ASTVars vars = Mockito.mock(ASTVars.class);
		ASTInstrs instrs = Mockito.mock(ASTInstrs.class);
		ASTTypeMeth type = new ASTVoidType(0, 0);
		Mockito.when(method.getIdent()).thenReturn(ident);
		Mockito.when(method.getParams()).thenReturn(params);
		Mockito.when(method.getVars()).thenReturn(vars);
		Mockito.when(method.getInstrs()).thenReturn(instrs);
		Mockito.when(method.getType()).thenReturn(type);
		Mockito.when(ident.getName()).thenReturn("m");
		Mockito.when(instrs.toString()).thenReturn("x := 1;");
		MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);
		Mockito.doReturn(0).when(spyCompiler).compile(params);
		Mockito.doReturn(0).when(spyCompiler).compile(vars);
		Mockito.doReturn(0).when(spyCompiler).compile(instrs);
		Mockito.doReturn(0).when(spyCompiler).free(vars);
		int nbInstr = spyCompiler.compile(method);
		assertEquals(6, nbInstr);
		assertEquals("jcnil\npush(3)\nnew(m, void, meth, 0)\ngoto(4)\nswap\nreturn\n", sb.toString());
	}

	@Test
	public void testCompileIdent() {
		ASTIdent ident = new ASTIdent("a", 0, 0);
		int nbInstr = miniJajaCompiler.compile(ident);
		assertEquals("load(a)\n", sb.toString());
		assertEquals(1, nbInstr);
	}

	@Test
	public void testCompileStringLiteral() {
		ASTStringLiteral stringLiteral = new ASTStringLiteral("text", 0, 0);
		int nbInstr = miniJajaCompiler.compile(stringLiteral);
		assertEquals("push(text)\n", sb.toString());
		assertEquals(1, nbInstr);
	}

	@Test
	public void testCompileIntLiteral() {
		ASTIntLiteral intLiteral = new ASTIntLiteral(3, 0, 0);
		int nbInstr = miniJajaCompiler.compile(intLiteral);
		assertEquals("push(3)\n", sb.toString());
		assertEquals(1, nbInstr);
	}

	@Test
	public void testCompileBoolLiteral() {
		ASTBoolLiteral trueBoolLiteral = new ASTBoolLiteral(true, 0, 0);
		ASTBoolLiteral falseBoolLiteral = new ASTBoolLiteral(false, 0, 0);
		int nbInstr = miniJajaCompiler.compile(trueBoolLiteral);

		assertEquals("push(true)\n", sb.toString());
		assertEquals(1, nbInstr);

		nbInstr = miniJajaCompiler.compile(falseBoolLiteral);
		assertEquals("push(true)\npush(false)\n", sb.toString());
		assertEquals(1, nbInstr);
	}

	@Test
	public void testCompileArrayAccess() {
		ASTIdent ident = new ASTIdent("t", 0, 0);
		ASTIntLiteral intLiteral = new ASTIntLiteral(2, 0, 0);
		ASTArrayAccess arrayAccess = new ASTArrayAccess(ident, intLiteral, 0, 0);

		MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);
		Mockito.doReturn(1).when(spyCompiler).compile(intLiteral);

		int nbInstr = spyCompiler.compile(arrayAccess);
		assertEquals(2, nbInstr);
		assertEquals("aload(t)\n", sb.toString());
	}

	@Test
	public void testCompileUnaryOpNegation() {
		ASTIntLiteral intLiteral = new ASTIntLiteral(5, 0, 0);
		ASTUnaryOp unaryOp = new ASTUnaryOp(intLiteral, UnaryOp.NEG, 0, 0);

		MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);
		Mockito.doReturn(1).when(spyCompiler).compile(intLiteral);

		int nbInstr = spyCompiler.compile(unaryOp);
		assertEquals(2, nbInstr);
		assertEquals("neg\n", sb.toString());
	}

	@Test
	public void testCompileUnaryOpNot() {
		ASTIntLiteral intLiteral = new ASTIntLiteral(1, 0, 0);
		ASTUnaryOp unaryOp = new ASTUnaryOp(intLiteral, UnaryOp.NOT, 0, 0);

		MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);
		Mockito.doReturn(1).when(spyCompiler).compile(intLiteral);

		int nbInstr = spyCompiler.compile(unaryOp);
		assertEquals(2, nbInstr);
		assertEquals("not\n", sb.toString());
	}

	@Test
	public void testCompileBinaryOpAdd() {
		ASTIntLiteral left = new ASTIntLiteral(1, 0, 0);
		ASTIntLiteral right = new ASTIntLiteral(2, 0, 0);
		ASTBinaryOp binaryOp = new ASTBinaryOp(left, right, BinaryOp.ADD, 0, 0);

		MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);
		Mockito.doReturn(1).when(spyCompiler).compile(left);
		Mockito.doReturn(1).when(spyCompiler).compile(right);

		int nbInstr = spyCompiler.compile(binaryOp);
		assertEquals(3, nbInstr);
		assertEquals("add\n", sb.toString());
	}

	@Test
	public void testCompileBinaryOpSub() {
		ASTIntLiteral left = new ASTIntLiteral(5, 0, 0);
		ASTIntLiteral right = new ASTIntLiteral(3, 0, 0);
		ASTBinaryOp binaryOp = new ASTBinaryOp(left, right, BinaryOp.SUB, 0, 0);

		MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);
		Mockito.doReturn(1).when(spyCompiler).compile(left);
		Mockito.doReturn(1).when(spyCompiler).compile(right);

		int nbInstr = spyCompiler.compile(binaryOp);
		assertEquals(3, nbInstr);
		assertEquals("sub\n", sb.toString());
	}

	@Test
	public void testCompileBinaryOpMul() {
		ASTIntLiteral left = new ASTIntLiteral(4, 0, 0);
		ASTIntLiteral right = new ASTIntLiteral(7, 0, 0);
		ASTBinaryOp binaryOp = new ASTBinaryOp(left, right, BinaryOp.MUL, 0, 0);

		MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);
		Mockito.doReturn(1).when(spyCompiler).compile(left);
		Mockito.doReturn(1).when(spyCompiler).compile(right);

		int nbInstr = spyCompiler.compile(binaryOp);
		assertEquals(3, nbInstr);
		assertEquals("mul\n", sb.toString());
	}

	@Test
	public void testCompileBinaryOpDiv() {
		ASTIntLiteral left = new ASTIntLiteral(8, 0, 0);
		ASTIntLiteral right = new ASTIntLiteral(2, 0, 0);
		ASTBinaryOp binaryOp = new ASTBinaryOp(left, right, BinaryOp.DIV, 0, 0);

		MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);
		Mockito.doReturn(1).when(spyCompiler).compile(left);
		Mockito.doReturn(1).when(spyCompiler).compile(right);

		int nbInstr = spyCompiler.compile(binaryOp);
		assertEquals(3, nbInstr);
		assertEquals("div\n", sb.toString());
	}

	@Test
	public void testCompileBinaryOpAnd() {
		ASTIntLiteral left = new ASTIntLiteral(1, 0, 0);
		ASTIntLiteral right = new ASTIntLiteral(0, 0, 0);
		ASTBinaryOp binaryOp = new ASTBinaryOp(left, right, BinaryOp.AND, 0, 0);

		MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);
		Mockito.doReturn(1).when(spyCompiler).compile(left);
		Mockito.doReturn(1).when(spyCompiler).compile(right);

		int nbInstr = spyCompiler.compile(binaryOp);
		assertEquals(3, nbInstr);
		assertEquals("and\n", sb.toString());
	}

	@Test
	public void testCompileBinaryOpOr() {
		ASTIntLiteral left = new ASTIntLiteral(0, 0, 0);
		ASTIntLiteral right = new ASTIntLiteral(1, 0, 0);
		ASTBinaryOp binaryOp = new ASTBinaryOp(left, right, BinaryOp.OR, 0, 0);

		MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);
		Mockito.doReturn(1).when(spyCompiler).compile(left);
		Mockito.doReturn(1).when(spyCompiler).compile(right);

		int nbInstr = spyCompiler.compile(binaryOp);
		assertEquals(3, nbInstr);
		assertEquals("or\n", sb.toString());
	}

	@Test
	public void testCompileBinaryOpEqual() {
		ASTIntLiteral left = new ASTIntLiteral(2, 0, 0);
		ASTIntLiteral right = new ASTIntLiteral(2, 0, 0);
		ASTBinaryOp binaryOp = new ASTBinaryOp(left, right, BinaryOp.EQ, 0, 0);

		MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);
		Mockito.doReturn(1).when(spyCompiler).compile(left);
		Mockito.doReturn(1).when(spyCompiler).compile(right);

		int nbInstr = spyCompiler.compile(binaryOp);
		assertEquals(3, nbInstr);
		assertEquals("cmp\n", sb.toString());
	}

	@Test
	public void testCompileBinaryOpGreater() {
		ASTIntLiteral left = new ASTIntLiteral(5, 0, 0);
		ASTIntLiteral right = new ASTIntLiteral(3, 0, 0);
		ASTBinaryOp binaryOp = new ASTBinaryOp(left, right, BinaryOp.GT, 0, 0);

		MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);
		Mockito.doReturn(1).when(spyCompiler).compile(left);
		Mockito.doReturn(1).when(spyCompiler).compile(right);

		int nbInstr = spyCompiler.compile(binaryOp);
		assertEquals(3, nbInstr);
		assertEquals("sup\n", sb.toString());
	}

	@Test
	public void testCompileExprsChain() {
		MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);

		ASTIntLiteral expr1 = Mockito.mock(ASTIntLiteral.class);
		ASTIntLiteral expr2 = Mockito.mock(ASTIntLiteral.class);

		ASTExprsNil nil = new ASTExprsNil(0, 0);
		ASTExprsChain innerChain = new ASTExprsChain(expr2, nil, 0, 0);
		ASTExprsChain outerChain = new ASTExprsChain(expr1, innerChain, 0, 0);

		Mockito.doReturn(1).when(spyCompiler).compile(Mockito.any(ASTIntLiteral.class));
		int nbInstr = spyCompiler.compile(outerChain);

		assertEquals(2, nbInstr);
	}

	@Test
	public void testCompileExprsNil() {
		int nbInstr = miniJajaCompiler.compile(new ASTExprsNil(0, 0));
		assertEquals(0, nbInstr);
	}

	@Test
	public void testCompileVarsChain() {
		MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);

		ASTVar var1 = Mockito.mock(ASTVar.class);
		ASTVar var2 = Mockito.mock(ASTVar.class);
		ASTVarsNil nil = new ASTVarsNil(0, 0);
		ASTVarsChain innerChain = new ASTVarsChain(var2, nil, 0, 0);
		ASTVarsChain outerChain = new ASTVarsChain(var1, innerChain, 0, 0);

		Mockito.doReturn(1).when(spyCompiler).compile(var1);
		Mockito.doReturn(1).when(spyCompiler).compile(var2);
		Mockito.doReturn(1).when(spyCompiler).compile(innerChain);
		Mockito.doReturn(0).when(spyCompiler).compile(nil);

		int nbInstr = spyCompiler.compile(outerChain);

		assertEquals(2, nbInstr);
	}

	@Test
	public void testCompileVarsNil() {
		MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);
		ASTVarsNil varsNil = new ASTVarsNil(0, 0);
		int nbInstr = spyCompiler.compile(varsNil);
		assertEquals(0, nbInstr);
	}

	@Test
	public void testCompileVarSimple() {
		MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);

		ASTVarSimple varSimple = Mockito.mock(ASTVarSimple.class);
		ASTIdent ident = Mockito.mock(ASTIdent.class);
		ASTIntType type = new ASTIntType(0, 0);
		ASTVExpr vexpr = Mockito.mock(ASTVExpr.class);

		Mockito.when(varSimple.getIdent()).thenReturn(ident);
		Mockito.when(varSimple.getType()).thenReturn(type);
		Mockito.when(varSimple.getVExpr()).thenReturn(vexpr);
		Mockito.when(ident.getName()).thenReturn("x");
		Mockito.doReturn(1).when(spyCompiler).compile(vexpr);

		int nbInstr = spyCompiler.compile(varSimple);

		assertEquals("new(x, int, var, 0)\n", sb.toString());
		assertEquals(2, nbInstr);
	}

	@Test
	public void testCompileVarSimpleNotInit() {
		MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);

		ASTVarSimple varSimple = Mockito.mock(ASTVarSimple.class);
		ASTIdent ident = Mockito.mock(ASTIdent.class);
		ASTIntType type = new ASTIntType(0, 0);

		Mockito.when(varSimple.getIdent()).thenReturn(ident);
		Mockito.when(varSimple.getType()).thenReturn(type);
		Mockito.when(varSimple.getVExpr()).thenReturn(null);
		Mockito.when(ident.getName()).thenReturn("x");

		int nbInstr = spyCompiler.compile(varSimple);

		assertEquals("push(0)\nnew(x, int, var, 0)\n", sb.toString());
		assertEquals(2, nbInstr);
	}

	@Test
	public void testCompileVarArray() {
		MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);

		ASTVarArray varArray = Mockito.mock(ASTVarArray.class);
		ASTIdent ident = Mockito.mock(ASTIdent.class);
		ASTIntType type = new ASTIntType(0, 0);
		ASTExpr expr = Mockito.mock(ASTExpr.class);

		Mockito.when(varArray.getIdent()).thenReturn(ident);
		Mockito.when(varArray.getType()).thenReturn(type);
		Mockito.when(varArray.getExpr()).thenReturn(expr);
		Mockito.when(ident.getName()).thenReturn("arr");
		Mockito.doReturn(1).when(spyCompiler).compile(expr);

		int nbInstr = spyCompiler.compile(varArray);

		assertEquals("newarray(arr, int)\n", sb.toString());
		assertEquals(2, nbInstr);
	}

	@Test
	public void testCompileVarConst() {
		MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);

		ASTVarConst varConst = Mockito.mock(ASTVarConst.class);
		ASTIdent ident = Mockito.mock(ASTIdent.class);
		ASTIntType type = new ASTIntType(0, 0);
		ASTVExpr vexpr = Mockito.mock(ASTVExpr.class);

		Mockito.when(varConst.getIdent()).thenReturn(ident);
		Mockito.when(varConst.getType()).thenReturn(type);
		Mockito.when(varConst.getVExpr()).thenReturn(vexpr);
		Mockito.when(ident.getName()).thenReturn("C");
		Mockito.doReturn(1).when(spyCompiler).compile(vexpr);

		int nbInstr = spyCompiler.compile(varConst);

		assertEquals("new(C, int, cst, 0)\n", sb.toString());
		assertEquals(2, nbInstr);
	}

	@Test
	public void testCompileVarConstNotInit() {
		MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);

		ASTVarConst varConst = Mockito.mock(ASTVarConst.class);
		ASTIdent ident = Mockito.mock(ASTIdent.class);
		ASTIntType type = new ASTIntType(0, 0);

		Mockito.when(varConst.getIdent()).thenReturn(ident);
		Mockito.when(varConst.getType()).thenReturn(type);
		Mockito.when(varConst.getVExpr()).thenReturn(null);
		Mockito.when(ident.getName()).thenReturn("C");

		int nbInstr = spyCompiler.compile(varConst);

		assertEquals("push(0)\nnew(C, int, cst, 0)\n", sb.toString());
		assertEquals(2, nbInstr);
	}

	@Test
	public void testFreeVarsChain() {
		MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);

		ASTVar var1 = Mockito.mock(ASTVar.class);
		ASTVar var2 = Mockito.mock(ASTVar.class);
		ASTVarsNil nil = new ASTVarsNil(0, 0);
		ASTVarsChain innerChain = new ASTVarsChain(var2, nil, 0, 0);
		ASTVarsChain outerChain = new ASTVarsChain(var1, innerChain, 0, 0);

		Mockito.doReturn(1).when(spyCompiler).free(var1);
		Mockito.doReturn(1).when(spyCompiler).free(var2);
		Mockito.doReturn(1).when(spyCompiler).free(innerChain);
		Mockito.doReturn(0).when(spyCompiler).free(nil);

		int nbInstr = spyCompiler.free(outerChain);

		assertEquals(2, nbInstr);
	}

	@Test
	public void testFreeVarsNil() {
		ASTVarsNil varsNil = Mockito.mock(ASTVarsNil.class);
		int nbInstr = miniJajaCompiler.free(varsNil);
		assertEquals(0, nbInstr);
	}

	@Test
	public void testFreeVar() {
		ASTVar var = Mockito.mock(ASTVar.class);
		int nbInstr = miniJajaCompiler.free(var);
		assertEquals("pop\n", sb.toString());
		assertEquals(1, nbInstr);
	}

	@Test
	public void testCompileVExpr() {
		MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);

		ASTIdent ident = new ASTIdent("a", 0, 0);
		ASTVExpr vexpr = new ASTVExpr(ident, 0, 0);

		Mockito.doReturn(1).when(spyCompiler).compile(ident);

		int nbInstr = spyCompiler.compile(vexpr);

		assertEquals(1, nbInstr);
	}
}
