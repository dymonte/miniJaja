package fr.umlp.m1info.projectvms.group2.miniJaja.ast.decls;

import fr.umlp.m1info.projectvms.group2.miniJaja.ast.ASTChain;
import fr.umlp.m1info.projectvms.group2.miniJaja.ast.decl.ASTDecl;

/**
 * Represents a chain of declarations in MiniJaja
 */
public class ASTDeclsChain extends ASTChain<ASTDecl, ASTDecls> implements ASTDecls {
	/**
	 * Constructs a new ASTDeclsChain node
	 *
	 * @param decl the first declaration
	 * @param decls the rest of the declarations
	 * @param line the source code line of this node
	 * @param column the source code column of this node
	 */
	public ASTDeclsChain(ASTDecl decl, ASTDecls decls, int line, int column) {
		super(decl, decls, line, column);
	}

	/**
	 * Returns a string representation of the declarations in MiniJaja syntax
	 *
	 * @return formatted string of this AST node
	 */
	@Override
	public String toString() {
		return toString(";");
	}
}