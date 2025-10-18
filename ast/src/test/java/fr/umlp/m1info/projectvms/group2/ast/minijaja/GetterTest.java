package fr.umlp.m1info.projectvms.group2.ast.minijaja;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTIdent;

import org.junit.Test;
import org.junit.Assert;

public class GetterTest {

	@Test
	public void getterIdent() {
		Assert.assertEquals("a", new ASTIdent("a", 0, 0).getName());
	}

	@Test
	public void getterNode() {
		Assert.assertEquals(0, new ASTIdent("a", 0, 1).getLine());
		Assert.assertEquals(1, new ASTIdent("a", 0, 1).getColumn());
	}
}
