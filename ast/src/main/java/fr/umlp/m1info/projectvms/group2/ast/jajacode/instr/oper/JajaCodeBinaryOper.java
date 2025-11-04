package fr.umlp.m1info.projectvms.group2.ast.jajacode.instr.oper;

public enum JajaCodeBinaryOper {
	ADD("add"),
	SUB("sub"),
	MUL("mul"),
	DIV("div"),
	CMP("cmp"),
	SUP("sup"),
	AND("and"),
	OR("or");

	private final String instruction;

	/**
	 * Constructs a new JajaCodeBinaryOper with its associated Jajacode instruction
	 *
	 * @param instruction the textual symbol of the operator
	 */
	JajaCodeBinaryOper(String instruction) {
		this.instruction = instruction;
	}

	/**
	 * Returns the MiniJaja symbol associated with this operator
	 */
	@Override
	public String toString() {
		return instruction;
	}

	/**
	 *
	 */
	public static JajaCodeBinaryOper fromSymbol(String instruction) {
		return switch (instruction) {
			case "add" -> ADD;
			case "sub" -> SUB;
			case "mul" -> MUL;
			case "div" -> DIV;
			case "cmp" -> CMP;
			case "sup" -> SUP;
			case "and" -> AND;
			case "or" -> OR;
			default -> throw new IllegalArgumentException("Unknow operator: " + instruction);
		};
	}
}
