package org.groupe2.projetVMs.miniJaja.ast.instrs;

import org.groupe2.projetVMs.miniJaja.ast.ASTChain;
import org.groupe2.projetVMs.miniJaja.ast.instr.ASTInstr;

/**
 * Represents a chain of instructions in MiniJaja
 */
public class ASTInstrsChain extends ASTChain<ASTInstr, ASTInstrs> implements ASTInstrs {
	/**
	 * Constructs a new ASTInstrsChain node
	 *
	 * @param var the first instruction
	 * @param vars the rest of the instructions
	 * @param line the source code line of this node
	 * @param column the source code column of this node
	 */
	public ASTInstrsChain(ASTInstr instr, ASTInstrs instrs, int line, int column) {
		super(instr, instrs, line, column);
	}

	/**
	 * Returns a string representation of the instructions in MiniJaja syntax
	 *
	 * @return formatted string of this AST node
	 */
	@Override
	public String toString() {
		return toString(";");
	}
}
