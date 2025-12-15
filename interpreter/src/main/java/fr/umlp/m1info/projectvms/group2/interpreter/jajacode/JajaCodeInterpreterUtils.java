package fr.umlp.m1info.projectvms.group2.interpreter.jajacode;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.instr.oper.JajaCodeBinaryOper;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.instr.oper.JajaCodeUnaryOper;
import fr.umlp.m1info.projectvms.group2.exception.InterpretRuntimeException;
import fr.umlp.m1info.projectvms.group2.utils.Type;

public abstract class JajaCodeInterpreterUtils {
    public static Object evalOperBinary(Object v1, Object v2, JajaCodeBinaryOper oper){
        Object result;
        switch (oper) {
            case ADD -> {
                result = (int) v1 + (int) v2;
            }
            case SUB -> {
                result = (int) v1 - (int) v2;
            }
            case MUL -> {
                result = (int) v1 * (int) v2;
            }
            case DIV -> {
                if ((int) v2 == 0)
                    throw new InterpretRuntimeException("Division by zero");
                result = (int) v1 / (int) v2;
            }
            case CMP -> {
                // Compare two integers
                result = (int) v1 == (int) v2;
            }
            case SUP -> {
                result = (int) v1 > (int) v2;
            }
            case AND -> {
                result = (boolean) v1 && (boolean) v2;
            }
            case OR -> {
                result = (boolean) v1 || (boolean) v2;
            }
            default -> {
                throw new InterpretRuntimeException("Unknown binary operator: " + oper);
            }
        }
        return result;
    }

    public static Object evalOperUnary(Object v1, JajaCodeUnaryOper oper) {
        Object result;
        switch (oper) {
            case NEG -> {
                if (!(v1 instanceof Integer))
                    throw new InterpretRuntimeException("NEG requires an integer operand.");
                result = -((int) v1);
            }
            case NOT -> {
                if (!(v1 instanceof Boolean))
                    throw new InterpretRuntimeException("NOT requires a boolean operand.");
                result = !((boolean) v1);
            }
            default -> throw new InterpretRuntimeException("Unknown unary operator: " + oper);
        }
        return result;
    }

    public static Type getTypeFromObject(Object o) {
        return o instanceof Integer ? Type.INT : Type.BOOL;
    }

}
