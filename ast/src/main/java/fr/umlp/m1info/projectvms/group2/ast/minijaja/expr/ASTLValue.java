package fr.umlp.m1info.projectvms.group2.ast.minijaja.expr;

/**
 * Represents all references in MiniJaja like Array or Simple variable
 */
public abstract class ASTLValue extends ASTExpr {
	/**
	 * Constructs a new ASTLValue node
	 *
	 * @param line the line in the source code
	 * @param column the column in the source code
	 */
	public ASTLValue(int line, int column) {
		super(line, column);
	}
}
