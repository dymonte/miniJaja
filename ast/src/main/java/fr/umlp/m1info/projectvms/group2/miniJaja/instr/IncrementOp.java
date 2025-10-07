package fr.umlp.m1info.projectvms.group2.ast.minijaja.instr;

/**
 * Enum representing supported compound increment operators
 */
public enum IncrementOp {
	INC("++");

	private final String symbol;
	
	IncrementOp(String symbol) {
		this.symbol = symbol;
	}

	@Override
	public String toString() {
		return symbol;
	}

	public static IncrementOp fromSymbol(String symbol) {
		return switch(symbol) {
			case "++" -> INC;
			default -> throw new IllegalArgumentException("Unknow operator: " + symbol);
		};
	} 
} 
