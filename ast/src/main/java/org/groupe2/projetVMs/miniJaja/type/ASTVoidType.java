package org.groupe2.projetVMs.miniJaja.ast.type;

import org.groupe2.projetVMs.miniJaja.ast.ASTNode;

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
