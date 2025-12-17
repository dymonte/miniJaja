package fr.umlp.m1info.projectvms.group2.ast.minijaja.cases;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.ASTNode;

/**
 * Abstract base class used to group ASTCase and ASTDefault
 */
public abstract class ASTCaseGroup extends ASTNode {

	/**
	 * Constructs a new ASTCaseGroup node
	 *
	 * @param line   the line number in the source code
	 * @param column the column number in the source code
	 */
	protected ASTCaseGroup(int line, int column) {
		super(line, column);
	}
}
