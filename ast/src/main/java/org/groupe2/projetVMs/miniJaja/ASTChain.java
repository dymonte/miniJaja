package org.groupe2.projetVMs.miniJaja.ast;

/**
 * Abstract base class representing a recursive chain in the AST (Abstract Syntax Tree)
 */
public abstract class ASTChain<L, R> extends ASTNode {
	protected final L left;
	protected final R right;

	/**
	 * Constructs a new ASTChain node
	 *
	 * @param left the left element of the chain
	 * @param right the right element of the chain
	 * @param line the line number of the node appears
	 * @param column the column of the node appears
	 */
	public ASTChain(L left, R right, int line, int column) {
		super(line, column);
		this.left = left;
		this.right = right;
	}

	/**
	 * Returns the left element of this chain node
	 *
	 * @return the left element
	 */
	public L getLeft() {
		return left;
	}

	/**
	 * Returns the right element of this chain node
	 *
	 * @return the right element
	 */
	public R getRight() {
		return right;
	}

	/**
	 * Returns a string representation of this chain node using the specified delimiter
	 *
	 * @param delimiter the string to insert between the left and right elements
	 * @return a string representation of the chain node
	 */
	public String toString(String delimiter) {
		return (left != null ? left : "") 
				+ delimiter + "\n" 
				+ (right != null ? right : "");
	}
}
