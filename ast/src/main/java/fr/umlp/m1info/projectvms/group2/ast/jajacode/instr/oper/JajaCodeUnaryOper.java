package fr.umlp.m1info.projectvms.group2.ast.jajacode.instr.oper;

public enum JajaCodeUnaryOper {
	NEG("neg"),
	NOT("not");

	private final String instruction;

	/**
	 * Constructs a new JajaCodeUnaryOper with its associated Jajacode instruction
	 *
	 * @param instruction the textual symbol of the operator
	 */
	JajaCodeUnaryOper(String instruction) {
		this.instruction = instruction;
	}

	/**
	 * @return the instruction associated with this operator
	 */
	@Override
	public String toString() {
		return instruction;
	}

	/**
     * Returns the JajaCodeUnaryOper associated with the given symbol
     *
     * @param instruction the textual symbol of the operator
     * @return the JajaCodeUnaryOper associated with the given symbol
	 */
	public static JajaCodeUnaryOper fromSymbol(String instruction) {
		return switch (instruction) {
			case "neg" -> NEG;
			case "not" -> NOT;
			default -> throw new IllegalArgumentException("Unknow operator: " + instruction);
		};
	}
}
