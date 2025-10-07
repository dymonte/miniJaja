package fr.umlp.m1info.projectvms.group2.ast.minijaja;

/**
 * Class representing a recursive chain in the AST (Abstract Syntax Tree)
 */
public class Chain<L, R> {
	private L left;
	private R right;

	/**
	 * Constructs a new Chain node
	 *
	 * @param left   the left element of the chain
	 * @param right  the right element of the chain
	 */
	public Chain(L left, R right) {
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
	 * Returns a string representation of this chain node using the specified
	 * delimiter
	 *
	 * @param delimiter the string to insert between the left and right elements
	 * @return a string representation of the chain node
	 */
	public String toString(String delimiter) {
		return (left != null ? left : "") + delimiter + " " + (right != null ? right : "");
	}
}
