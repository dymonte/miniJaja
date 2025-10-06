package fr.umlp.m1info.projectvms.group2.ast.minijaja.instr;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.params.ASTParams;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTIdent;

/**
 * Represents a method call from instruction in the MiniJaja abstract syntax tee (AST)
 */
public class ASTMethodCallInstr extends ASTInstr {
	private ASTIdent ident;
	private ASTParams params;
	
	/**
	 * Constructs a new ASTMethodCallInstr node
	 *
	 * @param ident the identifier of the method being called
	 * @param params the list of parameters passed to the method
	 * @param line the source code line where this node appears
	 * @param column the source code column where this node begins
	 */
	public ASTMethodCallInstr(ASTIdent ident, ASTParams params, int line, int column) {
		super(line, column);
		this.ident = ident;
		this.params = params;
	}

	/**
	 * Returns a string representation of this method call
	 *
	 * @return a formatted string
	 */
	@Override
	public String toString() {
		return ident + "(" + params + ")";
	}
}
