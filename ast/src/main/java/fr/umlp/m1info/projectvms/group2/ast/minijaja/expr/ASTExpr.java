package fr.umlp.m1info.projectvms.group2.ast.minijaja.expr;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.exprs.ASTExprs;

/**
 * Represents an abstract expression node in the MiniJaja Abstract Syntax Tree
 */
public abstract class ASTExpr extends ASTExprs {

	/**
	 * Constructs a new abstract expression node
	 *
	 * @param line   the source code line where this expression appears
	 * @param column the source code column where this expression appears
	 */
	protected ASTExpr(int line, int column) {
		super(line, column);
	}
}
