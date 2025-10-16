package fr.umlp.m1info.projectvms.group2.ast.minijaja.params;

import fr.umlp.m1info.projectvms.group2.utils.Chain;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decl.ASTParam;

/**
 * Represents a chain of parameter in MiniJaja
 */
public class ASTParamsChain extends ASTParams {
	private Chain<ASTParam, ASTParams> chain;

	/**
	 * Constructs a new ASTParamsChain node
	 *
	 * @param param  the first param declaration
	 * @param params the rest of the params declarations
	 * @param line   the source code line of this node
	 * @param column the source code column of this node
	 * @throws IllegalArgumentException if param or params in null
	 */
	public ASTParamsChain(ASTParam param, ASTParams params, int line, int column) {
		super(line, column);
		if (param == null) {
			throw new NullPointerException("param cannot be null");
		} else if (params == null) {
			throw new NullPointerException("params cannot be null");
		}
		chain = new Chain<>(param, params);
	}

	/**
	 * Returns a string representation of the parameter declarations in MiniJaja
	 * syntax
	 *
	 * @return formatted string of this AST node
	 */
	@Override
	public String toString() {
		return chain.toString(", ");
	}
}
