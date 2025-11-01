package fr.umlp.m1info.projectvms.group2.ast.minijaja.expr;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.exprs.ASTExprs;

/**
 * Represents a method call from expression in the MiniJaja abstract syntax tee
 * (AST)
 */
public class ASTMethodCallExpr extends ASTExpr {
	private ASTIdent ident;
	private ASTExprs exprs;

	/**
	 * Constructs a new ASTMethodCallExpr node
	 *
	 * @param ident  the identifier of the method being called
	 * @param exprs  the list of expression passed to the method
	 * @param line   the source code line where this node appears
	 * @param column the source code column where this node begins
	 * @throws NullPointerException if ident or exprs is null
	 */
	public ASTMethodCallExpr(ASTIdent ident, ASTExprs exprs, int line, int column) {
		super(line, column);
		if (ident == null) {
			throw new NullPointerException("ident cannot be null");
		} else if (exprs == null) {
			throw new NullPointerException("exprs cannot be null");
		}
		this.ident = ident;
		this.exprs = exprs;
	}

	/**
	 * Returns the identifier of the method being called
	 *
	 * @return the ASTIdent representing the method name
	 */
	public ASTIdent getIdent() {
		return ident;
	}

	/**
	 * Returns the list of expressions passed as arguments to the method
	 *
	 * @return the ASTExprs representing the method arguments
	 */
	public ASTExprs getExprs() {
		return exprs;
	}

	/**
	 * Returns a string representation of this method call
	 *
	 * @return a formatted string
	 */
	@Override
	public String toString() {
		return ident + "(" + exprs + ")";
	}
}
