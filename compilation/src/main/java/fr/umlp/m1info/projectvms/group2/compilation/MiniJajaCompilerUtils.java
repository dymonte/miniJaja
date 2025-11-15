package fr.umlp.m1info.projectvms.group2.compilation;

import fr.umlp.m1info.projectvms.group2.exception.CompilationRuntimeException;

public class MiniJajaCompilerUtils {
	private MiniJajaCompilerUtils() {
	}

	public static String binaryOpToString(String op) {
		return switch (op) {
			case "+" -> "add";
			case "-" -> "sub";
			case "*" -> "mul";
			case "/" -> "div";
			case "&&" -> "and";
			case "||" -> "or";
			case "==" -> "cmp";
			case ">" -> "sup";
			default -> throw new CompilationRuntimeException("Binary operation not supported: " + op);
		};
	}

	public static String unaryOpToString(String op) {
		return switch (op) {
			case "-" -> "neg";
			case "!" -> "not";
			default -> throw new CompilationRuntimeException("Unary operation not supported: " + op);
		};
	}
}
