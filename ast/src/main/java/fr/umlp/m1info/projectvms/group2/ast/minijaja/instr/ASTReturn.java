package fr.umlp.m1info.projectvms.group2.ast.minijaja.instr;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTExpr;

/**
 * Represents a return instruction in the MiniJaja AST
 */
public class ASTReturn extends ASTInstr {
	ASTExpr expr;

	/**
	 * Constructs a new ASTReturn node
	 *
	 * @param expr   the expression to return
	 * @param line   the source code line of this node
	 * @param column the source code column of this node
	 * @throws NullPointerException if expr is null
	 */
	public ASTReturn(ASTExpr expr, int line, int column) {
		super(line, column);
		if (expr == null) {
			throw new NullPointerException("expr cannot be null");
		}
		this.expr = expr;
	}

	/**
	 * Returns a string representation of the return instruction
	 *
	 * @return a formatted string
	 */
	public String toString() {
		return "return " + expr;
	}
}
