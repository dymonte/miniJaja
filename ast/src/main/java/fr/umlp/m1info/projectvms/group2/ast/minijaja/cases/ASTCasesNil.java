package fr.umlp.m1info.projectvms.group2.ast.minijaja.cases;

/**
 * Represents an empty sequence of case declarations in MiniJaja
 */
public class ASTCasesNil extends ASTCases {

	/**
	 * Constructs a ASTCasesNil node
	 *
	 * @param line   the line number of the nil declaration
	 * @param column the column number of the nil declaration
	 */
	public ASTCasesNil(int line, int column) {
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
