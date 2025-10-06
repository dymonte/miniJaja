
package org.groupe2.projetVMs.miniJaja.ast.exprs;

import org.groupe2.projetVMs.miniJaja.ast.ASTNode;
/**
 * Represents an empty sequence of expression in MiniJaja
 */
public class ASTExprsNil extends ASTNode implements ASTExprs {
	/**
	 * Constructs an empty expressions sequence
	 *
	 * @param line the line number of the nil declaration
	 * @param column the column number of the nil declaration
	 */
	public ASTExprsNil(int line, int column) {
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
