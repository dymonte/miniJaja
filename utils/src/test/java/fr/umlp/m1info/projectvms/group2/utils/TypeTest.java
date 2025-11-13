package fr.umlp.m1info.projectvms.group2.utils;

import org.junit.Assert;
import org.junit.Test;

public class TypeTest {
	@Test
	public void fromStringTest() {
		Type type = Type.fromString("int");
		Assert.assertEquals(Type.INT, type);
		Assert.assertEquals("int", type.toString());
		type = Type.fromString("boolean");
		Assert.assertEquals(Type.BOOL, type);
		Assert.assertEquals("boolean", type.toString());
		type = Type.fromString("string");
		Assert.assertEquals(Type.STRING, type);
		Assert.assertEquals("string", type.toString());
		type = Type.fromString("void");
		Assert.assertEquals(Type.VOID, type);
		Assert.assertEquals("void", type.toString());

		Assert.assertThrows(IllegalArgumentException.class, () -> Type.fromString("unknown"));
	}
}
