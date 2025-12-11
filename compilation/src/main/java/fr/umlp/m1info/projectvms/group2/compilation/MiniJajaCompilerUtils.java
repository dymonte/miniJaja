package fr.umlp.m1info.projectvms.group2.compilation;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.params.ASTParams;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.params.ASTParamsChain;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.params.ASTParamsNil;
import fr.umlp.m1info.projectvms.group2.exception.CompilationRuntimeException;

public class MiniJajaCompilerUtils {
	private MiniJajaCompilerUtils() {
	}

	public static String unaryOpToString(String op) {
		return switch (op) {
			case "-" -> "neg";
			case "!" -> "not";
			default -> throw new CompilationRuntimeException("Unary operation not supported: " + op);
		};
	}

	public static int countParams(ASTParams node) {
		if (node instanceof ASTParamsNil) {
			return 0;
		}
		ASTParamsChain chain = (ASTParamsChain) node;
		return 1 + countParams(chain.getParams());
	}
}
