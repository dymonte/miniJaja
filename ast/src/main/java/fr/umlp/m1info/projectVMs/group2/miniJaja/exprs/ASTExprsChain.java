package fr.umlp.m1info.projectVMs.group2.miniJaja.ast.exprs;

import fr.umlp.m1info.projectVMs.group2.miniJaja.ast.ASTChain;
import fr.umlp.m1info.projectVMs.group2.miniJaja.ast.expr.ASTExpr;

/**
 * Represents a chain of expressions in MiniJaja
 */
public class ASTExprsChain extends ASTChain<ASTExpr, ASTExprs> implements ASTExprs {
	/**
	 * Constructs a new ASTExprChain node
	 *
	 * @param var the first expression
	 * @param vars the rest of the expressions
	 * @param line the source code line of this node
	 * @param column the source code column of this node
	 */
	public ASTExprsChain(ASTExpr expr, ASTExprs exprs, int line, int column) {
		super(expr, exprs, line, column);
	}

	/**
	 * Returns a string representation of the expressions in MiniJaja syntax
	 *
	 * @return formatted string of this AST node
	 */
	@Override
	public String toString() {
		return toString(",");
	}
}
