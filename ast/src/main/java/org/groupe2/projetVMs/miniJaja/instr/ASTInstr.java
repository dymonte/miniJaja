package org.groupe2.projetVMs.miniJaja.ast.instr;

import org.groupe2.projetVMs.miniJaja.ast.ASTNode;

public abstract class ASTInstr extends ASTNode {
	protected ASTInstr(int line, int column) {
		super(line, column);
	}
}
