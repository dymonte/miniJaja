package fr.umlp.m1info.projectvms.group2.interpreter.jajacode;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.JajaCodeNode;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.instr.*;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.instr.oper.*;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.value.*;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.var.*;
import fr.umlp.m1info.projectvms.group2.exception.EmptyStackException;
import fr.umlp.m1info.projectvms.group2.exception.InterpretRuntimeException;
import fr.umlp.m1info.projectvms.group2.exception.BadSymbolKindException;
import fr.umlp.m1info.projectvms.group2.exception.EvalRuntimeException;
import fr.umlp.m1info.projectvms.group2.logger.InterpreterLogger;
import fr.umlp.m1info.projectvms.group2.memory.Memory;
import fr.umlp.m1info.projectvms.group2.utils.Kind;
import fr.umlp.m1info.projectvms.group2.utils.Type;

import static fr.umlp.m1info.projectvms.group2.interpreter.jajacode.JajaCodeInterpreterUtils.*;

public class JajaCodeInterpreter {
	Memory memory;
	List<Instr> instrs;

	public JajaCodeInterpreter(Memory memory, List<Instr> instrs) {
		if (memory == null || instrs == null) {
			throw new NullPointerException("Arguments cannot be null");
		}
		this.memory = memory;
		this.instrs = instrs;
	}

	private Instr getInstr(int line) throws InterpretRuntimeException {
		if (line <= 0 || line > instrs.size()) {
			throw new InterpretRuntimeException("Instruction not found in the line: " + line);
		}
		return instrs.get(line - 1);
	}

	private Instr getNextInstr(Instr instr) {
		if (instr == null) {
			throw new InterpretRuntimeException("Instruction cannot be null");
		}
		return getInstr(instr.getLineId() + 1);
	}

	/**
	 * Interprets (executes) a statement instruction node
	 *
	 * @param node the Jajacode node instruction to interpret
	 * @throws InterpretRuntimeException if the node is null or not supported
	 */
	public void interpret(JajaCodeNode node) {
		if (node == null) {
			throw new InterpretRuntimeException("node cannot be null");
		}

		Method m = findAnnotatedMethod(JajaCodeInterpretFunction.class, node.getClass());
		if (m == null) {
			throw new InterpretRuntimeException(
					"No @JajaCodeInterpretFunction found for " + node.getClass().getSimpleName());
		}
		try {
			m.invoke(this, node);
		} catch (Exception e) {
			throw new InterpretRuntimeException(
					"Error interpreting node " + node.getClass().getSimpleName() + ": " + e.getMessage());
		}
	}

	/**
	 * Evaluate a statement value node
	 *
	 * @param node the Jajacode node instruction to interpret
	 * @throws InterpretRuntimeException if the node is null or not supported
	 */
	public Object eval(JajaCodeNode node) {
		if (node == null) {
			throw new InterpretRuntimeException("node cannot be null");
		}

		Method m = findAnnotatedMethod(JajaCodeEvalFunction.class, node.getClass());
		if (m == null) {
			throw new InterpretRuntimeException(
					"No @JajaCodeInterpretFunction found for " + node.getClass().getSimpleName());
		}

		try {
			return m.invoke(this, node);
		} catch (Exception e) {
			throw new InterpretRuntimeException(
					"Error interpreting node eval" + node.getClass().getSimpleName());
		}
	}

	private Method findAnnotatedMethod(Class<? extends java.lang.annotation.Annotation> annotationClass,
			Class<?> nodeClass) {
		Class<?> cls = this.getClass();
		while (cls != null) {
			for (Method method : cls.getDeclaredMethods()) {
				var annotation = method.getAnnotation(annotationClass);
				if (annotation != null) {
					try {
						Class<?> handledType = (Class<?>) annotationClass.getMethod("value")
								.invoke(annotation);
						if (handledType.isAssignableFrom(nodeClass)) {
							return method;
						}
					} catch (Exception ignored) {
					}
				}
			}
			cls = cls.getSuperclass();
		}
		return null;
	}

	@JajaCodeInterpretFunction(InstrInit.class)
	protected void interpretInit(InstrInit node) {
		interpret(getNextInstr(node));
	}

	@JajaCodeInterpretFunction(InstrStop.class)
	protected void interpretStop(InstrStop node) {
		return;
	}

