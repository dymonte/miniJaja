package fr.umlp.m1info.projectvms.group2.ast.minijaja.expr;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.instrs.ASTInstrs;

/**
 * Represents bonuses expressions in the MiniJaja AST.
 * By bonuses we meant instructions that were not asked but were added
 * to make the project more original.
 *
 * Some other bonuses are not in this file such as ternary Operator, 
 * bonuses instructions, for and switch instruction, else if ...
 *
 * This file contains two bonus operations : jacquin(x, y) and bouquet(x, y) that acts
 * respectively like a pow and randomInt (in range [x, y[) functions.
 */
public class ASTBonusExpr extends ASTExpr {

	// This enum describes the operation this node is
	public enum Op {
		JACQUIN, // pow function (x^y)
		BOUQUET; // random integer function (in range [x, y[)

		@Override
		public String toString() {
			return name().toLowerCase();
		}
	}
	private Op operation;
	private ASTExpr expr1;
	private ASTExpr expr2;

	
	/**
	 * Constructs a new ASTBonusExpr node
	 *
	 * @param op the operation kind
	 * @param expr1 the first expression
	 * @param expr2 the second expression
	 * @param line   the source code line where this node appears
	 * @param column the source code column where this node appears
	 */
	public ASTBonusExpr(Op operation, ASTExpr expr1, ASTExpr expr2, int line, int column) {
		super(line, column);
		if (operation == null) {
			throw new NullPointerException("operation cannot be null");
		} else if (expr1 == null) {
			throw new NullPointerException("expr1 cannot be null");
		} else if (expr2 == null) {
			throw new NullPointerException("expr2 cannot be null");
		}
		this.operation = operation;
		this.expr1 = expr1;
		this.expr2 = expr2;
	}

	/**
	 * Returns the operation this node is
	 *
	 * @return the operation representing this node
	 */
	public Op getOp() {
		return operation;
	}

	/**
	 * Returns the first expression of this node
	 *
	 * @return this node's first expression
	 */
	public ASTExpr getExpr1() {
		return expr1;
	}

	/**
	 * Returns the second expression of this node
	 *
	 * @return this node's second expression
	 */
	public ASTExpr getExpr2() {
		return expr2;
	}

	/**
	 * Returns a string representation of this while loop in MiniJaja syntax
	 *
	 * @return a formatted string
	 */
	@Override
	public String toString() {
		return "" + operation + "(" + expr1 + ", " + expr2 + ")";

	}

}
