package fr.umlp.m1info.projectvms.group2.ast.minijaja.instr;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.ASTNode;

public abstract class ASTInstr extends ASTNode {
	protected ASTInstr(int line, int column) {
		super(line, column);
	}
}