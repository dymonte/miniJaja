package fr.umlp.m1info.projectvms.group2.ast.minijaja.instr;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTExpr;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTIdent;
/**
 * Represents a compound assignement instruction in the MiniJaja AST
 */
public class ASTAssignmentOp extends ASTInstr {
	private ASTIdent ident;
	private ASTExpr expr;
	private AssignmentOp op;

	/**
	 * Constructs a new ASTAssignmentOp node
	 *
	 * @param ident the target variable
	 * @param expr the expression to assing
	 * @param op the compound operator
	 * @param line the source code line of this node
	 * @param column the source code column of this node
	 */
	public ASTAssignmentOp(ASTIdent ident, ASTExpr expr, AssignmentOp op, int line, int column) {
		super(line, column);
		this.ident = ident;
		this.expr = expr;
		this.op = op;
	}

	/**
	 * Returns a string representation of this assignement in MiniJaja
	 *
	 * @return a formatted string
	 */
	public String toString() {
		return ident + " " + op + " " + expr;
	}

	/**
	 * Enum representing supported compound assignement operators
	 */
	public enum AssignmentOp {
		ADD("+=");

		private final String symbol;

		AssignmentOp(String symbol) {
			this.symbol = symbol;
		}

		@Override
		public String toString() {
			return symbol;
		}
	}
}
