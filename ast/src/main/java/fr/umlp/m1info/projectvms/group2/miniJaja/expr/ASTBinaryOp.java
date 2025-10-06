package fr.umlp.m1info.projectvms.group2.ast.minijaja.expr;

/**
 * Represents a binary operation expression in the MiniJaja abstract syntax tree (AST)
 */
public class ASTBinaryOp extends ASTExpr {
	private ASTExpr left;
	private ASTExpr right;
	private BinaryOp op;

	/**
	 * Constructs a new ASTBinaryOp node
	 *
	 * @param left the left-hand side expression
	 * @param right the right-hand side expression
	 * @param op the binary operator
	 * @param line the source code line where this node appears
	 * @param column the source code column where this node begins
	 */
	public ASTBinaryOp(ASTExpr left, ASTExpr right, BinaryOp op, int line, int column) {
		super(line, column);
		this.left = left;
		this.right = right;
		this.op = op;
	}

	/**
	 * Returns a string representation of this binary operation in MiniJaja syntax
	 *
	 * @return a formatted string
	 */
	public String toString() {
		return "" + left + " " + op + " " + right;
	}

	/**
	 * Enum representing all supported binary operators in MiniJaja
	 */
	public enum BinaryOp {
		ADD("+"),
		SUB("-"),
		MUL("*"),
		DIV("/"),
		AND("&&"),
		OR("||"),
		EQ("=="),
		GT(">"),
		LT("<");

		private final String symbol;

		BinaryOp(String symbol) {
			this.symbol = symbol;
		}

		@Override
		public String toString() {
			return symbol;
		}
	}
}
