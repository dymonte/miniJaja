package fr.umlp.m1info.projectvms.group2.ast.jajacode.utils;

/**
 * Enum representing the kind of a value
 */
public enum Kind {
	CONST("cst"),
	VAR("var"),
	ARR("arr"),
	METH("meth");

	private String symbol;

	Kind(String symbol) {
		this.symbol = symbol;
	}

	@Override
	public String toString() {
		return symbol;
	}

	public static Kind fromString(String symbol) {
		return switch (symbol) {
			case "cst" -> CONST;
			case "var" -> VAR;
			case "arr" -> ARR;
			case "meth" -> METH;
			default -> throw new IllegalArgumentException("Unknow operation: " + symbol);
		};
	}
}
