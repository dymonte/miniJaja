package fr.umlp.m1info.projectvms.group2.compilation;

import java.io.PrintStream;
import java.lang.reflect.Method;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.ASTNode;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decl.ASTClass;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decl.ASTMainMethod;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decl.ASTMethod;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decls.ASTDeclsChain;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decls.ASTDeclsNil;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTArrayAccess;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTBinaryOp;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTBoolLiteral;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTIdent;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTIntLiteral;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTLength;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTMethodCallExpr;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTStringLiteral;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTUnaryOp;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTVExpr;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.exprs.ASTExprsChain;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.exprs.ASTExprsNil;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.ASTAssignmentOp;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.ASTIf;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.ASTIncrementOp;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.ASTMethodCallInstr;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.ASTReturn;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.ASTWhile;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.ASTWrite;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instrs.ASTInstrsChain;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instrs.ASTInstrsNil;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.type.ASTVoidType;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.var.ASTVar;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.var.ASTVarArray;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.var.ASTVarConst;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.var.ASTVarSimple;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.vars.ASTVarsChain;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.vars.ASTVarsNil;
import fr.umlp.m1info.projectvms.group2.exception.CompilationRuntimeException;
import fr.umlp.m1info.projectvms.group2.exception.FreeCompilationRuntimeException;

/**
 * The compiler class is responsible for traversing and compiling an Abstract
 * Syntax Tree (AST) representing a MiniJaja program
 */
public class MiniJajaCompiler {

	private final StringBuilder sb;
	private int n;

	public MiniJajaCompiler(StringBuilder sb) {
		if (sb == null) {
			throw new CompilationRuntimeException("Output cannot be null");
		}
		this.sb = sb;
	}

	/**
	 * Compiles an AST node
	 *
	 * @param node the AST node to compile
	 * @return the number of instructions generated
	 * @throws CompilationRuntimeException if the node type is unsupported or null
	 */
	public Integer compile(ASTNode node) {
		if (node == null) {
			throw new CompilationRuntimeException("node cannot be null");
		}

		Method m = findAnnotatedMethod(CompilationFunction.class, node.getClass());
		if (m == null) {
			throw new CompilationRuntimeException("No @CompilationFunction found for " + node.getClass().getSimpleName());
		}

		try {
			return (Integer) m.invoke(this, node);
		} catch (Exception e) {
			throw new CompilationRuntimeException("Error interpreting node " + node.getClass().getSimpleName());
		}
	}

