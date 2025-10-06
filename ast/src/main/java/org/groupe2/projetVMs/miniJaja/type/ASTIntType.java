package org.groupe2.projetVMs.miniJaja.ast.type;

import org.groupe2.projetVMs.miniJaja.ast.ASTNode;

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
