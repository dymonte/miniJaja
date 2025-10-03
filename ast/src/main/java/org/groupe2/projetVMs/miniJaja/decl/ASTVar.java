package org.groupe2.projetVMs.miniJaja.ast.decl;

import org.groupe2.projetVMs.miniJaja.ast.ASTNode;
import org.groupe2.projetVMs.miniJaja.ast.expr.ASTIdent;

public abstract class ASTVar extends ASTNode {
	protected ASTIdent ident;
	protected ASTTypeMeth type;

	protected ASTVar(ASTIdent ident, ASTTypeMeth type, int line, int column) {
		super(line, column);
		this.ident = ident;
		this.type = type;
	}
}
