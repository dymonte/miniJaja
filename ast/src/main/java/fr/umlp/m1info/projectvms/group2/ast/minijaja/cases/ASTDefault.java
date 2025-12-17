package fr.umlp.m1info.projectvms.group2.ast.minijaja.cases;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.instrs.ASTInstrs;

/**
 * Represents the default case in a MiniJaja
 */
public class ASTDefault extends ASTCaseGroup {
	private ASTInstrs instrs;

	/**
	 * Constructs a new ASTDefault node
	 *
	 * @param instrs the instructions to execute for the default node
	 * @param line   the line number in the code source
	 * @param column the column number in the code source
	 * @throws NullPointerException if instrs is null
	 */
	public ASTDefault(ASTInstrs instrs, int line, int column) {
		super(line, column);
		if (instrs == null) {
			throw new NullPointerException("instrs cannot be null");
		}
		this.instrs = instrs;
	}

	public ASTInstrs getInstrs() {
		return instrs;
	}

	/**
	 * Returns a string representation of this default case in MiniJaja syntax
	 *
	 * @return a string representation of the default case
	 */
	@Override
	public String toString() {
		return "default: " + instrs;
	}
}
