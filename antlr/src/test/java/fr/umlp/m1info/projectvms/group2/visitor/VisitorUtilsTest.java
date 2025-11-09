package fr.umlp.m1info.projectvms.group2.visitor;

import org.junit.Test;
import org.junit.Assert;

import fr.umlp.m1info.projectvms.group2.exception.VisitorBuildException;

public class VisitorUtilsTest {
	@Test
	public void testLoggingMethodsDoNotThrow() {
		VisitorUtils.debug("debug message");
		VisitorUtils.info("info message");
		VisitorUtils.warn("warn message");
		VisitorUtils.error("error message");
		Assert.assertTrue(true);
	}
}
