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

class TypeEnv {
	private final Map<String, Type> vars = new HashMap<>();

	public void define(String name, Type type) {
		vars.put(name, type);
	}

	public Type get(String name) {
		return vars.get(name);
	}

	public boolean isDefine(String name) {
		return vars.containsKey(name);
	}

	public boolean hasMethod(String name) {
		for (String key : vars.keySet()) {
			System.out.println(key);
			if (key.startsWith("-" + name)) {
				return true;
			}
		}
		return false;
	}
}

public class TypeChecker implements Checker {
	private final Diagnostic diag;
	private TypeEnv env;

	public TypeChecker(Diagnostic diag) {
		if (diag == null) {
			throw new NullPointerException("Diagnostic cannot be null");
		}
		this.diag = diag;
		env = new TypeEnv();
	}

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

	@TypeCheckFunction(ASTClass.class)
	protected void checkClass(ASTClass node) {
		check(node.getDecls());
		check(node.getMainMethod());
	}

	@TypeCheckFunction(ASTMainMethod.class)
	protected void checkMainMethod(ASTMainMethod node) {
		check(node.getVars());
		check(node.getInstrs());
	}

	// Add it only in env
	@TypeCheckFunction(ASTMethod.class)
	protected void checkMethod(ASTMethod node) {
		Type type = TypeCheckerUtils.getTypeMeth(node.getType());
		List<Type> params = new ArrayList<>();

		ASTParams curr = node.getParams();
		while (!(curr instanceof ASTParamsNil)) {
			ASTParamsChain chain = (ASTParamsChain) curr;
			params.add(TypeCheckerUtils.getType(chain.getParam().getType()));
			curr = chain.getParams();
		}

		String name = TypeCheckerUtils.methodKey(node.getIdent().getName(), params);
		if (env.isDefine(name)) {
			diag.error(ErrorCode.METHOD_ALREADY_EXISTS,
					"The method '" + node.getIdent().getName() + "' already exists with the same arguments", node.getLine(),
					node.getColumn());
		}
		env.define(name, type);
	}

	@TypeCheckFunction(ASTDecls.class)
	protected void checkDecls(ASTDecls node) {
		ASTDecls curr = node;
		List<ASTMethod> allMethods = new ArrayList<>();
		while (!(curr instanceof ASTDeclsNil)) {
			ASTDeclsChain chain = (ASTDeclsChain) curr;

			if (chain.getDecl() instanceof ASTMethod method) {
				// To check after all instrs of method
				allMethods.add(method);
			}
			check(chain.getDecl());
			curr = chain.getDecls();
		}

		// Check all instrs in each methods
		for (ASTMethod method : allMethods) {
			check(method.getInstrs());
			ASTReturn ret = searchReturn(method.getInstrs());
			Type methodReturnType = TypeCheckerUtils.getTypeMeth(method.getType());
			if (ret == null) {
				if (methodReturnType != Type.VOID) {
					diag.error(ErrorCode.MISSING_RETURN_STATEMENT, "This method must return a result of type " + methodReturnType,
							method.getLine(), method.getColumn());
				}
			} else {
				Type returnType = evalType(ret);
				if (methodReturnType != returnType) {
					diag.error(ErrorCode.RETURN_TYPE_MISMATCH, "Not same type of return", ret.getLine(), ret.getColumn());
				}
			}

		}
	}

	@TypeCheckFunction(ASTAssignmentOp.class)
	protected void checkAssignment(ASTAssignmentOp node) {
		Type left = evalType(node.getLValue());
		Type right = evalType(node.getExpr());

		// right must be integer (left verified after)
		if (node.getOp() == AssignmentOp.ADD && right != Type.INT) {
			diag.error(ErrorCode.TYPE_MISMATCH, "Assignment += must be an integer value", node.getLine(), node.getColumn());
		}

		if (left != right) {
			diag.error(ErrorCode.TYPE_MISMATCH, "Assignment type mismatch: left=" + left + ", right=" + right,
					node.getLine(), node.getColumn());
		}
	}