	/**
	 * Interprets a nop instruction, do nothing and continue to interpret the next
	 * instruction
	 *
	 * @param node the nop instruction to interpret
	 */
	@JajaCodeInterpretFunction(InstrNop.class)
	protected void interpretNop(InstrNop node) {
		interpret(getNextInstr(node));
	}

	@JajaCodeInterpretFunction(InstrSwap.class)
	protected void interpretSwap(InstrSwap node) {
		memory.swap();
		interpret(getNextInstr(node));
	}

	@JajaCodeInterpretFunction(InstrNew.class)
	protected void interpretNew(InstrNew node) {
		String identifier = node.getValIdent().getIdent();
		Type type = node.getType().getType();
		switch (node.getKind().getKind()) {
			case VAR: {
				memory.identVal(identifier, Kind.VAR, type, node.getValDepth().getValue());
				break;
			}
			case VCONST: {
				memory.identVal(identifier, Kind.VCONST, type, 0);
				break;
			}
			case CONST: {
				memory.declCst(identifier, memory.pop(), type);
				break;
			}
			case METH: {
				memory.declMeth(identifier, memory.pop(), Collections.emptyList(), type);
				break;
			}
			default:
				throw new BadSymbolKindException(identifier, Kind.ARRAY);
		}
		interpret(getNextInstr(node));
	}

	@JajaCodeInterpretFunction(InstrNewArray.class)
	protected void interpretNewArray(InstrNewArray node) {
		if (node.getType() == null) {
			return;
		}

		memory.declArray(node.getValIdent().getIdent(), (int) memory.pop(), node.getType().getType());
		interpret(getNextInstr(node));
	}

	/**
	 * Interprets an invoke instruction
	 * Store the return address on the head of the stack and start to interpret the
	 * method
	 *
	 * @param node the invoke instruction to interpret
	 */
	@JajaCodeInterpretFunction(InstrInvoke.class)
	protected void interpretInvoke(InstrInvoke node) {
		String ident = node.getValIdent().getIdent();
		String identWithTypes = memory.getMethKey(ident, Collections.emptyList());
		Object addrObj = memory.getVal(identWithTypes);
		if (!(addrObj instanceof Integer)) {
			throw new InterpretRuntimeException("invalid method adress for" + ident);
		}
		int currentLine = node.getLineId();
		int methodAddress = (int) addrObj;
		memory.push(currentLine + 1, Type.INT);
		interpret(getInstr(methodAddress));
	}

	/**
	 * Interprets a return instruction
	 * Pop the return address from the stack and start to interpret the next
	 * instruction
	 *
	 * @param node the return instruction to interpret
	 */
	@JajaCodeInterpretFunction(InstrReturn.class)
	protected void interpretReturn(InstrReturn node) {
		int returnLine;
		try {
			returnLine = (int) memory.pop();
		} catch (EmptyStackException e) {
			throw new InterpretRuntimeException("Stack is empty, the return address is not present");
		} catch (ClassCastException e) {
			throw new InterpretRuntimeException("The head of stack is not an address");
		}
		interpret(getInstr(returnLine));
	}

	/**
	 * Interprets a write instruction
	 * Pop the value from the stack and log it
	 *
	 * @param node the write instruction to interpret
	 * @throws InterpretRuntimeException if the head of stack is not a string
	 */
	@JajaCodeInterpretFunction(InstrWrite.class)
	protected void interpretWrite(InstrWrite node) {
		String content;
		try {
			content = memory.pop().toString();
		} catch (EmptyStackException e) {
			throw new InterpretRuntimeException("Stack is empty, the value to write is not present");
		}

		writeInLog(String.valueOf(content));

		interpret(getNextInstr(node));
	}

	/**
	 * Interprets a writeln instruction
	 * Pop the value from the stack and log it with a newline
	 *
	 * @param node the writeln instruction to interpret
	 * @throws InterpretRuntimeException if the head of stack is not a string
	 */
	@JajaCodeInterpretFunction(InstrWriteln.class)
	protected void interpretWriteln(InstrWriteln node) {
		String content;
		try {
			content = memory.pop().toString();
		} catch (EmptyStackException e) {
			throw new InterpretRuntimeException("Stack is empty, the value to write is not present");
		}

		writeInLog(String.valueOf(content) + "\n");
		interpret(getNextInstr(node));
	}

	private void writeInLog(String s) {
		InterpreterLogger.getInstance().log(s, InterpreterLogger.MessageLevel.WRITE);
	}