	/**
	 * Frees the compiled declaration for a given AST node
	 *
	 * @param node the AST node to free
	 * @return number of freed instructions
	 * @throws RuntimeException if the node is unsupported or null
	 */
	public Integer free(ASTNode node) {
		if (node == null) {
			throw new FreeCompilationRuntimeException("node cannot be null");
		}

		Method m = findAnnotatedMethod(FreeCompilationFunction.class, node.getClass());
		if (m == null) {
			throw new FreeCompilationRuntimeException(
					"No @FreeCompilationFunction found for " + node.getClass().getSimpleName());
		}

		try {
			return (Integer) m.invoke(this, node);
		} catch (Exception e) {
			throw new FreeCompilationRuntimeException("Error interpreting node " + node.getClass().getSimpleName());
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

	private Integer emit(String instr) {
		sb.append(instr).append("\n");
		n++;
		return 1;
	}

	private void patch(String fromString, String toString, Integer offsetStart) {
		int start = sb.indexOf(fromString, offsetStart);
		int end = start + fromString.length();
		sb.replace(start, end, toString);
	}

	/** Compiles a class declaration. */
	@CompilationFunction(ASTClass.class)
	protected Integer compileClass(ASTClass node) {
		int count = 0;
		count += emit("init");
		count += compile(node.getDecls());
		count += compile(node.getMainMethod());
		count += free(node.getDecls());
		count += emit("pop");
		count += emit("jcstop");
		return count;
	}

	/** Compiles the main method of a program. */
	@CompilationFunction(ASTMainMethod.class)
	protected Integer compileMainMethod(ASTMainMethod node) {
		int count = 0;
		count += compile(node.getVars());
		count += compile(node.getInstrs());
		count += emit("push(0)");
		count += free(node.getVars());
		return count;
	}

	/** Compiles a method declaration. */
	@CompilationFunction(ASTMethod.class)
	protected Integer compileMethod(ASTMethod node) {
		int start = n;
		int count = 0;
		count += emit("jcnil");
		count += emit("push(" + (start + 3) + ")");
		count += emit("new(" + node.getIdent().getName() + ", " + node.getType() + ", meth, 0)");

		int gotoIndex = sb.length();
		count += emit("goto(?)");
		count += compile(node.getParams());
		count += compile(node.getVars());
		count += compile(node.getInstrs());
		count += free(node.getVars());

		if (!(node.getType() instanceof ASTVoidType)) {
			count += emit("push(0)");
		}

		patch("goto(?)", "goto(" + n + ")", gotoIndex);
		count += emit("swap");
		count += emit("return");
		return count;
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
		return compile(node.getVar()) + compile(node.getVars());
	}

	/** Compiles an empty variable declaration list. */
	@CompilationFunction(ASTVarsNil.class)
	protected Integer compileVarsNil(ASTVarsNil node) {
		return 0;
	}

	/** Compiles a simple variable declaration. */
	@CompilationFunction(ASTVarSimple.class)
	protected Integer compileVarSimple(ASTVarSimple node) {
		int count = 0;
		count += node.getVExpr() != null ? compile(node.getVExpr()) : emit("push(0)");
		count += emit("new(" + node.getIdent().getName() + ", " + node.getType() + ", var, 0)");
		return count;
	}

	/** Compiles an array variable declaration. */
	@CompilationFunction(ASTVarArray.class)
	protected Integer compileVarArray(ASTVarArray node) {
		int count = 0;
		count += compile(node.getExpr());
		count += emit("newarray(" + node.getIdent().getName() + ", " + node.getType() + ")");
		return count;
	}

	/** Compiles a constant variable declaration. */
	@CompilationFunction(ASTVarConst.class)
	protected Integer compileVarConst(ASTVarConst node) {
		int count = 0;
		count += node.getVExpr() != null ? compile(node.getVExpr()) : emit("push(0)");
		count += emit("new(" + node.getIdent().getName() + ", " + node.getType() + ", cst, 0)");
		return count;
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
		int count = 0;
		count += compile(node.getLeft());
		count += compile(node.getRight());
		count += emit(MiniJajaCompilerUtils.binaryOpToString(node.getOp().toString()));
		return count;
	}

	/** Compiles a unary operation expression. */
	@CompilationFunction(ASTUnaryOp.class)
	protected Integer compileUnaryOp(ASTUnaryOp node) {
		int count = 0;
		count += compile(node.getExpr());
		count += emit(MiniJajaCompilerUtils.unaryOpToString(node.getOp().toString()));
		return count;
	}

	/** Compiles an array element access expression. */
	@CompilationFunction(ASTArrayAccess.class)
	protected Integer compileArrayAccess(ASTArrayAccess node) {
		int count = 0;
		count += compile(node.getExpr());
		count += emit("aload(" + node.getIdent().getName() + ")");
		return count;
	}

	/** Compiles a boolean literal. */
	@CompilationFunction(ASTBoolLiteral.class)
	protected Integer compileBoolLiteral(ASTBoolLiteral node) {
		return emit("push(" + node.getValue() + ")");
	}

	/** Compiles an integer literal. */
	@CompilationFunction(ASTIntLiteral.class)
	protected Integer compileIntLiteral(ASTIntLiteral node) {
		return emit("push(" + node.getValue() + ")");
	}

	/** Compiles a string literal. */
	@CompilationFunction(ASTStringLiteral.class)
	protected Integer compileStringLiteral(ASTStringLiteral node) {
		return emit("push(" + node.getValue() + ")");
	}

	/** Compiles an identifier (variable) reference. */
	@CompilationFunction(ASTIdent.class)
	protected Integer compileIdent(ASTIdent node) {
		return emit("load(" + node.getName() + ")");
	}

	/** Compiles a chain of expressions. */
	@CompilationFunction(ASTExprsChain.class)
	protected Integer compileExprsChain(ASTExprsChain node) {
		int count = 0;
		count += compile(node.getExpr());
		count += compile(node.getExprs());
		return count;
	}

	/** Compiles an empty expression list. */
	@CompilationFunction(ASTExprsNil.class)
	protected Integer compileExprsNil(ASTExprsNil node) {
		return 0;
	}

	/** Compiles an expression. */
	@CompilationFunction(ASTVExpr.class)
	protected Integer compileVExpr(ASTVExpr node) {
		return compile(node.getExpr());
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
		return free(node.getVars()) + free(node.getVar());
	}

	/** Frees an empty variable declaration list. */
	@FreeCompilationFunction(ASTVarsNil.class)
	protected Integer freeVarsNil(ASTVarsNil node) {
		return 0;
	}

	/** Frees a single variable declaration. */
	@FreeCompilationFunction(ASTVar.class)
	protected Integer freeVar(ASTVar node) {
		return emit("pop");
	}
}
