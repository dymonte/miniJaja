package org.groupe2.projetVMs.miniJaja.ast.params;

import org.groupe2.projetVMs.miniJaja.ast.ASTChain;
import org.groupe2.projetVMs.miniJaja.ast.decl.ASTParam;

/**
 * Represents a chain of parameter in MiniJaja
 */
public class ASTParamsChain extends ASTChain<ASTParam, ASTParams> implements ASTParams {
	/**
	 * Constructs a new ASTParamsChain node
	 *
	 * @param param the first param declaration
	 * @param params the rest of the params declarations
	 * @param line the source code line of this node
	 * @param column the source code column of this node
	 */
	public ASTParamsChain(ASTParam param, ASTParams params, int line, int column) {
		super(param, params, line, column);
	}

	/**
	 * Returns a string representation of the parameter declarations in MiniJaja syntax
	 *
	 * @return formatted string of this AST node
	 */
	@Override
	public String toString() {
		return toString(",");
	}
}
