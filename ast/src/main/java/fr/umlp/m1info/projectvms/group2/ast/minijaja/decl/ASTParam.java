package fr.umlp.m1info.projectvms.group2.ast.minijaja.decl;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.params.ASTParams;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.type.ASTType;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTIdent;

/**
 * Represents a single parameter declaration in a MiniJaja method or function
 * definition
 */
public class ASTParam extends ASTParams {
	private ASTType type;
	private ASTIdent ident;

	/**
	 * Constructs a new ASTParam node
	 *
	 * @param type   the type of parameter
	 * @param ident  the identificator representing the parameter name
	 * @param line   the line of source code
	 * @param column the column of source code
	 * @throws NullPointerException if type or ident is null
	 */
	public ASTParam(ASTType type, ASTIdent ident, int line, int column) {
		super(line, column);
		if (type == null) {
			throw new NullPointerException("type cannot be null");
		} else if (ident == null) {
			throw new NullPointerException("ident cannot be null");
		}
		this.type = type;
		this.ident = ident;
	}

	@Override
	public String toString() {
		return "" + type + ident;
	}
}
