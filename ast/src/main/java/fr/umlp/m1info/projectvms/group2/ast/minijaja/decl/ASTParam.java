package fr.umlp.m1info.projectvms.group2.ast.minijaja.decl;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.params.ASTParams;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.type.ASTType;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTIdent;

public class ASTParam extends ASTParams {
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
