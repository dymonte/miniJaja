package fr.umlp.m1info.projectvms.group2.ast.minijaja.var;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.type.ASTType;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTIdent;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTVExpr;

/**
 * Represents a constant (final) variable declaration in MiniJaja
 */
public class ASTVarConst extends ASTVar {
	private ASTType type;
	private ASTVExpr vexpr;

	/**
	 * Constructs a constant variable declaration
	 *
	 * @param type   the type of the variable
	 * @param ident  the identifier of the varaible
	 * @param vexpr  the initialization expression
	 * @param line   the line number of the constant declaration
	 * @param column the column number of the constant declaration
	 */
	public ASTVarConst(ASTType type, ASTIdent ident, ASTVExpr vexpr, int line, int column) {
		super(ident, line, column);
		if (type == null) {
			throw new NullPointerException("type cannot be null");
		}
		this.type = type;
		this.vexpr = vexpr;
	}

	/**
	 * Returns a string representation of this constant variable declaration in
	 * MiniJaja syntax
	 *
	 * @return formatted string of constant declaration
	 */
	@Override
	public String toString() {
		return "final " + type + " " + ident + (vexpr != null ? (" " + vexpr) : "");
	}
}
