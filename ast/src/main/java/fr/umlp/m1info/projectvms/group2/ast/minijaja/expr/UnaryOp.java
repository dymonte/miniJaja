package fr.umlp.m1info.projectvms.group2.ast.minijaja.expr;

/**
 * Represents all supported unary operators in the MiniJaja language
 */
public enum UnaryOp {
	NOT("!"),
	NEG("-");

	private final String symbol;

	/**
	 * Constructs a new UnaryOp with its associated symbol
	 *
	 * @param symbol the textual symbol of the operator
	 */
	UnaryOp(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * Returns the MiniJaja symbol associated with this operator
	 *
	 * @return the opertor symbol as a string
	 */
	@Override
	public String toString() {
		return symbol;
	}

	/**
	 * Returns the UnaryOp constant corresponding to the given symbol
	 *
	 * @param symbol the operator symbol as it appears in MiniJaja
	 * @return the UnaryOp corresponding to the given symbol
	 * @throws IllegalArgumentException if the provided symbol does not match a
	 *                                  supported unary operator
	 */
	public static UnaryOp fromSymbol(String symbol) {
		return switch (symbol) {
			case "!" -> NOT;
			case "-" -> NEG;
			default -> throw new IllegalArgumentException("Unknow operator: " + symbol);
		};
	}
}
