package fr.umlp.m1info.projectvms.group2.ast.minijaja.instr;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTExpr;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instrs.ASTInstrs;

/**
 * Represents a while loop instruction in the MiniJaja abstract syntax tree (AST)
 */
public class ASTWhile extends ASTInstr {
	private ASTExpr expr;
	private ASTInstrs instrs;

	/**
	 * Constructs a new ASTWhile node
	 *
	 * @param expr the condition expression of the while loop
	 * @param instrs the body instructions executed when the condition is true
	 * @param line the source code line where this node appears
	 * @param column the source code column where this node appears
	 */
	public ASTWhile(ASTExpr expr, ASTInstrs instrs, int line, int column) {
		super(line, column);
		this.expr = expr;
		this.instrs = instrs;
	}

	/**
	 * Returns a string representation of this while loop in MiniJaja syntax
	 *
	 * @return a formatted string
	 */
	@Override
	public String toString() {
		return "while (" + expr + ") {\n" + instrs + "}\n";
	}
}
