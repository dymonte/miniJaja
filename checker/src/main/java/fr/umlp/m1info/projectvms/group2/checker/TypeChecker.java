package fr.umlp.m1info.projectvms.group2.checker;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.reflect.InvocationTargetException;

import fr.umlp.m1info.projectvms.group2.utils.Type;

import fr.umlp.m1info.projectvms.group2.exception.TypeCheckException;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.ASTNode;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decl.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decls.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.exprs.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instrs.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.params.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.var.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.vars.*;

import fr.umlp.m1info.projectvms.group2.checker.Diagnostic.ErrorCode;

/**
 * Maintains a type environment for variables and methods.
 */
class TypeEnv {
	private final Map<String, Type> vars = new HashMap<>();

	/**
	 * Defines a variable or method in the environment
	 *
	 * @param name the name of the variable
	 * @param type the type of the variable
	 */
	public void define(String name, Type type) {
		vars.put(name, type);
	}

	/**
	 * Returns the type of a variable of method by name
	 *
	 * @param name the name of variable of method
	 * @return the type of the variable or method
	 */
	public Type get(String name) {
		return vars.get(name);
	}

	/**
	 * Checks if a variable or method is defined in the environment
	 *
	 * @param name the name of variable of method
	 * @return if a variable or method is defined
	 */
	public boolean isDefined(String name) {
		return vars.containsKey(name);
	}

	/**
	 * Checks if a method with the given name exists (ignores parameters)
	 *
	 * @param name the name of method
	 * @return if a method name exists
	 */
	public boolean hasMethod(String name) {
		for (String key : vars.keySet()) {
			if (key.startsWith("-" + name)) {
				return true;
			}
		}
		return false;
	}
}

/**
 * The TypeChecker performs type-checking of MiniJaja AST nodes
 * It uses a diagnostic object to report errors
 */
public class TypeChecker implements Checker {
	private final Diagnostic diag;
	private TypeEnv env;

	/**
	 * Constructs a TypeChecker with a diagnostic object
	 *
	 * @param diag the diagnostic handler
	 * @throws NullPointerException if diag is null
	 */
	public TypeChecker(Diagnostic diag) {
		if (diag == null) {
			throw new NullPointerException("Diagnostic cannot be null");
		}
		this.diag = diag;
		env = new TypeEnv();
	}

	/**
	 * Type-checks an AST node by invoking the corresponding @TypeCheckFunction
	 * method
	 *
	 * @param node the AST node to check
	 * @throws NullPointerException if node is null
	 * @throws TypeCheckException   if no suitable check method exists
	 */
	public void check(ASTNode node) {
		if (node == null) {
			throw new NullPointerException("node cannot be null");
		}

		Method m = findAnnotatedMethod(TypeCheckFunction.class, node.getClass());
		if (m == null) {
			throw new TypeCheckException("No @TypeCheckFunction found for " + node.getClass().getSimpleName());
		}

		try {
			m.invoke(this, node);
		} catch (InvocationTargetException | IllegalAccessException e) {
			throw new TypeCheckException(e.getCause().getMessage());
		}
	}

	/**
	 * Evaluates the type of an AST node by invoking the
	 * corresponding @TypeEvalFunction method
	 *
	 * @param node the AST node
	 * @return the type of the node
	 * @throws NullPointerException if node is null
	 * @throws TypeCheckException   if no suitable eval method exists
	 */
	public Type evalType(ASTNode node) {
		if (node == null) {
			throw new NullPointerException("node cannot be null");
		}

		Method m = findAnnotatedMethod(TypeEvalFunction.class, node.getClass());
		if (m == null) {
			throw new TypeCheckException("No @TypeEvalFunction found for " + node.getClass().getSimpleName());
		}

		try {
			return (Type) m.invoke(this, node);
		} catch (InvocationTargetException | IllegalAccessException e) {
			throw new TypeCheckException(e.getCause().getMessage());
		}
	}

	/**
	 * Finds a method annotated with a specific annotation that can handle the given
	 * AST node class
	 *
	 * @param annotationClass the annotation class
	 * @param nodeClass       the AST node class
	 * @return the Method if found, null otherwise
	 */
	private Method findAnnotatedMethod(Class<? extends java.lang.annotation.Annotation> annotationClass,
			Class<?> nodeClass) {
		for (Method method : this.getClass().getDeclaredMethods()) {
			var annotation = method.getAnnotation(annotationClass);
			if (annotation != null) {
				try {
					Class<?> handled = (Class<?>) annotationClass.getMethod("value").invoke(annotation);
					if (handled.isAssignableFrom(nodeClass)) {
						return method;
					}
				} catch (Exception ignored) {
				}
			}
		}
		return null;
	}

