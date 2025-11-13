package fr.umlp.m1info.projectvms.group2.ast.jajacode;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.instr.*;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.instr.oper.InstrOperBinary;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.instr.oper.InstrOperUnary;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.instr.oper.JajaCodeBinaryOper;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.instr.oper.JajaCodeUnaryOper;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.value.*;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.var.VarKind;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.var.VarType;
import fr.umlp.m1info.projectvms.group2.utils.Kind;
import fr.umlp.m1info.projectvms.group2.utils.Type;

import org.junit.Assert;
import org.junit.Test;

public class JajaCodeNodeToStringTest {
	@Test
	public void testValToStringAddress() {
		int lineId = 1;
		ValAddress val = new ValAddress(lineId, 100);
		Assert.assertEquals("jcnbre(100)", val.toString());
	}

	@Test
	public void testValToStringIdent() {
		int lineId = 1;
		ValIdent val = new ValIdent(lineId, "a");
		Assert.assertEquals("jcident(a)", val.toString());
	}

	@Test
	public void testValToStringNbre() {
		int lineId = 1;
		ValNbre val = new ValNbre(lineId, 10);
		Assert.assertEquals("jcnbre(10)", val.toString());
	}

	@Test
	public void testValToStringBool() {
		int lineId = 1;
		ValBool val = new ValBool(lineId, true);
		Assert.assertEquals("jcvrai", val.toString());

		val = new ValBool(lineId, false);
		Assert.assertEquals("jcfaux", val.toString());
	}

	@Test
	public void testValToStringString() {
		int lineId = 1;
		ValString val = new ValString(lineId, "Hello");
		Assert.assertEquals("Hello", val.toString());
	}

	@Test
	public void testInstrToStringOperBinary() {
		int lineId = 1;
		InstrOperBinary instrADD = new InstrOperBinary(lineId++, JajaCodeBinaryOper.ADD);
		Assert.assertEquals("1 : add", instrADD.toString());

		InstrOperBinary instrSUB = new InstrOperBinary(lineId++, JajaCodeBinaryOper.SUB);
		Assert.assertEquals("2 : sub", instrSUB.toString());

		InstrOperBinary instrMUL = new InstrOperBinary(lineId++, JajaCodeBinaryOper.MUL);
		Assert.assertEquals("3 : mul", instrMUL.toString());

		InstrOperBinary instrDIV = new InstrOperBinary(lineId++, JajaCodeBinaryOper.DIV);
		Assert.assertEquals("4 : div", instrDIV.toString());

		InstrOperBinary instrAND = new InstrOperBinary(lineId++, JajaCodeBinaryOper.AND);
		Assert.assertEquals("5 : and", instrAND.toString());

		InstrOperBinary instrOR = new InstrOperBinary(lineId++, JajaCodeBinaryOper.OR);
		Assert.assertEquals("6 : or", instrOR.toString());
	}

	@Test
	public void testInstrToStringOperUnary() {
		InstrOperUnary instrNOT = new InstrOperUnary(1, JajaCodeUnaryOper.NOT);
		Assert.assertEquals("1 : not", instrNOT.toString());

		InstrOperUnary instrNEG = new InstrOperUnary(1, JajaCodeUnaryOper.NEG);
		Assert.assertEquals("1 : neg", instrNEG.toString());
	}

	@Test
	public void testInstrToStringAInc() {
		int lineId = 1;
		InstrAInc instr = new InstrAInc(lineId, new ValIdent(lineId, "a"));
		Assert.assertEquals("1 : ainc(jcident(a))", instr.toString());
	}

	@Test
	public void testInstrToStringALoad() {
		int lineId = 1;
		InstrALoad instr = new InstrALoad(lineId, new ValIdent(lineId, "a"));
		Assert.assertEquals("1 : aload(jcident(a))", instr.toString());
	}

	@Test
	public void testInstrToStringAStore() {
		int lineId = 1;
		InstrAStore instr = new InstrAStore(lineId, new ValIdent(lineId, "a"));
		Assert.assertEquals("1 : astore(jcident(a))", instr.toString());
	}

	@Test
	public void testInstrToStringGoto() {
		int lineId = 1;
		InstrGoto instr = new InstrGoto(lineId, new ValAddress(lineId, 100));
		Assert.assertEquals("1 : goto(jcnbre(100))", instr.toString());
	}

	@Test
	public void testInstrToStringIf() {
		int lineId = 1;
		InstrIf instr = new InstrIf(lineId, new ValAddress(lineId, 100));
		Assert.assertEquals("1 : if(jcnbre(100))", instr.toString());
	}

