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
	 */
	public ASTMainMethod(ASTVars vars, ASTInstrs instrs, int line, int column) {
		super(line, column);
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
		return "main " + "{\n"
				+ (vars != null ? vars : "")
				+ (instrs != null ? instrs : "")
				+ "}";
	}
}