	/** Checks a class declaration by checking its declarations and main method */
	@TypeCheckFunction(ASTClass.class)
	protected void checkClass(ASTClass node) {
		check(node.getDecls());
		check(node.getMainMethod());
	}

	/** Checks the main method by checking variables and instructions */
	@TypeCheckFunction(ASTMainMethod.class)
	protected void checkMainMethod(ASTMainMethod node) {
		check(node.getVars());
		check(node.getInstrs());
	}

	/** Registers a method in the environment and checks for duplicate signatures */
	@TypeCheckFunction(ASTMethod.class)
	protected void checkMethod(ASTMethod node) {
		Type returnType = TypeCheckerUtils.getTypeMeth(node.getType());
		List<Type> paramTypes = new ArrayList<>();

		for (ASTParams curr = node.getParams(); curr instanceof ASTParamsChain chain; curr = chain.getParams()) {
			paramTypes.add(TypeCheckerUtils.getType(chain.getParam().getType()));
		}

		String methodKey = TypeCheckerUtils.methodKey(node.getIdent().getName(), paramTypes);

		if (env.isDefined(methodKey)) {
			diag.error(ErrorCode.METHOD_ALREADY_EXISTS,
					"The method '" + node.getIdent().getName() + "' already exists with the same arguments", node.getLine(),
					node.getColumn());
		}
		env.define(methodKey, returnType);
	}

	/**
	 * Checks a sequence of declarations, and deferred method instructions after all
	 * declarations
	 */
	@TypeCheckFunction(ASTDecls.class)
	protected void checkDecls(ASTDecls node) {
		List<ASTMethod> deferredMethods = new ArrayList<>();

		for (ASTDecls curr = node; curr instanceof ASTDeclsChain chain; curr = chain.getDecls()) {
			ASTDecl decl = chain.getDecl();

			if (decl instanceof ASTMethod method) {
				deferredMethods.add(method);
			}

			check(decl);
		}
		checkMethods(deferredMethods);
	}

	private void checkMethods(List<ASTMethod> methods) {
		for (ASTMethod method : methods) {
			check(method.getInstrs());

			ASTReturn ret = searchReturn(method.getInstrs());
			Type expectedReturnType = TypeCheckerUtils.getTypeMeth(method.getType());

			if (ret == null) {
				if (!Type.VOID.equals(expectedReturnType)) {
					diag.error(ErrorCode.MISSING_RETURN_STATEMENT, "Method must return a value of type " + expectedReturnType,
							method.getLine(), method.getColumn());
				}
				continue;
			}

			Type actualReturnType = evalType(ret);
			if (!expectedReturnType.equals(actualReturnType)) {
				diag.error(ErrorCode.RETURN_TYPE_MISMATCH,
						"Return type mismatch: expected " + expectedReturnType + ", got " + actualReturnType, ret.getLine(),
						ret.getColumn());
			}
		}
	}

	/** Checks an assignment operation for type correctness */
	@TypeCheckFunction(ASTAssignmentOp.class)
	protected void checkAssignment(ASTAssignmentOp node) {
		Type leftType = evalType(node.getLValue());
		Type rightType = evalType(node.getExpr());

		// right must be integer (left verified after)
		if (node.getOp() == AssignmentOp.ADD && !Type.INT.equals(rightType)) {
			diag.error(ErrorCode.TYPE_MISMATCH,
					"Assignment '+=' requires an integer value on the right-hand side, got " + rightType, node.getLine(),
					node.getColumn());
		}

		if (!leftType.equals(rightType)) {
			diag.error(ErrorCode.TYPE_MISMATCH, "Assignment type mismatch: left = " + leftType + ", right = " + rightType,
					node.getLine(), node.getColumn());
		}
	}

	/** Checks an if statement for boolean condition and checks its branches */
	@TypeCheckFunction(ASTIf.class)
	protected void checkIf(ASTIf node) {
		Type conditionType = evalType(node.getExpr());

		if (!Type.BOOL.equals(conditionType)) {
			diag.error(ErrorCode.TYPE_MISMATCH, "If condition must be of type BOOLEAN, got " + conditionType, node.getLine(),
					node.getColumn());
		}

		check(node.getIfInstrs());
		check(node.getElseInstrs());
	}

	/** Checks increment operator for type compatibility */
	@TypeCheckFunction(ASTIncrementOp.class)
	protected void checkIncrement(ASTIncrementOp node) {
		Type expectedType = TypeCheckerUtils.getBinaryOpType(BinaryOp.ADD);
		Type actualType = evalType(node.getLValue());

		if (!expectedType.equals(actualType)) {
			diag.error(ErrorCode.TYPE_MISMATCH,
					"Increment operator can only be applied to type " + expectedType + ", got " + actualType, node.getLine(),
					node.getColumn());
		}
	}

