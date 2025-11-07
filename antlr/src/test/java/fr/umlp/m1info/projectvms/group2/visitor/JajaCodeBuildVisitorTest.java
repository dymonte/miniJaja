package fr.umlp.m1info.projectvms.group2.visitor;

import fr.umlp.m1info.projectvms.group2.utils.ParserUtils;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.instr.*;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.instr.oper.*;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.utils.Kind;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.utils.Type;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.value.*;

import org.junit.Test;

import java.util.List;

import org.junit.Assert;

public class JajaCodeBuildVisitorTest {

	private List<Instr> parsing(String input) {
		try {
			return ParserUtils.jajaCodeParseString(input);
		} catch (Exception e) {
			Assert.fail();
		}
		return null;
	}

	@Test
	public void testFromFile() {
		try {
			List<Instr> list = ParserUtils.jajaCodeParseFile("./src/test/resources/samples/jajacode.jcc");
			Assert.assertEquals(10, list.size());
			Assert.assertEquals(InstrInit.class, list.get(0).getClass());
			Assert.assertEquals(InstrIf.class, list.get(1).getClass());
			Assert.assertEquals(InstrGoto.class, list.get(2).getClass());
			Assert.assertEquals(InstrNop.class, list.get(3).getClass());
			Assert.assertEquals(InstrStop.class, list.get(4).getClass());
			Assert.assertEquals(InstrPush.class, list.get(5).getClass());
			Assert.assertEquals(InstrPop.class, list.get(6).getClass());
			Assert.assertEquals(InstrOperUnary.class, list.get(7).getClass());
			Assert.assertEquals(InstrOperBinary.class, list.get(8).getClass());
			Assert.assertEquals(InstrInit.class, list.get(9).getClass());
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@Test
	public void testVisitMultipleInstr() {
		List<Instr> list = parsing("init\npush(0)\ninc(a)");
		Assert.assertEquals(3, list.size());
		Assert.assertEquals(InstrInit.class, list.get(0).getClass());
		Assert.assertEquals(InstrPush.class, list.get(1).getClass());
		Assert.assertEquals(InstrInc.class, list.get(2).getClass());
	}

	@Test
	public void testVisitInit() {
		List<Instr> list = parsing("init");
		Assert.assertEquals(1, list.size());
		Assert.assertEquals(InstrInit.class, list.get(0).getClass());
	}

	@Test
	public void testVisitSwap() {
		List<Instr> list = parsing("swap");
		Assert.assertEquals(1, list.size());
		Assert.assertEquals(InstrSwap.class, list.get(0).getClass());
	}

	@Test
	public void testVisitNew() {
		List<Instr> list = parsing("new(tab, int, var, 3)");
		Assert.assertEquals(1, list.size());
		Assert.assertEquals(InstrNew.class, list.get(0).getClass());
		Assert.assertEquals("tab", ((InstrNew) list.get(0)).getValIdent().getIdent());
		Assert.assertEquals(Kind.VAR, ((InstrNew) list.get(0)).getKind().getKind());
		Assert.assertEquals(Type.INT, ((InstrNew) list.get(0)).getType().getType());
		Assert.assertEquals(3, ((ValAddress) ((InstrNew) list.get(0)).getValVal()).getAddress());
	}

	@Test
	public void testVisitNewArray() {
		List<Instr> list = parsing("newarray(tab, int)");
		Assert.assertEquals(1, list.size());
		Assert.assertEquals(InstrNewArray.class, list.get(0).getClass());
		Assert.assertEquals("tab", ((InstrNewArray) list.get(0)).getValIdent().getIdent());
		Assert.assertEquals(Type.INT, ((InstrNewArray) list.get(0)).getType().getType());
	}

	@Test
	public void testVisitInvoke() {
		List<Instr> list = parsing("invoke(a)");
		Assert.assertEquals(1, list.size());
		Assert.assertEquals(InstrInvoke.class, list.get(0).getClass());
		Assert.assertEquals("a", ((InstrInvoke) list.get(0)).getValIdent().getIdent());
	}

	@Test
	public void testVisitLength() {
		List<Instr> list = parsing("length(a)");
		Assert.assertEquals(1, list.size());
		Assert.assertEquals(InstrLength.class, list.get(0).getClass());
		Assert.assertEquals("a", ((InstrLength) list.get(0)).getValIdent().getIdent());
	}

	@Test
	public void testVisitReturn() {
		List<Instr> list = parsing("return");
		Assert.assertEquals(1, list.size());
		Assert.assertEquals(InstrReturn.class, list.get(0).getClass());
	}

	@Test
	public void testVisitWrite() {
		List<Instr> list = parsing("write");
		Assert.assertEquals(1, list.size());
		Assert.assertEquals(InstrWrite.class, list.get(0).getClass());
	}

	@Test
	public void testVisitWriteln() {
		List<Instr> list = parsing("writeln");
		Assert.assertEquals(1, list.size());
		Assert.assertEquals(InstrWriteln.class, list.get(0).getClass());
	}

	@Test
	public void testVisitPush() {
		List<Instr> list = parsing("push(1)");
		Assert.assertEquals(1, list.size());
		Assert.assertEquals(InstrPush.class, list.get(0).getClass());
		Assert.assertEquals(1, ((ValNbre) ((InstrPush) list.get(0)).getValVal()).getValue());

		list = parsing("push(true)");
		Assert.assertEquals(1, list.size());
		Assert.assertEquals(InstrPush.class, list.get(0).getClass());
		Assert.assertEquals(true, ((ValBool) ((InstrPush) list.get(0)).getValVal()).getValue());

		list = parsing("push()");
		Assert.assertEquals(1, list.size());
		Assert.assertEquals(InstrPush.class, list.get(0).getClass());
		Assert.assertEquals(0, ((ValNbre) ((InstrPush) list.get(0)).getValVal()).getValue());

		list = parsing("push(false)");
		Assert.assertEquals(1, list.size());
		Assert.assertEquals(InstrPush.class, list.get(0).getClass());
		Assert.assertEquals(false, ((ValBool) ((InstrPush) list.get(0)).getValVal()).getValue());

		list = parsing("push(\"ah\")");
		Assert.assertEquals(1, list.size());
		Assert.assertEquals(InstrPush.class, list.get(0).getClass());
		Assert.assertEquals("ah", ((ValString) ((InstrPush) list.get(0)).getValVal()).getValue());
	}

	@Test
	public void testVisitPop() {
		List<Instr> list = parsing("pop");
		Assert.assertEquals(1, list.size());
		Assert.assertEquals(InstrPop.class, list.get(0).getClass());
	}

	@Test
	public void testVisitLoad() {
		List<Instr> list = parsing("load(a)");
		Assert.assertEquals(1, list.size());
		Assert.assertEquals(InstrLoad.class, list.get(0).getClass());
		Assert.assertEquals("a", ((InstrLoad) list.get(0)).getValIdent().getIdent());
	}

	@Test
	public void testVisitALoad() {
		List<Instr> list = parsing("aload(a)");
		Assert.assertEquals(1, list.size());
		Assert.assertEquals(InstrALoad.class, list.get(0).getClass());
		Assert.assertEquals("a", ((InstrALoad) list.get(0)).getValIdent().getIdent());
	}

	@Test
	public void testVisitStore() {
		List<Instr> list = parsing("store(a)");
		Assert.assertEquals(1, list.size());
		Assert.assertEquals(InstrStore.class, list.get(0).getClass());
		Assert.assertEquals("a", ((InstrStore) list.get(0)).getValIdent().getIdent());
	}

	@Test
	public void testVisitAStore() {
		List<Instr> list = parsing("astore(a)");
		Assert.assertEquals(1, list.size());
		Assert.assertEquals(InstrAStore.class, list.get(0).getClass());
		Assert.assertEquals("a", ((InstrAStore) list.get(0)).getValIdent().getIdent());
	}

	@Test
	public void testVisitIf() {
		List<Instr> list = parsing("if(1)");
		Assert.assertEquals(1, list.size());
		Assert.assertEquals(InstrIf.class, list.get(0).getClass());
		Assert.assertEquals(1, ((InstrIf) list.get(0)).getValAddress().getAddress());
	}

	@Test
	public void testVisitGoto() {
		List<Instr> list = parsing("goto(1)");
		Assert.assertEquals(1, list.size());
		Assert.assertEquals(InstrGoto.class, list.get(0).getClass());
		Assert.assertEquals(1, ((InstrGoto) list.get(0)).getValAddress().getAddress());
	}

	@Test
	public void testVisitInc() {
		List<Instr> list = parsing("inc(a)");
		Assert.assertEquals(1, list.size());
		Assert.assertEquals(InstrInc.class, list.get(0).getClass());
		Assert.assertEquals("a", ((InstrInc) list.get(0)).getValIdent().getIdent());
	}

	@Test
	public void testVisitAInc() {
		List<Instr> list = parsing("ainc(a)");
		Assert.assertEquals(1, list.size());
		Assert.assertEquals(InstrAInc.class, list.get(0).getClass());
		Assert.assertEquals("a", ((InstrAInc) list.get(0)).getValIdent().getIdent());
	}

	@Test
	public void testVisitNop() {
		List<Instr> list = parsing("nop");
		Assert.assertEquals(1, list.size());
		Assert.assertEquals(InstrNop.class, list.get(0).getClass());
	}

	@Test
	public void testVisitJcStop() {
		List<Instr> list = parsing("jcstop");
		Assert.assertEquals(1, list.size());
		Assert.assertEquals(InstrStop.class, list.get(0).getClass());
	}

	@Test
	public void testVisitUnary() {
		List<Instr> list = parsing("neg");
		Assert.assertEquals(1, list.size());
		Assert.assertEquals(InstrOperUnary.class, list.get(0).getClass());
	}

	@Test
	public void testVisitBinary() {
		List<Instr> list = parsing("add");
		Assert.assertEquals(1, list.size());
		Assert.assertEquals(InstrOperBinary.class, list.get(0).getClass());
	}
}
