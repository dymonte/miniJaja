package org.groupe2.projetVMs.miniJaja.type;

import org.groupe2.projetVMs.miniJaja.ast.ASTNode;

/**
 * ASTIntType
 */
public class ASTIntType extends ASTNode implements ASTType {

	public ASTIntType(int line, int column) {
		super(line, column);
	}

	@Override
	public String toString() {
		return "int";
	}
}