	/** Checks a while loop condition type and its instructions */
	@TypeCheckFunction(ASTWhile.class)
	protected void checkWhile(ASTWhile node) {
		Type conditionType = evalType(node.getExpr());

		if (!Type.BOOL.equals(conditionType)) {
			diag.error(ErrorCode.TYPE_MISMATCH, "While condition must be boolean", node.getLine(), node.getColumn());
		}

		check(node.getInstrs());
	}

	/** Type-checks a write instruction */
	@TypeCheckFunction(ASTWrite.class)
	protected void checkWrite(ASTWrite node) {
		// do nothing
	}

	/** Checks a chain of instructions recursively */
	@TypeCheckFunction(ASTInstrsChain.class)
	protected void checkInstrsChain(ASTInstrsChain node) {
		check(node.getInstr());
		check(node.getInstrs());
	}

	/** Type-checks an empty instruction chain (nothing to check) */
	@TypeCheckFunction(ASTInstrsNil.class)
	protected void checkInstrsNil(ASTInstrsNil node) {
		// do nothing
	}

	/** Type-checks a simple variable declaration and its initializer */
	@TypeCheckFunction(ASTVarSimple.class)
	protected void checkVarSimple(ASTVarSimple node) {
		String name = node.getIdent().getName();
		Type declaredType = TypeCheckerUtils.getTypeMeth(node.getType());

		env.define(name, declaredType);

		ASTVExpr initializer = node.getVExpr();
		if (initializer == null) {
			return;
		}

		Type actualType = evalType(initializer.getExpr());
		if (!declaredType.equals(actualType)) {
			diag.error(ErrorCode.TYPE_MISMATCH,
					"Variable '" + name + "' assigned with wrong type: expected " + declaredType + ", got " + actualType,
					node.getLine(), node.getColumn());
		}
	}

	/** Type-checks an array variable declaration and its size expression */
	@TypeCheckFunction(ASTVarArray.class)
	protected void checkVarArray(ASTVarArray node) {
		String name = node.getIdent().getName();
		Type declaredType = TypeCheckerUtils.getTypeMeth(node.getType());

		env.define(name, declaredType);

		Type indexType = evalType(node.getExpr());
		if (!Type.INT.equals(indexType)) {
			diag.error(ErrorCode.TYPE_MISMATCH, "Array size must be integer", node.getLine(), node.getColumn());
		}
	}

	/**
	 * Type-checks a constant variable declaration and its initializer expression
	 */
	@TypeCheckFunction(ASTVarConst.class)
	protected void checkVarConst(ASTVarConst node) {
		String name = node.getIdent().getName();
		Type declaredType = TypeCheckerUtils.getType(node.getType());

		env.define(name, declaredType);

		ASTVExpr valueExpr = node.getVExpr();
		if (valueExpr == null) {
			return;
		}

		Type actualType = evalType(valueExpr.getExpr());
		if (!declaredType.equals(actualType)) {
			diag.error(ErrorCode.TYPE_MISMATCH,
					"Variable '" + name + "' assigned with wrong type: expected " + declaredType + ", got " + actualType,
					node.getLine(), node.getColumn());
		}
	}

	/** Type-checks a chain of variable declarations recursively */
	@TypeCheckFunction(ASTVarsChain.class)
	protected void checkVarsChain(ASTVarsChain node) {
		check(node.getVar());
		check(node.getVars());
	}

	/** Type-checks an empty variable declaration chain */
	@TypeCheckFunction(ASTVarsNil.class)
	protected void checkVarsNil(ASTVarsNil node) {
		// do nothing
	}

	/** Type-checks a return statement */
	@TypeCheckFunction(ASTReturn.class)
	protected void checkReturn(ASTReturn node) {
		// do nothing
	}

	/** Evaluates the type of a binary operation */
	@TypeEvalFunction(ASTBinaryOp.class)
	protected Type evalTypeBinary(ASTBinaryOp node) {
		Type expectedType = TypeCheckerUtils.getBinaryOpType(node.getOp());
		Type leftType = evalType(node.getLeft());
		Type rightType = evalType(node.getRight());

		if (!expectedType.equals(leftType) || !expectedType.equals(rightType)) {
			diag.error(ErrorCode.TYPE_MISMATCH, "Binary operation '" + node.getOp() + "' requires operands of type "
					+ expectedType + ", got left = " + leftType + ", right = " + rightType, node.getLine(), node.getColumn());
		}
		return expectedType;
	}

