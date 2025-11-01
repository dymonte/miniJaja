package fr.umlp.m1info.projectvms.group2.ast.minijaja.expr;

/**
 * Represents an identifier node in the Abstract Syntax Tree (AST)
 *
 * An ASTIdent corresponds to an identifier (e.g., variable name, class name,
 * method name) found in the MiniJaja source code. It stores both the
 * identifier's textual and its source position (line and column) for error
 * reporting and debugging
 */
public class ASTIdent extends ASTLValue {
	private String name;

	/**
	 * Constructs a new ASTIdent
	 *
	 * @param name   the textual name of the identifier
	 * @param line   the line number in the source code
	 * @param column the column number in the source code
	 */
	public ASTIdent(String name, int line, int column) {
		super(line, column);
		this.name = name;
	}

	/**
	 * Returns the textual name of the identifier
	 *
	 * @return the identifier's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * returns the identifier's name as its string representation
	 *
	 * @return the identifier name
	 */
	@Override
	public String toString() {
		return name;
	}
}