	/**
	 * Interprets a goto instruction
	 *
	 * @param node the goto instruction to interpret that contains the address of
	 *             the next instruction
	 */
	@JajaCodeInterpretFunction(InstrGoto.class)
	protected void interpretGoto(InstrGoto node) {
		interpret(getInstr(node.getValAddress().getAddress()));
	}

	/**
	 * Interprets a load instruction
	 * Push the value of the variable on the stack
	 *
	 * @param node the load instruction to interpret
	 * @throws InterpretRuntimeException if the variable is not declared
	 */
	@JajaCodeInterpretFunction(InstrLoad.class)
	protected void interpretLoad(InstrLoad node) {
		memory.push(memory.getVal(node.getValIdent().getIdent()), memory.getType(node.getValIdent().getIdent()));
		interpret(getNextInstr(node));
	}

	/**
	 * Interprets an aload instruction
	 * Retrieve the index i from the stack
	 * Push the value of the array at index i on the stack
	 *
	 * @param node the aload instruction to interpret
	 * @throws InterpretRuntimeException if the array is not declared
	 */
	@JajaCodeInterpretFunction(InstrALoad.class)
	protected void interpretALoad(InstrALoad node) {
		int index;
		try {
			index = (int) memory.pop();
		} catch (ClassCastException e) {
			throw new InterpretRuntimeException("The head of stack is not an integer");
		}
		memory.push(memory.getValArray(node.getValIdent().getIdent(), index),
				memory.getType(node.getValIdent().getIdent()));
		interpret(getNextInstr(node));
	}

	/**
	 * Interprets a store instruction
	 * Retrieve the value from the stack
	 * Update the variable with the value
	 *
	 * @param node the store instruction to interpret that contains the identifier
	 *             of the variable
	 * @throws InterpretRuntimeException if the variable is not declared
	 */
	@JajaCodeInterpretFunction(InstrStore.class)
	protected void interpretStore(InstrStore node) {
		Object value = memory.pop();
		Type memoryType = memory.getType(node.getValIdent().getIdent());

		if (!((memoryType == Type.INT && value instanceof Integer)
				|| (memoryType == Type.BOOL && value instanceof Boolean))) {
			throw new InterpretRuntimeException("The value is not of the expected type");
		}

		memory.updateVal(node.getValIdent().getIdent(), value);
		interpret(getNextInstr(node));
	}

	/**
	 * Interprets an astore instruction
	 * Retrieve the value and the index i from the stack
	 * Update the array at index i with the value
	 *
	 * @param node the astore instruction to interpret that contains the identifier
	 *             of the array
	 * @throws InterpretRuntimeException if the array is not declared
	 */
	@JajaCodeInterpretFunction(InstrAStore.class)
	protected void interpretAStore(InstrAStore node) {
		Object value;
		Object indexObj;

		try {
			value = memory.pop();
			indexObj = memory.pop();
		} catch (EmptyStackException e) {
			throw new InterpretRuntimeException("Stack is empty");
		}

		if (!(indexObj instanceof Integer)) {
			throw new InterpretRuntimeException("Array index must be an integer.");
		}

		int index = (int) indexObj;
		memory.updateValArray(node.getValIdent().getIdent(), index, value);

		interpret(getNextInstr(node));
	}

	/**
	 * Interprets a push instruction
	 * Push the value on the stack
	 *
	 * @param node the push instruction to interpret that contains the value to push
	 * @throws InterpretRuntimeException if the value is not of the expected type
	 */
	@JajaCodeInterpretFunction(InstrPush.class)
	protected void interpretPush(InstrPush node) {
		Val valNode = node.getValVal();
		Object value = eval(valNode);
		Type type;

		if (value instanceof Boolean) {
			type = Type.BOOL;
		} else if (value instanceof Integer) {
			type = Type.INT;
		} else if (value instanceof String) {
			type = Type.STRING;
		} else {
			throw new InterpretRuntimeException("The value is not of the expected type");
		}
		memory.push(value, type);
		interpret(getNextInstr(node));
	}

	/**
	 * Interprets a pop instruction
	 * Pop the value from the stack
	 *
	 * @param node the pop instruction to interpret
	 */
	@JajaCodeInterpretFunction(InstrPop.class)
	protected void interpretPop(InstrPop node) {
		if (memory == null) {
			throw new InterpretRuntimeException("Memory not initialized");
		}
		memory.pop();
		interpret(getNextInstr(node));
	}

