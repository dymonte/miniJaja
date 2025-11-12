package fr.umlp.m1info.projectvms.group2.utils;

/**
 * Represents the different data types
 */
public enum Type {
	INT("int"),
	BOOL("bool"),
	STRING("string"),
	VOID("void");

	private String symbol;

	/**
	 * Constructs a Type with the given string symbol
	 *
	 * @param symbol the string representation of the type
	 */
	private Type(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * Returns the string symbol associated with this type
	 *
	 * @return the string representation of this type
	 */
	@Override
	public String toString() {
		return symbol;
	}

	/**
	 * Converts a string symbol into its corresponding Type
	 *
	 * @param symbol the string representation of the type
	 * @return the Type corresponding to the given symbol
	 * @throws IllegalArgumentException if the symbol does not match any known type
	 */
	public static Type fromString(String symbol) {
		return switch (symbol) {
			case "int" -> INT;
			case "bool" -> BOOL;
			case "string" -> STRING;
			case "void" -> VOID;
			default -> throw new IllegalArgumentException("Unknow operation: " + symbol);
		};
	}
}
