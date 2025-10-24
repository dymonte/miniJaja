package fr.umlp.m1info.projectvms.group2.ast.minijaja.instr;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTLValue;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTStringLiteral;

/**
 * Represents a write instruction in the MiniJaja AST
 */
public class ASTWrite extends ASTInstr {
	private ASTLValue lvalue;
	private ASTStringLiteral literal;
	private boolean isLn;

	/**
	 * Constructs a write instruction printing a varaible
	 *
	 * @param lvalue  the variable reference to write
	 * @param literal the string to write
	 * @param isLn    true to append a newline (writeln), false for write
	 * @param line    the source code line
	 * @param column  the source code column
	 * @throws NullPointerException if lvalue and literral is null
	 */
	private ASTWrite(ASTLValue lvalue, ASTStringLiteral literal, boolean isLn, int line, int column) {
		super(line, column);
		if (lvalue == null && literal == null) {
			throw new NullPointerException("lvalue and literal cannot be null");
		}
		this.lvalue = lvalue;
		this.literal = literal;
		this.isLn = isLn;
	}

	/**
	 * Returns the value being printed, which is either a variable or a string
	 * literal
	 *
	 * @return the ASTLValue if printing a variable or ASTStringLiteral if printing
	 *         a literal
	 */
	public Object getValue() {
		if (lvalue == null) {
			return literal;
		}
		return lvalue;
	}

	/**
	 * Returns whether this instruction appends a newline
	 *
	 * @return true if writeln, false if write
	 */
	public boolean getIsLn() {
		return isLn;
	}

	/**
	 * Constructs a write instruction printing a variable
	 *
	 * @param lvalue the variable reference to write
	 * @param isLn   true to append a newline (writeln), false for write
	 * @param line   the source code line
	 * @param column the source code column
	 */
	public ASTWrite(ASTLValue lvalue, boolean isLn, int line, int column) {
		this(lvalue, null, isLn, line, column);
	}

	/**
	 * Constructs a write instruction printing a variable
	 *
	 * @param literal the string to write
	 * @param isLn    true to append a newline (writeln), false for write
	 * @param line    the source code line
	 * @param column  the source code column
	 */
	public ASTWrite(ASTStringLiteral literal, boolean isLn, int line, int column) {
		this(null, literal, isLn, line, column);
	}

	/**
	 * Returns a string representation of this node
	 * 
	 * @return a formatted string
	 */
	@Override
	public String toString() {
		String content = (lvalue != null ? lvalue.toString() : "\"" + literal + "\"");
		return (isLn ? "writeln(" : "write(") + content + ")";
	}
}
