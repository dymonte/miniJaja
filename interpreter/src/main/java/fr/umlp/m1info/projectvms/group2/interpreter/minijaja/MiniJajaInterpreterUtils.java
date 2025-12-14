package fr.umlp.m1info.projectvms.group2.interpreter.minijaja;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.BinaryOp;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.UnaryOp;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTExpr;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.exprs.ASTExprs;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.exprs.ASTExprsChain;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.exprs.ASTExprsNil;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.params.ASTParams;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.params.ASTParamsChain;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.params.ASTParamsNil;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.type.ASTBoolType;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.type.ASTIntType;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.type.ASTTypeMeth;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.type.ASTVoidType;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decl.ASTParam;
import fr.umlp.m1info.projectvms.group2.exception.InterpretRuntimeException;
import fr.umlp.m1info.projectvms.group2.memory.Memory;
import fr.umlp.m1info.projectvms.group2.utils.Type;

import java.util.List;
import java.util.ArrayList;

import java.util.function.IntBinaryOperator;

public class MiniJajaInterpreterUtils {
	private MiniJajaInterpreterUtils() {
	}

	public static void bindParameters(Memory memory, ASTParams params, ASTExprs exprs,
			MiniJajaInterpreter interpreter) {
		ASTParams currentParams = params;
		ASTExprs currentExprs = exprs;

		while (currentParams instanceof ASTParamsChain paramsChain
				&& currentExprs instanceof ASTExprsChain exprsChain) {
			ASTParam param = paramsChain.getParam();
			ASTExpr expr = exprsChain.getExpr();

			Type type = getTypeFromParam(param);
			Object value = interpreter.eval(expr);

			memory.declVar(param.getIdent().getName(), value, type);

			currentParams = paramsChain.getParams();
			currentExprs = exprsChain.getExprs();
		}

		if (!(currentParams instanceof ASTParamsNil) || !(currentExprs instanceof ASTExprsNil)) {
			throw new InterpretRuntimeException("Not the same amount of method parameters and call expressions");
		}
	}

	public static void unbindParameters(Memory memory, ASTParams params) {
		while (params instanceof ASTParamsChain chain) {
			ASTParam param = chain.getParam();

			memory.removeDecl(param.getIdent().getName());

			params = chain.getParams();
		}
	}

	public static List<Type> collectExprTypes(ASTExprs exprs, MiniJajaInterpreter interpreter) {
		List<Type> types = new ArrayList<>();
		ASTExprs currentExprs = exprs;

		while (currentExprs instanceof ASTExprsChain chain) {
			Object value = interpreter.eval(chain.getExpr());
			types.add(getTypeFromObject(value));
			currentExprs = chain.getExprs();
		}

		return types;
	}

	public static List<Type> collectParamTypes(ASTParams params) {
		ArrayList<Type> types = new ArrayList<>();
		ASTParams currentParams = params;

		while (currentParams instanceof ASTParamsChain chain) {
			types.add(getTypeFromParam(chain.getParam()));
			currentParams = chain.getParams();
		}

		return types;
	}

	public static Type getTypeFromTypeMeth(ASTTypeMeth node) {
		Type type = null;
		if (node instanceof ASTIntType) {
			type = Type.INT;
		} else if (node instanceof ASTBoolType) {
			type = Type.BOOL;
		} else if (node instanceof ASTVoidType) {
			type = Type.VOID;
		}
		return type;
	}

	public static Object binaryOperation(BinaryOp op, Object left, Object right) {
		if (left == null || right == null) {
			throw new NullPointerException("Cannot be null");
		}

		switch (op) {
			case ADD:
				return intOp(left, right, (a, b) -> a + b, "add");
			case SUB:
				return intOp(left, right, (a, b) -> a - b, "substract");
			case MUL:
				return intOp(left, right, (a, b) -> a * b, "multiply");
			case DIV:
				return intOp(left, right, (a, b) -> {
					if (b == 0)
						throw new InterpretRuntimeException("Division by zero");
					return a / b;
				}, "divide");
			case AND:
				return boolOp(left, right, (a, b) -> a && b, "AND");
			case OR:
				return boolOp(left, right, (a, b) -> a || b, "OR");
			case EQ:
				return left.equals(right);
			case GT:
				return intOp(left, right, (a, b) -> a > b ? 1 : 0, "compare >") != 0;
			case LT:
				return intOp(left, right, (a, b) -> a < b ? 1 : 0, "compare <") != 0;
			default:
				throw new InterpretRuntimeException("Unknown binary operator: " + op);
		}
	}

	public static Object unaryOperation(UnaryOp op, Object value) {
		if (value == null) {
			throw new NullPointerException("Cannot be null");
		}

		switch (op) {
			case NEG:
				return intOp(0, value, (a, b) -> a - b, "substract");
			case NOT:
				return boolOp(false, value, (a, b) -> !b, "not");
			default:
				throw new InterpretRuntimeException("Unknown unary operator: " + op);
		}
	}

	private static Integer intOp(Object left, Object right, IntBinaryOperator op, String opName) {
		if (left instanceof Integer li && right instanceof Integer ri) {
			return op.applyAsInt(li, ri);
		}
		throw new InterpretRuntimeException(
				"Cannot " + opName + " non-integer values: " + left.getClass().getSimpleName()
						+ ", " + right.getClass().getSimpleName());
	}

	@FunctionalInterface
	private interface BooleanBinaryOperator {
		boolean apply(boolean a, boolean b);
	}

	private static Boolean boolOp(Object left, Object right, BooleanBinaryOperator op, String opName) {
		if (left instanceof Boolean lb && right instanceof Boolean rb) {
			return op.apply(lb, rb);
		}
		throw new InterpretRuntimeException("Cannot apply " + opName + " to non-boolean values: "
				+ left.getClass().getSimpleName() + ", " + right.getClass().getSimpleName());
	}

	private static Type getTypeFromParam(ASTParam param) {
		if (param.getType() instanceof ASTIntType) {
			return Type.INT;
		} else {
			return Type.BOOL;
		}
	}

	public static Type getTypeFromObject(Object value) {
		if (value instanceof Integer) {
			return Type.INT;
		}
		if (value instanceof Boolean) {
			return Type.BOOL;
		}
		throw new InterpretRuntimeException("Unsupported argument type");
	}
}
