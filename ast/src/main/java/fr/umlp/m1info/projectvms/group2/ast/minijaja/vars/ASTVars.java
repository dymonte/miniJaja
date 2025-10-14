package fr.umlp.m1info.projectvms.group2.ast.minijaja.vars;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.ASTNode;

/**
 * Represents a sequence of variable declarations or an empty declaration
 */
public abstract class ASTVars extends ASTNode {

	/**
	 * Constructs a new ASTVars node
	 *
	 * @param line   the source code line where node appears
	 * @param column the source code column where node appears
	 */
	protected ASTVars(int line, int column) {
		super(line, column);
	}

}
