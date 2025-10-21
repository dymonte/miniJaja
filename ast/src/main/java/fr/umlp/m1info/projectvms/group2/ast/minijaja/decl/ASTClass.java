package fr.umlp.m1info.projectvms.group2.ast.minijaja.decl;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.ASTNode;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTIdent;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decls.ASTDecls;

/**
 * Represents a class declaration node in the Abstract Syntax Tree (AST)
 *
 * An ASTClass corresponds to a MiniJaja class definition
 */
public class ASTClass extends ASTNode {
	private static ASTIdent ident;
	private ASTDecls decls;
	private ASTMainMethod mainMethod;

	/**
	 * Constructs a new ASTClass node
	 *
	 * @param ident      the class identifier
	 * @param decls      the declarations contained in the class
	 * @param mainMethod the main method of the class
	 * @param line       the line number of the class declaration
	 * @param column     the column number of the class declaration
	 * @throws NullPointerException if ident, decls or mainMethod is null
	 */
	public ASTClass(ASTIdent ident, ASTDecls decls, ASTMainMethod mainMethod, int line, int column) {
		super(line, column);
		if (ident == null) {
			throw new NullPointerException("ident cannot be null");
		} else if (decls == null) {
			throw new NullPointerException("decls cannot be null");
		} else if (mainMethod == null) {
			throw new NullPointerException("mainMethod cannot be null");
		}
		ASTClass.ident = ident;
		this.decls = decls;
		this.mainMethod = mainMethod;
	}

	/**
	 * Returns the identifier of the current MiniJaja class as a static reference
	 *
	 * @return the ASTIdent representing the class identifier
	 */
	public static ASTIdent getClassVar() {
		return ident;
	}

	/**
	 * Returns the identifier of this class
	 *
	 * @return the ASTIdent representing the name of this class
	 */
	public ASTIdent getIdent() {
		return ASTClass.getClassVar();
	}

	/**
	 * Returns the declaration contained within this class
	 *
	 * @return the ASTDecls node representing the class declaration
	 */
	public ASTDecls getDecls() {
		return decls;
	}

	/**
	 * Returns the main method associated with this class
	 *
	 * @return the ASTMainMethod node representing the main method
	 */
	public ASTMainMethod getMainMethod() {
		return mainMethod;
	}

	/**
	 * Returns a string representation of the class in MiniJaja syntax
	 *
	 * @return the class as a formatted string
	 */
	@Override
	public String toString() {
		return "class " + ident + " {\n" + decls + mainMethod + "\n}";
	}
}
