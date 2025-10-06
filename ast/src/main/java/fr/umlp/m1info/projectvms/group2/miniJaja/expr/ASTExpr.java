package fr.umlp.m1info.projectvms.group2.ast.minijaja.expr;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.ASTNode;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.exprs.ASTExprs;

public abstract class ASTExpr extends ASTNode implements ASTExprs {
	public ASTExpr(int line, int column) {
		super(line, column);
	}

	@Override
	public String toString() {
		return "";
	}
}