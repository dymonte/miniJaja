package fr.umlp.m1info.projectvms.group2.utils;

import org.junit.Test;
import org.junit.Assert;

public class UtilsTest {

	@Test
	public void testToStringChain() {
		Assert.assertEquals("0,a", (new Chain<>(0, "a")).toString(","));
		Assert.assertEquals(",a", (new Chain<>((String) null, "a")).toString(","));
		Assert.assertEquals("0;\na", (new Chain<>(0, "a")).toString(";"));
		Assert.assertEquals("0,", (new Chain<>(0, (String) null)).toString(","));
		Assert.assertEquals(";\na", (new Chain<>((String) null, "a")).toString(";"));
		Assert.assertEquals("0;\n", (new Chain<>(0, (String) null)).toString(";"));
	}

	@Test
	public void getterChain() {
		Assert.assertEquals(0, (int) new Chain<>(0, "a").getLeft());
		Assert.assertEquals("a", new Chain<>(0, "a").getRight());
	}
}
