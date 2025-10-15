package fr.umlp.m1info.projectvms.group2.ast.minijaja.expr;

/**
 * Enum representing all supported binary operators in MiniJaja
 */
public enum BinaryOp {
	ADD("+"),
	SUB("-"),
	MUL("*"),
	DIV("/"),
	AND("&&"),
	OR("||"),
	EQ("=="),
	GT(">"),
	LT("<");

	private final String symbol;

	BinaryOp(String symbol) {
		this.symbol = symbol;
	}

	@Override
	public String toString() {
		return symbol;
	}


	public static BinaryOp fromSymbol(String symbol) {
		return switch(symbol) {
			case "+" -> ADD;
			case "-" -> SUB;
			case "*" -> MUL;
			case "/" -> DIV;
			case "&&" -> AND;
			case "||" -> OR;
			case "==" -> EQ;
			case ">" -> GT;
			case "<" -> LT;
			default -> throw new IllegalArgumentException("Unknown operator: " + symbol);
		};
	}
}
