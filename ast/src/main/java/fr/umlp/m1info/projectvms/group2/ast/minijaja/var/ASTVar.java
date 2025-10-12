package fr.umlp.m1info.projectvms.group2.ast.minijaja.var;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.decl.ASTDecl;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTIdent;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.type.ASTTypeMeth;

/**
 * Abstract base class for variable declarations in MiniJaja
 */
public abstract class ASTVar extends ASTDecl {
	protected ASTIdent ident;

	/**
	 * Constructor an AST variable node
	 *
	 * @param ident the identifier fo the variable
	 * @param expr the initialization expression
	 * @param line the line number of the variable declaration
	 * @param column the column number of the variable declaration
	 */
	protected ASTVar(ASTIdent ident, int line, int column) {
		super(line, column);
		this.ident = ident;
	}
}
