package fr.umlp.m1info.projectvms.group2.utils;

/**
 * Represents the different kinds of symbols
 */
public enum Kind {
	VAR("var"),
	CONST("cst"),
	VCONST("vcst"),
	ARRAY("array"),
	METH("meth");

	private String symbol;

	/**
	 * Constructs a Kind with the given string symbol
	 *
	 * @param symbol the string representation of the kind
	 */
	private Kind(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * Returns the string symbol associated with this kind
	 *
	 * @return the string representation of this kind
	 */
	@Override
	public String toString() {
		return symbol;
	}

	/**
	 * Converts a string symbol in to its corresponding Kind
	 *
	 * @param symbol the string representation of the kind
	 * @return the Kind corresponding to the given symbol
	 * @throws IllegalArgumentException if the symbol does not match any known kind
	 */
	public static Kind fromString(String symbol) {
		return switch (symbol) {
			case "var" -> VAR;
			case "cst" -> CONST;
			case "vcst" -> VCONST;
			case "array" -> ARRAY;
			case "meth" -> METH;
			default -> throw new IllegalArgumentException("Unknow operation: " + symbol);
		};
	}
}
