package fr.umlp.m1info.projectVMs.group2.miniJaja.ast.decls;

import fr.umlp.m1info.projectVMs.group2.miniJaja.ast.ASTNode;
/**
 * Represents an empty sequence of declaration in MiniJaja
 */
public class ASTDeclsNil extends ASTNode implements ASTDecls {
	/**
	 * Constructs an empty declaration sequence
	 *
	 * @param line the line number of the nil declaration
	 * @param column the column number of the nil declaration
	 */
	public ASTDeclsNil(int line, int column) {
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
