package fr.umlp.m1info.projectVMs.group2.miniJaja.ast.decl;

import fr.umlp.m1info.projectVMs.group2.miniJaja.ast.ASTNode;

/**
 * Abstract base class representing a declaration in the MiniJaja AST
 */
public abstract class ASTDecl extends ASTNode {

	/**
	 * Constructs a new declaration node with the specified source code position
	 *
	 * @param line the line number in the source code
	 * @param column the column number in the source code
	 */
	public ASTDecl(int line, int column) {
		super(line, column);
	}
}
