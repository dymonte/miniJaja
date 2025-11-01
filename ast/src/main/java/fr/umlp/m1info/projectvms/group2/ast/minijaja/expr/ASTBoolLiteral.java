package fr.umlp.m1info.projectvms.group2.ast.minijaja.expr;

/**
 * Represents a boolean literal in the MiniJaja abstract syntax tree
 */
public class ASTBoolLiteral extends ASTLiteral {
	private boolean value;

	/**
	 * Constructs a new ASTBoolLiteral node
	 *
	 * @param value  the boolean value of the literal
	 * @param line   the source code line where this literal appears
	 * @param column the source code column where this literal appears
	 */
	public ASTBoolLiteral(boolean value, int line, int column) {
		super(line, column);
		this.value = value;
	}

	/**
	 * Returns the boolean value of this literal
	 *
	 * @return the boolean value stored in the literal
	 */
	public boolean getValue() {
		return value;
	}

	/**
	 * Returns a string representation of this boolean literal in MiniJaja syntax
	 *
	 * @return true if the value is true, otherwise false
	 */
	@Override
	public String toString() {
		return (value ? "true" : "false");
	}
}
