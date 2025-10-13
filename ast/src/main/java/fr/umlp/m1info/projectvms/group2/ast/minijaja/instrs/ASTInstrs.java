package fr.umlp.m1info.projectvms.group2.ast.minijaja.instrs;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.ASTNode;
/**
 * Represents a sequence of instructions or an empty instruction
 */
public abstract class ASTInstrs extends ASTNode{

	/**
	 * Constructs a new ASTInstrs node
	 *
	 * @param line the source code line of the node
	 * @param column the source code column of the node
	 */
	public ASTInstrs(int line, int column) {
		super(line, column);
	}
}
