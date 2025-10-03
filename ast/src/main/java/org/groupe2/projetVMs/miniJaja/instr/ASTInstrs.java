package org.groupe2.projetVMs.miniJaja.ast.instr;

import org.groupe2.projetVMs.miniJaja.ast.ASTNode;

/**
 * Represents a sequence of instructions in MiniJaja
 *
 * This class is recursive
 */
public class ASTInstrs extends ASTNode {
	private ASTInstr instr;
	private ASTInstrs instrs;

	/**
	 * Constructs an ASTVars node
	 *
	 * @param var the first variable declaration
	 * @param vars the rest of the variable declaration
	 * @param line the source code line of this node
	 * @param column the source code column of this node
	 */
	public ASTInstrs(ASTInstr instr, ASTInstrs instrs, int line, int column) {
		super(line, column);
		this.instr = instr;
		this.instrs = instrs;
	}

	/**
	 * Returns a string representation of the variable declarations in MiniJaja syntax
	 *
	 * @return formatted stirng of this AST node
	 */
	@Override
	public String toString() {
		return (instr != null ? instr : "") 
				+ ";\n" 
				+ (instrs != null ? instrs : "");
	}
}
