package fr.umlp.m1info.projectvms.group2.compilation;

import java.lang.reflect.Method;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.ASTNode;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decl.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decls.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.exprs.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instrs.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.type.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.params.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.var.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.vars.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.cases.*;
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
			throw new CompilationRuntimeException(
					"No @CompilationFunction found for " + node.getClass().getSimpleName());
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
	 * Searches the class hierarchy of this instance for a method annotated with the
	 * specified annotation type whose value() attribute designates a type
	 * that is assignable from the provided nodeClass.
	 * 
	 * @param annotationClass the annotation type to search for. The annotation is
	 *                        expected to have a Class<?> value() method.
	 * @param nodeClass       the class that the annotation's value() type
	 *                        must be assignable from
	 * @return the first method in the class hierarchy whose annotation matches the
	 *         provided node class; or null if no such method is found
	 */
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
		int count = 0;
		count += emit("push(" + (n + 4) + ")");
		count += emit("new(" + node.getIdent().getName() + ", " + node.getType() + ", meth, 0)");

		int gotoIndex = sb.length();
		count += emit("goto(?)");
		count += compile(node.getParams());
		count += compile(node.getVars());
		count += compile(node.getInstrs());
		count += free(node.getVars());

		if (node.getType() instanceof ASTVoidType) {
			count += emit("push(0)");
		}

		patch("goto(?)", "goto(" + (n + 3) + ")", gotoIndex);
		count += emit("swap");
		count += emit("return");
		return count;
	}

	/** Compiles parameters. */
	@CompilationFunction(ASTParams.class)
	protected Integer compileParams(ASTParams node) {
		int k = MiniJajaCompilerUtils.countParams(node);
		return node instanceof ASTParamsNil ? compileParamsNil((ASTParamsNil) node) : compileParams(node, k);
	}

	/** Compiles parameters. */
	protected Integer compileParams(ASTParams node, int k) {
		ASTParamsChain chain = (ASTParamsChain) node;
		int nbInstr = compileParam(chain.getParam(), k);
		if (chain.getParams() instanceof ASTParamsNil paramNil) {
			nbInstr += compileParamsNil(paramNil);
		} else {
			nbInstr += compileParams(chain.getParams(), k - 1);
		}
		return nbInstr;
	}

	/** Compiles a parameter. */
	@CompilationFunction(ASTParam.class)
	protected Integer compileParam(ASTParam node, int k) {
		emit("new(" + node.getIdent().getName() + "," + node.getType().toString() + ",var," + k + ")");
		return 1;
	}

	/** Compiles an empty parameters list. */
	@CompilationFunction(ASTParamsNil.class)
	protected Integer compileParamsNil(ASTParamsNil node) {
		return 0;
	}

	/** Compiles a chain of declarations. */
	@CompilationFunction(ASTDeclsChain.class)
	protected Integer compileDeclsChain(ASTDeclsChain node) {
		return compile(node.getDecl()) + compile(node.getDecls());
	}

	/** Compiles an empty declaration list. */
	@CompilationFunction(ASTDeclsNil.class)
	protected Integer compileDeclsNil(ASTDeclsNil node) {
		// do nothing
		return 0;
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
		int nbInstrs = compile(node.getExpr());
		int offset = sb.length();
		nbInstrs += emit("if(TMP_IF)");
		nbInstrs += compile(node.getElseInstrs());
		patch("TMP_IF", "" + (n + 2), offset);
		offset = sb.length();
		nbInstrs += emit("goto(TMP_GOTO)");
		nbInstrs += compile(node.getIfInstrs());
		patch("TMP_GOTO", "" + (n + 1), offset);
		return nbInstrs;
	}

	/** Compiles a while loop. */
	@CompilationFunction(ASTWhile.class)
	protected Integer compileWhile(ASTWhile node) {
		int offset = sb.length();
		int nStartWhile = n;
		int nbInstrs = compile(node.getExpr());
		nbInstrs += emit("not");
		nbInstrs += emit("if(TMP_IF)");
		nbInstrs += compile(node.getInstrs());
		nbInstrs += emit("goto(" + (nStartWhile + 1) + ")");
		patch("TMP_IF", "" + (n + 1), offset);
		return nbInstrs;
	}

	/** Compiles a for loop. */
	@CompilationFunction(ASTFor.class)
	protected Integer compileFor(ASTFor node) {
		int nb = 0;
		if (node.getVar() != null) {
			nb += compile(node.getVar());
		}
		int offset = sb.length();
		int nStartFor = n;

		ASTExpr expr = node.getExpr();
		if (expr == null) {
			expr = new ASTBoolLiteral(true, node.getLine(), node.getColumn());
		}
		nb += compile(expr);
		nb += emit("not");
		nb += emit("if(TMP_IF_FOR)");
		nb += compile(node.getInstrs());
		if (node.getInc() != null) {
			nb += compile(node.getInc());
		}
		nb += emit("goto(" + (nStartFor + 1) + ")");
		patch("TMP_IF_FOR", "" + (n + 1), offset);
		if (node.getVar() != null) {
			nb += free(node.getVar());
		}
		return nb;
	}

	/** Compiles an assignment operation. */
	@CompilationFunction(ASTAssignmentOp.class)
	protected Integer compileAssignment(ASTAssignmentOp node) {
		int nbInstrs = 0;

		String op = switch (node.getOp()) {
			case EQ -> "store";
			case ADD -> "inc";
		};

		ASTLValue lvalue = node.getLValue();
		if (lvalue instanceof ASTIdent ident) {
			nbInstrs += compile(node.getExpr());
			nbInstrs += emit(op + "(" + ident.getName() + ")");
		} else if (lvalue instanceof ASTArrayAccess arrayAccess) {
			nbInstrs += compile(arrayAccess.getExpr());
			nbInstrs += compile(node.getExpr());
			nbInstrs += emit("a" + op + "(" + arrayAccess.getIdent().getName() + ")");
		} else {
			throw new CompilationRuntimeException("Unknown lvalue type: " + lvalue);
		}

		return nbInstrs;

	}

	/** Compiles an increment operation. */
	@CompilationFunction(ASTIncrementOp.class)
	protected Integer compileIncrement(ASTIncrementOp node) {
		int toPush = switch (node.getOp()) {
			case INC -> 1;
		};
		int nbInstrs = 0;

		ASTLValue lvalue = node.getLValue();
		if (lvalue instanceof ASTIdent ident) {
			nbInstrs += emit("push(" + toPush + ")");
			nbInstrs += emit("inc(" + ident.getName() + ")");
		} else if (lvalue instanceof ASTArrayAccess arrayAccess) {
			nbInstrs += compile(arrayAccess.getExpr());
			nbInstrs += emit("push(" + toPush + ")");
			nbInstrs += emit("ainc(" + arrayAccess.getIdent().getName() + ")");
		} else {
			throw new CompilationRuntimeException("Unknown lvalue type: " + lvalue);
		}
		return nbInstrs;
	}

	/** Compiles a return statement. */
	@CompilationFunction(ASTReturn.class)
	protected Integer compileReturn(ASTReturn node) {
		return compile(node.getExpr());
	}

	/** Compiles a write instruction. */
	@CompilationFunction(ASTWrite.class)
	protected Integer compileWrite(ASTWrite node) {
		int nbInstrs = compile((ASTNode) node.getValue());
		String op = node.getIsLn() ? "writeln" : "write";
		nbInstrs += emit(op);
		return nbInstrs;
	}

	/** Compiles a bonus instruction. */
	@CompilationFunction(ASTBonusInstr.class)
	protected Integer compileBonus(ASTBonusInstr node) {
		return compile(node.getExpr()) + emit(node.getOp().toString());
	}

	/** Compiles a bonus expression. */
	@CompilationFunction(ASTBonusExpr.class)
	protected Integer compileBonus(ASTBonusExpr node) {
		return compile(node.getExpr1()) + compile(node.getExpr2()) + emit(node.getOp().toString());
	}

	/** Compiles a method call used as an instruction. */
	@CompilationFunction(ASTMethodCallInstr.class)
	protected Integer compileMethodCallInstr(ASTMethodCallInstr node) {
		int nbInstrs = compile(node.getExprs());
		nbInstrs += emit("invoke(" + node.getIdent().getName() + ")");
		nbInstrs += free(node.getExprs());
		nbInstrs += emit("pop");
		return nbInstrs;
	}

	/** Compiles a chain of instructions. */
	@CompilationFunction(ASTInstrsChain.class)
	protected Integer compileInstrsChain(ASTInstrsChain node) {
		return compile(node.getInstr()) + compile(node.getInstrs());
	}

	/** Compiles an empty instruction list. */
	@CompilationFunction(ASTInstrsNil.class)
	protected Integer compileInstrsNil(ASTInstrsNil node) {
		// do nothing
		return 0;
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

	/** Compiles a ternary operator expression */
	@CompilationFunction(ASTTernaryOp.class)
	protected Integer compileTernaryOp(ASTTernaryOp node) {
		int nbInstrs = compile(node.getCondition());
		int offset = sb.length();
		nbInstrs += emit("if(TMP_IF_TERNARY)");
		nbInstrs += compile(node.getSecondCase());
		patch("TMP_IF_TERNARY", "" + (n + 2), offset);
		offset = sb.length();
		nbInstrs += emit("goto(TMP_GOTO)");
		nbInstrs += compile(node.getFirstCase());
		patch("TMP_GOTO", "" + (n + 1), offset);
		return nbInstrs;
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
		return emit("push(\"" + node.getValue() + "\")");
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
		int nbInstrs = 0;
		nbInstrs += emit("load(" + node.getIdent().getName() + ")");
		nbInstrs += emit("length");
		return nbInstrs;
	}

	/** Compiles a method call used as an expression. */
	@CompilationFunction(ASTMethodCallExpr.class)
	protected Integer compileMethodCallExpr(ASTMethodCallExpr node) {
		int nbInstrs = compile(node.getExprs());
		nbInstrs += emit("invoke(" + node.getIdent().getName() + ")");
		nbInstrs += free(node.getExprs());
		return nbInstrs;
	}

	/** Compiles a switch */
	@CompilationFunction(ASTSwitch.class)
	protected Integer compileSwitch(ASTSwitch node) {
		int nb = compile(node.getExpr());
		nb += compile(node.getCases());
		nb += emit("pop");
		return nb;
	}

	/** Compiles cases chain */
	@CompilationFunction(ASTCasesChain.class)
	protected Integer compileCasesChain(ASTCasesChain node) {
		ASTCaseGroup caseGroup = node.getCase();
		ASTCases cases = node.getCases();
		int offset = sb.length();
		int nb = 0;
		if (caseGroup instanceof ASTCase cs) {
			nb += emit("jdup");
			nb += compile(cs.getExpr());
			nb += emit("cmp");
			nb += emit("not");
			nb += emit("if(TMP_IF_CASE)");
			nb += compile(cs.getInstrs());
		} else if (caseGroup instanceof ASTDefault def) {
			nb += compile(def.getInstrs());
		}
		nb += emit("goto(TMP_GOTO_CASE)");
		if (caseGroup instanceof ASTCase) {
			patch("TMP_IF_CASE", "" + (n + 1), offset);
		}
		nb += compile(cases);

		patch("TMP_GOTO_CASE", "" + (n + 1), offset);
		return nb;
	}

	@CompilationFunction(ASTCasesNil.class)
	protected Integer compileCasesNil(ASTCasesNil node) {
		return 0;
	}

	/** Frees a null declaration. */
	@FreeCompilationFunction(ASTDeclsNil.class)
	protected Integer freeDeclsNil(ASTDeclsNil node) {
		return 0;
	}

	/** Frees a chain of declarations. */
	@FreeCompilationFunction(ASTDeclsChain.class)
	protected Integer freeDeclsChain(ASTDeclsChain node) {
		return free(node.getDecl()) + free(node.getDecls());
	}

	/** Frees a chain of expression declarations. */
	@FreeCompilationFunction(ASTExprsChain.class)
	protected Integer freeExprsChain(ASTExprsChain node) {
		return emit("swap") + emit("pop") + free(node.getExprs());
	}

	/** Frees a null expression */
	@FreeCompilationFunction(ASTExprsNil.class)
	protected Integer freeExprsNil(ASTExprsNil node) {
		return 0;
	}

	/** Frees a V expression */
	@FreeCompilationFunction(ASTVExpr.class)
	protected Integer freeVExpr(ASTVExpr node) {
		return emit("swap") + emit("pop");
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
		return emit("swap") + emit("pop");
	}

	/** Frees a method declaration. */
	@FreeCompilationFunction(ASTMethod.class)
	protected Integer freeMethod(ASTMethod node) {
		return emit("swap") + emit("pop");
	}
}
