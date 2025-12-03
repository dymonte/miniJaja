package fr.umlp.m1info.projectvms.group2.checker;

import fr.umlp.m1info.projectvms.group2.checker.Diagnostic.ErrorCode;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DiagnosticTest {
	Diagnostic diag;

	@Before
	public void setUp() {
		diag = new Diagnostic();
	}

	@Test
	public void diagTest() {
		diag.error(ErrorCode.TYPE_MISMATCH, "msg", 0, 0);
		Assert.assertEquals(1, diag.issues().size());

		Assert.assertEquals("ERROR E1001 [0:0]: msg", diag.issues().get(0).toString());

		diag.warning(ErrorCode.TYPE_MISMATCH, "warning", 1, 1);
		Assert.assertEquals(2, diag.issues().size());

		Assert.assertEquals("WARNING E1001 [1:1]: warning", diag.issues().get(1).toString());

		diag.note(ErrorCode.ARGUMENT_MISMATCH, "msg", 0, 0);
		Assert.assertEquals(3, diag.issues().size());

		Assert.assertEquals("NOTE E1031 [0:0]: msg", diag.issues().get(2).toString());
	}
}
