package fr.umlp.m1info.projectvms.group2.ast.jajacode;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.instr.*;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.instr.oper.*;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.utils.Kind;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.utils.Type;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.value.*;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.var.VarKind;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.var.VarType;
import org.junit.Assert;
import org.junit.Test;

public class JajaCodeNodeGetterTest {
    @Test
    public void testJajaCodeNodeGetterLineId() {
        int lineId = 1;
        JajaCodeNode node = new InstrNil(lineId);
        Assert.assertEquals(lineId, node.getLineId());
    }

    @Test
    public void testVarKindGetter() {
        int lineId = 1;
        VarKind varKind = new VarKind(lineId, Kind.VAR);
        Assert.assertEquals(Kind.VAR, varKind.getKind());
    }

    @Test
    public void testVarTypeGetter() {
        int lineId = 1;
        VarType varType = new VarType(lineId, Type.INT);
        Assert.assertEquals(Type.INT, varType.getType());
    }

    @Test
    public void testValToStringNbre() {
        int lineId = 1;
        ValNbre val = new ValNbre(lineId, 10);
        Assert.assertEquals(10, val.getValue());
    }

    @Test
    public void testValToStringString() {
        int lineId = 1;
        ValString val = new ValString(lineId, "Hello");
        Assert.assertEquals("Hello", val.getValue());
    }

    @Test
    public void testValGettersBool() {
        int lineId = 1;
        ValBool val = new ValBool(lineId++, true);
        Assert.assertTrue(val.getValue());

        val = new ValBool(lineId, false);
        Assert.assertFalse(val.getValue());
    }

    @Test
    public void testValGettersAddress() {
        int lineId = 1;
        ValAddress val = new ValAddress(lineId, 100);
        Assert.assertEquals(100, val.getAddress());
    }


    @Test
    public void testValGettersIdent() {
        int lineId = 1;
        ValIdent val = new ValIdent(lineId, "a");
        Assert.assertEquals("a", val.getIdent());
    }

    @Test
    public void testInstrGettersOperBinary() {
        int lineId = 1;
        InstrOperBinary instr = new InstrOperBinary(lineId++, JajaCodeBinaryOper.fromSymbol("add"));
        Assert.assertEquals(JajaCodeBinaryOper.ADD, instr.getOper());

        InstrOperBinary instr2 = new InstrOperBinary(lineId++, JajaCodeBinaryOper.fromSymbol("sub"));
        Assert.assertEquals(JajaCodeBinaryOper.SUB, instr2.getOper());

        InstrOperBinary instr3 = new InstrOperBinary(lineId++, JajaCodeBinaryOper.fromSymbol("mul"));
        Assert.assertEquals(JajaCodeBinaryOper.MUL, instr3.getOper());

        InstrOperBinary instr4 = new InstrOperBinary(lineId++, JajaCodeBinaryOper.fromSymbol("div"));
        Assert.assertEquals(JajaCodeBinaryOper.DIV, instr4.getOper());

        InstrOperBinary instr5 = new InstrOperBinary(lineId++, JajaCodeBinaryOper.fromSymbol("cmp"));
        Assert.assertEquals(JajaCodeBinaryOper.CMP, instr5.getOper());

        InstrOperBinary instr6 = new InstrOperBinary(lineId++, JajaCodeBinaryOper.fromSymbol("sup"));
        Assert.assertEquals(JajaCodeBinaryOper.SUP, instr6.getOper());

        InstrOperBinary instr7 = new InstrOperBinary(lineId++, JajaCodeBinaryOper.fromSymbol("and"));
        Assert.assertEquals(JajaCodeBinaryOper.AND, instr7.getOper());

        InstrOperBinary instr8 = new InstrOperBinary(lineId, JajaCodeBinaryOper.fromSymbol("or"));
        Assert.assertEquals(JajaCodeBinaryOper.OR, instr8.getOper());


        IllegalArgumentException exception = Assert.assertThrows(IllegalArgumentException.class, () ->
                new InstrOperBinary(1, JajaCodeBinaryOper.fromSymbol("unknown")));

        Assert.assertEquals("Unknow operator: unknown", exception.getMessage());
    }

