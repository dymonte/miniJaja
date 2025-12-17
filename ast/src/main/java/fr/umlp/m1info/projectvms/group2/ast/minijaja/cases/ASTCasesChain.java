package fr.umlp.m1info.projectvms.group2.ast.minijaja.cases;

import fr.umlp.m1info.projectvms.group2.utils.Chain;

/**
 * Represents a chain of case declarations in MiniJaja
 */
public class ASTCasesChain extends ASTCases {
	private Chain<ASTCaseGroup, ASTCases> chain;

	/**
	 * Constructs a new ASTCasesChain node
	 *
	 * @param c      the first case declaration
	 * @param cases  the rest of the case declarations
	 * @param line   the source code line of this node
	 * @param column the source code column of this node
	 * @throws NullPointerException if c or cases is null
	 */
	public ASTCasesChain(ASTCaseGroup c, ASTCases cases, int line, int column) {
		super(line, column);
		if (c == null) {
			throw new NullPointerException("case cannot be null");
		} else if (cases == null) {
			throw new NullPointerException("cases cannot be null");
		}
		chain = new Chain<>(c, cases);
	}

	public ASTCaseGroup getCase() {
		return chain.getLeft();
	}

	public ASTCases getCases() {
		return chain.getRight();
	}

	/**
	 * Returns a string representation of the case declarations in MIniJaja syntax
	 *
	 * @return formatted string of this AST ndoe
	 */
	@Override
	public String toString() {
		return chain.toString("");
	}
}
