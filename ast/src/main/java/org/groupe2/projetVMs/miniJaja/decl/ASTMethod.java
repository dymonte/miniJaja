package org.groupe2.projetVMs.miniJaja.ast.decl;

import org.groupe2.projetVMs.miniJaja.ast.expr.ASTIdent;
import org.groupe2.projetVMs.miniJaja.ast.type.ASTTypeMeth;
import org.groupe2.projetVMs.miniJaja.ast.instrs.ASTInstrs;
import org.groupe2.projetVMs.miniJaja.ast.vars.ASTVars;

/**
 * ASTMethod
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
	 */
	public ASTMethod(ASTTypeMeth typeMeth,
			ASTIdent ident,
			ASTParams params,
			ASTVars vars,
			ASTInstrs instrs,
			int line,
			int column) {
		super(line, column);
		this.typeMeth = typeMeth;
		this.ident = ident;
		this.params = params;
		this.vars = vars;
		this.instrs = instrs;
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
