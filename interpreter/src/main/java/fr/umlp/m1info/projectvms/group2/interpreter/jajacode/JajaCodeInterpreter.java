package fr.umlp.m1info.projectvms.group2.interpreter.jajacode;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.JajaCodeNode;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.instr.*;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.instr.oper.*;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.value.*;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.var.*;
import fr.umlp.m1info.projectvms.group2.exception.InterpretRuntimeException;
import fr.umlp.m1info.projectvms.group2.exception.BadSymbolKindException;
import fr.umlp.m1info.projectvms.group2.exception.EvalRuntimeException;
import fr.umlp.m1info.projectvms.group2.memory.Memory;
import fr.umlp.m1info.projectvms.group2.memory.symtable.ObjectKind;
import fr.umlp.m1info.projectvms.group2.memory.symtable.Type;

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
		if (0 >= line || line >= instrs.size()) {
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
					"Error interpreting node " + node.getClass().getSimpleName());
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
			throw new InterpretRuntimeException(
					"No @JajaCodeInterpretFunction found for " + node.getClass().getSimpleName());
		}

		try {
			m.invoke(this, node);
		} catch (Exception e) {
			throw new InterpretRuntimeException(
					"Error interpreting node " + node.getClass().getSimpleName());
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
		// since memory is already initialized in the constructor, does nothing
		return;
	}

	@JajaCodeInterpretFunction(InstrStop.class)
	protected void interpretStop(InstrStop node) {
		Thread.currentThread().interrupt();
	}

	@JajaCodeInterpretFunction(InstrNop.class)
	protected void interpretNop(InstrNop node) {
		// Do nothing
		return;
	}

	@JajaCodeInterpretFunction(InstrSwap.class)
	protected void interpretSwap(InstrSwap node) {
		memory.swap();
	}

	@JajaCodeInterpretFunction(InstrNew.class)
	protected void interpretNew(InstrNew node) {
		if (node.getType() == null || node.getKind() == null) {
			return;
		}

		Type type = switch (node.getType().getType()) {
			case INT -> Type.INT;
			case BOOL -> Type.BOOL;
		};

		String identifier = node.getValIdent().getIdent();

		switch (node.getKind().getKind()) {
			case VAR: {
				memory.identVal(identifier, ObjectKind.VAR, type,
						node.getValAddress().getAddress());
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
				throw new BadSymbolKindException(identifier, ObjectKind.ARRAY);
		}
	}

	@JajaCodeInterpretFunction(InstrNewArray.class)
	protected void interpretNewArray(InstrNewArray node) {
		if (node.getType() == null) {
			return;
		}
		Type type = switch (node.getType().getType()) {
			case INT -> Type.INT;
			case BOOL -> Type.BOOL;
		};

		memory.declArray(node.getValIdent().getIdent(), (int) memory.pop(), type);
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
