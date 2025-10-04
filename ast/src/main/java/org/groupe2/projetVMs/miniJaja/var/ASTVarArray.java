package org.groupe2.projetVMs.miniJaja.ast.var;

import org.groupe2.projetVMs.miniJaja.ast.type.ASTTypeMeth;
import org.groupe2.projetVMs.miniJaja.ast.expr.ASTIdent;
import org.groupe2.projetVMs.miniJaja.ast.expr.ASTExpr;

/**
 * Represents an array variable declaration in MiniJaja
 */
public class ASTVarArray extends ASTVar {
	private ASTTypeMeth type;

	/**
	 * Constructs an array variable declaration
	 *
	 * @param type the type of the array elements
	 * @param ident the identifier of the array variable
	 * @param expr the expression for the array size
	 * @param line the line number of the array declaration
	 * @param column the column number of the array declaration
	 */
	public ASTVarArray(ASTTypeMeth type, ASTIdent ident, ASTExpr expr, int line, int column) {
		super(ident, expr, line, column);
		this.type = type;
	}

	/**
	 * Returns a string representation of the array variable declaration in MiniJaja syntax
	 *
	 * @return formatted string of array
	 */
	@Override
	public String toString() {
		return (type != null ? type : "") + " "
				+ (ident != null ? ident : "")
				+ "[" + (expr != null ? expr : "") + "]";
	}
}
