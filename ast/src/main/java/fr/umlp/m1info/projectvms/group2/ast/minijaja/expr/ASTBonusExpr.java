package fr.umlp.m1info.projectvms.group2.ast.minijaja.instr;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTExpr;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instrs.ASTInstrs;

/**
 * TODO: javadoc
 */
public class ASTBonusExpr extends ASTExpr {

	//TODO: javadoc
	public enum Op {
		JACQUIN,
		BOUQUET;

		@Override
		public String toString() {
			return name().toLowerCase();
		}
	}
	private Op operation;
	private ASTExpr expr1;
	private ASTExpr expr2;

	
	/**
	 * TODO: javadoc
	 *
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
	 * TODO: javadoc
	 */
	public Op getOp() {
		return operation;
	}


	/**
	 * TODO: javadoc
	 */
	public ASTExpr getExpr1() {
		return expr1;
	}

	/**
	 * TODO: javadoc
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
