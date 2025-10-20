package fr.umlp.m1info.projectvms.group2.interpreter.minijaja;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTArrayAccess;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTIdent;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTLValue;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.BinaryOp;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.UnaryOp;
import fr.umlp.m1info.projectvms.group2.exception.InterpretRuntimeException;
import fr.umlp.m1info.projectvms.group2.memory.Memory;

public class MiniJajaInterpreterUtils {
	private MiniJajaInterpreterUtils() {
	}

	/** Represents a resolved lvalue: name, optional index, current value */
	public record LValueRef(String name, Integer index, Object currentValue) {
	}

	public static Object getValMemory(Memory memory, LValueRef ref) {
		if (ref.index() == null) {
			return memory.getVal(ref.name());
		} else {
			return memory.getValArray(ref.name(), ref.index());
		}
	}

	public static void updateValMemory(Memory memory, LValueRef ref, Object newValue) {
		if (ref.index() == null) {
			memory.updateVal(ref.name(), newValue);
		} else {
			memory.updateValArray(ref.name(), ref.index(), newValue);
		}
	}

	public static LValueRef resolveLValue(Memory memory, ASTLValue lvalue, MiniJajaInterpreter interpreter) {
		if (lvalue instanceof ASTIdent ident) {
			String name = (String) interpreter.eval(ident);
			return new LValueRef(name, null, memory.getVal(name));
		} else if (lvalue instanceof ASTArrayAccess array) {
			String name = (String) interpreter.eval(array.getIdent());
			Integer index = (Integer) interpreter.eval(array.getExpr());
			return new LValueRef(name, index, memory.getValArray(name, index));
		} else {
			throw new InterpretRuntimeException(
					"Invalid lvalue type: " + lvalue.getClass().getSimpleName());
		}
	}

	public static Object addValues(Object left, Object right) {
		if (left == null || right == null) {
			throw new InterpretRuntimeException("Cannot apply to null values");
		}

		if (left instanceof Boolean lb && right instanceof Boolean rb) {
			return lb || rb;
		}

		if (left instanceof Boolean bool) {
			left = bool ? 1 : 0;
		}
		if (right instanceof Boolean bool) {
			right = bool ? 1 : 0;
		}

		if (left instanceof Integer li && right instanceof Integer ri) {
			return li + ri;
		}

		throw new InterpretRuntimeException(
				"Cannot add values : " + left.getClass().getSimpleName() + " + " + right.getClass().getSimpleName());
	}

	public static Object evalBinaryOp(BinaryOp op, Object left, Object right) {
		return switch (op) {
			case ADD -> addValues(left, right);
			case SUB -> {
				if (left instanceof Integer li && right instanceof Integer ri) {
					yield li - ri;
				} else {
					throw new InterpretRuntimeException("Cannot subtract non-integer values");
				}
			}
			case MUL -> {
				if (left instanceof Integer li && right instanceof Integer ri) {
					yield li * ri;
				} else {
					throw new InterpretRuntimeException("Cannot multiply non-integer values");
				}
			}
			case DIV -> {
				if (left instanceof Integer li && right instanceof Integer ri && ri > 0) {
					yield li / ri;
				} else {
					throw new InterpretRuntimeException("Cannot divide non-integer values");
				}
			}
			case AND -> {
				if (left instanceof Boolean lb && right instanceof Boolean rb) {
					yield lb && rb;
				} else if (left instanceof Integer li && right instanceof Boolean rb) {
					yield (li != 0) && rb;
				} else if (left instanceof Boolean lb && right instanceof Integer ri) {
					yield lb && (ri != 0);
				} else if (left instanceof Integer li && right instanceof Integer ri) {
					yield (li != 0) && (ri != 0);
				} else {
					throw new InterpretRuntimeException("Cannot apply AND to non-boolean values");
				}
			}
			case OR -> {
				if (left instanceof Boolean lb && right instanceof Boolean rb) {
					yield lb || rb;
				} else if (left instanceof Integer li && right instanceof Boolean rb) {
					yield (li != 0) || rb;
				} else if (left instanceof Boolean lb && right instanceof Integer ri) {
					yield lb || (ri != 0);
				} else if (left instanceof Integer li && right instanceof Integer ri) {
					yield (li != 0) || (ri != 0);
				} else {
					throw new InterpretRuntimeException("Cannot apply OR to non-boolean values");
				}
			}
			case EQ -> {
				yield left.equals(right);
			}
			case GT -> {
				if (left instanceof Integer li && right instanceof Integer ri) {
					yield li > ri;
				} else {
					throw new InterpretRuntimeException("Cannot compare non-integer values");
				}
			}
			case LT -> {
				if (left instanceof Integer li && right instanceof Integer ri) {
					yield li < ri;
				} else {
					throw new InterpretRuntimeException("Cannot compare non-integer values");
				}
			}
			default -> throw new InterpretRuntimeException("Unknown binary operator: " + op);
		};
	}

	public static Object unaryOperation(UnaryOp op, Object value) {
		switch(op) {
			case NEG -> {
				if (value instanceof Integer intValue) {
					return -intValue;
				} else {
					throw new InterpretRuntimeException("Cannot apply negation to value of type: " + value.getClass().getSimpleName());
				}
			}
			case NOT -> {
				if (value instanceof Boolean boolValue) {
					return !boolValue;
				} else if (value instanceof Integer intValue) {
					return intValue != 0;
				} else {
					throw new InterpretRuntimeException("Cannot apply logical NOT to value of type: " + value.getClass().getSimpleName());
				}
			}
			default -> throw new InterpretRuntimeException("Unknown unary operator: " + op);
		}
	}
}
