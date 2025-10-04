package org.groupe2.projetVMs.miniJaja.ast;

import org.groupe2.projetVMs.miniJaja.ast.expr.*;
import org.groupe2.projetVMs.miniJaja.ast.instr.*;
import org.groupe2.projetVMs.miniJaja.ast.type.*;
import org.groupe2.projetVMs.miniJaja.ast.var.*;
import org.groupe2.projetVMs.miniJaja.ast.vars.*;
import org.groupe2.projetVMs.miniJaja.ast.decl.*;

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
