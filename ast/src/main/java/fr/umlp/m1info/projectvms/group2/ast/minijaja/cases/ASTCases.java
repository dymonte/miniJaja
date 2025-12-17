package fr.umlp.m1info.projectvms.group2.ast.minijaja.cases;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.ASTNode;

/**
 * Represents a sequence of case declarations or an empty declaration
 */
public abstract class ASTCases extends ASTNode {

	/**
	 * Constructs a new ASTVars node
	 *
	 * @param line   the source code line where node appears
	 * @param column the source code column where node appears
	 */
	protected ASTCases(int line, int column) {
		super(line, column);
	}
}
