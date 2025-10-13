package fr.umlp.m1info.projectvms.group2.ast.minijaja.decls;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.Chain;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decl.ASTDecl;

/**
 * Represents a chain of declarations in MiniJaja
 */
public class ASTDeclsChain extends ASTDecls {
	private Chain<ASTDecl, ASTDecls> chain;

	/**
	 * Constructs a new ASTDeclsChain node
	 *
	 * @param decl the first declaration
	 * @param decls the rest of the declarations
	 * @param line the source code line of this node
	 * @param column the source code column of this node
	 */
	public ASTDeclsChain(ASTDecl decl, ASTDecls decls, int line, int column) {
		super(line, column);
		chain = new Chain(decl, decls);
	}

	/**
	 * Returns a string representation of the declarations in MiniJaja syntax
	 *
	 * @return formatted string of this AST node
	 */
	@Override
	public String toString() {
		return chain.toString(";");
	}
}