	@Test
	public void testInstrToStringInc() {
		int lineId = 1;
		InstrInc instr = new InstrInc(lineId, new ValIdent(lineId, "a"));
		Assert.assertEquals("1 : inc(jcident(a))", instr.toString());
	}

	@Test
	public void testInstrToStringInit() {
		int lineId = 1;
		InstrInit instr = new InstrInit(lineId);
		Assert.assertEquals("1 : init", instr.toString());
	}

	@Test
	public void testInstrToStringInvoke() {
		int lineId = 1;
		InstrInvoke instr = new InstrInvoke(lineId, new ValIdent(lineId, "a"));
		Assert.assertEquals("1 : invoke(jcident(a))", instr.toString());
	}

	@Test
	public void testInstrToStringLength() {
		int lineId = 1;
		InstrLength instr = new InstrLength(lineId, new ValIdent(lineId, "l"));
		Assert.assertEquals("1 : length(jcident(l))", instr.toString());
	}

	@Test
	public void testInstrToStringLoad() {
		int lineId = 1;
		InstrLoad instr = new InstrLoad(lineId, new ValIdent(lineId, "a"));
		Assert.assertEquals("1 : load(jcident(a))", instr.toString());
	}

	@Test
	public void testInstrToStringNew() {
		int lineId = 1;
		InstrNew instr = new InstrNew(1,
				new ValIdent(lineId, "a"),
				new VarKind(lineId, Kind.VAR),
				new VarType(lineId, Type.INT),
				new ValAddress(lineId, 10));
		Assert.assertEquals("1 : new(var, jcident(a), int, jcnbre(10))", instr.toString());

		InstrNew instr2 = new InstrNew(1,
				new ValIdent(lineId, "b"),
				new VarKind(lineId, Kind.VAR),
				new VarType(lineId, Type.BOOL),
				new ValAddress(lineId, 1));
		Assert.assertEquals("1 : new(var, jcident(b), bool, jcnbre(1))", instr2.toString());
	}

	@Test
	public void testInstrToStringNewArray() {
		int lineId = 1;
		InstrNewArray instr = new InstrNewArray(1,
				new ValIdent(lineId, "a"),
				new VarType(lineId, Type.INT));
		Assert.assertEquals("1 : newarray(jcident(a), int)", instr.toString());
	}

	@Test
	public void testInstrToStringNil() {
		int lineId = 1;
		InstrNil instr = new InstrNil(lineId);
		Assert.assertEquals("1 : jcnil", instr.toString());
	}

	@Test
	public void testInstrToStringNop() {
		int lineId = 1;
		InstrNop instr = new InstrNop(lineId);
		Assert.assertEquals("1 : nop", instr.toString());
	}

	@Test
	public void testInstrToStringPop() {
		int lineId = 1;
		InstrPop instr = new InstrPop(lineId);
		Assert.assertEquals("1 : pop", instr.toString());
	}

	@Test
	public void testInstrToStringPush() {
		int lineId = 1;
		InstrPush instr = new InstrPush(lineId, new ValNbre(lineId, 10));
		Assert.assertEquals("1 : push(jcnbre(10))", instr.toString());
	}

	@Test
	public void testInstrToStringReturn() {
		int lineId = 1;
		InstrReturn instr = new InstrReturn(lineId);
		Assert.assertEquals("1 : return", instr.toString());
	}

	@Test
	public void testInstrToStringStop() {
		int lineId = 1;
		InstrStop instr = new InstrStop(lineId);
		Assert.assertEquals("1 : jcstop", instr.toString());
	}

	@Test
	public void testInstrToStringStore() {
		int lineId = 1;
		InstrStore instr = new InstrStore(lineId, new ValIdent(lineId, "a"));
		Assert.assertEquals("1 : store(jcident(a))", instr.toString());
	}

	@Test
	public void testInstrToStringSwap() {
		int lineId = 1;
		InstrSwap instr = new InstrSwap(lineId);
		Assert.assertEquals("1 : swap", instr.toString());
	}

	@Test
	public void testInstrToStringWrite() {
		int lineId = 1;
		InstrWrite instr = new InstrWrite(lineId);
		Assert.assertEquals("1 : write", instr.toString());
	}

	@Test
	public void testInstrToStringWriteln() {
		int lineId = 1;
		InstrWriteln instr = new InstrWriteln(lineId);
		Assert.assertEquals("1 : writeln", instr.toString());
	}

}
