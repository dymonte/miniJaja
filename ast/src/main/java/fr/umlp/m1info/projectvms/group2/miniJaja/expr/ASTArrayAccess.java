package fr.umlp.m1info.projectvms.group2.ast.minijaja.expr;

/**
 * Represents an array expression in the Abstract Syntax Tree (AST)
 *
 */
public class ASTArrayAccess extends ASTExpr {
	private ASTIdent ident;
	private ASTExpr expr;

	/**
	 * Constructs a new ASTArrayAccess node
	 *
	 * @param ident the array identifier
	 * @param expr the index expression
	 * @param line   the line number in the source code
	 * @param column the column number in the source code
	 */
	public ASTArrayAccess(ASTIdent ident, ASTExpr expr, int line, int column) {
		super(line, column);
		this.ident = ident;
		this.expr = expr;
	}

	/**
	 * returns a string representation of this array access expression
	 *
	 * @return a formatted string
	 */
	@Override
	public String toString() {
		return ident + "[" + expr + "]";
	}
}
