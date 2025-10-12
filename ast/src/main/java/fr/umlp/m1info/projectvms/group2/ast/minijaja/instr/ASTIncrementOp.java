package fr.umlp.m1info.projectvms.group2.ast.minijaja.instr;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTLValue;
/**
 * Represents a unary increment or decrement instruction in the MiniJaja AST
 */
public class ASTIncrementOp extends ASTInstr {
	private ASTLValue lvalue;
	private IncrementOp op;

	/**
	 * Constructs a new ASTIncrementOp node
	 *
	 * @param lvalue the target variable
	 * @param op the compound operator
	 * @param line the source code line of this node
	 * @param column the source code column of this node
	 */
	public ASTIncrementOp(ASTLValue lvalue, IncrementOp op, int line, int column) {
		super(line, column);
		this.lvalue = lvalue;
		this.op = op;
	}

	/**
	 * Returns a string representation of this increment in MiniJaja
	 *
	 * @return a formatted string
	 */
	public String toString() {
		return "" + lvalue + op;
	}
}
