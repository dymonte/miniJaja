package fr.umlp.m1info.projectvms.group2.exception;

import org.junit.Assert;
import org.junit.Test;

public class TypeCheckExceptionTest {
	@Test
	public void exceptionTest() {
		Assert.assertThrows(TypeCheckException.class, () -> {
			throw new TypeCheckException("msg");
		});
	}
}
