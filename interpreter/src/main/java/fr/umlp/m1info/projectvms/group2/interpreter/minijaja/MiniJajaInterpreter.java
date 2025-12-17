package fr.umlp.m1info.projectvms.group2.interpreter.minijaja;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

import fr.umlp.m1info.projectvms.group2.exception.EvalRuntimeException;
import fr.umlp.m1info.projectvms.group2.exception.InterpretRuntimeException;
import fr.umlp.m1info.projectvms.group2.exception.InterpreterInterruptedException;
import fr.umlp.m1info.projectvms.group2.logger.InterpreterLogger;

import fr.umlp.m1info.projectvms.group2.memory.Memory;
import fr.umlp.m1info.projectvms.group2.utils.Type;

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
import fr.umlp.m1info.projectvms.group2.ast.minijaja.cases.*;

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
		} catch (InvocationTargetException e) {
			Throwable cause = e.getTargetException();
			if (cause instanceof InterpretRuntimeException) {
				throw (InterpretRuntimeException) cause;
			} else if (cause instanceof EvalRuntimeException) {
				throw (EvalRuntimeException) cause;
			} else {
				throw new InterpretRuntimeException(
						"Error interpreting node " + node.getClass().getSimpleName() + ": " + cause.getMessage());
			}
		} catch (IllegalAccessException e) {
			throw new InterpretRuntimeException("Illegal access: " + e.getMessage());
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
		} catch (InvocationTargetException e) {
			Throwable cause = e.getTargetException();
			if (cause instanceof InterpretRuntimeException) {
				throw (InterpretRuntimeException) cause;
			} else if (cause instanceof EvalRuntimeException) {
				throw (EvalRuntimeException) cause;
			} else {
				throw new InterpretRuntimeException(
						"Error interpreting node " + node.getClass().getSimpleName() + ": " + cause.getMessage());
			}
		} catch (IllegalAccessException e) {
			throw new InterpretRuntimeException("Illegal access: " + e.getMessage());
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
		} catch (InvocationTargetException e) {
			Throwable cause = e.getTargetException();
			if (cause instanceof InterpretRuntimeException) {
				throw (InterpretRuntimeException) cause;
			} else if (cause instanceof EvalRuntimeException) {
				throw (EvalRuntimeException) cause;
			} else {
				throw new InterpretRuntimeException(
						"Error interpreting node " + node.getClass().getSimpleName() + ": " + cause.getMessage());
			}
		} catch (IllegalAccessException e) {
			throw new InterpretRuntimeException("Illegal access: " + e.getMessage());
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
		free(node.getDecls());
		memory.removeDecl(node.getIdent().getName());
	}

	/** Interprets the main method of a program */
	@InterpretFunction(ASTMainMethod.class)
	protected void interpretMainMethod(ASTMainMethod node) {
		interpret(node.getVars());
		interpret(node.getInstrs());
		free(node.getVars());
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

	/** Interprets a for loop */
	@InterpretFunction(ASTFor.class)
	protected void interpretFor(ASTFor node) {
		if (node.getVar() != null) {
			interpret(node.getVar());
		}

		while (node.getExpr() == null || (Boolean) eval(node.getExpr())) {
			interpret(node.getInstrs());
			if (node.getInc() != null) {
				interpret(node.getInc());
			}
		}

		if (node.getVar() != null) {
			free(node.getVar());
		}
	}

	/** Interprets a switch */
	@InterpretFunction(ASTSwitch.class)
	protected void interpretSwitch(ASTSwitch node) {
		Object value = eval(node.getExpr());
		memory.updateVal(ASTClass.getClassVar().getName(), value);
		interpret(node.getCases());
	}

	/** Interprets a write statement */
	@InterpretFunction(ASTWrite.class)
	protected void interpretWrite(ASTWrite node) {
		boolean isLn = node.getIsLn();
		Object value = eval((ASTNode) node.getValue());
		InterpreterLogger.getInstance().log(value + (isLn ? "\n" : ""), InterpreterLogger.MessageLevel.WRITE);
	}

	/** Interprets bonuses instructions */
	@InterpretFunction(ASTBonusInstr.class)
	protected void interpretBonuses(ASTBonusInstr node) {
		switch (node.getOp()) {
			case DUGOIS: {
				// TODO: verify that it is an integer expression
				Integer expr = (Integer) eval(node.getExpr());
				if (expr < 0) {
					throw new InterpretRuntimeException("(dugois): cannot sleep thread for a negative amount of time.");
				}
				try {
					Thread.sleep(Duration.ofSeconds(expr).toMillis());
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
				break;
			}
			case CHEVROT: {
				// TODO: verify that it is a boolean expression
				Boolean expr = (Boolean) eval(node.getExpr());
				if (!expr) {
					// TODO: maybe add a new custom kind of exception if we have time (or remove
					// this todo)
					throw new InterpretRuntimeException("(chevrot) Assertion failed : " + node.getExpr());
				}
				break;
			}
		}
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

	/** Interprets a chain of case declaration */
	@InterpretFunction(ASTCasesChain.class)
	protected void interpretCasesChain(ASTCasesChain node) {
		Boolean value = (Boolean) eval(node.getCase());
		if (!value) {
			interpret(node.getCases());
		}
	}

	/** Interprets an empty case declaration */
	@InterpretFunction(ASTCasesNil.class)
	protected void interpretCasesNil(ASTCasesNil node) {
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

	/** Evaluates a ternary operator */
	@EvalFunction(ASTTernaryOp.class)
	protected Object evalTernaryOp(ASTTernaryOp node) {
		// TODO: vérifier que l'expression condition est booléenne
		return (boolean) eval(node.getCondition()) ? eval(node.getFirstCase()) : eval(node.getSecondCase());
	}

	private int randomBetween(int bInf, int bSup) {
		if (bInf > bSup) {
			throw new InterpretRuntimeException("(bouquet): bInf must be <= bSup");
		}
		if (bInf == bSup) {
			return bInf;
		}
		return ThreadLocalRandom.current().nextInt(bInf, bSup);
	}

	/** Interprets bonuses expressions */
	@EvalFunction(ASTBonusExpr.class)
	protected Object interpretBonuses(ASTBonusExpr node) {
		// TODO: verify that those are integer expressions
		Integer val1 = (Integer) eval(node.getExpr1());
		Integer val2 = (Integer) eval(node.getExpr2());
		return switch (node.getOp()) {
			case JACQUIN -> (int) Math.round(Math.pow(val1, val2));
			case BOUQUET -> randomBetween(val1, val2);
		};
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

	/** Evaluates a case */
	@EvalFunction(ASTCase.class)
	protected Object evalCase(ASTCase node) {
		Object value = eval(node.getExpr());
		Object vSwitch = memory.getVal(ASTClass.getClassVar().getName());
		if (!value.equals(vSwitch)) {
			return false;
		}
		interpret(node.getInstrs());
		return true;
	}

	/** Evaluates a default */
	@EvalFunction(ASTDefault.class)
	protected Object evalDefault(ASTDefault node) {
		interpret(node.getInstrs());
		return true;
	}

	@FreeFunction(ASTDeclsChain.class)
	protected void freeDeclsChain(ASTDeclsChain node) {
		free(node.getDecls());
		free(node.getDecl());
	}

	@FreeFunction(ASTDeclsNil.class)
	protected void freeDeclsNil(ASTDeclsNil node) {
		// do nothing
	}

	@FreeFunction(ASTMethod.class)
	protected void freeMethod(ASTMethod node) {
		List<Type> paramsTypes = MiniJajaInterpreterUtils.collectParamTypes(node.getParams());
		memory.removeDecl(memory.getMethKey(node.getIdent().getName(), paramsTypes));
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

	/** Free a variable */
	@FreeFunction(ASTVar.class)
	protected void freeVar(ASTVar node) {
		memory.removeDecl(node.getIdent().getName());
	}
}
