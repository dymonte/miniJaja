package fr.umlp.m1info.projectvms.group2.ast.minijaja.expr;
/**
 * Enum representing all supported unary operators in MiniJaja
 */
public enum UnaryOp {
	NOT("!"),
	NEG("-");
	private final String symbol;

	UnaryOp(String symbol) {
		this.symbol = symbol;
	}

	@Override
	public String toString() {
		return symbol;
	}

	public static UnaryOp fromSymbol(String symbol) {
		return switch(symbol) {
			case "!" -> NOT;
			case "-" -> NEG;
			default -> throw new IllegalArgumentException("Unknow operator: " + symbol);
		};
	}
}

