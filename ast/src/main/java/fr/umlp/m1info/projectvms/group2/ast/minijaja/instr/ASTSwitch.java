package fr.umlp.m1info.projectvms.group2.ast.minijaja.instr;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTExpr;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.cases.ASTCases;

/**
 * Represents a switch instruction in the MiniJaja language
 */
public class ASTSwitch extends ASTInstr {
	private ASTExpr expr;
	private ASTCases cases;

	/**
	 * Constructs a new ASTSwitch node
	 *
	 * @param expr   the expression evaluated by the switch
	 * @param cases  the cases associated with the switch instruction
	 * @param line   the line number in the source code
	 * @param column the column number in the source code
	 */
	public ASTSwitch(ASTExpr expr, ASTCases cases, int line, int column) {
		super(line, column);
		if (expr == null) {
			throw new NullPointerException("expr cannot be null");
		} else if (cases == null) {
			throw new NullPointerException("cases cannot be null");
		}
		this.expr = expr;
		this.cases = cases;
	}

	public ASTExpr getExpr() {
		return expr;
	}

	public ASTCases getCases() {
		return cases;
	}

	/**
	 * Returns a string representation of this switch instruction
	 *
	 * @return a string representation of the switch instruction
	 */
	@Override
	public String toString() {
		return "switch(" + expr + ") {\n" + cases + "}";
	}
}
