package fr.umlp.m1info.projectvms.group2.ast.minijaja.expr;

/**
 * Represents a string literal in the MiniJaja abstract syntax tree
 */
public class ASTStringLiteral extends ASTLiteral {
	private String value;

	/**
	 * Constructs a new ASTStringLiteral node
	 *
	 * @param value  the string value of the literal
	 * @param line   the source code line where this literal appears
	 * @param column the source code column where this literal appears
	 */
	public ASTStringLiteral(String value, int line, int column) {
		super(line, column);
		this.value = value;
	}

	/**
	 * Returns the string value of this literal
	 *
	 * @return the string stored in this literal
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Returns a string representation of this string literal in MiniJaja syntax
	 *
	 * @return the string value of this literal
	 */
	@Override
	public String toString() {
		return value;
	}
}
