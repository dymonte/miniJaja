package fr.umlp.m1info.projectvms.group2.ast.minijaja;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.type.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.var.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.vars.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decl.*;

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
