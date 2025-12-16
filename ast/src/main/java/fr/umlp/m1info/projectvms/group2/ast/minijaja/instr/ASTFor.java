package fr.umlp.m1info.projectvms.group2.ast.minijaja.instr;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.var.ASTVar;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTExpr;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instrs.ASTInstrs;

/**
 * Represents a for loop instruction in the MiniJaja abstract syntax tree (AST)
 */
public class ASTFor extends ASTInstr {
	private ASTVar var;
	private ASTExpr expr;
	private ASTInstr inc;
	private ASTInstrs instrs;

	/**
	 * Constructs a new ASTFor node
	 *
	 * @param var    the var declaration of the for loop
	 * @param expr   the condition expression of the for loop
	 * @param inc    the increment var of the loop
	 * @param instrs the body instructions executed when the condition is true
	 * @param line   the source code line where this node appears
	 * @param column the source code column where this node appears
	 */
	public ASTFor(ASTVar var, ASTExpr expr, ASTInstr inc, ASTInstrs instrs, int line, int column) {
		super(line, column);
		if (instrs == null) {
			throw new NullPointerException("instrs cannot be null");
		}

		this.var = var;
		this.expr = expr;
		this.inc = inc;
		this.instrs = instrs;
	}

	/**
	 * Returns the var declaration of this for loop
	 *
	 * @return the ASTVar of the declaration
	 */
	public ASTVar getVar() {
		return var;
	}

	/**
	 * Returns the condition expression of this for loop
	 *
	 * @return the ASTExpr representing the loop condition
	 */
	public ASTExpr getExpr() {
		return expr;
	}

	/**
	 * Returns the instruction to increment the variable of the loop
	 *
	 * @return the ASTInstr of the increment
	 */
	public ASTInstr getInc() {
		return inc;
	}

	/**
	 * Returns the instructions excecuted in the body of the loop
	 *
	 * @return the ASTInstrs representing the loop body
	 */
	public ASTInstrs getInstrs() {
		return instrs;
	}

	/**
	 * Returns a string representation of this for loop in MiniJaja syntax
	 *
	 * @return a formatted string
	 */
	@Override
	public String toString() {
		String sVar = (var != null ? var.toString() : "");
		String sExpr = (expr != null ? expr.toString() : "");
		String sInc = (inc != null ? inc.toString() : "");
		return "for (" + sVar + ";" + sExpr + ";" + sInc + ") {\n" + instrs + "}";
	}
}
