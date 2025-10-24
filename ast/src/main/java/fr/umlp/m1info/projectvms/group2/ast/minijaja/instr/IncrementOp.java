package fr.umlp.m1info.projectvms.group2.ast.minijaja.instr;

/**
 * Enum representing supported compound increment operators in MiniJaja
 */
public enum IncrementOp {
	INC("++");

	private final String symbol;

	/**
	 * Constructs a new IncrementOp with the given symbol
	 *
	 * @param symbol the symbol representing the operator
	 */
	IncrementOp(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * Returns the symbol of this operator as a string
	 *
	 * @return the symbol of the operator
	 */
	@Override
	public String toString() {
		return symbol;
	}

	/**
	 * Returns the IncrementOp corresponding to the given symbol
	 *
	 * @param symbol the symbol to convert
	 * @return the corresponding IncrementOp
	 * @throws IllegalArgumentException if the symbol does not match any operator
	 */
	public static IncrementOp fromSymbol(String symbol) {
		return switch (symbol) {
			case "++" -> INC;
			default -> throw new IllegalArgumentException("Unknow operator: " + symbol);
		};
	}
}
