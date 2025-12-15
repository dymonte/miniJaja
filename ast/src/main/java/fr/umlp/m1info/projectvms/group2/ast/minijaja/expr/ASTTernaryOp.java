package fr.umlp.m1info.projectvms.group2.ast.minijaja.expr;

/**
 * TODO: javadoc
*/
public class ASTTernaryOp extends ASTExpr {
	private ASTExpr condition;
	private ASTExpr firstCase;
	private ASTExpr secondCase;
	

	/**
	 * TODO: javadoc
	 */
	public ASTTernaryOp(ASTExpr condition, ASTExpr firstCase, ASTExpr secondCase, int line, int column) {
		super(line, column);
		this.condition = condition;
		this.firstCase = firstCase;
		this.secondCase = secondCase;
	}

	/**
	 * TODO: javadoc
	 */
	public ASTExpr getCondition() {
		return condition;
	}

	/**
	 * TODO: javadoc
	 */
	public ASTExpr getFirstCase() {
		return firstCase;
	}

	/**
	 * TODO: javadoc
	 */
	public ASTExpr getSecondCase() {
		return secondCase;
	}

	/**
	 * TODO: javadoc
	 */
	@Override
	public String toString() {
		return condition + " ? " + firstCase + " : " + secondCase;
	}
}
