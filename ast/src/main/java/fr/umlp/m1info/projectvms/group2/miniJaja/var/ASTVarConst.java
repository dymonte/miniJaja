package fr.umlp.m1info.projectvms.group2.ast.minijaja.var;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.type.ASTType;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTIdent;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTExpr;

/**
 * Represents a constant (final) variable declaration in MiniJaja
 */
public class ASTVarConst extends ASTVar {
	private ASTType type;

	/**
	 * Constructs a constant variable declaration
	 *
	 * @param type   the type of the variable
	 * @param ident  the identifier of the varaible
	 * @param expr   the initialization expression
	 * @param line   the line number of the constant declaration
	 * @param column the column number of the constant declaration
	 */
	public ASTVarConst(ASTType type, ASTIdent ident, ASTExpr expr, int line, int column) {
		super(ident, expr, line, column);
		this.type = type;
	}

	/**
	 * Returns a string representation of this constant variable declaration in
	 * MiniJaja syntax
	 *
	 * @return formatted string of constant declaration
	 */
	@Override
	public String toString() {
		return "final" + (type != null ? type : "") + " "
				+ (ident != null ? ident : "") + " "
				+ (expr != null ? expr : "");
	}
}
