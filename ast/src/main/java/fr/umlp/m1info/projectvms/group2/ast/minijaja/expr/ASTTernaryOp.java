package fr.umlp.m1info.projectvms.group2.ast.minijaja.expr;

/**
 * Represents a ternary operator.
 */
public class ASTTernaryOp extends ASTExpr {
	private ASTExpr condition;
	private ASTExpr firstCase;
	private ASTExpr secondCase;

	/**
	 * Constructs a ASTTernaryOp node
	 *
	 * @param condition  the ternary operator condition
	 * @param firstCase  the ternary operator firstCase (when condition is true)
	 * @param secondCase the ternary operator secondCase (when condition is false)
	 * @param line       the source code line where this node appears
	 * @param column     the source code column where this node appears
	 */
	public ASTTernaryOp(ASTExpr condition, ASTExpr firstCase, ASTExpr secondCase, int line, int column) {
		super(line, column);
		this.condition = condition;
		this.firstCase = firstCase;
		this.secondCase = secondCase;
	}

	/**
	 * Returns the ternary operator condition
	 *
	 * @return the operator's condition
	 */
	public ASTExpr getCondition() {
		return condition;
	}

	/**
	 * Returns the ternary operator first case expression
	 * (when the condition is false)
	 *
	 * @return the operator's first case
	 */
	public ASTExpr getFirstCase() {
		return firstCase;
	}

	/**
	 * Returns the ternary operator second case expression
	 * (when the condition is true)
	 *
	 * @return the operator's second case
	 */
	public ASTExpr getSecondCase() {
		return secondCase;
	}

	/**
	 * Returns a string representation of this ternary operator
	 *
	 * @return a formatted string
	 */
	@Override
	public String toString() {
		return condition + " ? " + firstCase + " : " + secondCase;
	}
}
