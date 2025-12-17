package fr.umlp.m1info.projectvms.group2.ast.minijaja.cases;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTExpr;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instrs.ASTInstrs;

/**
 * Represents a single case in MiniJaja
 */
public class ASTCase extends ASTCaseGroup {
	private ASTExpr expr;
	private ASTInstrs instrs;

	/**
	 * Constructs a new ASTCase node
	 *
	 * @param expr   the expression defining the case value
	 * @param instrs the instructions to execute for this case
	 * @param line   the line number in the source code
	 * @param column the column number in the source code
	 * @throws NullPointerException if expr or instrs are null
	 */
	public ASTCase(ASTExpr expr, ASTInstrs instrs, int line, int column) {
		super(line, column);
		if (expr == null) {
			throw new NullPointerException("expr cannot be null");
		} else if (instrs == null) {
			throw new NullPointerException("instrs cannot be null");
		}
		this.expr = expr;
		this.instrs = instrs;
	}

	public ASTExpr getExpr() {
		return expr;
	}

	public ASTInstrs getInstrs() {
		return instrs;
	}

	/**
	 * Returns a string representation of this case in MiniJaja syntax
	 *
	 * @return a string representation of the case
	 */
	@Override
	public String toString() {
		return "case " + expr + ": " + instrs;
	}
}
