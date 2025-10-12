package fr.umlp.m1info.projectvms.group2.ast.minijaja.expr;

/**
 * Represents an expr affectation "= expr"
 */
public class ASTVExpr extends ASTExpr {
	private ASTExpr expr;

	/**
	 * Constructs a new ASTVExpr node
	 *
	 * @param line the source code line
	 * @param column the source code column
	 */
	public ASTVExpr(ASTExpr expr, int line, int column) {
		super(line, column);
		this.expr = expr;
	}

	/**
	 * Returns a string of this ndde
	 *
	 * @return a formatted string
	 */
	public String toString() {
		return "= " + expr;
	}
}
