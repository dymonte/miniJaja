package fr.umlp.m1info.projectvms.group2.ast.minijaja.expr;

/**
 * Represents a unary operation expression in the MiniJaja abstract syntax tree (AST)
 */
public class ASTUnaryOp extends ASTExpr {
	private ASTExpr expr;
	private UnaryOp op;

	/**
	 * Constructs a new ASTUnaryOp node
	 *
	 * @param expr the expression
	 * @param op the binary operator
	 * @param line the source code line where this node appears
	 * @param column the source code column where this node begins
	 */
	public ASTUnaryOp(ASTExpr expr, UnaryOp op, int line, int column) {
		super(line, column);
		this.expr = expr;
		this.op = op;
	}

	/**
	 * Returns a string representation of this unary expression operation in MiniJaja syntax
	 *
	 * @return a formatted string
	 */
	public String toString() {
		return "" + op + expr;
	}
}
