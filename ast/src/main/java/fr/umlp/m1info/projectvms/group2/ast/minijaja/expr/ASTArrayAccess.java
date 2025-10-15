package fr.umlp.m1info.projectvms.group2.ast.minijaja.expr;

/**
 * Represents an array expression in the Abstract Syntax Tree (AST)
 *
 */
public class ASTArrayAccess extends ASTLValue {
	private ASTIdent ident;
	private ASTExpr expr;

	/**
	 * Constructs a new ASTArrayAccess node
	 *
	 * @param ident  the array identifier
	 * @param expr   the index expression
	 * @param line   the line number in the source code
	 * @param column the column number in the source code
	 * @throws NullPointerException if ident or expr is null
	 */
	public ASTArrayAccess(ASTIdent ident, ASTExpr expr, int line, int column) {
		super(line, column);
		if (ident == null) {
			throw new NullPointerException("ident cannot be null");
		} else if (expr == null) {
			throw new NullPointerException("expr cannot be null");
		}
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
