package fr.umlp.m1info.projectvms.group2.ast.minijaja.instr;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTExpr;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instrs.ASTInstrs;

/**
 * TODO: javadoc
 */
public class ASTBonusInstr extends ASTInstr {

	//TODO: javadoc
	public enum Op {
		CHEVROT,
		DUGOIS;

		@Override
		public String toString() {
			return name().toLowerCase();
		}
	}

	private Op operation;
	private ASTExpr expr;

	
	/**
	 * TODO: javadoc
	 *
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
	 * TODO: javadoc
	 */
	public Op getOp() {
		return operation;
	}


	/**
	 * TODO: javadoc
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