	@TypeCheckFunction(ASTIf.class)
	protected void checkIf(ASTIf node) {
		Type type = evalType(node.getExpr());
		if (type != Type.BOOL) {
			diag.error(ErrorCode.TYPE_MISMATCH, "If condition must be boolean", node.getLine(), node.getColumn());
		}

		check(node.getIfInstrs());
		check(node.getElseInstrs());
	}

	@TypeCheckFunction(ASTIncrementOp.class)
	protected void checkIncrement(ASTIncrementOp node) {
		Type addType = TypeCheckerUtils.getBinaryOpType(BinaryOp.ADD);
		Type type = evalType(node.getLValue());
		if (type != addType) {
			diag.error(ErrorCode.TYPE_MISMATCH, "Increment can be possible only with" + addType, node.getLine(),
					node.getColumn());
		}
	}

	@TypeCheckFunction(ASTWhile.class)
	protected void checkWhile(ASTWhile node) {
		Type condType = evalType(node.getExpr());
		if (condType != Type.BOOL) {
			diag.error(ErrorCode.TYPE_MISMATCH, "While condition must be boolean", node.getLine(), node.getColumn());
		}
		check(node.getInstrs());
	}

	@TypeCheckFunction(ASTWrite.class)
	protected void checkWrite(ASTWrite node) {
		// do nothing
	}

	@TypeCheckFunction(ASTInstrsChain.class)
	protected void checkInstrsChain(ASTInstrsChain node) {
		check(node.getInstr());
		check(node.getInstrs());
	}

	@TypeCheckFunction(ASTInstrsNil.class)
	protected void checkInstrsNil(ASTInstrsNil node) {
		// do nothing
	}

	@TypeCheckFunction(ASTVarSimple.class)
	protected void checkVarSimple(ASTVarSimple node) {
		String name = node.getIdent().getName();
		Type type = TypeCheckerUtils.getTypeMeth(node.getType());

		env.define(name, type);

		if (node.getVExpr() != null && evalType(node.getVExpr().getExpr()) != type) {
			diag.error(ErrorCode.TYPE_MISMATCH, "Variable '" + name + "' assigned with wrong type", node.getLine(),
					node.getColumn());
		}
	}

	@TypeCheckFunction(ASTVarArray.class)
	protected void checkVarArray(ASTVarArray node) {
		String name = node.getIdent().getName();
		Type type = TypeCheckerUtils.getTypeMeth(node.getType());

		env.define(name, type);

		if (evalType(node.getExpr()) != Type.INT) {
			diag.error(ErrorCode.TYPE_MISMATCH, "Array size must be integer", node.getLine(), node.getColumn());
		}
	}

	@TypeCheckFunction(ASTVarConst.class)
	protected void checkVarConst(ASTVarConst node) {
		String name = node.getIdent().getName();
		Type type = TypeCheckerUtils.getType(node.getType());

		env.define(name, type);

		if (node.getVExpr() != null && evalType(node.getVExpr().getExpr()) != type) {
			diag.error(ErrorCode.TYPE_MISMATCH, "Constant '" + name + "'assigned with wront type", node.getLine(),
					node.getColumn());
		}
	}

	@TypeCheckFunction(ASTVarsChain.class)
	protected void checkVarsChain(ASTVarsChain node) {
		check(node.getVar());
		check(node.getVars());
	}

	@TypeCheckFunction(ASTVarsNil.class)
	protected void checkVarsNil(ASTVarsNil node) {
		// do nothing
	}

	@TypeCheckFunction(ASTReturn.class)
	protected void checkReturn(ASTReturn node) {
		// do nothing
	}

	@TypeEvalFunction(ASTBinaryOp.class)
	protected Type evalTypeBinary(ASTBinaryOp node) {
		Type type = TypeCheckerUtils.getBinaryOpType(node.getOp());
		Type left = evalType(node.getLeft());
		Type right = evalType(node.getRight());

		if (left != type || right != type) {
			diag.error(ErrorCode.TYPE_MISMATCH, "Binary operation types do not match operator", node.getLine(),
					node.getColumn());
		}
		return type;
	}

