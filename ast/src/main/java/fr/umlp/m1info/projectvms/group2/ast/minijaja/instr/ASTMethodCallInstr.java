package fr.umlp.m1info.projectvms.group2.ast.minijaja.instr;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.exprs.ASTExprs;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTIdent;

/**
 * Represents a method call from instruction in the MiniJaja abstract syntax
 * tree (AST)
 */
public class ASTMethodCallInstr extends ASTInstr {
	private ASTIdent ident;
	private ASTExprs exprs;

	/**
	 * Constructs a new ASTMethodCallInstr node
	 *
	 * @param ident  the identifier of the method being called
	 * @param exprs  the list of expressions passed to the method
	 * @param line   the source code line where this node appears
	 * @param column the source code column where this node begins
	 * @throws NullPointerExceptionException if ident or exprs is null
	 */
	public ASTMethodCallInstr(ASTIdent ident, ASTExprs exprs, int line, int column) {
		super(line, column);
		if (ident == null) {
			throw new NullPointerException("ident cannot be null");
		} else if (exprs == null) {
			throw new NullPointerException("exprs cannot be null");
		}
		this.ident = ident;
		this.exprs = exprs;
	}

	/**
	 * Returns a string representation of this method call
	 *
	 * @return a formatted string
	 */
	@Override
	public String toString() {
		return ident + "(" + exprs + ")";
	}
}
