package fr.umlp.m1info.projectvms.group2.ast.minijaja.expr;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.ASTMethodCallInstr;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.exprs.ASTExprs;

/**
 * Represents a method call from expression in the MiniJaja abstract syntax tee (AST)
 */
public class ASTMethodCallExpr extends ASTMethodCallInstr {
	/**
	 * Constructs a new ASTMethodCallExpr node
	 *
	 * @param ident the identifier of the method being called
	 * @param exprs the list of expression passed to the method
	 * @param line the source code line where this node appears
	 * @param column the source code column where this node begins
	 */
	public ASTMethodCallExpr(ASTIdent ident, ASTExprs exprs, int line, int column) {
		super(ident, exprs, line, column);
	}
}
