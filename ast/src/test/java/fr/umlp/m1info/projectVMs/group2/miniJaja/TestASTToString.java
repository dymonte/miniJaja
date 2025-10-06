package fr.umlp.m1info.projectVMs.group2.miniJaja.ast;

import fr.umlp.m1info.projectVMs.group2.miniJaja.ast.expr.*;
import fr.umlp.m1info.projectVMs.group2.miniJaja.ast.instr.*;
import fr.umlp.m1info.projectVMs.group2.miniJaja.ast.type.*;
import fr.umlp.m1info.projectVMs.group2.miniJaja.ast.var.*;
import fr.umlp.m1info.projectVMs.group2.miniJaja.ast.vars.*;
import fr.umlp.m1info.projectVMs.group2.miniJaja.ast.decl.*;

import org.junit.Test;
import org.junit.Assert;

public class TestASTToString {

	/**
	 * Expr
	 */
	@Test
	public void testToStringIdent() {
		Assert.assertEquals("a", (new ASTIdent("a", 0, 0)).toString());
	}

	/**
	 * Instr
	 */

	/**
	 * Type
	 */
	@Test
	public void testToStringBool() {
		Assert.assertEquals("boolean", (new ASTBoolType(0, 0)).toString());
	}

	@Test
	public void testToStringInt() {
		Assert.assertEquals("int", (new ASTIntType(0, 0)).toString());
	}

	@Test
	public void testToStringVoid() {
		Assert.assertEquals("void", (new ASTVoidType(0, 0)).toString());
	}
	/**
	 * Var
	 */

	/**
	 * Vars
	 */

	/**
	 * Decl
	 */
}
