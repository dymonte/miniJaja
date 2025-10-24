package fr.umlp.m1info.projectvms.group2.ast.minijaja.var;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.type.ASTTypeMeth;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTIdent;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTExpr;

/**
 * Represents an array variable declaration in MiniJaja
 */
public class ASTVarArray extends ASTVar {
	private ASTTypeMeth type;
	private ASTExpr expr;

	/**
	 * Constructs an array variable declaration
	 *
	 * @param type   the type of the array elements
	 * @param ident  the identifier of the array variable
	 * @param expr   the expression for the array size
	 * @param line   the line number of the array declaration
	 * @param column the column number of the array declaration
	 * @throws NullPointerException if type or expr is null
	 */
	public ASTVarArray(ASTTypeMeth type, ASTIdent ident, ASTExpr expr, int line, int column) {
		super(ident, line, column);
		if (type == null) {
			throw new NullPointerException("type cannot be null");
		} else if (expr == null) {
			throw new NullPointerException("expr cannot be null");
		}
		this.type = type;
		this.expr = expr;
	}

	/**
	 * Returns the tpye of the array elements
	 *
	 * @return the ASTTypeMeth representing the element type
	 */
	public ASTTypeMeth getType() {
		return type;
	}

	/**
	 * Returns the identifier of the variable
	 *
	 * @return the ASTIdent of the array variable
	 */
	public ASTIdent getIdent() {
		return ident;
	}

	/**
	 * Returns the expression representing the size of the array
	 *
	 * @return the ASTExpr representing the array size
	 */
	public ASTExpr getExpr() {
		return expr;
	}

	/**
	 * Returns a string representation of the array variable declaration in MiniJaja
	 * syntax
	 *
	 * @return formatted string of array
	 */
	@Override
	public String toString() {
		return type + " " + ident + "[" + expr + "]";
	}
}
