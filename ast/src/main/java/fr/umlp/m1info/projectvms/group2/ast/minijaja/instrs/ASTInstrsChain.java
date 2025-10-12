package fr.umlp.m1info.projectvms.group2.ast.minijaja.instrs;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.Chain;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.ASTInstr;

/**
 * Represents a chain of instructions in MiniJaja
 */
public class ASTInstrsChain extends ASTInstrs {
	private Chain<ASTInstr, ASTInstrs> chain;

	/**
	 * Constructs a new ASTInstrsChain node
	 *
	 * @param instr    the first instruction
	 * @param instrs   the rest of the instructions
	 * @param line   the source code line of this node
	 * @param column the source code column of this node
	 */
	public ASTInstrsChain(ASTInstr instr, ASTInstrs instrs, int line, int column) {
		super(line, column);
		chain = new Chain(instr, instrs);
	}

	/**
	 * Returns a string representation of the instructions in MiniJaja syntax
	 *
	 * @return formatted string of this AST node
	 */
	@Override
	public String toString() {
		return chain.toString(";\n");
	}
}
