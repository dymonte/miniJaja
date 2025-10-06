package org.groupe2.projetVMs.miniJaja.ast.expr;

import org.groupe2.projetVMs.miniJaja.ast.ASTNode;
import org.groupe2.projetVMs.miniJaja.ast.exprs.ASTExprs;

public abstract class ASTExpr extends ASTNode implements ASTExprs {
	public ASTExpr(int line, int column) {
		super(line, column);
	}

	@Override
	public String toString() {
		return "";
	}
}
