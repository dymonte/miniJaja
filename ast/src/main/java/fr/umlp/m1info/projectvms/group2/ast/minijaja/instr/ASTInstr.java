package fr.umlp.m1info.projectvms.group2.ast.minijaja.instr;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.ASTNode;

/**
 * Represents an abstract instruction node in the MiniJaja abstract syntax tree
 */
public abstract class ASTInstr extends ASTNode {

	/**
	 * Constructs a new abstract instruction node
	 *
	 * @param line   the source code line where this instruction apprears
	 * @param column the source code column where this instruction appears
	 */
	protected ASTInstr(int line, int column) {
		super(line, column);
	}
}
