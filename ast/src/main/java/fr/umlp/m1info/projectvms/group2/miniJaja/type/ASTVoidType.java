package fr.umlp.m1info.projectvms.group2.miniJaja.ast.type;

import fr.umlp.m1info.projectvms.group2.miniJaja.ast.ASTNode;

/**
 * ASTVoidType
 */
public final class ASTVoidType extends ASTNode implements ASTTypeMeth {

	public ASTVoidType(int line, int column) {
		super(line, column);
	}

	@Override
	public String toString() {
		return "void";
	}
}