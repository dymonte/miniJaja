package fr.umlp.m1info.projectvms.group2.utils;

import org.junit.Assert;
import org.junit.Test;

public class KindTest {
	@Test
	public void fromStringTest() {
		Kind kind = Kind.fromString("var");
		Assert.assertEquals(Kind.VAR, kind);
		Assert.assertEquals("var", kind.toString());
		kind = Kind.fromString("cst");
		Assert.assertEquals(Kind.CONST, kind);
		Assert.assertEquals("cst", kind.toString());
		kind = Kind.fromString("vcst");
		Assert.assertEquals(Kind.VCONST, kind);
		Assert.assertEquals("vcst", kind.toString());
		kind = Kind.fromString("array");
		Assert.assertEquals(Kind.ARRAY, kind);
		Assert.assertEquals("array", kind.toString());
		kind = Kind.fromString("meth");
		Assert.assertEquals(Kind.METH, kind);
		Assert.assertEquals("meth", kind.toString());

		Assert.assertThrows(IllegalArgumentException.class, () -> Kind.fromString("unknown"));
	}
}
