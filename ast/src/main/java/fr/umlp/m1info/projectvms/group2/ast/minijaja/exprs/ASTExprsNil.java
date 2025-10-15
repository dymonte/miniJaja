package fr.umlp.m1info.projectvms.group2.ast.minijaja.exprs;

/**
 * Represents an empty sequence of expression in MiniJaja
 */
public class ASTExprsNil extends ASTExprs {
	/**
	 * Constructs an empty expressions sequence
	 *
	 * @param line   the line number of the nil declaration
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
