package fr.umlp.m1info.projectvms.group2.ast.minijaja.params;

/**
 * Represents an empty sequence of parameter declarations in MiniJaja
 */
public class ASTParamsNil extends ASTParams {
	/**
	 * Constructs an empty parameter declaration sequence
	 *
	 * @param line the line number of the nil declaration
	 * @param column the column number of the nil declaration
	 */
	public ASTParamsNil(int line, int column) {
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
