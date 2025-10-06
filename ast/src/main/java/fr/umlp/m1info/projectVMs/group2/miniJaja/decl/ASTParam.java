package fr.umlp.m1info.projectVMs.group2.miniJaja.ast.decl;

import fr.umlp.m1info.projectVMs.group2.miniJaja.ast.ASTNode;
import fr.umlp.m1info.projectVMs.group2.miniJaja.ast.params.ASTParams;
import fr.umlp.m1info.projectVMs.group2.miniJaja.ast.type.ASTType;
import fr.umlp.m1info.projectVMs.group2.miniJaja.ast.expr.ASTIdent;

public class ASTParam extends ASTNode implements ASTParams {
	private ASTType type;
	private ASTIdent ident;

	public ASTParam(ASTType type, ASTIdent ident, int line, int column) {
		super(line, column);
		this.type = type;
		this.ident = ident;
	}

	@Override
	public String toString() {
		return "" + type + ident;
	}
}
