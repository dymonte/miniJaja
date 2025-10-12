package fr.umlp.m1info.projectvms.group2.ast.minijaja.expr;

/**
 * Represents a length(expr) expression in the MiniJaja abstract syntax tree (AST)
 */
public class ASTLength extends ASTExpr {
	private ASTIdent ident;

	/**
	 * Constructs a new ASTLength node
	 *
	 * @param ident the array identifier used in the length expression
	 * @param line the source code line where this node appears
	 * @param column the source code column where this node appears
	 */
	public ASTLength(ASTIdent ident, int line, int column) {
		super(line, column);
		this.ident = ident;
	}

	/**
	 * Returns a string representation of this node
	 *
	 * @return formatted string of length
	 */
	public String toString() {
		return "length(" + ident + ")";
	}
}
