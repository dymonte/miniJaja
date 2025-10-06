package fr.umlp.m1info.projectVMs.group2.miniJaja.ast.expr;

import fr.umlp.m1info.projectVMs.group2.miniJaja.ast.ASTNode;
import fr.umlp.m1info.projectVMs.group2.miniJaja.ast.exprs.ASTExprs;

public abstract class ASTExpr extends ASTNode implements ASTExprs {
	public ASTExpr(int line, int column) {
		super(line, column);
	}

	@Override
	public String toString() {
		return "";
	}
}
