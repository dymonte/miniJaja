package fr.umlp.m1info.projectvms.group2.ast.minijaja.params;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.ASTNode;

/**
 * Represents a sequence of parameter declarations or an empty declaration
 */
public abstract class ASTParams extends ASTNode {

	/**
	 * Constructs a new ASTParamsChain node
	 *
	 * @param line   the source code line of this node
	 * @param column the source code column of this node
	 */
	protected ASTParams(int line, int column) {
		super(line, column);
	}
}
