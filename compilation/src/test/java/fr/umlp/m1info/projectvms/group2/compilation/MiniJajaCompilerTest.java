package fr.umlp.m1info.projectvms.group2.compilation;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.decl.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decls.ASTDecls;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decls.ASTDeclsChain;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decls.ASTDeclsNil;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTArrayAccess;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTBinaryOp;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTBoolLiteral;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTTernaryOp;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTExpr;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTIdent;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTIntLiteral;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTLength;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTMethodCallExpr;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTStringLiteral;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTUnaryOp;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTVExpr;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.BinaryOp;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.UnaryOp;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.exprs.ASTExprs;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.exprs.ASTExprsChain;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.exprs.ASTExprsNil;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.ASTAssignmentOp;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.AssignmentOp;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.IncrementOp;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.ASTIf;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.ASTIncrementOp;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.ASTInstr;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.ASTMethodCallInstr;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.ASTReturn;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.ASTWhile;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.ASTWrite;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instrs.ASTInstrs;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instrs.ASTInstrsChain;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instrs.ASTInstrsNil;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.params.ASTParams;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.params.ASTParamsChain;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.params.ASTParamsNil;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.type.ASTIntType;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.type.ASTTypeMeth;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.type.ASTVoidType;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.var.ASTVar;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.var.ASTVarArray;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.var.ASTVarConst;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.var.ASTVarSimple;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.vars.ASTVars;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.vars.ASTVarsChain;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.vars.ASTVarsNil;
import fr.umlp.m1info.projectvms.group2.exception.CompilationRuntimeException;
import fr.umlp.m1info.projectvms.group2.exception.FreeCompilationRuntimeException;
import fr.umlp.m1info.projectvms.group2.utils.ParserUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import java.nio.file.Files;
import java.nio.file.Paths;

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
		Assert.assertEquals(3, nbInstr);
		Assert.assertEquals("init\npop\njcstop\n", sb.toString());
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
		Assert.assertEquals(1, result);
		Assert.assertEquals("push(0)\n", sb.toString());
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
		Assert.assertEquals(5, nbInstr);
		Assert.assertEquals("push(4)\nnew(m, int, meth, 0)\ngoto(6)\nswap\nreturn\n", sb.toString());
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
		Assert.assertEquals(6, nbInstr);
		Assert.assertEquals("push(4)\nnew(m, void, meth, 0)\ngoto(7)\npush(0)\nswap\nreturn\n", sb.toString());
	}

	@Test
	public void testCompileIdent() {
		ASTIdent ident = new ASTIdent("a", 0, 0);
		int nbInstr = miniJajaCompiler.compile(ident);
		Assert.assertEquals("load(a)\n", sb.toString());
		Assert.assertEquals(1, nbInstr);
	}

	@Test
	public void testCompileStringLiteral() {
		ASTStringLiteral stringLiteral = new ASTStringLiteral("text", 0, 0);
		int nbInstr = miniJajaCompiler.compile(stringLiteral);
		Assert.assertEquals("push(\"text\")\n", sb.toString());
		Assert.assertEquals(1, nbInstr);
	}

	@Test
	public void testCompileIntLiteral() {
		ASTIntLiteral intLiteral = new ASTIntLiteral(3, 0, 0);
		int nbInstr = miniJajaCompiler.compile(intLiteral);
		Assert.assertEquals("push(3)\n", sb.toString());
		Assert.assertEquals(1, nbInstr);
	}

	@Test
	public void testCompileBoolLiteral() {
		ASTBoolLiteral trueBoolLiteral = new ASTBoolLiteral(true, 0, 0);
		ASTBoolLiteral falseBoolLiteral = new ASTBoolLiteral(false, 0, 0);
		int nbInstr = miniJajaCompiler.compile(trueBoolLiteral);

		Assert.assertEquals("push(true)\n", sb.toString());
		Assert.assertEquals(1, nbInstr);

		nbInstr = miniJajaCompiler.compile(falseBoolLiteral);
		Assert.assertEquals("push(true)\npush(false)\n", sb.toString());
		Assert.assertEquals(1, nbInstr);
	}

	@Test
	public void testCompileArrayAccess() {
		ASTIdent ident = new ASTIdent("t", 0, 0);
		ASTIntLiteral intLiteral = new ASTIntLiteral(2, 0, 0);
		ASTArrayAccess arrayAccess = new ASTArrayAccess(ident, intLiteral, 0, 0);

		MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);
		Mockito.doReturn(1).when(spyCompiler).compile(intLiteral);

		int nbInstr = spyCompiler.compile(arrayAccess);
		Assert.assertEquals(2, nbInstr);
		Assert.assertEquals("aload(t)\n", sb.toString());
	}

	@Test
	public void testCompileUnaryOpNegation() {
		ASTIntLiteral intLiteral = new ASTIntLiteral(5, 0, 0);
		ASTUnaryOp unaryOp = new ASTUnaryOp(intLiteral, UnaryOp.NEG, 0, 0);

		MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);
		Mockito.doReturn(1).when(spyCompiler).compile(intLiteral);

		int nbInstr = spyCompiler.compile(unaryOp);
		Assert.assertEquals(2, nbInstr);
		Assert.assertEquals("neg\n", sb.toString());
	}

	@Test
	public void testCompileUnaryOpNot() {
		ASTIntLiteral intLiteral = new ASTIntLiteral(1, 0, 0);
		ASTUnaryOp unaryOp = new ASTUnaryOp(intLiteral, UnaryOp.NOT, 0, 0);

		MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);
		Mockito.doReturn(1).when(spyCompiler).compile(intLiteral);

		int nbInstr = spyCompiler.compile(unaryOp);
		Assert.assertEquals(2, nbInstr);
		Assert.assertEquals("not\n", sb.toString());
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
		Assert.assertEquals(3, nbInstr);
		Assert.assertEquals("add\n", sb.toString());
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
		Assert.assertEquals(3, nbInstr);
		Assert.assertEquals("sub\n", sb.toString());
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
		Assert.assertEquals(3, nbInstr);
		Assert.assertEquals("mul\n", sb.toString());
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
		Assert.assertEquals(3, nbInstr);
		Assert.assertEquals("div\n", sb.toString());
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
		Assert.assertEquals(3, nbInstr);
		Assert.assertEquals("and\n", sb.toString());
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
		Assert.assertEquals(3, nbInstr);
		Assert.assertEquals("or\n", sb.toString());
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
		Assert.assertEquals(3, nbInstr);
		Assert.assertEquals("cmp\n", sb.toString());
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
		Assert.assertEquals(3, nbInstr);
		Assert.assertEquals("sup\n", sb.toString());
	}

	@Test
	public void testCompileTernaryOp() {
		ASTBoolLiteral condition = new ASTBoolLiteral(true, 0, 0);
		ASTIntLiteral firstCase = new ASTIntLiteral(5, 0, 0);
		ASTIntLiteral secondCase = new ASTIntLiteral(3, 0, 0);
		ASTTernaryOp ternaryOp = new ASTTernaryOp(condition, firstCase, secondCase, 0, 0);

		int nbInstr = miniJajaCompiler.compile(ternaryOp);
		Assert.assertEquals(5, nbInstr);
		Assert.assertEquals("push(true)\nif(5)\npush(3)\ngoto(6)\npush(5)\n", sb.toString());

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

		Assert.assertEquals(2, nbInstr);
	}

	@Test
	public void testCompileExprsNil() {
		int nbInstr = miniJajaCompiler.compile(new ASTExprsNil(0, 0));
		Assert.assertEquals(0, nbInstr);
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

		Assert.assertEquals(2, nbInstr);
	}

	@Test
	public void testCompileVarsNil() {
		MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);
		ASTVarsNil varsNil = new ASTVarsNil(0, 0);
		int nbInstr = spyCompiler.compile(varsNil);
		Assert.assertEquals(0, nbInstr);
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

		Assert.assertEquals("new(x, int, var, 0)\n", sb.toString());
		Assert.assertEquals(2, nbInstr);
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

		Assert.assertEquals("push(0)\nnew(x, int, var, 0)\n", sb.toString());
		Assert.assertEquals(2, nbInstr);
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

		Assert.assertEquals("newarray(arr, int)\n", sb.toString());
		Assert.assertEquals(2, nbInstr);
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

		Assert.assertEquals("new(C, int, cst, 0)\n", sb.toString());
		Assert.assertEquals(2, nbInstr);
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

		Assert.assertEquals("push(0)\nnew(C, int, cst, 0)\n", sb.toString());
		Assert.assertEquals(2, nbInstr);
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

		Assert.assertEquals(2, nbInstr);
	}

	@Test
	public void testFreeVarsNil() {
		ASTVarsNil varsNil = Mockito.mock(ASTVarsNil.class);
		int nbInstr = miniJajaCompiler.free(varsNil);
		Assert.assertEquals(0, nbInstr);
	}

	@Test
	public void testFreeVar() {
		ASTVar var = Mockito.mock(ASTVar.class);
		int nbInstr = miniJajaCompiler.free(var);
		Assert.assertEquals("swap\npop\n", sb.toString());
		Assert.assertEquals(2, nbInstr);
	}

	@Test
	public void testCompileVExpr() {
		MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);

		ASTIdent ident = new ASTIdent("a", 0, 0);
		ASTVExpr vexpr = new ASTVExpr(ident, 0, 0);

		Mockito.doReturn(1).when(spyCompiler).compile(ident);

		int nbInstr = spyCompiler.compile(vexpr);

		Assert.assertEquals(1, nbInstr);
	}

	@Test
	public void testInstrsChain() {
		MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);

		ASTInstr instr1 = Mockito.mock(ASTInstr.class);
		ASTInstr instr2 = Mockito.mock(ASTInstr.class);
		ASTInstrsNil nil = new ASTInstrsNil(0, 0);
		ASTInstrsChain innerChain = new ASTInstrsChain(instr2, nil, 0, 0);
		ASTInstrsChain outerChain = new ASTInstrsChain(instr2, innerChain, 0, 0);

		Mockito.doReturn(1).when(spyCompiler).compile(instr1);
		Mockito.doReturn(1).when(spyCompiler).compile(instr2);
		Mockito.doReturn(1).when(spyCompiler).compile(innerChain);
		Mockito.doReturn(0).when(spyCompiler).compile(nil);

		int nbInstr = spyCompiler.compile(outerChain);

		Assert.assertEquals(2, nbInstr);
	}

	@Test
	public void testDeclsChain() {
		MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);

		ASTDecl decl1 = Mockito.mock(ASTDecl.class);
		ASTDecl decl2 = Mockito.mock(ASTDecl.class);
		ASTDeclsNil nil = new ASTDeclsNil(0, 0);
		ASTDeclsChain innerChain = new ASTDeclsChain(decl2, nil, 0, 0);
		ASTDeclsChain outerChain = new ASTDeclsChain(decl2, innerChain, 0, 0);

		Mockito.doReturn(1).when(spyCompiler).compile(decl1);
		Mockito.doReturn(1).when(spyCompiler).compile(decl2);
		Mockito.doReturn(1).when(spyCompiler).compile(innerChain);
		Mockito.doReturn(0).when(spyCompiler).compile(nil);

		int nbInstr = spyCompiler.compile(outerChain);

		Assert.assertEquals(2, nbInstr);
	}

	@Test
	public void testCompileAssignmentEQ() {
		MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);
		ASTAssignmentOp assignmentOp = Mockito.mock(ASTAssignmentOp.class);
		ASTIdent ident = new ASTIdent("x", 0, 0);
		ASTExpr expr = Mockito.mock(ASTExpr.class);
		AssignmentOp op = AssignmentOp.EQ;

		Mockito.when(assignmentOp.getLValue()).thenReturn(ident);
		Mockito.when(assignmentOp.getOp()).thenReturn(op);
		Mockito.when(assignmentOp.getExpr()).thenReturn(expr);
		Mockito.doReturn(1).when(spyCompiler).compile(expr);

		int nbInstr = spyCompiler.compile(assignmentOp);

		Assert.assertEquals(2, nbInstr);
		Assert.assertEquals("store(x)\n", sb.toString());
	}

	@Test
	public void testCompileAssignmentADD() {
		MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);
		ASTAssignmentOp assignmentOp = Mockito.mock(ASTAssignmentOp.class);
		ASTIdent ident = new ASTIdent("x", 0, 0);
		ASTExpr expr = Mockito.mock(ASTExpr.class);
		AssignmentOp op = AssignmentOp.ADD;

		Mockito.when(assignmentOp.getLValue()).thenReturn(ident);
		Mockito.when(assignmentOp.getOp()).thenReturn(op);
		Mockito.when(assignmentOp.getExpr()).thenReturn(expr);
		Mockito.doReturn(1).when(spyCompiler).compile(expr);

		int nbInstr = spyCompiler.compile(assignmentOp);

		Assert.assertEquals(2, nbInstr);
		Assert.assertEquals("inc(x)\n", sb.toString());
	}

	@Test
	public void testCompileAssignmentArray() {
		MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);
		ASTAssignmentOp assignmentOp = Mockito.mock(ASTAssignmentOp.class);
		ASTIdent ident = new ASTIdent("x", 0, 0);
		ASTArrayAccess arrayAccess = Mockito.mock(ASTArrayAccess.class);
		ASTExpr expr1 = Mockito.mock(ASTExpr.class);
		ASTExpr expr2 = Mockito.mock(ASTExpr.class);
		AssignmentOp op = AssignmentOp.ADD;

		Mockito.when(assignmentOp.getLValue()).thenReturn(arrayAccess);
		Mockito.when(assignmentOp.getOp()).thenReturn(op);
		Mockito.when(assignmentOp.getExpr()).thenReturn(expr1);
		Mockito.when(arrayAccess.getExpr()).thenReturn(expr2);
		Mockito.when(arrayAccess.getIdent()).thenReturn(ident);

		Mockito.doReturn(1).when(spyCompiler).compile(expr1);
		Mockito.doReturn(1).when(spyCompiler).compile(expr2);

		int nbInstr = spyCompiler.compile(assignmentOp);

		Assert.assertEquals(3, nbInstr);
		Assert.assertEquals("ainc(x)\n", sb.toString());
	}

	@Test
	public void testCompileIncrementArray() {
		MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);
		ASTIncrementOp incrementOp = Mockito.mock(ASTIncrementOp.class);
		IncrementOp op = IncrementOp.INC;
		ASTIdent ident = new ASTIdent("x", 0, 0);
		ASTArrayAccess arrayAccess = Mockito.mock(ASTArrayAccess.class);
		ASTExpr expr = Mockito.mock(ASTExpr.class);

		Mockito.when(incrementOp.getLValue()).thenReturn(arrayAccess);
		Mockito.when(incrementOp.getOp()).thenReturn(op);
		Mockito.when(arrayAccess.getExpr()).thenReturn(expr);
		Mockito.when(arrayAccess.getIdent()).thenReturn(ident);
		Mockito.doReturn(1).when(spyCompiler).compile(expr);

		int nbInstr = spyCompiler.compile(incrementOp);

		Assert.assertEquals(3, nbInstr);
		Assert.assertEquals("push(1)\nainc(x)\n", sb.toString());
	}

	@Test
	public void testCompileIncrement() {
		ASTIncrementOp incrementOp = Mockito.mock(ASTIncrementOp.class);
		IncrementOp op = IncrementOp.INC;
		ASTIdent ident = new ASTIdent("x", 0, 0);

		Mockito.when(incrementOp.getLValue()).thenReturn(ident);
		Mockito.when(incrementOp.getOp()).thenReturn(op);

		int nbInstr = miniJajaCompiler.compile(incrementOp);

		Assert.assertEquals(2, nbInstr);
		Assert.assertEquals("push(1)\ninc(x)\n", sb.toString());
	}

	@Test
	public void testCompileIfWithElse() {
		ASTIdent ident = new ASTIdent("x", 0, 0);
		ASTIntLiteral literal = new ASTIntLiteral(1, 0, 0);
		ASTAssignmentOp assign = new ASTAssignmentOp(ident, literal, AssignmentOp.EQ, 0, 0);
		ASTInstrs thenInstrs = new ASTInstrsChain(assign, new ASTInstrsNil(0, 0), 0, 0);
		ASTInstrs elseInstrs = new ASTInstrsChain(assign, new ASTInstrsNil(0, 0), 0, 0);
		ASTBoolLiteral cond = new ASTBoolLiteral(true, 0, 0);
		ASTIf ifNode = new ASTIf(cond, thenInstrs, elseInstrs, 0, 0);

		int nbInstr = miniJajaCompiler.compile(ifNode);

		Assert.assertEquals(7, nbInstr);
		Assert.assertEquals("push(true)\nif(6)\npush(1)\nstore(x)\ngoto(8)\npush(1)\nstore(x)\n", sb.toString());
	}

	@Test
	public void testCompileIfWithoutElse() {
		ASTIdent ident = new ASTIdent("x", 0, 0);
		ASTIntLiteral literal = new ASTIntLiteral(1, 0, 0);
		ASTAssignmentOp assign = new ASTAssignmentOp(ident, literal, AssignmentOp.EQ, 0, 0);
		ASTInstrs thenInstrs = new ASTInstrsChain(assign, new ASTInstrsNil(0, 0), 0, 0);
		ASTInstrsNil elseInstrs = new ASTInstrsNil(0, 0);
		ASTBoolLiteral cond = new ASTBoolLiteral(true, 0, 0);
		ASTIf ifNode = new ASTIf(cond, thenInstrs, elseInstrs, 0, 0);

		int nbInstr = miniJajaCompiler.compile(ifNode);

		Assert.assertEquals(5, nbInstr);
		Assert.assertEquals("push(true)\nif(4)\ngoto(6)\npush(1)\nstore(x)\n", sb.toString());
	}

	@Test
	public void testCompileWhile() {
		ASTIdent ident = new ASTIdent("x", 0, 0);
		ASTIntLiteral literal = new ASTIntLiteral(1, 0, 0);
		ASTAssignmentOp assign = new ASTAssignmentOp(ident, literal, AssignmentOp.EQ, 0, 0);
		ASTInstrs body = new ASTInstrsChain(assign, new ASTInstrsNil(0, 0), 0, 0);
		ASTBoolLiteral cond = new ASTBoolLiteral(true, 0, 0);
		ASTWhile whileNode = new ASTWhile(cond, body, 0, 0);

		int nbInstr = miniJajaCompiler.compile(whileNode);

		Assert.assertEquals(6, nbInstr);
		Assert.assertEquals("push(true)\nnot\nif(7)\npush(1)\nstore(x)\ngoto(1)\n", sb.toString());
	}

	@Test
	public void testCompileMethodCallInstr() {
		MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);
		ASTMethodCallInstr methodCall = Mockito.mock(ASTMethodCallInstr.class);
		ASTIdent ident = new ASTIdent("myMethod", 0, 0);
		ASTExprs exprs = Mockito.mock(ASTExprs.class);

		Mockito.when(methodCall.getIdent()).thenReturn(ident);
		Mockito.when(methodCall.getExprs()).thenReturn(exprs);
		Mockito.doReturn(2).when(spyCompiler).compile(exprs);
		Mockito.doReturn(2).when(spyCompiler).free(exprs);

		int nbInstr = spyCompiler.compile(methodCall);

		Assert.assertEquals(6, nbInstr);
		Assert.assertEquals("invoke(myMethod)\npop\n", sb.toString());
	}

	@Test
	public void testCompileMethodCallExpr() {
		MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);
		ASTMethodCallExpr methodCall = Mockito.mock(ASTMethodCallExpr.class);
		ASTIdent ident = new ASTIdent("myMethod", 0, 0);
		ASTExprs exprs = Mockito.mock(ASTExprs.class);

		Mockito.when(methodCall.getIdent()).thenReturn(ident);
		Mockito.when(methodCall.getExprs()).thenReturn(exprs);
		Mockito.doReturn(2).when(spyCompiler).compile(exprs);
		Mockito.doReturn(2).when(spyCompiler).free(exprs);

		int nbInstr = spyCompiler.compile(methodCall);

		Assert.assertEquals(5, nbInstr);
		Assert.assertEquals("invoke(myMethod)\n", sb.toString());
	}

	@Test
	public void testCompileWrite() {
		MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);
		ASTWrite writeNode = Mockito.mock(ASTWrite.class);
		ASTStringLiteral astStringLiteral = Mockito.mock(ASTStringLiteral.class);

		Mockito.when(writeNode.getValue()).thenReturn(astStringLiteral);
		Mockito.when(writeNode.getIsLn()).thenReturn(false);
		Mockito.doReturn(1).when(spyCompiler).compile(astStringLiteral);

		int nbInstr = spyCompiler.compile(writeNode);

		Assert.assertEquals(2, nbInstr);
		Assert.assertEquals("write\n", sb.toString());
	}

	@Test
	public void testCompileWriteLn() {
		MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);
		ASTWrite writeNode = Mockito.mock(ASTWrite.class);
		ASTStringLiteral astStringLiteral = Mockito.mock(ASTStringLiteral.class);

		Mockito.when(writeNode.getValue()).thenReturn(astStringLiteral);
		Mockito.when(writeNode.getIsLn()).thenReturn(true);
		Mockito.doReturn(1).when(spyCompiler).compile(astStringLiteral);

		int nbInstr = spyCompiler.compile(writeNode);

		Assert.assertEquals(2, nbInstr);
		Assert.assertEquals("writeln\n", sb.toString());
	}

	@Test
	public void testCompileReturn() {
		MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);
		ASTReturn returnNode = Mockito.mock(ASTReturn.class);
		ASTExpr expr = Mockito.mock(ASTExpr.class);

		Mockito.when(returnNode.getExpr()).thenReturn(expr);
		Mockito.doReturn(1).when(spyCompiler).compile(expr);

		int nbInstr = spyCompiler.compile(returnNode);

		Assert.assertEquals(1, nbInstr);
	}

	@Test
	public void testCompileLength() {
		ASTLength lengthNode = Mockito.mock(ASTLength.class);
		ASTIdent ident = new ASTIdent("myArray", 0, 0);
		Mockito.when(lengthNode.getIdent()).thenReturn(ident);

		int nbInstr = miniJajaCompiler.compile(lengthNode);

		Assert.assertEquals(2, nbInstr);
		Assert.assertEquals("load(myArray)\nlength\n", sb.toString());
	}

	@Test
	public void testCompileDeclsNil() {
		ASTDeclsNil declsNil = new ASTDeclsNil(0, 0);
		int nbInstr = miniJajaCompiler.compile(declsNil);
		Assert.assertEquals(0, nbInstr);
		Assert.assertTrue(sb.toString().isEmpty());
	}

	@Test
	public void testCompileInstrsNil() {
		ASTInstrsNil instrsNil = new ASTInstrsNil(0, 0);
		int nbInstr = miniJajaCompiler.compile(instrsNil);
		Assert.assertEquals(0, nbInstr);
		Assert.assertTrue(sb.toString().isEmpty());
	}

	@Test
	public void testFreeVExpr() {
		MiniJajaCompiler spy = Mockito.spy(miniJajaCompiler);
		ASTExpr innerExpr = Mockito.mock(ASTExpr.class);
		ASTVExpr vexpr = new ASTVExpr(innerExpr, 0, 0);

		Mockito.doReturn(2).when(spy).free(innerExpr);

		int nbInstr = spy.free(vexpr);

		Assert.assertEquals(2, nbInstr);
		Assert.assertEquals("swap\npop\n", sb.toString());
	}

	@Test
	public void testFreeExprsNil() {
		ASTExprsNil nil = new ASTExprsNil(0, 0);

		int nbInstr = miniJajaCompiler.free(nil);

		Assert.assertEquals(0, nbInstr);
		Assert.assertTrue(sb.toString().isEmpty());
	}

	@Test
	public void testFreeExprsChain() {
		MiniJajaCompiler spy = Mockito.spy(miniJajaCompiler);
		ASTExpr expr1 = Mockito.mock(ASTExpr.class);
		ASTExpr expr2 = Mockito.mock(ASTExpr.class);
		ASTExprsNil nil = new ASTExprsNil(0, 0);
		ASTExprsChain chain2 = new ASTExprsChain(expr2, nil, 0, 0);
		ASTExprsChain chain1 = new ASTExprsChain(expr1, chain2, 0, 0);

		Mockito.doReturn(2).when(spy).free(expr1);
		Mockito.doReturn(2).when(spy).free(expr2);
		Mockito.doReturn(0).when(spy).free(nil);

		int nbInstr = spy.free(chain1);

		Assert.assertEquals(4, nbInstr);
	}


    @Test
    public void testCompileSingleParam() {
        ASTParam param = Mockito.mock(ASTParam.class);
        ASTIdent ident = new ASTIdent("x", 0, 0);
        ASTIntType type = new ASTIntType(0, 0);

        Mockito.when(param.getIdent()).thenReturn(ident);
        Mockito.when(param.getType()).thenReturn(type);

        int nbInstr = miniJajaCompiler.compileParam(param, 1);

        Assert.assertEquals(1, nbInstr);
        Assert.assertEquals("new(x,int,var,1)\n", sb.toString());
    }

    @Test
    public void testCompileParamsChain() {
        // Create 2 parameters
        ASTParam param1 = Mockito.mock(ASTParam.class);
        ASTParam param2 = Mockito.mock(ASTParam.class);
        ASTIdent ident1 = new ASTIdent("a", 0, 0);
        ASTIdent ident2 = new ASTIdent("b", 0, 0);
        ASTIntType type1 = new ASTIntType(0, 0);
        ASTIntType type2 = new ASTIntType(0, 0);

        Mockito.when(param1.getIdent()).thenReturn(ident1);
        Mockito.when(param1.getType()).thenReturn(type1);
        Mockito.when(param2.getIdent()).thenReturn(ident2);
        Mockito.when(param2.getType()).thenReturn(type2);

        ASTParamsNil nil = new ASTParamsNil(0, 0);
        ASTParamsChain chain = new ASTParamsChain(param1, new ASTParamsChain(param2, nil, 0, 0), 0, 0);

        int nbInstr = miniJajaCompiler.compileParams(chain);

        Assert.assertEquals(2, nbInstr);
        Assert.assertEquals("new(a,int,var,2)\nnew(b,int,var,1)\n", sb.toString());
    }

    @Test
    public void testCompileParamsNil() {
        ASTParamsNil nil = new ASTParamsNil(0, 0);
        int nbInstr = miniJajaCompiler.compileParamsNil(nil);

        Assert.assertEquals(0, nbInstr);
    }

	@Ignore
	public void testFullFiles() throws Exception {
		String[] files = {"hello_world", "0", "1", "synonymie"};
		for(String file: files) {
			ASTClass ast = (ASTClass) ParserUtils.parseFile("src/test/resources/samples/" + file + ".mjj");

			StringBuilder sb = new StringBuilder();
			MiniJajaCompiler compiler = new MiniJajaCompiler(sb);
			compiler.compile(ast);

			String expected = Files.readString(Paths.get("src/test/resources/expected/" + file + ".jjc"));
			Assert.assertEquals(expected.trim(), sb.toString().trim());
		}
	}
}