	/**
	 * Interprets an if instruction
	 * Pop the value from the stack
	 * If the value is true, interpret the instruction at the target address
	 *
	 * @param node the if instruction to interpret that contains the target address
	 * @throws InterpretRuntimeException if the top of stack is not a boolean
	 */
	@JajaCodeInterpretFunction(InstrIf.class)
	protected void interpretIf(InstrIf node) {
		Object topValue = memory.pop();
		if (!(topValue instanceof Boolean)) {
			throw new InterpretRuntimeException(
					"Top of stack must be a boolean for IF instruction.");
		}
		boolean condition = (Boolean) topValue;
		int targetAddress = node.getValAddress().getAddress();
		Instr nextInstr;
		nextInstr = condition ? getInstr(targetAddress) : getNextInstr(node);
		interpret(nextInstr);
	}

	@JajaCodeInterpretFunction(InstrOperBinary.class)
	protected void interpretOperBinary(InstrOperBinary node) {
		Object v2 = memory.pop();
		Object v1 = memory.pop();
		JajaCodeBinaryOper oper = node.getOper();
		Object result = evalOperBinary(v1, v2, oper);
		Type type = getTypeFromObject(result);
		memory.push(result, type);
		interpret(getNextInstr(node));
	}

	@JajaCodeInterpretFunction(InstrOperUnary.class)
	protected void interpretOperUnary(InstrOperUnary node) {
		Object v1 = memory.pop();
		JajaCodeUnaryOper oper = node.getOper();
		Object result = evalOperUnary(v1, oper);

		Type type = getTypeFromObject(result);

		memory.push(result, type);
		interpret(getNextInstr(node));
	}

	@JajaCodeInterpretFunction(InstrInc.class)
	protected void interpretInc(InstrInc node) {
		ValIdent valIdent = node.getValIdent();
		String varName = valIdent.getIdent();
		Object v = memory.pop();
		Object currentVal = memory.getVal(varName);
		if (!(currentVal instanceof Integer) || !(v instanceof Integer)) {
			throw new InterpretRuntimeException("Invalid types for inc operation on variable: " + varName);
		}
		int newVal = (int) currentVal + (int) v;
		memory.updateVal(varName, newVal);
		interpret(getNextInstr(node));
	}

	@JajaCodeInterpretFunction(InstrAInc.class)
	protected void interpretAInc(InstrAInc node) {
		String arrayName = node.getValIdent().getIdent();
		Object value;
		Object indexObj;
		value = memory.pop();
		indexObj = memory.pop();
		if (!(indexObj instanceof Integer)) {
			throw new InterpretRuntimeException("Array index must be an integer.");
		}
		int index = (int) indexObj;
		Object currentVal = memory.getValArray(arrayName, index);
		if (!(currentVal instanceof Integer) || !(value instanceof Integer)) {
			throw new InterpretRuntimeException("Invalid types for ainc operation on array: " + arrayName);
		}
		int newVal = (int) currentVal + (int) value;
		memory.updateValArray(arrayName, index, newVal);
		interpret(getNextInstr(node));
	}

	@JajaCodeInterpretFunction(InstrLength.class)
	protected void interpretLength(InstrLength node) {
		memory.push(memory.getLengthArray(node.getValIdent().getIdent()));
		interpret(getNextInstr(node));
	}

	@JajaCodeEvalFunction(ValAddress.class)
	protected Object evalAddress(ValAddress node) {
		return node.getAddress();
	}

	@JajaCodeEvalFunction(ValBool.class)
	protected Object evalBool(ValBool node) {
		return node.getValue();
	}

	@JajaCodeEvalFunction(ValIdent.class)
	protected Object evalIdent(ValIdent node) {
		String ident = node.getIdent();
		return memory.getVal(ident);

	}

	@JajaCodeEvalFunction(ValNbre.class)
	protected Object evalNbre(ValNbre node) {
		return node.getValue();
	}

	@JajaCodeEvalFunction(ValString.class)
	protected Object evalString(ValString node) {
		return node.getValue(); //
	}

	@JajaCodeEvalFunction(VarKind.class)
	protected Object evalKind(VarKind node) {
		throw new EvalRuntimeException("Not implemented yet");
	}
}
