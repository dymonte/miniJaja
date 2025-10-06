package fr.umlp.m1info.projectvms.group2.ast.minijaja.instr;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTIdent;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTStringLiteral;

/**
 * Represents a write instruction in the MiniJaja AST
 */
public class ASTWrite extends ASTInstr {
	private ASTIdent ident;
	private ASTStringLiteral literal;
	private boolean isLn;

	/**
	 * Constructs a write instruction printing a varaible
	 *
	 * @param ident the variable reference to write
	 * @param literal the string to write
	 * @param isLn true to append a newline (writeln), false for write
	 * @param line the source code line
	 * @param column the source code column
	 */
	private ASTWrite(ASTIdent ident, ASTStringLiteral literal, boolean isLn, int line, int column) {
		super(line, column);
		this.ident = ident;
		this.literal = literal;
		this.isLn = isLn;
	}

	/**
	 * Constructs a write instruction printing a variable
	 *
	 * @param ident the variable reference to write
	 * @param isLn true to append a newline (writeln), false for write
	 * @param line the source code line
	 * @param column the source code column
	 */
	public ASTWrite(ASTIdent ident, boolean isLn, int line, int column) {
		this(ident, null, isLn, line, column);
	}
	
	/**
	 * Constructs a write instruction printing a variable
	 *
	 * @param literal the string to write
	 * @param isLn true to append a newline (writeln), false for write
	 * @param line the source code line
	 * @param column the source code column
	 */
	public ASTWrite(ASTStringLiteral literal, boolean isLn, int line, int column) {
		this(null, literal, isLn, line, column);
	}

	/**
	 * Returns a string representation of this node
	 * 
	 * @param a formatted string
	 */
	public String toString() {
		String content = (ident != null ? ident.toString() : "\"" + literal + "\"");
		return (isLn ? "writeln(" : "write(") + content + ")";
	}
}
