package fr.umlp.m1info.projectvms.group2.ast.minijaja.var;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.type.ASTTypeMeth;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTIdent;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTVExpr;

/**
 * Represents a simple variable declaration in MiniJaja
 */
public class ASTVarSimple extends ASTVar {
	private ASTTypeMeth type;
	private ASTVExpr vexpr;

	/**
	 * Constructors a simple variable declaration
	 *
	 * @param type   the type of the variable
	 * @param ident  the identifier of the variable
	 * @param vexpr  the initialization expression
	 * @param line   the line number of the class declaration
	 * @param column the column number of the class declaration
	 * @throws NullPointerException if type is null
	 */
	public ASTVarSimple(ASTTypeMeth type, ASTIdent ident, ASTVExpr vexpr, int line, int column) {
		super(ident, line, column);
		if (type == null) {
			throw new NullPointerException("type cannot be null");
		}
		this.type = type;
		this.vexpr = vexpr;
	}

	/**
	 * Returns the type of the variable
	 *
	 * @return the ASTTypeMeth representing the variable type
	 */
	public ASTTypeMeth getType() {
		return type;
	}

	/**
	 * Returns the identifier of the variable
	 *
	 * @return the ASTIdent representing the variable name
	 */
	public ASTIdent getIdent() {
		return ident;
	}

	/**
	 * Returns the initialization expression of the variable
	 *
	 * @return the ASTVExpr representing the initialization, may be null
	 */
	public ASTVExpr getVExpr() {
		return vexpr;
	}

	/**
	 * Returns a string representaion of this variable declaration in MiniJaja
	 * syntax
	 *
	 * @return the formatted string of type, identifier, and optional expression
	 */
	@Override
	public String toString() {
		return type + " " + ident + (vexpr != null ? (" " + vexpr) : "");
	}
}
