package fr.umlp.m1info.projectvms.group2.ast.minijaja.decl;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTIdent;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.type.ASTTypeMeth;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instrs.ASTInstrs;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.vars.ASTVars;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.params.ASTParams;

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
