package fr.umlp.m1info.projectvms.group2.ast.minijaja.instr;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTExpr;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTLValue;

/**
 * Represents a compound assignement instruction in the MiniJaja AST
 */
public class ASTAssignmentOp extends ASTInstr {
	private ASTLValue lvalue;
	private ASTExpr expr;
	private AssignmentOp op;

	/**
	 * Constructs a new ASTAssignmentOp node
	 *
	 * @param lvalue the target variable
	 * @param expr   the expression to assing
	 * @param op     the compound operator
	 * @param line   the source code line of this node
	 * @param column the source code column of this node
	 */
	public ASTAssignmentOp(ASTLValue lvalue, ASTExpr expr, AssignmentOp op, int line, int column) {
		super(line, column);
		this.lvalue = lvalue;
		this.expr = expr;
		this.op = op;
	}

	/**
	 * Returns the target variable of the assignement
	 *
	 * @return the ASTLValue representing the variable
	 */
	public ASTLValue getLValue() {
		return lvalue;
	}

	/**
	 * Returns the expression whose value is assigned to the variable
	 *
	 * @return the ASTExpr representing the assigned value
	 */
	public ASTExpr getExpr() {
		return expr;
	}

	/**
	 * Returns the assignment operator
	 *
	 * @return the AssignmentOp used in this assignment
	 */
	public AssignmentOp getOp() {
		return op;
	}

	/**
	 * Returns a string representation of this assignement in MiniJaja
	 *
	 * @return a formatted string
	 */
	public String toString() {
		return lvalue + " " + op + " " + expr;
	}
}
