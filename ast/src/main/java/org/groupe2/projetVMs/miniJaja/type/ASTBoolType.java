package org.groupe2.projetVMs.miniJaja.ast.type;

import org.groupe2.projetVMs.miniJaja.ast.ASTNode;

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
