package org.groupe2.projetVMs.miniJaja.ast.decl;

import org.groupe2.projetVMs.miniJaja.ast.ASTNode;
import org.groupe2.projetVMs.miniJaja.ast.params.ASTParams;
import org.groupe2.projetVMs.miniJaja.ast.type.ASTType;
import org.groupe2.projetVMs.miniJaja.ast.expr.ASTIdent;

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
