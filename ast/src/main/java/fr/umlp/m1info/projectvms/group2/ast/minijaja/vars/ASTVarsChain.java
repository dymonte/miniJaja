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
	 * @throws NullPointerException if var or vars is null;
	 */
	public ASTVarsChain(ASTVar var, ASTVars vars, int line, int column) {
		super(line, column);
		if (var == null) {
			throw new NullPointerException("var cannot be null");
		} else if (vars == null) {
			throw new NullPointerException("vars cannot be null");
		}
		chain = new Chain<ASTVar, ASTVars>(var, vars);
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