    @Test
    public void testInstrGettersOperUnary() {
        int lineId = 1;
        InstrOperUnary instr = new InstrOperUnary(lineId++, JajaCodeUnaryOper.fromSymbol("not"));
        Assert.assertEquals(JajaCodeUnaryOper.NOT, instr.getOper());

        InstrOperUnary instr2 = new InstrOperUnary(lineId, JajaCodeUnaryOper.fromSymbol("neg"));
        Assert.assertEquals(JajaCodeUnaryOper.NEG, instr2.getOper());

        IllegalArgumentException exception = Assert.assertThrows(IllegalArgumentException.class, () ->
                new InstrOperUnary(1, JajaCodeUnaryOper.fromSymbol("unknown")));

        Assert.assertEquals("Unknow operator: unknown", exception.getMessage());
    }

    @Test
    public void testInstrGettersAInc() {
        int lineId = 1;
        ValIdent valIdent = new ValIdent(lineId, "a");
        InstrAInc instr = new InstrAInc(lineId, valIdent);
        Assert.assertSame(valIdent, instr.getValIdent());
    }

    @Test
    public void testInstrGettersALoad() {
        int lineId = 1;
        ValIdent valIdent = new ValIdent(lineId, "a");
        InstrALoad instr = new InstrALoad(lineId, valIdent);
        Assert.assertSame(valIdent, instr.getValIdent());
    }

    @Test
    public void testInstrGettersAStore() {
        int lineId = 1;
        ValIdent valIdent = new ValIdent(lineId, "a");
        InstrAStore instr = new InstrAStore(lineId, valIdent);
        Assert.assertSame(valIdent, instr.getValIdent());
    }

    @Test
    public void testInstrGettersGoto() {
        int lineId = 1;
        ValAddress addr = new ValAddress(lineId, 100);
        InstrGoto instr = new InstrGoto(lineId, addr);
        Assert.assertSame(addr, instr.getValAddress());
    }

    @Test
    public void testInstrGettersIf() {
        int lineId = 1;
        ValAddress addr = new ValAddress(lineId, 100);
        InstrIf instr = new InstrIf(lineId, addr);
        Assert.assertSame(addr, instr.getValAddress());
    }

    @Test
    public void testInstrGettersInc() {
        int lineId = 1;
        ValIdent valIdent = new ValIdent(lineId, "a");
        InstrInc instr = new InstrInc(lineId, valIdent);
        Assert.assertSame(valIdent, instr.getValIdent());
    }

    @Test
    public void testInstrGettersInvoke() {
        int lineId = 1;
        ValIdent valIdent = new ValIdent(lineId, "a");
        InstrInvoke instr = new InstrInvoke(lineId, valIdent);
        Assert.assertSame(valIdent, instr.getValIdent());
    }

    @Test
    public void testInstrGettersLength() {
        int lineId = 1;
        ValIdent valIdent = new ValIdent(lineId, "l");
        InstrLength instr = new InstrLength(lineId, valIdent);
        Assert.assertSame(valIdent, instr.getValIdent());
    }

    @Test
    public void testInstrGettersLoad() {
        int lineId = 1;
        ValIdent valIdent = new ValIdent(lineId, "a");
        InstrLoad instr = new InstrLoad(lineId, valIdent);
        Assert.assertSame(valIdent, instr.getValIdent());
    }

    @Test
    public void testInstrGettersNew() {
        int lineId = 1;
        ValIdent valIdent = new ValIdent(lineId, "a");
        VarKind kind = new VarKind(lineId, Kind.VAR);
        VarType type = new VarType(lineId, Type.INT);
        ValAddress valAddress = new ValAddress(lineId, 10);

        InstrNew instr = new InstrNew(lineId, valIdent, kind, type, valAddress);

        Assert.assertSame(valIdent, instr.getValIdent());
        Assert.assertSame(kind, instr.getKind());
        Assert.assertSame(type, instr.getType());
        Assert.assertSame(valAddress, instr.getValAddress());
    }

    @Test
    public void testInstrGettersNewArray() {
        int lineId = 1;
        ValIdent valIdent = new ValIdent(lineId, "a");
        VarType type = new VarType(lineId, Type.INT);

        InstrNewArray instr = new InstrNewArray(lineId, valIdent, type);

        Assert.assertSame(valIdent, instr.getValIdent());
        Assert.assertSame(type, instr.getType());
    }

    @Test
    public void testInstrGettersPush() {
        int lineId = 1;
        ValNbre valNbre = new ValNbre(lineId, 10);
        InstrPush instr = new InstrPush(lineId, valNbre);
        Assert.assertSame(valNbre, instr.getValVal());
    }

    @Test
    public void testInstrGettersStore() {
        int lineId = 1;
        ValIdent valIdent = new ValIdent(lineId, "a");
        InstrStore instr = new InstrStore(lineId, valIdent);
        Assert.assertSame(valIdent, instr.getValIdent());
    }
}