package fr.umlp.m1info.projectvms.group2.interpreter.minijaja;

import fr.umlp.m1info.projectvms.group2.exception.InterpretRuntimeException;
import fr.umlp.m1info.projectvms.group2.memory.Memory;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTLValue;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTIdent;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTArrayAccess;

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

		if (left instanceof Boolean) {
			left = ((Boolean) left) ? 1 : 0;
		}
		if (right instanceof Boolean) {
			right = ((Boolean) right) ? 1 : 0;
		}

		if (left instanceof Integer li && right instanceof Integer ri) {
			return li + ri;
		}

		throw new InterpretRuntimeException(
				"Cannot add values : " + left.getClass().getSimpleName() + " + " + right.getClass().getSimpleName());
	}
}
