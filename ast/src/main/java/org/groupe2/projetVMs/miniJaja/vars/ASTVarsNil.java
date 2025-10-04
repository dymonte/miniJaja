package org.groupe2.projetVMs.miniJaja.ast.vars;

/**
 * Represents an empty sequence of variable declarations in MiniJaja
 */
public class ASTVarsNil extends ASTVars {
	/**
	 * Constructs an empty variable declaration sequence
	 *
	 * @param line the line number of the nil declaration
	 * @param column the column number of the nil declaration
	 */
	public ASTVarsNil(int line, int column) {
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
