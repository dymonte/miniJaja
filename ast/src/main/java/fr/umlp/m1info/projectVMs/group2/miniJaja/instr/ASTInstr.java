package fr.umlp.m1info.projectVMs.group2.miniJaja.ast.instr;

import fr.umlp.m1info.projectVMs.group2.miniJaja.ast.ASTNode;

public abstract class ASTInstr extends ASTNode {
	protected ASTInstr(int line, int column) {
		super(line, column);
	}
}