	/** Evaluates the type of a unary operation */
	@TypeEvalFunction(ASTUnaryOp.class)
	protected Type evalTypeUnary(ASTUnaryOp node) {
		Type expectedType = TypeCheckerUtils.getUnaryOpType(node.getOp());
		Type valueType = evalType(node.getExpr());

		if (!expectedType.equals(valueType)) {
			diag.error(ErrorCode.TYPE_MISMATCH,
					"Unary operation '" + node.getOp() + "' requires type " + expectedType + ", but got " + valueType,
					node.getLine(), node.getColumn());
		}
		return expectedType;
	}

	/** Evaluates the type of an array access expression */
	@TypeEvalFunction(ASTArrayAccess.class)
	protected Type evalTypeArrayAccess(ASTArrayAccess node) {
		Type indexType = evalType(node.getExpr());

		if (!Type.INT.equals(indexType)) {
			diag.error(ErrorCode.TYPE_MISMATCH, "Array index must be of type INT, got " + indexType, node.getLine(),
					node.getColumn());
		}
		return evalType(node.getIdent());
	}

	/** Evaluates the type of a method call instruction */
	@TypeEvalFunction(ASTMethodCallInstr.class)
	protected Type evalTypeMethodCallInstr(ASTMethodCallInstr node) {
		String methodBaseName = node.getIdent().getName();

		List<Type> argTypes = new ArrayList<>();
		for (ASTExprs exprs = node.getExprs(); exprs instanceof ASTExprsChain chain; exprs = chain.getExprs()) {
			argTypes.add(evalType(chain.getExpr()));
		}

		String methodKey = TypeCheckerUtils.methodKey(methodBaseName, argTypes);

		Type resolvedType = env.get(methodKey);
		if (resolvedType != null) {
			return resolvedType;
		}

		if (env.hasMethod(methodBaseName)) {
			diag.error(ErrorCode.ARGUMENT_MISMATCH, "Argument mismatch for method '" + methodBaseName + "'", node.getLine(),
					node.getColumn());
		} else {
			diag.error(ErrorCode.UNKNOWN_FUNCTION, "Unknown method '" + node.getIdent().getName() + "'", node.getLine(),
					node.getColumn());
		}
		return null;
	}

	/** Evaluates the type of a method call expression */
	@TypeEvalFunction(ASTMethodCallExpr.class)
	protected Type evalTypeMethodCallExpr(ASTMethodCallExpr node) {
		Type type = evalType(new ASTMethodCallInstr(node.getIdent(), node.getExprs(), node.getLine(), node.getColumn()));
		if (Type.VOID.equals(type)) {
			diag.error(ErrorCode.TYPE_MISMATCH, "Method call does not return a value", node.getLine(), node.getColumn());
		}
		return type;
	}

	/** Evaluates the type of a return statement */
	@TypeEvalFunction(ASTReturn.class)
	protected Type evalTypeReturn(ASTReturn node) {
		return evalType(node.getExpr());
	}

	/** Evaluates the type of a length expression */
	@TypeEvalFunction(ASTLength.class)
	protected Type evalTypeLength(ASTLength node) {
		return Type.INT;
	}

	/** Evaluates the type of an integer literal */
	@TypeEvalFunction(ASTIntLiteral.class)
	protected Type evalTypeIntLiteral(ASTIntLiteral node) {
		return Type.INT;
	}

	/** Evaluates the type of a boolean literal */
	@TypeEvalFunction(ASTBoolLiteral.class)
	protected Type evalTypeBoolLiteral(ASTBoolLiteral node) {
		return Type.BOOL;
	}

	/** Evaluates the type of a string literal */
	@TypeEvalFunction(ASTStringLiteral.class)
	protected Type evalTypeStringLiteral(ASTStringLiteral node) {
		return Type.STRING;
	}

	/** Evaluates the type of an identifier */
	@TypeEvalFunction(ASTIdent.class)
	protected Type evalTypeIdent(ASTIdent node) {
		Type type = env.get(node.getName());
		if (type == null) {
			diag.error(ErrorCode.UNKNOWN_VARIABLE, "Unknow variable '" + node.getName() + "'", node.getLine(),
					node.getColumn());
		}
		return type;
	}

	/**
	 * Searches for the first return statement in an instruction sequence.
	 *
	 * @param node the instruction sequence
	 * @return the first ASTReturn found or null if none
	 */
	private ASTReturn searchReturn(ASTInstrs node) {
		for (ASTInstrs curr = node; curr instanceof ASTInstrsChain chain; curr = chain.getInstrs()) {
			if (chain.getInstr() instanceof ASTReturn ret) {
				return ret;
			}
		}
		return null;
	}
}
