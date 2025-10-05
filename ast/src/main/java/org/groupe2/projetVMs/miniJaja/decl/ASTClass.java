package org.groupe2.projetVMs.miniJaja.ast.decl;

import org.groupe2.projetVMs.miniJaja.ast.ASTNode;
import org.groupe2.projetVMs.miniJaja.ast.expr.ASTIdent;
import org.groupe2.projetVMs.miniJaja.ast.decls.ASTDecls;

/**
 * Represents a class declaration node in the Abstract Syntax Tree (AST)
 *
 * An ASTClass corresponds to a MiniJaja class definition
 */
public class ASTClass extends ASTNode {
	private ASTIdent ident;
	private ASTDecls decls;
	private ASTMainMethod mainMethod;

	/**
	 * Constructs a new ASTClass node
	 *
	 * @param ident the class identifier
	 * @param decls the declarations contained in the class
	 * @param mainMethod the main method of the class
	 * @param line the line number of the class declaration
	 * @param column the column number of the class declaration
	 */
	public ASTClass(ASTIdent ident, ASTDecls decls, ASTMainMethod mainMethod, int line, int column) {
		super(line, column);
		this.ident = ident;
		this.decls = decls;
		this.mainMethod = mainMethod;
	}

	/**
	 * Returns a string representation of the class in MiniJaja syntax
	 *
	 * @return the class as a formatted string
	 */
	@Override
	public String toString() {
		return "class " + ident + " {\n" 
				+ (decls != null ? decls : "") 
				+ (mainMethod != null ? mainMethod : "")
				+ "\n}";
	}
}
