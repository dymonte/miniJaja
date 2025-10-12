package fr.umlp.m1info.projectvms.group2.ast.minijaja.instrs;

/**
 * Represents an empty sequence of variable declarations in MiniJaja
 */
public class ASTInstrsNil extends ASTInstrs {
	/**
	 * Constructs an empty instruction sequence
	 *
	 * @param line   the line number of the nil declaration
	 * @param column the column number of the nil declaration
	 */
	public ASTInstrsNil(int line, int column) {
		super(line, column);
	}

	/**
	 * Returns an empty string
	 *
	 * @return an empty string
	 */
	@Override
	public String toString() {
		return "";
	}
}
