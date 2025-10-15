package fr.umlp.m1info.projectvms.group2.ast.minijaja.instr;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTExpr;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instrs.ASTInstrs;

/**
 * Represents an if-else instruction in the MiniJaja AST
 */
public class ASTIf extends ASTInstr {
	private ASTExpr expr;
	private ASTInstrs ifInstrs;
	private ASTInstrs elseInstrs;

	/**
	 * Constructs a new ASTIf node
	 *
	 * @param expr       the condition expression
	 * @param ifInstrs   the instructions executed if the condition is true
	 * @param elseInstrs the instructions executed if the condition is false
	 * @param line       the source code line of this node
	 * @param column     the source code column of this node
	 * @throws NullPointerException if expr or ifInstrs is null
	 */
	public ASTIf(ASTExpr expr, ASTInstrs ifInstrs, ASTInstrs elseInstrs, int line, int column) {
		super(line, column);
		if (expr == null) {
			throw new NullPointerException("expr cannot be null");
		} else if (ifInstrs == null) {
			throw new NullPointerException("ifInstrs cannot be null");
		}
		this.expr = expr;
		this.ifInstrs = ifInstrs;
		this.elseInstrs = elseInstrs;
	}

	/**
	 * Returns a string representation of this if-else statement
	 * 
	 * @return a formatted string
	 */
	@Override
	public String toString() {
		return "if (" + expr + ") {\n" +
				ifInstrs + "}" + (elseInstrs != null ? " else {\n" +
						elseInstrs + "}" : "");

	}
}
