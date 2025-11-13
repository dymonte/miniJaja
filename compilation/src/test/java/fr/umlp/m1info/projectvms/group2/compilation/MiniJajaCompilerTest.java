package fr.umlp.m1info.projectvms.group2.compilation;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

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
    public void testCompileIdent() {
        ASTIdent ident = new ASTIdent("a", 0, 0);
        int nbInstr =  miniJajaCompiler.compileIdent(ident);
        Assert.assertEquals("load(a)\n", outputStream.toString());
        Assert.assertEquals(1, nbInstr);
    }

    @Test
    public void testCompileStringLiteral() {
        ASTStringLiteral stringLiteral = new ASTStringLiteral("text", 0, 0);
        int nbInstr =  miniJajaCompiler.compileStringLiteral(stringLiteral);
        Assert.assertEquals("push(text)\n", outputStream.toString());
        Assert.assertEquals(1, nbInstr);
    }

    @Test
    public void testCompileIntLiteral() {
        ASTIntLiteral intLiteral = new ASTIntLiteral(3, 0, 0);
        int nbInstr =  miniJajaCompiler.compileIntLiteral(intLiteral);
        Assert.assertEquals("push(3)\n", outputStream.toString());
        Assert.assertEquals(1, nbInstr);
    }

    @Test
    public void testCompileBoolLiteral() {
        ASTBoolLiteral trueBoolLiteral = new ASTBoolLiteral(true, 0, 0);
        ASTBoolLiteral falseBoolLiteral = new ASTBoolLiteral(false, 0, 0);
        int nbInstr =  miniJajaCompiler.compileBoolLiteral(trueBoolLiteral);

        Assert.assertEquals("push(true)\n", outputStream.toString());
        Assert.assertEquals(1, nbInstr);

        nbInstr = miniJajaCompiler.compileBoolLiteral(falseBoolLiteral);
        Assert.assertEquals("push(true)\npush(false)\n", outputStream.toString());
        Assert.assertEquals(1, nbInstr);
    }

    @Test
    public void testCompileArrayAccess() {
        ASTIdent ident = new ASTIdent("t", 0, 0);
        ASTIntLiteral intLiteral = new ASTIntLiteral(2, 0,0);
        ASTArrayAccess arrayAccess = new ASTArrayAccess(ident, intLiteral, 0,0);

        MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);
        Mockito.doReturn(1).when(spyCompiler).compile(intLiteral);

        int nbInstr = spyCompiler.compileArrayAccess(arrayAccess);
        Assert.assertEquals(2, nbInstr);
        Assert.assertEquals("aload(t)\n", outputStream.toString());
    }

    @Test
    public void testCompileUnaryOpNegation() {
        ASTIntLiteral intLiteral = new ASTIntLiteral(5, 0, 0);
        ASTUnaryOp unaryOp = new ASTUnaryOp(intLiteral, UnaryOp.NEG, 0, 0);

        MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);
        Mockito.doReturn(1).when(spyCompiler).compile(intLiteral);

        int nbInstr = spyCompiler.compileUnaryOp(unaryOp);
        Assert.assertEquals(2, nbInstr);
        Assert.assertEquals("neg\n", outputStream.toString());
    }

    @Test
    public void testCompileUnaryOpNot() {
        ASTIntLiteral intLiteral = new ASTIntLiteral(1, 0, 0);
        ASTUnaryOp unaryOp = new ASTUnaryOp(intLiteral, UnaryOp.NOT, 0, 0);

        MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);
        Mockito.doReturn(1).when(spyCompiler).compile(intLiteral);

        int nbInstr = spyCompiler.compileUnaryOp(unaryOp);
        Assert.assertEquals(2, nbInstr);
        Assert.assertEquals("not\n", outputStream.toString());
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
        Assert.assertEquals(3, nbInstr);
        Assert.assertEquals("add\n", outputStream.toString());
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
        Assert.assertEquals(3, nbInstr);
        Assert.assertEquals("sub\n", outputStream.toString());
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
        Assert.assertEquals(3, nbInstr);
        Assert.assertEquals("mul\n", outputStream.toString());
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
        Assert.assertEquals(3, nbInstr);
        Assert.assertEquals("div\n", outputStream.toString());
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
        Assert.assertEquals(3, nbInstr);
        Assert.assertEquals("and\n", outputStream.toString());
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
        Assert.assertEquals(3, nbInstr);
        Assert.assertEquals("or\n", outputStream.toString());
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
        Assert.assertEquals(3, nbInstr);
        Assert.assertEquals("cmp\n", outputStream.toString());
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
        Assert.assertEquals(3, nbInstr);
        Assert.assertEquals("sup\n", outputStream.toString());
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
        Assert.assertEquals(2, nbInstr);
    }

    @Test
    public void testCompileExprsNil() {
        int nbInstr = miniJajaCompiler.compileExprsNil(new ASTExprsNil(0,0));
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

        Mockito.doReturn(1).when(spyCompiler).compile(Mockito.any(ASTVar.class));
        Mockito.doReturn(1).when(spyCompiler).compile(Mockito.any(ASTVarsChain.class));
        Mockito.doReturn(0).when(spyCompiler).compile(nil);

        int nbInstr = spyCompiler.compileVarsChain(outerChain);

        Mockito.verify(spyCompiler).compile(var1);
        Mockito.verify(spyCompiler).compile(innerChain);
        Assert.assertEquals(2, nbInstr);
    }

    @Test
    public void testCompileVarsNil() {
        MiniJajaCompiler spyCompiler = Mockito.spy(miniJajaCompiler);
        ASTVarsNil varsNil = new ASTVarsNil(0, 0);
        int nbInstr = spyCompiler.compileVarsNil(varsNil);
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

        int nbInstr = spyCompiler.compileVarSimple(varSimple);

        Assert.assertEquals("new(x, int, var, 0)\n", outputStream.toString());
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

        int nbInstr = spyCompiler.compileVarArray(varArray);

        Assert.assertEquals("new(arr, int, cst, 0)\n", outputStream.toString());
        Assert.assertEquals(2, nbInstr);
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

        Assert.assertEquals("newarray(C, bool)\n", outputStream.toString());
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

        Mockito.doReturn(1).when(spyCompiler).free(Mockito.any(ASTVar.class));
        Mockito.doReturn(1).when(spyCompiler).free(Mockito.any(ASTVarsChain.class));
        Mockito.doReturn(0).when(spyCompiler).free(nil);

        int nbInstr = spyCompiler.freeVarsChain(outerChain);

        Mockito.verify(spyCompiler).free(var1);
        Mockito.verify(spyCompiler).free(innerChain);
        Assert.assertEquals(2, nbInstr);
    }

    @Test
    public void testFreeVarsNil() {
        ASTVarsNil varsNil = Mockito.mock(ASTVarsNil.class);
        int nbInstr = miniJajaCompiler.freeVarsNil(varsNil);
        Assert.assertEquals(0, nbInstr);
    }

    @Test
    public void testFreeVar() {
        ASTVar var = Mockito.mock(ASTVar.class);
        int nbInstr = miniJajaCompiler.freeVar(var);
        Assert.assertEquals("pop\n", outputStream.toString());
        Assert.assertEquals(1, nbInstr);
    }
}
