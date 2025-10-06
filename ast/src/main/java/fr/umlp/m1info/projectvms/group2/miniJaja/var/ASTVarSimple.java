package fr.umlp.m1info.projectvms.group2.ast.minijaja.var;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.type.ASTTypeMeth;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTIdent;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTExpr;

/**
 * Represents a simple variable declaration in MiniJaja
 */
public class ASTVarSimple extends ASTVar {
	private ASTTypeMeth type;

	/**
	 * Constructors a simple variable declaration
	 *
	 * @param type   the type of the variable
	 * @param ident  the identifier of the variable
	 * @param expr   the initialization expression
	 * @param line   the line number of the class declaration
	 * @param column the column number of the class declaration
	 */
	public ASTVarSimple(ASTTypeMeth type, ASTIdent ident, ASTExpr expr, int line, int column) {
		super(ident, expr, line, column);
		this.type = type;
	}

	/**
	 * Returns a string representaion of this variable declaration in MiniJaja
	 * syntax
	 *
	 * @return the formatted string of type, identifier, and optional expression
	 */
	@Override
	public String toString() {
		return (type != null ? type : "") + " "
				+ (ident != null ? ident : "") + " "
				+ (expr != null ? expr : "");
	}
}
