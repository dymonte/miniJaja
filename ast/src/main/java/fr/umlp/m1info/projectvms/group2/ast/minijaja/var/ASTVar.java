package fr.umlp.m1info.projectvms.group2.ast.minijaja.var;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.decl.ASTDecl;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTIdent;

/**
 * Abstract base class for variable declarations in MiniJaja
 */
public abstract class ASTVar extends ASTDecl {
	protected ASTIdent ident;

	/**
	 * Constructor an AST variable node
	 *
	 * @param ident  the identifier fo the variable
	 * @param line   the line number of the variable declaration
	 * @param column the column number of the variable declaration
	 * @throws IllegalArgumentException if ident is null
	 */
	protected ASTVar(ASTIdent ident, int line, int column) {
		super(line, column);
		if (ident == null) {
			throw new IllegalArgumentException("ident cannot be null");
		}
		this.ident = ident;
	}

	/**
	 * Returns the identifier of the variable
	 *
	 * @return the ASTIdent representing the variable name
	 */
	public ASTIdent getIdent() {
		return ident;
	}
}
