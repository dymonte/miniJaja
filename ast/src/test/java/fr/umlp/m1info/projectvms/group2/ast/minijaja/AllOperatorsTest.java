package fr.umlp.m1info.projectvms.group2.ast.minijaja;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.BinaryOp;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.UnaryOp;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.IncrementOp;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.AssignmentOp;

import org.junit.Test;
import org.junit.Assert;

public class AllOperatorsTest {

	@Test
	public void incrementOperatorTest() {
		Assert.assertEquals("++", IncrementOp.fromSymbol("++").toString());
		Assert.assertThrows(IllegalArgumentException.class, () -> IncrementOp.fromSymbol("Exception"));
	}

	@Test
	public void assignmentOperatorTest() {
		Assert.assertEquals("=", AssignmentOp.fromSymbol("=").toString());
		Assert.assertEquals("+=", AssignmentOp.fromSymbol("+=").toString());
		Assert.assertThrows(IllegalArgumentException.class, () -> AssignmentOp.fromSymbol("Exception"));
	}

	@Test
	public void binaryOperatorTest() {
		Assert.assertEquals("+", BinaryOp.fromSymbol("+").toString());
		Assert.assertEquals("-", BinaryOp.fromSymbol("-").toString());
		Assert.assertEquals("*", BinaryOp.fromSymbol("*").toString());
		Assert.assertEquals("/", BinaryOp.fromSymbol("/").toString());
		Assert.assertEquals("&&", BinaryOp.fromSymbol("&&").toString());
		Assert.assertEquals("||", BinaryOp.fromSymbol("||").toString());
		Assert.assertEquals("==", BinaryOp.fromSymbol("==").toString());
		Assert.assertEquals("<", BinaryOp.fromSymbol("<").toString());
		Assert.assertEquals(">", BinaryOp.fromSymbol(">").toString());
		Assert.assertThrows(IllegalArgumentException.class, () -> BinaryOp.fromSymbol("Exception"));
	}

	@Test
	public void unaryOperatorTest() {
		Assert.assertEquals("-", UnaryOp.fromSymbol("-").toString());
		Assert.assertEquals("!", UnaryOp.fromSymbol("!").toString());
		Assert.assertThrows(IllegalArgumentException.class, () -> UnaryOp.fromSymbol("Exception"));
	}
}
