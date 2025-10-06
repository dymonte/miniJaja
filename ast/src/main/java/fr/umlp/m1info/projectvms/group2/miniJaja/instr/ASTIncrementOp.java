package fr.umlp.m1info.projectvms.group2.ast.minijaja.instr;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTIdent;
/**
 * Represents a unary increment or decrement instruction in the MiniJaja AST
 */
public class ASTIncrementOp extends ASTInstr {
	private ASTIdent ident;
	private IncrementOp op;

	/**
	 * Constructs a new ASTIncrementOp node
	 *
	 * @param ident the target variable
	 * @param op the compound operator
	 * @param line the source code line of this node
	 * @param column the source code column of this node
	 */
	public ASTIncrementOp(ASTIdent ident, IncrementOp op, int line, int column) {
		super(line, column);
		this.ident = ident;
		this.op = op;
	}

	/**
	 * Returns a string representation of this increment in MiniJaja
	 *
	 * @return a formatted string
	 */
	public String toString() {
		return ident + " " + op;
	}

	/**
	 * Enum representing supported increment or decrement operators
	 */
	public enum IncrementOp {
		INC("++");

		private final String symbol;

		IncrementOp(String symbol) {
			this.symbol = symbol;
		}

		@Override
		public String toString() {
			return symbol;
		}
	}
}
