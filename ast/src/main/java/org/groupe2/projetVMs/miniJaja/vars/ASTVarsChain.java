package org.groupe2.projetVMs.miniJaja.ast.vars;

import org.groupe2.projetVMs.miniJaja.ast.var.ASTVar;

/**
 * Represents a chain of variable declarations in MiniJaja
 */
public class ASTVarsChain extends ASTVars {
	private ASTVar var;
	private ASTVars vars;

	/**
	 * Constructs a new ASTVarsChain node
	 *
	 * @param var the first variable declaration
	 * @param vars the rest of the variable declarations
	 * @param line the source code line of this node
	 * @param column the source code column of this node
	 */
	public ASTVarsChain(ASTVar var, ASTVars vars, int line, int column) {
		super(line, column);
		this.var = var;
		this.vars = vars;
	}	

	/**
	 * Returns a string representation of the variable declarations in MiniJaja syntax
	 *
	 * @return formatted string of this AST node
	 */
	@Override
	public String toString() {
		return (var != null ? var : "") 
				+ ";\n" 
				+ (vars != null ? vars : "");
	}
}
