package fr.umlp.m1info.projectvms.group2.miniJaja.ast.expr;

public class ASTBoolLiteral extends ASTLiteral {
	private boolean value;

	public ASTBoolLiteral(boolean value, int line, int column) {
		super(line, column);
		this.value = value;
	}

	@Override
	public String toString() {
		return (value ? "true" : "false");
	}
}
