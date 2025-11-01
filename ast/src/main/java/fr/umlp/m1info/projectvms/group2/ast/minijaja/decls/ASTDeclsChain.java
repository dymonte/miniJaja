package fr.umlp.m1info.projectvms.group2.ast.minijaja.decls;

import fr.umlp.m1info.projectvms.group2.utils.Chain;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decl.ASTDecl;

/**
 * Represents a chain of declarations in MiniJaja
 */
public class ASTDeclsChain extends ASTDecls {
	private Chain<ASTDecl, ASTDecls> chain;

	/**
	 * Constructs a new ASTDeclsChain node
	 *
	 * @param decl   the first declaration
	 * @param decls  the rest of the declarations
	 * @param line   the source code line of this node
	 * @param column the source code column of this node
	 * @throws NullPointerException if decl or decls is null
	 */
	public ASTDeclsChain(ASTDecl decl, ASTDecls decls, int line, int column) {
		super(line, column);
		if (decl == null) {
			throw new NullPointerException("decl cannot be null");
		} else if (decls == null) {
			throw new NullPointerException("decls cannot be null");
		}
		chain = new Chain<>(decl, decls);
	}

	/**
	 * Returns the first declaration in this chain
	 *
	 * @return the ASTDecl node representing the first declaration in the chain
	 */
	public ASTDecl getDecl() {
		return chain.getLeft();
	}

	/**
	 * Returns the reset of the declarations in this chain
	 *
	 * @return the ASTDecls node representing the remaining declarations in the
	 *         chain
	 */
	public ASTDecls getDecls() {
		return chain.getRight();
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
