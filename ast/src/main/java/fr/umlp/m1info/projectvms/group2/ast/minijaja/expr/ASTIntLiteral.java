package fr.umlp.m1info.projectvms.group2.ast.minijaja.expr;

/**
 * Represents an integer literal in the MiniJaja abstract syntax tree
 */
public class ASTIntLiteral extends ASTLiteral {
	private final int value;

	/**
	 * Constructs a new ASTIntLiteral node
	 *
	 * @param value  the integer value of the literal
	 * @param line   the source code line where this literal appears
	 * @param column the source code column where this literal appears
	 */
	public ASTIntLiteral(int value, int line, int column) {
		super(line, column);
		this.value = value;
	}

	/**
	 * Returns the integer value of this literal
	 * 
	 * @retrun the integer value stored in this literal
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Returns a string representation of this integer literal
	 *
	 * @return the string representation of this integer value
	 */
	@Override
	public String toString() {
		return Integer.toString(value);
	}
}
