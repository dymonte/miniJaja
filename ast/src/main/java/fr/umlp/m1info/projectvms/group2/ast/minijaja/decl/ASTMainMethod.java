package fr.umlp.m1info.projectvms.group2.ast.minijaja.decl;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.ASTNode;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instrs.ASTInstrs;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.vars.ASTVars;

/**
 * Represents a the main method node in the Abstract Syntax Tree (AST)
 *
 * An ASTMainMethod corresponds to a MiniJaja main definition
 */
public class ASTMainMethod extends ASTNode {
	private ASTVars vars;
	private ASTInstrs instrs;

	/**
	 * Constructs a new ASTMainMethod node
	 *
	 * @param vars   the variable declarations inside the main method
	 * @param instrs the list of instructions inside the main method
	 * @param line   the line number of the main method declaration
	 * @param column the column number of the main method declaration
	 * @throws NullPointerException if vars or instrs is null
	 */
	public ASTMainMethod(ASTVars vars, ASTInstrs instrs, int line, int column) {
		super(line, column);
		if (vars == null) {
			throw new NullPointerException("vars cannot be null");
		} else if (instrs == null) {
			throw new NullPointerException("intrs cannot be null");
		}
		this.vars = vars;
		this.instrs = instrs;
	}

	/**
	 * Returns a string representation of the class in MiniJaja syntax
	 *
	 * @return the class as a formatted string
	 */
	@Override
	public String toString() {
		return "main " + "{\n" + vars + instrs + "}";
	}
}
