package fr.umlp.m1info.projectvms.group2.ast.minijaja.decls;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.ASTNode;

/**
 * Represents a sequence of declarations or an empty declaration
 */
public abstract class ASTDecls extends ASTNode {

	/**
	 * Constructs a new ASTDecls node
	 *
	 * @param line   the source code line of this node
	 * @param column the source code column of this node
	 */
	protected ASTDecls(int line, int column) {
		super(line, column);
	}
}
