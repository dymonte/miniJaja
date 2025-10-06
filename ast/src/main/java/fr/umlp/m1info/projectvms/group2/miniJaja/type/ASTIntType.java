package fr.umlp.m1info.projectvms.group2.miniJaja.ast.type;

import fr.umlp.m1info.projectvms.group2.miniJaja.ast.ASTNode;

/**
 * ASTIntType
 */
public final class ASTIntType extends ASTNode implements ASTType, ASTTypeMeth {

	public ASTIntType(int line, int column) {
		super(line, column);
	}

	@Override
	public String toString() {
		return "int";
	}
}