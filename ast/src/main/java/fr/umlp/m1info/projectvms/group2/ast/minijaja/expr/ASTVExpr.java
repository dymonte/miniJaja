package fr.umlp.m1info.projectvms.group2.ast.minijaja.expr;

/**
 * Represents an expr affectation "= expr"
 */
public class ASTVExpr extends ASTExpr {
	private ASTExpr expr;

	/**
	 * Constructs a new ASTVExpr node
	 *
	 * @param line   the source code line
	 * @param column the source code column
	 * @throws NullPointerException if expr is null
	 */
	public ASTVExpr(ASTExpr expr, int line, int column) {
		super(line, column);
		if (expr == null) {
			throw new NullPointerException("expr cannot be null");
		}
		this.expr = expr;
	}

	/**
	 * Returns the expression contained in this assignment
	 *
	 * @return the ASTExpr representing the rigth-hand side of the assignment
	 */
	public ASTExpr getExpr() {
		return expr;
	}

	/**
	 * Returns a string of this ndde
	 *
	 * @return a formatted string
	 */
	@Override
	public String toString() {
		return "= " + expr;
	}
}
