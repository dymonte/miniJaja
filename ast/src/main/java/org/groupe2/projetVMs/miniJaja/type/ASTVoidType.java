package org.groupe2.projetVMs.miniJaja.type;

/**
 * ASTVoidType
 */
public class ASTVoidType extends ASTNode implements ASTTypeMeth {

	public ASTVoidType(int line, int column) {
		super(line, column);
	}

	@Override
	public String toString() {
		return "void";
	}
}
