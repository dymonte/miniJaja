package org.groupe2.projetVMs.miniJaja.type;

import org.groupe2.projetVMs.miniJaja.ast.ASTNode;

/**
 * ASTBoolType
 */
public class ASTBoolType extends ASTNode implements ASTType {

	public ASTBoolType(int line, int column) {
		super(line, column);
	}

	@Override
	public String toString() {
		return "bool";
	}
}
