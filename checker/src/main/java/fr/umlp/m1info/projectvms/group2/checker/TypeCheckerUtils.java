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

/**
 * Utility class providing methods to convert AST nodes and operations
 * into corresponding Type values. Contains helpers for literals,
 * method return types, and operator type inference
 */
public class TypeCheckerUtils {
	private TypeCheckerUtils() {
	}

	/**
	 * Returns the Type of a literal node
	 *
	 * @param node the AST literal
	 * @return the corresponding Type (INT, BOOL, STRING)
	 * @throws TypeCheckException if the literal type is unknown
	 */
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

	/**
	 * Returns the Type of a method type node
	 *
	 * @param node the AST type for method return
	 * @return the corresponding Type (INT, BOOL, VOID)
	 * @throws TypeCheckException if the type is unknown
	 */
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

	/**
	 * Returns the Type of a general type node (non-void)
	 *
	 * @param node the AST type
	 * @return the corresponding Type (INT or BOOL)
	 * @throws TypeCheckException if the type is unknown
	 */
	public static Type getType(ASTType node) {
		if (node instanceof ASTIntType) {
			return Type.INT;
		}
		if (node instanceof ASTBoolType) {
			return Type.BOOL;
		}
		throw new TypeCheckException("Unknow value type: " + node.getClass());
	}

	/**
	 * Returns the expected type for a binary operation
	 *
	 * @param binary the binary operation
	 * @return INT for arithmetic operations, BOOL for logical/comparison
	 * @throws TypeCheckException if the operation is unknown
	 */
	public static Type getBinaryOpType(BinaryOp binary) {
		return switch (binary) {
			case ADD, SUB, MUL, DIV, MOD -> Type.INT;
			case EQ, AND, OR, GT, LT, NEQ, GE, LE -> Type.BOOL;
			default -> throw new TypeCheckException("Unknow operation: " + binary);
		};
	}

	/**
	 * Returns the expected type argument for a binary operation
	 *
	 * @param binary the binary operation
	 * @return INT for arithmetic, BOOL for logical/comparison
	 * @throws TypeCheckException if the operation is unknown
	 */
	public static Type getBinaryOpArgumentType(BinaryOp binary) {
		return switch (binary) {
			case ADD, SUB, MUL, DIV, GT, LT, GE, LE -> Type.INT;
			case EQ, AND, OR, NEQ -> Type.BOOL;
			default -> throw new TypeCheckException("Unknow operation: " + binary);
		};
	}

	/**
	 * Returns the expected type for a unary operation
	 *
	 * @param unary the unary operation
	 * @return INT for negation, BOOL for NOT
	 * @throws TypeCheckException if the operation is unknown
	 */
	public static Type getUnaryOpType(UnaryOp unary) {
		return switch (unary) {
			case NEG -> Type.INT;
			case NOT -> Type.BOOL;
			default -> throw new TypeCheckException("Unknow operation: " + unary);
		};
	}

	/**
	 * Generates a unique method key for the environment map based on
	 * the method name and its parameter types
	 *
	 * @param name   the method name
	 * @param params the list of parameter types
	 * @return a unique string key representing the method
	 */
	public static String methodKey(String name, List<Type> params) {
		StringBuilder sb = new StringBuilder(name);
		for (Type type : params) {
			sb.append(type.name());
		}
		return "-" + sb.toString();
	}
}
