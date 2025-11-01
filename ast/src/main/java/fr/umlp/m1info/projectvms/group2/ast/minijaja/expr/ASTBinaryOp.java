package fr.umlp.m1info.projectvms.group2.ast.minijaja.expr;

/**
 * Represents a binary operation expression in the MiniJaja abstract syntax tree
 * (AST)
 */
public class ASTBinaryOp extends ASTExpr {
	private ASTExpr left;
	private ASTExpr right;
	private BinaryOp op;

	/**
	 * Constructs a new ASTBinaryOp node
	 *
	 * @param left   the left-hand side expression
	 * @param right  the right-hand side expression
	 * @param op     the binary operator
	 * @param line   the source code line where this node appears
	 * @param column the source code column where this node begins
	 * @throws NullPointerException if left, right or op is null
	 */
	public ASTBinaryOp(ASTExpr left, ASTExpr right, BinaryOp op, int line, int column) {
		super(line, column);
		if (left == null) {
			throw new NullPointerException("left cannot be null");
		} else if (right == null) {
			throw new NullPointerException("right cannot be null");
		} else if (op == null) {
			throw new NullPointerException("op cannot be null");
		}
		this.left = left;
		this.right = right;
		this.op = op;
	}

	/**
	 * Returns the left-hand side expression of this binary operation
	 *
	 * @return the ASTExpr representating the left operand
	 */
	public ASTExpr getLeft() {
		return left;
	}

	/**
	 * Returns the right-hand side expression of this binary operation.
	 *
	 * @return the ASTExpr representing the right operand
	 */
	public ASTExpr getRight() {
		return right;
	}

	/**
	 * Returns the binary operator of this expression.
	 *
	 * @return the BinaryOp representing the operator
	 */
	public BinaryOp getOp() {
		return op;
	}

	/**
	 * Returns a string representation of this binary operation in MiniJaja syntax
	 *
	 * @return a formatted string
	 */
	@Override
	public String toString() {
		return "" + left + " " + op + " " + right;
	}
}
