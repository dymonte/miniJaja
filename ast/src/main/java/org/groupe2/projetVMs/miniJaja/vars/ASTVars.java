package org.groupe2.projetVMs.miniJaja.ast.vars;

import org.groupe2.projetVMs.miniJaja.ast.ASTNode;

/**
 * Abstract base class for variable declaration sequences in MiniJaja
 */
public abstract class ASTVars extends ASTNode {
	
	/**
	 * Constructs a new ASTVars node
	 *
	 * @param line the line number of the vars declaration
	 * @param column the column number of the vars declaration
	 */
	public ASTVars(int line, int column) {
		super(line, column);
	}
}
