package fr.umlp.m1info.projectvms.group2.ast.minijaja.instr;

/**
 * Enum representing supported compound assignement operators
 */
public enum AssignmentOp {
	EQ("="),
	ADD("+=");

	private final String symbol;

	/**
	 * Constructs a new AssignmentOp with the given symbol
	 *
	 * @param symbol the symbol representing the operator
	 */
	AssignmentOp(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * Returns the symbol of this operator as a string
	 *
	 * @return the symbol representing this operator
	 */
	@Override
	public String toString() {
		return symbol;
	}

	/**
	 * Returns the AssignmentOp corresponding to the given symbol
	 *
	 * @param symbol the symbol to convert
	 * @return the corresponding AssignmentOp
	 * @throws IllegalArgumentException if the symbol does not match any operator
	 */
	public static AssignmentOp fromSymbol(String symbol) {
		return switch (symbol) {
			case "=" -> EQ;
			case "+=" -> ADD;
			default -> throw new IllegalArgumentException("Unknow operator: " + symbol);
		};
	}
}
