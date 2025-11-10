package fr.umlp.m1info.projectvms.group2.interpreter.minijaja;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Method;

import fr.umlp.m1info.projectvms.group2.exception.EvalRuntimeException;
import fr.umlp.m1info.projectvms.group2.exception.InterpretRuntimeException;
import fr.umlp.m1info.projectvms.group2.exception.InterpreterInterruptedException;
import fr.umlp.m1info.projectvms.group2.logger.InterpreterLogger;

import fr.umlp.m1info.projectvms.group2.memory.Memory;
import fr.umlp.m1info.projectvms.group2.memory.symtable.Type;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.ASTNode;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.type.ASTTypeMeth;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decl.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decls.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.exprs.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instrs.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.params.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.vars.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.var.*;

/**
 * The Interpreter class traverses and executes an Abstract Syntax Tree(AST) by
 * interpreting statement nodes and evaluating expressions nodes
 */
public class MiniJajaInterpreter {
	Memory memory;

	public MiniJajaInterpreter(Memory memory) {
		if (memory == null) {
			throw new NullPointerException("memory cannot be null");
		}
		this.memory = memory;
	}

	/**
	 * Interprets (executes) a statement node
	 *
	 * @param node the AST node to interpret
	 * @throws InterpretRuntimeException if the node type is not supported for
	 *                                   interpretation or if node is null
	 */
	public void interpret(ASTNode node) {
		if (node == null) {
			throw new InterpretRuntimeException("node cannot be null");
		}

		if (Thread.currentThread().isInterrupted()) {
			throw new InterpreterInterruptedException("Interpreter interrupted");
		}

		Method m = findAnnotatedMethod(InterpretFunction.class, node.getClass());
		if (m == null) {
			throw new InterpretRuntimeException("No @InterpretFunction found for " + node.getClass().getSimpleName());
		}

		try {
			m.invoke(this, node);
		} catch (Exception e) {
			throw new InterpretRuntimeException("Error interpreting node " + node.getClass().getSimpleName());
		}
	}

	/**
	 * Evaluate (computes the value of) an expression node
	 *
	 * @param node the AST node to evaluate
	 * @return the computed value of the expression
	 * @throws EvalRuntimeException if the node type is not supported for evaluation
	 */
	public Object eval(ASTNode node) {
		if (node == null) {
			throw new EvalRuntimeException("node cannot be null");
		}

		Method m = findAnnotatedMethod(EvalFunction.class, node.getClass());
		if (m == null) {
			throw new EvalRuntimeException("No @EvalFunction found for " + node.getClass().getSimpleName());
		}

		try {
			return m.invoke(this, node);
		} catch (Exception e) {
			throw new InterpretRuntimeException("Error interpreting node " + node.getClass().getSimpleName());
		}
	}

