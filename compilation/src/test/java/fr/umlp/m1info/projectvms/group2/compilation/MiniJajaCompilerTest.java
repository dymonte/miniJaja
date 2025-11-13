package fr.umlp.m1info.projectvms.group2.compilation;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.decl.ASTClass;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decl.ASTMainMethod;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decl.ASTMethod;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decls.ASTDecls;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instrs.ASTInstrs;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.params.ASTParams;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.type.ASTTypeMeth;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.vars.ASTVars;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
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
import fr.umlp.m1info.projectvms.group2.ast.minijaja.type.ASTBoolType;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.type.ASTIntType;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.var.ASTVar;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.var.ASTVarArray;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.var.ASTVarConst;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.var.ASTVarSimple;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.vars.ASTVarsChain;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.vars.ASTVarsNil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MiniJajaCompilerTest {
    MiniJajaCompiler miniJajaCompiler;
    ByteArrayOutputStream outputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);
        miniJajaCompiler = new MiniJajaCompiler(out);
    }
    @Test
    public void testCompileClasse() {
        ASTClass classNode = Mockito.mock(ASTClass.class);
        ASTDecls decls = Mockito.mock(ASTDecls.class);
        ASTMainMethod mainMethod = Mockito.mock(ASTMainMethod.class);
        Mockito.when(classNode.getDecls()).thenReturn(decls);
        Mockito.when(classNode.getMainMethod()).thenReturn(mainMethod);
        MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);
        Mockito.doReturn(3).when(spyCompiler).compile(decls);
        Mockito.doReturn(5).when(spyCompiler).compile(mainMethod);
        Mockito.doReturn(2).when(spyCompiler).free(decls);
        int nbInstr = spyCompiler.compileClass(classNode);
        assertEquals(3 + 5 + 2 + 3, nbInstr);
        assertEquals(13, nbInstr);
        String expected =
                "init\n" +
                        "pop\n" +
                        "jcstop\n";
        assertEquals(expected, outputStream.toString());
        Mockito.verify(spyCompiler).compile(decls);
        Mockito.verify(spyCompiler).compile(mainMethod);
        Mockito.verify(spyCompiler).free(decls);
    }

    @Test
    public void testCompileMainMethod() {
        ASTMainMethod mainMethod = Mockito.mock(ASTMainMethod.class);
        ASTVars vars = Mockito.mock(ASTVars.class);
        ASTInstrs instrs = Mockito.mock(ASTInstrs.class);
        Mockito.when(mainMethod.getVars()).thenReturn(vars);
        Mockito.when(mainMethod.getInstrs()).thenReturn(instrs);
        PrintStream mockOut = Mockito.mock(PrintStream.class);
        MiniJajaCompiler compiler = Mockito.spy(new MiniJajaCompiler(mockOut));
        Mockito.doReturn(4).when(compiler).compile(vars);
        Mockito.doReturn(6).when(compiler).compile(instrs);
        Mockito.doReturn(2).when(compiler).free(vars);
        int result = compiler.compileMainMethod(mainMethod);
        assertEquals(4 + 6 + 2 + 1, result);
        assertEquals(13, result);   // 4 + 6 + 1 + 2
        Mockito.verify(mockOut).println("push(0)");
        Mockito.verify(compiler).compile(vars);
        Mockito.verify(compiler).compile(instrs);
        Mockito.verify(compiler).free(vars);
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
        Mockito.doReturn(2).when(spyCompiler).compile(params);
        Mockito.doReturn(3).when(spyCompiler).compile(vars);
        Mockito.doReturn(4).when(spyCompiler).compile(instrs);
        Mockito.doReturn(1).when(spyCompiler).free(vars);
        int nbInstr = spyCompiler.compileMethod(method);
        int expected = 3 + 2 + 3 + 4 + 1 + 1 + 1 + 1 + 1;
        assertEquals(expected, nbInstr);
        String out = outputStream.toString();
        assertTrue(out.contains("jcnil"));
        assertTrue(out.contains("push(3)")); // start = 0 → push(0+3)
        assertTrue(out.contains("new(m, int, meth, 0)"));
        assertTrue(out.contains("goto("));
        assertTrue(out.contains("push(0)"));
        assertTrue(out.contains("swap"));
        assertTrue(out.contains("return"));
        Mockito.verify(spyCompiler).compile(params);
        Mockito.verify(spyCompiler).compile(vars);
        Mockito.verify(spyCompiler).compile(instrs);
        Mockito.verify(spyCompiler).free(vars);
    }
    @Test
    public void testCompileIdent() {
        ASTIdent ident = new ASTIdent("a", 0, 0);
        int nbInstr =  miniJajaCompiler.compileIdent(ident);
        assertEquals("load(a)\n", outputStream.toString());
        assertEquals(1, nbInstr);
    }

    @Test
    public void testCompileStringLiteral() {
        ASTStringLiteral stringLiteral = new ASTStringLiteral("text", 0, 0);
        int nbInstr =  miniJajaCompiler.compileStringLiteral(stringLiteral);
        assertEquals("push(text)\n", outputStream.toString());
        assertEquals(1, nbInstr);
    }

    @Test
    public void testCompileIntLiteral() {
        ASTIntLiteral intLiteral = new ASTIntLiteral(3, 0, 0);
        int nbInstr =  miniJajaCompiler.compileIntLiteral(intLiteral);
        assertEquals("push(3)\n", outputStream.toString());
        assertEquals(1, nbInstr);
    }

    @Test
    public void testCompileBoolLiteral() {
        ASTBoolLiteral trueBoolLiteral = new ASTBoolLiteral(true, 0, 0);
        ASTBoolLiteral falseBoolLiteral = new ASTBoolLiteral(false, 0, 0);
        int nbInstr =  miniJajaCompiler.compileBoolLiteral(trueBoolLiteral);

        assertEquals("push(true)\n", outputStream.toString());
        assertEquals(1, nbInstr);

        nbInstr = miniJajaCompiler.compileBoolLiteral(falseBoolLiteral);
        assertEquals("push(true)\npush(false)\n", outputStream.toString());
        assertEquals(1, nbInstr);
    }

    @Test
    public void testCompileArrayAccess() {
        ASTIdent ident = new ASTIdent("t", 0, 0);
        ASTIntLiteral intLiteral = new ASTIntLiteral(2, 0,0);
        ASTArrayAccess arrayAccess = new ASTArrayAccess(ident, intLiteral, 0,0);

        MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);
        Mockito.doReturn(1).when(spyCompiler).compile(intLiteral);

        int nbInstr = spyCompiler.compileArrayAccess(arrayAccess);
        assertEquals(2, nbInstr);
        assertEquals("aload(t)\n", outputStream.toString());
    }

    @Test
    public void testCompileUnaryOpNegation() {
        ASTIntLiteral intLiteral = new ASTIntLiteral(5, 0, 0);
        ASTUnaryOp unaryOp = new ASTUnaryOp(intLiteral, UnaryOp.NEG, 0, 0);

        MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);
        Mockito.doReturn(1).when(spyCompiler).compile(intLiteral);

        int nbInstr = spyCompiler.compileUnaryOp(unaryOp);
        assertEquals(2, nbInstr);
        assertEquals("neg\n", outputStream.toString());
    }

    @Test
    public void testCompileUnaryOpNot() {
        ASTIntLiteral intLiteral = new ASTIntLiteral(1, 0, 0);
        ASTUnaryOp unaryOp = new ASTUnaryOp(intLiteral, UnaryOp.NOT, 0, 0);

        MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);
        Mockito.doReturn(1).when(spyCompiler).compile(intLiteral);

        int nbInstr = spyCompiler.compileUnaryOp(unaryOp);
        assertEquals(2, nbInstr);
        assertEquals("not\n", outputStream.toString());
    }

    @Test
    public void testCompileBinaryOpAdd() {
        ASTIntLiteral left = new ASTIntLiteral(1, 0, 0);
        ASTIntLiteral right = new ASTIntLiteral(2, 0, 0);
        ASTBinaryOp binaryOp = new ASTBinaryOp(left, right, BinaryOp.ADD, 0, 0);

        MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);
        Mockito.doReturn(1).when(spyCompiler).compile(left);
        Mockito.doReturn(1).when(spyCompiler).compile(right);

        int nbInstr = spyCompiler.compileBinaryOp(binaryOp);
        assertEquals(3, nbInstr);
        assertEquals("add\n", outputStream.toString());
    }

    @Test
    public void testCompileBinaryOpSub() {
        ASTIntLiteral left = new ASTIntLiteral(5, 0, 0);
        ASTIntLiteral right = new ASTIntLiteral(3, 0, 0);
        ASTBinaryOp binaryOp = new ASTBinaryOp(left, right, BinaryOp.SUB, 0, 0);

        MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);
        Mockito.doReturn(1).when(spyCompiler).compile(left);
        Mockito.doReturn(1).when(spyCompiler).compile(right);

        int nbInstr = spyCompiler.compileBinaryOp(binaryOp);
        assertEquals(3, nbInstr);
        assertEquals("sub\n", outputStream.toString());
    }

    @Test
    public void testCompileBinaryOpMul() {
        ASTIntLiteral left = new ASTIntLiteral(4, 0, 0);
        ASTIntLiteral right = new ASTIntLiteral(7, 0, 0);
        ASTBinaryOp binaryOp = new ASTBinaryOp(left, right, BinaryOp.MUL, 0, 0);

        MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);
        Mockito.doReturn(1).when(spyCompiler).compile(left);
        Mockito.doReturn(1).when(spyCompiler).compile(right);

        int nbInstr = spyCompiler.compileBinaryOp(binaryOp);
        assertEquals(3, nbInstr);
        assertEquals("mul\n", outputStream.toString());
    }

    @Test
    public void testCompileBinaryOpDiv() {
        ASTIntLiteral left = new ASTIntLiteral(8, 0, 0);
        ASTIntLiteral right = new ASTIntLiteral(2, 0, 0);
        ASTBinaryOp binaryOp = new ASTBinaryOp(left, right, BinaryOp.DIV, 0, 0);

        MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);
        Mockito.doReturn(1).when(spyCompiler).compile(left);
        Mockito.doReturn(1).when(spyCompiler).compile(right);

        int nbInstr = spyCompiler.compileBinaryOp(binaryOp);
        assertEquals(3, nbInstr);
        assertEquals("div\n", outputStream.toString());
    }

    @Test
    public void testCompileBinaryOpAnd() {
        ASTIntLiteral left = new ASTIntLiteral(1, 0, 0);
        ASTIntLiteral right = new ASTIntLiteral(0, 0, 0);
        ASTBinaryOp binaryOp = new ASTBinaryOp(left, right, BinaryOp.AND, 0, 0);

        MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);
        Mockito.doReturn(1).when(spyCompiler).compile(left);
        Mockito.doReturn(1).when(spyCompiler).compile(right);

        int nbInstr = spyCompiler.compileBinaryOp(binaryOp);
        assertEquals(3, nbInstr);
        assertEquals("and\n", outputStream.toString());
    }

    @Test
    public void testCompileBinaryOpOr() {
        ASTIntLiteral left = new ASTIntLiteral(0, 0, 0);
        ASTIntLiteral right = new ASTIntLiteral(1, 0, 0);
        ASTBinaryOp binaryOp = new ASTBinaryOp(left, right, BinaryOp.OR, 0, 0);

        MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);
        Mockito.doReturn(1).when(spyCompiler).compile(left);
        Mockito.doReturn(1).when(spyCompiler).compile(right);

        int nbInstr = spyCompiler.compileBinaryOp(binaryOp);
        assertEquals(3, nbInstr);
        assertEquals("or\n", outputStream.toString());
    }

    @Test
    public void testCompileBinaryOpEqual() {
        ASTIntLiteral left = new ASTIntLiteral(2, 0, 0);
        ASTIntLiteral right = new ASTIntLiteral(2, 0, 0);
        ASTBinaryOp binaryOp = new ASTBinaryOp(left, right, BinaryOp.EQ, 0, 0);

        MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);
        Mockito.doReturn(1).when(spyCompiler).compile(left);
        Mockito.doReturn(1).when(spyCompiler).compile(right);

        int nbInstr = spyCompiler.compileBinaryOp(binaryOp);
        assertEquals(3, nbInstr);
        assertEquals("cmp\n", outputStream.toString());
    }

    @Test
    public void testCompileBinaryOpGreater() {
        ASTIntLiteral left = new ASTIntLiteral(5, 0, 0);
        ASTIntLiteral right = new ASTIntLiteral(3, 0, 0);
        ASTBinaryOp binaryOp = new ASTBinaryOp(left, right, BinaryOp.GT, 0, 0);

        MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);
        Mockito.doReturn(1).when(spyCompiler).compile(left);
        Mockito.doReturn(1).when(spyCompiler).compile(right);

        int nbInstr = spyCompiler.compileBinaryOp(binaryOp);
        assertEquals(3, nbInstr);
        assertEquals("sup\n", outputStream.toString());
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
        int nbInstr = spyCompiler.compileExprsChain(outerChain);

        Mockito.verify(spyCompiler).compile(expr1);
        Mockito.verify(spyCompiler).compile(expr2);
        Mockito.verify(spyCompiler, Mockito.times(2)).compileExprsChain(Mockito.any(ASTExprsChain.class));
        Mockito.verify(spyCompiler).compileExprsNil(nil);
        assertEquals(2, nbInstr);
    }

    @Test
    public void testCompileExprsNil() {
        int nbInstr = miniJajaCompiler.compileExprsNil(new ASTExprsNil(0,0));
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

        Mockito.doReturn(1).when(spyCompiler).compile(Mockito.any(ASTVar.class));
        Mockito.doReturn(1).when(spyCompiler).compile(Mockito.any(ASTVarsChain.class));
        Mockito.doReturn(0).when(spyCompiler).compile(nil);

        int nbInstr = spyCompiler.compileVarsChain(outerChain);

        Mockito.verify(spyCompiler).compile(var1);
        Mockito.verify(spyCompiler).compile(innerChain);
        assertEquals(2, nbInstr);
    }

    @Test
    public void testCompileVarsNil() {
        MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);
        ASTVarsNil varsNil = new ASTVarsNil(0, 0);
        int nbInstr = spyCompiler.compileVarsNil(varsNil);
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

        int nbInstr = spyCompiler.compileVarSimple(varSimple);

        assertEquals("new(x, int, var, 0)\n", outputStream.toString());
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

        int nbInstr = spyCompiler.compileVarArray(varArray);

        assertEquals("new(arr, int, cst, 0)\n", outputStream.toString());
        assertEquals(2, nbInstr);
    }

    @Ignore
    @Test
    public void testCompileVarConst() {
        MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);

        ASTVarConst varConst = Mockito.mock(ASTVarConst.class);
        ASTIdent ident = Mockito.mock(ASTIdent.class);
        ASTBoolType type = new ASTBoolType(0, 0);
        ASTVExpr vexpr = Mockito.mock(ASTVExpr.class);

        Mockito.when(varConst.getIdent()).thenReturn(ident);
        Mockito.when(varConst.getType()).thenReturn(type);
        Mockito.when(varConst.getVExpr()).thenReturn(vexpr);
        Mockito.when(ident.getName()).thenReturn("C");
        Mockito.doReturn(1).when(spyCompiler).compile(vexpr);

        int nbInstr = spyCompiler.compileVarConst(varConst);

        assertEquals("newarray(C, bool)\n", outputStream.toString());
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

        Mockito.doReturn(1).when(spyCompiler).free(Mockito.any(ASTVar.class));
        Mockito.doReturn(1).when(spyCompiler).free(Mockito.any(ASTVarsChain.class));
        Mockito.doReturn(0).when(spyCompiler).free(nil);

        int nbInstr = spyCompiler.freeVarsChain(outerChain);

        Mockito.verify(spyCompiler).free(var1);
        Mockito.verify(spyCompiler).free(innerChain);
        assertEquals(2, nbInstr);
    }

    @Test
    public void testFreeVarsNil() {
        ASTVarsNil varsNil = Mockito.mock(ASTVarsNil.class);
        int nbInstr = miniJajaCompiler.freeVarsNil(varsNil);
        assertEquals(0, nbInstr);
    }

    @Test
    public void testFreeVar() {
        ASTVar var = Mockito.mock(ASTVar.class);
        int nbInstr = miniJajaCompiler.freeVar(var);
        assertEquals("pop\n", outputStream.toString());
        assertEquals(1, nbInstr);
    }
}