	@TypeEvalFunction(ASTUnaryOp.class)
	protected Type evalTypeUnary(ASTUnaryOp node) {
		Type type = TypeCheckerUtils.getUnaryOpType(node.getOp());
		Type value = evalType(node.getExpr());

		if (value != type) {
			diag.error(ErrorCode.TYPE_MISMATCH, "Unary operation type mismatch", node.getLine(), node.getColumn());
		}
		return type;
	}

	@TypeEvalFunction(ASTArrayAccess.class)
	protected Type evalTypeArrayAccess(ASTArrayAccess node) {
		Type accessType = evalType(node.getExpr());
		if (accessType != Type.INT) {
			diag.error(ErrorCode.TYPE_MISMATCH, "Array index must be integer", node.getLine(), node.getColumn());
		}
		return evalType(node.getIdent());
	}

	@TypeEvalFunction(ASTMethodCallInstr.class)
	protected Type evalTypeMethodCallInstr(ASTMethodCallInstr node) {
		String name = node.getIdent().getName();
		List<Type> paramsCall = new ArrayList<>();

		ASTExprs curr = node.getExprs();
		while (!(curr instanceof ASTExprsNil)) {
			ASTExprsChain chain = (ASTExprsChain) curr;
			paramsCall.add(evalType(chain.getExpr()));
			curr = chain.getExprs();
		}

		String methodName = TypeCheckerUtils.methodKey(name, paramsCall);
		Type type = env.get(methodName);
		if (type == null) {
			if (env.hasMethod(name)) {
				diag.error(ErrorCode.ARGUMENT_MISMATCH, "Argument mismatch", node.getLine(), node.getColumn());
			} else {
				diag.error(ErrorCode.UNKNOWN_FUNCTION, "Unknown method '" + node.getIdent().getName() + "'", node.getLine(),
						node.getColumn());
			}
		}
		return type;
	}

	@TypeEvalFunction(ASTMethodCallExpr.class)
	protected Type evalTypeMethodCallExpr(ASTMethodCallExpr node) {
		Type type = evalType(new ASTMethodCallInstr(node.getIdent(), node.getExprs(), node.getLine(), node.getColumn()));
		if (type == Type.VOID) {
			diag.error(ErrorCode.TYPE_MISMATCH, "No value been returned", node.getLine(), node.getColumn());
		}
		return type;
	}

	@TypeEvalFunction(ASTReturn.class)
	protected Type evalTypeReturn(ASTReturn node) {
		return evalType(node.getExpr());
	}

	@TypeEvalFunction(ASTLength.class)
	protected Type evalTypeLength(ASTLength node) {
		return Type.INT;
	}

	@TypeEvalFunction(ASTIntLiteral.class)
	protected Type evalTypeIntLiteral(ASTIntLiteral node) {
		return Type.INT;
	}

	@TypeEvalFunction(ASTBoolLiteral.class)
	protected Type evalTypeBoolLiteral(ASTBoolLiteral node) {
		return Type.BOOL;
	}

	@TypeEvalFunction(ASTStringLiteral.class)
	protected Type evalTypeStringLiteral(ASTStringLiteral node) {
		return Type.STRING;
	}

	@TypeEvalFunction(ASTIdent.class)
	protected Type evalTypeIdent(ASTIdent node) {
		Type type = env.get(node.getName());
		if (type == null) {
			diag.error(ErrorCode.UNKNOWN_VARIABLE, "Unknow variable '" + node.getName() + "'", node.getLine(),
					node.getColumn());
		}
		return type;
	}

	private ASTReturn searchReturn(ASTInstrs node) {
		ASTInstrs curr = node;
		while (!(curr instanceof ASTInstrsNil)) {
			ASTInstrsChain chain = (ASTInstrsChain) curr;
			if (chain.getInstr() instanceof ASTReturn) {
				return (ASTReturn) chain.getInstr();
			}
			curr = chain.getInstrs();
		}
		return null;
	}
}