	/**
	 * Remove declaration of the node
	 *
	 * @param node the AST node to remove declaration
	 * @throws InterpretRuntimeException if the node is null or not supported
	 */
	public void free(ASTNode node) {
		if (node == null) {
			throw new InterpretRuntimeException("node cannot be null");
		}

		Method m = findAnnotatedMethod(FreeFunction.class, node.getClass());
		if (m == null) {
			throw new InterpretRuntimeException("No @FreeFunction found for " + node.getClass().getSimpleName());
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

	/** Interprets a class declaration */
	@InterpretFunction(ASTClass.class)
	protected void interpretClass(ASTClass node) {
		memory.declVar(node.getIdent().getName(), 0, Type.INT);
		interpret(node.getDecls());
		interpret(node.getMainMethod());
	}

	/** Interprets the main method of a program */
	@InterpretFunction(ASTMainMethod.class)
	protected void interpretMainMethod(ASTMainMethod node) {
		interpret(node.getVars());
		interpret(node.getInstrs());
	}

	/** Interprets a method declaration */
	@InterpretFunction(ASTMethod.class)
	protected void interpretMethod(ASTMethod node) {
		String ident = node.getIdent().getName();
		ASTParams params = node.getParams();
		List<Type> paramsTypes = MiniJajaInterpreterUtils.collectParamTypes(params);
		Type returnType = MiniJajaInterpreterUtils.getTypeFromTypeMeth(node.getType());

		memory.declMeth(ident, node, paramsTypes, returnType);
	}

	/** Interprets a chain of declaration */
	@InterpretFunction(ASTDeclsChain.class)
	protected void interpretDeclsChain(ASTDeclsChain node) {
		interpret(node.getDecl());
		interpret(node.getDecls());
	}

	/** Interprets an empty declaration list */
	@InterpretFunction(ASTDeclsNil.class)
	protected void interpretDeclsNil(ASTDeclsNil node) {
		// do nothing
	}

	/** Interprets an assignement operation */
	@InterpretFunction(ASTAssignmentOp.class)
	protected void interpretAssignment(ASTAssignmentOp node) {
		Object exprValue = eval(node.getExpr());

		Object newValue = switch (node.getOp()) {
			case EQ -> exprValue;
			case ADD -> MiniJajaInterpreterUtils.binaryOperation(BinaryOp.ADD, eval(node.getLValue()), exprValue);
		};

		if (node.getLValue() instanceof ASTIdent ident) {
			memory.updateVal(ident.getName(), newValue);
		} else if (node.getLValue() instanceof ASTArrayAccess arrayAccess) {
			memory.updateValArray(arrayAccess.getIdent().getName(), (int) eval(arrayAccess.getExpr()), newValue);
		} else {
			throw new InterpretRuntimeException("Left value not compatible");
		}
	}

	/** Interprets an if-(else)-statement */
	@InterpretFunction(ASTIf.class)
	protected void interpretIf(ASTIf node) {
		Object value = eval(node.getExpr());
		if (value instanceof Boolean bool) {
			if (bool) {
				interpret(node.getIfInstrs());
			} else {
				interpret(node.getElseInstrs());
			}
		} else {
			throw new InterpretRuntimeException("Condition must evaluate to a boolean value");
		}
	}

	/** Interprets an increment operation */
	@InterpretFunction(ASTIncrementOp.class)
	protected void interpretIncrement(ASTIncrementOp node) {

		Object newValue = switch (node.getOp()) {
			case INC -> MiniJajaInterpreterUtils.binaryOperation(BinaryOp.ADD, eval(node.getLValue()), 1);
		};

		if (node.getLValue() instanceof ASTIdent ident) {
			memory.updateVal(ident.getName(), newValue);
		} else if (node.getLValue() instanceof ASTArrayAccess arrayAccess) {
			memory.updateValArray(arrayAccess.getIdent().getName(), (int) eval(arrayAccess.getExpr()), newValue);
		} else {
			throw new InterpretRuntimeException("Left value not compatible");
		}
	}

	/** Interprets a method call used as an instruction */
	@InterpretFunction(ASTMethodCallInstr.class)
	protected void interpretMethodCallInstr(ASTMethodCallInstr node) {
		String methodName = node.getIdent().getName();
		ASTExprs exprs = node.getExprs();

		ArrayList<Type> argumentTypes = MiniJajaInterpreterUtils.collectExprTypes(exprs, this);

		String methodKey = memory.getMethKey(methodName, argumentTypes);
		ASTMethod method = (ASTMethod) memory.getVal(methodKey);
		String classVarName = ASTClass.getClassVar().getName();
		Type oldClassType = memory.getType(classVarName);
		memory.updateType(classVarName,
				MiniJajaInterpreterUtils.getTypeFromTypeMeth(method.getType()));

		MiniJajaInterpreterUtils.bindParameters(memory, method.getParams(), node.getExprs(), this);
		try {
			interpret(method.getVars());
			interpret(method.getInstrs());
		} finally {
			MiniJajaInterpreterUtils.unbindParameters(memory, method.getParams());
			free(method.getVars());
			memory.updateType(classVarName, oldClassType);
		}
	}

	/** Interprets a return statement */
	@InterpretFunction(ASTReturn.class)
	protected void interpretReturn(ASTReturn node) {
		Object value = eval(node.getExpr());
		memory.updateVal(ASTClass.getClassVar().getName(), value);
	}

	/** Interprets a while loop */
	@InterpretFunction(ASTWhile.class)
	protected void interpretWhile(ASTWhile node) {
		while ((Boolean) eval(node.getExpr())) {
			interpret(node.getInstrs());
		}
	}

	/** Interprets a write statement */
	@InterpretFunction(ASTWrite.class)
	protected void interpretWrite(ASTWrite node) {
		boolean isLn = node.getIsLn();
		Object value = eval((ASTNode) node.getValue());
		InterpreterLogger.getInstance().log(value + (isLn ? "\n" : ""), InterpreterLogger.MessageLevel.WRITE);
	}

	/** Interprets a chain of instructions */
	@InterpretFunction(ASTInstrsChain.class)
	protected void interpretInstrsChain(ASTInstrsChain node) {
		interpret(node.getInstr());
		interpret(node.getInstrs());
	}

	/** Interprets an empty instruction list */
	@InterpretFunction(ASTInstrsNil.class)
	protected void interpretInstrsNil(ASTInstrsNil node) {
		// do nothing
	}

	/** Interprets a array variable declaration */
	@InterpretFunction(ASTVarArray.class)
	protected void interpretVarArray(ASTVarArray node) {
		String ident = node.getIdent().getName();
		Object value = eval(node.getExpr());
		Type type = MiniJajaInterpreterUtils.getTypeFromTypeMeth(node.getType());
		if (value instanceof Integer) {
			if ((int) value > 0) {
				memory.declArray(ident, (int) value, type);
			} else {
				throw new InterpretRuntimeException(
						"Size must evaluate to an integer value greater than 0");
			}
		} else {
			throw new InterpretRuntimeException("Size must evaluate to an integer value");
		}
	}

	/** Interprets a constant variable declaration */
	@InterpretFunction(ASTVarConst.class)
	protected void interpretVarConst(ASTVarConst node) {
		String ident = node.getIdent().getName();
		var vexp = node.getVExpr();
		Type type = MiniJajaInterpreterUtils.getTypeFromTypeMeth((ASTTypeMeth) node.getType());
		if (vexp == null) {
			memory.declCst(ident, type);
		} else {
			Object value = eval(vexp.getExpr());
			if (type == MiniJajaInterpreterUtils.getTypeFromObject(value)) {
				memory.declCst(ident, value, type);
			} else {
				throw new InterpretRuntimeException(
						"Variable type does not match the type of the assigned value");
			}
		}
	}

	/** Interprets a simple variable declaration */
	@InterpretFunction(ASTVarSimple.class)
	protected void interpretVarSimple(ASTVarSimple node) {
		String ident = node.getIdent().getName();
		var vexp = node.getVExpr();
		Type type = MiniJajaInterpreterUtils.getTypeFromTypeMeth(node.getType());
		if (vexp == null) {
			memory.declVar(ident, null, type);
		} else {
			Object value = eval(vexp.getExpr());
			if (type == MiniJajaInterpreterUtils.getTypeFromObject(value)) {
				memory.declVar(ident, value, type);
			} else {
				throw new InterpretRuntimeException(
						"Variable type does not match the type of the assigned value");
			}
		}
	}

	/** Interprets a chain of variable declarations */
	@InterpretFunction(ASTVarsChain.class)
	protected void interpretVarsChain(ASTVarsChain node) {
		interpret(node.getVar());
		interpret(node.getVars());
	}

	/** Interprets an empty variable declaration list */
	@InterpretFunction(ASTVarsNil.class)
	protected void interpretVarsNil(ASTVarsNil node) {
		// do nothing
	}

	/** Evaluates an array element access expression */
	@EvalFunction(ASTArrayAccess.class)
	protected Object evalArrayAccess(ASTArrayAccess node) {
		return memory.getValArray(node.getIdent().getName(), (Integer) eval(node.getExpr()));
	}

	/** Evaluates a binary operation */
	@EvalFunction(ASTBinaryOp.class)
	protected Object evalBinaryOp(ASTBinaryOp node) {
		return MiniJajaInterpreterUtils.binaryOperation(node.getOp(), eval(node.getLeft()),
				eval(node.getRight()));
	}

	/** Evaluates a boolean literal */
	@EvalFunction(ASTBoolLiteral.class)
	protected Object evalBoolLiteral(ASTBoolLiteral node) {
		return node.getValue();
	}

	/** Evaluates an identifier expression */
	@EvalFunction(ASTIdent.class)
	protected Object evalIdent(ASTIdent node) {
		return memory.getVal(node.getName());
	}

	/** Evaluates an integer literal */
	@EvalFunction(ASTIntLiteral.class)
	protected Object evalIntLiteral(ASTIntLiteral node) {
		return node.getValue();
	}

	/** Evaluates a length expression on arrays */
	@EvalFunction(ASTLength.class)
	protected Object evalLength(ASTLength node) {
		return memory.getLengthArray(node.getIdent().getName());
	}

	/** Evaluates a method call used as an expression */
	@EvalFunction(ASTMethodCallExpr.class)
	protected Object evalMethodCall(ASTMethodCallExpr node) {
		interpret(new ASTMethodCallInstr(node.getIdent(), node.getExprs(), node.getLine(), node.getColumn()));

		return memory.getVal(ASTClass.getClassVar().getName());
	}

	/** Evaluates a string literal */
	@EvalFunction(ASTStringLiteral.class)
	protected Object evalStringLiteral(ASTStringLiteral node) {
		return node.getValue();
	}

	/** Evaluates a unary operation */
	@EvalFunction(ASTUnaryOp.class)
	protected Object evalUnaryOp(ASTUnaryOp node) {
		return MiniJajaInterpreterUtils.unaryOperation(node.getOp(), eval(node.getExpr()));
	}

	/** Free a variable for memory */
	@FreeFunction(ASTVarsChain.class)
	protected void freeVarsChain(ASTVarsChain node) {
		free(node.getVar());
		free(node.getVars());
	}

	/** Free an empty variable */
	@FreeFunction(ASTVarsNil.class)
	protected void freeVarsNil(ASTVarsNil node) {
		// do nothing
	}

	/** Free an variable */
	@FreeFunction(ASTVar.class)
	protected void freeVar(ASTVar node) {
		memory.removeDecl(node.getIdent().getName());
	}
}
