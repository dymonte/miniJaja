package fr.umlp.m1info.projectvms.group2.ast.jajacode.utils;

/**
 * Enum representing the type of an array or value
 */
public enum Type {
	INT("int"),
	BOOL("bool");

	private String symbol;

	Type(String symbol) {
		this.symbol = symbol;
	}

	@Override
	public String toString() {
		return symbol;
	}

	public static Type fromString(String symbol) {
		return switch (symbol) {
			case "int" -> INT;
			case "bool" -> BOOL;
			default -> throw new IllegalArgumentException("Unknow operation: " + symbol);
		};
	}
}
