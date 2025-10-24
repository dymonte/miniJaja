package fr.umlp.m1info.projectvms.group2.ast.minijaja.expr;

/**
 * Represents an abstract literal expression in the MiniJaja abstract syntax
 * tree
 */
public abstract class ASTLiteral extends ASTExpr {

	/**
	 * Constructs a new abstract literal expression node
	 *
	 * @param line   the source code line where this literal appears
	 * @param column the source code column where this literal appears
	 */
	protected ASTLiteral(int line, int column) {
		super(line, column);
	}
}
