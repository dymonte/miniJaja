package org.groupe2.projetVMs.miniJaja.ast.var;

import org.groupe2.projetVMs.miniJaja.ast.ASTNode;
import org.groupe2.projetVMs.miniJaja.ast.expr.ASTIdent;
import org.groupe2.projetVMs.miniJaja.ast.expr.ASTExpr;
import org.groupe2.projetVMs.miniJaja.ast.type.ASTTypeMeth;

/**
 * Abstract base class for variable declarations in MiniJaja
 */
public abstract class ASTVar extends ASTNode {
	protected ASTIdent ident;
	protected ASTExpr expr;

	/**
	 * Constructor an AST variable node
	 *
	 * @param ident the identifier fo the variable
	 * @param expr the initialization expression
	 * @param line the line number of the variable declaration
	 * @param column the column number of the variable declaration
	 */
	protected ASTVar(ASTIdent ident, ASTExpr expr, int line, int column) {
		super(line, column);
		this.ident = ident;
		this.expr = expr;
	}
}
