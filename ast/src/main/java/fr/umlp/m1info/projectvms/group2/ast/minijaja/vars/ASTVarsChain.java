package fr.umlp.m1info.projectvms.group2.ast.minijaja.vars;

import fr.umlp.m1info.projectvms.group2.utils.Chain;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.var.ASTVar;

/**
 * Represents a chain of variable declarations in MiniJaja
 */
public class ASTVarsChain extends ASTVars {
	private Chain<ASTVar, ASTVars> chain;

	/**
	 * Constructs a new ASTVarsChain node
	 *
	 * @param var    the first variable declaration
	 * @param vars   the rest of the variable declarations
	 * @param line   the source code line of this node
	 * @param column the source code column of this node
	 */
	public ASTVarsChain(ASTVar var, ASTVars vars, int line, int column) {
		super(line, column);
		chain = new Chain(var, vars);
	}

	/**
	 * Returns a string representation of the variable declarations in MiniJaja
	 * syntax
	 *
	 * @return formatted string of this AST node
	 */
	@Override
	public String toString() {
		return chain.toString(";");
	}
}
