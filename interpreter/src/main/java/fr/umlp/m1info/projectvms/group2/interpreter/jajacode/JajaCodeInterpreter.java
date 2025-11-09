package fr.umlp.m1info.projectvms.group2.interpreter.jajacode;

import java.lang.reflect.Method;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.JajaCodeNode;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.instr.*;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.instr.oper.*;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.value.*;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.var.*;
import fr.umlp.m1info.projectvms.group2.exception.InterpretRuntimeException;
import fr.umlp.m1info.projectvms.group2.exception.EvalRuntimeException;
import fr.umlp.m1info.projectvms.group2.memory.Memory;

public class JajaCodeInterpreter {
	Memory memory;

	public JajaCodeInterpreter(Memory memory) {
		if (memory == null) {
			throw new NullPointerException("memory cannot be null");
		}
		this.memory = memory;
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
			throw new InterpretRuntimeException("No @JajaCodeInterpretFunction found for " + node.getClass().getSimpleName());
		}

		try {
			m.invoke(this, node);
		} catch (Exception e) {
			throw new InterpretRuntimeException("Error interpreting node " + node.getClass().getSimpleName());
		}
	}

	/**
	 * Evaluate a statement value node
	 *
	 * @param node the Jajacode node instruction to interpret
	 * @throws InterpretRuntimeException if the node is null or not supported
	 */
	public void eval(JajaCodeNode node) {
		if (node == null) {
			throw new InterpretRuntimeException("node cannot be null");
		}

		Method m = findAnnotatedMethod(JajaCodeEvalFunction.class, node.getClass());
		if (m == null) {
			throw new InterpretRuntimeException("No @JajaCodeInterpretFunction found for " + node.getClass().getSimpleName());
		}

		try {
			m.invoke(this, node);
		} catch (Exception e) {
			throw new InterpretRuntimeException("Error interpreting node " + node.getClass().getSimpleName());
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
						Class<?> handledType = (Class<?>) annotationClass.getMethod("value").invoke(annotation);
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
		throw new InterpretRuntimeException("Not implemented yet");
	}

	@JajaCodeInterpretFunction(InstrStop.class)
	protected void interpretStop(InstrStop node) {
		throw new InterpretRuntimeException("Not implemented yet");
	}

	@JajaCodeInterpretFunction(InstrNop.class)
	protected void interpretNop(InstrNop node) {
		throw new InterpretRuntimeException("Not implemented yet");
	}

	@JajaCodeInterpretFunction(InstrSwap.class)
	protected void interpretSwap(InstrSwap node) {
		throw new InterpretRuntimeException("Not implemented yet");
	}

	@JajaCodeInterpretFunction(InstrNew.class)
	protected void interpretNew(InstrNew node) {
		throw new InterpretRuntimeException("Not implemented yet");
	}

	@JajaCodeInterpretFunction(InstrNewArray.class)
	protected void interpretNewArray(InstrNewArray node) {
		throw new InterpretRuntimeException("Not implemented yet");
	}

	@JajaCodeInterpretFunction(InstrInvoke.class)
	protected void interpretInvoke(InstrInvoke node) {
		throw new InterpretRuntimeException("Not implemented yet");
	}

	@JajaCodeInterpretFunction(InstrReturn.class)
	protected void interpretReturn(InstrReturn node) {
		throw new InterpretRuntimeException("Not implemented yet");
	}

	@JajaCodeInterpretFunction(InstrWrite.class)
	protected void interpretWrite(InstrWrite node) {
		throw new InterpretRuntimeException("Not implemented yet");
	}

	@JajaCodeInterpretFunction(InstrWriteln.class)
	protected void interpretWriteln(InstrWriteln node) {
		throw new InterpretRuntimeException("Not implemented yet");
	}

	@JajaCodeInterpretFunction(InstrGoto.class)
	protected void interpretGoto(InstrGoto node) {
		throw new InterpretRuntimeException("Not implemented yet");
	}

	@JajaCodeInterpretFunction(InstrLoad.class)
	protected void interpretLoad(InstrLoad node) {
		throw new InterpretRuntimeException("Not implemented yet");
	}

	@JajaCodeInterpretFunction(InstrALoad.class)
	protected void interpretALoad(InstrALoad node) {
		throw new InterpretRuntimeException("Not implemented yet");
	}

	@JajaCodeInterpretFunction(InstrStore.class)
	protected void interpretStore(InstrStore node) {
		throw new InterpretRuntimeException("Not implemented yet");
	}

	@JajaCodeInterpretFunction(InstrPop.class)
	protected void interpretPop(InstrPop node) {
		throw new InterpretRuntimeException("Not implemented yet");
	}

	@JajaCodeInterpretFunction(InstrIf.class)
	protected void interpretIf(InstrIf node) {
		throw new InterpretRuntimeException("Not implemented yet");
	}

	@JajaCodeInterpretFunction(InstrOperBinary.class)
	protected void interpretOperBinary(InstrOperBinary node) {
		throw new InterpretRuntimeException("Not implemented yet");
	}

	@JajaCodeInterpretFunction(InstrOperUnary.class)
	protected void interpretOperUnary(InstrOperUnary node) {
		throw new InterpretRuntimeException("Not implemented yet");
	}

	@JajaCodeInterpretFunction(InstrInc.class)
	protected void interpretInc(InstrInc node) {
		throw new InterpretRuntimeException("Not implemented yet");
	}

	@JajaCodeInterpretFunction(InstrAInc.class)
	protected void interpretAInc(InstrAInc node) {
		throw new InterpretRuntimeException("Not implemented yet");
	}

	@JajaCodeEvalFunction(ValAddress.class)
	protected Object evalAddress(ValAddress node) {
		throw new EvalRuntimeException("Not implemented yet");
	}

	@JajaCodeEvalFunction(ValBool.class)
	protected Object evalBool(ValBool node) {
		throw new EvalRuntimeException("Not implemented yet");
	}

	@JajaCodeEvalFunction(ValIdent.class)
	protected Object evalIdent(ValIdent node) {
		throw new EvalRuntimeException("Not implemented yet");
	}

	@JajaCodeEvalFunction(ValNbre.class)
	protected Object evalNbre(ValNbre node) {
		throw new EvalRuntimeException("Not implemented yet");
	}

	@JajaCodeEvalFunction(ValString.class)
	protected Object evalString(ValString node) {
		throw new EvalRuntimeException("Not implemented yet");
	}

	@JajaCodeEvalFunction(VarKind.class)
	protected Object evalKind(VarKind node) {
		throw new EvalRuntimeException("Not implemented yet");
	}
}
