package fr.umlp.m1info.projectvms.group2.ast.jajacode;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.instr.*;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.instr.oper.InstrOperBinary;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.instr.oper.InstrOperUnary;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.value.ValIdent;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.value.ValString;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.var.VarKind;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.var.VarType;
import fr.umlp.m1info.projectvms.group2.utils.Kind;
import fr.umlp.m1info.projectvms.group2.utils.Type;

import org.junit.Assert;
import org.junit.Test;

public class JajaCodeNodeNullArgumentTest {
	// Utils
	@Test
	public void testKindNull() {
		NullPointerException exception = Assert.assertThrows(NullPointerException.class, () -> new VarKind(1, null));
		Assert.assertEquals("kind cannot be null", exception.getMessage());
	}

	@Test
	public void testTypeNull() {
		NullPointerException exception = Assert.assertThrows(NullPointerException.class, () -> new VarType(1, null));
		Assert.assertEquals("type cannot be null", exception.getMessage());
	}

	// Values
	@Test
	public void testValIdentNull() {
		NullPointerException exception = Assert.assertThrows(NullPointerException.class, () -> new ValIdent(1, null));
		Assert.assertEquals("id cannot be null", exception.getMessage());
	}

	@Test
	public void testValStringNull() {
		NullPointerException exception = Assert.assertThrows(NullPointerException.class, () -> new ValString(1, null));
		Assert.assertEquals("value cannot be null", exception.getMessage());
	}

	// Instructions
	@Test
	public void testInstrOperNull() {
		NullPointerException exception = Assert.assertThrows(NullPointerException.class,
				() -> new InstrOperBinary(1, null));
		Assert.assertEquals("oper cannot be null", exception.getMessage());

		exception = Assert.assertThrows(NullPointerException.class, () -> new InstrOperUnary(1, null));
		Assert.assertEquals("oper cannot be null", exception.getMessage());
	}

	@Test
	public void testInstrAIncNull() {
		NullPointerException exception = Assert.assertThrows(NullPointerException.class, () -> new InstrAInc(1, null));
		Assert.assertEquals("valIdent cannot be null", exception.getMessage());
	}

	@Test
	public void testInstrALoadNull() {
		NullPointerException exception = Assert.assertThrows(NullPointerException.class, () -> new InstrALoad(1, null));
		Assert.assertEquals("valIdent cannot be null", exception.getMessage());
	}

	@Test
	public void testInstrAStoreNull() {
		NullPointerException exception = Assert.assertThrows(NullPointerException.class, () -> new InstrAStore(1, null));
		Assert.assertEquals("valIdent cannot be null", exception.getMessage());
	}

	@Test
	public void testInstrGotoNull() {
		NullPointerException exception = Assert.assertThrows(NullPointerException.class, () -> new InstrGoto(1, null));
		Assert.assertEquals("valAddress cannot be null", exception.getMessage());
	}

	@Test
	public void testInstrIfNull() {
		NullPointerException exception = Assert.assertThrows(NullPointerException.class, () -> new InstrIf(1, null));
		Assert.assertEquals("valAddress cannot be null", exception.getMessage());
	}

	@Test
	public void testInstrIncNull() {
		NullPointerException exception = Assert.assertThrows(NullPointerException.class, () -> new InstrInc(1, null));
		Assert.assertEquals("valIdent cannot be null", exception.getMessage());
	}

	@Test
	public void testInstrInvokeNull() {
		NullPointerException exception = Assert.assertThrows(NullPointerException.class, () -> new InstrInvoke(1, null));
		Assert.assertEquals("valIdent cannot be null", exception.getMessage());
	}

	@Test
	public void testInstrLengthNull() {
		NullPointerException exception = Assert.assertThrows(NullPointerException.class, () -> new InstrLength(1, null));
		Assert.assertEquals("valIdent cannot be null", exception.getMessage());
	}

	@Test
	public void testInstrLoadNull() {
		NullPointerException exception = Assert.assertThrows(NullPointerException.class, () -> new InstrLoad(1, null));
		Assert.assertEquals("valIdent cannot be null", exception.getMessage());
	}

	@Test
	public void testInstrNewNull() {
		NullPointerException exception = Assert.assertThrows(NullPointerException.class,
				() -> new InstrNew(1, null, null, null, null));
		Assert.assertEquals("valIdent cannot be null", exception.getMessage());

		exception = Assert.assertThrows(NullPointerException.class,
				() -> new InstrNew(1, new ValIdent(1, "a"), null, null, null));
		Assert.assertEquals("kind cannot be null", exception.getMessage());

		exception = Assert.assertThrows(NullPointerException.class,
				() -> new InstrNew(1, new ValIdent(1, "a"), new VarKind(1, Kind.VAR), null, null));
		Assert.assertEquals("type cannot be null", exception.getMessage());

		exception = Assert.assertThrows(NullPointerException.class,
				() -> new InstrNew(1, new ValIdent(1, "a"), new VarKind(1, Kind.VAR), new VarType(1, Type.INT), null));
		Assert.assertEquals("val cannot be null", exception.getMessage());
	}

	@Test
	public void testInstrNewArrayNull() {
		NullPointerException exception = Assert.assertThrows(NullPointerException.class,
				() -> new InstrNewArray(1, null, null));
		Assert.assertEquals("valIdent cannot be null", exception.getMessage());

		exception = Assert.assertThrows(NullPointerException.class, () -> new InstrNewArray(1, new ValIdent(1, "a"), null));
		Assert.assertEquals("type cannot be null", exception.getMessage());
	}

	@Test
	public void testInstrPushNull() {
		NullPointerException exception = Assert.assertThrows(NullPointerException.class, () -> new InstrPush(1, null));
		Assert.assertEquals("val cannot be null", exception.getMessage());
	}

	@Test
	public void testInstrStoreNull() {
		NullPointerException exception = Assert.assertThrows(NullPointerException.class, () -> new InstrStore(1, null));
		Assert.assertEquals("valIdent cannot be null", exception.getMessage());
	}

}
