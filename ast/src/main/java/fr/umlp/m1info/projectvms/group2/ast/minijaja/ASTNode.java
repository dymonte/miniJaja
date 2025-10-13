package fr.umlp.m1info.projectvms.group2.ast.minijaja;

/**
 * Base class for all nodes in the Abstract Syntax Tree (AST)
 *
 * Each ASTNode stores its position in the source code using a line and column
 * number. This information is useful for semantic analysis, error reporting,
 * and debugging
 */
public abstract class ASTNode {
	protected final int line;
	protected final int column;

	/**
	 * Constructs a new AST node with the given source position
	 * 
	 * @param line   the line number where the node appears
	 * @param column the column number where the node starts
	 * @throws IllegalArgumentException if line or column is less than 0
	 */
	public ASTNode(int line, int column) {
		if (line < 0) {
			throw new IllegalArgumentException("line must be higher than -1");
		} else if (column < 0) {
			throw new IllegalArgumentException("column must be higher than -1");
		}
		this.line = line;
		this.column = column;
	}

	/**
	 * Returns the line number of this node in the source code
	 *
	 * @return the line number (1-based)
	 */
	public int getLine() {
		return line;
	}

	/**
	 * Returns the column number of this node in the source code
	 *
	 * @return the column number (0-based)
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * Returns a string representation of this node
	 *
	 * @return an empty string by default
	 */
	public abstract String toString();
}
