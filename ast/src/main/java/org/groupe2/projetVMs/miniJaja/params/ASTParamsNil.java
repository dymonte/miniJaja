package org.groupe2.projetVMs.miniJaja.ast.params;

import org.groupe2.projetVMs.miniJaja.ast.ASTNode;
/**
 * Represents an empty sequence of parameter declarations in MiniJaja
 */
public class ASTParamsNil extends ASTNode implements ASTParams {
	/**
	 * Constructs an empty parameter declaration sequence
	 *
	 * @param line the line number of the nil declaration
	 * @param column the column number of the nil declaration
	 */
	public ASTParamsNil(int line, int column) {
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
