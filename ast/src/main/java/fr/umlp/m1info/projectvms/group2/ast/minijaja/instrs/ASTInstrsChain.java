package fr.umlp.m1info.projectvms.group2.ast.minijaja.instrs;

import fr.umlp.m1info.projectvms.group2.utils.Chain;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.ASTInstr;

/**
 * Represents a chain of instructions in MiniJaja
 */
public class ASTInstrsChain extends ASTInstrs {
	private Chain<ASTInstr, ASTInstrs> chain;

	/**
	 * Constructs a new ASTInstrsChain node
	 *
	 * @param instr  the first instruction
	 * @param instrs the rest of the instructions
	 * @param line   the source code line of this node
	 * @param column the source code column of this node
	 * @throws NullPointerException if instr or instrs in null
	 */
	public ASTInstrsChain(ASTInstr instr, ASTInstrs instrs, int line, int column) {
		super(line, column);
		if (instr == null) {
			throw new NullPointerException("instr cannot be null");
		} else if (instrs == null) {
			throw new NullPointerException("instrs cannot be null");
		}
		chain = new Chain<>(instr, instrs);
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
