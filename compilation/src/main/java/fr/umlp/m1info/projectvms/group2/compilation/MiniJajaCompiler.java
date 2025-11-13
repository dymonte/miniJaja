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
import fr.umlp.m1info.projectvms.group2.ast.minijaja.var.ASTVar;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.var.ASTVarArray;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.var.ASTVarConst;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.var.ASTVarSimple;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.vars.ASTVarsChain;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.vars.ASTVarsNil;
import fr.umlp.m1info.projectvms.group2.exception.CompilationRuntimeException;

/**
 * The compiler class is responsible for traversing and compiling an Abstract
 * Syntax Tree (AST) representing a MiniJaja program
 */
public class MiniJajaCompiler {

	private PrintStream out;

	public MiniJajaCompiler(PrintStream out) {
		if (out == null) {
			throw new CompilationRuntimeException("Output cannot be null");
		}
		this.out = out;
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
	 * @throws RuntimeException if the node is unsupported or null
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
        int count = 0;
        out.println("init");
        count++;
        int ndss = compile(node.getDecls());
        count += ndss;
        int nmma = compile(node.getMainMethod());
        count += nmma;
        int nrdss = free(node.getDecls());
        count += nrdss;
        out.println("pop");
        out.println("jcstop");
        count += 2;
        return count;
    }

	/** Compiles the main method of a program. */
	@CompilationFunction(ASTMainMethod.class)
	protected Integer compileMainMethod(ASTMainMethod node) {
        int count = 0;
        int ndvs = compile(node.getVars());
        count += ndvs;
        int niss = compile(node.getInstrs());
        count += niss;
        out.println("push(0)");
        count++;
        int nrdvs = free(node.getVars());
        count += nrdvs;
        return count;
    }

	/** Compiles a method declaration. */
	@CompilationFunction(ASTMethod.class)
	protected Integer compileMethod(ASTMethod node) {throw new RuntimeException("Not implemented");}

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
		int vexpr = compile(node.getVExpr());
		String i = node.getIdent().getName();
		String t = node.getType().toString();
		out.println("new(" + i + ", " + t + ", var, 0)");
		return vexpr + 1;
	}

	/** Compiles an array variable declaration. */
	@CompilationFunction(ASTVarArray.class)
	protected Integer compileVarArray(ASTVarArray node) {
		int vexpr = compile(node.getExpr());
		String i = node.getIdent().getName();
		String t = node.getType().toString();
		out.println("new(" + i + ", " + t + ", cst, 0)");
		return vexpr + 1;
	}

	/** Compiles a constant variable declaration. */
	@CompilationFunction(ASTVarConst.class)
	protected Integer compileVarConst(ASTVarConst node) {
		int vexpr = compile(node.getVExpr());
		String i = node.getIdent().getName();
		String t = node.getType().toString();
		out.println("newarray(" + i + ", " + t + ")");
		return vexpr + 1;
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
		int nbInstr = compile(node.getLeft());
        nbInstr += compile(node.getRight());
        switch (node.getOp().toString()) {
            case "+" -> out.println("add");
            case "-" -> out.println("sub");
            case "*" -> out.println("mul");
            case "/" -> out.println("div");
            case "&&" -> out.println("and");
            case "||" -> out.println("or");
            case "==" -> out.println("cmp");
            case ">" -> out.println("sup");
            default -> throw new CompilationRuntimeException("Operation not supported");
        }
        return nbInstr + 1;
	}

	/** Compiles a unary operation expression. */
	@CompilationFunction(ASTUnaryOp.class)
	protected Integer compileUnaryOp(ASTUnaryOp node) {
        int nbInstr = compile(node.getExpr());
        switch (node.getOp().toString()) {
            case "-" -> out.println("neg");
            case "!" -> out.println("not");
            default -> throw new CompilationRuntimeException("Operation not supported");
        }
        return nbInstr + 1;
	}

	/** Compiles an array element access expression. */
	@CompilationFunction(ASTArrayAccess.class)
	protected Integer compileArrayAccess(ASTArrayAccess node) {
		int nbInstr = compile(node.getExpr());
        out.println("aload(" + node.getIdent().getName() + ")");
        return nbInstr + 1;
	}

	/** Compiles a boolean literal. */
	@CompilationFunction(ASTBoolLiteral.class)
	protected Integer compileBoolLiteral(ASTBoolLiteral node) {
        if(node.getValue()) {
            out.println("push(true)");
        } else {
            out.println("push(false)");
        }
		return 1;
	}

	/** Compiles an integer literal. */
	@CompilationFunction(ASTIntLiteral.class)
	protected Integer compileIntLiteral(ASTIntLiteral node) {
        out.println("push(" + node.getValue() + ")");
		return 1;
	}

	/** Compiles a string literal. */
	@CompilationFunction(ASTStringLiteral.class)
	protected Integer compileStringLiteral(ASTStringLiteral node) {
		out.println("push(" + node.getValue() + ")");
        return 1;
	}

	/** Compiles an identifier (variable) reference. */
	@CompilationFunction(ASTIdent.class)
	protected Integer compileIdent(ASTIdent node) {
		out.println("load(" + node.getName() + ")");
        return 1;
	}

    /** Compiles a chain of expressions. */
    @CompilationFunction(ASTExprsChain.class)
    protected Integer compileExprsChain(ASTExprsChain node) {
        int nbInstr = compile(node.getExpr());
        nbInstr += compile(node.getExprs());
        return nbInstr;
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
		out.println("pop");
		return 1;
	}
}
