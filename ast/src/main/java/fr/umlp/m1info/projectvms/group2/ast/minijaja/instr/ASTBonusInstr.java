package fr.umlp.m1info.projectvms.group2.ast.minijaja.instr;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTExpr;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instrs.ASTInstrs;

/**
 * Represents bonuses instructions in the MiniJaja AST.
 * By bonuses we meant instructions that were not asked but were added
 * to make the project more original.
 *
 * Some other bonuses are not in this file such as ternary Operator, 
 * bonuses expressions, for and switch instruction, else if ...
 *
 * This file contains two bonus operations : chevrot(e) and dugois(e) that acts 
 * respectively like an assert and a sleep functions.
 */
public class ASTBonusInstr extends ASTInstr {

	// This enum describes the operation this node is
	public enum Op {
		CHEVROT, // performs an assertion
		DUGOIS; // performs a sleep on the execution thread

		@Override
		public String toString() {
			return name().toLowerCase();
		}
	}

	private Op operation;
	private ASTExpr expr;

	
	/**
	 * Constructs a new ASTBonusInstr node
	 *
	 * @param op the operation kind
	 * @param expr the expression
	 * @param line   the source code line where this node appears
	 * @param column the source code column where this node appears
	 */
	public ASTBonusInstr(Op operation, ASTExpr expr, int line, int column) {
		super(line, column);
		if (operation == null) {
			throw new NullPointerException("operation cannot be null");
		} else if (expr == null) {
			throw new NullPointerException("expr cannot be null");
		}
		this.operation = operation;
		this.expr = expr;
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
	 * Returns the expression of this node
	 *
	 * @return this node's expression
	 */
	public ASTExpr getExpr() {
		return expr;
	}

	/**
	 * Returns a string representation of this while loop in MiniJaja syntax
	 *
	 * @return a formatted string
	 */
	@Override
	public String toString() {
		return "" + operation + "(" + expr + ")";

	}

}
