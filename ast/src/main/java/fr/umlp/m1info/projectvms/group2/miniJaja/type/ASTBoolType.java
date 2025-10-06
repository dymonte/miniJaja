package fr.umlp.m1info.projectvms.group2.miniJaja.ast.type;

import fr.umlp.m1info.projectvms.group2.miniJaja.ast.ASTNode;

/**
 * ASTBoolType
 */
public final class ASTBoolType extends ASTNode implements ASTType, ASTTypeMeth {

	public ASTBoolType(int line, int column) {
		super(line, column);
	}

	@Override
	public String toString() {
		return "boolean";
	}
}