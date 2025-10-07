package fr.umlp.m1info.projectvms.group2.ast.minijaja.instr;

/**
 * Enum representing supported compound assignement operators
 */
public enum AssignmentOp {
	ADD("+=");

	private final String symbol;
	
	AssignmentOp(String symbol) {
		this.symbol = symbol;
	}

	@Override
	public String toString() {
		return symbol;
	}

	public static AssignmentOp fromSymbol(String symbol) {
		return switch(symbol) {
			case "+=" -> ADD;
			default -> throw new IllegalArgumentException("Unknow operator: " + symbol);
		};
	} 
} 
