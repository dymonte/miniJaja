package fr.umlp.m1info.projectvms.group2.ast.minijaja.expr;

/**
 * Represents all supported binary operators in the MiniJaja language
 */
public enum BinaryOp {
	ADD("+"),
	SUB("-"),
	MUL("*"),
	DIV("/"),
	MOD("%"),
	AND("&&"),
	OR("||"),
	EQ("=="),
	NEQ("!="),
	GT(">"),
	LT("<"),
	GE(">="),
	LE("<=");

	private final String symbol;

	/**
	 * Constructs a new BinaryOp with its associated MiniJaja symbol
	 *
	 * @param symbol the textual symbol of the operator
	 */
	BinaryOp(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * Returns the MiniJaja symbol associated with this operator
	 */
	@Override
	public String toString() {
		return symbol;
	}

	/**
	 * Returns the BinaryOp constant corresponding the the given symbol
	 *
	 * @param symbol the operator symbol as it appears in MiniJaja
	 * @return the BinaryOp corresponding to the given symbol
	 * @throws IllegalArgumentException if the symbol does not match any supported
	 *                                  binary operator
	 */
	public static BinaryOp fromSymbol(String symbol) {
		return switch (symbol) {
			case "+" -> ADD;
			case "-" -> SUB;
			case "*" -> MUL;
			case "/" -> DIV;
			case "%" -> MOD;
			case "&&" -> AND;
			case "||" -> OR;
			case "==" -> EQ;
			case "!=" -> NEQ;
			case ">" -> GT;
			case "<" -> LT;
			case ">=" -> GE;
			case "<=" -> LE;
			default -> throw new IllegalArgumentException("Unknown operator: " + symbol);
		};
	}
}
