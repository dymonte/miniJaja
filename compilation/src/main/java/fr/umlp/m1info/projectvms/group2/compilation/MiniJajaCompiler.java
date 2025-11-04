package fr.umlp.m1info.projectvms.group2.compilation;

import java.lang.reflect.Method;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.ASTNode;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decl.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decls.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.vars.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.var.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instrs.*;

/**
 * The compiler class is responsible for traversing and compiling an Abstract
 * Syntax Tree (AST) representing a MiniJaja program
 */
public class MiniJajaCompiler {

	/**
	 * Compiles an AST node
	 *
	 * @param node the AST node to compile
	 * @return the number of instructions generated
	 * @throws CompilationRuntimeException if the node type is unsupported or null
	 */
	public Integer compile(ASTNode node) {
		if (node == null) {
			throw new RuntimeException();
		}

		Method m = findAnnotatedMethod(CompilationFunction.class, node.getClass());
		if (m == null) {
			throw new RuntimeException();

		}

		try {
			return (Integer) m.invoke(this, node);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	/**
	 * Frees the compiled declaration for a given AST node
	 *
	 * @param node the AST node to free
	 * @return number of freed instructions
	 * @throws FreeCompilationRuntimeException if the node is unsupported or null
	 */
	public Integer free(ASTNode node) {
		if (node == null) {
			throw new RuntimeException();
		}

		Method m = findAnnotatedMethod(FreeCompilationFunction.class, node.getClass());
		if (m == null) {
			throw new RuntimeException();
		}

		try {
			return (Integer) m.invoke(this, node);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	/**
	 * Search for a method annotated with the given annotation that matches the node
	 * class or its parent type
	 */
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

	/** Compiles a class declaration. */
	@CompilationFunction(ASTClass.class)
	protected Integer compileClass(ASTClass node) {
		throw new RuntimeException("Not implemented");
	}

	/** Compiles the main method of a program. */
	@CompilationFunction(ASTMainMethod.class)
	protected Integer compileMainMethod(ASTMainMethod node) {
		throw new RuntimeException("Not implemented");
	}

	/** Compiles a method declaration. */
	@CompilationFunction(ASTMethod.class)
	protected Integer compileMethod(ASTMethod node) {
		throw new RuntimeException("Not implemented");
	}

	/** Compiles a chain of declarations. */
	@CompilationFunction(ASTDeclsChain.class)
	protected Integer compileDeclsChain(ASTDeclsChain node) {
		throw new RuntimeException("Not implemented");
	}

	/** Compiles an empty declaration list. */
	@CompilationFunction(ASTDeclsNil.class)
	protected Integer compileDeclsNil(ASTDeclsNil node) {
		throw new RuntimeException("Not implemented");
	}

	/** Compiles a chain of variable declarations. */
	@CompilationFunction(ASTVarsChain.class)
	protected Integer compileVarsChain(ASTVarsChain node) {
		throw new RuntimeException("Not implemented");
	}

	/** Compiles an empty variable declaration list. */
	@CompilationFunction(ASTVarsNil.class)
	protected Integer compileVarsNil(ASTVarsNil node) {
		throw new RuntimeException("Not implemented");
	}

	/** Compiles a simple variable declaration. */
	@CompilationFunction(ASTVarSimple.class)
	protected Integer compileVarSimple(ASTVarSimple node) {
		throw new RuntimeException("Not implemented");
	}

	/** Compiles an array variable declaration. */
	@CompilationFunction(ASTVarArray.class)
	protected Integer compileVarArray(ASTVarArray node) {
		throw new RuntimeException("Not implemented");
	}

	/** Compiles a constant variable declaration. */
	@CompilationFunction(ASTVarConst.class)
	protected Integer compileVarConst(ASTVarConst node) {
		throw new RuntimeException("Not implemented");
	}

	/** Compiles an if-else statement. */
	@CompilationFunction(ASTIf.class)
	protected Integer compileIf(ASTIf node) {
		throw new RuntimeException("Not implemented");
	}

	/** Compiles a while loop. */
	@CompilationFunction(ASTWhile.class)
	protected Integer compileWhile(ASTWhile node) {
		throw new RuntimeException("Not implemented");
	}

	/** Compiles an assignment operation. */
	@CompilationFunction(ASTAssignmentOp.class)
	protected Integer compileAssignment(ASTAssignmentOp node) {
		throw new RuntimeException("Not implemented");
	}

	/** Compiles an increment operation. */
	@CompilationFunction(ASTIncrementOp.class)
	protected Integer compileIncrement(ASTIncrementOp node) {
		throw new RuntimeException("Not implemented");
	}

	/** Compiles a return statement. */
	@CompilationFunction(ASTReturn.class)
	protected Integer compileReturn(ASTReturn node) {
		throw new RuntimeException("Not implemented");
	}

	/** Compiles a write instruction. */
	@CompilationFunction(ASTWrite.class)
	protected Integer compileWrite(ASTWrite node) {
		throw new RuntimeException("Not implemented");
	}

	/** Compiles a method call used as an instruction. */
	@CompilationFunction(ASTMethodCallInstr.class)
	protected Integer compileMethodCallInstr(ASTMethodCallInstr node) {
		throw new RuntimeException("Not implemented");
	}

	/** Compiles a chain of instructions. */
	@CompilationFunction(ASTInstrsChain.class)
	protected Integer compileInstrsChain(ASTInstrsChain node) {
		throw new RuntimeException("Not implemented");
	}

	/** Compiles an empty instruction list. */
	@CompilationFunction(ASTInstrsNil.class)
	protected Integer compileInstrsNil(ASTInstrsNil node) {
		throw new RuntimeException("Not implemented");
	}

	/** Compiles a binary operation expression. */
	@CompilationFunction(ASTBinaryOp.class)
	protected Integer compileBinaryOp(ASTBinaryOp node) {
		throw new RuntimeException("Not implemented");
	}

	/** Compiles a unary operation expression. */
	@CompilationFunction(ASTUnaryOp.class)
	protected Integer compileUnaryOp(ASTUnaryOp node) {
		throw new RuntimeException("Not implemented");
	}

	/** Compiles an array element access expression. */
	@CompilationFunction(ASTArrayAccess.class)
	protected Integer compileArrayAccess(ASTArrayAccess node) {
		throw new RuntimeException("Not implemented");
	}

	/** Compiles a boolean literal. */
	@CompilationFunction(ASTBoolLiteral.class)
	protected Integer compileBoolLiteral(ASTBoolLiteral node) {
		throw new RuntimeException("Not implemented");
	}

	/** Compiles an integer literal. */
	@CompilationFunction(ASTIntLiteral.class)
	protected Integer compileIntLiteral(ASTIntLiteral node) {
		throw new RuntimeException("Not implemented");
	}

	/** Compiles a string literal. */
	@CompilationFunction(ASTStringLiteral.class)
	protected Integer compileStringLiteral(ASTStringLiteral node) {
		throw new RuntimeException("Not implemented");
	}

	/** Compiles an identifier (variable) reference. */
	@CompilationFunction(ASTIdent.class)
	protected Integer compileIdent(ASTIdent node) {
		throw new RuntimeException("Not implemented");
	}

	/** Compiles a length expression on arrays. */
	@CompilationFunction(ASTLength.class)
	protected Integer compileLength(ASTLength node) {
		throw new RuntimeException("Not implemented");
	}

	/** Compiles a method call used as an expression. */
	@CompilationFunction(ASTMethodCallExpr.class)
	protected Integer compileMethodCallExpr(ASTMethodCallExpr node) {
		throw new RuntimeException("Not implemented");
	}

	/** Frees a chain of variable declarations. */
	@FreeCompilationFunction(ASTVarsChain.class)
	protected Integer freeVarsChain(ASTVarsChain node) {
		throw new RuntimeException("Not implemented");
	}

	/** Frees an empty variable declaration list. */
	@FreeCompilationFunction(ASTVarsNil.class)
	protected Integer freeVarsNil(ASTVarsNil node) {
		throw new RuntimeException("Not implemented");
	}

	/** Frees a single variable declaration. */
	@FreeCompilationFunction(ASTVar.class)
	protected Integer freeVar(ASTVar node) {
		throw new RuntimeException("Not implemented");
	}
}
