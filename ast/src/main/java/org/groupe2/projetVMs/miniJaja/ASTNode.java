package org.groupe2.projetVMs.miniJaja.ast;

/**
 * Base class for all nodes in the Abstract Syntax Tree (AST)
 *
 * Each ASTNode stores its position in the source code using a line and column number. This information is useful for semantic analysis, error reporting, and debugging
 */
public class ASTNode {
	private final int line;
	private final int column;

	/**
	 * Constructs a new AST node with the given source position
	 * 
	 * @param line the line number where the node appears
	 * @param column the column number where the node starts
	 */
	public ASTNode(int line, int column) {
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
	public String toString() {
		return "";
	}
}
