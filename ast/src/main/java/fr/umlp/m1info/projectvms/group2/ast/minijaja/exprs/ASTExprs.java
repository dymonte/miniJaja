package fr.umlp.m1info.projectvms.group2.ast.minijaja.exprs;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.ASTNode;

/**
 * Represents a sequence of expressions or an empty expression
 */
public abstract class ASTExprs extends ASTNode {

	/**
	 * Constructs a new ASTExprs node
	 *
	 * @param line the line number of the node appears
	 * @param column the column of the node appears
	 */
	public ASTExprs(int line, int column) {
		super(line, column);
	}
}
