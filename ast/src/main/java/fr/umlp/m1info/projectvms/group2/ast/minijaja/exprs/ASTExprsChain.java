package fr.umlp.m1info.projectvms.group2.ast.minijaja.exprs;

import fr.umlp.m1info.projectvms.group2.utils.Chain;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTExpr;

/**
 * Represents a chain of expressions in MiniJaja
 */
public class ASTExprsChain extends ASTExprs {
	private Chain<ASTExpr, ASTExprs> chain;

	/**
	 * Constructs a new ASTExprChain node
	 *
	 * @param expr   the first expression
	 * @param exprs  the rest of the expressions
	 * @param line   the source code line of this node
	 * @param column the source code column of this node
	 * @throws NullPointerException if expr or exprs is null
	 */
	public ASTExprsChain(ASTExpr expr, ASTExprs exprs, int line, int column) {
		super(line, column);
		if (expr == null) {
			throw new NullPointerException("expr cannot be null");
		} else if (exprs == null) {
			throw new NullPointerException("exprs cannot be null");
		}
		chain = new Chain<ASTExpr, ASTExprs>(expr, exprs);
	}

	/**
	 * Returns a string representation of the expressions in MiniJaja syntax
	 *
	 * @return formatted string of this AST node
	 */
	@Override
	public String toString() {
		return chain.toString(",");
	}
}
