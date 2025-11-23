package fr.umlp.m1info.projectvms.group2.checker;

import java.util.List;

import fr.umlp.m1info.projectvms.group2.exception.TypeCheckException;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTIntLiteral;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTLiteral;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTBoolLiteral;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTStringLiteral;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.BinaryOp;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.UnaryOp;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.type.ASTBoolType;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.type.ASTIntType;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.type.ASTType;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.type.ASTTypeMeth;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.type.ASTVoidType;
import fr.umlp.m1info.projectvms.group2.utils.Type;

public class TypeCheckerUtils {
	private TypeCheckerUtils() {
	}

	public static Type getType(ASTLiteral node) {
		if (node instanceof ASTIntLiteral) {
			return Type.INT;
		}
		if (node instanceof ASTBoolLiteral) {
			return Type.BOOL;
		}
		if (node instanceof ASTStringLiteral) {
			return Type.STRING;
		}
		throw new TypeCheckException("Unknow literal: " + node.getClass());
	}

	public static Type getTypeMeth(ASTTypeMeth node) {
		if (node instanceof ASTIntType) {
			return Type.INT;
		}
		if (node instanceof ASTBoolType) {
			return Type.BOOL;
		}
		if (node instanceof ASTVoidType) {
			return Type.VOID;
		}
		throw new TypeCheckException("Unknow value type: " + node.getClass());
	}

	public static Type getType(ASTType node) {
		if (node instanceof ASTIntType) {
			return Type.INT;
		}
		if (node instanceof ASTBoolType) {
			return Type.BOOL;
		}
		throw new TypeCheckException("Unknow value type: " + node.getClass());
	}

	public static Type getBinaryOpType(BinaryOp binary) {
		return switch (binary) {
			case ADD, SUB, MUL, DIV -> Type.INT;
			case EQ, AND, OR -> Type.BOOL;
			default -> throw new TypeCheckException("Unknow operation: " + binary);
		};
	}

	public static Type getUnaryOpType(UnaryOp unary) {
		return switch (unary) {
			case NEG -> Type.INT;
			case NOT -> Type.BOOL;
			default -> throw new TypeCheckException("Unknow operation: " + unary);
		};
	}

	public static String methodKey(String name, List<Type> params) {
		for (Type type : params) {
			name += type.name();
		}
		return "-" + name;
	}
}
