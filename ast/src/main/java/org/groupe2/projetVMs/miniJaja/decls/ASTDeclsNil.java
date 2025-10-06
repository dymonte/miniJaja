package org.groupe2.projetVMs.miniJaja.ast.decls;

import org.groupe2.projetVMs.miniJaja.ast.ASTNode;
/**
 * Represents an empty sequence of declaration in MiniJaja
 */
public class ASTDeclsNil extends ASTNode implements ASTDecls {
	/**
	 * Constructs an empty declaration sequence
	 *
	 * @param line the line number of the nil declaration
	 * @param column the column number of the nil declaration
	 */
	public ASTDeclsNil(int line, int column) {
		super(line, column);
	}

	/**
	 * Returns an empty string
	 *
	 * @return an empty string
	 */
	@Override
	public String toString() {
		return "";
	}
}
