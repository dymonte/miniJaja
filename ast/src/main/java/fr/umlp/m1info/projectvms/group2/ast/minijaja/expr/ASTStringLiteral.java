package fr.umlp.m1info.projectvms.group2.ast.minijaja.expr;

public class ASTStringLiteral extends ASTLiteral {
	private String value;

	public ASTStringLiteral(String value, int line, int column) {
		super(line, column);
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
