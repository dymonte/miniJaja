package fr.umlp.m1info.projectvms.group2.compilation;

import java.io.PrintStream;
import java.lang.reflect.Method;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.ASTNode;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decl.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decls.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.exprs.ASTExprsChain;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.exprs.ASTExprsNil;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.vars.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.var.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instrs.*;

import fr.umlp.m1info.projectvms.group2.exception.CompilationRuntimeException;
import fr.umlp.m1info.projectvms.group2.exception.FreeCompilationRuntimeException;

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
