package fr.umlp.m1info.projectvms.group2.ast.minijaja.decl;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTIdent;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.type.ASTTypeMeth;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instrs.ASTInstrs;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.vars.ASTVars;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.params.ASTParams;

/**
 * Represents a method declaration node in the MiniJaja Abstract Syntax Tree
 */
public class ASTMethod extends ASTDecl {
	private ASTTypeMeth typeMeth;
	private ASTIdent ident;
	private ASTParams params;
	private ASTVars vars;
	private ASTInstrs instrs;

	/**
	 * Constructs a new ASTMethod node
	 *
	 * @param typeMeth the method type
	 * @param ident    the method identifier
	 * @param params   the method parameters
	 * @param vars     inside the method body, the variables declarations
	 * @param instrs   inside the method body, the instructions
	 * @param line     the line number of the class declaration
	 * @param column   the column number of the class declaration
	 * @throws NullPointerException if ident, params, vars or instrs is null
	 */
	public ASTMethod(ASTTypeMeth typeMeth, ASTIdent ident, ASTParams params, ASTVars vars, ASTInstrs instrs, int line,
			int column) {
		super(line, column);
		if (ident == null) {
			throw new NullPointerException("ident cannot be null");
		} else if (params == null) {
			throw new NullPointerException("params cannot be null");
		} else if (vars == null) {
			throw new NullPointerException("vars cannot be null");
		} else if (instrs == null) {
			throw new NullPointerException("instrs cannot be null");
		}
		this.typeMeth = typeMeth;
		this.ident = ident;
		this.params = params;
		this.vars = vars;
		this.instrs = instrs;
	}

	/**
	 * Returns the type of this method
	 *
	 * @return the ASTTypeMeth representing the method's return type
	 */
	public ASTTypeMeth getType() {
		return typeMeth;
	}

	/**
	 * Returns the identifier of this method
	 *
	 * @return the ASTIdent representing the method name
	 */
	public ASTIdent getIdent() {
		return ident;
	}

	/**
	 * Returns the list of parameters defined for this method
	 *
	 * @return the ASTParams node representing the method parameters
	 */
	public ASTParams getParams() {
		return params;
	}

	/**
	 * Returns the variable declarations defined within this method
	 *
	 * @return the ASTVars node representing the method's local variables
	 */
	public ASTVars getVars() {
		return vars;
	}

	/**
	 * Returns the instructions contained in the body of this method
	 *
	 * @return the ASTInstrs node representing the method body
	 */
	public ASTInstrs getInstrs() {
		return instrs;
	}

	/**
	 * Returns a string representation of the method in MiniJaja syntax
	 *
	 * @return the method as a formatted string
	 */
	@Override
	public String toString() {
		return "" + typeMeth + " " + ident + "(" + params + ") {\n" + vars + instrs + "}";
	}
}
