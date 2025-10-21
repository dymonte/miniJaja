package fr.umlp.m1info.projectvms.group2.interpreter.minijaja;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.ASTNode;

import java.awt.List;
import java.util.ArrayList;

import fr.umlp.m1info.projectvms.group2.exception.EvalRuntimeException;
import fr.umlp.m1info.projectvms.group2.exception.InterpretRuntimeException;
import fr.umlp.m1info.projectvms.group2.logger.InterpreterLogger;

import fr.umlp.m1info.projectvms.group2.memory.Memory;
import fr.umlp.m1info.projectvms.group2.memory.symtable.Type;

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

		if (node instanceof ASTClass astClass) {
			interpretClass(astClass);
		} else if (node instanceof ASTMainMethod astMainMethod) {
			interpretMainMethod(astMainMethod);
		} else if (node instanceof ASTMethod astMethod) {
			interpretMethod(astMethod);
		} else if (node instanceof ASTDeclsChain astDeclsChain) {
			interpretDeclsChain(astDeclsChain);
		} else if (node instanceof ASTDeclsNil astDeclsNil) {
			interpretDeclsNil(astDeclsNil);
		} else if (node instanceof ASTIdent astIdent) {
			interpretIdent(astIdent);
		} else if (node instanceof ASTExprsChain astExprsChain) {
			interpretExprsChain(astExprsChain);
		} else if (node instanceof ASTExprsNil astExprsNil) {
			interpretExprsNil(astExprsNil);
		} else if (node instanceof ASTAssignmentOp astAssignment) {
			interpretAssignment(astAssignment);
		} else if (node instanceof ASTIf astIf) {
			interpretIf(astIf);
		} else if (node instanceof ASTIncrementOp astIncrement) {
			interpretIncrement(astIncrement);
		} else if (node instanceof ASTMethodCallInstr astMethodCallInstr) {
			interpretMethodCallInstr(astMethodCallInstr);
		} else if (node instanceof ASTReturn astReturn) {
			interpretReturn(astReturn);
		} else if (node instanceof ASTWhile astWhile) {
			interpretWhile(astWhile);
		} else if (node instanceof ASTWrite astWrite) {
			interpretWrite(astWrite);
		} else if (node instanceof ASTInstrsChain astInstrsChain) {
			interpretInstrsChain(astInstrsChain);
		} else if (node instanceof ASTInstrsNil astInstrsNil) {
			interpretInstrsNil(astInstrsNil);
		} else if (node instanceof ASTVarArray astVarArray) {
			interpretVarArray(astVarArray);
		} else if (node instanceof ASTVarConst astVarConst) {
			interpretVarConst(astVarConst);
		} else if (node instanceof ASTVarSimple astVarSimple) {
			interpretVarSimple(astVarSimple);
		} else if (node instanceof ASTVarsChain astVarsChain) {
			interpretVarsChain(astVarsChain);
		} else if (node instanceof ASTVarsNil astVarsNil) {
			interpretVarsNil(astVarsNil);
		} else {
			throw new InterpretRuntimeException(
					"Cannot interpret node: " + node.getClass().getSimpleName());
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

		if (node instanceof ASTArrayAccess astArray) {
			return evalArrayAccess(astArray);
		} else if (node instanceof ASTBinaryOp astBinaryOp) {
			return evalBinaryOp(astBinaryOp);
		} else if (node instanceof ASTBoolLiteral astBoolLiteral) {
			return evalBoolLiteral(astBoolLiteral);
		} else if (node instanceof ASTIdent astIdent) {
			return evalIdent(astIdent);
		} else if (node instanceof ASTIntLiteral astIntLiteral) {
			return evalIntLiteral(astIntLiteral);
		} else if (node instanceof ASTLength astLength) {
			return evalLength(astLength);
		} else if (node instanceof ASTMethodCallExpr astMethodCall) {
			return evalMethodCall(astMethodCall);
		} else if (node instanceof ASTStringLiteral astStringLiteral) {
			return evalStringLiteral(astStringLiteral);
		} else if (node instanceof ASTUnaryOp astUnaryOp) {
			return evalUnaryOp(astUnaryOp);
		} else {
			throw new EvalRuntimeException("Cannot evaluate node: " + node.getClass().getSimpleName());
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

		if (node instanceof ASTVarsChain astVarsChain) {
			freeVarsChain(astVarsChain);
		} else if (node instanceof ASTVarsNil astVarsNil) {
			freeVarsNil(astVarsNil);
		} else if (node instanceof ASTVar astVar) {
			freeVar(astVar);
		} else {
			throw new InterpretRuntimeException("Cannot free node: " + node.getClass().getSimpleName());
		}
	}

	/** Interprets a class declaration */
	protected void interpretClass(ASTClass node) {
		interpret(node.getDecls());
		interpret(node.getMainMethod());
	}

	/** Interprets the main method of a program */
	protected void interpretMainMethod(ASTMainMethod node) {
		interpret(node.getVars());
		interpret(node.getInstrs());
	}

	/** Interprets a method declaration */
	protected void interpretMethod(ASTMethod node) {
		String ident = (String) eval(node.getIdent());
		ASTParams params = node.getParams();
		ArrayList<Type> paramsTypes = MiniJajaInterpreterUtils.collectParamTypes(params);
		Type returnType = MiniJajaInterpreterUtils.getTypeFromTypeMeth(node.getType());

		memory.declMeth(ident, node, paramsTypes, returnType);
	}

	/** Interprets a chain of declaration */
	protected void interpretDeclsChain(ASTDeclsChain node) {
		interpret(node.getDecl());
		interpret(node.getDecls());
	}

	/** Interprets an empty declaration list */
	protected void interpretDeclsNil(ASTDeclsNil node) {
		return;// do nothing
	}

	/** Interprets an identifier as a standalone statement */
	protected void interpretIdent(ASTIdent node) {
		throw new InterpretRuntimeException("interpretIdent() not implemented");
	}

	/** Interprets a chain of expressions in a statement context */
	protected void interpretExprsChain(ASTExprsChain node) {
		throw new InterpretRuntimeException("interpretExprsChain() not implemented");
	}

	/** Interprets an empty expression chain */
	protected void interpretExprsNil(ASTExprsNil node) {
		throw new InterpretRuntimeException("interpretExprsNil() not implemented");
	}

	/** Interprets an assignement operation */
	protected void interpretAssignment(ASTAssignmentOp node) {
		var ref = MiniJajaInterpreterUtils.resolveLValue(memory, node.getLValue(), this);
		Object value;
		if (node.getExpr() instanceof ASTLValue astLValue) {
			var refValue = MiniJajaInterpreterUtils.resolveLValue(memory, astLValue, this);
			value = MiniJajaInterpreterUtils.getValMemory(memory, refValue);
		} else {
			value = eval(node.getExpr());
		}

		Object newValue = switch (node.getOp()) {
			case EQ -> value;
			case ADD -> MiniJajaInterpreterUtils.binaryOperation(BinaryOp.ADD, ref.currentValue(), value);
		};

		MiniJajaInterpreterUtils.updateValMemory(memory, ref, newValue);
	}

	/** Interprets an if-(else)-statement */
	protected void interpretIf(ASTIf node) {
		Object value = eval(node.getExpr());
		if (value instanceof Boolean) {
			if ((Boolean) value) {
				interpret(node.getIfInstrs());
			} else {
				interpret(node.getElseInstrs());
			}
		} else {
			throw new InterpretRuntimeException("Condition must evaluate to a boolean value");
		}
	}

	/** Interprets an increment operation */
	protected void interpretIncrement(ASTIncrementOp node) {
		var ref = MiniJajaInterpreterUtils.resolveLValue(memory, node.getLValue(), this);

		Object newValue = switch (node.getOp()) {
			case INC -> MiniJajaInterpreterUtils.binaryOperation(BinaryOp.ADD, ref.currentValue(), 1);
		};

		MiniJajaInterpreterUtils.updateValMemory(memory, ref, newValue);
	}

	/** Interprets a method call used as an instruction */
	protected void interpretMethodCallInstr(ASTMethodCallInstr node) {
		String methodName = (String) eval(node.getIdent());
		ASTExprs exprs = node.getExprs();

		ArrayList<Type> argumentTypes = MiniJajaInterpreterUtils.collectExprTypes(exprs, this);

		String methodKey = memory.getMethKey(methodName, argumentTypes);
		ASTMethod method = (ASTMethod) memory.getVal(methodKey);

		MiniJajaInterpreterUtils.bindParameters(memory, method.getParams(), node.getExprs(), this);
		try {
			interpret(method.getVars());
			interpret(method.getInstrs());
		} finally {
			MiniJajaInterpreterUtils.unbindParameters(memory, method.getParams());
			free(method.getVars());
		}
	}

	/** Interprets a return statement */
	protected void interpretReturn(ASTReturn node) {
		Object value = eval(node.getExpr());
		memory.updateVal(ASTClass.getClassVar().getName(), value);
	}

	/** Interprets a while loop */
	protected void interpretWhile(ASTWhile node) {
		Boolean value = (Boolean) eval(node.getExpr());
		if (Boolean.TRUE.equals(value)) {
			interpret(node.getInstrs());
			interpret(node);
		}
	}

	/** Interprets a write statement */
	protected void interpretWrite(ASTWrite node) {
		boolean isLn = node.getIsLn();
		Object value = node.getValue();

		if (value instanceof ASTLValue lvalue) {
			Object write = eval(lvalue);
			InterpreterLogger.getInstance().log(write + (isLn ? "\n" : ""),
					InterpreterLogger.MessageLevel.WRITE);
		} else if (value instanceof ASTStringLiteral stringLiteral) {
			String str = (String) eval(stringLiteral);
			InterpreterLogger.getInstance().log(str + (isLn ? "\n" : ""),
					InterpreterLogger.MessageLevel.WRITE);
		} else {
			throw new InterpretRuntimeException(
					"Invalid value type to write: " + value.getClass().getSimpleName());
		}
	}

	/** Interprets a chain of instructions */
	protected void interpretInstrsChain(ASTInstrsChain node) {
		interpret(node.getInstr());
		interpret(node.getInstrs());
	}

	/** Interprets an empty instruction list */
	protected void interpretInstrsNil(ASTInstrsNil node) {
		return; // do nothing
	}

	/** Interprets a array variable declaration */
	protected void interpretVarArray(ASTVarArray node) {
		throw new InterpretRuntimeException("interpretVarArray() not implemented");
	}

	/** Interprets a constant variable declaration */
	protected void interpretVarConst(ASTVarConst node) {
		throw new InterpretRuntimeException("interpretVarConst() not implemented");
	}

	/** Interprets a simple variable declaration */
	protected void interpretVarSimple(ASTVarSimple node) {
		throw new InterpretRuntimeException("interpretVarSimple() not implemented");
	}

	/** Interprets a chain of variable declarations */
	protected void interpretVarsChain(ASTVarsChain node) {
		throw new InterpretRuntimeException("interpretVarsChain() not implemented");
	}

	/** Interprets an empty variable declaration list */
	protected void interpretVarsNil(ASTVarsNil node) {
		throw new InterpretRuntimeException("interpretVarsNil() not implemented");
	}

	/** Evaluates an array element access expression */
	protected Object evalArrayAccess(ASTArrayAccess node) {
		return memory.getValArray((String) eval(node.getIdent()), (Integer) eval(node.getExpr()));
	}

	/** Evaluates a binary operation */
	protected Object evalBinaryOp(ASTBinaryOp node) {
		return MiniJajaInterpreterUtils.binaryOperation(node.getOp(), eval(node.getLeft()),
				eval(node.getRight()));
	}

	/** Evaluates a boolean literal */
	protected Object evalBoolLiteral(ASTBoolLiteral node) {
		return node.getValue();
	}

	/** Evaluates an identifier expression */
	protected Object evalIdent(ASTIdent node) {
		return node.getName();
	}

	/** Evaluates an integer literal */
	protected Object evalIntLiteral(ASTIntLiteral node) {
		return node.getValue();
	}

	/** Evaluates a length expression on arrays */
	protected Object evalLength(ASTLength node) {
		throw new EvalRuntimeException("evalLength() not implemented");
	}

	/** Evaluates a method call used as an expression */
	protected Object evalMethodCall(ASTMethodCallExpr node) {
		throw new EvalRuntimeException("evalMethodCall() not implemented");
	}

	/** Evaluates a string literal */
	protected Object evalStringLiteral(ASTStringLiteral node) {
		return node.getValue();
	}

	/** Evaluates a unary operation */
	protected Object evalUnaryOp(ASTUnaryOp node) {
		return MiniJajaInterpreterUtils.unaryOperation(node.getOp(), eval(node.getExpr()));
	}

	/** Free a variable for memory */
	protected void freeVarsChain(ASTVarsChain node) {
		free(node.getVar());
		free(node.getVars());
	}

	/** Free an empty variable */
	protected void freeVarsNil(ASTVarsNil node) {
		return; // Do nothing
	}

	/** Free an variable */
	protected void freeVar(ASTVar node) {
		String name = (String) eval(node.getIdent());
		memory.removeDecl(name);
	}
}
